package com.manyun.business.controller.notify;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.alipay.AliPayApi;
import com.manyun.business.service.IOrderService;
import com.manyun.common.pays.abs.impl.AliComm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 支付宝支付    验证
 */
@CrossOrigin
@RestController
@RequestMapping("/notify_pay/aliPay")
@Slf4j
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class AliPayNotifyController {

    @Autowired
    private AliComm aliComm;

    @Autowired
    private IOrderService orderService;



    @RequestMapping(value = "/boxNotify")
    @ResponseBody
    public String certNotifyUrl(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);
            // 获取支付宝POST过来反馈信息
            boolean verifyResult = rsaCertCheckV1(params);
            if (verifyResult) {
                Object out_trade_no = params.get("out_trade_no");
                log.info("out_trade_no:{}",out_trade_no);
                String flag = params.get("trade_status");
                if (!"TRADE_SUCCESS".equals(flag)){
                    return "success";
                }
                orderService.notifyPaySuccess(out_trade_no.toString());
                return "success";
            } else {
                log.info("test_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }
    /**
     * 统一验证信息
     */
    public boolean rsaCertCheckV1(Map<String, String> params)throws AlipayApiException{
         boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliComm.getAliPayApiConfig().getAliPublicKey(), "UTF-8", "RSA2");
        return verifyResult;
    }

}
