package com.manyun.business.controller.notify;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.shaded.com.google.gson.JsonObject;
import com.manyun.business.design.pay.bean.cashier.ResponsePayee;
import com.manyun.business.design.pay.bean.cashier.ResponsePayer;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Money;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.pays.utils.llpay.security.LLianPayAccpSignature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.*;

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

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private IAuctionPriceService auctionPriceService;

    @Autowired
    private IAuctionOrderService auctionOrderService;

    @Autowired
    private ILogsService logsService;


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
                String userId = resultObj.getString("user_id");
                String userStatus = resultObj.getString("user_status");
                log.info("user_status:======="+resultObj.getString("user_status"));
                log.info("remark:======="+resultObj.getString("remark"));
                if("NORMAL".equals(userStatus)){
                    Money money = Builder.of(Money::new).with(Money::setLlAccountStatus,"1").build();
                    moneyService.update(money, Wrappers.<Money>lambdaUpdate().eq(Money::getUserId,userId));
                }else {
                    log.error("开户失败!");
                    return null;
                }
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
    public String LlBindCardApplyNotify(HttpServletRequest req) throws IOException {
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
            }
        }else {
            log.error("返回响应验证签名异常，请核实！");
            return null;
        }
        return "Success";
    }

    @PostMapping(value = "/LlUserTopupNotify")
    @ResponseBody
    @ApiOperation(value = "连连支付充值回调",hidden = true)
    public String LlUserTopupNotify(HttpServletRequest req) throws IOException {
        log.info("连连支付充值回调:=============================进入回调了");
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
                if("TRADE_SUCCESS".equals(txnStatus)){
                    JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                    log.info("订单信息:============================="+orderInfo.toString());
                    List<ResponsePayer> responsePayerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                    log.info("付款方信息:============================="+responsePayerList.toString());
                    String payer_id = responsePayerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                    log.info("用户id:============================="+payer_id);
                    String amount = orderInfo.getString("total_amount");
                    log.info("多少钱:============================="+amount);
                    logsService.saveLogs(
                            LogInfoDto
                                    .builder()
                                    .buiId(payer_id)
                                    .jsonTxt("账户余额充值")
                                    .formInfo(amount)
                                    .isType(PULL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
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
                    log.info("txn_seqno:======="+orderInfo.getString("txn_seqno"));

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
        log.info("连连支付提现回调:=============================进入回调了");
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
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                log.info("订单信息:============================="+orderInfo.toString());
                List<ResponsePayer> responsePayerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                log.info("付款方信息:============================="+responsePayerList.toString());
                String payer_id = responsePayerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                log.info("payer_id:============================="+payer_id);
                String amount = orderInfo.getString("total_amount");
                log.info("amount:============================="+amount);
                logsService.saveLogs(
                        LogInfoDto
                                .builder()
                                .buiId(payer_id)
                                .jsonTxt("账户余额提现")
                                .formInfo(amount)
                                .isType(POLL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
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
        log.info("进入连连盲盒藏品支付回调");
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
                if("TRADE_SUCCESS".equals(txnStatus)){
                    JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                    List<ResponsePayer> payerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                    String payer_id = payerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                    log.info("txn_seqno:======="+orderInfo.getString("txn_seqno").split("-")[0]);
                    log.info("payer_id:======="+payer_id);
                    orderService.notifyPaySuccess(orderInfo.getString("txn_seqno").split("-")[0]);
                    logsService.saveLogs(
                            LogInfoDto
                                    .builder()
                                    .buiId(payer_id)
                                    .jsonTxt("用户购买商品")
                                    .formInfo(orderInfo.getString("total_amount"))
                                    .isType(POLL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
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
                if("TRADE_SUCCESS".equals(txnStatus)){
                JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                List<ResponsePayer> payerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                String payer_id = payerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                List<ResponsePayee> responsePayeeList = JSONObject.parseArray(resultObj.getJSONArray("payeeInfo").toJSONString(), ResponsePayee.class);
                log.info("txn_seqno:======="+orderInfo.getString("txn_seqno"));
                log.info("responsePayeeList:========"+responsePayeeList.toString());
                orderService.notifyPayConsignmentSuccess(orderInfo.getString("txn_seqno").split("-")[0]);
                List<LogInfoDto> list = new ArrayList<>();
                list.add(LogInfoDto
                        .builder()
                        .buiId(payer_id)
                        .jsonTxt("用户寄售商品交易")
                        .formInfo(orderInfo.getString("total_amount"))
                        .isType(POLL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                if(responsePayeeList.size()>0){
                    Optional<ResponsePayee> optional = responsePayeeList.parallelStream().filter(f -> f.getPayee_type().equals("USER")).findFirst();
                    if(optional.isPresent()){
                        list.add(LogInfoDto
                                .builder()
                                .buiId(optional.get().getPayee_id())
                                .jsonTxt("用户寄售商品交易")
                                .formInfo(optional.get().getAmount())
                                .isType(PULL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                    }
                }
                if(list.size()>0){
                    logsService.saveBatch(
                            list.parallelStream().map(m->{
                                Logs logs = Builder.of(Logs::new).build();
                                BeanUtil.copyProperties(m,logs);
                                logs.setId(IdUtil.getSnowflake().nextIdStr());
                                logs.createD(m.getBuiId());
                                return logs;
                            }).collect(Collectors.toList())
                    );
                }
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

    @PostMapping(value = "/auctionMarginNotify")
    @ResponseBody
    @ApiOperation(value = "保证金支付",hidden = true)
    public String auctionMarginNotify(HttpServletRequest req) throws IOException {
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
                String tradeNo = orderInfo.getString("txn_seqno").split("-")[0];
                if("TRADE_SUCCESS".equals(txnStatus) && Objects.nonNull(orderInfo)){
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));
                    auctionPriceService.notifyPayMarginSuccess(tradeNo);
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

    @PostMapping(value = "/auctionNotify")
    @ResponseBody
    @ApiOperation(value = "拍卖支付",hidden = true)
    public String auctionNotify(HttpServletRequest req) throws IOException {
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
                if("TRADE_SUCCESS".equals(txnStatus)){
                    JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                    List<ResponsePayer> payerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                    String payer_id = payerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                    List<ResponsePayee> responsePayeeList = JSONObject.parseArray(resultObj.getJSONArray("payeeInfo").toJSONString(), ResponsePayee.class);
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));
                    log.info("responsePayeeList:======="+responsePayeeList.toString());
                    auctionOrderService.notifyPaySuccess(orderInfo.getString("txn_seqno").split("-")[0]);
                    List<LogInfoDto> list = new ArrayList<>();
                    list.add(LogInfoDto
                            .builder()
                            .buiId(payer_id)
                            .jsonTxt("用户拍卖商品交易")
                            .formInfo(orderInfo.getString("total_amount"))
                            .isType(POLL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                    if(responsePayeeList.size()>0){
                        Optional<ResponsePayee> optional = responsePayeeList.parallelStream().filter(f -> f.getPayee_type().equals("USER")).findFirst();
                        if(optional.isPresent()){
                            list.add(LogInfoDto
                                    .builder()
                                    .buiId(optional.get().getPayee_id())
                                    .jsonTxt("用户拍卖商品交易")
                                    .formInfo(optional.get().getAmount())
                                    .isType(PULL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                        }
                    }
                    if(list.size()>0){
                        logsService.saveBatch(
                                list.parallelStream().map(m->{
                                    Logs logs = Builder.of(Logs::new).build();
                                    BeanUtil.copyProperties(m,logs);
                                    logs.setId(IdUtil.getSnowflake().nextIdStr());
                                    logs.createD(m.getBuiId());
                                    return logs;
                                }).collect(Collectors.toList())
                        );
                    }
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

    @PostMapping(value = "/auctionFixNotify")
    @ResponseBody
    @ApiOperation(value = "一口价支付",hidden = true)
    public String auctionFixNotify(HttpServletRequest req) throws IOException {
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
                if("TRADE_SUCCESS".equals(txnStatus)){
                    JSONObject orderInfo = JSONObject.parseObject(resultObj.getString("orderInfo"));
                    List<ResponsePayer> payerList = JSONObject.parseArray(resultObj.getJSONArray("payerInfo").toJSONString(), ResponsePayer.class);
                    String payer_id = payerList.parallelStream().filter(f -> f.getPayer_type().equals("USER")).findFirst().get().getPayer_id();
                    List<ResponsePayee> responsePayeeList = JSONObject.parseArray(resultObj.getJSONArray("payeeInfo").toJSONString(), ResponsePayee.class);
                    log.info("txn_seqno:======="+resultObj.getString("txn_seqno"));
                    log.info("responsePayeeList:======="+responsePayeeList.toString());
                    auctionOrderService.notifyPaySuccess(orderInfo.getString("txn_seqno").split("-")[0]);
                    List<LogInfoDto> list = new ArrayList<>();
                    list.add(LogInfoDto
                            .builder()
                            .buiId(payer_id)
                            .jsonTxt("用户商品交易")
                            .formInfo(orderInfo.getString("total_amount"))
                            .isType(POLL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                    if(responsePayeeList.size()>0){
                        Optional<ResponsePayee> optional = responsePayeeList.parallelStream().filter(f -> f.getPayee_type().equals("USER")).findFirst();
                        if(optional.isPresent()){
                            list.add(LogInfoDto
                                    .builder()
                                    .buiId(optional.get().getPayee_id())
                                    .jsonTxt("用户商品交易")
                                    .formInfo(optional.get().getAmount())
                                    .isType(PULL_SOURCE).modelType(LL_MONEY_MODEL_TYPE).build());
                        }
                    }
                    if(list.size()>0){
                        logsService.saveBatch(
                                list.parallelStream().map(m->{
                                    Logs logs = Builder.of(Logs::new).build();
                                    BeanUtil.copyProperties(m,logs);
                                    logs.setId(IdUtil.getSnowflake().nextIdStr());
                                    logs.createD(m.getBuiId());
                                    return logs;
                                }).collect(Collectors.toList())
                        );
                    }
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
