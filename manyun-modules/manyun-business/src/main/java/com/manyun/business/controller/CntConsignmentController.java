package com.manyun.business.controller;
import cn.hutool.core.lang.Assert;
import com.manyun.business.domain.form.ConsignmentOrderSellForm;
import com.manyun.business.domain.form.ConsignmentSellForm;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentOrderQuery;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ICntConsignmentService;
import com.manyun.business.service.ISystemService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.XXutils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/openConsignmentList")
    public R<List<ConsignmentOpenListVo>> openConsignmentList(){

        return R.ok(cntConsignmentService.openConsignmentList());
    }


    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询寄售 藏品|盲盒 完整 标题信息",notes = "返回的都是 藏品|盲盒词条完整信息,已寄售状态的寄售业务名称")
    public R<List<KeywordVo>> queryDict(@PathVariable String keyword){
        return R.ok(cntConsignmentService.queryDict(keyword));
    }


    @PostMapping("/consignmentAssets")
    @ApiOperation("对资产进行寄售")
    @Lock("consignmentAssets")
    public R  consignmentAssets(@RequestBodyRsa @Valid UserConsignmentForm userConsignmentForm){
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
    public R<TableDataInfo<ConsignmentCollectionListVo>> pageConsignmentList(@RequestBodyRsa @Valid ConsignmentQuery consignmentQuery){
        //XXutils.dingxiangTokenCheck(consignmentQuery.getToken());
        return R.ok(cntConsignmentService.pageConsignmentList(consignmentQuery));
    }


    @PostMapping("/pageConsignmentBoxList")
    @ApiOperation(value = "分页查询寄售市场盲盒的信息",notes = "详情还是调用盲盒详情即可\n,用盲盒编号查详情\n购买的时候,按照寄售购买的流程进行！！！")
    public R<TableDataInfo<ConsignmentBoxListVo>> pageConsignmentBoxList(@RequestBodyRsa @Valid ConsignmentQuery consignmentQuery){
        //XXutils.dingxiangTokenCheck(consignmentQuery.getToken());
        return R.ok(cntConsignmentService.pageConsignmentBoxList(consignmentQuery));
    }

    @PostMapping("/businessConsignment")
    @ApiOperation(value = "寄售市场进行交易",notes = "对某个 资产 进行购买\n 此接口被废弃，属于一次调用支付 + 创建订单")
    @Deprecated
    public R<PayVo> businessConsignment(@RequestBody @Valid ConsignmentSellForm consignmentSellForm){
        Assert.isTrue(Boolean.FALSE,"接口已废弃!");
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String payUserId = notNullLoginBusinessUser.getUserId();
        PayVo payVo =  cntConsignmentService.businessConsignment(payUserId,consignmentSellForm);
        return R.ok(payVo);
    }


    @PostMapping("/consignmentCreateOrder")
    @ApiOperation(value = "购买寄售_预先_生成订单",notes = "用来预先 生成一个待支付订单,返回订单编号,用来二次提交支付\n version 1.0.1")
    @Lock("consignmentCreateOrder")
    public R<String>  consignmentCreateOrder(@RequestBodyRsa @Valid ConsignmentOrderSellForm consignmentOrderSellForm){
        //XXutils.dingxiangTokenCheck(consignmentOrderSellForm.getToken());
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String payUserId = notNullLoginBusinessUser.getUserId();
        return R.ok(cntConsignmentService.consignmentCreateOrder( payUserId,consignmentOrderSellForm));
     }



    @PostMapping("/consignmentPageOrder")
    @ApiOperation(value = "寄售订单列表",notes = "寄售方的订单\n买方还是到我的订单流程中！！！")
    public R<TableDataInfo<ConsignmentOrderVo>> consignmentPageOrder(@RequestBody ConsignmentOrderQuery consignmentOrderQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(cntConsignmentService.consignmentPageOrder(notNullLoginBusinessUser.getUserId(),consignmentOrderQuery));
    }


    @GetMapping("/cancelSchedulingConsignment")
    @ApiOperation("取消寄售市场中的资产")
    @InnerAuth
    public R cancelSchedulingConsignment(){
        cntConsignmentService.cancelSchedulingConsignment();
        return R.ok();
    }

    @GetMapping("/cancelConsignmentById/{id}")
    @ApiOperation(value = "寄售方取消寄售市场中的资产",notes = "id 为寄售订单的id")
    @Lock("cancelConsignmentById")
    public R cancelConsignmentById(@PathVariable String id){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        cntConsignmentService.cancelConsignmentById(id,notNullLoginBusinessUser.getUserId());
        return R.ok();
    }

    @GetMapping("/consignmentSuccess/{id}")
    @ApiOperation(value = "审核通过;id = 寄售编号",hidden = true)
    @InnerAuth
    @Lock("consignmentSuccess")
    public R  consignmentSuccess(@PathVariable(name = "id") String id){
        //cntConsignmentService.consignmentSuccess(id);
        return R.ok();
    }

}

