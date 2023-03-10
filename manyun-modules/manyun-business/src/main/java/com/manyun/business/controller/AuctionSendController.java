package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.form.AuctionSendForm;
import com.manyun.business.domain.query.AuctionMarketQuery;
import com.manyun.business.domain.query.AuctionSendQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.IAuctionSendService;
import com.manyun.business.service.ISystemService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.annotation.RequestBodyRsa;
import com.manyun.common.core.constant.BusinessConstants;
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

    @Autowired
    private ISystemService systemService;


    @GetMapping("/isAuction")

    @ApiOperation("是否开启拍卖市场（1开启，0关闭）")
    public R<String> isAuction() {
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_ACC, String.class));
    }

    @PostMapping("/auctionSend")
    @ApiOperation("提交送拍")
    @Lock("auctionSend")
    public R auctionSend(@Valid @RequestBodyRsa AuctionSendForm auctionSendForm) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return auctionSendService.auctionSend(auctionSendForm, loginBusinessUser.getUserId());
    }

    @GetMapping("/auctionSendConfig")
    @ApiOperation("获取保证金比例")
    public R<BigDecimal> auctionSendConfig() {
        return auctionSendService.auctionSendConfig();
    }

    @GetMapping("/auctionSendCommission")
    @ApiOperation("获取佣金比例")
    public R<BigDecimal> auctionSendCommission() {
        return auctionSendService.auctionSendCommission();
    }

    @PostMapping("/reAuctionSend")
    @ApiOperation("重新送拍")
    @Lock("reAuctionSend")
    public R reAuctionSend(@Valid @RequestBodyRsa AuctionSendForm auctionSendForm, String auctionSendId) {
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

    @GetMapping("/sellInfo")
    @ApiOperation(value = "出售须知")
    public R<String> sellInfo(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_SELL_INFO, String.class));
    }

    @GetMapping("/marginInfo")
    @ApiOperation(value = "保证金说明")
    public R<String> marginInfo() {
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_INFO, String.class));
    }

    @GetMapping("/commissionInfo")
    @ApiOperation(value = "佣金说明")
    public R<String> commissionInfo() {
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.COMMISSION_INFO, String.class));
    }



}

