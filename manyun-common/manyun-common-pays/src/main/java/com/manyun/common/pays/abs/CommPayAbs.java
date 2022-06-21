package com.manyun.common.pays.abs;
import cn.hutool.core.lang.Assert;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.manyun.common.pays.model.PayAliModel;
import com.manyun.common.pays.model.PayWxModel;
import lombok.SneakyThrows;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author yanwei
 * @since 2020-12-17
 */
public abstract class CommPayAbs {


    /**
     * 初始化支付宝信息
     */
    @SneakyThrows
    public void initAliConfig(){
        // 需要进行获取对应支付参数
        PayAliModel payModel = getAliModel();
        Assert.isTrue(Objects.nonNull(payModel),"暂不支持支付宝支付!");
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(payModel.getAliAppId())
                .setAliPayPublicKey(payModel.getAliPublicKey())
                .setCharset("UTF-8")
                .setPrivateKey(payModel.getAliPrivateKey())
                .setServiceUrl("https://openapi.alipay.com/gateway.do")
                .setSignType("RSA2")
                // 普通公钥方式
                .build();
                // 证书模式
                //.buildByCert();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);

    }

    /**
     * 初始化微信信息
     */
    @SneakyThrows
    public void initWxConfig(){
        // 需要进行获取对应支付参数
        PayWxModel payModel = getPayModel();
        Assert.isTrue(Objects.nonNull(payModel),"暂不支持微信支付!");
        WxPayApiConfig apiConfig = WxPayApiConfig.builder()
                .appId(payModel.getWxAppId())
                .mchId(payModel.getWxAppId())
                .partnerKey(payModel.getWxMchKey())
                .certPath(payModel.getWxCertAddr())
                .domain(payModel.getWebUrl())
                .build();
        WxPayApiConfigKit.setThreadLocalWxPayApiConfig(apiConfig);

    }

   protected abstract PayWxModel getPayModel();
    protected abstract PayAliModel getAliModel();

}
