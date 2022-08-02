package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntPostExistVo;
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
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.service.ICntPostExistService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置已经拥有Controller
 *
 * @author yanwei
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/exist")
@Api(tags = "提前购配置已经拥有apis")
public class CntPostExistController extends BaseController
{
    @Autowired
    private ICntPostExistService cntPostExistService;

    /**
     * 查询提前购配置已经拥有列表
     */
    //@RequiresPermissions("admin:exist:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购配置已经拥有列表")
    public TableDataInfo<CntPostExistVo> list(PageQuery pageQuery)
    {
        return cntPostExistService.selectCntPostExistList(pageQuery);
    }

    /**
     * 获取提前购配置已经拥有详细信息
     */
    //@RequiresPermissions("admin:exist:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取提前购配置已经拥有详细信息")
    public R<CntPostExist> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntPostExistService.selectCntPostExistById(id));
    }

    /**
     * 新增提前购配置已经拥有
     */
    //@RequiresPermissions("admin:exist:add")
    @Log(title = "提前购配置已经拥有", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提前购配置已经拥有")
    public R add(@RequestBody CntPostExist cntPostExist)
    {
        return toResult(cntPostExistService.insertCntPostExist(cntPostExist));
    }

    /**
     * 修改提前购配置已经拥有
     */
    //@RequiresPermissions("admin:exist:edit")
    @Log(title = "提前购配置已经拥有", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改提前购配置已经拥有")
    public R edit(@RequestBody CntPostExist cntPostExist)
    {
        return toResult(cntPostExistService.updateCntPostExist(cntPostExist));
    }

    /**
     * 删除提前购配置已经拥有
     */
    //@RequiresPermissions("admin:exist:remove")
    @Log(title = "提前购配置已经拥有", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除提前购配置已经拥有")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntPostExistService.deleteCntPostExistByIds(ids));
    }
}
