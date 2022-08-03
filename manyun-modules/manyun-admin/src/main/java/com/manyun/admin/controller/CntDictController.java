package com.manyun.admin.controller;


import com.manyun.admin.domain.query.ActionTarDictQuery;
import com.manyun.admin.domain.query.DrawRulesDictQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.CntDictService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public R collectionDict()
    {
        return cntDictService.collectionDict();
    }

    @GetMapping("/collectionCateDict")
    @ApiOperation("查询藏品系列字典")
    public R collectionCateDict()
    {
        return cntDictService.collectionCateDict();
    }

    @GetMapping("/creationdDict")
    @ApiOperation("创作者字典")
    public R creationdDict()
    {
        return cntDictService.creationdDict();
    }

    @GetMapping("/lableDict")
    @ApiOperation("标签字典")
    public R lableDict()
    {
        return cntDictService.lableDict();
    }

    @GetMapping("/customerServiceDict")
    @ApiOperation("客服字典")
    public R customerServiceDict()
    {
        return cntDictService.customerServiceDict();
    }

    @PostMapping("/drawRulesDict")
    @ApiOperation("抽签规则字典")
    public R drawRulesDict(@Valid @RequestBody DrawRulesDictQuery drawRulesDictQuery)
    {
        return cntDictService.drawRulesDict(drawRulesDictQuery);
    }

    @GetMapping("/postSellDict")
    @ApiOperation("提前购配置可以购买字典")
    public R postSellDict()
    {
        return cntDictService.postSellDict();
    }

    @GetMapping("/postExistDict")
    @ApiOperation("提前购配置已经拥有字典")
    public R postExistDict()
    {
        return cntDictService.postExistDict();
    }

    @PostMapping("/actionTarDict")
    @ApiOperation("活动合成材料字典")
    public R actionTarDict(@Valid @RequestBody ActionTarDictQuery tarDictQuery)
    {
        return cntDictService.actionTarDict(tarDictQuery);
    }

}
