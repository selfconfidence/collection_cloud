package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntPostSellVo;
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
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.service.ICntPostSellService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置可以购买Controller
 *
 * @author yanwei
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/sell")
@Api(tags = "提前购配置可以购买apis")
public class CntPostSellController extends BaseController
{
    @Autowired
    private ICntPostSellService cntPostSellService;

    /**
     * 查询提前购配置可以购买列表
     */
    //@RequiresPermissions("admin:sell:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购配置可以购买列表")
    public TableDataInfo<CntPostSellVo> list()
    {
        startPage();
        List<CntPostSellVo> list = cntPostSellService.selectCntPostSellList(new CntPostSell());
        return getDataTable(list);
    }

    /**
     * 获取提前购配置可以购买详细信息
     */
    //@RequiresPermissions("admin:sell:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取提前购配置可以购买详细信息")
    public R<CntPostSell> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntPostSellService.selectCntPostSellById(id));
    }

    /**
     * 新增提前购配置可以购买
     */
    //@RequiresPermissions("admin:sell:add")
    @Log(title = "提前购配置可以购买", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增提前购配置可以购买")
    public R add(@RequestBody CntPostSell cntPostSell)
    {
        return toResult(cntPostSellService.insertCntPostSell(cntPostSell));
    }

    /**
     * 修改提前购配置可以购买
     */
    //@RequiresPermissions("admin:sell:edit")
    @Log(title = "提前购配置可以购买", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改提前购配置可以购买")
    public R edit(@RequestBody CntPostSell cntPostSell)
    {
        return toResult(cntPostSellService.updateCntPostSell(cntPostSell));
    }

    /**
     * 删除提前购配置可以购买
     */
    //@RequiresPermissions("admin:sell:remove")
    @Log(title = "提前购配置可以购买", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除提前购配置可以购买")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntPostSellService.deleteCntPostSellByIds(ids));
    }
}
