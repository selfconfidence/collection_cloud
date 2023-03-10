package com.manyun.business.controller;


import cn.hutool.core.lang.Assert;
import com.manyun.business.domain.form.TranAccForm;
import com.manyun.business.service.ISystemService;
import com.manyun.business.service.ITranService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-29
 */
@RestController
@RequestMapping("/tran")
@Api(tags = "转赠相关Apis")
public class TranController {

    @Autowired
    private ISystemService systemService;

    @Autowired
    private ITranService tranService;


    @GetMapping("/isTranOpen")
    @ApiOperation(value = "是否开启转赠",notes = "1开启,0关闭  字符串")
    public R<String> isTranOpen(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.TRAN_ACC, String.class));
    }

    @GetMapping("/tranInfo")
    @ApiOperation(value = "转赠说明")
    public R<String> tranInfo(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.TRAN_INFO, String.class));
    }
    @PostMapping("/tranTypeToPoint")
    @ApiOperation("进行转赠")
    @Lock("tranTypeToPoint")
    public R  tranTypeToPoint(@RequestBodyRsa @Valid TranAccForm tranAccForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Assert.isTrue("1".equals(systemService.getVal(BusinessConstants.SystemTypeConstant.TRAN_ACC, String.class)),"转赠未开启!");
        tranService.tranTypeToPoint(notNullLoginBusinessUser.getCntUser(),tranAccForm);
        return R.ok();
    }








}

