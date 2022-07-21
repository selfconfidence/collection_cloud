package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntCollectionInfoVo;
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
import com.manyun.admin.domain.CntCollectionInfo;
import com.manyun.admin.service.ICntCollectionInfoService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/collectionInfo")
@Api(tags = "发行方apis")
public class CntCollectionInfoController extends BaseController
{
    @Autowired
    private ICntCollectionInfoService cntCollectionInfoService;

    //@RequiresPermissions("admin:info:list")
    @GetMapping("/list")
    @ApiOperation("查询发行方列表")
    public TableDataInfo<CntCollectionInfoVo> list(CntCollectionInfo cntCollectionInfo)
    {
        startPage();
        List<CntCollectionInfoVo> list = cntCollectionInfoService.selectCntCollectionInfoList(cntCollectionInfo);
        return getDataTable(list);
    }


    //@RequiresPermissions("admin:info:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取发行方详细信息")
    public R<CntCollectionInfo> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntCollectionInfoService.selectCntCollectionInfoById(id));
    }

    //@RequiresPermissions("admin:info:add")
    @Log(title = "新增发行方详情", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增发行方详情")
    public R add(@RequestBody CntCollectionInfo cntCollectionInfo)
    {
        return toResult(cntCollectionInfoService.insertCntCollectionInfo(cntCollectionInfo));
    }

    //@RequiresPermissions("admin:info:edit")
    @Log(title = "修改发行方详情", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改发行方详情")
    public R edit(@RequestBody CntCollectionInfo cntCollectionInfo)
    {
        return toResult(cntCollectionInfoService.updateCntCollectionInfo(cntCollectionInfo));
    }

   //@RequiresPermissions("admin:info:remove")
    @Log(title = "删除发行方详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除发行方详情")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntCollectionInfoService.deleteCntCollectionInfoByIds(ids));
    }
}
