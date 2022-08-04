package com.manyun.business.controller;

import com.manyun.business.domain.form.AuctionPayFixedForm;
import com.manyun.business.domain.form.AuctionPayForm;
import com.manyun.business.domain.form.AuctionPayMarginForm;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.query.MyAuctionPriceQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.IAuctionPriceService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.PageUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * <p>
 * 竞价表 前端控制器
 * </p>
 *
 * @author 
 * @since 2022-06-30
 */
@RestController
@RequestMapping("/auctionPrice")
@Api(tags = "竞拍相关apis")
public class AuctionPriceController {
    @Autowired
    private IAuctionPriceService auctionPriceService;

    @PostMapping("/myAuctionPrice")
    @ApiOperation("我的出价")
    public R<BigDecimal> myAuctionPrice(@Valid @RequestBody AuctionPriceForm auctionPriceForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return auctionPriceService.myAuctionPrice(auctionPriceForm, loginBusinessUser.getUserId());
    }

    @PostMapping("/auctionPriceList")
    @ApiOperation("竞价列表")
    public R<TableDataInfo<AuctionPriceVo>> auctionPriceList(@RequestBody AuctionPriceQuery auctionPriceQuery) {
        PageUtils.startPage(auctionPriceQuery.getPageNum(), auctionPriceQuery.getPageSize());
        TableDataInfo<AuctionPriceVo> auctionPriceVoTableDataInfo = auctionPriceService.auctionPriceList(auctionPriceQuery);
        return R.ok(auctionPriceVoTableDataInfo);
    }

    @PostMapping("/myAuctionPriceList")
    @ApiOperation("我的出价列表")
    public R<TableDataInfo<MyAuctionPriceVo>> myAuctionPriceList(@RequestBody MyAuctionPriceQuery auctionPriceQuery) {
        PageUtils.startPage(auctionPriceQuery.getPageNum(), auctionPriceQuery.getPageSize());
        LoginBusinessUser businessUser = SecurityUtils.getNotNullLoginBusinessUser();
        TableDataInfo<MyAuctionPriceVo> myAuctionPriceVoTableDataInfo = auctionPriceService.myAuctionPriceList(auctionPriceQuery, businessUser.getUserId());
        return R.ok(myAuctionPriceVoTableDataInfo);
    }

    @GetMapping("/priceCollectionInfo/{collectionId}/{auctionSendId}")
    @ApiOperation(value = "出价藏品详情", notes = "根据藏品id和送拍id查询")
    public R<AuctionCollectionAllVo> priceCollectionInfo(@PathVariable String collectionId, @PathVariable String auctionSendId) {
        return R.ok(auctionPriceService.priceCollectionInfo(collectionId, auctionSendId));
    }

    @GetMapping("/priceBoxInfo/{boxId}/{auctionSendId}")
    @ApiOperation(value = "出价盲盒详情", notes = "根据盲盒id和送拍id查询")
    public R<AuctionBoxAllVo> priceBoxInfo(@PathVariable String boxId, @PathVariable String auctionSendId) {
        return R.ok(auctionPriceService.priceBoxInfo(boxId, auctionSendId));
    }

    @PostMapping("/payAuction")
    @ApiOperation(value = "拍卖市场支付")
    public R<PayVo> payAuction(@RequestBody @Valid AuctionPayForm auctionPayForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String payUserId = loginBusinessUser.getUserId();
        PayVo payVo = auctionPriceService.payAuction(payUserId, auctionPayForm);
        return R.ok(payVo);
    }

    @PostMapping("/payMargin")
    @ApiOperation(value = "拍卖市场支付保证金")
    public R<PayVo> payMargin(@RequestBody @Valid AuctionPayMarginForm auctionPayMarginForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String payUserId = loginBusinessUser.getUserId();
        PayVo payVo = auctionPriceService.payMargin(payUserId, auctionPayMarginForm);
        return R.ok(payVo);
    }

    @PostMapping("/checkPayMargin")


    @ApiOperation(value = "是否支付过保证金")
    public R checkPayMargin(@RequestBody @Valid AuctionPriceForm auctionPriceForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String userId = loginBusinessUser.getUserId();
        return auctionPriceService.checkPayMargin(auctionPriceForm, userId);
    }

    @PostMapping("/payFixed")
    @ApiOperation(value = "一口价")
    public R<PayVo> payFixed(@RequestBody @Valid AuctionPayFixedForm auctionPayFixedForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        String userId = loginBusinessUser.getUserId();
        PayVo payVo = auctionPriceService.payFixed(userId, auctionPayFixedForm);
        return R.ok(payVo);
    }


    @GetMapping("/checkAuctionEnd")
    @ApiOperation(value = "定时调度判断是否已流拍",hidden = true)
    @InnerAuth
    public R checkAuctionEnd(){
        auctionPriceService.checkAuctionEnd();
        return R.ok();
    }

    @GetMapping("/checkWinner")
    @ApiOperation(value = "定时调度结束正常拍卖流程",hidden = true)
    @InnerAuth
    public R checkWinner(){
        auctionPriceService.checkWinner();
        return R.ok();
    }

}

