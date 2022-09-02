package com.manyun.admin.controller;

import com.manyun.admin.domain.query.AuctionOrderQuery;
import com.manyun.admin.domain.query.AuctionPriceQuery;
import com.manyun.admin.domain.vo.AuctionPriceVo;
import com.manyun.admin.domain.vo.CntAuctionOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.admin.service.ICntAuctionOrderService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 拍卖订单Controller
 *
 * @author yanwei
 * @date 2022-09-01
 */
@RestController
@RequestMapping("/auctionOrder")
@Api(tags = "拍卖订单apis")
public class CntAuctionOrderController extends BaseController
{
    @Autowired
    private ICntAuctionOrderService cntAuctionOrderService;

    /**
     * 查询拍卖订单列表
     */
    //@RequiresPermissions("admin:order:list")
    @GetMapping("/list")
    @ApiOperation("查询拍卖订单列表")
    public TableDataInfo<CntAuctionOrderVo> list(AuctionOrderQuery auctionOrderQuery)
    {
        return cntAuctionOrderService.selectCntAuctionOrderList(auctionOrderQuery);
    }

    /**
     * 竞价列表
     */
    @GetMapping("/auctionPriceList")
    @ApiOperation("竞价列表")
    public TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery auctionPriceQuery) {
        return cntAuctionOrderService.auctionPriceList(auctionPriceQuery);
    }

}
