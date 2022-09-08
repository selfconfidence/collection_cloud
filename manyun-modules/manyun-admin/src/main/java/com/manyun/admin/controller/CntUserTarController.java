package com.manyun.admin.controller;


import com.manyun.admin.domain.excel.PostExcel;
import com.manyun.admin.domain.excel.UserTarExcel;
import com.manyun.admin.domain.query.UserTarQuery;
import com.manyun.admin.domain.vo.CntUserTarVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.admin.service.ICntUserTarService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户抽签购买藏品或盲盒中间Controller
 *
 * @author yanwei
 * @date 2022-09-08
 */
@RestController
@RequestMapping("/userTar")
@Api(tags = "用户抽签购买藏品或盲盒中间apis")
public class CntUserTarController extends BaseController
{
    @Autowired
    private ICntUserTarService cntUserTarService;

    /**
     * 查询用户抽签购买藏品或盲盒中间列表
     */
    //@RequiresPermissions("admin:tar:list")
    @GetMapping("/list")
    @ApiOperation("查询用户抽签购买藏品或盲盒中间列表")
    public TableDataInfo<CntUserTarVo> list(UserTarQuery userTarQuery)
    {
        return cntUserTarService.selectCntUserTarList(userTarQuery);
    }

    /**
     * 删除用户抽签购买藏品或盲盒中间
     */
    @RequiresPermissions("admin:tar:remove")
    @DeleteMapping("/{ids}")
    @ApiOperation("删除用户抽签购买藏品或盲盒中间")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntUserTarService.deleteCntUserTarByIds(ids));
    }


    @ApiOperation("下载模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<UserTarExcel> util = new ExcelUtil<UserTarExcel>(UserTarExcel.class);
        util.importTemplateExcel(response, "抽签记录数据");
    }

    @ApiOperation("获取导入的数据,并处理")
    @PostMapping("/importData")
    public R importData(@RequestPart("file") MultipartFile file) throws Exception
    {
        ExcelUtil<UserTarExcel> util = new ExcelUtil<UserTarExcel>(UserTarExcel.class);
        List<UserTarExcel> userTarExcelList = util.importExcel(file.getInputStream());
        return cntUserTarService.importPostExcel(userTarExcelList);
    }
}
