package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.AuctionSend;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.MediaVo;
import com.manyun.business.domain.vo.MyAuctionSendVo;
import com.manyun.business.mapper.AuctionSendMapper;
import com.manyun.business.service.IAuctionSendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IBoxService;
import com.manyun.business.service.ICollectionService;
import com.manyun.business.service.IMediaService;
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


    @Override
    public R auctionSend(AuctionSendForm auctionSendForm, String userId) {
        AuctionSend auctionSend = Builder.of(AuctionSend::new)
                .with(AuctionSend::setUserId, userId)
                .with(AuctionSend::setAuctionStatus, AuctionStatus.WAIT_START.getCode())
                .with(AuctionSend::setGoodsId, auctionSendForm.getGoodsId())
                .with(AuctionSend::setGoodsType, auctionSendForm.getGoodsType())
                .with(AuctionSend::setStartPrice, auctionSendForm.getStartPrice())
                .with(AuctionSend::setSoldPrice, auctionSendForm.getSoldPrice())
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
            CntCollection collection = collectionService.getById(auctionSend.getGoodsId());
            auctionSendVo.setGoodsName(collection.getCollectionName());
            List<MediaVo> mediaVos = mediaService.initMediaVos(collection.getId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            auctionSendVo.setMediaVos(mediaVos);
        }
        if (auctionSend.getGoodsType() == 2) {
            Box box = boxService.getById(auctionSend.getGoodsId());
            auctionSendVo.setGoodsName(box.getBoxTitle());
        }
        return auctionSendVo;
    }
}
