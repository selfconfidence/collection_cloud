package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.CollectionAllVo;
import com.manyun.business.domain.vo.CollectionVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.ICollectionService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 藏品表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/collection")
@Api(tags = "藏品相关apis")
public class CollectionController extends BaseController{

    @Autowired
    private ICollectionService collectionService;

    @PostMapping("/pageList")
    @ApiOperation("分页查询藏品列表信息")
    public R<TableDataInfo<CollectionVo>> pageList(@RequestBody CollectionQuery collectionQuery){
        PageHelper.startPage(collectionQuery.getPageNum(),collectionQuery.getPageSize());
        List<CollectionVo> collectionVos =  collectionService.pageQueryList(collectionQuery);
        return R.ok(getDataTable(collectionVos));
    }

    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询藏品详情信息",notes = "根据藏品编号查询藏品详情信息")
    public R<CollectionAllVo> info(@PathVariable String id){
        return R.ok(collectionService.info(id));
    }


    @PostMapping("sellCollection")
    @ApiOperation("购买藏品")
    public R<PayVo> sellCollection(@RequestBody @Valid CollectionSellForm collectionSellForm){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.sellCollection(loginBusinessUser.getUserId(),collectionSellForm));
    }
}

