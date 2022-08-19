package com.manyun.admin.controller;

import com.manyun.admin.domain.query.PostConfigQuery;
import com.manyun.admin.domain.vo.CntPostConfigVo;
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
import com.manyun.admin.domain.CntPostConfig;
import com.manyun.admin.service.ICntPostConfigService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置-只能有一条Controller
 *
 * @author yanwei
 * @date 2022-08-15
 */
@RestController
@RequestMapping("/postConfig")
@Api(tags = "提前购配置-只能有一条apis")
public class CntPostConfigController extends BaseController
{
    @Autowired
    private ICntPostConfigService cntPostConfigService;

    /**
     * 查询提前购配置-只能有一条列表
     */
    //@RequiresPermissions("admin:config:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购配置-只能有一条列表")
    public TableDataInfo<CntPostConfigVo> list(PostConfigQuery postConfigQuery)
    {
        return cntPostConfigService.selectCntPostConfigList(postConfigQuery);
    }

    /**
     * 获取提前购配置-只能有一条详细信息
     */
    //@RequiresPermissions("admin:config:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取提前购配置-只能有一条详细信息")
    public R<CntPostConfig> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntPostConfigService.selectCntPostConfigById(id));
    }

    /**
     * 新增提前购配置-只能有一条
     */
    //@RequiresPermissions("admin:config:add")
    @Log(title = "提前购配置-只能有一条", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提前购配置-只能有一条")
    public R add(@RequestBody CntPostConfig cntPostConfig)
    {
        return toResult(cntPostConfigService.insertCntPostConfig(cntPostConfig));
    }

    /**
     * 修改提前购配置-只能有一条
     */
    //@RequiresPermissions("admin:config:edit")
    @Log(title = "提前购配置-只能有一条", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改提前购配置-只能有一条")
    public R edit(@RequestBody CntPostConfig cntPostConfig)
    {
        return toResult(cntPostConfigService.updateCntPostConfig(cntPostConfig));
    }

    /**
     * 删除提前购配置-只能有一条
     */
    //@RequiresPermissions("admin:config:remove")
    @Log(title = "提前购配置-只能有一条", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除提前购配置-只能有一条")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntPostConfigService.deleteCntPostConfigByIds(ids));
    }
}
