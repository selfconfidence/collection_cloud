package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.query.UserMoneyQuery;
import com.manyun.admin.domain.vo.UserCollectionVo;
import com.manyun.admin.domain.vo.UserMoneyVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        startPage();
        List<UserMoneyVo> list = cntUserService.selectUserMoneyList(userMoneyQuery);
        return getDataTable(list);
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
    public TableDataInfo<CntOrder> myOrderList(String userId)
    {
        startPage();
        List<CntOrder> list = cntUserService.myOrderList(userId);
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

}