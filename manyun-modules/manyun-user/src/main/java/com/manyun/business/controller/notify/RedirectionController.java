package com.manyun.business.controller.notify;

import cn.hutool.core.util.StrUtil;
import com.manyun.business.config.AliRealConfig;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.business.service.ICntUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/*
 * 重定向控制器
 *
 * @author yanwei
 * @create 2022-09-15
 */
@Controller
@RequestMapping("/real")
@Slf4j
public class RedirectionController {
    @Autowired
    private ICntUserService cntUserService;

    @Autowired
    private AliRealConfig aliRealConfig;

    @GetMapping("/h5RealRedirection")
    public void h5RealRedirection(@RequestParam("userId") String userId, HttpServletResponse response){
        log.info("h5RealRedirection returnUrl 来了");
        response.setStatus(302);
        try {
            // 验证开始
            CntUser cntUser = cntUserService.getById(userId);
            if (Objects.nonNull(cntUser) && StrUtil.isNotBlank(cntUser.getCertifyId()))
            cntUserService.checkCertifyIdH5Status(cntUser.getCertifyId(), userId);
            response.sendRedirect(aliRealConfig.getH5ReturnUrl());
        } catch (IOException e) {
            log.error("h5RealRedirection",e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
