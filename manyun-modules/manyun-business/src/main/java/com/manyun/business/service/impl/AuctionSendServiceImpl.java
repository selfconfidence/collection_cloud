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
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionSendStatus;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
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
import java.util.stream.Collectors;


/**
 * <p>
 * 拍卖表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
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

    /**
     * 查询保证金比例
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
     * 送拍到拍卖市场
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
     * 推送到拍卖市场
     * @param auctionSendForm
     * @param userId
     * @param cateId
     * @param goodsName
     */
    private void pushAuctionMarket(AuctionSendForm auctionSendForm, String userId, String cateId, String goodsName) {
        //收藏人数
        int concernedNum = 0;
        if (auctionSendForm.getGoodsType() == 1) {
            concernedNum = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getCollectionId, auctionSendForm.getGoodsId()))
                    .parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet()).size();
        } else {
            concernedNum = userBoxService.list(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getBoxId, auctionSendForm.getGoodsId()))
                    .parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet()).size();
        }

        //佣金
        //BigDecimal commission = auctionSendForm.getStartPrice().multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class)).setScale(2, RoundingMode.DOWN);
        Integer preTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRE_TIME, Integer.class);
        Integer bidTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_BID_TIME, Integer.class);
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        AuctionSend auctionSend = Builder.of(AuctionSend::new)
                .with(AuctionSend::setId, IdUtil.getSnowflakeNextIdStr())
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
                .with(AuctionSend::setStartTime, LocalDateTime.now().plusMinutes(preTime))
                .with(AuctionSend::setEndTime,LocalDateTime.now().plusMinutes(preTime + bidTime + delayTime)).build();
        auctionSend.createD(userId);
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
            List<MediaVo> mediaVos = mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            auctionSendVo.setMediaVos(mediaVos);
        }
        if (auctionSend.getGoodsType() == 2) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
            auctionSendVo.setMediaVos(mediaVos);
        }
        return auctionSendVo;
    }

    /**
     * 拍卖市场列表
     * @param marketQuery
     * @return
     */

    @Override
    public TableDataInfo<AuctionMarketVo> auctionMarketList(AuctionMarketQuery marketQuery) {
        List<AuctionSend> list = list(getAuctionSendQueryWrappers(marketQuery));
        return TableDataInfoUtil.pageTableDataInfo(list.parallelStream().map(this::providerAuctionMarketVo).collect(Collectors.toList()), list);
    }

    //拍卖市场排序
    private LambdaQueryWrapper<AuctionSend> getAuctionSendQueryWrappers(AuctionMarketQuery marketQuery) {
        LambdaQueryWrapper<AuctionSend> lambdaQueryWrapper = Wrappers.<AuctionSend>lambdaQuery();
        lambdaQueryWrapper.eq(AuctionSend::getGoodsType, marketQuery.getGoodsType());
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


    //查询藏品详情

    @Override
    public AuctionCollectionAllVo auctionCollectionInfo(String collectionId, String auctionSendId) {
        //点击详情，判断时间，修改状态
        checkStatus(auctionSendId);
        AuctionSend auctionSend = getById(auctionSendId);
        AuctionCollectionAllVo auctionCollectionAllVo = Builder.of(AuctionCollectionAllVo::new)
                .with(AuctionCollectionAllVo::setCollectionVo, collectionService.getBaseCollectionVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionInfoVo,providerCollectionInfoVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionNumber,userCollectionService.getById(auctionSend.getMyGoodsId()).getCollectionNumber())
                .with(AuctionCollectionAllVo::setAuctionVo,providerAuctionCollectionVo(auctionSendId)).build();

        return auctionCollectionAllVo;
    }

    //查询盲盒详情
    @Override
    public AuctionBoxAllVo auctionBoxInfo(String boxId, String auctionSendId) {
        //点击详情，判断时间，修改状态
        checkStatus(auctionSendId);
        AuctionBoxAllVo auctionBoxAllVo = Builder.of(AuctionBoxAllVo::new)
                .with(AuctionBoxAllVo::setBoxListVo,boxService.getBaseBoxListVo(boxId))
                .with(AuctionBoxAllVo::setBoxCollectionJoinVos,boxCollectionService.findJoinCollections(boxId))
                .with(AuctionBoxAllVo::setAuctionVo, providerAuctionCollectionVo(auctionSendId)).build();

        return auctionBoxAllVo;
    }

    //点击详情，判断时间，修改状态
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
        //出价次数
        int size = auctionPriceServiceObjectFactory.getObject().list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId())).size();
        marketVo.setPriceCount(size);
        BeanUtil.copyProperties(auctionSend, marketVo);
        if (auctionSend.getGoodsType() == 1) {
            marketVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        }
        if (auctionSend.getGoodsType() == 2) {
            marketVo.setMediaVos(mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        }
        return marketVo;
    }


    @Override
    public void reloadAuctionSend(List<AuctionSend> auctionSendList) {
        for (AuctionSend auctionSend : auctionSendList) {
            auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BREAK.getCode());
            auctionSend.updateD(auctionSend.getUserId());
            String info = "已违约，从拍卖市场退回";
            if (auctionSend.getGoodsType() == 1) {
                userCollectionService.showUserCollection(auctionSend.getUserId(), auctionSend.getMyGoodsId(), info);
            }
            if (auctionSend.getGoodsType() == 2) {
                userBoxService.showUserBox(auctionSend.getUserId(), auctionSend.getMyGoodsId(), info);
            }
        }
        updateBatchById(auctionSendList);

    }

    //流拍后重新送拍
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


    private void checkAll(AuctionSendForm auctionSendForm, String userId) {
        Integer type = auctionSendForm.getGoodsType();
        // 藏品
        if (type == 1)
            Assert.isTrue(userCollectionService.existUserCollection(userId,auctionSendForm.getMyGoodsId()),"选择的藏品有误,请核实藏品详细信息!");

        // 盲盒
        if (type == 2)
            Assert.isTrue(userBoxService.existUserBox(userId,auctionSendForm.getMyGoodsId()),"选择的盲盒有误,请核实盲盒详细信息!");

        boolean exists = this.baseMapper.exists(Wrappers.<AuctionSend>lambdaQuery()
                .eq(AuctionSend::getMyGoodsId, auctionSendForm.getMyGoodsId())
                .ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BREAK.getCode())
                .ne(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_PASS.getCode()));
        Assert.isFalse(exists, "请勿重复送拍,请确定竞品当前状态");
    }

    private void aspect(@NotNull AuctionSendForm auctionSendForm, @NotNull String userId) {
        // 统一逻辑操作,将自己的藏品,盲盒 取消，推到拍卖市场中拍卖。
        Integer type = auctionSendForm.getGoodsType();
        String myGoodsId = auctionSendForm.getMyGoodsId();

        String realBuiId = null;
        String info = null;
        String cateId = null;
        String buiName= null;
        if (type == 1) {
            // 将自己的藏品隐藏,归并状态以及词条
            info = StrUtil.format("该藏品被送拍了,已将藏品移送到拍卖市场!");
            realBuiId =  userCollectionService.hideUserCollection(myGoodsId,userId,info);
            CntCollection cntCollection = collectionService.getById(realBuiId);
            cateId = cntCollection.getCateId();
            buiName = cntCollection.getCollectionName();
        }
        if (type == 2) {
            // 将自己的盲盒 隐藏,归并状态及词条
            info = StrUtil.format("该盲盒被送拍了,已将盲盒移送到拍卖市场!");
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
    public synchronized void timeStartAuction() {
        List<AuctionSend> list = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.WAIT_START.getCode()));
        if (list.isEmpty()) return;
        for (AuctionSend auctionSend : list) {
            if (LocalDateTime.now().isAfter(auctionSend.getStartTime())) {
                auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_BIDING.getCode());
            }
        }
        updateBatchById(list);
    }

}
