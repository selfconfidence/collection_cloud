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


    /**
     * 11:32:11.636 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,31] - 进入回调
     * 11:32:11.636 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,34] - 接收到后台通知数据：{"head":{"version":"1.0","respTime":"20220829113140","respCode":"000000","respMsg":"成功"},"body":{"mid":"6888801048981","orderCode":"630c3305e4b09f5444cfe71b","tradeNo":"630c3305e4b09f5444cfe71b","clearDate":"20220829","totalAmount":"000000000100","orderStatus":"1","payTime":"20220829113139","settleAmount":"000000000100","buyerPayAmount":"000000000100","discAmount":"000000000000","txnCompleteTime":"20220829113140","payOrderCode":"2022082900024900000382750110201","accLogonNo":"621700*********2734","accNo":"621700*********2734","midFee":"000000000010","extraFee":"000000000000","specialFee":"000000000000","plMidFee":"000000000000","bankserial":"202208291050000739908721010012H11661743900854590","externalProductCode":"00000018","cardNo":"621700*********2734","creditFlag":"","bid":"SDSMP00688880104898120220826043232446637","benefitAmount":"000000000000","remittanceCode":"","extend":""}}
     * 11:32:11.637 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,35] - 接收到后台通知签名：NxMJVX/km3ekCBQ12sND63EdtPPS6pLFwT4YoPW7tlgkT4Dae/usJ3Cvmti0q1QrLu9x9DYHo6vq4fKw9qDm8yftqCEVc9xsCW3CD8mbNtQhc147WwL2IaMkC4SdC0di9jlUl6J16CAjGg5U1kO1Ei0kZdDhR5V7QF/vZ22N5cR8wShIyPBe9X810i0eXgBrH3SMZFWaOrrn1vDT7hcjF7NhL/jLc3RseRw6Nt377ykwirHHfRb7SOZ00BKkyxf4HutbxurelOVaw2LGxuRV6sAyzlFr83Sar4IF3hn9G94MtAtYFGmFddLn/9zNwFezl8uA5qVQTdEzUfJ4DG5bqw==
     * 11:32:11.637 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,42] - verify sign fail.
     * 11:32:11.637 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,43] - 签名字符串(data)为：{"head":{"version":"1.0","respTime":"20220829113140","respCode":"000000","respMsg":"成功"},"body":{"mid":"6888801048981","orderCode":"630c3305e4b09f5444cfe71b","tradeNo":"630c3305e4b09f5444cfe71b","clearDate":"20220829","totalAmount":"000000000100","orderStatus":"1","payTime":"20220829113139","settleAmount":"000000000100","buyerPayAmount":"000000000100","discAmount":"000000000000","txnCompleteTime":"20220829113140","payOrderCode":"2022082900024900000382750110201","accLogonNo":"621700*********2734","accNo":"621700*********2734","midFee":"000000000010","extraFee":"000000000000","specialFee":"000000000000","plMidFee":"000000000000","bankserial":"202208291050000739908721010012H11661743900854590","externalProductCode":"00000018","cardNo":"621700*********2734","creditFlag":"","bid":"SDSMP00688880104898120220826043232446637","benefitAmount":"000000000000","remittanceCode":"","extend":""}}
     * 11:32:11.638 [http-nio-9205-exec-2] INFO  c.m.b.c.n.ShandePayNotifyController - [ShandeNotify,44] - 签名值(sign)为：NxMJVX/km3ekCBQ12sND63EdtPPS6pLFwT4YoPW7tlgkT4Dae/usJ3Cvmti0q1QrLu9x9DYHo6vq4fKw9qDm8yftqCEVc9xsCW3CD8mbNtQhc147WwL2IaMkC4SdC0di9jlUl6J16CAjGg5U1kO1Ei0kZdDhR5V7QF/vZ22N5cR8wShIyPBe9X810i0eXgBrH3SMZFWaOrrn1vDT7hcjF7NhL/jLc3RseRw6Nt377ykwirHHfRb7SOZ00BKkyxf4HutbxurelOVaw2LGxuRV6sAyzlFr83Sar4IF3hn9G94MtAtYFGmFddLn/9zNwFezl8uA5qVQTdEzUfJ4DG5bqw==
     * @param req
     * @return
     */
    @RequestMapping(value = "/ShandeNotify")
    @ResponseBody
    @ApiOperation(value = "杉德支付回调",hidden = true)
    public String ShandeNotify(HttpServletRequest req) {
        log.info("进入回调");
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
