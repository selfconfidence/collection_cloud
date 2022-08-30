package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.PayTypeEnum;
import com.manyun.common.core.enums.ShandePayEnum;
import com.manyun.common.core.utils.MD5Util;
import com.manyun.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 杉德支付
 */
@Component
@Slf4j
public class ShandePay implements RootPayServer {



    @Autowired
    private IMoneyService moneyService;

    public static final String defaultReturnUrl = "http://h5.dcalliance.com.cn/";

    /**
     *
     * @param payInfoDto 相关项
     * @return
     */
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        if (PayTypeEnum.SHANDE_TYPE.getCode().equals(payInfoDto.getPayType())){
            String body = pay(payInfoDto);
            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        throw new IllegalArgumentException("not fount pay_type = " + payInfoDto.getPayType());

    }

    /**
     * 杉德支付
     * @param payInfoDto 所需参数
     * @return 支付的url
     */
    public String pay(PayInfoDto payInfoDto) {
        UserMoneyDto userMoneyDto = moneyService.userMoneyInfo(payInfoDto.getUserId());
        Assert.isTrue((Objects.nonNull(payInfoDto) && Objects.nonNull(userMoneyDto)),"支付失败!");
        ShandePayEnum shandePayEnum = payInfoDto.getShandePayEnum();
        //用户短编号
        String userNumber = userMoneyDto.getUserNumber();
        //真实姓名
        String realName = userMoneyDto.getRealName();
        //身份证号
        String cartNo = userMoneyDto.getCartNo();
        //订单号
        String outTradeNo = payInfoDto.getOutHost();
        //商品名称
        String goodsName = payInfoDto.getGoodsName();
        //商品价格
        BigDecimal amount = payInfoDto.getRealPayMoney();
        //ip
        String ipaddr = payInfoDto.getIpaddr();
        Assert.isTrue(
                (
                        StringUtils.isNotBlank(userNumber)
                     && StringUtils.isNotBlank(realName)
                     && StringUtils.isNotBlank(cartNo)
                     && StringUtils.isNotBlank(outTradeNo)
                     && StringUtils.isNotBlank(goodsName)
                     && StringUtils.isNotBlank(amount.toString())
                     && StringUtils.isNotBlank(ipaddr)
                ),
                "缺失支付参数!");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String createTime = sdf.format(calendar.getTime());
        //订单失效时间-----建议设置0.5～1小时
        calendar.add(Calendar.MINUTE, 5);
        String endTime = sdf.format(calendar.getTime());

        String version = "10";
        //商户号
        String mer_no = "6888801048981"; //活的
        //商户key1
        String mer_key = "PZWgkPqM09qpDbUngCDdGL5kgg/9jDd3wvZUn87m0CnI6CklVzmtMi2AkXOd7P4xWmEjjzEo5bo="; //活的
        //回调地址
        String notify_url = shandePayEnum.getNotifyUrl();
        String return_url = shandePayEnum.getReturnUrl();
        //支付扩展域
        //"userId":"用户在商户下唯一标识 1-10位",
        // "userName":"证件姓名",
        // "idCard":"18位身份证号码"
        String pay_extra = "{\"userId\":\""+userNumber+"\",\"userName\":\""+realName+"\",\"idCard\":\""+cartNo+"\"}";
        //ip
        String ipAddr = ipaddr.replace(".","_");

        //md5key
        String key = "7K5ebmqXFyW7H+0f9pFg3pqFC9RJGpaWH9CtpBMv/2lsqE2dwWMDvIY9LWLQDFZkY5SGoaF6n5WXBNCeuEaVy6OyyTxXr+vDJIO14AMZ11iBu4eML//3XbkwbAckAEQGs5w3/aGir9xixuz+UKFTiw=="; //活的


        Map<String, String> map = new LinkedHashMap<>();
        map.put("accsplit_flag","NO");
        map.put("create_ip",ipAddr);
        map.put("create_time",createTime);

        map.put("mer_key",mer_key);
        map.put("mer_no",mer_no);
        map.put("mer_order_no",outTradeNo);
        map.put("notify_url",notify_url);
        map.put("order_amt",amount.toString());
        map.put("pay_extra",pay_extra);
        map.put("return_url",return_url);
        map.put("sign_type","MD5");
        map.put("store_id","000000");
        map.put("version",version);
        map.put("key",key);

        String signature = "";

        for (String s : map.keySet()){
            if(!(map.get(s)==null||map.get(s).equals(""))){
                signature+=s+"=";
                signature+=map.get(s)+"&";
            }
        }
        signature = signature.substring(0,signature.length()-1);

        String sign = MD5Util.encode(signature).toUpperCase();

        //拼接
        String url = "https://sandcash.mixienet.com.cn/pay/h5/quicktopup?" +
                "version="+version+"" +
                "&mer_no="+mer_no+"" +
                "&mer_key="+ URLEncoder.encode(mer_key)+"" +
                "&mer_order_no="+outTradeNo+"" +
                "&create_time="+createTime+"" +
                "&expire_time="+endTime+"" +  //endTime
                "&order_amt="+ amount+"" +
                "&notify_url="+URLEncoder.encode(notify_url)+"" +
                "&return_url=" +URLEncoder.encode(return_url)+"" +
                "&create_ip=" + ipAddr +
                "&goods_name="+URLEncoder.encode(goodsName)+"" +
                "&store_id=000000" +
                "&product_code=06030003" +   ""+
                "&clear_cycle=3" +
                "&pay_extra="     +URLEncoder.encode(pay_extra)+""+
                "&meta_option=%5B%7B%22s%22%3A%22Android%22,%22n%22%3A%22wxDemo%22,%22id%22%3A%22com.pay.paytypetest%22,%22sc%22%3A%22com.pay.paytypetest%22%7D%5D" +
                "&accsplit_flag=NO" +
                "&jump_scheme=" +

                "&sign_type=MD5" +
                "&sign="+sign+"" ;
        log.info("最终链接：\n\n"+url);
        return url;
    }

    public static void main(String[] args) {

    }

}
