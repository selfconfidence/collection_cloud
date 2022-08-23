package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.dto.MyCollectionDto;
import com.manyun.admin.domain.dto.MyOrderDto;
import com.manyun.admin.domain.dto.UpdateBalanceDto;
import com.manyun.admin.domain.excel.UserPhoneExcel;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.UserBoxVo;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.comm.api.domain.SysUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.service.ICntUserService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/userMoney")
@Api(tags = "用户管理apis")
public class CntUserController extends BaseController
{
    @Autowired
    private ICntUserService cntUserService;

    //@RequiresPermissions("admin:user:list")
    @GetMapping("/list")
    @ApiOperation("用户管理列表")
    public TableDataInfo<UserMoneyVo> list(UserMoneyQuery userMoneyQuery)
    {
        return cntUserService.selectUserMoneyList(userMoneyQuery);
    }

    @Log(title = "导出用户数据", businessType = BusinessType.EXPORT)
    //@RequiresPermissions("system:user:export")
    @PostMapping("/export")
    @ApiOperation("导出用户数据")
    public void export(HttpServletResponse response)
    {
        List<UserPhoneExcel> list = cntUserService.selectUserList();
        ExcelUtil<UserPhoneExcel> util = new ExcelUtil<UserPhoneExcel>(UserPhoneExcel.class);
        util.exportExcel(response, list, "用户数据");
    }

    //@RequiresPermissions("admin:user:edit")
    @Log(title = "修改用户", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改用户")
    public R edit(@RequestBody CntUser cntUser)
    {
        return toResult(cntUserService.updateCntUser(cntUser));
    }

    @GetMapping("/myOrderList")
    @ApiOperation("我的订单")
    public TableDataInfo<CntOrderVo> myOrderList(MyOrderDto orderDto)
    {
        return cntUserService.myOrderList(orderDto);
    }

    @GetMapping("/myCollectionList")
    @ApiOperation("我的藏品")
    public TableDataInfo<UserCollectionVo> myCollectionList(MyCollectionDto collectionDto)
    {
        return cntUserService.myCollectionList(collectionDto);
    }

    @GetMapping("/myBoxList")
    @ApiOperation("我的盲盒")
    public TableDataInfo<UserBoxVo> myBoxList(MyBoxDto boxDto)
    {
        return cntUserService.myBoxList(boxDto);
    }

    @PostMapping("/updateBalance")
    @ApiOperation("修改余额")
    public R updateBalance(@RequestBody UpdateBalanceDto balanceDto)
    {
        return toResult(cntUserService.updateBalance(balanceDto));
    }

}
