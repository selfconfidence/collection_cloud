package com.manyun.admin.controller;

import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.service.MyChainxSystemService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 重试上链Controller
 *
 * @author yanwei
 * @date 2022-07-25
 */
@RestController
@RequestMapping("/mychain")
@Api(tags = "重试上链apis")
public class MyChainxSystemController extends BaseController {

    @Autowired
    private MyChainxSystemService chainxSystemService;

    @GetMapping("/list")
    @ApiOperation("查询重试上链列表")
    public TableDataInfo<MyChainxVo> list()
    {
        startPage();
        List<MyChainxVo> list = chainxSystemService.list();
        return getDataTable(list);
    }

    @PutMapping
    @ApiOperation("重新上链")
    public R edit(@Valid @RequestBody MyChainxDto myChainxDto)
    {
        return chainxSystemService.update(myChainxDto);
    }

}
