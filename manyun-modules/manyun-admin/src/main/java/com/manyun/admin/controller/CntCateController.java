package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntCateVo;
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
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.service.ICntCateService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/cate")
@Api(tags = "藏品分类apis")
public class CntCateController extends BaseController
{
    @Autowired
    private ICntCateService cntCateService;

    //@RequiresPermissions("admin:cate:list")
    @GetMapping("/list")
    @ApiOperation("藏品分类列表")
    public TableDataInfo<CntCateVo> list(CntCate cntCate)
    {
        startPage();
        List<CntCateVo> list = cntCateService.selectCntCateList(cntCate);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:cate:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("藏品分类详细信息")
    public R<CntCate> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntCateService.selectCntCateById(id));
    }

    //@RequiresPermissions("admin:cate:add")
    @Log(title = "新增藏品分类", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增藏品分类")
    public R add(@RequestBody CntCate cntCate)
    {
        return toResult(cntCateService.insertCntCate(cntCate));
    }

    //@RequiresPermissions("admin:cate:edit")
    @Log(title = "修改藏品分类", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改藏品分类")
    public R edit(@RequestBody CntCate cntCate)
    {
        return toResult(cntCateService.updateCntCate(cntCate));
    }

    //@RequiresPermissions("admin:cate:remove")
    @Log(title = "删除藏品分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除藏品分类")
    public R remove(@PathVariable String[] ids)
    {
        return cntCateService.deleteCntCateByIds(ids);
    }
}
