package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.query.SystemQuery;
import com.manyun.admin.domain.vo.CntSystemVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntSystem;
import com.manyun.admin.service.ICntSystemService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 平台规则Controller
 *
 * @author yanwei
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/cntSystem")
@Api(tags = "平台规则apis")
public class CntSystemController extends BaseController
{
    @Autowired
    private ICntSystemService cntSystemService;

    /**
     * 查询平台规则列表
     */
    //@RequiresPermissions("admin:system:list")
    @GetMapping("/list")
    @ApiOperation("查询平台规则列表")
    public TableDataInfo<CntSystemVo> list(SystemQuery SystemQuery)
    {
        startPage();
        List<CntSystemVo> list = cntSystemService.selectCntSystemList(SystemQuery);
        return getDataTable(list);
    }

    /**
     * 获取平台规则详细信息
     */
    //@RequiresPermissions("admin:system:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取平台规则详细信息")
    public R<CntSystemVo> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntSystemService.selectCntSystemById(id));
    }

    /**
     * 修改平台规则
     */
    //@RequiresPermissions("admin:system:edit")
    @Log(title = "平台规则", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改平台规则")
    public R edit(@RequestBody CntSystemVo cntSystemVo)
    {
        return toResult(cntSystemService.updateCntSystem(cntSystemVo));
    }

}
