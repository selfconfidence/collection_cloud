package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntBoxOrderVo;
import com.manyun.admin.domain.vo.CntBoxVo;
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
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/box")
@Api(tags = "盲盒apis")
public class CntBoxController extends BaseController
{
    @Autowired
    private ICntBoxService cntBoxService;

    //@RequiresPermissions("admin:box:list")
    @GetMapping("/list")
    @ApiOperation("盲盒列表")
    public TableDataInfo list(CntBox cntBox)
    {
        startPage();
        List<CntBoxVo> list = cntBoxService.selectCntBoxList(cntBox);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:box:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("盲盒详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(cntBoxService.selectCntBoxById(id));
    }

    //@RequiresPermissions("admin:box:add")
    @Log(title = "新增盲盒", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增盲盒")
    public AjaxResult add(@RequestBody CntBox cntBox)
    {
        return toAjax(cntBoxService.insertCntBox(cntBox));
    }

    //@RequiresPermissions("admin:box:edit")
    @Log(title = "修改盲盒", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改盲盒")
    public AjaxResult edit(@RequestBody CntBox cntBox)
    {
        return toAjax(cntBoxService.updateCntBox(cntBox));
    }

    //@RequiresPermissions("admin:box:remove")
    @Log(title = "删除盲盒", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除盲盒")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(cntBoxService.deleteCntBoxByIds(ids));
    }

    @GetMapping("/boxOrderList")
    @ApiOperation("盲盒订单列表")
    public TableDataInfo boxOrderList()
    {
        startPage();
        List<CntBoxOrderVo> list = cntBoxService.boxOrderList();
        return getDataTable(list);
    }

}
