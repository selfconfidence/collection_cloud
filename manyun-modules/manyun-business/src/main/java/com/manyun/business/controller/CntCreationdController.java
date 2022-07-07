package com.manyun.business.controller;


import cn.hutool.core.bean.BeanUtil;
import com.manyun.business.domain.entity.CntCreationd;
import com.manyun.business.domain.vo.CnfCreationdVo;
import com.manyun.business.service.ICntCreationdService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 创作者表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/cnfCreationd")
@Api(tags = "创作者相关Apis")
public class CntCreationdController {

    @Autowired
    private ICntCreationdService cntCreationdService;


    @GetMapping("/info/{id}")
    @ApiOperation("根据创作者编号查询详情")
    public R<CnfCreationdVo> info(@PathVariable String id){
        CntCreationd cntCreationd = cntCreationdService.getById(id);
        CnfCreationdVo cnfCreationdVo = Builder.of(CnfCreationdVo::new).build();
        BeanUtil.copyProperties(cntCreationd, cnfCreationdVo);
        return R.ok(cnfCreationdVo);
    }

}

