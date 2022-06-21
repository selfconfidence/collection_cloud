package com.manyun.business.controller;


import com.manyun.business.domain.vo.CateVo;
import com.manyun.business.service.ICateService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 藏品系列_分类 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/cate")
@Api(tags = "系列相关Apis")
public class CateController extends BaseController {

    @Autowired
    private ICateService cateService;


    @GetMapping("/cateAll/{type}")
    @ApiOperation(value = "系列列表",notes = "无需分页,1=藏品系列，2=盲盒分类")
    //@ApiImplicitParam(name = "系列分类类型",value = "type",dataTypeClass = int.class,dataType = "int",required = true,paramType = "path")
    public R<List<CateVo>>  cateAll(@PathVariable Integer type){
        return R.ok(cateService.cateAll(type));
    }

}

