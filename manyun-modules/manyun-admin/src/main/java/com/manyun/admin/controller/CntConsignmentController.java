package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.*;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.query.ConsignmentStatusQuery;
import com.manyun.admin.domain.query.OrderListQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.comm.api.RemoteConsignmentService;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.admin.service.ICntConsignmentService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

import javax.validation.Valid;

@RestController
@RequestMapping("/consignment")
@Api(tags = "订单管理apis")
public class CntConsignmentController extends BaseController
{
    @Autowired
    private ICntConsignmentService cntConsignmentService;

    @Autowired
    private RemoteConsignmentService remoteConsignmentService;

    @GetMapping("/orderList")
    @ApiOperation("订单列表")
    public TableDataInfo<CntOrderVo> orderList(OrderListQuery orderListQuery)
    {
        return cntConsignmentService.orderList(orderListQuery);
    }

    @PostMapping("/orderInfo")
    @ApiOperation("订单列表详情")
    public R<CntOrderVo> orderInfo(@Valid @RequestBody OrderInfoDto orderInfoDto)
    {
        return cntConsignmentService.orderInfo(orderInfoDto);
    }

    @GetMapping("/collectionList")
    @ApiOperation("寄售藏品订单管理列表")
    public TableDataInfo<CntConsignmentVo> collectionList(ConsignmentQuery consignmentQuery)
    {
        return cntConsignmentService.collectionList(consignmentQuery);
    }

    @GetMapping("/boxList")
    @ApiOperation("寄售盲盒订单管理列表")
    public TableDataInfo<CntConsignmentVo> boxList(ConsignmentQuery consignmentQuery)
    {
        return cntConsignmentService.boxList(consignmentQuery);
    }

    @PostMapping("/consignmentStatus")
    @ApiOperation("修改寄售状态")
    public R consignmentStatus(@Valid @RequestBody ConsignmentStatusQuery consignmentStatusQuery)
    {
        return toResult(cntConsignmentService.consignmentStatus(consignmentStatusQuery));
    }

    @PostMapping("/getInfo")
    @ApiOperation("获取订单管理详细信息")
    public R<CntConsignmentVo> getInfo(@Valid @RequestBody ConsignmentInfoDto consignmentInfoDto)
    {
        return R.ok(cntConsignmentService.selectConsignmentOrderById(consignmentInfoDto));
    }

    @PostMapping("/paymentReview")
    @ApiOperation("打款审核")
    public R paymentReview(@Valid @RequestBody PaymentReviewDto paymentReviewDto)
    {
        return remoteConsignmentService.consignmentSuccess(paymentReviewDto.getId(), SecurityConstants.INNER);
    }

}
