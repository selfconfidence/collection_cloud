package com.manyun.common.core.utils;

import cn.hutool.core.lang.Assert;
import com.dingxianginc.ctu.client.CaptchaClient;
import com.dingxianginc.ctu.client.model.CaptchaResponse;
import com.manyun.common.core.utils.ip.IpUtils;

/*
 *
 *
 * @author yanwei
 * @create 2022-09-29
 */
public class XXutils {
    public static void dingxiangTokenCheck(String token){
        /**构造入参为appId和appSecret
         * appId和前端验证码的appId保持一致，appId可公开
         * appSecret为秘钥，请勿公开
         * token在前端完成验证后可以获取到，随业务请求发送到后台，token有效期为两分钟
         * ip 可选，提交业务参数的客户端ip
         **/
        String appId = "a0ab66c6771a8e8bd9da35c6788cfc63";
        String appSecret = "78cfa7486febe8a12a094db640c54592";
        CaptchaClient captchaClient = new CaptchaClient(appId,appSecret);
        captchaClient.setCaptchaUrl("https://cap-api.dingxiang-inc.com");
//指定服务器地址，saas可在控制台，应用管理页面最上方获取
        CaptchaResponse response = null;
        try {
            response = captchaClient.verifyToken(token, IpUtils.getIpAddr(ServletUtils.getRequest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//CaptchaResponse response = captchaClient.verifyToken(token, ip);
//针对一些token冒用的情况，业务方可以采集客户端ip随token一起提交到验证码服务，验证码服务除了判断token的合法性还会校验提交业务参数的客户端ip和验证码颁发token的客户端ip是否一致
        //    System.out.println(response.getCaptchaStatus());
//确保验证状态是SERVER_SUCCESS，SDK中有容错机制，在网络出现异常的情况会返回通过
//System.out.println(response.getIp());
//验证码服务采集到的客户端ip
        Assert.isTrue(response.getResult(),"验证失败,请重试！");
    }
}
