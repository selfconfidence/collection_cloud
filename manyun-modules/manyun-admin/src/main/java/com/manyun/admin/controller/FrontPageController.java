package com.manyun.admin.controller;

import com.manyun.admin.domain.vo.FrontPageVo;
import com.manyun.admin.service.IFrontPageService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontPage")
@Api(tags = "首页apis")
public class FrontPageController extends BaseController
{

    @Autowired
    private IFrontPageService frontPageService;

    @GetMapping("/list")
    @ApiOperation("首页")
    public R<FrontPageVo> list()
    {
        return frontPageService.list();
    }

}
