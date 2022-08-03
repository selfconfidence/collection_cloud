package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.common.core.domain.R;
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

    @GetMapping("/list")
    @ApiOperation("订单管理列表")
    public TableDataInfo<CntConsignmentVo> list(ConsignmentQuery consignmentQuery)
    {
        return cntConsignmentService.selectCntConsignmentList(consignmentQuery);
    }

    @PostMapping("/paymentReview")
    @ApiOperation("打款审核")
    public R paymentReview(@Valid @RequestBody PaymentReviewDto paymentReviewDto)
    {
        return cntConsignmentService.paymentReview(paymentReviewDto);
    }

}
