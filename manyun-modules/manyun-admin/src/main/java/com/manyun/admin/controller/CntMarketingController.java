package com.manyun.admin.controller;

import java.util.List;
import java.io.IOException;
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
import com.manyun.admin.domain.CntMarketing;
import com.manyun.admin.service.ICntMarketingService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/marketing")
@Api(tags = "营销配置apis")
public class CntMarketingController extends BaseController
{
    @Autowired
    private ICntMarketingService cntMarketingService;

    //@RequiresPermissions("admin:marketing:list")
    @GetMapping("/list")
    @ApiOperation("查询营销配置列表")
    public TableDataInfo<CntMarketing> list(CntMarketing cntMarketing)
    {
        startPage();
        List<CntMarketing> list = cntMarketingService.selectCntMarketingList(cntMarketing);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:marketing:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取营销配置详细信息")
    public R<CntMarketing> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntMarketingService.selectCntMarketingById(id));
    }

    //@RequiresPermissions("admin:marketing:add")
    @Log(title = "营销配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增营销配置")
    public R add(@RequestBody CntMarketing cntMarketing)
    {
        return toResult(cntMarketingService.insertCntMarketing(cntMarketing));
    }

    //@RequiresPermissions("admin:marketing:edit")
    @Log(title = "营销配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改营销配置")
    public R edit(@RequestBody CntMarketing cntMarketing)
    {
        return toResult(cntMarketingService.updateCntMarketing(cntMarketing));
    }

    //@RequiresPermissions("admin:marketing:remove")
    @Log(title = "营销配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除营销配置")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntMarketingService.deleteCntMarketingByIds(ids));
    }
}
