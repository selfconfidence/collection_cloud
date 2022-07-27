package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.IAuctionSendService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 拍卖表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/auctionSend")
@Api(tags = "我的送拍相关apis")
public class AuctionSendController {

    @Autowired
    private IAuctionSendService auctionSendService;

    @PostMapping("/auctionSend")
    @ApiOperation("提交送拍")
    public R auctionSend(@Valid @RequestBody AuctionSendForm auctionSendForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return auctionSendService.auctionSend(auctionSendForm, loginBusinessUser.getUserId());
    }

    @GetMapping("/auctionSendConfig")
    @ApiOperation("获取保证金比例")
    public BigDecimal auctionSendConfig() {
        return auctionSendService.auctionSendConfig();
    }

    @PostMapping("/reAuctionSend")
    @ApiOperation("重新送拍")
    public R reAuctionSend(@Valid @RequestBody AuctionSendForm auctionSendForm, String auctionSendId) {
        return auctionSendService.reAuctionSend(auctionSendForm, auctionSendId);
    }

    @PostMapping("/mySend")
    @ApiOperation("我的送拍列表")
    public R<TableDataInfo<MyAuctionSendVo>> mySend(@RequestBody AuctionSendQuery sendQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        PageHelper.startPage(sendQuery.getPageNum(), sendQuery.getPageSize());
        return R.ok(auctionSendService.pageList(sendQuery,loginBusinessUser.getUserId()));
    }

    @GetMapping("/queryDict/{keyword}")
    @ApiOperation(value = "/根据词条 查询藏品完整 标题信息",notes = "返回的都是 盲盒词条完整信息 ")
    public R<List<KeywordVo>> queryDict(@PathVariable String keyword){
        return R.ok(auctionSendService.queryDict(keyword));
    }

    @PostMapping("/auctionMarketList")
    @ApiOperation("拍卖市场列表")
    public R<TableDataInfo<AuctionMarketVo>> auctionMarketList(@RequestBody AuctionMarketQuery marketQuery) {
        PageHelper.startPage(marketQuery.getPageNum(), marketQuery.getPageSize());
        return R.ok(auctionSendService.auctionMarketList(marketQuery));
    }

    @GetMapping("/auctionCollectionInfo/{collectionId}/{auctionSendId}")
    @ApiOperation(value = "拍卖藏品详情", notes = "根据藏品id和送拍id查询")
    public R<AuctionCollectionAllVo> auctionCollectionInfo(@PathVariable String collectionId, @PathVariable String auctionSendId) {
        return R.ok(auctionSendService.auctionCollectionInfo(collectionId, auctionSendId));
    }

    @GetMapping("/auctionBoxInfo/{boxId}/{auctionSendId}")
    @ApiOperation(value = "拍卖盲盒详情", notes = "根据盲盒id和送拍id查询")
    public R<AuctionBoxAllVo> auctionBoxInfo(@PathVariable String boxId, @PathVariable String auctionSendId) {
        return R.ok(auctionSendService.auctionBoxInfo(boxId, auctionSendId));
    }


    @GetMapping("/timeStartAuction")
    @ApiOperation(value = "定时调度修改竞拍状态",hidden = true)
    @InnerAuth
    public R timeStartAuction(){
        auctionSendService.timeStartAuction();
        return R.ok();
    }


}

