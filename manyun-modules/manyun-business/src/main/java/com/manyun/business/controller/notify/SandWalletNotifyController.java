package com.manyun.business.controller.notify;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.config.cashier.sdk.CertUtil;
import com.manyun.business.config.cashier.sdk.CryptoUtil;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;

@CrossOrigin
@RestController
@RequestMapping("/notify_pay/sandWallet")
@Slf4j
@Api(hidden = true)
public class SandWalletNotifyController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IAuctionPriceService auctionPriceService;

    @Autowired
    private IAuctionOrderService auctionOrderService;

    @Autowired
    private ILogsService logsService;

    @Autowired
    private IAuctionMarginService auctionMarginService;

    @Autowired
    private IMoneyService moneyService;

    @RequestMapping(value = "/sandCreateAccountNotify")
    @ApiOperation(value = "杉德开户回调",hidden = true)
    public JSONObject sandCreateAccountNotify(HttpServletRequest request) {
        log.info("进入杉德钱包开户回调-------------");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
        log.info("接收到后台通知数据："+data);
        log.info("接收到后台通知签名："+sign);
        // 验证签名
        boolean valid;
        try {
            valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicProKey(), "SHA1WithRSA");
            if (!valid) {
                log.info("验签失败");
                log.info("签名字符串(data)为："+ data);
                log.info("签名值(sign)为："+ sign);
                return null;
            }else {
                log.info("verify sign success");
                JSONObject dataJson = JSONObject.parseObject(data);
                String respCode = dataJson.getString("respCode");
                String userNo = dataJson.getString("bizUserNo");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("respCode：" + respCode);
                    if ("00000".equals(respCode)) {
                        //todo 处理业务逻辑
                        Money userMoney = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userNo));
                        userMoney.setSandAccountStatus(1);
                        moneyService.updateById(userMoney);

                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }

        return commResult();

    }

    @RequestMapping(value = "/sandWalletPayNotify")
    @ApiOperation(value = "杉德钱包支付回调（c2c）",hidden = true)
    public JSONObject sandWalletPayNotify(HttpServletRequest request) {
        log.info("进入杉德钱包支付回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
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
                return null;
            }else {
                JSONObject dataJson = JSONObject.parseObject(data);
                String respCode = dataJson.getString("respCode");
                String orderNo = dataJson.getString("orderNo");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("respCode：" + respCode);
                    if ("00000".equals(respCode)) {

                        //todo 处理业务逻辑
                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }

    public JSONObject sandWalletPayNotifyC2b(HttpServletRequest request) {
        log.info("进入回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
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
                return null;
            }else {
                log.info("verify sign success");
                JSONObject dataJson = JSONObject.parseObject(data);
                JSONObject bodyData = JSONObject.parseObject(dataJson.getString("body"));
                String orderStatus = bodyData.getString("orderStatus");
                String tradeNo = bodyData.getString("tradeNo");
                String orderAmount = bodyData.getString("buyerPayAmount");
                BigDecimal i = BigDecimal.valueOf(Integer.valueOf(orderAmount)).divide(BigDecimal.valueOf(100));
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("orderStatus：" + orderStatus);
                    log.info("tradeNo：" + tradeNo);
                    //todo业务逻辑处理

                } else {
                    log.info("通知数据异常！！！");
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }

    /**
     * 盲盒藏品支付回调（C2B）
     * @return
     */
    @RequestMapping(value = "/collectionJoinBoxNotify")
    @ApiOperation(value = "盲盒藏品聚合杉德钱包支付回调",hidden = true)
    public JSONObject collectionJoinBoxNotify(HttpServletRequest req) {
        log.info("进入盲盒藏品聚合杉德钱包支付回调");
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
                return null;
            }else {
                log.info("verify sign success");
                JSONObject dataJson = JSONObject.parseObject(data);
                JSONObject bodyData = JSONObject.parseObject(dataJson.getString("body"));
                String orderStatus = bodyData.getString("orderStatus");
                String tradeNo = bodyData.getString("tradeNo");
                String orderAmount = bodyData.getString("buyerPayAmount");
                BigDecimal i = BigDecimal.valueOf(Integer.valueOf(orderAmount)).divide(BigDecimal.valueOf(100));
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("orderStatus：" + orderStatus);
                    log.info("tradeNo：" + tradeNo.split("-")[0]);
                    log.info("orderAmount", i);
                    if ("1".equals(orderStatus)){
                        orderService.notifyPaySuccess(tradeNo.split("-")[0]);
                        /*Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, tradeNo.split("-")[0]));
                        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");*/
                        /*logsService.saveLogs(
                                LogInfoDto
                                        .builder()
                                        .buiId(order.getUserId())
                                        .jsonTxt("消费")
                                        .formInfo(i.toString())
                                        .isType(POLL_SOURCE).modelType(MONEY_TYPE).build());*/
                    }
                } else {
                    log.info("通知数据异常！！！");
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }


    /**
     * 寄售支付回调(C2C)
     * @return
     */
    @RequestMapping(value = "/consignmentNotify")
    @ApiOperation(value = "寄售支付回调",hidden = true)
    public JSONObject consignmentNotify(HttpServletRequest request) {
        log.info("进入杉德钱包寄售支付回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
        log.info("接收到后台通知数据："+data);
        log.info("接收到后台通知签名："+sign);
        // 验证签名
        boolean valid;
        try {
            valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicProKey(), "SHA1WithRSA");
            if (!valid) {
                log.info("verify sign fail.");
                log.info("签名字符串(data)为："+ data);
                log.info("签名值(sign)为："+ sign);
                return null;
            }else {
                JSONObject dataJson = JSONObject.parseObject(data);
                String respCode = dataJson.getString("respCode");
                String orderNo = dataJson.getString("orderNo");
                String orderStatus = dataJson.getString("orderStatus");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("respCode：" + respCode);
                    if ("00000".equals(respCode) && "00".equals(orderStatus)) {
                        //todo 处理业务逻辑
                        orderService.notifyPayConsignmentSuccess(orderNo.split("-")[0]);
                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }

    /**
     * 保证金支付回调(C2B)
     * @return
     */
    @RequestMapping(value = "/auctionMarginNotify")
    @ApiOperation(value = "保证金支付回调",hidden = true)
    public JSONObject auctionMarginNotify(HttpServletRequest request) {
        log.info("进入保证金杉德钱包支付回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
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
                return null;
            }else {
                log.info("verify sign success");
                JSONObject dataJson = JSONObject.parseObject(data);
                JSONObject bodyData = JSONObject.parseObject(dataJson.getString("body"));
                String orderStatus = bodyData.getString("orderStatus");
                String tradeNo = bodyData.getString("tradeNo");
                String orderAmount = bodyData.getString("buyerPayAmount");
                BigDecimal i = BigDecimal.valueOf(Integer.valueOf(orderAmount)).divide(BigDecimal.valueOf(100));
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("orderStatus：" + orderStatus);
                    log.info("tradeNo：" + tradeNo.split("-")[0]);
                    log.info("orderAmount", i);
                    if ("1".equals(orderStatus)){
                        auctionPriceService.notifyPayMarginSuccess(tradeNo.split("-")[0]);
                        /*Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, tradeNo));
                        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
                        logsService.saveLogs(
                                LogInfoDto
                                        .builder()
                                        .buiId(order.getUserId())
                                        .jsonTxt("消费")
                                        .formInfo(i.toString())
                                        .isType(POLL_SOURCE).modelType(MONEY_TYPE).build());*/
                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }

    /**
     * 拍卖支付回调(C2C)
     * @return
     */
    @RequestMapping(value = "/auctionNotify")
    @ApiOperation(value = "拍卖支付回调",hidden = true)
    public JSONObject auctionNotify(HttpServletRequest request) {
        log.info("进入杉德钱包拍卖支付回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
        log.info("接收到后台通知数据："+data);
        log.info("接收到后台通知签名："+sign);
        // 验证签名
        boolean valid;
        try {
            valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicProKey(), "SHA1WithRSA");
            if (!valid) {
                log.info("verify sign fail.");
                log.info("签名字符串(data)为："+ data);
                log.info("签名值(sign)为："+ sign);
                return null;
            }else {
                JSONObject dataJson = JSONObject.parseObject(data);
                String respCode = dataJson.getString("respCode");
                String orderNo = dataJson.getString("orderNo");
                String orderStatus = dataJson.getString("orderStatus");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("respCode：" + respCode);
                    if ("00000".equals(respCode) && "00".equals(orderStatus)) {
                        //todo 处理业务逻辑
                        auctionOrderService.notifyPaySuccess(orderNo.split("-")[0]);
                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }


    /**
     * 一口价支付回调(C2C)
     * @return
     */
    @RequestMapping(value = "/auctionFixNotify")
    @ApiOperation(value = "一口价支付回调",hidden = true)
    public JSONObject auctionFixNotify(HttpServletRequest request) {
        log.info("进入杉德钱包拍卖支付回调");
        String data=request.getParameter("data");
        String sign=request.getParameter("sign");
        log.info("接收到后台通知数据："+data);
        log.info("接收到后台通知签名："+sign);
        // 验证签名
        boolean valid;
        try {
            valid = CryptoUtil.verifyDigitalSign(data.getBytes("utf-8"), Base64.decodeBase64(sign),
                    CertUtil.getPublicProKey(), "SHA1WithRSA");
            if (!valid) {
                log.info("verify sign fail.");
                log.info("签名字符串(data)为："+ data);
                log.info("签名值(sign)为："+ sign);
                return null;
            }else {
                JSONObject dataJson = JSONObject.parseObject(data);
                String respCode = dataJson.getString("respCode");
                String orderNo = dataJson.getString("orderNo");
                String orderStatus = dataJson.getString("orderStatus");
                if (dataJson != null) {
                    log.info("后台通知业务数据为：" + JSONObject.toJSONString(dataJson, true));
                    log.info("respCode：" + respCode);
                    if ("00000".equals(respCode) && "00".equals(orderStatus)) {
                        //todo 处理业务逻辑
                        auctionOrderService.notifyPaySuccess(orderNo.split("-")[0]);
                    } else {
                        return null;
                    }
                } else {
                    log.info("通知数据异常！！！");
                    return null;
                }
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return  null;
        }
        return commResult();
    }




    private JSONObject commResult(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("respCode", "000000");
        return jsonObject;
    }

    @RequestMapping(value = "/xxxxxx")
    @ApiOperation(value = "杉德开户回调",hidden = true)
    public String receiveNotice(HttpServletRequest req, HttpServletResponse resp){

        Map<String, String[]> parameterMap = req.getParameterMap();

        if(parameterMap != null && !parameterMap.isEmpty()) {
            String data=req.getParameter("data");
            String sign=req.getParameter("sign");
            String signType =req.getParameter("signType");
            // 验证签名
            boolean valid;
            try {
                Class ceasClass = Class.forName("cn.com.sand.ceas.sdk.CeasHttpUtil");
                Object o = ceasClass.newInstance();
                Method method = ceasClass.getDeclaredMethod("verifySign", JSONObject.class);
                method.setAccessible(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data",data);
                jsonObject.put("sign",sign);
                jsonObject.put("signType",signType);
                //执行verifySign方法
                valid = (boolean) method.invoke(o, jsonObject);
                if (!valid) {//如果验签失败
                    log.error("verify sign fail.");
                    log.error("验签失败的签名字符串(data)为："+ data);
                    log.error("验签失败的签名值(sign)为："+ sign);
                }else {//验签成功
                    log.info("verify sign success");
                    JSONObject dataJson = JSONObject.parseObject(data);
                    if (dataJson != null) {
                        log.info("接收到的异步通知数据为：\n"+ JSONObject.toJSONString(dataJson, true));
                    } else {
                        log.error("接收异步通知数据异常！！！");
                    }
                    return "respCode=000000";
                }
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return null;
    }



}
