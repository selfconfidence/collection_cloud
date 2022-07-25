package com.manyun.admin.controller;


import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.CntDictService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dict")
@Api(tags = "字典apis")
public class CntDictController extends BaseController
{

    @Autowired
    private CntDictService cntDictService;

    @GetMapping("/collectionDict")
    @ApiOperation("查询藏品字典")
    public TableDataInfo<CntCollectionDictVo> collectionDict()
    {
        startPage();
        List<CntCollectionDictVo> list = cntDictService.collectionDict();
        return getDataTable(list);
    }

    @GetMapping("/collectionCateDict")
    @ApiOperation("查询藏品系列字典")
    public TableDataInfo<CollectionCateDictVo> collectionCateDict()
    {
        startPage();
        List<CollectionCateDictVo> list = cntDictService.collectionCateDict();
        return getDataTable(list);
    }

    @GetMapping("/creationdDict")
    @ApiOperation("创作者字典")
    public TableDataInfo<CreationdDictVo> creationdDict()
    {
        startPage();
        List<CreationdDictVo> list = cntDictService.creationdDict();
        return getDataTable(list);
    }

    @GetMapping("/lableDict")
    @ApiOperation("标签字典")
    public TableDataInfo<LableDictVo> lableDict()
    {
        startPage();
        List<LableDictVo> list = cntDictService.lableDict();
        return getDataTable(list);
    }

    @GetMapping("/customerServiceDict")
    @ApiOperation("客服字典")
    public TableDataInfo<CustomerServiceDictVo> customerServiceDict()
    {
        startPage();
        List<CustomerServiceDictVo> list = cntDictService.customerServiceDict();
        return getDataTable(list);
    }

}
