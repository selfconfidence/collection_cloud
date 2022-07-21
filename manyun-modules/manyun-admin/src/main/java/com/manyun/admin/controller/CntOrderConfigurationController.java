package com.manyun.admin.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.manyun.admin.domain.CntOrderConfiguration;
import com.manyun.admin.service.ICntOrderConfigurationService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/orderConfiguration")
@Api(tags = "订单配置apis")
public class CntOrderConfigurationController extends BaseController
{
    @Autowired
    private ICntOrderConfigurationService cntOrderConfigurationService;

    //@RequiresPermissions("admin:configuration:list")
    @GetMapping("/list")
    @ApiOperation("查询订单配置列表")
    public TableDataInfo list(CntOrderConfiguration cntOrderConfiguration)
    {
        startPage();
        List<CntOrderConfiguration> list = cntOrderConfigurationService.selectCntOrderConfigurationList(cntOrderConfiguration);
        return getDataTable(list);
    }


    //@RequiresPermissions("admin:configuration:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取订单配置详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(cntOrderConfigurationService.selectCntOrderConfigurationById(id));
    }

    //@RequiresPermissions("admin:configuration:add")
    @Log(title = "订单配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增订单配置")
    public AjaxResult add(@RequestBody CntOrderConfiguration cntOrderConfiguration)
    {
        return toAjax(cntOrderConfigurationService.insertCntOrderConfiguration(cntOrderConfiguration));
    }

    //@RequiresPermissions("admin:configuration:edit")
    @Log(title = "订单配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改订单配置")
    public AjaxResult edit(@RequestBody CntOrderConfiguration cntOrderConfiguration)
    {
        return toAjax(cntOrderConfigurationService.updateCntOrderConfiguration(cntOrderConfiguration));
    }

    //@RequiresPermissions("admin:configuration:remove")
    @Log(title = "订单配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除订单配置")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(cntOrderConfigurationService.deleteCntOrderConfigurationByIds(ids));
    }
}
