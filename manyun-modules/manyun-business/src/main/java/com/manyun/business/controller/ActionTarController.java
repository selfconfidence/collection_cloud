package com.manyun.business.controller;


import cn.hutool.core.util.IdUtil;
import com.manyun.business.config.cashier.sdk.CertUtil;
import com.manyun.business.design.pay.LLPayUtils;
import com.manyun.business.design.pay.ShandePay;
import com.manyun.business.design.pay.bean.query.AcctserialAcctbal;
import com.manyun.business.design.pay.bean.query.LinkedAcctlist;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.query.LLGeneralConsumeQuery;
import com.manyun.business.domain.query.LLInnerUserQuery;
import com.manyun.business.domain.query.LLUserTopupQuery;
import com.manyun.common.core.domain.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

/**
 * <p>
 * 活动合成表附属信息 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Controller
@RequestMapping("/actionTar")
@Slf4j
@CrossOrigin(origins = "*")
public class ActionTarController {


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        //开户
       return LLPayUtils.innerUser(
               Builder.of(LLInnerUserQuery::new)
                       .with(LLInnerUserQuery::setUserId,"1569888830315663360")
                       .with(LLInnerUserQuery::setPhone,"15703977803")
                       .with(LLInnerUserQuery::setRealName,"和飞杭")
                       .with(LLInnerUserQuery::setCartNo,"41102419930808401X")
                       .with(LLInnerUserQuery::setReturnUrl,"http://h5.dcalliance.com.cn/")
               .build()
       );
       //充值
      /* return LLPayUtils.userTopup(
               Builder.of(LLUserTopupQuery::new)
               .with(LLUserTopupQuery::setUserId,"1556912957824274432")
               .with(LLUserTopupQuery::setRealName,"高丰雷")
               .with(LLUserTopupQuery::setPhone,"15036380340")
               .with(LLUserTopupQuery::setIpAddr,"172.16.14.194")
               .with(LLUserTopupQuery::setAmount,BigDecimal.valueOf(0.05))
               .with(LLUserTopupQuery::setReturnUrl,"http://h5.dcalliance.com.cn/")
               .build()
       );*/
      /*//消费
        return LLPayUtils.generalConsume(
                Builder.of(LLGeneralConsumeQuery::new)
                .with(LLGeneralConsumeQuery::setOrderId, IdUtil.getSnowflakeNextIdStr())
                .with(LLGeneralConsumeQuery::setGoodsName, "啦啦啦")
                .with(LLGeneralConsumeQuery::setAmount, BigDecimal.valueOf(0.2))
                .with(LLGeneralConsumeQuery::setUserId,"4564623145465")
                .with(LLGeneralConsumeQuery::setRealName,"高丰雷")
                .with(LLGeneralConsumeQuery::setPhone,"15036380340")
                .with(LLGeneralConsumeQuery::setIpAddr,"127.0.0.1")
                .with(LLGeneralConsumeQuery::setReturnUrl,"http://h5.dcalliance.com.cn/")
                .build()
        );*/
        //余额
        //return LLPayUtils.queryAcctinfo("1556912957824274432");
        //绑卡列表数据
        //return LLPayUtils.queryLinkedacct("LLianPayTest-In-User-12345");
        //资金流水
        //return LLPayUtils.queryAcctserial("1556912957824274432","20220913000000","20220914000000","1","10");
    }

    /**
     * 墨云支付成功回调
     *
     * @param request 回调请求
     * @return 回调响应参数
     * @throws IOException 读取配置文件异常
     */
    @GetMapping("/myNotify")
    @ResponseBody
    public void myNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("进入回调方法---------");
        String key = "09a9709dd74d464998a15c011844bd0d";
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> map = new HashMap<>(2);
        parameterMap.forEach((k, v) -> map.put(k, String.join(",", v)));
        if (map.size() < 1) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF8");
            PrintWriter writer = response.getWriter();
            writer.write("fail");
            writer.close();
            return;
        }
        String sign = map.remove("sign");
        String sign2 = encrypt(map, key);
        if (sign2.equals(sign)) {
            String out_trade_no = request.getParameter("orderNo");
            BigDecimal money = new BigDecimal(request.getParameter("money"));
            if ("RECEIVED".equals(String.valueOf(request.getParameter("status")))) {
                log.info("走进来了~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                log.info("订单号为:"+out_trade_no+"-----------------------"+money+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //System.out.println("走进来了~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                //System.out.println("订单号为:"+out_trade_no+"-----------------------"+money+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            }
            //todo 支付成功操作
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF8");
            PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.close();
            return;
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF8");
        log.info("response_____+++++------"+ response.toString());
        PrintWriter writer = response.getWriter();
        writer.write("fail");
        writer.close();
    }



    /**
     * 签名
     *
     * @param srcMap 带加密的字符串
     * @return 签名字符串
     */
    public String encrypt(Map<String, String> srcMap, String key) {
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String src = map2String(srcMap);
            src += "&key=" + key;
            messageDigest.update(src.getBytes(StandardCharsets.UTF_8));
            byte[] mdResult = messageDigest.digest();
            for (byte b : mdResult) {
                result.append(Integer.toHexString(0x000000ff & b | 0xffffff00).substring(6));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * map 排序 转换为 string
     *
     * @param map map
     * @return string
     */
    public String map2String(Map<String, String> map) {
        Map<String, String> treeMap =
                new TreeMap<>(
                        new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(o2);
                            }
                        });
        treeMap.putAll(map);
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            stringBuilder
                    .append(entry.getKey().trim())
                    .append("=")
                    .append(entry.getValue().trim())
                    .append("&");
        }
        String result = stringBuilder.toString();
        return result.substring(0, result.length() - 1);
    }



}

