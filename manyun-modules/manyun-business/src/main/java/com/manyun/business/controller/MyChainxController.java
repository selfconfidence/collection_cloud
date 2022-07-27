package com.manyun.business.controller;

import com.manyun.business.service.IUserCollectionService;
import com.manyun.common.core.domain.R;
import com.manyun.common.security.annotation.InnerAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mychain")
@Api(tags = "蚂蚁链上联服务apis",hidden = true)
public class MyChainxController {

    @Autowired
    private IUserCollectionService userCollectionService;


    /**
     * 重新上链接口
     * 适用于外部调用
     * @param userId 用户编号
     * @param userCollectionId 上联失败的用户和藏品中间表
     */
    @GetMapping("/resetUpLink")
    @ApiOperation(value = "重新上链",hidden = true)
    @InnerAuth
    public R resetUpLink(@RequestParam("userId") String userId,@RequestParam("userCollectionId") String userCollectionId){
        userCollectionService.resetUpLink(userId,userCollectionId);
        return R.ok();
    }









}
