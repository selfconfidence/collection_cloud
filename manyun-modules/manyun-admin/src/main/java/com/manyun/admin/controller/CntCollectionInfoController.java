package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.query.CollectionInfoQuery;
import com.manyun.admin.domain.vo.CntCollectionInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.admin.service.ICntCollectionInfoService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/collectionInfo")
@Api(tags = "发行方apis")
public class CntCollectionInfoController extends BaseController
{
    @Autowired
    private ICntCollectionInfoService cntCollectionInfoService;

    //@RequiresPermissions("admin:info:list")
    @GetMapping("/list")
    @ApiOperation("查询发行方列表")
    public TableDataInfo<CntCollectionInfoVo> list(CollectionInfoQuery collectionInfoQuery)
    {
        return cntCollectionInfoService.selectCntCollectionInfoList(collectionInfoQuery);
    }
}
