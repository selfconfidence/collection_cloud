package com.manyun.business.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.cloudauth20200618.Client;
import com.aliyun.cloudauth20200618.models.DescribeSmartVerifyRequest;
import com.aliyun.cloudauth20200618.models.DescribeSmartVerifyResponse;
import com.aliyun.cloudauth20200618.models.InitSmartVerifyRequest;
import com.aliyun.cloudauth20200618.models.InitSmartVerifyResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaUnretryableException;
import com.aliyun.tearpc.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cloudauth.model.v20190307.DescribeFaceVerifyRequest;
import com.aliyuncs.cloudauth.model.v20190307.DescribeFaceVerifyResponse;
import com.aliyuncs.cloudauth.model.v20190307.InitFaceVerifyRequest;
import com.aliyuncs.cloudauth.model.v20190307.InitFaceVerifyResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.manyun.business.domain.form.UserAliyunRealForm;
import com.manyun.business.domain.vo.AliRealVo;
import com.manyun.common.core.domain.Builder;
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

    private Long h5SceneId;

    private String accessKey;
    private String  accessKeySecret;

    private String  h5ReturnUrl;
    private String  h5CallbackUrl;

    private String h5Redirection;


    /**
     * 金融级实人方案 h5-进行的调用
     * @param aliyunRealForm
     * @return
     */
    @SneakyThrows
    public AliRealVo getCertifyIdH5(UserAliyunRealForm aliyunRealForm,String userId){
        IAcsClient client = new DefaultAcsClient(getDefaultProfile());

        InitFaceVerifyRequest request = new InitFaceVerifyRequest();
        request.setMetaInfo(aliyunRealForm.getMetaInfo());
        request.setCertName(aliyunRealForm.getRealName());
        request.setCertNo(aliyunRealForm.getCartNo());
// 固定值。
        request.setCertType("IDENTITY_CARD");
        request.setSceneId(h5SceneId);
        request.setOuterOrderNo(IdUtil.getSnowflakeNextIdStr());
        request.setCallbackToken(userId);
        request.setCallbackUrl(h5CallbackUrl);
// 固定值。
        request.setProductCode("ID_PRO");
// Web SDK请求时为必填。
        request.setReturnUrl(h5Redirection.concat("?userId=").concat(userId));
        InitFaceVerifyResponse response = client.getAcsResponse(request);
        log.info(response.getRequestId());
        log.info(response.getCode());
        log.info(response.getMessage());
        log.info(response.getResultObject() == null ? null
                : response.getResultObject().getCertifyId());
        Assert.isTrue(response.getCode().equals("200"),response.getMessage());
        InitFaceVerifyResponse.ResultObject resultObject = response.getResultObject();
        return Builder.of(AliRealVo::new).with(AliRealVo::setCertifyUrl,resultObject.getCertifyUrl()).with(AliRealVo::setCertifyId, resultObject.getCertifyId()).build();
    }


    /**
     * 增强版实人认证 - app双端在调用
     * @param certifyId
     */
    public void checkCertifyIdH5Status(String certifyId) throws ClientException {
// 等App客户端提交刷脸认证后，在客户端SDK的回调函数中，由客户端通知服务端运行以下代码查询认证结果。
        log.info("开始h5实名认证查询！");
        DescribeFaceVerifyRequest request2 = new DescribeFaceVerifyRequest();
        request2.setCertifyId(certifyId);
        request2.setSceneId(h5SceneId);
        IAcsClient client = new DefaultAcsClient(getDefaultProfile());
        DescribeFaceVerifyResponse response = client.getAcsResponse(request2);
//        System.out.println(response2.getCode());
//        System.out.println(response2.getMessage());
//        System.out.println(response2.getRequestId());
//        System.out.println(response2.getResultObject().getPassed());
//        System.out.println(response2.getResultObject().getSubCode());
//        System.out.println(response2.getResultObject().getMaterialInfo());
//        System.out.println(response2.getResultObject().getIdentityInfo());
        Assert.isTrue("200".equals(response.getCode()),response.getMessage());
        Assert.isTrue("200".equals(response.getResultObject().getSubCode()),response.getResultObject().getPassed());
        log.info("h5实名认证结果！{}", JSONUtil.toJsonStr(response.getResultObject()));
    }


    /**
     * 增强版实人认证 - app双端在调用
     * @param userAliyunRealForm
     * @return
     */
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


    /**
     * 增强版实人认证 - app双端在调用
     * @param certifyId
     */
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
        Assert.isTrue("200".equals(response.getCode()),response.getMessage());

        Assert.isTrue("200".equals(response.getResultObject().getSubCode()),response.getResultObject().getPassed());
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

    private DefaultProfile getDefaultProfile(){
        return DefaultProfile.getProfile(
                "cn-hangzhou",    // 固定为cn-hangzhou。
                accessKey,      // 您的AccessKey ID。
               accessKeySecret);  // 您的AccessKey Secret。

    }

}
