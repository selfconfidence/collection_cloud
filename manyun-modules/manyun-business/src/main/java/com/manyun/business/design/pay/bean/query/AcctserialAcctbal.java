package com.manyun.business.design.pay.bean.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class AcctserialAcctbal {
    //账务日期。交易账期，格式：yyyyMMdd
    private String date_acct;
    //账户号。
    private String oid_acctno;
    //资金流水号。ACCP账务系统资金流水唯一标识。
    private String jno_acct;
    //该笔资金流水对应的ACCP系统交易单号：accp_txno。
    private String accp_txnno;
    /*
    交易类型。
    用户充值：USER_TOPUP
    商户充值：MCH_TOPUP
    普通消费：GENERAL_CONSUME
    担保消费：SECURED_CONSUME
    手续费收取：SERVICE_FEE
    内部代发：INNER_FUND_EXCHANGE
    外部代发：OUTER_FUND_EXCHANGE
    账户提现：ACCT_CASH_OUT
    担保确认：SECURED_CONFIRM
    手续费应收应付核销：CAPITAL_CANCEL
    定向内部代发：INNER_DIRECT_EXCHANGE
     */
    private String txn_type;
    /*
    产品编码
    消费API	CONSUMPTION_API
    消费页面	CONSUMPTION_WEB
    充值API	DEPOSIT_API
    充值页面	DEPOSIT_WEB
    普通代发(内部)API	NORMAL_TRANSFER_ACCOUNT_API
    普通代发(内部)WEB	NORMAL_TRANSFER_ACCOUNT_WEB
    普通代发(外部)API	NORMAL_TRANSFER_CARD_API
    普通代发(外部)WEB	NORMAL_TRANSFER_CARD_WEB
    垫资代发(内部)API	FUNDS_TRANSFER_ACCOUNT_API
    垫资代发(内部)WEB	FUNDS_TRANSFER_ACCOUNT_WEB
    垫资代发(外部)API	FUNDS_TRANSFER_CARD_API
    垫资代发(外部)WEB	FUNDS_TRANSFER_CARD_WEB
    普通提现API	NORMAL_WITHDRAW_API
    普通提现WEB	NORMAL_WITHDRAW_WEB
    垫资提现API	FUNDS_WITHDRAW_API
    垫资提现WEB	FUNDS_WITHDRAW_WEB
    内部调账	NTERNAL_ACCOUNT_TRANSFER
    日终结转	DAILY_SETTLEMENT
     */
    private String product_code;
    //商户系统交易时间。     格式：yyyyMMddHHmmss
    private String txn_time;
    //账户出入账标识   DEBIT：出账    CREDIT：入账
    private String flag_dc;
    //出入账金额。单位 元。
    private String amt;
    //交易后余额。单位 元
    private String amt_bal;
    //资金流水备注
    private String memo;
    //商户订单号。资金流水对应的商户交易单号，一条商户订单号可能对应多条资金流水。
    private String jno_cli;
}
