package com.manyun.business.controller;


import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.context.SecurityContextHolder;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 轮播表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "首页轮播相关")
public class BannerController {


    @GetMapping("/list")
    @ApiOperation("查询列表")
    public R list(){
        //LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok();
    }
}

