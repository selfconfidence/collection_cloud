package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntActionRecordVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntActionRecord;
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
    public TableDataInfo<CntActionRecordVo> list(CntActionRecord cntActionRecord)
    {
        startPage();
        List<CntActionRecordVo> list = cntActionRecordService.selectCntActionRecordList(cntActionRecord);
        return getDataTable(list);
    }

    /**
     * 获取活动合成记录详细信息
     */
    //@RequiresPermissions("admin:record:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取活动合成记录详细信息")
    public R<CntActionRecord> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntActionRecordService.selectCntActionRecordById(id));
    }

    /**
     * 新增活动合成记录
     */
    @RequiresPermissions("admin:record:add")
    @Log(title = "活动合成记录", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增活动合成记录")
    public R add(@RequestBody CntActionRecord cntActionRecord)
    {
        return toResult(cntActionRecordService.insertCntActionRecord(cntActionRecord));
    }

    /**
     * 修改活动合成记录
     */
    @RequiresPermissions("admin:record:edit")
    @Log(title = "活动合成记录", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改活动合成记录")
    public R edit(@RequestBody CntActionRecord cntActionRecord)
    {
        return toResult(cntActionRecordService.updateCntActionRecord(cntActionRecord));
    }

    /**
     * 删除活动合成记录
     */
    @RequiresPermissions("admin:record:remove")
    @Log(title = "活动合成记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除活动合成记录")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntActionRecordService.deleteCntActionRecordByIds(ids));
    }
}
