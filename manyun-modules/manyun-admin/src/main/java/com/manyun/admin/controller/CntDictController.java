package com.manyun.admin.controller;


import com.manyun.admin.domain.query.ActionTarDictQuery;
import com.manyun.admin.domain.query.DrawRulesDictQuery;
import com.manyun.admin.service.CntDictService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/boxDict")
    @ApiOperation("查询盲盒字典")
    public R boxDict()
    {
        return cntDictService.boxDict();
    }

    @GetMapping("/collectionCateDict")
    @ApiOperation("查询藏品系列字典")
    public R collectionCateDict()
    {
        return cntDictService.collectionCateDict();
    }

    @GetMapping("/boxCateDict")
    @ApiOperation("查询盲盒系列字典")
    public R boxCateDict()
    {
        return cntDictService.boxCateDict();
    }

    @GetMapping("/creationdDict")
    @ApiOperation("创作者字典")
    public R creationdDict()
    {
        return cntDictService.creationdDict();
    }

    @GetMapping("/issuanceDict")
    @ApiOperation("发行发字典")
    public R issuanceDict()
    {
        return cntDictService.issuanceDict();
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

    @PostMapping("/postConfigGoodsDict")
    @ApiOperation("满足提前购的商品字典")
    public R postConfigGoodsDict()
    {
        return cntDictService.postConfigGoodsDict();
    }

    @GetMapping("/postExistDict")
    @ApiOperation("提前购前置条件字典")
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

    @PostMapping("/cateDict")
    @ApiOperation("藏品分类字典")
    public R cateDict()
    {
        return cntDictService.cateDict();
    }


    @GetMapping("/boxScoreDict")
    @ApiOperation("盲盒评分字典")
    public R boxScoreDict()
    {
        return cntDictService.boxScoreDict();
    }

    @GetMapping("/agreementTypeDict")
    @ApiOperation("协议类型字典")
    public R agreementTypeDict()
    {
        return cntDictService.agreementTypeDict();
    }

    @GetMapping("/bannerJumpLinkDict")
    @ApiOperation("轮播跳转链接字典")
    public R bannerJumpLinkDict()
    {
        return cntDictService.bannerJumpLinkDict();
    }

}
