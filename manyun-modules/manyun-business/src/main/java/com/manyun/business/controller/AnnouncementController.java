package com.manyun.business.controller;


import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.vo.AnnouncementVo;
import com.manyun.business.service.IAnnouncementService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author qxh
 * @since 2022-06-23
 */
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告列表")
public class AnnouncementController extends BaseController {

    @Autowired
    private IAnnouncementService announcementService;

    @PostMapping("/list")
    @ApiOperation("查询公告")
    public R<TableDataInfo<AnnouncementVo>> list (@RequestBody PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<AnnouncementVo> list = announcementService.list(pageQuery);
        return R.ok(getDataTable(list));

    }

}

