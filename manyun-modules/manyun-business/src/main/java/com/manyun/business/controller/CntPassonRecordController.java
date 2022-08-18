package com.manyun.business.controller;

import com.manyun.business.domain.vo.PassonRecordVo;
import com.manyun.business.service.ICntPassonRecordService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.PageUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cntPassonRecord")
@Api(tags = "转赠列表apis")
public class CntPassonRecordController {
    @Autowired
    private ICntPassonRecordService cntPassonRecordService;

    @PostMapping("/passonRecordList")
    @ApiOperation("分页查询我的转赠记录")
    public R<TableDataInfo<PassonRecordVo>> passonRecordList(@RequestBody PageQuery pageQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(cntPassonRecordService.passonList(notNullLoginBusinessUser.getUserId(), pageQuery));
    }
}
