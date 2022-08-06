package com.manyun.business.controller;


import com.manyun.business.domain.vo.MsgVo;
import com.manyun.business.service.IMsgService;
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

/**
 * <p>
 * 用户消息 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "消息相关Apis")
public class MsgController {

    @Autowired
    private IMsgService msgService;


    @PostMapping("/pageMsgList")
    @ApiOperation(value = "分页查询首页系统消息",notes = "类似 xxx 买了藏品")
    public R<TableDataInfo<MsgVo>>  pageMsgList(@RequestBody PageQuery pageQuery){
       return R.ok(msgService.pageMsgList(pageQuery));
    }


    @PostMapping("/pageMsgThisList")
    @ApiOperation("分页查询自己得系统消息")
    public R<TableDataInfo<MsgVo>> pageMsgThisList(@RequestBody PageQuery pageQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(msgService.pageMsgThisList(notNullLoginBusinessUser.getUserId(),pageQuery));
    }




}

