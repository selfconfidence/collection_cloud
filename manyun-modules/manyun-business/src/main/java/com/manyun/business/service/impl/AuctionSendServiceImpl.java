package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.AuctionSendMapper;
import com.manyun.business.mapper.CollectionInfoMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionSendStatus;
import com.manyun.common.core.enums.DelayLevelEnum;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.mq.producers.deliver.DeliverProducer;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import com.manyun.common.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;


/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
@Slf4j
public class AuctionSendServiceImpl extends ServiceImpl<AuctionSendMapper, AuctionSend> implements IAuctionSendService {


    @Autowired
    private IMediaService mediaService;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IBoxService boxService;

    @Autowired
    private CollectionInfoMapper collectionInfoMapper;

    @Autowired
    private IBoxCollectionService boxCollectionService;

    @Autowired
    private ObjectFactory<IAuctionPriceService> auctionPriceServiceObjectFactory;

    @Autowired
    private ObjectFactory<IAuctionOrderService> auctionOrderServiceObjectFactory;

    @Autowired
    private RemoteBuiUserService userService;

    @Autowired
    private DeliverProducer deliverProducer;

    /**
     * ?????????????????????
     * @return
     */
    @Override
    public R<BigDecimal> auctionSendConfig() {
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class));
    }

    @Override
    public R<BigDecimal> auctionSendCommission() {
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class));
    }

    /**
     * ?????????????????????
     * @param auctionSendForm
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R auctionSend(AuctionSendForm auctionSendForm, String userId) {
        checkAll(auctionSendForm, userId);
        aspect(auctionSendForm, userId);
        return R.ok();
    }

    /**
     * ?????????????????????
     * @param auctionSendForm
     * @param userId
     * @param cateId
     * @param goodsName
     */
    private void pushAuctionMarket(AuctionSendForm auctionSendForm, String userId, String cateId, String goodsName) {
        //????????????
        int concernedNum = 0;
        if (auctionSendForm.getGoodsType() == 1) {
            concernedNum = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getCollectionId, auctionSendForm.getGoodsId()))
                    .parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet()).size();
        } else {
            concernedNum = userBoxService.list(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getBoxId, auctionSendForm.getGoodsId()))
                    .parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet()).size();
        }

        //??????
        //BigDecimal commission = auctionSendForm.getStartPrice().multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class)).setScale(2, RoundingMode.DOWN);
        Integer preTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRE_TIME, Integer.class);
        Integer bidTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_BID_TIME, Integer.class);
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        String idStr = IdUtil.getSnowflakeNextIdStr();
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(preTime, DelayLevelEnum.LEVEL_6);
        AuctionSend auctionSend = Builder.of(AuctionSend::new)
                .with(AuctionSend::setId, idStr)
                .with(AuctionSend::setUserId, userId)
                .with(AuctionSend::setAuctionSendStatus, AuctionSendStatus.WAIT_START.getCode())
                .with(AuctionSend::setMyGoodsId, auctionSendForm.getMyGoodsId())
                .with(AuctionSend::setGoodsId, auctionSendForm.getGoodsId())
                .with(AuctionSend::setGoodsType, auctionSendForm.getGoodsType())
                .with(AuctionSend::setStartPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setCateId, cateId)
                .with(AuctionSend::setGoodsName, goodsName)
                .with(AuctionSend::setNowPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setSoldPrice, auctionSendForm.getSoldPrice())
                .with(AuctionSend::setConcernedNum, concernedNum)
                .with(AuctionSend::setCommission, systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class))
                .with(AuctionSend::setMargin, auctionSendForm.getStartPrice()
                        .multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class)).setScale(2, RoundingMode.HALF_UP))
                .with(AuctionSend::setStartTime, LocalDateTime.now().plusMinutes(DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES)))
                .with(AuctionSend::setEndTime,LocalDateTime.now().plusMinutes(preTime + bidTime + delayTime)).build();
        auctionSend.createD(userId);
        deliverProducer.sendAuctionStart(idStr, Builder.of(DeliverMsg::new).with(DeliverMsg::setBuiId,idStr).with(DeliverMsg::setBuiName,StrUtil.format("???????????????,???????????????:{}",idStr)).with(DeliverMsg::setResetHost, idStr).build(), defaultEnum);
        save(auctionSend);
    }

    @Override
    public List<KeywordVo> queryDict(String keyword) {
        List<String> collectIonNames = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getGoodsType,1)
                .eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.WAIT_START.getCode())
                .or().eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BIDING.getCode())
                .select(AuctionSend::getGoodsName).like(AuctionSend::getGoodsName, keyword).orderByDesc(AuctionSend::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getGoodsName()).collect(Collectors.toList());
        List<String> boxNames = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getGoodsType, 2)
                .eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.WAIT_START.getCode())
                .or().eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BIDING.getCode()).
                select(AuctionSend::getGoodsName).like(AuctionSend::getGoodsName, keyword).orderByDesc(AuctionSend::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getGoodsName()).collect(Collectors.toList());
        return  initKeywordVo(collectIonNames,boxNames);
    }

    private List<KeywordVo> initKeywordVo(List<String> collectIonNames,List<String> boxNames){
        List<KeywordVo> keywordVos = Lists.newArrayList();
        for (String collectIonName : collectIonNames) {
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setCommTitle(collectIonName);
            keywordVo.setType(1);
            keywordVos.add(keywordVo);

        }
        for (String boxName : boxNames) {
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setCommTitle(boxName);
            keywordVo.setType(2);
            keywordVos.add(keywordVo);
        }
        Collections.shuffle(keywordVos);
        return keywordVos;
    }

    @Override
    public TableDataInfo<MyAuctionSendVo> pageList(AuctionSendQuery sendQuery, String userId) {
        List<AuctionSend> auctionSendList = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getUserId, userId)
                .eq(sendQuery.getAuctionSendStatus() != null && sendQuery.getAuctionSendStatus() != 0, AuctionSend::getAuctionSendStatus, sendQuery.getAuctionSendStatus())
                .orderByDesc(AuctionSend::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(auctionSendList.parallelStream().map(this::providerMyAuctionSendVo).collect(Collectors.toList()), auctionSendList);
    }

    private MyAuctionSendVo providerMyAuctionSendVo(AuctionSend auctionSend) {
        MyAuctionSendVo auctionSendVo = Builder.of(MyAuctionSendVo::new).build();
        BeanUtil.copyProperties(auctionSend, auctionSendVo);
        auctionSendVo.setDelayTime(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class));
        auctionSendVo.setCommission(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class));
        if (auctionSend.getEndPayTime() != null) {
            auctionSendVo.setEndPayTime(auctionSend.getEndPayTime());
        }
        if (auctionSend.getGoodsType() == 1) {
            auctionSendVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            auctionSendVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            auctionSendVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        }
        if (auctionSend.getGoodsType() == 2) {
            auctionSendVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            auctionSendVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            auctionSendVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        }
        return auctionSendVo;
    }

    /**
     * ??????????????????
     * @param marketQuery
     * @return
     */

    @Override
    public TableDataInfo<AuctionMarketVo> auctionMarketList(AuctionMarketQuery marketQuery) {
        List<AuctionSend> list = list(getAuctionSendQueryWrappers(marketQuery));
        return TableDataInfoUtil.pageTableDataInfo(list.parallelStream().map(this::providerAuctionMarketVo).collect(Collectors.toList()), list);
    }

    //??????????????????
    private LambdaQueryWrapper<AuctionSend> getAuctionSendQueryWrappers(AuctionMarketQuery marketQuery) {
        LambdaQueryWrapper<AuctionSend> lambdaQueryWrapper = Wrappers.<AuctionSend>lambdaQuery();
        lambdaQueryWrapper.eq(AuctionSend::getGoodsType, marketQuery.getGoodsType());
        lambdaQueryWrapper.ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.WAIT_PAY.getCode());
        lambdaQueryWrapper.ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_PASS.getCode());
        lambdaQueryWrapper.ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BREAK.getCode());
        lambdaQueryWrapper.ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_SUCCESS.getCode());

        lambdaQueryWrapper.eq(StrUtil.isNotBlank(marketQuery.getCateId()), AuctionSend::getCateId, marketQuery.getCateId());
        lambdaQueryWrapper.like(StrUtil.isNotBlank(marketQuery.getCommName()), AuctionSend::getGoodsName, marketQuery.getCommName());

        if (Objects.nonNull(marketQuery.getPriceOrder()) && Integer.valueOf(0).equals(marketQuery.getPriceOrder()))
            lambdaQueryWrapper.orderByDesc(AuctionSend::getStartPrice);
        if (Objects.nonNull(marketQuery.getPriceOrder()) && Integer.valueOf(1).equals(marketQuery.getPriceOrder()))
            lambdaQueryWrapper.orderByAsc(AuctionSend::getStartPrice);
        return lambdaQueryWrapper;
    }


    //??????????????????

    @Override
    public AuctionCollectionAllVo auctionCollectionInfo(String collectionId, String auctionSendId) {
        //??????????????????????????????????????????
        checkStatus(auctionSendId);
        AuctionSend auctionSend = getById(auctionSendId);
        AuctionCollectionAllVo auctionCollectionAllVo = Builder.of(AuctionCollectionAllVo::new)
                .with(AuctionCollectionAllVo::setCollectionVo, collectionService.getBaseCollectionVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionInfoVo,providerCollectionInfoVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionNumber,userCollectionService.getById(auctionSend.getMyGoodsId()).getCollectionNumber())
                .with(AuctionCollectionAllVo::setAuctionVo,providerAuctionCollectionVo(auctionSendId)).build();

        return auctionCollectionAllVo;
    }

    //??????????????????
    @Override
    public AuctionBoxAllVo auctionBoxInfo(String boxId, String auctionSendId) {
        //??????????????????????????????????????????
        checkStatus(auctionSendId);
        AuctionBoxAllVo auctionBoxAllVo = Builder.of(AuctionBoxAllVo::new)
                .with(AuctionBoxAllVo::setBoxListVo,boxService.getBaseBoxListVo(boxId))
                .with(AuctionBoxAllVo::setBoxCollectionJoinVos,boxCollectionService.findJoinCollections(boxId))
                .with(AuctionBoxAllVo::setAuctionVo, providerAuctionCollectionVo(auctionSendId)).build();

        return auctionBoxAllVo;
    }

    //??????????????????????????????????????????
    private void checkStatus(String auctionSendId) {
        AuctionSend auctionSend = getById(auctionSendId);
        if (AuctionSendStatus.WAIT_START.getCode().equals(auctionSend.getAuctionSendStatus())) {
            if (LocalDateTime.now().isAfter(auctionSend.getStartTime())) {
                auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BIDING.getCode());
                updateById(auctionSend);
            }
        }
    }


    private AuctionVo providerAuctionCollectionVo(String auctionSendId) {
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        AuctionSend auctionSend = getById(auctionSendId);
        AuctionVo auctionVo = Builder.of(AuctionVo::new).build();
        auctionVo.setCommission(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class));
        auctionVo.setMargin(auctionSend.getMargin());
        auctionVo.setNowPrice(auctionSend.getNowPrice());
        auctionVo.setSoldPrice(auctionSend.getSoldPrice());
        auctionVo.setStartPrice(auctionSend.getStartPrice());
        auctionVo.setConcernedNum(auctionSend.getConcernedNum());
        auctionVo.setDelayTime(delayTime);
        auctionVo.setStartTime(auctionSend.getStartTime());
        auctionVo.setEndTime(auctionSend.getEndTime());
        auctionVo.setAuctionPriceRange(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRICE_RANGE, BigDecimal.class));
        auctionVo.setAuctionSendStatus(auctionSend.getAuctionSendStatus());
        if (StringUtils.isNotBlank(auctionSend.getAuctionOrderId())) {
            AuctionOrder auctionOrder = auctionOrderServiceObjectFactory.getObject().getById(auctionSend.getAuctionOrderId());
            auctionVo.setEndPayTime(auctionOrder.getEndTime());
        }

        return auctionVo;
    }

    @Override
    public AuctionVo getAuctionSendVo(String auctionSendId) {
        return providerAuctionCollectionVo(auctionSendId);
    }


    private CollectionInfoVo providerCollectionInfoVo(String collectionId) {
        CollectionInfoVo collectionInfoVo = Builder.of(CollectionInfoVo::new).build();
        CollectionInfo collectionInfo = collectionInfoMapper.selectOne(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, collectionId));
        BeanUtil.copyProperties(collectionInfo,collectionInfoVo);
        return collectionInfoVo;
    }

    @Override
    public CollectionInfoVo getBaseCollectionInfoVo(String collectionId) {
        return providerCollectionInfoVo(collectionId);
    }


    private AuctionMarketVo providerAuctionMarketVo (AuctionSend auctionSend) {
        AuctionMarketVo marketVo = Builder.of(AuctionMarketVo::new).build();
        //????????????
        int size = auctionPriceServiceObjectFactory.getObject().list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId())).size();
        marketVo.setPriceCount(size);
        BeanUtil.copyProperties(auctionSend, marketVo);
        if (auctionSend.getGoodsType() == 1) {
            marketVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            marketVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            marketVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        }
        if (auctionSend.getGoodsType() == 2) {
            marketVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            marketVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            marketVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        }
        return marketVo;
    }


    @Override
    public void reloadAuctionSend(List<AuctionSend> auctionSendList) {
        for (AuctionSend auctionSend : auctionSendList) {
            auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BREAK.getCode());
            auctionSend.updateD(auctionSend.getUserId());
            String info = "?????????????????????????????????";
            if (auctionSend.getGoodsType() == 1) {
                userCollectionService.showUserCollection(auctionSend.getUserId(), auctionSend.getMyGoodsId(), info);
            }
            if (auctionSend.getGoodsType() == 2) {
                userBoxService.showUserBox(auctionSend.getMyGoodsId(), auctionSend.getUserId(), info);
            }
        }
        updateBatchById(auctionSendList);

    }

    @Override
    public void reloadAuctionSendForMq(AuctionSend auctionSend) {
        auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BREAK.getCode());
        auctionSend.updateD(auctionSend.getUserId());
        String info = "?????????????????????????????????";
        if (auctionSend.getGoodsType() == 1) {
            userCollectionService.showUserCollection(auctionSend.getUserId(), auctionSend.getMyGoodsId(), info);
        }
        if (auctionSend.getGoodsType() == 2) {
            userBoxService.showUserBox(auctionSend.getMyGoodsId(), auctionSend.getUserId(), info);
        }
        updateById(auctionSend);
    }

    //?????????????????????
    @Override
    public R reAuctionSend(AuctionSendForm auctionSendForm, String auctionSendId) {
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        /*Integer preTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRE_TIME, Integer.class);
        Integer bidTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_BID_TIME, Integer.class);
        AuctionSend auctionSend = getById(auctionSendId);
        auctionSend.setAuctionSendStatus(AuctionSendStatus.WAIT_START.getCode());
        auctionSend.setStartPrice(auctionSendForm.getStartPrice());
        auctionSend.setSoldPrice(auctionSendForm.getSoldPrice());
        auctionSend.setNowPrice(auctionSendForm.getStartPrice());
        auctionSend.setMargin(auctionSendForm.getStartPrice()
                .multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class)).setScale(2, RoundingMode.HALF_UP));
        auctionSend.setCommission(auctionSendForm.getStartPrice()
                .multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class)).setScale(2, RoundingMode.DOWN));
        auctionSend.setStartTime(LocalDateTime.now().plusMinutes(preTime));
        auctionSend.setEndTime(LocalDateTime.now().plusMinutes(preTime + bidTime));
        auctionSend.updateD(auctionSend.getUserId());
        updateById(auctionSend);*/
        checkAll(auctionSendForm, userId);
        aspect(auctionSendForm, userId);
        return R.ok();
    }

    private void realCheck(String userId) {
        R<CntUserDto> cntUserDtoR = userService.commUni(userId, SecurityConstants.INNER);
        CntUserDto data = cntUserDtoR.getData();
        cn.hutool.core.lang.Assert.isTrue(OK_REAL.getCode().equals(data.getIsReal()),"??????????????????,???????????????!");
    }


    private void checkAll(AuctionSendForm auctionSendForm, String userId) {
        String isOpen = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_ACC, String.class);
        Assert.isTrue("1".equals(isOpen), "????????????????????????");
        realCheck(userId);
        Integer type = auctionSendForm.getGoodsType();
        Assert.isTrue(auctionSendForm.getStartPrice().compareTo(auctionSendForm.getSoldPrice()) < 1 ,"???????????????????????????");

        boolean exists = this.baseMapper.exists(Wrappers.<AuctionSend>lambdaQuery()
                .eq(AuctionSend::getMyGoodsId, auctionSendForm.getMyGoodsId())
                .ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BREAK.getCode())
                .ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_PASS.getCode()));
        Assert.isFalse(exists, "??????????????????,???????????????????????????");
        // ??????
        if (type == 1)
            Assert.isTrue(userCollectionService.existUserCollection(userId,auctionSendForm.getMyGoodsId()),"?????????????????????,???????????????????????????!");

        // ??????
        if (type == 2)
            Assert.isTrue(userBoxService.existUserBox(userId,auctionSendForm.getMyGoodsId()),"?????????????????????,???????????????????????????!");
    }

    private void aspect(@NotNull AuctionSendForm auctionSendForm, @NotNull String userId) {
        // ??????????????????,??????????????????,?????? ???????????????????????????????????????
        Integer type = auctionSendForm.getGoodsType();
        String myGoodsId = auctionSendForm.getMyGoodsId();

        String realBuiId = null;
        String info = null;
        String cateId = null;
        String buiName= null;
        if (type == 1) {
            // ????????????????????????,????????????????????????
            info = StrUtil.format("?????????????????????,?????????????????????????????????!");
            realBuiId =  userCollectionService.hideUserCollection(myGoodsId,userId,info);
            CntCollection cntCollection = collectionService.getById(realBuiId);
            cateId = cntCollection.getCateId();
            buiName = cntCollection.getCollectionName();
        }
        if (type == 2) {
            // ?????????????????? ??????,?????????????????????
            info = StrUtil.format("?????????????????????,?????????????????????????????????!");
            realBuiId = userBoxService.hideUserBox(myGoodsId,userId,info);
            Box box = boxService.getById(realBuiId);
            cateId = box.getCateId();
            buiName= box.getBoxTitle();
        }
        if (StrUtil.isBlank(info) && StrUtil.isBlank(realBuiId))
            throw new ServiceException("not fount type [0-1] now type is "+type+"");

        pushAuctionMarket(auctionSendForm, userId, cateId, buiName);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock("timeStartAuction")
    public void timeStartAuction() {
        List<AuctionSend> list = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.WAIT_START.getCode()));
        if (list.isEmpty()) return;
        for (AuctionSend auctionSend : list) {
            if (LocalDateTime.now().isAfter(auctionSend.getStartTime())) {
                auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BIDING.getCode());
            }
        }
        updateBatchById(list);
    }

    /**
     * ?????????????????????????????????????????????
     * @param id
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startAuction(String id) {
        Integer bidTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_BID_TIME, Integer.class);
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(bidTime, DelayLevelEnum.LEVEL_6);
        AuctionSend auctionSend = getById(id);
        Assert.isTrue(Objects.nonNull(auctionSend) && AuctionSendStatus.WAIT_START.getCode().equals(auctionSend.getAuctionSendStatus()),"???????????????????????????!");
        auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BIDING.getCode());
        auctionSend.setEndTime(LocalDateTime.now().plusMinutes(DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES)));
        deliverProducer.sendAuctionEnd(id, Builder.of(DeliverMsg::new).with(DeliverMsg::setBuiId,id).with(DeliverMsg::setBuiName,StrUtil.format("???????????????,?????????:{}",id)).with(DeliverMsg::setResetHost, IdUtil.getSnowflakeNextIdStr()).build(), defaultEnum);
        auctionSend.updateD(auctionSend.getUserId());
        updateById(auctionSend);
    }

}
