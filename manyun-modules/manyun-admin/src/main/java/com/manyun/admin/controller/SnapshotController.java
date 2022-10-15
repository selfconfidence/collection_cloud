package com.manyun.admin.controller;

import com.manyun.admin.domain.query.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.ISnapshotService;
import com.manyun.common.core.utils.poi.ExcelUtil;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 快照Controller
 *
 * @author yanwei
 * @date 2022-08-19
 */
@RestController
@RequestMapping("/snapshot")
@Api(tags = "快照apis")
public class SnapshotController extends BaseController {

    @Autowired
    private ISnapshotService snapshotService;

    @PostMapping("/statisticalGoods")
    @ApiOperation("规定时间统计商品查询")
    public TableDataInfo<StatisticalGoodsVo> statisticalGoods(@Valid @RequestBody StatisticalGoodsQuery statisticalGoodsQuery)
    {
        return snapshotService.statisticalGoods(statisticalGoodsQuery);
    }

    @PostMapping("/statisticalGoodsExcel")
    @ApiOperation("规定时间统计商品数据导出")
    public void statisticalGoodsExcel(@RequestBody StatisticalGoodsQuery statisticalGoodsQuery, HttpServletResponse response)
    {
       List<StatisticalGoodsVo> statisticalGoodsVoList = snapshotService.statisticalGoodsExcel(statisticalGoodsQuery);
       ExcelUtil<StatisticalGoodsVo> util = new ExcelUtil<StatisticalGoodsVo>(StatisticalGoodsVo.class);
       util.exportExcel(response, statisticalGoodsVoList, "规定时间统计商品数据导出");
    }

    @PostMapping("/collectionNumberQuery")
    @ApiOperation("藏品编号查询")
    public TableDataInfo<CollectionNumberVo> collectionNumberQuery(@Valid @RequestBody CollectionNumberQuery collectionNumberQuery)
    {
        return snapshotService.collectionNumberQuery(collectionNumberQuery);
    }

    @PostMapping("/collectionNumberExcel")
    @ApiOperation("藏品编号数据导出")
    public void collectionNumberExcel(@Valid CollectionNumberQuery collectionNumberQuery,HttpServletResponse response)
    {
        List<CollectionNumberVo> collectionNumberVos = snapshotService.collectionNumberExcel(collectionNumberQuery);
        ExcelUtil<CollectionNumberVo> util = new ExcelUtil<CollectionNumberVo>(CollectionNumberVo.class);
        util.exportExcel(response, collectionNumberVos, "藏品编号数据导出");
    }

        @PostMapping("/collectionBusinessQuery")
    @ApiOperation("规定时间藏品交易量及交易金额查询")
    public TableDataInfo<CollectionBusinessVo> collectionBusinessQuery(@Valid @RequestBody CollectionBusinessQuery collectionBusinessQuery)
    {
        return snapshotService.collectionBusinessQuery(collectionBusinessQuery);
    }

    @PostMapping("/collectionBusinessExcel")
    @ApiOperation("规定时间藏品交易量及交易金额数据导出")
    public void collectionBusinessExcel(@Valid CollectionBusinessQuery collectionBusinessQuery,HttpServletResponse response)
    {
        List<CollectionBusinessVo> collectionBusinessVoList = snapshotService.collectionBusinessExcel(collectionBusinessQuery);
        ExcelUtil<CollectionBusinessVo> util = new ExcelUtil<CollectionBusinessVo>(CollectionBusinessVo.class);
        util.exportExcel(response, collectionBusinessVoList, "规定时间藏品交易量及交易金额数据导出");
    }

    @PostMapping("/collectionSalesStatistics")
    @ApiOperation("规定时间指定藏品买卖查询")
    public TableDataInfo<CollectionSalesStatisticsVo> collectionSalesStatistics(@Valid @RequestBody CollectionSalesStatisticsQuery collectionSalesStatisticsQuery)
    {
        return snapshotService.collectionSalesStatistics(collectionSalesStatisticsQuery);
    }

    @PostMapping("/collectionSalesStatisticsExcel")
    @ApiOperation("规定时间指定藏品买卖数据导出")
    public void collectionSalesStatisticsExcel(@Valid CollectionSalesStatisticsQuery collectionSalesStatisticsQuery,HttpServletResponse response)
    {
        List<CollectionSalesStatisticsVo> collectionSalesStatisticsVoList = snapshotService.collectionSalesStatisticsExcel(collectionSalesStatisticsQuery);
        ExcelUtil<CollectionSalesStatisticsVo> util = new ExcelUtil<CollectionSalesStatisticsVo>(CollectionSalesStatisticsVo.class);
        util.exportExcel(response, collectionSalesStatisticsVoList, "规定时间指定藏品买卖数据导出");
    }

    @PostMapping("/collectionTotalNumber")
    @ApiOperation("规定时间指定一件或多件藏品的持有总量查询")
    public TableDataInfo<CollectionTotalNumberVo> collectionTotalNumber(@Valid @RequestBody CollectionTotalNumberQuery collectionTotalNumberQuery)
    {
        return snapshotService.collectionTotalNumber(collectionTotalNumberQuery);
    }

    @PostMapping("/collectionTotalNumberExcel")
    @ApiOperation("规定时间指定一件或多件藏品的持有总量数据导出")
    public void collectionTotalNumberExcel(@Valid CollectionTotalNumberQuery collectionTotalNumberQuery,HttpServletResponse response)
    {
        List<CollectionTotalNumberVo> collectionTotalNumberVoList = snapshotService.collectionTotalNumberExcel(collectionTotalNumberQuery);
        ExcelUtil<CollectionTotalNumberVo> util = new ExcelUtil<CollectionTotalNumberVo>(CollectionTotalNumberVo.class);
        util.exportExcel(response, collectionTotalNumberVoList, "规定时间指定一件或多件藏品的持有总量数据导出");
    }

    @PostMapping("/userCollectionNumber")
    @ApiOperation("每个用户藏品总量")
    public TableDataInfo<UserCollectionNumberVo> userCollectionNumber(@RequestBody UserCollectionNumberQuery userCollectionNumberQuery)
    {
        return snapshotService.userCollectionNumber(userCollectionNumberQuery);
    }

    @PostMapping("/userCollectionNumberExcel")
    @ApiOperation("每个用户藏品总量数据导出")
    public void userCollectionNumberExcel(UserCollectionNumberQuery userCollectionNumberQuery,HttpServletResponse response)
    {
        List<UserCollectionNumberVo> userCollectionNumberVoList = snapshotService.userCollectionNumberExcel(userCollectionNumberQuery);
        ExcelUtil<UserCollectionNumberVo> util = new ExcelUtil<UserCollectionNumberVo>(UserCollectionNumberVo.class);
        util.exportExcel(response, userCollectionNumberVoList, "每个用户藏品总量数据导出");
    }

}
