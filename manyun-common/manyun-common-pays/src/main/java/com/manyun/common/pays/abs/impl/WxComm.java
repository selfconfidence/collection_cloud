package com.manyun.common.pays.abs.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.TransferModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.WxPayEnum;
import com.manyun.common.pays.abs.CommPayAbs;
import com.manyun.common.pays.config.WechatPayConfig;
import com.manyun.common.pays.model.PayAliModel;
import com.manyun.common.pays.model.PayWxModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 支付宝 相关 支付 & 用户等
 */
@Slf4j
public class WxComm extends CommPayAbs {

    private WechatPayConfig wechatPayConfig;
    public WxComm(WechatPayConfig wechatPayConfig){
        this.wechatPayConfig = wechatPayConfig;
    }




    /**
     * app支付
     * @param outTradeNo 平台唯一单号，用来回调进行锁定单号
     * @param amount      支付金额，元为单位
     * @param wxPayEnum  回调,内容枚举类
     * @return  app所需要在线调用的参数,直接返回app即可。
     * @throws AlipayApiException
     */

   public String appPay(String outTradeNo, BigDecimal amount, WxPayEnum wxPayEnum)  {
           initWxConfig();
       WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
       Map<String, String> params = UnifiedOrderModel
               .builder()
               .appid(wxPayApiConfig.getAppId())
               .mch_id(wxPayApiConfig.getMchId())
               .nonce_str(WxPayKit.generateStr())
               .body("App支付")
               .attach("App 支付")
               .out_trade_no(outTradeNo)
               .total_fee(amount.multiply(NumberUtil.add(100D)).toString())
               .spbill_create_ip("127.0.0.1")
               .notify_url(this.wechatPayConfig.webUrl.concat(wxPayEnum.getNotifyUrl()))
               .trade_type(TradeType.APP.getTradeType())
               .build()
               .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

       String xmlResult = WxPayApi.pushOrder(false, params);
       log.info(xmlResult);
       Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

       String returnCode = result.get("return_code");
       String returnMsg = result.get("return_msg");
       Assert.isTrue(WxPayKit.codeIsOk(returnCode),returnMsg);
       // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
       String prepayId = result.get("prepay_id");
       Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
               wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
       String jsonStr = JSON.toJSONString(packageParams);
       log.info("返回apk的参数:" + jsonStr);
        return jsonStr;
    }


    /**
     * app支付
     * @param outTradeNo 平台唯一单号，用来回调进行锁定单号
     * @param amount      支付金额，元为单位
     * @param wxPayEnum  回调,内容枚举类
     * @return  app所需要在线调用的参数,直接返回app即可。
     * @throws AlipayApiException
     */

    public String appPay(String outTradeNo, BigDecimal amount, WxPayEnum wxPayEnum, String passbackParams)  {
        initWxConfig();
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("App支付")
                .attach("App 支付")
                .out_trade_no(outTradeNo)
                .total_fee(amount.multiply(NumberUtil.add(100D)).toString())
                .spbill_create_ip("127.0.0.1")
                .attach(passbackParams)
                .notify_url(this.wechatPayConfig.webUrl.concat(wxPayEnum.getNotifyUrl()))
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        Assert.isTrue(WxPayKit.codeIsOk(returnCode),returnMsg);
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        String jsonStr = JSON.toJSONString(packageParams);
        log.info("返回apk的参数:" + jsonStr);
        return jsonStr;
    }

    /**
     * 企业付款到零钱
     * @return
     */

    public String wxTransfer(String openId,BigDecimal amount){
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = TransferModel.builder()
                .mch_appid(wxPayApiConfig.getAppId())
                .mchid(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(WxPayKit.generateStr())
                .openid(openId)
                .check_name("NO_CHECK")
                .amount(amount.subtract(NumberUtil.add(100D)).toString())
                .desc("APP")
                .spbill_create_ip("127.0.0.1")
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5, false);
        // 提现
        String transfers = WxPayApi.transfers(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        log.info("提现结果:" + transfers);
        Map<String, String> map = WxPayKit.xmlToMap(transfers);
        String returnCode = map.get("return_code");
        String resultCode = map.get("result_code");
        if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
            // 提现成功
        } else {
            // 提现失败
        }
        return transfers;
    }


    @Override
    @SneakyThrows
    protected PayWxModel getPayModel() {
        // 得到对应微信数据
       return Builder.of(PayWxModel::new)
               .with(PayWxModel::setWebUrl,this.wechatPayConfig.webUrl)
               .with(PayWxModel::setWxAppId,this.wechatPayConfig.wxAppId)
               .with(PayWxModel::setWxCertAddr,this.wechatPayConfig.wxCertAddr)
               .with(PayWxModel::setWxMchKey,this.wechatPayConfig.wxMchKey)
               .with(PayWxModel::setWxMchId,this.wechatPayConfig.wxMchId)
               .build();

    }

    @SneakyThrows
    @Override
    protected PayAliModel getAliModel() {
        throw new RuntimeException("系统支付错乱!");
    }
}
