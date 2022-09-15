package com.manyun.business.controller;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.design.pay.LLPayUtils;
import com.manyun.business.design.pay.bean.query.AcctserialAcctbal;
import com.manyun.business.design.pay.bean.query.LinkedAcctlist;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.query.*;
import com.manyun.business.domain.vo.AcctSerIalVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 连连相关
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/lianlian")
@Api(tags = "连连相关apis")
public class LianLianController {

    @Autowired
    private IMoneyService moneyService;

    @GetMapping("/innerUser/{returnUrl}")
    @ApiOperation(value = "连连开户")
    public R<String> innerUser(@PathVariable String returnUrl){
        Assert.isTrue(StringUtils.isNotBlank(returnUrl),"请求参数有误!");
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        if(loginBusinessUser.getCntUser().getIsReal()==1)return R.fail("用户暂未实名,请先实名!");
        String userId = loginBusinessUser.getUserId();
        String phone = loginBusinessUser.getCntUser().getPhone();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,userId));
        if("1".equals(money.getLlAccountStatus())){
            return R.fail("0000","用户已开户!");
        }
        Assert.isTrue(Objects.nonNull(money),"请求参数有误!");
        return R.ok(
                LLPayUtils.innerUser(
                Builder.of(LLInnerUserQuery::new)
                        .with(LLInnerUserQuery::setUserId,userId)
                        .with(LLInnerUserQuery::setPhone,phone)
                        .with(LLInnerUserQuery::setRealName,money.getRealName())
                        .with(LLInnerUserQuery::setCartNo,money.getCartNo())
                        .with(LLInnerUserQuery::setReturnUrl,returnUrl)
                        .build()
                )
        );
    }

    @PostMapping("/withdraw")
    @ApiOperation("提现申请")
    public R<Map<String,String>> withdraw(@RequestBody LLWithdrawQuery llWithdrawQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.withdraw(loginBusinessUser.getUserId(),llWithdrawQuery.getPassWord(),llWithdrawQuery.getAmount()));
    }

    @PostMapping("/validationSms")
    @ApiOperation("交易二次短信验证")
    public R<Map<String,String>> validationSms(@RequestBody LLValidationSmsQuery llValidationSmsQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.validationSms(loginBusinessUser.getUserId(),llValidationSmsQuery.getTxnSeqno(),llValidationSmsQuery.getAmount(),llValidationSmsQuery.getToken(),llValidationSmsQuery.getVerifyCode()));
    }

    @PostMapping("/userTopup")
    @ApiOperation("充值")
    public R<String> userTopup(@RequestBody LLUserTopupQuery llUserTopupQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        Assert.isTrue(Objects.nonNull(money),"请求参数有误!");
        return R.ok(LLPayUtils.userTopup(loginBusinessUser.getUserId(),money.getRealName(),loginBusinessUser.getCntUser().getPhone(),loginBusinessUser.getIpaddr(),llUserTopupQuery.getAmount(),llUserTopupQuery.getReturnUrl()));
    }

    @GetMapping("/queryAcctinfo")
    @ApiOperation(value = "查询余额")
    public R<String> queryAcctinfo(){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.queryAcctinfo(loginBusinessUser.getUserId()));
    }

    @PostMapping("/queryAcctserial")
    @ApiOperation("查询资金流水")
    public R<AcctSerIalVo> queryAcctserial(@RequestBody LLAcctserialQuery llAcctserialQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Date start = llAcctserialQuery.getStartDate();
        Date end = llAcctserialQuery.getEndDate();
        String startDate="";
        String endDate="";
        if(start!=null)startDate = DateUtils.getDateToStr(start,DateUtils.YYYYMMDDHHMMSS);
        if(end!=null)endDate = DateUtils.getDateToStr(end,DateUtils.YYYYMMDDHHMMSS);
        return R.ok(LLPayUtils.queryAcctserial(loginBusinessUser.getUserId(), start == null ? "" : endDate, end == null ? "" : endDate, llAcctserialQuery.getPageNo(), llAcctserialQuery.getPageSize()));
    }

}

