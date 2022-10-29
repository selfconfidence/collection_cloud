package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.UpdateWithDrawDto;
import com.manyun.admin.domain.query.SystemWithdrawQuery;
import com.manyun.admin.domain.vo.SystemWithdrawVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.admin.service.ICntSystemWithdrawService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 系统余额提现Controller
 *
 * @author yanwei
 * @date 2022-09-17
 */
@RestController
@RequestMapping("/withdraw")
@Api(tags = "系统余额提现apis")
public class CntSystemWithdrawController extends BaseController
{
    @Autowired
    private ICntSystemWithdrawService cntSystemWithdrawService;

    /**
     * 查询系统余额提现列表
     */
    //@RequiresPermissions("admin:withdraw:list")
    @GetMapping("/list")
    @ApiOperation("查询系统余额提现列表")
    public TableDataInfo<SystemWithdrawVo> list(SystemWithdrawQuery systemWithdrawQuery)
    {
        return cntSystemWithdrawService.selectCntSystemWithdrawList(systemWithdrawQuery);
    }

    @PostMapping("/updateWithdrawStatus")
    @ApiOperation("修改打款状态")
    public R updateWithdrawStatus(@RequestBody UpdateWithDrawDto withDrawDto)
    {
        //return toResult(cntSystemWithdrawService.updateWithdrawStatus(withDrawDto));
        cntSystemWithdrawService.updateWithdrawStatus(withDrawDto);
        return R.ok();
    }

    @PostMapping("/cancelWithDraw")
    @ApiOperation("取消打款")
    public R cancelWithDraw(@RequestBody UpdateWithDrawDto withDrawDto)
    {
        //return toResult(cntSystemWithdrawService.updateWithdrawStatus(withDrawDto));
        cntSystemWithdrawService.cancelWithDraw(withDrawDto);
        return R.ok();
    }

    @PostMapping("/export")
    @ApiOperation("导出提现记录数据")
    public void export(HttpServletResponse response)
    {
        List<SystemWithdrawVo> list = cntSystemWithdrawService.selectExportList();
        ExcelUtil<SystemWithdrawVo> util = new ExcelUtil<SystemWithdrawVo>(SystemWithdrawVo.class);
        util.exportExcel(response, list, "提现记录数据");
    }

}
