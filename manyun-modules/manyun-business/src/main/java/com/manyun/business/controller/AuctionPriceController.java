package com.manyun.business.controller;


import com.manyun.business.domain.entity.AuctionPrice;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.vo.AuctionPriceVo;
import com.manyun.business.service.IAuctionPriceService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.PageUtils;
import com.manyun.common.core.web.page.TableDataInfo;
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
    public R myAuctionPrice(@Valid @RequestBody AuctionPriceForm auctionPriceForm) {
        return auctionPriceService.myAuctionPrice(auctionPriceForm);
    }

    @PostMapping("/auctionPriceList")
    @ApiOperation("竞价列表")
    public R<TableDataInfo<AuctionPriceVo>> auctionPriceList(@RequestBody AuctionPriceQuery auctionPriceQuery) {
        PageUtils.startPage();
        TableDataInfo<AuctionPriceVo> auctionPriceVoTableDataInfo = auctionPriceService.auctionPriceList(auctionPriceQuery);
        return R.ok(auctionPriceVoTableDataInfo);
    }

}

