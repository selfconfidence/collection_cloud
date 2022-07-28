package com.manyun.business.config;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.manyun.business.config.unionUtil.ColorUtil;
import com.manyun.business.config.unionUtil.SM2;
import com.manyun.business.domain.form.UserRealForm;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "union.real")
@Data
@Slf4j
public class UnionRealConfig {
    private String appId;
    private String appKey;
    //商户私钥
    private String priKey;
    private String url;
    //平台公钥
    private String pubKey;

    public R unionReal (UserRealForm userRealForm) {

        Map<String,String> body = new HashMap<>();

        body.put("cardNo",userRealForm.getBankCart());
        body.put("certType","01");
        body.put("certNo",userRealForm.getCartNo());
        body.put("name",userRealForm.getRealName());
        body.put("phoneNo",userRealForm.getPhone());
        body.put("personalMandate", "1");
        body.put("sceneId","25");
        body.put("bsnType", "01");
        body.put("appName", "00CNT");
        body.put("protocolVersion", "1.0.1");
        body.put("protocolNo", IdUtil.getSnowflakeNextIdStr());
        body.put("ipType", "04");
        body.put("sourceIp", addZero(IpUtils.getIpAddr(ServletUtils.getRequest())));
        Map<String,String> data = new HashMap<>();
        String encData= SM2.sm2Encrypt(JSON.toJSONString(body), pubKey);
        data.put("data",encData);
        log.info("明文参数---" + JSON.toJSONString(body));
        log.info("加密结果---" + encData);
        log.info("上送报文---" + JSON.toJSONString(data));
        //System.out.println(ColorUtil.log("明文参数") + JSON.toJSONString(body));
        //System.out.println(ColorUtil.log("加密结果") + encData);
        //System.out.println(ColorUtil.log("上送报文") + JSON.toJSONString(data));
        try{
            String authorization =  getOpenBodySig(appId, appKey, JSON.toJSONString(data));
            String str = post(url,JSON.toJSONString(data), authorization);
            JSONObject rspMap = JSONObject.parseObject(str);
            //System.out.println(ColorUtil.log("响应报文") + JSON.toJSONString(rspMap));
            log.info("响应报文---"+ JSON.toJSONString(rspMap));
            String rawData = SM2.sm2Decrypt(rspMap.getString("data"), priKey);
            log.info("响应报文data解密" + rawData);
            //System.out.println(ColorUtil.log("响应报文data解密") + rawData);
            JSONObject jsonObject = JSONObject.parseObject(rawData);
            if (!"0000".equals(jsonObject.getString("detailRespCode"))) {
                return R.fail(jsonObject.getString("detailRespMsg"));
            }
        }catch(Exception e){
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
        return R.ok();
    }

    private static String post(String requestUrl,String params, String authorization)throws Exception{
        HttpURLConnection connection = null;
        BufferedReader reader=null;
        try {
            //创建连接
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置http连接属性
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST"); // 提交 POST请求
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            //设置http头 消息
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");  //设定 请求格式 application/json
            connection.setRequestProperty("Accept","application/json");//设定响应的信息的格式为application/json
            connection.setRequestProperty("Authorization", authorization);//替换成Test.java返回的token
            connection.connect();
            //添加 请求内容
            OutputStream out = connection.getOutputStream();
            out.write(params.toString().getBytes("utf-8"));
            out.flush();
            out.close();
            // 读取响应
            int responseCode=connection.getResponseCode();
            if(responseCode!=200){
                log.info("返回错误");
                //System.out.println("返回错误");
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            connection.disconnect();
            return sb.toString();
        } catch (Exception e) {
            throw e;
        }finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    public static String getOpenBodySig(String appId, String appKey, String body) throws Exception {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        byte[] data = body.getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(data);
        String bodyDigest = testSHA256(is);
        String str1_C = appId + timestamp + nonce + bodyDigest;
        byte[] localSignature = hmacSHA256(str1_C.getBytes(), appKey.getBytes());
        String localSignatureStr = Base64.encodeBase64String(localSignature);
        return ("OPEN-BODY-SIG AppId=" + "\"" + appId + "\"" + ", Timestamp=" + "\"" + timestamp + "\"" + ", Nonce=" + "\"" + nonce + "\"" + ", Signature=" + "\"" + localSignatureStr + "\"");
    }
    private static String testSHA256(InputStream is) {
        try {
            return DigestUtils.sha256Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] hmacSHA256(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data);
    }

    private static String addZero(String ip){
        String result=""; //用保存处理后的结果
        String []array=ip.split("\\."); //这个函数将传入的字符串根据这个小点来分解，因为这个点式一个转义字符，所以需要写成"\\."
        for(int i=0;i<array.length;i++){
            while(array[i].length()<3){ //一共被分成了四个字符串，字符串里已经没有了小点，如果一个字符串的长度小于三，那么就在前面加零
                array[i]="0"+array[i];
            }
            if(i!=array.length-1){ //加完后，再继续在后面加上一个小点，因为最后一个不需要加，所以用if控制一下
                array[i]=array[i]+".";
            }
        }
        for(int i=0;i<array.length;i++){
            result+=array[i]; //将处理好的四个字符串连起来，这四个分别是 062. 004. 022. 115
        }
        return result;
    }


}
