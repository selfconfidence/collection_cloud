package com.manyun.business.controller;

import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.CollectionAllVo;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.business.service.ICollectionService;
import com.manyun.business.service.IOrderService;
import com.manyun.comm.api.model.LoginBusinessUser;
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
        LoginBusinessUser loginBusinessUser = SecurityUtils.getTestLoginBusinessUser();
        PageHelper.startPage(orderQuery.getPageNum(), orderQuery.getPageSize());
        TableDataInfo<OrderVo> orderVoTableDataInfo = orderService.pageQueryList(orderQuery, loginBusinessUser.getUserId());
        return R.ok(orderVoTableDataInfo);

    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询藏品详情信息",notes = "根据藏品编号查询藏品详情信息")
    public R<CollectionAllVo> info(@PathVariable String id){
        System.out.println(111);

        return R.ok(collectionService.info(id));
    }

    @GetMapping("/timeCancel")
    @ApiOperation(value = "定时调度取消未支付的订单",hidden = true)
    @InnerAuth
    public R timeCancel(){
         orderService.timeCancel();
        return R.ok();
    }

}

