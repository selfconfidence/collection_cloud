package com.manyun.admin.controller;


import com.manyun.admin.domain.query.BannerJumpLinkQuery;
import com.manyun.admin.domain.vo.BannerJumpLinkVo;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.manyun.common.log.annotation.Log;
import com.manyun.common.log.enums.BusinessType;
import com.manyun.admin.domain.CntBannerJumpLink;
import com.manyun.admin.service.ICntBannerJumpLinkService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 轮播跳转链接Controller
 *
 * @author yanwei
 * @date 2022-08-30
 */
@RestController
@RequestMapping("/link")
@Api(tags = "轮播跳转链接apis")
public class CntBannerJumpLinkController extends BaseController
{
    @Autowired
    private ICntBannerJumpLinkService cntBannerJumpLinkService;

    /**
     * 查询轮播跳转链接列表
     */
    //@RequiresPermissions("admin:link:list")
    @GetMapping("/list")
    @ApiOperation("查询轮播跳转链接列表")
    public TableDataInfo<BannerJumpLinkVo> list(BannerJumpLinkQuery bannerJumpLinkQuery)
    {
        return cntBannerJumpLinkService.selectCntBannerJumpLinkList(bannerJumpLinkQuery);
    }

    /**
     * 获取轮播跳转链接详细信息
     */
    //@RequiresPermissions("admin:link:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取轮播跳转链接详细信息")
    public R<CntBannerJumpLink> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntBannerJumpLinkService.selectCntBannerJumpLinkById(id));
    }

    /**
     * 新增轮播跳转链接
     */
    //@RequiresPermissions("admin:link:add")
    @Log(title = "轮播跳转链接", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增轮播跳转链接")
    public R add(@RequestBody CntBannerJumpLink cntBannerJumpLink)
    {
        return toResult(cntBannerJumpLinkService.insertCntBannerJumpLink(cntBannerJumpLink));
    }

    /**
     * 修改轮播跳转链接
     */
    //@RequiresPermissions("admin:link:edit")
    @Log(title = "轮播跳转链接", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改轮播跳转链接")
    public R edit(@RequestBody CntBannerJumpLink cntBannerJumpLink)
    {
        return toResult(cntBannerJumpLinkService.updateCntBannerJumpLink(cntBannerJumpLink));
    }

    /**
     * 删除轮播跳转链接
     */
    //@RequiresPermissions("admin:link:remove")
    @Log(title = "轮播跳转链接", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除轮播跳转链接")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntBannerJumpLinkService.deleteCntBannerJumpLinkByIds(ids));
    }
}
