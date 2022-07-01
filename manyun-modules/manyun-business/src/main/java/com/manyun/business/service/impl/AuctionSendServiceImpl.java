package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.AuctionSend;
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
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    ICollectionService collectionService;

    @Autowired
    IBoxService boxService;

    @Autowired
    IMediaService mediaService;

    @Autowired
    IUserCollectionService userCollectionService;

    @Autowired
    IUserBoxService userBoxService;


    @Override
    public R auctionSend(AuctionSendForm auctionSendForm, String userId) {
        //判断是否已送拍过
        boolean exists = this.baseMapper.exists(Wrappers.<AuctionSend>lambdaQuery()
                .eq(AuctionSend::getMyGoodsId, auctionSendForm.getMyGoodsId())
                .ne(AuctionSend::getAuctionStatus, AuctionStatus.BID_BREAK.getCode()));
        if (exists) {
            return R.fail("请勿重复送拍");
        }
        AuctionSend auctionSend = Builder.of(AuctionSend::new)
                .with(AuctionSend::setUserId, userId)
                .with(AuctionSend::setAuctionStatus, AuctionStatus.WAIT_START.getCode())
                .with(AuctionSend::setMyGoodsId, auctionSendForm.getMyGoodsId())
                .with(AuctionSend::setGoodsId, auctionSendForm.getGoodsId())
                .with(AuctionSend::setGoodsType, auctionSendForm.getGoodsType())
                .with(AuctionSend::setGoodsName, auctionSendForm.getGoodsName())
                .with(AuctionSend::setStartPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setSoldPrice, auctionSendForm.getSoldPrice())
                .with(AuctionSend::setAuctionStatus, AuctionStatus.WAIT_START.getCode())
                .with(AuctionSend::setCreatedTime, LocalDateTime.now()).build();
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



}
