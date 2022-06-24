package com.manyun.business.controller;


import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.vo.BoxListVo;
import com.manyun.business.domain.vo.BoxVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.domain.vo.UserBoxVo;
import com.manyun.business.service.IBoxService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

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



    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询盲盒完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    public R<List<String>> queryDict(@PathVariable String keyword){
        return R.ok(boxService.queryDict(keyword));
    }


    @PostMapping("/pageList")
    @ApiOperation("分页查询盲盒列表")
    public R<TableDataInfo<BoxListVo>>  pageList(@RequestBody BoxQuery boxQuery){
        return R.ok(boxService.pageList(boxQuery));
    }


    @GetMapping("/info/{id}")
    @ApiOperation("根据盲盒编号,查询盲盒的详细信息")
    public R<BoxVo> info(@PathVariable String id){
        return R.ok(boxService.info(id));
    }

    @PostMapping("/sellBox")
    @ApiOperation("购买普通盲盒")
    public R<PayVo> sellBox(@Valid @RequestBody BoxSellForm boxSellForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.sellBox(boxSellForm,notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/userBoxPageList")
    @ApiOperation("查询用户的盲盒列表")
    public R<TableDataInfo<UserBoxVo>> userBoxPageList(@RequestBody PageQuery pageQuery){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.userBoxPageList(pageQuery,loginBusinessUser.getUserId()));
    }

    @GetMapping("/openBox/{boxId}")
    @ApiOperation(value = "开启盲盒",notes = "盲盒编号  点击后, 返回的 data 会弹出对应的提示信息给用户即可.")
    public R<String> openBox(@PathVariable String boxId){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(boxService.openBox(boxId,notNullLoginBusinessUser.getUserId()));
    }



}

