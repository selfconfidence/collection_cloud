package com.manyun.business.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Banner;
import com.manyun.business.domain.vo.BannerVo;
import com.manyun.business.service.IBannerService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.pays.abs.impl.AliComm;
import com.manyun.common.pays.abs.impl.WxComm;
import io.swagger.annotations.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 轮播表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "首页轮播相关")
public class BannerController {


    @Autowired
    private IBannerService bannerService;



    /**
     * 跳转逻辑流转
     * @return
     */
    @GetMapping("/list/{type}")
    @ApiOperation(value = "查询轮播列表,最多只返回6条",notes = "无需查询轮播详情，根据返回的 jumpLink 标识符去判定流转逻辑")
    public R<List<BannerVo>> list(@PathVariable Integer type){
        //LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(bannerService.list(Wrappers.<Banner>lambdaQuery().eq(Banner::getBannerType,type).orderByDesc(Banner::getCreatedTime,Banner::getUpdatedTime).last(" limit 6")).parallelStream().map(item ->{
            BannerVo bannerVo = Builder.of(BannerVo::new).build();
            BeanUtil.copyProperties(item,bannerVo);
            return bannerVo;
        }).collect(Collectors.toList()));
    }
}

