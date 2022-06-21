package com.manyun.business.controller;


import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.BoxListVo;
import com.manyun.business.domain.vo.BoxVo;
import com.manyun.business.service.IBoxService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;

/**
 * <p>
 * 盲盒;盲盒主体表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/box")
@Api(tags = "盲盒相关apis")
public class BoxController extends BaseController {

    @Autowired
    private IBoxService boxService;


    @PostMapping("/pageList")
    @ApiOperation("分页查询盲盒列表")
    public R<TableDataInfo<BoxListVo>>  pageList(@RequestBody BoxQuery boxQuery){
        return R.ok(getDataTable(boxService.pageList(boxQuery)));
    }


    @GetMapping("/info/{id}")
    @ApiOperation("根据盲盒编号,查询盲盒的详细信息")
    public R<BoxVo> info(@PathVariable String id){
        return R.ok(boxService.info(id));
    }

    @PostMapping("/sellBox")
    @ApiOperation("购买普通盲盒")
    public R sellBox(@Valid @RequestBody BoxSellForm boxSellForm){

        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        boxService.sellBox(boxSellForm,notNullLoginBusinessUser.getUserId());
        return R.ok();
    }



}

