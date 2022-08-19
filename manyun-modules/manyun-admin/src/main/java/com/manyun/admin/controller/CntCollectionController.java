package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.excel.BachAirdopExcel;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/collection")
@Api(tags = "藏品管理apis")
public class CntCollectionController extends BaseController
{
    @Autowired
    private ICntCollectionService cntCollectionService;

    //@RequiresPermissions("admin:collection:list")
    @GetMapping("/list")
    @ApiOperation("查询藏品管理列表")
    public TableDataInfo<CntCollectionVo> list(CollectionQuery collectionQuery)
    {
        return cntCollectionService.selectCntCollectionList(collectionQuery);
    }

    //@RequiresPermissions("admin:collection:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取藏品管理详细信息")
    public R<CntCollectionDetailsVo> getInfo(@PathVariable("id")  String id)
    {
        return R.ok(cntCollectionService.selectCntCollectionById(id));
    }

    //@RequiresPermissions("admin:collection:add")
    @Log(title = "新增藏品管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增藏品管理")
    public R add(@RequestBody CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        return cntCollectionService.insertCntCollection(collectionAlterCombineDto);
    }

    //@RequiresPermissions("admin:collection:edit")
    @Log(title = "修改藏品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改藏品管理")
    public R edit(@RequestBody CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        return cntCollectionService.updateCntCollection(collectionAlterCombineDto);
    }

    @PostMapping("/airdrop")
    @ApiOperation("空投")
    public R airdrop(@Valid @RequestBody AirdropDto airdropDto)
    {
        return cntCollectionService.airdrop(airdropDto);
    }

    @ApiOperation("批量空投下载模板")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<BachAirdopExcel> util = new ExcelUtil<BachAirdopExcel>(BachAirdopExcel.class);
        util.importTemplateExcel(response, "批量空投数据");
    }

    @ApiOperation("批量空投获取导入的数据,并处理")
    @PostMapping("/importData")
    public R importData(@RequestPart("file") MultipartFile file) throws Exception
    {
        ExcelUtil<BachAirdopExcel> util = new ExcelUtil<BachAirdopExcel>(BachAirdopExcel.class);
        List<BachAirdopExcel> bachAirdopExcels = util.importExcel(file.getInputStream());
        return cntCollectionService.postExcelList(bachAirdopExcels);
    }

}
