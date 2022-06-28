package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.AuctionOrder;
import com.manyun.business.domain.query.AuctionOrderQuery;
import com.manyun.business.domain.vo.AuctionOrderVo;
import com.manyun.business.mapper.AuctionOrderMapper;
import com.manyun.business.service.IAuctionOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.bean.BeanUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 拍卖订单表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class AuctionOrderServiceImpl extends ServiceImpl<AuctionOrderMapper, AuctionOrder> implements IAuctionOrderService {

    @Override
    public TableDataInfo<AuctionOrderVo> myAuctionOrderList(AuctionOrderQuery orderQuery, String userId) {
        List<AuctionOrder> list = list(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getUserId, userId)
                .eq(orderQuery.getAuctionStatus() != null && orderQuery.getAuctionStatus() != 0, AuctionOrder::getAuctionStatus, orderQuery.getAuctionStatus())
                .orderByDesc(AuctionOrder::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(list.parallelStream().map(this::providerAuctionOrderVo).collect(Collectors.toList()), list);
    }

    private AuctionOrderVo providerAuctionOrderVo(AuctionOrder auctionOrder) {
        AuctionOrderVo auctionOrderVo = Builder.of(AuctionOrderVo::new).build();
        BeanUtil.copyProperties(auctionOrder, auctionOrderVo);
        return auctionOrderVo;
    }
}
