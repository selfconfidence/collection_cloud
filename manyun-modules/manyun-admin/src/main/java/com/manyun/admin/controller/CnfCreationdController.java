package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CnfCreationdVo;
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
import com.manyun.admin.domain.CnfCreationd;
import com.manyun.admin.service.ICnfCreationdService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/creationd")
@Api(tags = "创作者apis")
public class CnfCreationdController extends BaseController
{
    @Autowired
    private ICnfCreationdService cnfCreationdService;

    //@RequiresPermissions("admin:creationd:list")
    @GetMapping("/list")
    @ApiOperation("查询创作者列表")
    public TableDataInfo<CnfCreationdVo> list(CnfCreationd cnfCreationd)
    {
        startPage();
        List<CnfCreationdVo> list = cnfCreationdService.selectCnfCreationdList(cnfCreationd);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:creationd:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取创作者详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(cnfCreationdService.selectCnfCreationdById(id));
    }

    //@RequiresPermissions("admin:creationd:add")
    @Log(title = "创作者", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增创作者")
    public AjaxResult add(@RequestBody CnfCreationd cnfCreationd)
    {
        return toAjax(cnfCreationdService.insertCnfCreationd(cnfCreationd));
    }

    //@RequiresPermissions("admin:creationd:edit")
    @Log(title = "创作者", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改创作者")
    public AjaxResult edit(@RequestBody CnfCreationd cnfCreationd)
    {
        return toAjax(cnfCreationdService.updateCnfCreationd(cnfCreationd));
    }

    //@RequiresPermissions("admin:creationd:remove")
    @Log(title = "创作者", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除创作者")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(cnfCreationdService.deleteCnfCreationdByIds(ids));
    }
}
