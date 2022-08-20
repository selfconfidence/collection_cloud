package com.manyun.business.controller;


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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
public class ActionTarController {

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
                System.out.println("走进来了~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("订单号为:"+out_trade_no+"-----------------------"+money+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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

