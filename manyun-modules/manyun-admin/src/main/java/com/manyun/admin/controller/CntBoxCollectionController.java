package com.manyun.admin.controller;

import java.util.List;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.admin.domain.vo.BoxCollectionDictVo;
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
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.service.ICntBoxCollectionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/boxCollection")
@Api(tags = "盲盒中的物品对象")
public class CntBoxCollectionController extends BaseController
{
    @Autowired
    private ICntBoxCollectionService cntBoxCollectionService;

    //@RequiresPermissions("admin:collection:list")
    @GetMapping("/list")
    @ApiOperation("查询盲盒中的物品对象")
    public TableDataInfo<CntBoxCollectionVo> list(CntBoxCollection cntBoxCollection)
    {
        startPage();
        List<CntBoxCollectionVo> list = cntBoxCollectionService.selectCntBoxCollectionList(cntBoxCollection);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:collection:add")
    @Log(title = "新增盲盒中的物品对象", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增盲盒中的物品对象")
    public R add(@RequestBody List<CntBoxCollection> cntBoxCollectionList)
    {
        return toResult(cntBoxCollectionService.insertCntBoxCollection(cntBoxCollectionList));
    }

    //@RequiresPermissions("admin:collection:edit")
    @Log(title = "修改盲盒中的物品对象", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改盲盒中的物品对象")
    public R edit(@RequestBody List<CntBoxCollection> cntBoxCollectionList)
    {
        return toResult(cntBoxCollectionService.updateCntBoxCollection(cntBoxCollectionList));
    }

    //@RequiresPermissions("admin:collection:remove")
    @Log(title = "删除盲盒中的物品对象", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除盲盒中的物品对象")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntBoxCollectionService.deleteCntBoxCollectionByIds(ids));
    }


    @GetMapping("/boxCollectionDict")
    @ApiOperation("查询藏品下拉框")
    public TableDataInfo<BoxCollectionDictVo> boxCollectionDict()
    {
        startPage();
        List<BoxCollectionDictVo> list = cntBoxCollectionService.boxCollectionDict();
        return getDataTable(list);
    }

}
