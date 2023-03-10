package com.manyun.business.controller;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import com.manyun.business.service.ISystemService;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.DelayLevelEnum;
import com.manyun.common.mq.producers.deliver.DeliverProducer;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import com.manyun.common.security.annotation.InnerAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.*;

/**
 * <p>
 * 平台规则表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/system")
@Api(tags = "部分固定内容APis")
public class SystemController {

    @Autowired
    private ISystemService systemService;



/*    @Autowired
    private RocketMQTemplate rocketMQTemplate;*/


    @GetMapping("/sellInfo")
    @ApiOperation("购买须知")
    public R<String>  sellInfo() {
        return R.ok(systemService.getVal(SELL_INFO,String.class));
    }



    @GetMapping("/collectionInfo")
    @ApiOperation("关于藏品")
    public R<String>  collectionInfo(){
        return R.ok(systemService.getVal(COLLECTION_INFO,String.class));
    }

    @GetMapping("/findType/{type}")
    @ApiOperation(value = "根据类型查询对应得业务规则",hidden = true)
    @InnerAuth
    public R<String> findType(@PathVariable String type){
        return R.ok(systemService.getVal(type,String.class));
    }

    @GetMapping("/openBoxGif")
    @ApiOperation("开盲盒动效图")
    public R<String> openBoxGif() {
        return R.ok(systemService.getVal(OPEN_BOX_GIF, String.class));
    }

    @GetMapping("/synthesisGif")
    @ApiOperation("合成动效图")
    public R<String> synthesisGif() {
        return R.ok(systemService.getVal(SYNTHESIS_GIF, String.class));
    }

    @GetMapping("/withdrawInfo")
    @ApiOperation("连连余额提现规则")
    public R<String> withdrawInfo() {
        return R.ok(systemService.getVal(WITHDRAW_INFO, String.class));
    }

    @GetMapping("/systemWithdrawCharge")
    @ApiOperation("平台余额提现手续费比例")
    public R<BigDecimal> systemWithdrawCharge() {
        return R.ok(systemService.getVal(SYSTEM_WITHDRAW_CHARGE, BigDecimal.class));
    }

    @GetMapping("/systemWithdrawInfo")
    @ApiOperation("平台余额提现规则")
    public R<String> systemWithdrawInfo() {
        return R.ok(systemService.getVal(SYSTEM_WITHDRAW_INFO, String.class));
    }

    @GetMapping("/consignmentInfo")
    @ApiOperation("寄售须知")
    public R<String> consignmentInfo() {
        return R.ok(systemService.getVal(CONSIGNMENT_INFO, String.class));
    }

    @PostMapping("/testRedisson")
    @Lock("testRedisson")
    public R<String> testRedisson() throws InterruptedException {
        return R.ok(systemService.testRedisson());
    }

    @PostMapping("/testRedisson2")
    @Lock("testRedisson")
    public R<String> testRedisson2(){
        return R.ok();
    }

}

