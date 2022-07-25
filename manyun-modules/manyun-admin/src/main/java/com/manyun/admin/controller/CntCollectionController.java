package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;
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
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.common.core.web.controller.BaseController;
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
    public TableDataInfo<CntCollectionVo> list(CollectionQuery collectionQuery)
    {
        startPage();
        List<CntCollectionVo> list = cntCollectionService.selectCntCollectionList(collectionQuery);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:collection:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取藏品管理详细信息")
    public R<CntCollectionDetailsVo> getInfo(@PathVariable("id")  String id)
    {
        return R.ok(cntCollectionService.selectCntCollectionById(id));
    }

    //@RequiresPermissions("admin:collection:add")
    @Log(title = "新增藏品管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增藏品管理")
    public R add(@RequestBody CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        return toResult(cntCollectionService.insertCntCollection(collectionAlterCombineDto));
    }

    //@RequiresPermissions("admin:collection:edit")
    @Log(title = "修改藏品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改藏品管理")
    public R edit(@RequestBody CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        return toResult(cntCollectionService.updateCntCollection(collectionAlterCombineDto));
    }

    //@RequiresPermissions("admin:collection:remove")
    @Log(title = "删除藏品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation("删除藏品管理")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntCollectionService.deleteCntCollectionByIds(ids));
    }

    @PostMapping("/airdrop")
    @ApiOperation("空投")
    public R airdrop(@RequestBody AirdropDto airdropDto)
    {
        return toResult(cntCollectionService.airdrop(airdropDto));
    }

}
