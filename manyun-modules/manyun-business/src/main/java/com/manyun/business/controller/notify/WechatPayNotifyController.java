package com.manyun.business.controller.notify;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.manyun.business.service.IOrderService;
import com.manyun.common.pays.abs.impl.WxComm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * 微信    验证
 */
@CrossOrigin
@RestController
@RequestMapping("/notify_pay/wechatPay")
@Slf4j
@Api(hidden = true)
public class WechatPayNotifyController {

    @Autowired
    private WxComm wxComm;

    @Autowired
    private IOrderService orderService;

    @RequestMapping(value = "/boxNotify")
    @ResponseBody
    @ApiOperation(value = "盲盒支付回调",hidden = true)
    public String certNotifyUrl(HttpServletRequest request) {
        String xmlMsg = HttpKit.readData(request);
        log.info("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        String returnCode = params.get("return_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        // 注意此处签名方式需与统一下单的签名类型一致
        //TODO  WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey() 有问题,线程组切换导致数据不一致问题 待修复
        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
            if (WxPayKit.codeIsOk(returnCode)) {
                // 更新订单信息
                String out_trade_no = params.get("out_trade_no");
                // 发送通知等
                orderService.notifyPaySuccess(out_trade_no);
                return WxPayKit.toXml(getStringStringMap());
            }
        }
        return null;
    }
    private Map<String, String> getStringStringMap() {
        Map<String, String> xml = new HashMap<String, String>(2);
        xml.put("return_code", "SUCCESS");
        xml.put("return_msg", "OK");
        return xml;
    }
}
