package com.manyun.business.controller;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.ConsignmentBoxListVo;
import com.manyun.business.domain.vo.ConsignmentCollectionListVo;
import com.manyun.business.service.ICntConsignmentService;
import com.manyun.business.service.ISystemService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * <p>
 * 寄售市场主表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
@RestController
@RequestMapping("/consignment")
@Api(tags = "寄售相关Apis")
public class CntConsignmentController {

    @Autowired
    private ISystemService systemService;

    @Autowired
    private ICntConsignmentService cntConsignmentService;


    @PostMapping("/consignmentAssets")
    @ApiOperation("对资产进行寄售")
    public R  consignmentAssets(@RequestBody @Valid UserConsignmentForm userConsignmentForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        cntConsignmentService.consignmentAssets(userConsignmentForm,notNullLoginBusinessUser.getUserId());
        return R.ok();
    }

    @GetMapping("/consignmentServerCharge")
    @ApiOperation(value = "寄售手续费",notes = "返回寄售手续费 百分比")
    public R<BigDecimal> consignmentServerCharge(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.CONSIGNMENT_SERVER_CHARGE,BigDecimal.class));
    }

    @PostMapping("/pageConsignmentCollectionList")
    @ApiOperation(value = "分页查询寄售市场藏品的信息",notes = "详情还是调用藏品详情即可\n,用藏品编号查详情\n购买的时候,按照寄售购买的流程进行！！！")
    public R<TableDataInfo<ConsignmentCollectionListVo>> pageConsignmentList(@RequestBody @Valid ConsignmentQuery consignmentQuery){
        return R.ok(cntConsignmentService.pageConsignmentList(consignmentQuery));
    }


    @PostMapping("/pageConsignmentBoxList")
    @ApiOperation(value = "分页查询寄售市场盲盒的信息",notes = "详情还是调用盲盒详情即可\n,用盲盒编号查详情\n购买的时候,按照寄售购买的流程进行！！！")
    public R<TableDataInfo<ConsignmentBoxListVo>> pageConsignmentBoxList(@RequestBody @Valid ConsignmentQuery consignmentQuery){
        return R.ok(cntConsignmentService.pageConsignmentBoxList(consignmentQuery));
    }

    @PostMapping("/businessConsignment")
    @ApiOperation("寄售市场进行交易")
    public R businessConsignment(){

        return R.ok();
    }


    @PostMapping("/consignmentPageOrder")
    @ApiOperation(value = "寄售订单",notes = "寄售方的订单\n买方还是到我的订单流程中！！！")
    public R consignmentPageOrder(){


        return R.ok();
    }

}

