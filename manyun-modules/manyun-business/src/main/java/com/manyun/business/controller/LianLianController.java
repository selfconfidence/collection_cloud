package com.manyun.business.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.design.pay.LLPayUtils;
import com.manyun.business.design.pay.bean.QueryRefundResult;
import com.manyun.business.design.pay.bean.changePayPass.*;
import com.manyun.business.design.pay.bean.closePayment.ClosePaymentResult;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardApplyResult;
import com.manyun.business.design.pay.bean.individual.IndividualBindCardVerifyResult;
import com.manyun.business.design.pay.bean.individual.UnlinkedacctIndApplyResult;
import com.manyun.business.design.pay.bean.morePayeeRefund.MorePayeeRefundResult;
import com.manyun.business.design.pay.bean.queryPayment.QueryPaymentResult;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.query.*;
import com.manyun.business.domain.vo.*;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IMoneyService;
import com.manyun.business.service.ISystemService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.LianLianPayEnum;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
 * ????????????
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/lianlian")
@Api(tags = "????????????apis")
public class LianLianController {

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private ILogsService logsService;

    @Autowired
    private RemoteBuiUserService userService;

    @Value("${open.url}")
    private String url;

    @GetMapping("/isAccountOpening")
    @ApiOperation(value = "????????????",notes = ("?????????   ?????????"))
    public R<String> isAccountOpening(){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        return R.ok("0".equalsIgnoreCase(money.getLlAccountStatus())?"?????????":"?????????");
    }

    @PostMapping("/innerUser")
    @ApiOperation(value = "????????????")
    public R<String> innerUser(@RequestBody InnerUserVo innerUserVo){
        Assert.isTrue(StringUtils.isNotBlank(innerUserVo.getReturnUrl()),"??????????????????!");
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        R<CntUserDto> cntUserDtoR = userService.commUni(loginBusinessUser.getUserId(), SecurityConstants.INNER);
        if(cntUserDtoR.getData().getIsReal()==1)return R.fail("??????????????????,????????????!");
        String userId = loginBusinessUser.getUserId();
        String phone = cntUserDtoR.getData().getPhone();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,userId));
        Assert.isFalse("1".equals(money.getLlAccountStatus()),"???????????????!");
        Assert.isTrue(Objects.nonNull(money),"??????????????????!");
        return R.ok(
                LLPayUtils.innerUser(
                Builder.of(LLInnerUserQuery::new)
                        .with(LLInnerUserQuery::setUserId,userId)
                        .with(LLInnerUserQuery::setPhone,phone)
                        .with(LLInnerUserQuery::setRealName,money.getRealName())
                        .with(LLInnerUserQuery::setCartNo,money.getCartNo())
                        .with(LLInnerUserQuery::setReturnUrl,innerUserVo.getReturnUrl())
                        .with(LLInnerUserQuery::setNotifyurl,(url+ LianLianPayEnum.INNER_USER.getNotifyUrl()))
                        .build()
                )
        );
    }

    @GetMapping("/withdrawServerCharge")
    @ApiOperation(value = "?????????????????????",notes = "?????????????????? ?????????")
    public R<BigDecimal> consignmentServerCharge(){
        return R.ok(systemService.getVal(BusinessConstants.SystemTypeConstant.WITHDRAW_CHARGE,BigDecimal.class));
    }

    @PostMapping("/withdraw")
    @ApiOperation("????????????")
    public R<Map<String,String>> withdraw(@RequestBody LLWithdrawQuery llWithdrawQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        BigDecimal val = systemService.getVal(BusinessConstants.SystemTypeConstant.WITHDRAW_CHARGE, BigDecimal.class);
        return R.ok(LLPayUtils.withdraw(loginBusinessUser.getUserId(),llWithdrawQuery.getRandomKey(),llWithdrawQuery.getPassWord(),llWithdrawQuery.getAmount(),val,(url+LianLianPayEnum.WITHDRAW.getNotifyUrl()), loginBusinessUser.getCntUser().getPhone(), DateUtils.getDateToStr(DateUtils.toDate(loginBusinessUser.getCntUser().getCreatedTime()),DateUtils.YYYYMMDDHHMMSS)));
    }

    @PostMapping("/validationSms")
    @ApiOperation("????????????????????????")
    public R<Map<String,String>> validationSms(@RequestBody LLValidationSmsQuery llValidationSmsQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.validationSms(loginBusinessUser.getUserId(),llValidationSmsQuery.getTxnSeqno(),llValidationSmsQuery.getAmount(),llValidationSmsQuery.getToken(),llValidationSmsQuery.getVerifyCode()));
    }

    @PostMapping("/userTopup")
    @ApiOperation("??????")
    public R<String> userTopup(@RequestBody LLUserTopupQuery llUserTopupQuery) {
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        CntUserDto cntUser = loginBusinessUser.getCntUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        Assert.isTrue(Objects.nonNull(money),"??????????????????!");
        return R.ok(LLPayUtils.userTopup(
                loginBusinessUser.getUserId(),
                money.getRealName(),
                loginBusinessUser.getCntUser().getPhone(),
                money.getCartNo(),
                loginBusinessUser.getIpaddr(),
                DateUtils.getDateToStr(DateUtils.toDate(cntUser.getCreatedTime()),DateUtils.YYYYMMDDHHMMSS),
                llUserTopupQuery.getAmount(),
                llUserTopupQuery.getReturnUrl()
                ,(url+LianLianPayEnum.USER_TOPUP.getNotifyUrl())));
    }

    @GetMapping("/queryAcctinfo")
    @ApiOperation(value = "????????????")
    public R<LlBalanceVo> queryAcctinfo(){
        LoginBusinessUser loginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,loginBusinessUser.getUserId()));
        if("0".equalsIgnoreCase(money.getLlAccountStatus()))return R.ok(Builder.of(LlBalanceVo::new).with(LlBalanceVo::setTotalBalance,new BigDecimal(0)).with(LlBalanceVo::setWithdrawBalance,new BigDecimal(0)).with(LlBalanceVo::setPsettleBalance,new BigDecimal(0)).build());
        return R.ok(LLPayUtils.queryAcctinfo(loginBusinessUser.getUserId()));
    }


    @PostMapping("/queryAcctserial")
    @ApiOperation("??????????????????")
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
    @ApiOperation("??????????????????")
    public R<GetRandomVo> getRandom(@RequestBody getRandomQuery getRandomQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.getRandom(notNullLoginBusinessUser.getUserId(),getRandomQuery.getFlagChnl(),getRandomQuery.getPkgName(),getRandomQuery.getAppName()));
    }


    @GetMapping("/queryLinkedacct")
    @ApiOperation("??????????????????")
    public R<List<LinkedacctVo>> queryLinkedacct() {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.queryLinkedacct(notNullLoginBusinessUser.getUserId())
                .parallelStream().map(m->{
                   return Builder.of(LinkedacctVo::new)
                   .with(LinkedacctVo::setLinkedPhone,m.getLinked_phone())
                   .with(LinkedacctVo::setLinkedBrbankname,m.getLinked_brbankname())
                   .with(LinkedacctVo::setLinkedAcctno,m.getLinked_acctno())
                   .build();
                }).collect(Collectors.toList())
                );
    }


    @PostMapping("/bindCardApply")
    @ApiOperation("??????????????????????????????")
    public R<BindCardVo> bindCardApply(@Valid @RequestBody BindCardQuery bindCardQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        IndividualBindCardApplyResult individualBindCardApplyResult = LLPayUtils.bindCardApply(
                notNullLoginBusinessUser.getUserId(),
                bindCardQuery.getLinkedAcctno(),
                bindCardQuery.getLinkedPhone(),
                bindCardQuery.getPassword(),
                bindCardQuery.getRandomKey(),
                (url + LianLianPayEnum.BIND_CARD_APPLY.getNotifyUrl())
        );
        return R.ok(
                Builder.of(BindCardVo::new)
                .with(BindCardVo::setTxnSeqno,individualBindCardApplyResult.getTxn_seqno())
                .with(BindCardVo::setToken,individualBindCardApplyResult.getToken())
                .build()
        );
    }


    @PostMapping("/bindCardVerify")
    @ApiOperation("??????????????????????????????")
    public R bindCardVerify(@Valid @RequestBody BindCardVerifyQuery bindCardVerifyQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        IndividualBindCardVerifyResult individualBindCardVerifyResult = LLPayUtils.bindCardVerify(
                notNullLoginBusinessUser.getUserId(),
                bindCardVerifyQuery.getTxnSeqno(),
                bindCardVerifyQuery.getToken(),
                bindCardVerifyQuery.getVerifyCode()
        );
        Assert.isTrue(Objects.nonNull(individualBindCardVerifyResult),individualBindCardVerifyResult.getRet_msg());
        return R.ok();
    }


    @PostMapping("/indApply")
    @ApiOperation("???????????????????????????")
    public R indApply(@Valid @RequestBody IndApplyQuery indApplyQuery) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        UnlinkedacctIndApplyResult unlinkedacctIndApplyResult = LLPayUtils.indApply(
                notNullLoginBusinessUser.getUserId(),
                indApplyQuery.getLinkedAcctno(),
                indApplyQuery.getPassword(),
                indApplyQuery.getRandomKey(),
                (url + LianLianPayEnum.IND_APPLY.getNotifyUrl())
        );
        Assert.isTrue(Objects.nonNull(unlinkedacctIndApplyResult),unlinkedacctIndApplyResult.getRet_msg());
        return R.ok();
    }


    @GetMapping("/queryPayment/{txnSeqno}")
    @ApiOperation(value = "??????????????????",notes = "txnSeqno==???????????????")
    public R<QueryPaymentResult> queryPayment(@PathVariable String txnSeqno) {
        return R.ok(LLPayUtils.queryPayment(txnSeqno));
    }


    @PostMapping("/morePayeeRefund")
    @ApiOperation("????????????")
    public R<MorePayeeRefundResult> morePayeeRefund(@Valid @RequestBody MorePayeeRefundQuery morePayeeRefundQuery) {
        return R.ok(LLPayUtils.morePayeeRefund(morePayeeRefundQuery,(url + LianLianPayEnum.MORE_PAYEE_REFUND.getNotifyUrl())));
    }

    @GetMapping("/queryRefund/{refundSeqno}")
    @ApiOperation(value = "??????????????????",notes = "refundSeqno==???????????????")
    public R<QueryRefundResult> queryRefund(@PathVariable String refundSeqno) {
        return R.ok(LLPayUtils.queryRefund(refundSeqno));
    }

    @PostMapping("/changeLianLianPayPass")
    @ApiOperation("??????????????????")
    public R<ChangePayPassResult> changeLianLianPayPass(@Valid @RequestBody ChangePayPassForm changePayPassForm) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.changeLianLianPayPass(changePayPassForm, notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/changeLianLianPayPassVerify")
    @ApiOperation("??????????????????")
    public R<ChangePayPassVerifyResult> changeLianLianPayPassVerify(@Valid @RequestBody ChangePayPassVerifyForm changePayPassVerifyForm) {
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(LLPayUtils.changeLianLianPayPassVerify(changePayPassVerifyForm, notNullLoginBusinessUser.getUserId()));
    }

    @GetMapping("/closePayment/{txnSeqno}")
    @ApiOperation(value = "???????????????",notes = "txnSeqno==?????????????????????????????????")
    public R<ClosePaymentResult> closePayment(@PathVariable String txnSeqno) {
        return R.ok(LLPayUtils.closePayment(txnSeqno));
    }

}

