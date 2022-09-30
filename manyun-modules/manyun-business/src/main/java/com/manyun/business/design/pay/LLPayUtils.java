package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.manyun.business.design.pay.bean.OpenacctApplyAccountInfo;
import com.manyun.business.design.pay.bean.OpenacctApplyBasicInfo;
import com.manyun.business.design.pay.bean.OpenacctApplyParams;
import com.manyun.business.design.pay.bean.OpenacctApplyResult;
import com.manyun.business.design.pay.bean.cashier.*;
import com.manyun.business.design.pay.bean.individual.*;
import com.manyun.business.design.pay.bean.query.*;
import com.manyun.business.design.pay.bean.random.GetRandomParams;
import com.manyun.business.design.pay.bean.random.GetRandomResult;
import com.manyun.business.design.pay.bean.txn.*;
import com.manyun.business.domain.query.*;
import com.manyun.business.domain.vo.GetRandomVo;
import com.manyun.business.domain.vo.LlBalanceVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.LianLianPayEnum;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.pays.utils.llpay.LLianPayDateUtils;
import com.manyun.common.pays.utils.llpay.client.LLianPayClient;
import com.manyun.common.pays.utils.llpay.config.LLianPayConstant;
import com.manyun.common.pays.utils.llpay.security.LLianPayAccpSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 连连支付 相关api
 */
@Component
public class LLPayUtils {

    //开户
    private final static String openacctApply = "https://accpgw.lianlianpay.com/v1/acctmgr/openacct-apply";
    //绑卡申请
    private final static String individualBindcardApply = "https://accpapi.lianlianpay.com/v1/acctmgr/individual-bindcard-apply";
    //绑卡验证
    private final static String individualBindcardVerify = "https://accpapi.lianlianpay.com/v1/acctmgr/individual-bindcard-verify";
    //解绑申请
    private final static String unlinkedacctIndApply = "https://accpapi.lianlianpay.com/v1/acctmgr/unlinkedacct-ind-apply";
    //提现申请
    private final static String withdrawal = "https://accpapi.lianlianpay.com/v1/txn/withdrawal";
    //交易二次短信验证
    private final static String validationSms = "https://accpapi.lianlianpay.com/v1/txn/validation-sms";
    //充值丶消费
    private final static String paycreate = "https://accpgw.lianlianpay.com/v1/cashier/paycreate";
    //查询余额
    private final static String queryAcctinfo = "https://accpapi.lianlianpay.com/v1/acctmgr/query-acctinfo";
    //查询绑卡列表
    private final static String queryLinkedacct = "https://accpapi.lianlianpay.com/v1/acctmgr/query-linkedacct";
    //查询资金流水列表
    private final static String queryAcctserial = "https://accpapi.lianlianpay.com/v1/acctmgr/query-acctserial";
    //随机因子获取
    private final static String getRandom = "https://accpapi.lianlianpay.com/v1/acctmgr/get-random";

    //使用时,需确认用户实名状况,必须是实名用户
    /**
     * 开户
     * @param innerUserQuery
     * @return 开户的url地址
     */
    public static String innerUser(LLInnerUserQuery innerUserQuery) {
        Assert.isTrue(Objects.nonNull(innerUserQuery),"请求参数有误!");
        Assert.isTrue(
                StringUtils.isNotBlank(innerUserQuery.getUserId()) ||  StringUtils.isNotBlank(innerUserQuery.getRealName()) ||
                            StringUtils.isNotBlank(innerUserQuery.getPhone()) ||  StringUtils.isNotBlank(innerUserQuery.getCartNo()) ||
                            StringUtils.isNotBlank(innerUserQuery.getReturnUrl()),"请求参数有误!"
        );
        OpenacctApplyParams params = new OpenacctApplyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(innerUserQuery.getUserId());
        params.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        params.setTxn_time(timestamp);
        /*
        交易发起渠道。
        ANDROID
        IOS
        H5
        PC
         */
        params.setFlag_chnl("H5");
        // 交易完成回跳页面地址，H5/PC渠道必传。
        params.setReturn_url(innerUserQuery.getReturnUrl());
        // 交易结果异步通知接收地址，建议HTTPS协议。
        params.setNotify_url(innerUserQuery.getNotifyurl());
        /*
        用户类型。
        INNERUSER：个人用户
        INNERCOMPANY：企业用户
         */
        params.setUser_type("INNERUSER");

        // 设置开户账户申请信息
        OpenacctApplyAccountInfo accountInfo = new OpenacctApplyAccountInfo();
        /*
        个人支付账户  PERSONAL_PAYMENT_ACCOUNT
        企业支付账户  ENTERPRISE_PAYMENT_ACCOUNT
         */
        accountInfo.setAccount_type("PERSONAL_PAYMENT_ACCOUNT");
        accountInfo.setAccount_need_level("V3");
        params.setAccountInfo(accountInfo);

        //开户基本信息
        OpenacctApplyBasicInfo basicInfo = new OpenacctApplyBasicInfo();
        basicInfo.setUser_name(innerUserQuery.getRealName());
        basicInfo.setReg_phone(innerUserQuery.getPhone());
        basicInfo.setId_type("ID_CARD");
        basicInfo.setId_no(innerUserQuery.getCartNo());
        params.setBasicInfo(basicInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(openacctApply, JSON.toJSONString(params));
        OpenacctApplyResult openacctApplyResult = JSON.parseObject(resultJsonStr, OpenacctApplyResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(openacctApplyResult.getRet_code()),openacctApplyResult.getRet_msg());
        return openacctApplyResult.getGateway_url();
    }


    /**
     * 提现
     * @param   randomKey 随机因子key
     * @param   userId 用户Id
     * @param   passWord 支付密码
     * @param   amount 提现金额
     * @param   val 提现手续费 百分比
     */
    public static Map<String,String> withdraw(String userId,String randomKey, String passWord, BigDecimal amount,BigDecimal val, String notifyUrl, String phone, String registerTime) {
        List<LinkedAcctlist> linkedAcctlists = LLPayUtils.queryLinkedacct(userId);
        Assert.isTrue(linkedAcctlists.size()>0,"请求参数有误!");
        Assert.isTrue(amount.compareTo(BigDecimal.valueOf(1)) > 0, "提现金额需大于1元");
        Map<String,String> map = new HashMap<String, String>();
        WithDrawalParams params = new WithDrawalParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setNotify_url(notifyUrl);
        params.setPay_time_type("TRANS_NEXT_TIME");
        params.setRisk_item(
                "{" +
                        "\"frms_ware_category\":\"4007\"," +
                        "\"goods_name\":\"用户提现\"," +
                        "\"user_info_mercht_userno\":\"" +userId+ "\"," +
                        "\"user_info_dt_register\":\"" +registerTime+ "\"," +
                        "\"user_info_bind_phone\":\"" +phone+ "\"," +
                        "}"
        );
        params.setLinked_agrtno(linkedAcctlists.parallelStream().map(LinkedAcctlist::getLinked_agrtno).findFirst().get());

        // 设置商户订单信息
        WithDrawalOrderInfo orderInfo = new WithDrawalOrderInfo();
        orderInfo.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(amount);
        if(val!=null){
        orderInfo.setFee_amount(amount.multiply(val).compareTo(BigDecimal.valueOf(1)) > 0 ? amount.multiply(val) : BigDecimal.valueOf(1));
        }
        orderInfo.setPostscript("用户提现");
        params.setOrderInfo(orderInfo);

        // 设置付款方信息
        WithDrawalPayerInfo payerInfo = new WithDrawalPayerInfo();
        payerInfo.setPayer_type("USER");
        payerInfo.setPayer_id(userId);
        // 用户：LLianPayTest-In-User-12345 密码：qwerty，本地测试环境测试，没接入密码控件，使用本地加密方法加密密码（仅限测试环境使用）
        payerInfo.setPassword(passWord);
        payerInfo.setRandom_key(randomKey);
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(withdrawal, JSON.toJSONString(params));
        WithDrawalResult drawalResult = JSON.parseObject(resultJsonStr, WithDrawalResult.class);

        // 小额免验，不需要验证码，直接返回0000
        if ("0000".equals(drawalResult.getRet_code())) {
            map.put("code",drawalResult.getRet_code());
            map.put("msg","提现申请成功!");
            return map;
        }else if("8888".equals(drawalResult.getRet_code())){
            map.put("code",drawalResult.getRet_code());
            map.put("msg","提现需要再次信息短信验证码校验!");
            map.put("userId",drawalResult.getUser_id());
            map.put("txn_seqno",drawalResult.getTxn_seqno());
            map.put("total_amount",drawalResult.getTotal_amount().toString());
            map.put("token",drawalResult.getToken());
            return map;
        }
        map.put("code",drawalResult.getRet_code());
        map.put("msg",drawalResult.getRet_msg());
        return map;
    }


    /**
     * 交易二次短信验证
     * @param userId 用户id
     * @param txnSeqno 商户系统唯一交易流水号。【注：与创建交易时所传流水号相同】
     * @param amount 订单总金额，单位为元，精确到小数点后两位。
     * @param token  授权令牌，有效期为30分钟。
     * @param verifyCode  短信验证码。交易需要短信验证时发送给用户手机的验证码。
     */
    public static Map<String,String> validationSms(String userId, String txnSeqno,String amount,String token,String verifyCode) {
        Assert.isTrue(
        StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(txnSeqno) ||
                    StringUtils.isNotBlank(amount) || StringUtils.isNotBlank(token) ||
                    StringUtils.isNotBlank(verifyCode)
               ,"请求参数有误!");
        Map<String,String> map = new HashMap<>();
        ValidationSmsParams params=new ValidationSmsParams();
        params.setTimestamp(LLianPayDateUtils.getTimestamp());
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setPayer_type("USER");
        params.setPayer_id(userId);
        params.setTxn_seqno(txnSeqno);
        params.setTotal_amount(amount);
        params.setToken(token);
        params.setVerify_code(verifyCode);
        LLianPayClient lLianPayClient = new LLianPayClient();

        String resultJsonStr = lLianPayClient.sendRequest(validationSms, JSON.toJSONString(params));
        ValidationSmsResult validationSmsResult = JSON.parseObject(resultJsonStr, ValidationSmsResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(validationSmsResult.getRet_code()),validationSmsResult.getRet_msg());
        map.put("code",validationSmsResult.getRet_code());
        map.put("msg",validationSmsResult.getRet_msg());
        return map;
    }


    /**
     * 用户充值
     * @param userId 用户id
     * @param realName 用户真实姓名
     * @param phone 手机号
     * @param cartNo 身份证号
     * @param ipAddr
     * @param amount 充值金额
     * @param returnUrl ios Android h5 表单提交之后跳转回app的地址
     * @param notifyUrl 回调地址
     */
    public static String userTopup(String userId, String realName, String phone, String cartNo,  String ipAddr, String registerTime, BigDecimal amount, String returnUrl ,String notifyUrl) {
        CashierPayCreateParams params = new CashierPayCreateParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        // 用户充值
        params.setTxn_type("USER_TOPUP");
        params.setUser_id(userId);
        /*
        用户类型。默认：注册用户。
        注册用户：REGISTERED
        匿名用户：ANONYMOUS
         */
        params.setUser_type("REGISTERED");
        params.setNotify_url(notifyUrl);
        params.setReturn_url(returnUrl);
        // 交易发起渠道设置
        params.setFlag_chnl("H5");
        // 测试风控参数
        params.setRisk_item(
                "{" +
                        "\"frms_ware_category\":\"4007\"," +
                        "\"goods_name\":\"用户充值\"," +
                        "\"user_info_mercht_userno\":\"" +userId+ "\"," +
                        "\"user_info_dt_register\":\"" +registerTime+ "\"," +
                        "\"user_info_bind_phone\":\"" +phone+ "\"," +
                        "\"user_info_full_name\":\"" +realName+ "\"," +
                        "\"user_info_id_no\":\"" + cartNo + "\"," +
                        "\"user_info_identify_type\":\"4\"," +
                        "\"user_info_id_type\":\"0\"," +
                        "\"frms_client_chnl\":\" 16\"," +
                        "\"frms_ip_addr\":\"" +ipAddr+ "\"," +
                        "\"user_auth_flag\":\"1\"" +
                        "}"
        );


        // 设置商户订单信息
        CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
        orderInfo.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(amount);
        orderInfo.setGoods_name("用户充值");
        params.setOrderInfo(orderInfo);

        // 设置付款方信息
        CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
        payerInfo.setPayer_id(userId);
        payerInfo.setPayer_type("USER");
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(paycreate, JSON.toJSONString(params));
        CashierPayCreateResult cashierPayCreateResult = JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(cashierPayCreateResult.getRet_code()),cashierPayCreateResult.getRet_msg());
        return cashierPayCreateResult.getGateway_url();
    }


    /**
     * 普通消费
     * @param llGeneralConsumeQuery 消费所需参数
     * @param type 交易类型 type==true 用户交易用户 type==false 用户交易商户
     */
    public static String generalConsume(LLGeneralConsumeQuery llGeneralConsumeQuery,Boolean type) {
        CashierPayCreateParams params = new CashierPayCreateParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        // 普通消费
        params.setTxn_type("GENERAL_CONSUME");
        params.setUser_id(llGeneralConsumeQuery.getUserId());
        /*
        用户类型。默认：注册用户。
        注册用户：REGISTERED
        匿名用户：ANONYMOUS
         */
        params.setUser_type("REGISTERED");
        params.setNotify_url(llGeneralConsumeQuery.getNotifyUrl());
        params.setReturn_url(llGeneralConsumeQuery.getReturnUrl());
        // 交易发起渠道设置
        params.setFlag_chnl("H5");
        // 测试风控参数
        params.setRisk_item(
                "{" +
                        "\"frms_ware_category\":\"4007\"," +
                        "\"goods_name\":\"" +llGeneralConsumeQuery.getGoodsName()+ "\"," +
                        "\"user_info_mercht_userno\":\"" +llGeneralConsumeQuery.getUserId()+ "\"," +
                        "\"user_info_dt_register\":\"" +llGeneralConsumeQuery.getRegisterTime()+ "\"," +
                        "\"user_info_bind_phone\":\"" +llGeneralConsumeQuery.getPhone()+ "\"," +
                        "\"user_info_full_name\":\"" +llGeneralConsumeQuery.getRealName()+ "\"," +
                        "\"user_info_id_no\":\"" + llGeneralConsumeQuery.getCartNo() + "\"," +
                        "\"user_info_identify_state\":\"1\"," +
                        "\"user_info_identify_type\":\"4\"," +
                        "\"user_info_id_type\":\"0\"," +
                        "\"frms_client_chnl\":\" 16\"," +
                        "\"frms_ip_addr\":\"" +llGeneralConsumeQuery.getIpAddr()+ "\"," +
                        "\"user_auth_flag\":\"1\"" +
                        "}"
        );
        // 设置商户订单信息
        CashierPayCreateOrderInfo orderInfo = new CashierPayCreateOrderInfo();
        orderInfo.setTxn_seqno((llGeneralConsumeQuery.getOrderId()+"-"+LLianPayDateUtils.getTimestamp()));
        orderInfo.setTxn_time(timestamp);
        orderInfo.setTotal_amount(llGeneralConsumeQuery.getAmount());
        orderInfo.setGoods_name(llGeneralConsumeQuery.getGoodsName());
        params.setOrderInfo(orderInfo);

        // 设置收款方信息
        if(type){
            CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
            mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
            mPayeeInfo.setPayee_type("MERCHANT");
            mPayeeInfo.setPayee_amount(llGeneralConsumeQuery.getServiceCharge());
            mPayeeInfo.setPayee_memo("手续费");

            CashierPayCreatePayeeInfo uPayeeInfo = new CashierPayCreatePayeeInfo();
            uPayeeInfo.setPayee_id(llGeneralConsumeQuery.getPayeeUserId());
            uPayeeInfo.setPayee_type("USER");
            uPayeeInfo.setPayee_amount((llGeneralConsumeQuery.getAmount().subtract(llGeneralConsumeQuery.getServiceCharge())));
            uPayeeInfo.setPayee_memo("用户商品交易");
            params.setPayeeInfo(new CashierPayCreatePayeeInfo[]{mPayeeInfo, uPayeeInfo});
        }else {
            CashierPayCreatePayeeInfo mPayeeInfo = new CashierPayCreatePayeeInfo();
            mPayeeInfo.setPayee_id(LLianPayConstant.OidPartner);
            mPayeeInfo.setPayee_type("MERCHANT");
            mPayeeInfo.setPayee_amount(llGeneralConsumeQuery.getAmount());
            mPayeeInfo.setPayee_memo("用户购买商品");
            params.setPayeeInfo(new CashierPayCreatePayeeInfo[]{mPayeeInfo});
        }

        // 设置付款方信息
        CashierPayCreatePayerInfo payerInfo = new CashierPayCreatePayerInfo();
        payerInfo.setPayer_id(llGeneralConsumeQuery.getUserId());
        payerInfo.setPayer_type("USER");
        params.setPayerInfo(payerInfo);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(paycreate, JSON.toJSONString(params));
        CashierPayCreateResult cashierPayCreateResult = JSON.parseObject(resultJsonStr, CashierPayCreateResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(cashierPayCreateResult.getRet_code()),cashierPayCreateResult.getRet_msg());
        return cashierPayCreateResult.getGateway_url();
    }

    public static void main(String[] args) {
        BigDecimal num1 = new BigDecimal("10");
        BigDecimal num2 = new BigDecimal("0.2");
        System.out.println(num1.subtract(num1.multiply(num2)));
    }

    /**
     * 查询余额
     * @param userId 用户id
     */
    public static LlBalanceVo queryAcctinfo(String userId) {
        Assert.isTrue(StringUtils.isNotBlank(userId),"请求参数有误,请检查!");
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",LLianPayDateUtils.getTimestamp());
        map.put("oid_partner",LLianPayConstant.OidPartner);
        map.put("user_id",userId);
        map.put("user_type","INNERUSER");

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(queryAcctinfo, JSON.toJSONString(map));
        AcctInfoResult acctInfoResult = JSON.parseObject(resultJsonStr, AcctInfoResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(acctInfoResult.getRet_code()),acctInfoResult.getRet_msg());
        Optional<AcctinfoList> optional = acctInfoResult.getAcctinfo_list().parallelStream().filter(f -> ("USEROWN_AVAILABLE".equals(f.getAcct_type()) && "NORMAL".equals(f.getAcct_state()))).findFirst();
        Optional<AcctinfoList> optional1 = acctInfoResult.getAcctinfo_list().parallelStream().filter(f -> ("USEROWN_PSETTLE".equals(f.getAcct_type()) && "NORMAL".equals(f.getAcct_state()))).findFirst();
        if(!optional.isPresent() || !optional1.isPresent()){
            Assert.isTrue(Boolean.FALSE,"用户余额查询失败,请重试!");
        }
        //可用余额
        BigDecimal bigDecimal = new BigDecimal(optional.get().getAmt_balaval());
        //待结算余额
        BigDecimal bigDecimal1 = new BigDecimal(optional1.get().getAmt_balaval());
        return Builder.of(LlBalanceVo::new).with(LlBalanceVo::setTotalBalance,(bigDecimal.add(bigDecimal1))).with(LlBalanceVo::setWithdrawBalance,bigDecimal).with(LlBalanceVo::setPsettleBalance,bigDecimal1).build();
    }



    /**
     * 查询绑卡列表
     * @param userId 用户id
     */
    public static List<LinkedAcctlist> queryLinkedacct(String userId) {
        Assert.isTrue(StringUtils.isNotBlank(userId),"请求参数有误,请检查!");
        Map<String,String> map = new HashMap<>();
        map.put("timestamp",LLianPayDateUtils.getTimestamp());
        map.put("oid_partner",LLianPayConstant.OidPartner);
        map.put("user_id",userId);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(queryLinkedacct, JSON.toJSONString(map));
        LinkeDacctResult linkeDacctResult = JSON.parseObject(resultJsonStr, LinkeDacctResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(linkeDacctResult.getRet_code()),linkeDacctResult.getRet_msg());
        return linkeDacctResult.getLinked_acctlist();
    }



    /**
     * 个人用户新增绑卡申请
     */
    public static IndividualBindCardApplyResult bindCardApply(String userId, String linkedAcctno, String linkedPhone, String password, String randomKey, String notifyUrl) {
        IndividualBindCardApplyParams params = new IndividualBindCardApplyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(userId);
        params.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        params.setTxn_time(timestamp);
        params.setNotify_url(notifyUrl);
        // 设置银行卡号
        params.setLinked_acctno(linkedAcctno);
        // 设置绑卡手机号
        params.setLinked_phone(linkedPhone);
        // 设置钱包密码，正式环境要接密码控件，调试API可以用连连公钥加密密码
        params.setPassword(password);
        params.setRandom_key(randomKey);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(individualBindcardApply, JSON.toJSONString(params));
        IndividualBindCardApplyResult bindCardApplyResult = JSON.parseObject(resultJsonStr, IndividualBindCardApplyResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(bindCardApplyResult.getRet_code()),bindCardApplyResult.getRet_msg());
        return bindCardApplyResult;
    }


    /**
     * 个人用户新增绑卡验证
     */
    public static IndividualBindCardVerifyResult bindCardVerify(String userId, String txnSeqno, String token, String verifyCode) {
        IndividualBindCardVerifyParams params = new IndividualBindCardVerifyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(userId);
        params.setTxn_seqno(txnSeqno);
        params.setToken(token);
        // 测试环境首次绑卡，不下发短信验证码，任意6位数字
        params.setVerify_code(verifyCode);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(individualBindcardVerify, JSON.toJSONString(params));
        IndividualBindCardVerifyResult bindCardVerifyResult = JSON.parseObject(resultJsonStr, IndividualBindCardVerifyResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(bindCardVerifyResult.getRet_code()),bindCardVerifyResult.getRet_msg());
        return bindCardVerifyResult;
    }


    /**
     * 个人用户解绑银行卡
     */
    public static UnlinkedacctIndApplyResult indApply(String userId, String linkedAcctno, String password, String randomKey, String notifyUrl) {
        UnlinkedacctIndApplyParams params = new UnlinkedacctIndApplyParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(userId);
        params.setTxn_seqno(IdUtils.getSnowflakeNextIdStr());
        params.setTxn_time(timestamp);
        params.setNotify_url(notifyUrl);
        params.setLinked_acctno(linkedAcctno);
        params.setPassword(password);
        params.setRandom_key(randomKey);

        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(unlinkedacctIndApply, JSON.toJSONString(params));
        UnlinkedacctIndApplyResult unlinkedacctIndApplyResult = JSON.parseObject(resultJsonStr, UnlinkedacctIndApplyResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(unlinkedacctIndApplyResult.getRet_code()),unlinkedacctIndApplyResult.getRet_msg());
        return unlinkedacctIndApplyResult;
    }


    /**
     * 随机因子获取
     * @param
     */
    public static GetRandomVo getRandom(String userId, String flagChnl, String pkgName, String appName) {
        Assert.isTrue(StringUtils.isNotBlank(userId) || StringUtils.isNotBlank(flagChnl),"请求参数有误,请检查!");
        GetRandomParams params = new GetRandomParams();
        String timestamp = LLianPayDateUtils.getTimestamp();
        params.setTimestamp(timestamp);
        params.setOid_partner(LLianPayConstant.OidPartner);
        params.setUser_id(userId);
        params.setFlag_chnl(flagChnl);
        if(StringUtils.isNotBlank(pkgName)){
            params.setPkg_name(pkgName);
        }
        if(StringUtils.isNotBlank(appName)){
            params.setApp_name(appName);
        }
        params.setEncrypt_algorithm("RSA");
        LLianPayClient lLianPayClient = new LLianPayClient();
        String resultJsonStr = lLianPayClient.sendRequest(getRandom, JSON.toJSONString(params));
        GetRandomResult getRandomResult = JSON.parseObject(resultJsonStr, GetRandomResult.class);
        Assert.isTrue("0000".equalsIgnoreCase(getRandomResult.getRet_code()),getRandomResult.getRet_msg());
        return Builder.of(GetRandomVo::new)
                .with(GetRandomVo::setUserId,getRandomResult.getUser_id())
                .with(GetRandomVo::setRandomKey,getRandomResult.getRandom_key())
                .with(GetRandomVo::setRandomValue,getRandomResult.getRandom_value())
                .with(GetRandomVo::setLicense,getRandomResult.getLicense())
                .with(GetRandomVo::setRsaPublicContent,getRandomResult.getRsa_public_content())
                .with(GetRandomVo::setMapArr, getRandomResult.getMap_arr())
                .build();
    }

}
