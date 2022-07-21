package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.*;
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
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/collection")
@Api(tags = "藏品管理apis")
public class CntCollectionController extends BaseController
{
    @Autowired
    private ICntCollectionService cntCollectionService;

    //@RequiresPermissions("admin:collection:list")
    @GetMapping("/list")
    @ApiOperation("查询藏品管理列表")
    public TableDataInfo list(CntCollection cntCollection)
    {
        startPage();
        List<CntCollectionVo> list = cntCollectionService.selectCntCollectionList(cntCollection);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:collection:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取藏品管理详细信息")
    public AjaxResult getInfo(@PathVariable("id")  String id)
    {
        return AjaxResult.success(cntCollectionService.selectCntCollectionById(id));
    }

    //@RequiresPermissions("admin:collection:add")
    @Log(title = "新增藏品管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增藏品管理")
    public AjaxResult add(@RequestBody CntCollectionAlterCombineVo collectionAlterCombineVo)
    {
        return toAjax(cntCollectionService.insertCntCollection(collectionAlterCombineVo));
    }

    //@RequiresPermissions("admin:collection:edit")
    @Log(title = "修改藏品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改藏品管理")
    public AjaxResult edit(@RequestBody CntCollectionAlterCombineVo collectionAlterCombineVo)
    {
        return toAjax(cntCollectionService.updateCntCollection(collectionAlterCombineVo));
    }

    //@RequiresPermissions("admin:collection:remove")
    @Log(title = "删除藏品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除藏品管理")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(cntCollectionService.deleteCntCollectionByIds(ids));
    }


    @GetMapping("/collectionCateDict")
    @ApiOperation("查询藏品分类下拉框")
    public TableDataInfo collectionCateDict()
    {
        startPage();
        List<CollectionCateDictVo> list = cntCollectionService.collectionCateDict();
        return getDataTable(list);
    }

    @GetMapping("/collectionCreationdDict")
    @ApiOperation("查询藏品创作者下拉框")
    public TableDataInfo collectionCreationdDict()
    {
        startPage();
        List<CollectionCreationdDictVo> list = cntCollectionService.collectionCreationdDict();
        return getDataTable(list);
    }

    @GetMapping("/collectionLableDict")
    @ApiOperation("查询藏品标签下拉框")
    public TableDataInfo collectionLableDict()
    {
        startPage();
        List<CollectionLableDictVo> list = cntCollectionService.collectionLableDict();
        return getDataTable(list);
    }

}
