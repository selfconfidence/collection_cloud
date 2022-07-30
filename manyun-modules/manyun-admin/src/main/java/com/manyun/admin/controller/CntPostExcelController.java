package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.excel.PostExcel;
import com.manyun.admin.domain.vo.CntPostExcelVo;
import com.manyun.comm.api.domain.SysUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntPostExcel;
import com.manyun.admin.service.ICntPostExcelService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 提前购格Controller
 *
 * @author yanwei
 * @date 2022-07-27
 */
@RestController
@RequestMapping("/postExcel")
@Api(tags = "提前购格apis")
public class CntPostExcelController extends BaseController
{
    @Autowired
    private ICntPostExcelService cntPostExcelService;

    /**
     * 查询提前购格列表
     */
    @RequiresPermissions("admin:excel:list")
    @GetMapping("/list")
    @ApiOperation("查询提前购格列表")
    public TableDataInfo<CntPostExcelVo> list(CntPostExcel cntPostExcel)
    {
        startPage();
        List<CntPostExcelVo> list = cntPostExcelService.selectCntPostExcelList(cntPostExcel);
        return getDataTable(list);
    }

    /**
     * 删除提前购格
     */
    @RequiresPermissions("admin:excel:remove")
    @Log(title = "提前购格", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除提前购格")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntPostExcelService.deleteCntPostExcelByIds(ids));
    }

    @ApiOperation("下载模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<PostExcel> util = new ExcelUtil<PostExcel>(PostExcel.class);
        util.importTemplateExcel(response, "提前购数据");
    }

    @ApiOperation("获取导入的数据,并处理")
    @PostMapping("/importData")
    public R importData(@RequestPart("file") MultipartFile file) throws Exception
    {
        ExcelUtil<PostExcel> util = new ExcelUtil<PostExcel>(PostExcel.class);
        List<PostExcel> postExcelList = util.importExcel(file.getInputStream());
        return cntPostExcelService.importPostExcel(postExcelList);
    }

}
