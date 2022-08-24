package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.query.AgreementTypeQuery;
import com.manyun.admin.domain.vo.CntAgreementTypeVo;
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
import com.manyun.admin.domain.CntAgreementType;
import com.manyun.admin.service.ICntAgreementTypeService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 协议类型Controller
 *
 * @author yanwei
 * @date 2022-08-23
 */
@RestController
@RequestMapping("/agreementType")
@Api(tags = "协议类型apis")
public class CntAgreementTypeController extends BaseController
{
    @Autowired
    private ICntAgreementTypeService cntAgreementTypeService;

    /**
     * 查询协议类型列表
     */
    @RequiresPermissions("admin:type:list")
    @GetMapping("/list")
    @ApiOperation("查询协议类型列表")
    public TableDataInfo<CntAgreementTypeVo> list(AgreementTypeQuery agreementTypeQuery)
    {
        return cntAgreementTypeService.selectCntAgreementTypeList(agreementTypeQuery);
    }

    /**
     * 获取协议类型详细信息
     */
    @RequiresPermissions("admin:type:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取协议类型详细信息")
    public R<CntAgreementType> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntAgreementTypeService.selectCntAgreementTypeById(id));
    }

    /**
     * 新增协议类型
     */
    @RequiresPermissions("admin:type:add")
    @Log(title = "协议类型", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增协议类型")
    public R add(@RequestBody CntAgreementType cntAgreementType)
    {
        return toResult(cntAgreementTypeService.insertCntAgreementType(cntAgreementType));
    }

    /**
     * 修改协议类型
     */
    @RequiresPermissions("admin:type:edit")
    @Log(title = "协议类型", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改协议类型")
    public R edit(@RequestBody CntAgreementType cntAgreementType)
    {
        return toResult(cntAgreementTypeService.updateCntAgreementType(cntAgreementType));
    }

    /**
     * 删除协议类型
     */
    @RequiresPermissions("admin:type:remove")
    @Log(title = "协议类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除协议类型")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntAgreementTypeService.deleteCntAgreementTypeByIds(ids));
    }
}
