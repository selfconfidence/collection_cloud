package com.manyun.business.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.comm.api.domain.dto.AccountInfoDto;
import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.annotation.InnerAuth;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;

import static com.manyun.common.core.constant.BusinessConstants.RedisDict.USER_ACTIVE_NUMBERS;

/**
 * <p>
 * 钱包表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/money")
@Api(tags = "钱包相关Apis")
public class MoneyController extends BaseController {
    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private RedisService redisService;


    @GetMapping("/thisMoney")
    @ApiOperation(value = "查看自己钱包余额",notes = "返回钱包余额")
    public R<BigDecimal>  thisMoney(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money moneyUser = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, notNullLoginBusinessUser.getUserId()));
        redisService.setActives(USER_ACTIVE_NUMBERS, notNullLoginBusinessUser.getUserId());
        return R.ok(moneyUser.getMoneyBalance());
    }

    @GetMapping("/userActives")
    @ApiOperation(value = "查看用户日活",hidden = true)
    @Deprecated
    public R userActives(){
        return R.ok(redisService.getActives(USER_ACTIVE_NUMBERS));
    }


    @PostMapping("/accountInfo")
    @ApiOperation("查询收款账户信息")
    public R<AccountInfoVo> accountInfo(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(moneyService.accountInfo(notNullLoginBusinessUser.getUserId()));
    }


    @GetMapping("/userMoneyById/{userId}")
    @ApiOperation(value = "根据用户编号查询钱包",notes = "返回钱包余额")
    @InnerAuth
    public R<AccountInfoDto>  userMoneyById(@PathVariable String userId){
        AccountInfoVo accountInfoVo = moneyService.accountInfo(userId);
        AccountInfoDto accountInfoDto = Builder.of(AccountInfoDto::new).build();
        BeanUtil.copyProperties(accountInfoVo, accountInfoDto);
        return R.ok(accountInfoDto);
    }

    @PostMapping("/updateAccountInfo")
    @ApiOperation("完善收款账户信息")
    public R updateAccountInfo(@RequestBody @Valid AccountInfoForm accountInfoForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        moneyService.updateAccountInfo(notNullLoginBusinessUser.getUserId(),accountInfoForm);
        return R.ok();
    }

    @PostMapping("/pageMoneyLog")
    @ApiOperation("分页查询钱包的收支记录")
    public R<TableDataInfo<MoneyLogVo>> pageMoneyLog(@RequestBody MoneyLogQuery moneyLogQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(moneyService.pageMoneyLog(notNullLoginBusinessUser.getUserId(),moneyLogQuery));
    }



    @GetMapping("/bindCard")
    @ApiOperation("调取银联 一键绑卡 -暂未实现 ")
    @Deprecated
    public R bindCard(){
        return R.fail();
    }

    @PostMapping("/fullMoney")
    @ApiOperation("钱包充值")
    @Deprecated
    public R fullMoney(){
        return R.fail();
    }

    @GetMapping("/initUserMoney/{userId}")
    @ApiOperation(value = "初始化钱包",hidden = true)
    @InnerAuth
    public R initUserMoney(@PathVariable String userId){
        moneyService.initUserMoney(userId);
       return R.ok();
    }

    @PostMapping("/updateUserMoney")
    @ApiOperation(value = "实名认证绑定数据",hidden = true)
    @InnerAuth
    public R updateUserMoney(@RequestBody UserRealMoneyForm userRealMoneyForm) {
        return moneyService.updateUserMoney(userRealMoneyForm);
    }

    @GetMapping("/checkIdentity/{identityNo}")
    @ApiOperation(value = "检查是否已实名过", hidden = true)
    @InnerAuth
    public R checkIdentity(@PathVariable String identityNo) {
        return moneyService.checkIdentity(identityNo);
    }




}

