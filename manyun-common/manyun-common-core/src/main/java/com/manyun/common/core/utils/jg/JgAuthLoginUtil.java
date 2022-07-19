package com.manyun.common.core.utils.jg;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * 一键手机号登录能力
 */
@Slf4j
public class JgAuthLoginUtil {
    //Android 和 iOS 使用
   private  String androidIosPostUrl  = "https://api.verification.jpush.cn/v1/web/loginTokenVerify";
    private  String masterSecret;
    private  String appKey;

    private String priKey;
    // 私钥
    private  RSA priRsa;

    public JgAuthLoginUtil(String masterSecret, String appKey,String priKey) {
        this.masterSecret = masterSecret;
        this.appKey = appKey;
        this.priRsa = new RSA(priKey, null);
    }
    //private static String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALzMUtKOBCt6R5UeZHEbhHKcPmdPyHE2JGYtn0bZ9ZQxThgyda9zI0k8C2kd2mExA8W4Fp0zzVpnKHVlmrU2uJGfTbpedSXL31Dih4CbbCr38j6l8qXaSkLS07MmtohtGC/wAx8FhFC7LeoyGO8A0x1a76vgvrMSK9D++HAQfPsnAgMBAAECgYBnlCWANL/e5ogtLG5oi3M/ua6W2XObgNu5XyA6K8wKkH5K0iw0pJNgU1vjQKiVl+F88QEfH9Ny3JOazLJy5uGcuVEqTA4KHP2PqOoPlkgzvxhXQYnI/YD0W6WYbyO8wvxiJyoaFEiGZRrlAt63e1puiIJXZeIvVC3gMXRkKjXNMQJBAOiAUd35+E48dgqsxq6o8fKQAoWEvQhLT3bGkndPW41HoQYKjcoikxUugoM/9XYeH/hCBIBit8G8JMQ9xVvyNJ8CQQDP4T19AuXtZOf4FrOg2bxrMTCSXt5EMbP65ijGf8aPyrY0lGpeQiii59DKT7ZzD+J45McQ44J03/7TiOEZDGR5AkEAmL1kCv3i4BLcQVsME0Yt3Ho7DdgMD1zaUV9WbUcXEBNUd5GLYJWiJrItT2g/K1/TBNbp+iXgLkgZp0olU6gkZQJBAIYElUqh8q/wjOhRYm8B0MpehQzoYm0eigToG0OVnuKW8o7FXCn2hxI3V1EGwP4/MGd2PqwxsKo+up+PsGcgqSkCQEXKmhIr+n/oodL6fmKuAGLf6hJB2gO9IjBdyfw+bQRISWWQ/2F5JDlSaCMGc9bJCAZ/CJM7K15nuOfkF1J4iEw=";
    //private static String pubKey = "????";

    //   private String base64Auth = Base64.decodeStr(appKey.concat(":").concat(masterSecret));
    //   public static void main(String[] args) {
//        String loginToken = "STsid0000001542695429579Ob28vB7b0cYTI9w0GGZrv8ujUu05qZvw";
//        //RSA priRsa = SecureUtil.rsa(priKey.getBytes(StandardCharsets.UTF_8), null);
//       // RSA pubRsa = SecureUtil.rsa(null,pubKey.getBytes(StandardCharsets.UTF_8));
//        RSA priRsa = new RSA(priKey, null);
//        RSA pubRsa = new RSA(null, pubKey);
//
//        String phne = "150xxxx6666";
//        String encryptBase64 = pubRsa.encryptBase64(phne, KeyType.PublicKey);
//        System.out.println(encryptBase64);
//        System.out.println(priRsa.decryptStr(encryptBase64, KeyType.PrivateKey, StandardCharsets.UTF_8));
//    }

    /**
     * 获取手机号
     * @param loginToken
     * @return
     */
    public String jgAuthAllPhone(String loginToken){
        HttpResponse httpResponse = HttpUtil.createPost(androidIosPostUrl).header("Authorization", HttpUtil.buildBasicAuth(appKey, masterSecret, StandardCharsets.UTF_8), Boolean.TRUE)
                .body(JSONUtil.toJsonStr(MapUtil.builder().put("loginToken", loginToken).put("exID", IdUtil.fastUUID()).build())).execute();
        JSONObject responseJson = JSONUtil.toBean(httpResponse.body(), JSONObject.class);
        // Map bean = JSONUtil.toBean(httpResponse.body(), Map.class);
        Assert.isTrue(Integer.valueOf(8000).equals(responseJson.getInt("code")),responseJson.getStr("content"));
        // 解析密钥
        return SecureUtil.rsa(Base64.encode(priKey, StandardCharsets.UTF_8), null).decryptStr(responseJson.getStr("phone"), KeyType.PrivateKey, StandardCharsets.UTF_8);
    }

}
