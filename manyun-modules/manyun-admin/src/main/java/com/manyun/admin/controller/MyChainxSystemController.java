package com.manyun.admin.controller;

import cn.hutool.core.lang.Assert;
import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.admin.service.MyChainxSystemService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public TableDataInfo<MyChainxVo> list(PageQuery pageQuery)
    {
        return chainxSystemService.list(pageQuery);
    }

    @PutMapping
    @ApiOperation("重新上链")
    public R edit(@Valid @RequestBody MyChainxDto myChainxDto)
    {
        return chainxSystemService.update(myChainxDto);
    }

    @PostMapping("/bachChains")
    @ApiOperation("批量重新上链")
    public R bachChains(@RequestBody List<MyChainxDto> myChainxDtoList)
    {
        Assert.isTrue(myChainxDtoList.size()>0, "上链失败!");
        List<MyChainxDto> myChainxDtos = myChainxDtoList.parallelStream().filter(f -> (!"".equals(f.getId()) && !"".equals(f.getUserId()))).collect(Collectors.toList());
        Thread thread = new Thread() {
            @Override
            public void run() {
                myChainxDtos.parallelStream().forEach(e->{
                    chainxSystemService.update(e);
                });
            }
        };
        thread.start();
        return R.ok();
     }

}
