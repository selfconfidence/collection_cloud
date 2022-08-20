package com.manyun.business.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Agreement;
import com.manyun.business.domain.vo.AgreementVo;
import com.manyun.business.service.IAgreementService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 协议相关 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/agreement")
@Api(tags = "协议相关api")
public class AgreementController {

    @Autowired
    private IAgreementService agreementService;

    @GetMapping("/info/{type}")
    @ApiOperation(value = "协议等一些信息",notes = "(type = 1 用户协议，type = 2 关于我们,type = 3 隐私协议, type = 4,推广规则,type = 5,邀请规则)")
    public R<AgreementVo> info(@PathVariable Integer type){
        Agreement serviceOne = agreementService.getOne(Wrappers.<Agreement>lambdaQuery().eq(Agreement::getAgreementType, type));
        AgreementVo agreementVo = Builder.of(AgreementVo::new).build();
        BeanUtil.copyProperties(serviceOne,agreementVo);
        return R.ok(agreementVo);
    }


}

