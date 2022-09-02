package com.manyun.admin.controller;


import com.manyun.admin.domain.query.AirdropRecordQuery;
import com.manyun.admin.domain.vo.CntAirdropRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.admin.service.ICntAirdropRecordService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 空投记录Controller
 *
 * @author yanwei
 * @date 2022-09-02
 */
@RestController
@RequestMapping("/airdropRecord")
@Api(tags = "空投记录apis")
public class CntAirdropRecordController extends BaseController
{
    @Autowired
    private ICntAirdropRecordService cntAirdropRecordService;

    /**
     * 查询空投记录列表
     */
    //@RequiresPermissions("admin:record:list")
    @GetMapping("/list")
    @ApiOperation("查询空投记录列表")
    public TableDataInfo<CntAirdropRecordVo> list(AirdropRecordQuery airdropRecordQuery)
    {
        return cntAirdropRecordService.selectCntAirdropRecordList(airdropRecordQuery);
    }

}
