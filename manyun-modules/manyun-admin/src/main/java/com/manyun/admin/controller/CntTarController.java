package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntTarVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
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
import com.manyun.admin.domain.CntTar;
import com.manyun.admin.service.ICntTarService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 抽签规则(盲盒,藏品)Controller
 *
 * @author yanwei
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/tar")
@Api(tags = "抽签规则(盲盒,藏品)apis")
public class CntTarController extends BaseController
{
    @Autowired
    private ICntTarService cntTarService;

    /**
     * 查询抽签规则(盲盒,藏品)列表
     */
    //@RequiresPermissions("admin:tar:list")
    @GetMapping("/list")
    @ApiOperation("查询抽签规则(盲盒,藏品)列表")
    public TableDataInfo<CntTarVo> list(PageQuery pageQuery)
    {
        return cntTarService.selectCntTarList(pageQuery);
    }

    /**
     * 获取抽签规则(盲盒,藏品)详细信息
     */
    //@RequiresPermissions("admin:tar:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取抽签规则(盲盒,藏品)详细信息")
    public R<CntTar> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntTarService.selectCntTarById(id));
    }

    /**
     * 新增抽签规则(盲盒,藏品)
     */
    //@RequiresPermissions("admin:tar:add")
    @Log(title = "抽签规则(盲盒,藏品)", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增抽签规则(盲盒,藏品)")
    public R add(@RequestBody CntTar cntTar)
    {
        return toResult(cntTarService.insertCntTar(cntTar));
    }

    /**
     * 修改抽签规则(盲盒,藏品)
     */
    //@RequiresPermissions("admin:tar:edit")
    @Log(title = "抽签规则(盲盒,藏品)", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改抽签规则(盲盒,藏品)")
    public R edit(@RequestBody CntTar cntTar)
    {
        return toResult(cntTarService.updateCntTar(cntTar));
    }

    /**
     * 删除抽签规则(盲盒,藏品)
     */
    //@RequiresPermissions("admin:tar:remove")
    @Log(title = "抽签规则(盲盒,藏品)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除抽签规则(盲盒,藏品)")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntTarService.deleteCntTarByIds(ids));
    }
}
