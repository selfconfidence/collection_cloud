package com.manyun.common.pays.abs;
import cn.hutool.core.lang.Assert;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.manyun.common.pays.model.PayAliModel;
import com.manyun.common.pays.model.PayWxModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author yanwei
 * @since 2020-12-17
 */
@Slf4j
public abstract class CommPayAbs {

    private static final String aliAppId = "2021003159664036";
    private static final String aliPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAia9HF0ngaLGC65ikBGA+cQCuFXds3Pno2M/SIkurHFJxuZrUIYOH97zBUAGtoiB6+mKmFKHAtRUnUr/ttATz/0ygFsneUma5Igu0J0oVCy7ynq/OWSIBxNe+AMPzDf0xAVKUo5UssR8a0QC+r9DNEsUkPlO1HkzRdTWxzgQUosl8MQ11gM1sM+w/R1qqcNoeq7gXRiVQD8LMsHupLQB30qNvkZfieik77JuOGI+aRGrelAphyO93REAp58UksQfSh+OeK+z01C64nWI+NCoQoJAkKvKq6l2L961vv2k72vK1vuMqjI2i8TeNAmecdqpD9Wwy2MDNW0hw5uO5DYl/swIDAQAB";
    private static final String aliPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJr0cXSeBosYLrmKQEYD5xAK4Vd2zc+ejYz9IiS6scUnG5mtQhg4f3vMFQAa2iIHr6YqYUocC1FSdSv+20BPP/TKAWyd5SZrkiC7QnShULLvKer85ZIgHE174Aw/MN/TEBUpSjlSyxHxrRAL6v0M0SxSQ+U7UeTNF1NbHOBBSiyXwxDXWAzWwz7D9HWqpw2h6ruBdGJVAPwsywe6ktAHfSo2+Rl+J6KTvsm44Yj5pEat6UCmHI73dEQCnnxSSxB9KH454r7PTULridYj40KhCgkCQq8qrqXYv3rW+/aTva8rW+4yqMjaLxN40CZ5x2qkP1bDLYwM1bSHDm47kNiX+zAgMBAAECggEAWj0Xweq4esWFAfaL4RZXNmb0sbsRDY95jcg/eBzR7AEY6kXQrJpxXexHYrGG5JCXKU3IfhM5ozVXDYQfPfJVHMlSzzzFSopq4iZ7j5idTDQb7edZvc9tC18+9291+IAuQyilpMUbgobZY4wybjbSgW/V/keMmGxOh5yGiXjhMYMXguibRgXPxjidlkBKH79WeyhWwkhOuDxC2Zqq8KriKL91OtJc9Kh4ps2FxZuRQVUsw0nRqcTzLbqkC0Z50vTVjiJGUXPRAesdH9bRSsF+hyc1yd94A1ZeUmvhe8gBDgUTnL/oihyTCNzr0JX3jVHgZYdNllSzUrgJD7WmMHxmOQKBgQDOSSAxPd+VRqF6uSk2nGm6QesUnfnM2PQQ5CS5VgxJKNfFvnez1EgSk0o+uNeD8FqFB32uJWopqQNWWXPw3MpCn4r1JEb/jp+6ZLWV9RYoiCW1XIp0fuYpBs41BSbHsv5taDuUKQTZQRzPZvKAylFnDvi0tvaeSpzLADMMlGaEZwKBgQCq3cnEjX6AgC+Y57meUAFI1VmjG8B9IC1zDqK1h+zdSyWkRLCl5ssYgqAXhyR6BZ2hj5wOEoQiov4d2Q1y1GXA23fAO9nkDIPAkkcITONrmVaQAksXuhgqYbky/lZ5b73etFY5ycJ2QFAdST8ZBPIq8zIG/+rYwwP29WzLvRI61QKBgQC7u8sjZTbkSHpPENHUFHcX/kOt6LNc3RkJgkd1sDzQG+561QNlUk0hMpCAEoJ4XbZTmOSlJwG91kXmQNSGILml9kJlJNXEYA03ec3UIk8/JeCDdCvBJ3fZYIsrr3uChICOGy0VglsaSOqqSftqgDWi//oRpO0+U5LsIEtVKH1AAwKBgHzg4AgxSZVtGlxsb7MO0gGDPKIDNGm+zLjcp3p5P1p1A6vNMDXwOdtODOdlM/mihRSBOxLl4YDXE1aJPlUAeuW7AQiUNamApAJoZlXgrA4F4cgahPtwXR0Mkyh7zoOucVyE/Mf7d46pbmQZFm6YuM0/NP78p42I6iriMIOvSdWJAoGAJxfAg7UjS8nBI0j7su09EvdJNk5tH/nWn+Y0H9a8mghw1oyizaaSGw+6qDan7UhnXqSnOewVIqWUrkDw0Dfa1jYpqDIaUCVjzhEp/TTZNwy4P8UjGABGqJQopODvWNbeDeUpkIuAVwgFZ3+M+RRboJcq9r9HbFDMKDw22oibWVs=";


    /**
     * 初始化支付宝信息
     */
    @SneakyThrows
    public void initAliConfig(){
        // 需要进行获取对应支付参数
        PayAliModel payModel = getAliModel();
        log.info("进入阿里云配置------"+ payModel.toString());
        Assert.isTrue(Objects.nonNull(payModel),"暂不支持支付宝支付!");
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliAppId)
                .setAliPayPublicKey(aliPublicKey)
                .setCharset("UTF-8")
                .setPrivateKey(aliPrivateKey)
                .setServiceUrl("https://openapi.alipay.com/gateway.do")
                .setSignType("RSA2")
                // 普通公钥方式
                .build();
                // 证书模式
                //.buildByCert();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);

    }

    public AlipayClient initAliCertConfig() {
        //String privateKey = aliPrivateKey;
        log.info("初始化支付宝配置----");
        CertAlipayRequest alipayConfig = new CertAlipayRequest();
        alipayConfig.setPrivateKey(aliPrivateKey);
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setAppId(aliAppId);
        alipayConfig.setCharset("UTF8");
        alipayConfig.setSignType("RSA2");
        alipayConfig.setEncryptor("");
        alipayConfig.setFormat("json");
        alipayConfig.setCertPath("/cert/appCertPublicKey_2021003159664036.crt");
        alipayConfig.setAlipayPublicCertPath("/cert/alipayCertPublicKey_RSA2.crt");
        alipayConfig.setRootCertPath("/cert/alipayRootCert.crt");
        AlipayClient alipayClient = null;
        try {
            alipayClient = new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        log.info(alipayClient.toString());
        return alipayClient;
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
