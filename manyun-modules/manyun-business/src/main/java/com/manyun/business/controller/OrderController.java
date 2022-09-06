package com.manyun.business.controller;

import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.OrderPayForm;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ICollectionService;
import com.manyun.business.service.IOrderService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单相关apis")
public class OrderController extends BaseController {

    @Autowired
    IOrderService orderService;

    @Autowired
    private ICollectionService collectionService;

    @PostMapping("/myOrderList")
    @ApiOperation("分页查询我的订单信息")
    public R<TableDataInfo<OrderVo>> myOrderList (@RequestBody OrderQuery orderQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        PageHelper.startPage(orderQuery.getPageNum(), orderQuery.getPageSize());
        TableDataInfo<OrderVo> orderVoTableDataInfo = orderService.pageQueryList(orderQuery, loginBusinessUser.getUserId());
        return R.ok(orderVoTableDataInfo);

    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询藏品详情信息",notes = "根据藏品编号查询藏品详情信息")
    @Deprecated
    public R<CollectionAllVo> info(@PathVariable String id){
       // System.out.println(111);
        return R.ok(collectionService.info(id));
    }

    @GetMapping("/orderInfo/{id}")
    @ApiOperation(value = "查询订单的详情",notes = "结构比较复杂，按状态区分\t " +
            "moneyBln 是组合支付的时候 用余额抵扣后剩余的金额, payType =0 才有用,如果订单状态是待支付,并且 这个值 不是0.00,那就说明 那么剩余需要支付的金额就是 (订单金额 - moneyBln) 并且二次支付 必须指定是 payType = 0,这种情况固定死即可!  " +
            " \n")
    public R<OrderInfoVo> orderInfo(@PathVariable String id){
        return R.ok(orderService.info(id));
    }

    /**
     * 取消订单
     * @return
     */
    @GetMapping("/cancelOrder/{id}")
    @ApiOperation(value = "取消订单",notes = "根据订单编号取消订单即可\t 寄售订单也可以通过该接口进行取消 \n version 1.0.1")
    public synchronized R cancelOrder(@PathVariable String id){
        orderService.cancelOrder(id);
        return R.ok();
    }

    @PostMapping("/unifiedOrder")
    @ApiOperation(value = "(普通订单 & 寄售订单)根据订单编号统一下单支付",notes = "传递订单编号,待支付订单都可以通过此接口二次支付!\t " +
            "如果当前支付类型是 (组合支付)0 余额支付,那么用户余额不够的情况下直接扣除所有的余额,其他的调用银联进行支付!" +
            " \n version 1.0.1")
    public synchronized R<PayVo> unifiedOrder(@RequestBodyRsa @Valid OrderPayForm orderPayForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(orderService.unifiedOrder(orderPayForm,notNullLoginBusinessUser.getUserId()));
    }


/*    @PostMapping("/consignmentUnifiedOrder")
    @ApiOperation(value = "(寄售订单)根据订单编号对寄售统一下单",notes = "传递订单编号,待支付订单都可以通过此接口二次支付!\t " +
            "如果当前支付类型是 (组合支付)0 余额支付,那么用户余额不够的情况下直接扣除所有的余额,其他的调用银联进行支付!" +
            " \n version 1.0.1")
    public R<PayVo> consignmentUnifiedOrder(@RequestBody @Valid OrderPayForm orderPayForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(orderService.consignmentUnifiedOrder(orderPayForm,notNullLoginBusinessUser.getUserId()));
    }*/

    @GetMapping("/timeCancel")
    @ApiOperation(value = "定时调度取消未支付的订单",hidden = true)
    @InnerAuth
    public R timeCancel(){
         orderService.timeCancel();
        return R.ok();
    }

}

