package com.manyun.admin.controller;



import com.manyun.admin.domain.query.PleaseBoxQuery;
import com.manyun.admin.domain.vo.CntPleaseBoxVo;
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
import com.manyun.admin.domain.CntPleaseBox;
import com.manyun.admin.service.ICntPleaseBoxService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 邀请好友送盲盒规则Controller
 *
 * @author yanwei
 * @date 2022-08-06
 */
@RestController
@RequestMapping("/invite")
@Api(tags = "邀请好友送盲盒规则apis")
public class CntPleaseBoxController extends BaseController
{
    @Autowired
    private ICntPleaseBoxService cntPleaseBoxService;

    /**
     * 查询邀请好友送盲盒规则列表
     */
    @RequiresPermissions("admin:box:list")
    @GetMapping("/list")
    @ApiOperation("查询邀请好友送盲盒规则列表")
    public TableDataInfo<CntPleaseBoxVo> list(PleaseBoxQuery boxQuery)
    {
        return cntPleaseBoxService.selectCntPleaseBoxList(boxQuery);
    }

    /**
     * 获取邀请好友送盲盒规则详细信息
     */
    @RequiresPermissions("admin:box:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取邀请好友送盲盒规则详细信息")
    public R<CntPleaseBox> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntPleaseBoxService.selectCntPleaseBoxById(id));
    }

    /**
     * 新增邀请好友送盲盒规则
     */
    @RequiresPermissions("admin:box:add")
    @Log(title = "邀请好友送盲盒规则", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增邀请好友送盲盒规则")
    public R add(@RequestBody CntPleaseBox cntPleaseBox)
    {
        return toResult(cntPleaseBoxService.insertCntPleaseBox(cntPleaseBox));
    }

    /**
     * 修改邀请好友送盲盒规则
     */
    @RequiresPermissions("admin:box:edit")
    @Log(title = "邀请好友送盲盒规则", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改邀请好友送盲盒规则")
    public R edit(@RequestBody CntPleaseBox cntPleaseBox)
    {
        return toResult(cntPleaseBoxService.updateCntPleaseBox(cntPleaseBox));
    }

    /**
     * 删除邀请好友送盲盒规则
     */
    @RequiresPermissions("admin:box:remove")
    @Log(title = "邀请好友送盲盒规则", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除邀请好友送盲盒规则")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntPleaseBoxService.deleteCntPleaseBoxByIds(ids));
    }
}
