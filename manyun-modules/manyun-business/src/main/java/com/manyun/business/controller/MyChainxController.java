package com.manyun.business.controller;

import com.manyun.business.design.mychain.MyChainService;
import com.manyun.business.service.IUserCollectionService;
import com.manyun.comm.api.domain.dto.CallAccountDto;
import com.manyun.comm.api.domain.vo.ChainAccountVo;
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

    @Autowired
    private MyChainService myChainService;


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


    /**
     * 创建账户
     * 适用于外部调用
     * @param callAccountDto 内部用户信息
     */
    @PostMapping("/accountCreate")
    @ApiOperation(value = "重新上链",hidden = true)
    @InnerAuth
    public R<String> accountCreate(@RequestBody CallAccountDto callAccountDto){
        return R.ok(myChainService.accountCreate(callAccountDto));
    }


    /**
     * 创建账户
     * 真实开户地址
     */
    @GetMapping("/createInit/{account}")
    @ApiOperation("创建账户")
    @InnerAuth
    public R<ChainAccountVo>  createInit(@PathVariable String account){
        return R.ok(myChainService.createInit(account));
    }






}
