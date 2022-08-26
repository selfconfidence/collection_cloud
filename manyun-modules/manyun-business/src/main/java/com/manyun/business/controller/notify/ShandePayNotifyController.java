package com.manyun.business.controller.notify;

import com.alibaba.fastjson.JSONObject;
import com.manyun.business.config.cashier.sdk.CertUtil;
import com.manyun.business.config.cashier.sdk.CryptoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 杉德支付    验证
 */
@CrossOrigin
@RestController
@RequestMapping("/notify_pay/ShandePay")
@Slf4j
@Api(hidden = true)
public class ShandePayNotifyController {

    @RequestMapping(value = "/ShandeNotify")
    @ResponseBody
    @ApiOperation(value = "杉德支付回调",hidden = true)
    public String ShandeNotify(HttpServletRequest req) {
        String data=req.getParameter("data");
        String sign=req.getParameter("sign");
        log.info("接收到后台通知数据："+data);
        log.info("接收到后台通知签名："+sign);
        // 验证签名
        boolean valid;
        try {
            valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicKey(), "SHA1WithRSA");
            if (!valid) {
                log.info("verify sign fail.");
                log.info("签名字符串(data)为："+ data);
                log.info("签名值(sign)为："+ sign);
            }else {
                log.info("verify sign success");
                JSONObject dataJson = JSONObject.parseObject(data);
                JSONObject bodyData = JSONObject.parseObject(dataJson.getString("body"));
                String orderStatus = bodyData.getString("orderStatus");
                String tradeNo = bodyData.getString("tradeNo");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("orderStatus：" + orderStatus);
                    log.info("tradeNo：" + tradeNo);
                } else {
                    log.info("通知数据异常！！！");
                }
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

}
