package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.AuctionMarketVo;
import com.manyun.business.domain.vo.MyAuctionSendVo;
import com.manyun.business.service.IAuctionSendService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
        LoginBusinessUser loginBusinessUser = SecurityUtils.getTestLoginBusinessUser();
        return auctionSendService.auctionSend(auctionSendForm, loginBusinessUser.getUserId());
    }


    @PostMapping("/mySend")
    @ApiOperation("我的送拍列表")
    public R<TableDataInfo<MyAuctionSendVo>> mySend(@RequestBody AuctionSendQuery sendQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getTestLoginBusinessUser();
        PageHelper.startPage(sendQuery.getPageNum(), sendQuery.getPageSize());
        return R.ok(auctionSendService.pageList(sendQuery,loginBusinessUser.getUserId()));
    }

    @PostMapping("/auctionMarketList")
    @ApiOperation("拍卖市场列表")
    public R<TableDataInfo<AuctionMarketVo>> auctionMarketList(@RequestBody AuctionMarketQuery marketQuery) {
        PageHelper.startPage(marketQuery.getPageNum(), marketQuery.getPageSize());
        return R.ok(auctionSendService.auctionMarketList(marketQuery));
    }


}

