package com.manyun.admin.controller;

import com.manyun.admin.domain.dto.BoxAirdropDto;
import com.manyun.admin.domain.dto.BoxStateDto;
import com.manyun.admin.domain.dto.CntBoxAlterCombineDto;
import com.manyun.admin.domain.excel.BoxBachAirdopExcel;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.CntBoxDetailsVo;
import com.manyun.admin.domain.vo.CntBoxOrderVo;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/box")
@Api(tags = "盲盒apis")
public class CntBoxController extends BaseController
{
    @Autowired
    private ICntBoxService cntBoxService;

    //@RequiresPermissions("admin:box:list")
    @GetMapping("/list")
    @ApiOperation("盲盒列表")
    public TableDataInfo<CntBoxVo> list(BoxQuery boxQuery)
    {
        return cntBoxService.selectCntBoxList(boxQuery);
     }

    //@RequiresPermissions("admin:box:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("盲盒详细信息")
    public R<CntBoxDetailsVo> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntBoxService.selectCntBoxById(id));
    }

    //@RequiresPermissions("admin:box:add")
    @Log(title = "新增盲盒", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增盲盒")
    public R add(@RequestBody CntBoxAlterCombineDto boxAlterCombineDto)
    {
        return cntBoxService.insertCntBox(boxAlterCombineDto);
    }

    //@RequiresPermissions("admin:box:edit")
    @Log(title = "修改盲盒", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改盲盒")
    public R edit(@RequestBody CntBoxAlterCombineDto boxAlterCombineDto)
    {
        return cntBoxService.updateCntBox(boxAlterCombineDto);
    }

    @PostMapping("/updateState")
    @ApiOperation("修改状态")
    public R updateState(@Valid @RequestBody BoxStateDto boxStateDto)
    {
        return toResult(cntBoxService.updateState(boxStateDto));
    }

    @GetMapping("/boxOrderList")
    @ApiOperation("盲盒订单列表")
    public TableDataInfo<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery)
    {
        return cntBoxService.boxOrderList(orderQuery);
    }

    @PostMapping("/airdrop")
    @ApiOperation("空投")
    public R airdrop(@Valid @RequestBody BoxAirdropDto boxAirdropDto)
    {
        return cntBoxService.airdrop(boxAirdropDto);
    }

    @ApiOperation("批量空投下载模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BoxBachAirdopExcel> util = new ExcelUtil<BoxBachAirdopExcel>(BoxBachAirdopExcel.class);
        util.importTemplateExcel(response, "批量空投数据");
    }

    @ApiOperation("批量空投获取导入的数据,并处理")
    @PostMapping("/importData")
    public R importData(@RequestPart("file") MultipartFile file) throws Exception
    {
        ExcelUtil<BoxBachAirdopExcel> util = new ExcelUtil<BoxBachAirdopExcel>(BoxBachAirdopExcel.class);
        List<BoxBachAirdopExcel> boxBachAirdopExcels = util.importExcel(file.getInputStream());
        return cntBoxService.postExcelList(boxBachAirdopExcels);
    }

}
