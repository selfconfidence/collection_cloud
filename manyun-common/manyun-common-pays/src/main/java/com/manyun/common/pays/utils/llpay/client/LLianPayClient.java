package com.manyun.common.pays.utils.llpay.client;


import com.manyun.common.pays.utils.llpay.security.LLianPayAccpSignature;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;

public class LLianPayClient {
    private static Logger log = LoggerFactory.getLogger(LLianPayClient.class);

    public String sendRequest(String url, String body) {
        if (url == null || "".equals(url)) {
            throw new RuntimeException("请求URL非法");
        }
        return sendRequest(url, body, LLianPayAccpSignature.getInstance().sign(body));
    }

    public String sendRequest(String url, String body, String sign) {
        if (url == null || "".equals(url)) {
            throw new RuntimeException("请求URL非法");
        }
        log.info(String.format("请求URL：%s", url));
        log.info(String.format("请求签名值：%s", sign));
        log.info(String.format("请求参数：%s", body));

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        post.setHeader("Signature-Type", "RSA");
        post.setHeader("Signature-Data", sign);
        try {
            StringEntity stringEntity = new StringEntity(body, "UTF-8");
            stringEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING, "UTF-8"));
            // 设置请求主体
            post.setEntity(stringEntity);
            // 发起交易
            HttpResponse resp = LLianPayHttpClient.getLLianPayHttpClient().execute(post);
            int ret = resp.getStatusLine().getStatusCode();
            if (ret == HttpStatus.SC_OK) {
                // 响应分析
                HttpEntity entity = resp.getEntity();

                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                StringBuffer responseString = new StringBuffer();
                String result = br.readLine();
                while (result != null) {
                    responseString.append(result);
                    result = br.readLine();
                }
                String resSignatureData = "";
                Header headers[] = resp.getAllHeaders();
                int i = 0;
                while (i < headers.length) {
                    //System.out.println(headers[i].getName() + ":  " + headers[i].getValue());
                    if ("Signature-Data".equals(headers[i].getName())) {
                        resSignatureData = headers[i].getValue();
                    }
                    i++;
                }
                log.info(String.format("响应结果：%s", responseString));
                if (!"".equalsIgnoreCase(resSignatureData)) {
                    log.info(String.format("响应签名：%s", resSignatureData));
                    boolean checksign = LLianPayAccpSignature.getInstance().checkSign(responseString.toString(), resSignatureData);
                    if (!checksign) {
                        log.error("返回响应验证签名异常，请核实！");
                        //throw new RuntimeException("返回响应验证签名异常");
                    } else {
                        log.info(String.format("响应验签通过！"));
                    }
                }
                return responseString.toString();
            }
            throw new RuntimeException("请求结果异常，响应状态码为：" + ret);
        } catch (ConnectTimeoutException cte) {
            log.error(cte.getMessage());
            throw new RuntimeException(cte);
        } catch (SocketTimeoutException cte) {
            log.error(cte.getMessage());
            throw new RuntimeException(cte);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
