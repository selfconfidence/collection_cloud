package com.manyun.business.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.design.pay.LLPayUtils;
import com.manyun.business.design.pay.bean.query.AcctserialAcctbal;
import com.manyun.business.design.pay.bean.query.LinkedAcctlist;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.query.*;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IMoneyService;
import com.manyun.business.service.ISystemService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.LL_MONEY_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;

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

    @Autowired
    private ISystemService systemService;

    @Autowired
    private ILogsService logsService;

    @GetMapping("/isAccountOpening")
    @ApiOperation(value = "是否开户",notes = ("已开户   未开户"))
    public R<String> isAccountOpening(){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        return R.ok("0".equalsIgnoreCase(money.getLlAccountStatus())?"未开户":"已开户");
    }

    @PostMapping("/innerUser")
    @ApiOperation(value = "连连开户")
    public R<String> innerUser(@RequestBody InnerUserVo innerUserVo){
        Assert.isTrue(StringUtils.isNotBlank(innerUserVo.getReturnUrl()),"请求参数有误!");
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        if(loginBusinessUser.getCntUser().getIsReal()==1)return R.fail("用户暂未实名,请先实名!");
        String userId = loginBusinessUser.getUserId();
        String phone = loginBusinessUser.getCntUser().getPhone();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,userId));
        Assert.isFalse("1".equals(money.getLlAccountStatus()),"用户已开户!");
        Assert.isTrue(Objects.nonNull(money),"请求参数有误!");
        return R.ok(
                LLPayUtils.innerUser(
                Builder.of(LLInnerUserQuery::new)
                        .with(LLInnerUserQuery::setUserId,userId)
                        .with(LLInnerUserQuery::setPhone,phone)
                        .with(LLInnerUserQuery::setRealName,money.getRealName())
                        .with(LLInnerUserQuery::setCartNo,money.getCartNo())
                        .with(LLInnerUserQuery::setReturnUrl,innerUserVo.getReturnUrl())
                        .build()
                )
        );
    }

    @GetMapping("/withdrawServerCharge")
    @ApiOperation(value = "连连提现手续费",notes = "返回连连提现 百分比")
    public R<BigDecimal> consignmentServerCharge(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.WITHDRAW_CHARGE,BigDecimal.class));
    }

    @PostMapping("/withdraw")
    @ApiOperation("提现申请")
    public R<Map<String,String>> withdraw(@RequestBody LLWithdrawQuery llWithdrawQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        BigDecimal val = systemService.getVal(BusinessConstants.SystemTypeConstant.WITHDRAW_CHARGE, BigDecimal.class);
        return R.ok(LLPayUtils.withdraw(loginBusinessUser.getUserId(),llWithdrawQuery.getPassWord(),llWithdrawQuery.getAmount(),val));
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
    public R<LlBalanceVo> queryAcctinfo(){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        if("0".equalsIgnoreCase(money.getLlAccountStatus()))return R.ok(Builder.of(LlBalanceVo::new).with(LlBalanceVo::setTotalBalance,new BigDecimal(0)).with(LlBalanceVo::setWithdrawBalance,new BigDecimal(0)).with(LlBalanceVo::setPsettleBalance,new BigDecimal(0)).build());
        return R.ok(LLPayUtils.queryAcctinfo(loginBusinessUser.getUserId()));
    }

    public static void main(String[] args) {
        LlBalanceVo llBalanceVo = new LlBalanceVo();
        String a = "485780.43";
        String b = "4015.00";
        BigDecimal aa = new BigDecimal(a);
        BigDecimal bb = new BigDecimal(b);
        System.out.println(llBalanceVo.getTotalBalance());
    }

    @PostMapping("/queryAcctserial")
    @ApiOperation("查询资金流水")
    public R<TableDataInfo<MoneyLogVo>> queryAcctserial(@RequestBody MoneyLogQuery moneyLogQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        PageHelper.startPage(moneyLogQuery.getPageNum(),moneyLogQuery.getPageSize());
        List<Logs> logsList = logsService.list(Wrappers.<Logs>lambdaQuery().eq(Logs::getBuiId, notNullLoginBusinessUser.getUserId()).eq(Logs::getModelType, LL_MONEY_MODEL_TYPE).eq(Objects.nonNull(moneyLogQuery.getIsType()), Logs::getIsType, moneyLogQuery.getIsType()).apply(Objects.nonNull(moneyLogQuery.getCreatedTime()), "  DATE_FORMAT(created_time,'%Y-%m-%d') = '" + moneyLogQuery.getCreatedTime() + "' ").orderByDesc(Logs::getCreatedTime));
        List<MoneyLogVo> moneyLogVos = logsList.parallelStream().map(this::initMoneyLogVo).collect(Collectors.toList());
        return R.ok(TableDataInfoUtil.pageTableDataInfo(moneyLogVos,logsList));
    }

    private MoneyLogVo initMoneyLogVo(Logs logs) {
        MoneyLogVo moneyLogVo = Builder.of(MoneyLogVo::new).build();
        BeanUtil.copyProperties(logs,moneyLogVo);
        return moneyLogVo;
    }


    @PostMapping("/getRandom")
    @ApiOperation("随机因子获取")
    public R<GetRandomVo> getRandom(@RequestBody getRandomQuery getRandomQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.getRandom(notNullLoginBusinessUser.getUserId(),getRandomQuery.getFlagChnl(),getRandomQuery.getPkgName(),getRandomQuery.getAppName()));
    }

}

