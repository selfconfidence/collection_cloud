package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.AuctionOrder;
import com.manyun.business.mapper.AuctionOrderMapper;
import com.manyun.business.service.IAuctionOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
