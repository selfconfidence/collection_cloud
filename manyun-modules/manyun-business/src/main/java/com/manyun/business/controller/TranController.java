package com.manyun.business.controller;


import com.manyun.business.domain.form.TranAccForm;
import com.manyun.business.service.ISystemService;
import com.manyun.business.service.ITranService;
import com.manyun.comm.api.model.LoginBusinessUser;
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


    @PostMapping("/tranTypeToPoint")
    @ApiOperation("进行转赠")
    public synchronized R  tranTypeToPoint(@RequestBody @Valid TranAccForm tranAccForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        tranService.tranTypeToPoint(notNullLoginBusinessUser.getUserId(),tranAccForm);
        return R.ok();
    }




}

