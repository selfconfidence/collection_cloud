package com.manyun.business.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.aliyun.cloudauth20200618.Client;
import com.aliyun.cloudauth20200618.models.DescribeSmartVerifyRequest;
import com.aliyun.cloudauth20200618.models.DescribeSmartVerifyResponse;
import com.aliyun.cloudauth20200618.models.InitSmartVerifyRequest;
import com.aliyun.cloudauth20200618.models.InitSmartVerifyResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.aliyun.tearpc.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.manyun.business.domain.form.UserAliyunRealForm;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "ali.real")
@Data
@Slf4j
public class AliRealConfig {
    // 场景ID
    private Long sceneId;
    private String accessKey;
    private String  accessKeySecret;


    public String getCertifyId(UserAliyunRealForm userAliyunRealForm){
        // 通过以下代码创建API请求并设置参数。
        InitSmartVerifyRequest request = new InitSmartVerifyRequest();
        // 请输入场景ID+L。
        request.setSceneId(sceneId);
        request.setOuterOrderNo(IdUtil.objectId());
        // 卡证核身类型，固定值。
        request.setMode("MARKET_SAFE");
        request.setOcr("F");
        // 证件类型，固定值。
        request.setCertType("IDENTITY_CARD");

        request.setCertName(userAliyunRealForm.getRealName());
        request.setCertNo(userAliyunRealForm.getCartNo());

        // MetaInfo环境参数，需要通过客户端SDK获取。
        request.setMetaInfo(userAliyunRealForm.getMetaInfo());
        request.setMobile(userAliyunRealForm.getPhone());

        // 支持服务路由。
        InitSmartVerifyResponse response = initSmartVerifyAutoRoute(request);


        log.info(response.getRequestId());
        log.info(response.getCode());
        log.info(response.getMessage());
        log.info(response.getResultObject() == null ? null
                : response.getResultObject().getCertifyId());
        Assert.isTrue(response.getCode().equals("200"),response.getMessage());
        return response.getResultObject().getCertifyId();

    }


    private  InitSmartVerifyResponse initSmartVerifyAutoRoute(InitSmartVerifyRequest request) {
        // 第一个为主区域Endpoint，第二个为备区域Endpoint。
        List<String> endpoints = Arrays.asList("cloudauth.cn-shanghai.aliyuncs.com",
                "cloudauth.cn-beijing.aliyuncs.com");
        InitSmartVerifyResponse lastResponse = null;
        for (String endpoint : endpoints) {
            try {
                InitSmartVerifyResponse response = initSmartVerify(endpoint, request);
                lastResponse = response;

                // 服务端错误，切换到下个区域调用。
                if (response != null && "500".equals(response.getCode())) {
                    continue;
                }

                return response;
            } catch (Exception e) {
                // 网络异常，切换到下个区域调用。
                if (e.getCause() instanceof TeaException) {
                    TeaException teaException = ((TeaException)e.getCause());
                    if (teaException.getData() != null && "ServiceUnavailable".equals(
                            teaException.getData().get("Code"))) {
                        continue;
                    }
                }

                if (e.getCause() instanceof TeaUnretryableException) {
                    continue;
                }
            }
        }

        return lastResponse;
    }

    private  InitSmartVerifyResponse initSmartVerify(String endpoint, InitSmartVerifyRequest request)
            throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKey);
        config.setAccessKeySecret(accessKeySecret);
        config.setEndpoint(endpoint);
        // 设置http代理。
        //config.setHttpProxy("http://xx.xx.xx.xx:xxxx");
        // 设置https代理。
        //config.setHttpsProxy("https://xx.xx.xx.xx:xxxx");
        Client client = new Client(config);

        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;

        return client.initSmartVerify(request, runtime);
    }


    @SneakyThrows
    public void checkCertifyIdStatus(String certifyId){
        // 通过以下代码创建API请求并设置参数。
        DescribeSmartVerifyRequest request = new DescribeSmartVerifyRequest();
        // 请输入场景ID+L。
        request.setSceneId(sceneId);

        request.setCertifyId(certifyId);

        // 推荐，支持服务路由。
        DescribeSmartVerifyResponse response = describeSmartVerifyAutoRoute(request);

        // 不支持服务自动路由。
      //  DescribeSmartVerifyResponse response = describeSmartVerify("cloudauth.cn-shanghai.aliyuncs.com", request);
//
//        System.out.println(response.getRequestId());
//        System.out.println(response.getCode());
//        System.out.println(response.getMessage());
//        System.out.println(
//                response.getResultObject() == null ? null : response.getResultObject().getPassed());
//        System.out.println(
//                response.getResultObject() == null ? null : response.getResultObject().getSubCode());
//        System.out.println(response.getResultObject() == null ? null
//                : response.getResultObject().getMaterialInfo());
        Assert.isTrue(response.getCode().equals("200"),response.getMessage());

        Assert.isTrue(response.getResultObject().getSubCode().equals("200"),response.getResultObject().getMaterialInfo());
    }


    private  DescribeSmartVerifyResponse describeSmartVerifyAutoRoute(DescribeSmartVerifyRequest request) {
        // 第一个为主区域Endpoint，第二个为备区域Endpoint。
        List<String> endpoints = Arrays.asList("cloudauth.cn-shanghai.aliyuncs.com",
                "cloudauth.cn-beijing.aliyuncs.com");
        DescribeSmartVerifyResponse lastResponse = null;
        for (String endpoint : endpoints) {
            try {
                DescribeSmartVerifyResponse response = describeSmartVerify(endpoint, request);
                lastResponse = response;

                // 服务端错误，切换到下个区域调用。
                if (response != null && "500".equals(response.getCode())) {
                    continue;
                }

                return response;
            } catch (Exception e) {
                // 网络异常，切换到下个区域调用。
                if (e.getCause() instanceof TeaException) {
                    TeaException teaException = ((TeaException)e.getCause());
                    if (teaException.getData() != null && "ServiceUnavailable".equals(
                            teaException.getData().get("Code"))) {
                        continue;
                    }
                }

                if (e.getCause() instanceof TeaUnretryableException) {
                    continue;
                }
            }
        }

        return lastResponse;
    }

    private  DescribeSmartVerifyResponse describeSmartVerify(String endpoint, DescribeSmartVerifyRequest request)
            throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKey);
        config.setAccessKeySecret(accessKeySecret);
        config.setEndpoint(endpoint);
        // 设置http代理。
        //config.setHttpProxy("http://xx.xx.xx.xx:xxxx");
        // 设置https代理。
        //config.setHttpsProxy("https://xx.xx.xx.xx:xxxx");
        Client client = new Client(config);

        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;

        return client.describeSmartVerify(request, runtime);
    }

}
