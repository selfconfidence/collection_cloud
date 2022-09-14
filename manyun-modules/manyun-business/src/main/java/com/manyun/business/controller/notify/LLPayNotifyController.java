package com.manyun.business.controller.notify;

import com.alibaba.fastjson.JSONObject;
import com.manyun.business.service.IOrderService;
import com.manyun.common.pays.utils.llpay.security.LLianPayAccpSignature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * 连连支付    验证
 */
@CrossOrigin
@RestController
@RequestMapping("/notify_pay/LlPay")
@Slf4j
@Api(hidden = true)
public class LLPayNotifyController {
    @Autowired
    private IOrderService orderService;


    @PostMapping(value = "/LlInnerUserNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付开户回调",hidden = true)
    public String LlInnerUserNotify(HttpServletRequest req) throws IOException {
        String sign = req.getHeader("Signature-Data");
        //获取request的输入流，并设置格式为UTF-8
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        //将输入流数据放入StringBuilder
        StringBuilder resultSB = new StringBuilder();
        String inputStr = null;
        while ((inputStr = streamReader.readLine()) != null) {
            resultSB.append(inputStr);
        }
        log.info(String.format("响应结果：%s", resultSB));
        if (!"".equalsIgnoreCase(sign)) {
            log.info(String.format("响应签名：%s", sign));
            boolean checksign = LLianPayAccpSignature.getInstance().checkSign(resultSB.toString(), sign);
            if (!checksign) {
                log.error("返回响应验证签名异常，请核实！");
                return null;
            } else {
                log.info(String.format("响应验签通过！"));
                JSONObject resultObj = JSONObject.parseObject(resultSB.toString());
                log.info("user_status:======="+resultObj.getString("user_status"));
                log.info("remark:======="+resultObj.getString("remark"));
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }

    @PostMapping(value = "/LlBindCardApplyNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付新增绑卡申请回调",hidden = true)
    public JSONObject LlBindCardApplyNotify(HttpServletRequest req) throws IOException {
        ServletInputStream inputStream = req.getInputStream();
        return null;
    }

    @PostMapping(value = "/LlUserTopupNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付充值回调",hidden = true)
    public JSONObject LlUserTopupNotify(HttpServletRequest req) throws IOException {
        ServletInputStream inputStream = req.getInputStream();
        return null;
    }

    @PostMapping(value = "/LlGeneralConsumeNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付消费回调",hidden = true)
    public String LlGeneralConsumeNotify(HttpServletRequest req) throws IOException {
        String sign = req.getHeader("Signature-Data");
        //获取request的输入流，并设置格式为UTF-8
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        //将输入流数据放入StringBuilder
        StringBuilder resultSB = new StringBuilder();
        String inputStr = null;
        while ((inputStr = streamReader.readLine()) != null) {
            resultSB.append(inputStr);
        }
        log.info(String.format("响应结果：%s", resultSB));
        if (!"".equalsIgnoreCase(sign)) {
            log.info(String.format("响应签名：%s", sign));
            boolean checksign = LLianPayAccpSignature.getInstance().checkSign(resultSB.toString(), sign);
            if (!checksign) {
                log.error("返回响应验证签名异常，请核实！");
                return null;
            } else {
                log.info(String.format("响应验签通过！"));
                JSONObject resultObj = JSONObject.parseObject(resultSB.toString());
                String txnStatus = resultObj.getString("txn_status");
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                if("TRADE_SUCCESS".equals(txnStatus) && Objects.nonNull(orderInfo)){
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));

                }else {
                    log.info("响应结果异常!");
                    return null;
                }
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }

    @PostMapping(value = "/LlWithdrawNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付提现回调",hidden = true)
    public String LlWithdrawNotify(HttpServletRequest req) throws IOException {
        String sign = req.getHeader("Signature-Data");
        //获取request的输入流，并设置格式为UTF-8
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        //将输入流数据放入StringBuilder
        StringBuilder resultSB = new StringBuilder();
        String inputStr = null;
        while ((inputStr = streamReader.readLine()) != null) {
            resultSB.append(inputStr);
        }
        log.info(String.format("响应结果：%s", resultSB));
        if (!"".equalsIgnoreCase(sign)) {
            log.info(String.format("响应签名：%s", sign));
            boolean checksign = LLianPayAccpSignature.getInstance().checkSign(resultSB.toString(), sign);
            if (!checksign) {
                log.error("返回响应验证签名异常，请核实！");
                return null;
            } else {
                log.info(String.format("响应验签通过！"));
                JSONObject resultObj = JSONObject.parseObject(resultSB.toString());
                log.info("userId:======="+resultObj.getString("user_id"));
                log.info("finish_time:======="+resultObj.getString("finish_time"));
                log.info("txn_status:======="+resultObj.getString("txn_status"));
                log.info("failure_reason:======="+resultObj.getString("failure_reason"));
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                log.info("txn_seqno:======="+orderInfo.getString("txn_seqno"));
                log.info("txn_time:======="+orderInfo.getString("txn_time"));
                log.info("total_amount:======="+orderInfo.getString("total_amount"));
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }




    @PostMapping(value = "/collectionJoinBoxNotify")
    @ResponseBody
    @ApiOperation(value = "盲盒藏品支付",hidden = true)
    public String collectionJoinBoxNotify(HttpServletRequest req) throws IOException {
        String sign = req.getHeader("Signature-Data");
        //获取request的输入流，并设置格式为UTF-8
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        //将输入流数据放入StringBuilder
        StringBuilder resultSB = new StringBuilder();
        String inputStr = null;
        while ((inputStr = streamReader.readLine()) != null) {
            resultSB.append(inputStr);
        }
        log.info(String.format("响应结果：%s", resultSB));
        if (!"".equalsIgnoreCase(sign)) {
            log.info(String.format("响应签名：%s", sign));
            boolean checksign = LLianPayAccpSignature.getInstance().checkSign(resultSB.toString(), sign);
            if (!checksign) {
                log.error("返回响应验证签名异常，请核实！");
                return null;
            } else {
                log.info(String.format("响应验签通过！"));
                JSONObject resultObj = JSONObject.parseObject(resultSB.toString());
                String txnStatus = resultObj.getString("txn_status");
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                if("TRADE_SUCCESS".equals(txnStatus) && Objects.nonNull(orderInfo)){
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));
                    orderService.notifyPaySuccess(resultObj.getString("txn_seqno"));
                }else {
                    log.info("响应结果异常!");
                    return null;
                }
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }


    @PostMapping(value = "/consignmentNotify")
    @ResponseBody
    @ApiOperation(value = "寄售支付",hidden = true)
    public String consignmentNotify(HttpServletRequest req) throws IOException {
        String sign = req.getHeader("Signature-Data");
        //获取request的输入流，并设置格式为UTF-8
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));
        //将输入流数据放入StringBuilder
        StringBuilder resultSB = new StringBuilder();
        String inputStr = null;
        while ((inputStr = streamReader.readLine()) != null) {
            resultSB.append(inputStr);
        }
        log.info(String.format("响应结果：%s", resultSB));
        if (!"".equalsIgnoreCase(sign)) {
            log.info(String.format("响应签名：%s", sign));
            boolean checksign = LLianPayAccpSignature.getInstance().checkSign(resultSB.toString(), sign);
            if (!checksign) {
                log.error("返回响应验证签名异常，请核实！");
                return null;
            } else {
                log.info(String.format("响应验签通过！"));
                JSONObject resultObj = JSONObject.parseObject(resultSB.toString());
                String txnStatus = resultObj.getString("txn_status");
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                if("TRADE_SUCCESS".equals(txnStatus) && Objects.nonNull(orderInfo)){
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));
               orderService.notifyPayConsignmentSuccess(resultObj.getString("txn_seqno"));
                }else {
                    log.info("响应结果异常!");
                    return null;
                }
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }

}
