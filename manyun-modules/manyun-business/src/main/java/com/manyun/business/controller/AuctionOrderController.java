package com.manyun.business.controller;

import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.query.AuctionOrderQuery;
import com.manyun.business.domain.vo.AuctionOrderVo;
import com.manyun.business.service.IAuctionOrderService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 拍卖订单表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/auctionOrder")
@Api(tags = "我的拍卖订单相关apis")
public class AuctionOrderController {

    @Autowired
    private IAuctionOrderService auctionOrderService;

    @PostMapping("/myAuctionOrderList")
    @ApiOperation("分页查询我的订单信息")
    public R<TableDataInfo<AuctionOrderVo>> myAuctionOrderList(@RequestBody AuctionOrderQuery orderQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        PageHelper.startPage(orderQuery.getPageNum(), orderQuery.getPageSize());
        TableDataInfo<AuctionOrderVo> auctionOrderVoTableDataInfo = auctionOrderService.myAuctionOrderList(orderQuery, loginBusinessUser.getUserId());
        return R.ok(auctionOrderVoTableDataInfo);

    }

    @GetMapping("/timeCancelAuction")
    @ApiOperation(value = "定时调度取消未支付的拍卖订单",hidden = true)
    @InnerAuth
    public R timeCancel(){
        auctionOrderService.timeCancelAuction();
        return R.ok();
    }


}

