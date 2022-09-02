package com.manyun.business.controller;


import com.manyun.business.service.ISystemService;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.annotation.InnerAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.*;

/**
 * <p>
 * 平台规则表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/system")
@Api(tags = "部分固定内容APis")
public class SystemController {

    @Autowired
    private ISystemService systemService;


    @GetMapping("/sellInfo")
    @ApiOperation("购买须知")
    public R<String>  sellInfo(){
        return R.ok(systemService.getVal(SELL_INFO,String.class));
    }



    @GetMapping("/collectionInfo")
    @ApiOperation("关于藏品")
    public R<String>  collectionInfo(){
        return R.ok(systemService.getVal(COLLECTION_INFO,String.class));
    }

    @GetMapping("/findType/{type}")
    @ApiOperation(value = "根据类型查询对应得业务规则",hidden = true)
    @InnerAuth
    public R<String> findType(@PathVariable String type){
        return R.ok(systemService.getVal(type,String.class));
    }

    @GetMapping("/openBoxGif")
    @ApiOperation("开盲盒动效图")
    public R<String> openBoxGif() {
        return R.ok(systemService.getVal(OPEN_BOX_GIF, String.class));
    }

    @GetMapping("/synthesisGif")
    @ApiOperation("合成动效图")
    public R<String> synthesisGif() {
        return R.ok(systemService.getVal(SYNTHESIS_GIF, String.class));
    }

}

