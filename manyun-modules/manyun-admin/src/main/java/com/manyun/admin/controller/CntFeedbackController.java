package com.manyun.admin.controller;


import com.manyun.admin.domain.query.FeedbackQuery;
import com.manyun.admin.domain.vo.CntFeedbackVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.domain.CntFeedback;
import com.manyun.admin.service.ICntFeedbackService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * 产品举报反馈Controller
 *
 * @author yanwei
 * @date 2022-07-26
 */
@RestController
@RequestMapping("/feedback")
@Api(tags = "产品举报反馈apis")
public class CntFeedbackController extends BaseController
{
    @Autowired
    private ICntFeedbackService cntFeedbackService;

    /**
     * 查询产品举报反馈列表
     */
    //@RequiresPermissions("admin:feedback:list")
    @GetMapping("/list")
    @ApiOperation("查询产品举报反馈列表")
    public TableDataInfo<CntFeedbackVo> list(FeedbackQuery feedbackQuery)
    {
        return cntFeedbackService.selectCntFeedbackList(feedbackQuery);
    }

    /**
     * 修改产品举报反馈
     */
    //@RequiresPermissions("admin:feedback:edit")
    @Log(title = "产品举报反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改产品举报反馈")
    public R edit(@RequestBody CntFeedback cntFeedback)
    {
        return toResult(cntFeedbackService.updateCntFeedback(cntFeedback));
    }

}
