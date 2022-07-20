package com.manyun.admin.controller;

import java.util.List;
import java.io.IOException;
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
import com.manyun.admin.domain.CntWithdraw;
import com.manyun.admin.service.ICntWithdrawService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/withdraw")
@Api(tags = "提现配置apis")
public class CntWithdrawController extends BaseController
{
    @Autowired
    private ICntWithdrawService cntWithdrawService;

    //@RequiresPermissions("admin:withdraw:list")
    @GetMapping("/list")
    @ApiOperation("查询提现配置列表")
    public TableDataInfo list(CntWithdraw cntWithdraw)
    {
        startPage();
        List<CntWithdraw> list = cntWithdrawService.selectCntWithdrawList(cntWithdraw);
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:withdraw:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取提现配置详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(cntWithdrawService.selectCntWithdrawById(id));
    }

    //@RequiresPermissions("admin:withdraw:add")
    @Log(title = "提现配置", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提现配置")
    public AjaxResult add(@RequestBody CntWithdraw cntWithdraw)
    {
        return toAjax(cntWithdrawService.insertCntWithdraw(cntWithdraw));
    }

    //@RequiresPermissions("admin:withdraw:edit")
    @Log(title = "提现配置", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改提现配置")
    public AjaxResult edit(@RequestBody CntWithdraw cntWithdraw)
    {
        return toAjax(cntWithdrawService.updateCntWithdraw(cntWithdraw));
    }

    //@RequiresPermissions("admin:withdraw:remove")
    @Log(title = "提现配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除提现配置")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(cntWithdrawService.deleteCntWithdrawByIds(ids));
    }
}
