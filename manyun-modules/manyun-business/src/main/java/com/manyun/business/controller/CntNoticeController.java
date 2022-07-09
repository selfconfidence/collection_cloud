package com.manyun.business.controller;


import com.manyun.business.domain.query.NoticeQuery;
import com.manyun.business.domain.vo.CntNoticeVo;
import com.manyun.business.service.ICntNoticeService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 通知公告表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-07-08
 */
@RestController
@RequestMapping("/cntNotice")
public class CntNoticeController {

    @Autowired
    private ICntNoticeService noticeService;

    @PostMapping("/pageNoticeList")
    @ApiOperation("分页查询公告相关")
    public R<TableDataInfo<CntNoticeVo>> pageNoticeList(@RequestBody NoticeQuery noticeQuery){
        return R.ok(noticeService.pageNoticeList(noticeQuery));
    }

}

