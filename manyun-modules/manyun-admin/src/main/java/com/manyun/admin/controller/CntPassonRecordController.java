package com.manyun.admin.controller;


import com.manyun.admin.domain.query.PassonRecordQuery;
import com.manyun.admin.domain.vo.CntPassonRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.service.ICntPassonRecordService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 转赠记录Controller
 *
 * @author yanwei
 * @date 2022-08-18
 */
@RestController
@RequestMapping("/passonRecord")
@Api(tags = "转赠记录apis")
public class CntPassonRecordController extends BaseController
{
    @Autowired
    private ICntPassonRecordService cntPassonRecordService;

    /**
     * 查询转赠记录列表
     */
    //@RequiresPermissions("admin:record:list")
    @GetMapping("/list")
    @ApiOperation("查询转赠记录列表")
    public TableDataInfo<CntPassonRecordVo> list(PassonRecordQuery passonRecordQuery)
    {
        return cntPassonRecordService.selectCntPassonRecordList(passonRecordQuery);
    }
}
