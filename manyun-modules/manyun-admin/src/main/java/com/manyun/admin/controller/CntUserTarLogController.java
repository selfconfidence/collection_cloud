package com.manyun.admin.controller;

import com.manyun.admin.domain.query.UserTarLogQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntUserTarLog;
import com.manyun.admin.service.ICntUserTarLogService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 抽签记录日志Controller
 *
 * @author yanwei
 * @date 2022-10-09
 */
@RestController
@RequestMapping("/log")
@Api(tags = "抽签记录日志apis")
public class CntUserTarLogController extends BaseController
{
    @Autowired
    private ICntUserTarLogService cntUserTarLogService;

    /**
     * 查询抽签记录日志列表
     */
    @RequiresPermissions("admin:log:list")
    @GetMapping("/list")
    @ApiOperation("查询抽签记录日志列表")
    public TableDataInfo<CntUserTarLog> list(UserTarLogQuery userTarLogQuery)
    {
        return cntUserTarLogService.selectCntUserTarLogList(userTarLogQuery);
    }

}
