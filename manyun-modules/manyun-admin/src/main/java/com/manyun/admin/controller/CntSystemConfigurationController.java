package com.manyun.admin.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
import com.manyun.admin.domain.CntSystemConfiguration;
import com.manyun.admin.service.ICntSystemConfigurationService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/configuration")
@Api(tags = "系统配置apis")
public class CntSystemConfigurationController extends BaseController
{
    @Autowired
    private ICntSystemConfigurationService cntSystemConfigurationService;

    //@RequiresPermissions("admin:configuration:list")
    @GetMapping("/list")
    @ApiOperation("查询系统配置列表")
    public TableDataInfo<CntSystemConfiguration> list(CntSystemConfiguration cntSystemConfiguration)
    {
        startPage();
        List<CntSystemConfiguration> list = cntSystemConfigurationService.selectCntSystemConfigurationList(cntSystemConfiguration);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:configuration:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取系统配置详细信息")
    public R<CntSystemConfiguration> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntSystemConfigurationService.selectCntSystemConfigurationById(id));
    }

    //@RequiresPermissions("admin:configuration:add")
    @Log(title = "系统配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增系统配置")
    public R add(@RequestBody CntSystemConfiguration cntSystemConfiguration)
    {
        return toResult(cntSystemConfigurationService.insertCntSystemConfiguration(cntSystemConfiguration));
    }

    //@RequiresPermissions("admin:configuration:edit")
    @Log(title = "系统配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改系统配置")
    public R edit(@RequestBody CntSystemConfiguration cntSystemConfiguration)
    {
        return toResult(cntSystemConfigurationService.updateCntSystemConfiguration(cntSystemConfiguration));
    }

    //@RequiresPermissions("admin:configuration:remove")
    @Log(title = "系统配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除系统配置")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntSystemConfigurationService.deleteCntSystemConfigurationByIds(ids));
    }
}
