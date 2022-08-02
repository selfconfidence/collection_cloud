package com.manyun.admin.controller;

import java.util.List;

import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
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
import com.manyun.common.security.annotation.RequiresPermissions;
import com.manyun.admin.domain.CntAnnouncement;
import com.manyun.admin.service.ICntAnnouncementService;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 公告Controller
 *
 * @author yanwei
 * @date 2022-07-25
 */
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告apis")
public class CntAnnouncementController extends BaseController
{
    @Autowired
    private ICntAnnouncementService cntAnnouncementService;

    /**
     * 查询公告列表
     */
    @RequiresPermissions("admin:announcement:list")
    @GetMapping("/list")
    @ApiOperation("查询公告列表")
    public TableDataInfo<CntAnnouncement> list(PageQuery pageQuery)
    {
        return cntAnnouncementService.selectCntAnnouncementList(pageQuery);
    }

    /**
     * 获取公告详细信息
     */
    @RequiresPermissions("admin:announcement:query")
    @GetMapping(value = "/{id}")
    @ApiOperation("获取公告详细信息")
    public R<CntAnnouncement> getInfo(@PathVariable("id") String id)
    {
        return R.ok(cntAnnouncementService.selectCntAnnouncementById(id));
    }

    /**
     * 新增公告
     */
    @RequiresPermissions("admin:announcement:add")
    @Log(title = "公告", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation("新增公告")
    public R add(@RequestBody CntAnnouncement cntAnnouncement)
    {
        return toResult(cntAnnouncementService.insertCntAnnouncement(cntAnnouncement));
    }

    /**
     * 修改公告
     */
    @RequiresPermissions("admin:announcement:edit")
    @Log(title = "公告", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation("修改公告")
    public R edit(@RequestBody CntAnnouncement cntAnnouncement)
    {
        return toResult(cntAnnouncementService.updateCntAnnouncement(cntAnnouncement));
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("admin:announcement:remove")
    @Log(title = "公告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation("删除公告")
    public R remove(@PathVariable String[] ids)
    {
        return toResult(cntAnnouncementService.deleteCntAnnouncementByIds(ids));
    }
}
