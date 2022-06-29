package com.manyun.business.controller;


import com.manyun.business.domain.vo.VersionVo;
import com.manyun.business.service.IVersionService;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 版本表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@RestController
@RequestMapping("/version")
@Api(tags = "版本相关Apis")
public class VersionController {

    @Autowired
    private  IVersionService versionService;
    @GetMapping("/newVersion/{isType}")
    @ApiOperation(value = "最新版本",notes = "类型;（安卓 1 ,ios 2）")
    public R<VersionVo> newVersion(@PathVariable Integer isType){
        VersionVo versionVo =  versionService.newVersion(isType);
        return R.ok(versionVo);
    }

    @GetMapping("/allVersionList/{isType}")
    @ApiOperation(value = "查询所有历史更新记录",notes = "类型;（安卓 1 ,ios 2）")
    public R<List<VersionVo>>  allVersionList(@PathVariable Integer isType){
        return R.ok(versionService.allVersionList(isType));
    }




}

