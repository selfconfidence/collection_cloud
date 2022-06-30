package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.vo.SynthesisVo;
import com.manyun.business.domain.vo.SyntheticRecordVo;
import com.manyun.business.service.IActionService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
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
public class ActionController {

    @Autowired
    private IActionService actionService;

    @PostMapping("/synthesisList")
    @ApiOperation("查询合成列表")
    public R<TableDataInfo<SynthesisVo>> synthesisList(@RequestBody PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        return R.ok(actionService.synthesisList());
    }

    @PostMapping("/syntheticRecordList")
    @ApiOperation("查询合成记录列表")
    public R<TableDataInfo<SyntheticRecordVo>> syntheticRecordList(@RequestBody PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        return R.ok(actionService.syntheticRecordList());
    }

}

