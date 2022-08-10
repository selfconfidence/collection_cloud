package com.manyun.admin.controller;


import com.manyun.admin.domain.query.IssuanceQuery;
import com.manyun.admin.domain.vo.CnfIssuanceVo;
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
import com.manyun.admin.domain.CnfIssuance;
import com.manyun.admin.service.ICnfIssuanceService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 发行方Controller
 *
 * @author yanwei
 * @date 2022-08-09
 */
@RestController
@RequestMapping("/issuance")
@Api(tags = "发行方apis")
public class CnfIssuanceController extends BaseController
{
    @Autowired
    private ICnfIssuanceService cnfIssuanceService;

    /**
     * 查询发行方列表
     */
    //@RequiresPermissions("admin:issuance:list")
    @GetMapping("/list")
    @ApiOperation("查询发行方列表")
    public TableDataInfo<CnfIssuanceVo> list(IssuanceQuery issuanceQuery)
    {
        return cnfIssuanceService.selectCnfIssuanceList(issuanceQuery);
    }

    /**
     * 获取发行方详细信息
     */
    //@RequiresPermissions("admin:issuance:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取发行方详细信息")
    public R<CnfIssuance> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cnfIssuanceService.selectCnfIssuanceById(id));
    }

    /**
     * 新增发行方
     */
    //@RequiresPermissions("admin:issuance:add")
    @Log(title = "发行方", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增发行方")
    public R add(@RequestBody CnfIssuance cnfIssuance)
    {
        return toResult(cnfIssuanceService.insertCnfIssuance(cnfIssuance));
    }

    /**
     * 修改发行方
     */
    //@RequiresPermissions("admin:issuance:edit")
    @Log(title = "发行方", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改发行方")
    public R edit(@RequestBody CnfIssuance cnfIssuance)
    {
        return toResult(cnfIssuanceService.updateCnfIssuance(cnfIssuance));
    }

    /**
     * 删除发行方
     */
    //@RequiresPermissions("admin:issuance:remove")
    @Log(title = "发行方", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除发行方")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cnfIssuanceService.deleteCnfIssuanceByIds(ids));
    }
}
