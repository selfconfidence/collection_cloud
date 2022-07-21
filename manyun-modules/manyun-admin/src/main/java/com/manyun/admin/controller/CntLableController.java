package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntLableVo;
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
import com.manyun.admin.domain.CntLable;
import com.manyun.admin.service.ICntLableService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/lable")
@Api(tags = "标签管理apis")
public class CntLableController extends BaseController
{
    @Autowired
    private ICntLableService cntLableService;

    //@RequiresPermissions("admin:lable:list")
    @GetMapping("/list")
    @ApiOperation("查询标签管理列表")
    public TableDataInfo<CntLableVo> list(CntLable cntLable)
    {
        startPage();
        List<CntLableVo> list = cntLableService.selectCntLableList(cntLable);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:lable:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取标签管理详细信息")
    public R<CntLable> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntLableService.selectCntLableById(id));
    }

    //@RequiresPermissions("admin:lable:add")
    @Log(title = "新增标签管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增标签管理")
    public R add(@RequestBody CntLable cntLable)
    {
        return toResult(cntLableService.insertCntLable(cntLable));
    }

    //@RequiresPermissions("admin:lable:edit")
    @Log(title = "修改标签管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改标签管理")
    public R edit(@RequestBody CntLable cntLable)
    {
        return toResult(cntLableService.updateCntLable(cntLable));
    }

    //@RequiresPermissions("admin:lable:remove")
    @Log(title = "删除标签管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除标签管理")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntLableService.deleteCntLableByIds(ids));
    }
}
