package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ICollectionService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
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

    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询藏品完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    public R<List<String>> queryDict(@PathVariable String keyword){
        return R.ok(collectionService.queryDict(keyword));
    }

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


    @PostMapping("/sellCollection")
    @ApiOperation("购买藏品")
    public R<PayVo> sellCollection(@RequestBody @Valid CollectionSellForm collectionSellForm){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.sellCollection(loginBusinessUser.getUserId(),collectionSellForm));
    }

    @PostMapping("/userCollectionPageList")
    @ApiOperation("用户查询自己的藏品信息")
    public R<TableDataInfo<UserCollectionVo>> userCollectionPageList(@RequestBody PageQuery pageQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(getDataTable(collectionService.userCollectionPageList(pageQuery,notNullLoginBusinessUser.getUserId())));
    }

    @GetMapping("/cateCollectionAll")
    @ApiOperation("用户查询自己所有 系列分组的藏品信息")
    public R<List<UserCateVo>> cateCollectionAll(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.cateCollectionByUserId(notNullLoginBusinessUser.getUserId()));
    }
}

