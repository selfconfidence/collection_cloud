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
import com.manyun.admin.domain.CntAgreement;
import com.manyun.admin.service.ICntAgreementService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/agreement")
@Api(tags = "协议相关apis")
public class CntAgreementController extends BaseController
{
    @Autowired
    private ICntAgreementService cntAgreementService;

    //@RequiresPermissions("admin:agreement:list")
    @GetMapping("/list")
    @ApiOperation("查询协议相关列表")
    public TableDataInfo<CntAgreement> list()
    {
        startPage();
        List<CntAgreement> list = cntAgreementService.selectCntAgreementList(new CntAgreement());
        return getDataTable(list);
    }

    //@RequiresPermissions("admin:agreement:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取协议相关详细信息")
    public R<CntAgreement> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntAgreementService.selectCntAgreementById(id));
    }

    //@RequiresPermissions("admin:agreement:add")
    @Log(title = "协议相关", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增协议相关")
    public R add(@RequestBody CntAgreement cntAgreement)
    {
        return toResult(cntAgreementService.insertCntAgreement(cntAgreement));
    }

    //@RequiresPermissions("admin:agreement:edit")
    @Log(title = "协议相关", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改协议相关")
    public R edit(@RequestBody CntAgreement cntAgreement)
    {
        return toResult(cntAgreementService.updateCntAgreement(cntAgreement));
    }

    //@RequiresPermissions("admin:agreement:remove")
    @Log(title = "协议相关", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除协议相关")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntAgreementService.deleteCntAgreementByIds(ids));
    }
}
