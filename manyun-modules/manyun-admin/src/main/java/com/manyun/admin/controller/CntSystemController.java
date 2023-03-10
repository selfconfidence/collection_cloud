package com.manyun.admin.controller;


import com.manyun.admin.domain.dto.PosterDto;
import com.manyun.admin.domain.query.SystemQuery;
import com.manyun.admin.domain.vo.CntSystemVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.service.ICntSystemService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

import javax.validation.Valid;

/**
 * 平台规则Controller
 *
 * @author yanwei
 * @date 2022-07-28
 */
@RestController
@RequestMapping("/cntSystem")
@Api(tags = "平台规则apis")
public class CntSystemController extends BaseController
{
    @Autowired
    private ICntSystemService cntSystemService;

    /**
     * 查询平台规则列表
     */
    //@RequiresPermissions("admin:system:list")
    @GetMapping("/list")
    @ApiOperation("查询平台规则列表")
    public TableDataInfo<CntSystemVo> list(SystemQuery systemQuery)
    {
        return cntSystemService.selectCntSystemList(systemQuery);
    }

    /**
     * 获取平台规则详细信息
     */
    //@RequiresPermissions("admin:system:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取平台规则详细信息")
    public R<CntSystemVo> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntSystemService.selectCntSystemById(id));
    }

    /**
     * 修改平台规则
     */
    //@RequiresPermissions("admin:system:edit")
    @Log(title = "平台规则", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改平台规则")
    public R edit(@RequestBody CntSystemVo cntSystemVo)
    {
        return toResult(cntSystemService.updateCntSystem(cntSystemVo));
    }


    /**
     * 查询邀请海报详情
     */
    @GetMapping("queryPosterInfo")
    @ApiOperation("查询邀请海报详情")
    public R<PosterDto> queryPosterInfo()
    {
        return R.ok(cntSystemService.queryPosterInfo());
    }

    /**
     * 更新邀请海报
     */
    @PostMapping("updatePoster")
    @ApiOperation("更新邀请海报")
    public R updatePoster(@Valid @RequestBody PosterDto posterDto)
    {
        return toResult(cntSystemService.updatePoster(posterDto));
    }

    /**
     * 查询用户默认头像
     */
    @GetMapping("queryUserDeafultAvatar")
    @ApiOperation("查询用户默认头像")
    public R<PosterDto> queryUserDeafultAvatar()
    {
        return R.ok(cntSystemService.queryUserDeafultAvatar());
    }

    /**
     * 更新用户默认头像
     */
    @PostMapping("updateUserDeafultAvatar")
    @ApiOperation("更新用户默认头像")
    public R updateUserDeafultAvatar(@Valid @RequestBody PosterDto posterDto)
    {
        return toResult(cntSystemService.updateUserDeafultAvatar(posterDto));
    }

    /**
     * 查询合成动图
     */
    @GetMapping("querySyntheticAnimation")
    @ApiOperation("查询合成动图")
    public R<PosterDto> querySyntheticAnimation()
    {
        return R.ok(cntSystemService.querySyntheticAnimation());
    }

    /**
     * 更新活动动图
     */
    @PostMapping("updateSyntheticAnimation")
    @ApiOperation("更新合成动图")
    public R updateSyntheticAnimation(@Valid @RequestBody PosterDto posterDto)
    {
        return toResult(cntSystemService.updateSyntheticAnimation(posterDto));
    }

    /**
     * 查询开盲合动图
     */
    @GetMapping("queryOpenBoxGif")
    @ApiOperation("查询开盲合动图")
    public R<PosterDto> queryOpenBoxGif()
    {
        return R.ok(cntSystemService.queryOpenBoxGif());
    }

    /**
     * 更新开盲合动图
     */
    @PostMapping("updateOpenBoxGif")
    @ApiOperation("更新开盲合动图")
    public R updateOpenBoxGif(@Valid @RequestBody PosterDto posterDto)
    {
        return toResult(cntSystemService.updateOpenBoxGif(posterDto));
    }

}
