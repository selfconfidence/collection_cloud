package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.vo.SynthesisVo;
import com.manyun.business.domain.vo.SynthesizeNowVo;
import com.manyun.business.domain.vo.SyntheticActivityVo;
import com.manyun.business.domain.vo.SyntheticRecordVo;
import com.manyun.business.service.IActionService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 活动表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/action")
@Api(tags = "活动合成相关apis")
public class ActionController {

    @Autowired
    private IActionService actionService;

    @PostMapping("/syntheticActivityList")
    @ApiOperation("查询合成活动列表")
    public R<TableDataInfo<SyntheticActivityVo>> syntheticActivityList(@RequestBody PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        return R.ok(actionService.syntheticActivityList());
    }

    @PostMapping("/syntheticRecordList")
    @ApiOperation("查询合成记录列表")
    public R<TableDataInfo<SyntheticRecordVo>> syntheticRecordList(@RequestBody PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        return R.ok(actionService.syntheticRecordList());
    }

    @PostMapping("/synthesisInfo")
    @ApiOperation("查询合成详情")
    public R<SynthesisVo> synthesisInfo(String id) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return actionService.synthesisInfo(loginBusinessUser.getUserId(),id);
    }

    @PostMapping("/SynthesizeNow")
    @ApiOperation("立即合成")
    public R<SynthesizeNowVo> synthesizeNow(String id) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return actionService.synthesizeNow(loginBusinessUser.getUserId(),id);
    }

}

