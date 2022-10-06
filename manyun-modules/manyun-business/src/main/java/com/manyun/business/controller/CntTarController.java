package com.manyun.business.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.CntTar;
import com.manyun.business.service.ICntTarService;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.security.annotation.InnerAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.manyun.common.core.enums.TarResultFlag.FLAG_PROCESS;

/**
 * <p>
 * 抽签规则(盲盒,藏品) 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/tar")
@Api(tags = "抽签相关apis")
@Slf4j
public class CntTarController{


    @Autowired
    private ICntTarService cntTarService;


    @GetMapping("/taskEndFlag")
    @ApiOperation(value = "定时调度执行开奖", hidden = true)
    @InnerAuth
    @Lock(value = "taskEndFlag",expireTime = 600000) // 10分钟过期
    public R taskEndFlag(){
        // 查询出将要开奖的抽签主体
        List<CntTar> cntTarList = cntTarService.list(Wrappers.<CntTar>lambdaQuery().eq(CntTar::getEndFlag, FLAG_PROCESS.getCode()).le(CntTar::getOpenTime, LocalDateTime.now()));
        if (!cntTarList.isEmpty())log.info("本次抽签公布个数有{}",cntTarList.size());
        for (CntTar cntTar : cntTarList) {
            cntTarService.openTarResult(cntTar.getId());
        }
        return R.ok();
    }


}

