package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.query.QueryConsignmentVo;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.admin.service.ICntConsignmentService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/consignment")
@Api(tags = "订单管理apis")
public class CntConsignmentController extends BaseController
{
    @Autowired
    private ICntConsignmentService cntConsignmentService;

    @GetMapping("/list")
    @ApiOperation("订单管理列表")
    public TableDataInfo list(QueryConsignmentVo queryConsignmentVo)
    {
        startPage();
        List<CntConsignmentVo> list = cntConsignmentService.selectCntConsignmentList(queryConsignmentVo);
        return getDataTable(list);
    }

}
