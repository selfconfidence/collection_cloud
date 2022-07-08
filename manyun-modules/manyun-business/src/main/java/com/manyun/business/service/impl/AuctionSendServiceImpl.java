package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.AuctionMarketVo;
import com.manyun.business.domain.vo.MediaVo;
import com.manyun.business.domain.vo.MyAuctionSendVo;
import com.manyun.business.mapper.AuctionSendMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionStatus;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public R auctionSend(AuctionSendForm auctionSendForm, String userId) {

        checkAll(auctionSendForm, userId);

        aspect(auctionSendForm, userId);
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
        BigDecimal commission = auctionSendForm.getStartPrice().multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_SCALE, BigDecimal.class)).setScale(2, RoundingMode.DOWN);
        Integer preTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRE_TIME, Integer.class);
        Integer bidTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_BID_TIME, Integer.class);
        AuctionSend auctionSend = Builder.of(AuctionSend::new)
                .with(AuctionSend::setUserId, userId)
                .with(AuctionSend::setAuctionStatus, AuctionStatus.WAIT_START.getCode())
                .with(AuctionSend::setMyGoodsId, auctionSendForm.getMyGoodsId())
                .with(AuctionSend::setGoodsId, auctionSendForm.getGoodsId())
                .with(AuctionSend::setGoodsType, auctionSendForm.getGoodsType())
                .with(AuctionSend::setGoodsName, auctionSendForm.getGoodsName())
                .with(AuctionSend::setStartPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setNowPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setSoldPrice, auctionSendForm.getSoldPrice())
                .with(AuctionSend::setConcernedNum, concernedNum)
                .with(AuctionSend::setCommission, commission)
                .with(AuctionSend::setMargin, auctionSendForm.getStartPrice()
                        .multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class)).setScale(2, RoundingMode.HALF_UP))
                .with(AuctionSend::setAuctionStatus, AuctionStatus.WAIT_START.getCode())
                .with(AuctionSend::setCreatedTime, LocalDateTime.now())
                .with(AuctionSend::setStartTime, LocalDateTime.now().plusMinutes(preTime))
                .with(AuctionSend::setEndTime,LocalDateTime.now().plusMinutes(preTime + bidTime)).build();
        return this.save(auctionSend) ? R.ok() : R.fail();
    }

    @Override
    public TableDataInfo<MyAuctionSendVo> pageList(AuctionSendQuery sendQuery, String userId) {
        List<AuctionSend> auctionSendList = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getUserId, userId)
                .eq(sendQuery.getAuctionSendStatus() != null && sendQuery.getAuctionSendStatus() != 0, AuctionSend::getAuctionStatus, sendQuery.getAuctionSendStatus())
                .orderByDesc(AuctionSend::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(auctionSendList.parallelStream().map(this::providerMyAuctionSendVo).collect(Collectors.toList()), auctionSendList);
    }

    private MyAuctionSendVo providerMyAuctionSendVo(AuctionSend auctionSend) {
        MyAuctionSendVo auctionSendVo = Builder.of(MyAuctionSendVo::new).build();
        BeanUtil.copyProperties(auctionSend, auctionSendVo);
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

    @Override
    public TableDataInfo<AuctionMarketVo> auctionMarketList(AuctionMarketQuery marketQuery) {
        List<AuctionSend> list = list(Wrappers.<AuctionSend>lambdaQuery().eq(AuctionSend::getGoodsType, marketQuery.getGoodsType())
                .orderByDesc(AuctionSend::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(list.parallelStream().map(this::providerAuctionMarketVo).collect(Collectors.toList()), list);
    }

    private AuctionMarketVo providerAuctionMarketVo (AuctionSend auctionSend) {
        AuctionMarketVo marketVo = Builder.of(AuctionMarketVo::new).build();
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
            auctionSend.setAuctionStatus(AuctionStatus.WAIT_START.getCode());
            auctionSend.updateD(auctionSend.getUserId());
        }
        updateBatchById(auctionSendList);

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
                .ne(AuctionSend::getAuctionStatus, AuctionStatus.BID_BREAK.getCode()));
        Assert.isFalse(exists, "请勿重复送拍");
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
            return;
        }
        if (type == 2) {
            // 将自己的盲盒 隐藏,归并状态及词条
            info = StrUtil.format("该盲盒被寄售了,已将盲盒移送到寄售市场!");
            realBuiId = userBoxService.hideUserBox(myGoodsId,userId,info);
            Box box = boxService.getById(realBuiId);
            cateId = box.getCateId();
            buiName= box.getBoxTitle();
            return;
        }
        if (StrUtil.isBlank(info) && StrUtil.isBlank(realBuiId))
            throw new ServiceException("not fount type [0-1] now type is "+type+"");
    }

}
