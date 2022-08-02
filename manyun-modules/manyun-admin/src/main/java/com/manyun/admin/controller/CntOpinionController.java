package com.manyun.admin.controller;

import java.util.List;

import com.manyun.admin.domain.vo.CntOpinionVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntOpinion;
import com.manyun.admin.service.ICntOpinionService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 产品建议Controller
 *
 * @author yanwei
 * @date 2022-07-26
 */
@RestController
@RequestMapping("/opinion")
@Api(tags = "产品建议apis")
public class CntOpinionController extends BaseController
{
    @Autowired
    private ICntOpinionService cntOpinionService;

    /**
     * 查询产品建议列表
     */
    //@RequiresPermissions("admin:opinion:list")
    @GetMapping("/list")
    @ApiOperation("查询产品建议列表")
    public TableDataInfo<CntOpinionVo> list(PageQuery pageQuery)
    {
        return cntOpinionService.selectCntOpinionList(pageQuery);
    }

    /**
     * 修改产品建议
     */
    //@RequiresPermissions("admin:opinion:edit")
    @Log(title = "产品建议", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改产品建议")
    public R edit(@RequestBody CntOpinion cntOpinion)
    {
        return toResult(cntOpinionService.updateCntOpinion(cntOpinion));
    }

}
