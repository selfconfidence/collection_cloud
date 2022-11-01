package com.manyun.business.design.pay.bean.sandEnum;

import cn.com.sand.ceas.sdk.busi.BusinessTypeConsts;
import cn.com.sand.ceas.sdk.enums.SandEnum;

import java.util.Arrays;

/**
 * @title: SandMethodEnum
 * @description: 所有method的枚举
 * @author: sandpay
 * @create: 2021-02-04 14:50
 * @since: JDK8
 */
public enum SandMethodEnum implements SandEnum {
    /***账户-start***/
    CEAS_ELEC_MEMBER_INFO("ceas.elec.member.info.query", BusinessTypeConsts.ELECACCOUNT, "开户信息查询"),

    CEAS_ELEC_PAPERS_UPLOAD("ceas.elec.file.upload", BusinessTypeConsts.ELECACCOUNT, "附件上传"),

    CEAS_ACCOUNT_REGISTER_OPEN_SERVER("ceas.elec.account.register.open", BusinessTypeConsts.ELECACCOUNT, "会员注册实名并开户"),

    CEAS_ELEC_BIND_CARD("ceas.elec.bind.card", BusinessTypeConsts.ELECACCOUNT, "关联银行卡"),

    CEAS_ELEC_QUERY_ACCOUNT_BALANCE("ceas.elec.account.balance.query", BusinessTypeConsts.ELECACCOUNT, "账户余额查询"),

    CEAS_ELEC_BIND_CARD_QUERY("ceas.elec.bind.card.query", BusinessTypeConsts.ELECACCOUNT, "关联卡信息查询"),

    CEAS_ELEC_UNBIND_CARD("ceas.elec.unbind.card", BusinessTypeConsts.ELECACCOUNT, "解绑关联卡"),

    CEAS_ELEC_ACCOUNT_PROTOCOL_SIGN("ceas.elec.account.protocol.sign", BusinessTypeConsts.ELECACCOUNT, "协议签约"),

    CEAS_ELEC_MEMBER_STATUS_QUERY("ceas.elec.member.status.query", BusinessTypeConsts.ELECACCOUNT, "会员状态查询"),

    CEAS_ELEC_ACC_CHANGE_DETAILS("ceas.elec.acc.change.details", BusinessTypeConsts.ELECACCOUNT, "账户变动明细查询"),

    CEAS_ELEC_ACCOUNT_PAY_PASSWORD_QUERY("ceas.elec.account.pay.password.query", BusinessTypeConsts.ELECACCOUNT, "密码状态查询"),

    CEAS_ELEC_ACCOUNT_PAY_PASSWORD_MANAGE("ceas.elec.account.pay.password.manage", BusinessTypeConsts.ELECACCOUNT, "密码管理"),

    CEAS_ELEC_ACCOUNT_PROTOCOL_OPEN("ceas.elec.account.protocol.open", BusinessTypeConsts.ELECACCOUNT, "一键开户"),

    CEAS_ELEC_ACCOUNT_MEMBER_STATUS_MODIFY("ceas.elec.account.member.status.modify", BusinessTypeConsts.ELECACCOUNT, "会员状态管理"),

    CEAS_ELEC_ACCOUNT_MEMBER_MODIFY_CONFIRM("ceas.elec.account.member.modify.confirm", BusinessTypeConsts.ELECACCOUNT, "账户操作确认"),

    CEAS_ELEC_ACCOUNT_PROTOCOL_MANAGE("ceas.elec.account.protocol.manage", BusinessTypeConsts.ELECACCOUNT, "协议管理"),

    CEAS_ELEC_ACCOUNT_QUICK_BINDCARD_OPEN("ceas.elec.account.quick.bindcard.open", BusinessTypeConsts.ELECACCOUNT, "个人会员绑卡并开户"),

    CEAS_ELEC_BIND_CARD_CONFIRM("ceas.elec.bind.card.confirm", BusinessTypeConsts.ELECACCOUNT, "绑卡确认"),

    /***账户-end***/



    /***交易-start***/
    CEAS_SERVER_SMS_SEND("ceas.elec.sms.send", BusinessTypeConsts.ELECTRANS, "发送短信"),

    CEAS_SERVER_BYF_BUY("ceas.elec.trans.byf.buy", BusinessTypeConsts.ELECTRANS, "权益申购"),

    CEAS_SERVER_TRANSFER_CORP("ceas.elec.trans.corp.transfer", BusinessTypeConsts.ELECTRANS, "转账（企业转个人）"),

    CEAS_SERVER_ORDER_QUERY("ceas.elec.trans.order.query", BusinessTypeConsts.ELECTRANS, "订单状态查询"),

    CEAS_SERVER_BILL_QUERY("ceas.elec.trans.bill.query", BusinessTypeConsts.ELECTRANS, "获取对账单下载链接"),

    CEAS_SERVER_WITHDRAW_APPLY("ceas.elec.trans.withdraw.apply", BusinessTypeConsts.ELECTRANS, "提现申请"),

    CEAS_SERVER_TRANSFER_PERSON("ceas.elec.trans.transfer.apply", BusinessTypeConsts.ELECTRANS, "转账申请（会员转出）"),

    CEAS_SERVER_ORDER_CONFIRM("ceas.elec.trans.order.confirm", BusinessTypeConsts.ELECTRANS, "资金操作确认（订单确认）"),

    CEAS_SERVER_BOUNTY_INNUES("ceas.elec.trans.bounty.issues", BusinessTypeConsts.ELECTRANS, "奖励金下发"),

    CEAS_SERVER_FUND_FREEZE("ceas.elec.trans.member.account.fund.freeze", BusinessTypeConsts.ELECTRANS, "资金冻结解冻"),

    CEAS_SERVER_RECEIVE_CONFIRM("ceas.elec.trans.receive.confirm", BusinessTypeConsts.ELECTRANS, "收款资金确认"),

    CEAS_SERVER_DEPOSIT_APPLY("ceas.elec.trans.quick.deposit.apply", BusinessTypeConsts.ELECTRANS, "云账户充值"),

    CEAS_SERVER_PAYMENT_DEPOSIT("ceas.elec.trans.third.payment.deposit", BusinessTypeConsts.ELECTRANS, "云账户后台充值下单"),

    CEAS_SERVER_PAY_CODE("ceas.elec.trans.pay.code.apply", BusinessTypeConsts.ELECTRANS, "付款码申请"),

    CEAS_SERVER_HB_ISSUE("ceas.elec.trans.hb.issue", BusinessTypeConsts.ELECTRANS, "红包发放"),

    CEAS_SERVER_HB_RECEIVE("ceas.elec.trans.hb.receive", BusinessTypeConsts.ELECTRANS, "红包领取"),

    CEAS_SERVER_HB_QUERY("ceas.elec.trans.hb.query", BusinessTypeConsts.ELECTRANS, "红包查询"),

    CEAS_SERVER_PAYMENT_APPLY("ceas.elec.trans.payment.apply", BusinessTypeConsts.ELECTRANS, "付款申请"),

    CEAS_SERVER_GUARANTEE_CONFIRM("ceas.elec.trans.guarantee.confirm", BusinessTypeConsts.ELECTRANS, "担保确认"),

    CEAS_SERVER_TRANS_INFO_QUERY("ceas.elec.trans.info.query", BusinessTypeConsts.ELECTRANS, "交易订单查询"),


    ;

    /***交易-end***/

    SandMethodEnum(String methodCode, String businessType, String methodDesc) {
        this.methodCode = methodCode;
        this.businessType = businessType;
        this.methodDesc = methodDesc;
    }

    private String methodCode; // 方法code

    private String businessType; // 业务类型

    private String methodDesc; // 方法描述


    public String getMethodCode() {
        return methodCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    /**
     * 通过方法code获取枚举信息
     *
     * @param methodCode 方法code
     * @return
     * @date: 2021-03-10 10:11
     */
    public static SandMethodEnum get(String methodCode) {
        return Arrays.stream(SandMethodEnum.values()).filter(e -> e.getMethodCode().equals(methodCode)).findFirst().orElse(null);
    }
}
