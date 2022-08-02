package com.manyun.admin.controller;


import com.manyun.admin.domain.query.ActionRecordQuery;
import com.manyun.admin.domain.vo.CntActionRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.admin.service.ICntActionRecordService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成记录Controller
 *
 * @author yanwei
 * @date 2022-07-21
 */
@RestController
@RequestMapping("/record")
@Api(tags = "活动合成记录apis")
public class CntActionRecordController extends BaseController
{
    @Autowired
    private ICntActionRecordService cntActionRecordService;

    /**
     * 查询活动合成记录列表
     */
    //@RequiresPermissions("admin:record:list")
    @GetMapping("/list")
    @ApiOperation("查询活动合成记录列表")
    public TableDataInfo<CntActionRecordVo> list(ActionRecordQuery recordQuery)
    {
        return cntActionRecordService.selectCntActionRecordList(recordQuery);
    }
}
