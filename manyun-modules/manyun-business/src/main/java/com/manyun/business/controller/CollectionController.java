package com.manyun.business.controller;


import cn.hutool.core.lang.Assert;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.CollectionOrderSellForm;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.form.PushMuseumForm;
import com.manyun.business.domain.form.ShareCollectionForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ICollectionService;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IUserCollectionService;
import com.manyun.comm.api.domain.redis.collection.CollectionAllRedisVo;
import com.manyun.comm.api.domain.redis.collection.CollectionRedisVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.annotation.RequestBodyRsa;
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

    @Autowired
    private ILogsService logsService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询藏品完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    public R<List<KeywordVo>> queryDict(@PathVariable String keyword){
        return R.ok(collectionService.queryDict(keyword));
    }

    @GetMapping("/thisAssertQueryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询我的(藏品|盲盒)完整 标题信息",notes = "返回的都是 (藏品|盲盒)词条完整信息 ")
    public R<List<KeywordVo>> thisAssertQueryDict(@PathVariable String keyword){
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        return R.ok(collectionService.thisAssertQueryDict(userId,keyword));
    }


    @PostMapping("/pageList")
    @ApiOperation("分页查询藏品列表信息")
    public R<TableDataInfo<CollectionVo>> pageList(@RequestBody CollectionQuery collectionQuery){
        PageHelper.startPage(collectionQuery.getPageNum(),collectionQuery.getPageSize());
        return R.ok(collectionService.pageQueryList(collectionQuery));
    }

    @PostMapping("/homeCacheList")
    @ApiOperation(value = "查询首页藏品缓存列表",notes = "不分页,默认查询全部上架!\n 调用此接口和之前一样即可,只需要变动下接口即可,但查询条件是无效的,分页也是无效的!")
    public R<TableDataInfo<CollectionVo>>  homeCacheList(){
        return R.ok(collectionService.homeCacheList());
    }


    @GetMapping("/info/{id}")
    @ApiOperation(value = "查询藏品详情信息",notes = "根据藏品编号查询藏品详情信息 -需登录")
    public R<CollectionAllRedisVo> info(@PathVariable String id){
        String userId = SecurityUtils.getBuiUserId();
        return R.ok(collectionService.infoCache(id,userId));
    }

    @GetMapping("/tarCollection/{id}")
    @ApiOperation(value = "对需要抽签的藏品,进行抽签",notes = " id = 藏品编号  \rdata = 提示信息 例如 您已经参与对${buiName}抽签了,抽签结果将在${openTime}公布!\n")
    @Lock("tarCollection")
    public R<String> tarCollection(@PathVariable String id){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.tarCollection(id,notNullLoginBusinessUser.getUserId()));
    }


//    @PostMapping("/sellCollection")
//    @ApiOperation("购买藏品")
//    @Deprecated
//    @Lock("sellCollection")
//    public R<PayVo> sellCollection(@RequestBody @Valid CollectionSellForm collectionSellForm){
//        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
//        return R.ok(collectionService.sellCollection(loginBusinessUser.getUserId(),collectionSellForm));
//    }


    @PostMapping("/sellOrderCollection")
    @ApiOperation(value = "购买藏品_预先_生成订单",notes = "用来预先 生成一个待支付订单,返回订单编号,用来二次提交支付\n version 1.0.1")
    @Lock(value = "sellOrderCollection",waitTime = 1000L)
    public R<String> sellOrderCollection(@RequestBodyRsa @Valid CollectionOrderSellForm collectionOrderSellForm){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.sellOrderCollection(loginBusinessUser.getUserId(),collectionOrderSellForm));
    }

    @PostMapping("/phpSellOrderCollection")
    @ApiOperation(value = "购买藏品_预先_生成订单",notes = "用来预先 生成一个待支付订单,返回订单编号,用来二次提交支付\n version 1.0.1")
    @Lock(value = "sellOrderCollection",waitTime = 1000L)
    public R<String> phpSellOrderCollection(@RequestBody @Valid CollectionOrderSellForm collectionOrderSellForm){
        Assert.isTrue(Boolean.FALSE,"接口已废弃!");
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.sellOrderCollection(loginBusinessUser.getUserId(),collectionOrderSellForm));
    }

    @PostMapping("/userCollectionPageList")
    @ApiOperation("用户查询自己的藏品信息")
    public R<TableDataInfo<UserCollectionVo>> userCollectionPageList(@RequestBody UseAssertQuery useAssertQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.userCollectionPageList(useAssertQuery,notNullLoginBusinessUser.getUserId()));
    }

    @GetMapping("/cateCollectionAll")
    @ApiOperation("用户查询自己所有 系列分组的藏品信息")
    public R<List<UserCateVo>> cateCollectionAll(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(collectionService.cateCollectionByUserId(notNullLoginBusinessUser.getUserId()));
    }

    @GetMapping("/cateCollectionChildList/{parentId}")
    @ApiOperation(value = "查询所有夫级(品牌馆)系列分组的藏品信息",notes = "version 1.0.1")
    public R<List<CateCollectionVo>> cateCollectionChildList(@PathVariable String parentId){
        String userId = SecurityUtils.getNotNullLoginBusinessUser().getUserId();
        return R.ok(collectionService.cateCollectionChildList(userId,parentId));
    }

    @GetMapping("/userCollectionInfo/{id}")
    @ApiOperation(value = "用户查询自己得藏品详情信息",notes = "用户拥有藏品得编号,不是藏品编号")
    public R<UserCollectionForVo> userCollectionInfo(@PathVariable String id){
        return R.ok(collectionService.userCollectionInfo(id));
    }

    @PostMapping("/logsPage")
    @ApiOperation("分页查询藏品相关记录信息")
    public R<TableDataInfo<CollectionLogPageVo>> logsPage(@RequestBody PageQuery pageQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(logsService.logsCollectionPage(pageQuery,notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/shareCollection")
    @ApiOperation("分享藏品")
    public R<String> shareCollection(@RequestBody @Valid ShareCollectionForm shareCollectionForm) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return userCollectionService.shareCollection(notNullLoginBusinessUser.getUserId(),shareCollectionForm.getMyGoodsId() );
    }

    @PostMapping("/pushMuseum")
    @ApiOperation("选择藏品到展馆")
    public R pushMuseum(@RequestBody PushMuseumForm pushMuseumForm) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        userCollectionService.pushMuseum(pushMuseumForm, notNullLoginBusinessUser.getUserId());
        return R.ok();
    }

    @PostMapping("/museumList")
    @ApiOperation("展馆列表")
    public R<List<MuseumListVo>> museumList() {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        List<MuseumListVo> museumListVos = userCollectionService.museumList(notNullLoginBusinessUser.getUserId());
        return R.ok(museumListVos);
    }

    @GetMapping("/museumListForApp/{userId}")
    @ApiOperation("展馆列表(app查询)")
    public R<List<MuseumListVo>> museumListForApp(@PathVariable String userId) {
        List<MuseumListVo> museumListVos = userCollectionService.museumList(userId);
        return R.ok(museumListVos);
    }

}

