package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.query.ActionQuery;
import com.manyun.admin.domain.vo.CntActionVo;
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
import com.manyun.admin.domain.CntAction;
import com.manyun.admin.service.ICntActionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动Controller
 *
 * @author yanwei
 * @date 2022-07-21
 */
@RestController
@RequestMapping("/action")
@Api(tags = "活动apis")
public class CntActionController extends BaseController
{
    @Autowired
    private ICntActionService cntActionService;

    /**
     * 查询活动列表
     */
    //@RequiresPermissions("admin:action:list")
    @GetMapping("/list")
    @ApiOperation("查询活动列表")
    public TableDataInfo<CntActionVo> list(ActionQuery actionQuery)
    {
        startPage();
        List<CntActionVo> list = cntActionService.selectCntActionList(actionQuery);
        return getDataTable(list);
    }

    /**
     * 获取活动详细信息
     */
    //@RequiresPermissions("admin:action:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取活动详细信息")
    public R<CntAction> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntActionService.selectCntActionById(id));
    }

    /**
     * 新增活动
     */
    @RequiresPermissions("admin:action:add")
    @Log(title = "活动", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增活动")
    public R add(@RequestBody CntAction cntAction)
    {
        return toResult(cntActionService.insertCntAction(cntAction));
    }

    /**
     * 修改活动
     */
    @RequiresPermissions("admin:action:edit")
    @Log(title = "活动", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改活动")
    public R edit(@RequestBody CntAction cntAction)
    {
        return toResult(cntActionService.updateCntAction(cntAction));
    }

    /**
     * 删除活动
     */
    @RequiresPermissions("admin:action:remove")
    @Log(title = "活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除活动")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntActionService.deleteCntActionByIds(ids));
    }
}
