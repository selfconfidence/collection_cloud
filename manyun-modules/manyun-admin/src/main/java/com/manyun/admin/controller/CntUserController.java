package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.dto.UpdateBalanceDto;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.common.core.domain.R;
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
    public TableDataInfo<CntOrderVo> myOrderList(String userId)
    {
        startPage();
        List<CntOrderVo> list = cntUserService.myOrderList(userId);
        return getDataTable(list);
    }

    @GetMapping("/myCollectionList")
    @ApiOperation("我的藏品")
    public TableDataInfo<UserCollectionVo> myCollectionList(String userId)
    {
        startPage();
        List<UserCollectionVo> list = cntUserService.myCollectionList(userId);
        return getDataTable(list);
    }

    @PostMapping("/updateBalance")
    @ApiOperation("修改余额")
    public R updateBalance(@RequestBody UpdateBalanceDto balanceDto)
    {
        return toResult(cntUserService.updateBalance(balanceDto));
    }

}
