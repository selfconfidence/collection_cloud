package com.manyun.demo.controller;


import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测试表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-14
 */
@RestController
@RequestMapping("/demo/tbBui")
@Api("业务模块相关apis(web)")
public class TbBuiController {

    @GetMapping("/list")
    public R list(){
        return R.ok();
    }


}

