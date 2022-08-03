package com.manyun.admin.controller;


import com.manyun.admin.domain.vo.CntCustomerServiceVo;
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
import com.manyun.admin.domain.CntCustomerService;
import com.manyun.admin.service.ICntCustomerServiceService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 客服Controller
 *
 * @author yanwei
 * @date 2022-07-21
 */
@RestController
@RequestMapping("/service")
@Api(tags = "客服apis")
public class CntCustomerServiceController extends BaseController
{
    @Autowired
    private ICntCustomerServiceService cntCustomerServiceService;

    /**
     * 查询客服列表
     */
    //@RequiresPermissions("admin:service:list")
    @GetMapping("/list")
    @ApiOperation("查询客服列表")
    public TableDataInfo<CntCustomerServiceVo> list(PageQuery pageQuery)
    {
        return cntCustomerServiceService.selectCntCustomerServiceList(pageQuery);
    }

    /**
     * 获取客服详细信息
     */
   // @RequiresPermissions("admin:service:query")
    @GetMapping(value = "/{menuId}")
    @ApiOperation("获取客服详细信息")
    public R<CntCustomerService> getInfo(@PathVariable("menuId") Long menuId)
    {
        return R.ok(cntCustomerServiceService.selectCntCustomerServiceByMenuId(menuId));
    }

    /**
     * 新增客服
     */
    //@RequiresPermissions("admin:service:add")
    @Log(title = "客服", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增客服")
    public R add(@RequestBody CntCustomerService cntCustomerService)
    {
        return toResult(cntCustomerServiceService.insertCntCustomerService(cntCustomerService));
    }

    /**
     * 修改客服
     */
    //@RequiresPermissions("admin:service:edit")
    @Log(title = "客服", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改客服")
    public R edit(@RequestBody CntCustomerService cntCustomerService)
    {
        return toResult(cntCustomerServiceService.updateCntCustomerService(cntCustomerService));
    }

    /**
     * 删除客服
     */
    //@RequiresPermissions("admin:service:remove")
    @Log(title = "客服", businessType = BusinessType.DELETE)
	@DeleteMapping("/{menuIds}")
    @ApiOperation("删除客服")
    public R remove(@PathVariable Long[] menuIds)
    {
        return cntCustomerServiceService.deleteCntCustomerServiceByMenuIds(menuIds);
     }
}
