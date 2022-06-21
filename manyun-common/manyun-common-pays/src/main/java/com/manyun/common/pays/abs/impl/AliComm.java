package com.manyun.common.pays.abs.impl;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.Participant;
import com.ijpay.alipay.AliPayApi;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.AliPayEnum;
import com.manyun.common.core.utils.uuid.UUID;
import com.manyun.common.pays.abs.CommPayAbs;
import com.manyun.common.pays.config.AliPayConfig;
import com.manyun.common.pays.model.PayAliModel;
import com.manyun.common.pays.model.PayWxModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

/**
 * 支付宝 相关 支付 & 用户等
 */
@Slf4j
public class AliComm extends CommPayAbs {
    AliPayConfig aliPayApiConfig ;

    public AliComm(AliPayConfig aliPayApiConfig) {
        this.aliPayApiConfig = aliPayApiConfig;
    }

    /**
     *   单笔商家对个人付款
     *    最新版
     * @param aliAccount 收款人支付宝账号 .
     * @param amount     转账多少钱，元为单位.
     * @param realName   支付宝真实姓名
     * @throws AlipayApiException
     */
    public void aliTransfer(String aliAccount, BigDecimal amount, String realName) throws AlipayApiException {
        initAliConfig();
        //AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        model.setOutBizNo(UUID.fastUUID().toString(Boolean.TRUE));
        log.info("支付宝商家对个人转账: 收款人:{},收款金额:{}",aliAccount,amount);
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        model.setPayeeInfo(Builder.of(Participant::new).with(Participant::setIdentity,aliAccount).with(Participant::setIdentityType,"ALIPAY_LOGON_ID").with(Participant::setName,realName).build());
        model.setTransAmount(amount.setScale(2,BigDecimal.ROUND_DOWN).toString());
        model.setOrderTitle("xxxx");
        model.setBizScene("DIRECT_TRANSFER");
        String body = AliPayApi.uniTransferToResponse(model, null).getBody();
        //String body = AliPayApi.transferToResponse(model).getBody();
        Assert.isTrue(JSON.parseObject(body).getJSONObject("alipay_fund_trans_uni_transfer_response").getString("code").equals("10000"),body);
        log.info(body);
        log.info("支付宝商家对个人转账: 收款人:{},收款金额:{}",aliAccount,amount);
    }






    /**
     * app支付
     * @param outTradeNo 平台唯一单号，用来回调进行锁定单号
     * @param amount      支付金额，元为单位
     * @param aliPayEnum  回调,内容枚举类
     * @return  app所需要在线调用的参数,直接返回app即可。
     * @throws AlipayApiException
     */
   public String appPay(String outTradeNo, BigDecimal amount, AliPayEnum aliPayEnum) throws AlipayApiException {
        initAliConfig();
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody(aliPayEnum.getBody());
            model.setSubject("xxx");
            model.setOutTradeNo(outTradeNo);
            model.setTimeoutExpress("30m");
            model.setTotalAmount(amount.toString());
            model.setPassbackParams("callback params");
            model.setProductCode("QUICK_MSECURITY_PAY");
             String orderInfo = AliPayApi.appPayToResponse(model, this.aliPayApiConfig.webUrl.concat(aliPayEnum.getNotifyUrl())).getBody();
            log.info("app支付返回支付参数为:{}",orderInfo);

        return orderInfo;
    }
    /**
     * app支付
     * @param outTradeNo 平台唯一单号，用来回调进行锁定单号
     * @param amount      支付金额，元为单位
     * @param aliPayEnum  回调,内容枚举类
     * @param passbackParams    公用回传参数
     * @return  app所需要在线调用的参数,直接返回app即可。
     * @throws AlipayApiException
     * @Author 姚
     */
   public String appPay(String outTradeNo, BigDecimal amount, AliPayEnum aliPayEnum, String passbackParams) throws AlipayApiException {
       initAliConfig();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(aliPayEnum.getBody());
        model.setSubject("xxx");
        model.setOutTradeNo(outTradeNo);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(amount.toString());
        model.setProductCode("QUICK_MSECURITY_PAY");
        model.setPassbackParams(passbackParams);
        String orderInfo = AliPayApi.appPayToResponse(model,this.aliPayApiConfig.webUrl.concat(aliPayEnum.getNotifyUrl())).getBody();
        log.info("app支付返回支付参数为:{}",orderInfo);
        return orderInfo;
    }


    /**
     * app用户登陆
     * @param code 授权code 编码
     */
/*    public AliAppUserAccToken aliAppLogin(String code) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest());
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        AlipaySystemOauthTokenResponse response = alipayClient.certificateExecute(request);
        String body = response.getBody();
        log.info("app用户根据code 登陆,结果为:{}",body);
        if(response.isSuccess()){
            JSONObject oauthTokenResponse = JSON.parseObject(body).getJSONObject("alipay_system_oauth_token_response");
            AliAppUserAccToken aliAppUserAccToken = oauthTokenResponse.toJavaObject(AliAppUserAccToken.class);
         return aliAppUserAccToken;
        } else {
            log.info(body);
           throw new RuntimeException("调用失败!");
        }

    }*/

    /**
     * 根据 回话 token 得到用户信息
     * @param accessToken  回话token
     */
/*    public AliAppUserInfo getAliUserInfo(String accessToken) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest());
        AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        AlipayUserInfoShareResponse response = alipayClient.certificateExecute(request,accessToken);
        String body = response.getBody();
        log.info("app用户根据accessToken 获取用户信息,结果为:{}",body);
        if(response.isSuccess()){
            JSONObject oauthTokenResponse = JSON.parseObject(body).getJSONObject("alipay_user_info_share_response");
            AliAppUserInfo aliAppUserInfo = oauthTokenResponse.toJavaObject(AliAppUserInfo.class);
            return aliAppUserInfo;
        } else {
            log.info(body);
            throw new RuntimeException("调用失败!");
        }
    }*/

    /**
     * 证书模式
     */
    @Deprecated
    private CertAlipayRequest certAlipayRequest(){
        CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
/*        certAlipayRequest.setServerUrl(getAliConfig().getServiceUrl());
        certAlipayRequest.setAppId(getAliConfig().getAppId());
        certAlipayRequest.setPrivateKey(getAliConfig().getPrivateKey());
        certAlipayRequest.setCharset("utf-8");
        certAlipayRequest.setFormat("json");
        certAlipayRequest.setSignType("RSA2");
        certAlipayRequest.setCertPath(getAliConfig().getAppCertPath());
        certAlipayRequest.setAlipayPublicCertPath(getAliConfig().getAliPayCertPath());
        certAlipayRequest.setRootCertPath(getAliConfig().getAliPayRootCertPath());*/
        return certAlipayRequest;
    }

    @Override
    protected PayWxModel getPayModel() {
        throw new RuntimeException("系统支付错乱!");
    }

    @SneakyThrows
    @Override
    protected PayAliModel getAliModel() {
        return Builder.of(PayAliModel::new)
                .with(PayAliModel::setAliAppId,this.aliPayApiConfig.aliAppId)
                .with(PayAliModel::setAliPrivateKey,this.aliPayApiConfig.aliPrivateKey)
                .with(PayAliModel::setAliPublicKey,this.aliPayApiConfig.aliPublicKey)
                .with(PayAliModel::setWebUrl,this.aliPayApiConfig.webUrl)
                .build();
    }
}
