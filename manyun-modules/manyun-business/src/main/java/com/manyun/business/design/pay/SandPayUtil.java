package com.manyun.business.design.pay;

import cn.com.sand.ceas.sdk.CeasHttpUtil;
import cn.com.sand.ceas.sdk.config.CertCache;
import cn.com.sand.ceas.sdk.util.SignatureUtils;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.manyun.business.design.pay.bean.sandAccount.*;
import com.manyun.business.design.pay.bean.sandEnum.SandMethodEnum;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.service.IMoneyService;
import com.manyun.common.core.utils.MD5Util;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class SandPayUtil {

    @Autowired
    private static IMoneyService moneyService;




    public static void main(String[] args) {
        //openAccount(new OpenAccountParams());
        //sandWalletPay();
        //sanAccountPayC2B();
        /*String plainText ="123456789";//AES加密后的data
        log.info("待签名报文：{}", plainText);
        String sign = "";
        try {
            sign = SignatureUtils.sign(plainText, "SHA1WithRSA", CertCache.getCertCache().getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sign);*/
    }

    public static String sandWalletPay(PayInfoDto payInfoDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String createTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,1);
        String endTime = sdf.format(calendar.getTime());

        String version = "10";
        //商户号
        String mer_no = "6888801048981";
        //商户key1
        String mer_key = "PZWgkPqM09qpDbUngCDdGL5kgg/9jDd3wvZUn87m0CnI6CklVzmtMi2AkXOd7P4xWmEjjzEo5bo=";
        //订单号
        String mer_order_no = payInfoDto.getOutHost();
        //回调地址
        String notify_url = payInfoDto.getSandAccountEnum().getNotifyUrl();
        String return_url = payInfoDto.getSandAccountEnum().getReturnUrl();
        //金额
        String order_amt = payInfoDto.getRealPayMoney().toString();
        //商品名称
        String goods_name = payInfoDto.getGoodsName();

        String create_ip = payInfoDto.getIpaddr().replace(".","_");

        /*String mer_order_no = IdUtil.objectId();
        //回调地址
        String notify_url = "www.baidu.com";
        String return_url = "www.baidu.com";
        //金额
        String order_amt = "0.5";
        //商品名称
        String goods_name = "测试";

        String create_ip = "127_0_0_1";*/


        //支付扩展域
        //"operationType":"操作类型",//  1:转账申请 2:确认收款 3:转账退回
        // "recvUserId":"收款方会员编号", //所有操作类型必填参数
        // "remark":"备注" //非必填

        // 当operationType为1时参数按照下面说明填：return_url,notify_url为必填参数
        //  "bizType":"转账类型", //必填 1：转账确认模式 2：实时转账模式
        //  "payUserId":"付款方会员编号，用户在商户系统中的唯一编号 ；",//必填
        //  "userFeeAmt":"用户服务费，商户向用户收取的服务费 ",//非必填
        //   "postscript":"附言",// 非必填
        String pay_extra = "{\"operationType\":\"1\",\"recvUserId\":\""+ payInfoDto.getReceiveUserId()+"\",\"bizType\":\"2\",\"payUserId\":\""+payInfoDto.getUserId()+"\",\"userFeeAmt\":\""+payInfoDto.getServiceCharge()+"\"}";
        //String pay_extra = "{\"userId\":\""+userNumber+"\",\"userName\":\""+realName+"\",\"idCard\":\""+cartNo+"\"}";
        //md5key
        String key = "7K5ebmqXFyW7H+0f9pFg3pqFC9RJGpaWH9CtpBMv/2lsqE2dwWMDvIY9LWLQDFZkY5SGoaF6n5WXBNCeuEaVy6OyyTxXr+vDJIO14AMZ11iBu4eML//3XbkwbAckAEQGs5w3/aGir9xixuz+UKFTiw==";



        Map<String, String> map = new LinkedHashMap<>();
        map.put("accsplit_flag","NO");
        map.put("create_ip",create_ip);
        map.put("create_time",createTime);

        map.put("mer_key",mer_key);
        map.put("mer_no",mer_no);
        map.put("mer_order_no",mer_order_no);
        map.put("notify_url",notify_url);
        map.put("order_amt",order_amt);
        map.put("pay_extra",pay_extra);
        map.put("return_url",return_url);
        map.put("sign_type","MD5");
        map.put("store_id","000000");
        map.put("version",version);
        map.put("key",key);


//        map.put("expire_time",endTime);
//        map.put("goods_name",goods_name);
//        map.put("product_code","02010006");
//        map.put("clear_cycle","0");

        String signature = "";

        for (String s : map.keySet()){
            if(!(map.get(s)==null||map.get(s).equals(""))){
                signature+=s+"=";
                signature+=map.get(s)+"&";
            }
        }
        signature = signature.substring(0,signature.length()-1);
        System.out.println("参与签名字符串：\n"+signature);

        String sign = MD5Util.encode(signature).toUpperCase();

        System.out.println("签名串：\n"+sign);


        //拼接url
        String url = "https://faspay-oss.sandpay.com.cn/pay/h5/cloud?" +
//     云函数h5： applet  ；支付宝H5：alipay  ； 微信公众号H5：wechatpay   ；
// 一键快捷：fastpayment   ；H5快捷 ：unionpayh5    ；支付宝扫码：alipaycode ;快捷充值:quicktopup
//电子钱包【云账户】：cloud
                "version="+version+"" +
                "&mer_no="+mer_no+"" +
                "&mer_key="+URLEncoder.encode(mer_key)+"" +
                "&mer_order_no="+mer_order_no+"" +
                "&create_time="+createTime+"" +
                "&expire_time="+endTime+"" +  //endTime
                "&order_amt=" +order_amt +"" +
                "&notify_url="+URLEncoder.encode(notify_url)+"" +
                "&return_url=" +URLEncoder.encode(return_url)+"" +
                "&create_ip="+ create_ip + "" +
                "&goods_name="+URLEncoder.encode(goods_name)+"" +
                "&store_id=000000" +
// 产品编码: 云函数h5：  02010006  ；支付宝H5：  02020002  ；微信公众号H5：02010002   ；
//一键快捷：  05030001  ；H5快捷：  06030001   ；支付宝扫码：  02020005 ；快捷充值：  06030003
//电子钱包【云账户】：开通账户并支付product_code应为：04010001；消费（C2C）product_code 为：04010003 ; 我的账户页面 product_code 为：00000001
                "&product_code=04010003" +   ""+
                "&clear_cycle=3" +
                "&pay_extra="     +URLEncoder.encode(pay_extra)+""+
                "&meta_option=%5B%7B%22s%22%3A%22Android%22,%22n%22%3A%22wxDemo%22,%22id%22%3A%22com.pay.paytypetest%22,%22sc%22%3A%22com.pay.paytypetest%22%7D%5D" +
                "&accsplit_flag=NO" +
                "&jump_scheme=" +

                "&sign_type=MD5" +
                "&sign="+sign+"" ;

        System.out.println("最终链接：\n\n"+url);
        return url;
    }

    public static String sanAccountPayC2B(PayInfoDto payInfoDto, String realName) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String createTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,1);
        String endTime = sdf.format(calendar.getTime());

        String version = "10";
        //商户号
        String mer_no = "6888801048981";
        //商户key1
        String mer_key = "PZWgkPqM09qpDbUngCDdGL5kgg/9jDd3wvZUn87m0CnI6CklVzmtMi2AkXOd7P4xWmEjjzEo5bo=";
        //订单号
        String mer_order_no = IdUtil.objectId();
        //回调地址
        String notify_url = payInfoDto.getSandAccountEnum().getNotifyUrl();
        String return_url = payInfoDto.getSandAccountEnum().getReturnUrl();
        //金额
        String order_amt = payInfoDto.getRealPayMoney().toString();
        //商品名称
        String goods_name = payInfoDto.getGoodsName();

        String create_ip = payInfoDto.getIpaddr().replace(".","_");
        //支付扩展域
        //"userId":"用户在商户系统中的唯一编号", "nickName":"会员昵称","accountType"："账户类型"  （选填）
        String pay_extra = "{\"userId\":\""+payInfoDto.getUserId()+"\",\"nickName\":\""+realName+"\",\"accountType\":\"1\"}";
        //String pay_extra = "{\"userId\":\""+payInfoDto.getUserId()+"\",\"userName\":\""+realName+"\",\"idCard\":\""+cartNo+"\"}";

        //md5key
        String key = "7K5ebmqXFyW7H+0f9pFg3pqFC9RJGpaWH9CtpBMv/2lsqE2dwWMDvIY9LWLQDFZkY5SGoaF6n5WXBNCeuEaVy6OyyTxXr+vDJIO14AMZ11iBu4eML//3XbkwbAckAEQGs5w3/aGir9xixuz+UKFTiw==";



        Map<String, String> map = new LinkedHashMap<>();
        map.put("accsplit_flag","NO");
        map.put("create_ip",create_ip);
        map.put("create_time",createTime);

        map.put("mer_key",mer_key);
        map.put("mer_no",mer_no);
        map.put("mer_order_no",mer_order_no);
        map.put("notify_url",notify_url);
        map.put("order_amt",order_amt);
        map.put("pay_extra",pay_extra);
        map.put("return_url",return_url);
        map.put("sign_type","MD5");
        map.put("store_id","000000");
        map.put("version",version);
        map.put("key",key);


//        map.put("expire_time",endTime);
//        map.put("goods_name",goods_name);
//        map.put("product_code","02010006");
//        map.put("clear_cycle","0");

        String signature = "";

        for (String s : map.keySet()){
            if(!(map.get(s)==null||map.get(s).equals(""))){
                signature+=s+"=";
                signature+=map.get(s)+"&";
            }
        }
        signature = signature.substring(0,signature.length()-1);
        System.out.println("参与签名字符串：\n"+signature);

        String sign = MD5Util.encode(signature).toUpperCase();

        System.out.println("签名串：\n"+sign);


        //拼接url
        String url = "https://faspay-oss.sandpay.com.cn/pay/h5/cloud?" +
//     云函数h5： applet  ；支付宝H5：alipay  ； 微信公众号H5：wechatpay   ；
// 一键快捷：fastpayment   ；H5快捷 ：unionpayh5    ；支付宝扫码：alipaycode ;快捷充值:quicktopup
//电子钱包【云账户】：cloud
                "version="+version+"" +
                "&mer_no="+mer_no+"" +
                "&mer_key="+URLEncoder.encode(mer_key)+"" +
                "&mer_order_no="+mer_order_no+"" +
                "&create_time="+createTime+"" +
                "&expire_time="+endTime+"" +  //endTime
                "&order_amt="+order_amt+"" +
                "&notify_url="+URLEncoder.encode(notify_url)+"" +
                "&return_url=" +URLEncoder.encode(return_url)+"" +
                "&create_ip="+create_ip+"" +
                "&goods_name="+URLEncoder.encode(goods_name)+"" +
                "&store_id=000000" +
// 产品编码: 云函数h5：  02010006  ；支付宝H5：  02020002  ；微信公众号H5：02010002   ；
//一键快捷：  05030001  ；H5快捷：  06030001   ；支付宝扫码：  02020005 ；快捷充值：  06030003
//电子钱包【云账户】：开通账户并支付product_code应为：04010001；消费（C2C）product_code 为：04010003 ; 我的账户页面 product_code 为：00000001
                "&product_code=04010001" +   ""+
                "&clear_cycle=3" +
                "&pay_extra="     +URLEncoder.encode(pay_extra)+""+
                "&meta_option=%5B%7B%22s%22%3A%22Android%22,%22n%22%3A%22wxDemo%22,%22id%22%3A%22com.pay.paytypetest%22,%22sc%22%3A%22com.pay.paytypetest%22%7D%5D" +
                "&accsplit_flag=NO" +
                "&jump_scheme=" +

                "&sign_type=MD5" +
                "&sign="+sign+"" ;

        System.out.println("最终链接：\n\n"+url);
        return url;
    }


    public static String openAccount(OpenAccountParams openAccountParams) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String createTime = sdf.format(calendar.getTime());
        calendar.add(Calendar.HOUR,1);
        String endTime = sdf.format(calendar.getTime());

        String version = "10";
        //商户号
        String mer_no = "6888801048981";
        //商户key1
        String mer_key = "PZWgkPqM09qpDbUngCDdGL5kgg/9jDd3wvZUn87m0CnI6CklVzmtMi2AkXOd7P4xWmEjjzEo5bo=";
        //订单号

        String mer_order_no = IdUtil.objectId();
        //回调地址
        String notify_url = openAccountParams.getNotifyUrl();

        String return_url = openAccountParams.getReturnUrl();
        //金额
        String order_amt = "0.5";
        //商品名称
        String goods_name = "开户";
        String userId = openAccountParams.getUserId();
        String nickName = openAccountParams.getUserName();

        String create_ip = IpUtils.getIpAddr(ServletUtils.getRequest()).replace(".","_");
        //支付扩展域
        //"userId":"用户在商户系统中的唯一编号", "nickName":"会员昵称","accountType"："账户类型"  （选填）
        String pay_extra = "{\"userId\":\""+ userId +"\",\"nickName\":\""+nickName+"\",\"accountType\":\"1\"}";
        //String pay_extra = "{\"userId\":\""+userNumber+"\",\"userName\":\""+realName+"\",\"idCard\":\""+cartNo+"\"}";

        //md5key
        String key = "7K5ebmqXFyW7H+0f9pFg3pqFC9RJGpaWH9CtpBMv/2lsqE2dwWMDvIY9LWLQDFZkY5SGoaF6n5WXBNCeuEaVy6OyyTxXr+vDJIO14AMZ11iBu4eML//3XbkwbAckAEQGs5w3/aGir9xixuz+UKFTiw==";



        Map<String, String> map = new LinkedHashMap<>();
        map.put("accsplit_flag","NO");
        map.put("create_ip", create_ip);
        map.put("create_time",createTime);

        map.put("mer_key",mer_key);
        map.put("mer_no",mer_no);
        map.put("mer_order_no",mer_order_no);
        map.put("notify_url",notify_url);
        map.put("order_amt",order_amt);
        map.put("pay_extra",pay_extra);
        map.put("return_url",return_url);
        map.put("sign_type","MD5");
        map.put("store_id","000000");
        map.put("version",version);
        map.put("key",key);


//        map.put("expire_time",endTime);
//        map.put("goods_name",goods_name);
//        map.put("product_code","02010006");
//        map.put("clear_cycle","0");

        String signature = "";

        for (String s : map.keySet()){
            if(!(map.get(s)==null||map.get(s).equals(""))){
                signature+=s+"=";
                signature+=map.get(s)+"&";
            }
        }
        signature = signature.substring(0,signature.length()-1);
        log.info("参与签名字符串：\n"+signature);

        String sign = MD5Util.encode(signature).toUpperCase();

        log.info("签名串：\n"+sign);


        //拼接url
        String url = "https://faspay-oss.sandpay.com.cn/pay/h5/cloud?" +
//     云函数h5： applet  ；支付宝H5：alipay  ； 微信公众号H5：wechatpay   ；
// 一键快捷：fastpayment   ；H5快捷 ：unionpayh5    ；支付宝扫码：alipaycode ;快捷充值:quicktopup
//电子钱包【云账户】：cloud
                "version="+version+"" +
                "&mer_no="+mer_no+"" +
                "&mer_key="+ URLEncoder.encode(mer_key)+"" +
                "&mer_order_no="+mer_order_no+"" +
                "&create_time="+createTime+"" +
                "&expire_time="+endTime+"" +  //endTime
                "&order_amt="+ order_amt +""+
                "&notify_url="+URLEncoder.encode(notify_url)+"" +
                "&return_url=" +URLEncoder.encode(return_url)+"" +
                "&create_ip=" + create_ip +""+
                "&goods_name="+URLEncoder.encode(goods_name)+"" +
                "&store_id=000000" +
// 产品编码: 云函数h5：  02010006  ；支付宝H5：  02020002  ；微信公众号H5：02010002   ；
//一键快捷：  05030001  ；H5快捷：  06030001   ；支付宝扫码：  02020005 ；快捷充值：  06030003
//电子钱包【云账户】：开通账户并支付product_code应为：04010001；消费（C2C）product_code 为：04010003 ; 我的账户页面 product_code 为：00000001
                "&product_code=00000001" +   ""+
                "&clear_cycle=3" +
                "&pay_extra=" +URLEncoder.encode(pay_extra)+""+
                "&meta_option=%5B%7B%22s%22%3A%22Android%22,%22n%22%3A%22wxDemo%22,%22id%22%3A%22com.pay.paytypetest%22,%22sc%22%3A%22com.pay.paytypetest%22%7D%5D" +
                "&accsplit_flag=NO" +
                "&jump_scheme=" +

                "&sign_type=MD5" +
                "&sign="+sign+"" ;

        log.info("最终链接：\n\n"+url);

        return url;
    }


    /**
     * 个人会员注册并开户
     * 需要先发送短信，然后再调用协议签约接口签约才算开户成功
     */
    public void openAccountApi(SandOpenAccountParams openAccountParams) {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", ""); //商户订单号 需要和发送短信订单号一致
        param.put("bizUserNo", openAccountParams.getBizUserNo()); //会员编号
        param.put("nickName", openAccountParams.getNickName()); //昵称
        param.put("mobile", openAccountParams.getMobile()); //手机号码
        param.put("name", openAccountParams.getName()); //客户姓名
        param.put("idType", "01"); //证件类型 01:身份证
        param.put("idNo", openAccountParams.getIdNo()); //证件号码
        param.put("smsCode", openAccountParams.getSmsCode()); //短信验证码
        param.put("smsSerialNo", openAccountParams.getSmsSerialNo()); //短信验证码流水号
        // TODO: 银行卡及身份证可先不传，后边通过关联卡及附件上传操作，会自动绑定
        JSONObject bankInfo = new JSONObject();
        bankInfo.put("cardNo", openAccountParams.getCardNo()); //卡号
        bankInfo.put("bankMobile", openAccountParams.getMobile());//银行预留手机号
        param.put("bankInfo", bankInfo); //绑卡信息
//        JSONObject idImgList = new JSONObject();
//        idImgList.put("frontImgNo", ""); //国徽面文件编号
//        idImgList.put("reverseImgNo", ""); //人脸面文件编号
//        param.put("idImgList", idImgList); //身份证附件

        JSONObject respResult = invoke(param, SandMethodEnum.CEAS_ACCOUNT_REGISTER_OPEN_SERVER);

    }


    /**
     * 发送短信
     * 用于个人会员注册及开户接口发送短信用
     */
    public String smsSend(SandSmsSendParam sandSmsSendParam) {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());//商户订单号
        param.put("bizUserNo", sandSmsSendParam.getUserId());//会员编号
        param.put("mobile", sandSmsSendParam.getPhone());
        param.put("bizScene", "01");//业务场景号：01：开户
        JSONObject resp = invoke(param, SandMethodEnum.CEAS_SERVER_SMS_SEND);
        return resp.getString("smsSerialNo");
    }

    /**
     * 会员状态查询
     * 可以通过该接口进行会员相关状态查询。
     */
    public void queryAccountStatus() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",DemoBase.getCustomerOrderNo()); //商户订单号 可以和关联卡订单号一致
        param.put("bizUserNo", ""); //会员编号
        invoke(param, SandMethodEnum.CEAS_ELEC_MEMBER_STATUS_QUERY);
    }


    /**
     * 关联银行卡
     * 当绑定的是 02:快捷充值+提现卡时，需要调用绑卡确认接口
     */
    public void bindCard() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",  DemoBase.getCustomerOrderNo()); //商户订单号 不能重复
        param.put("bizUserNo", ""); //会员编号
        param.put("cardNo", ""); //卡号
        param.put("bankMobile", ""); //银行预留手机号
        param.put("relatedCardType", "01"); //绑卡类型 01：提现卡（默认）02:快捷充值+提现卡
        invoke(param, SandMethodEnum.CEAS_ELEC_BIND_CARD);
    }

    /**
     * 绑卡确认
     * 适用于关联快捷充值提现银行卡时进行短信确认。
     */
    public void bindCardConfirm() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo()); //商户订单号
        param.put("bizUserNo", ""); //会员编号
        param.put("notifyUrl", ""); //异步通知地址
        param.put("oriCustomerOrderNo", ""); //原绑卡订单号
        param.put("smsCode", ""); //短信验证码
        invoke(param, SandMethodEnum.CEAS_ELEC_BIND_CARD_CONFIRM);
    }

    /**
     * 关联卡信息查询
     */
    public void queryBindCardList() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",  DemoBase.getCustomerOrderNo()); //商户订单号 可以和关联银行卡订单号一致
        param.put("bizUserNo", ""); //会员编号
        //param.put("relatedCardNo", ""); //关联卡编号
        invoke(param, SandMethodEnum.CEAS_ELEC_BIND_CARD_QUERY);
    }

    /**
     * 解绑关联卡
     */
    public void unBindCard() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",  DemoBase.getCustomerOrderNo()); //商户订单号 不能重复
        param.put("bizUserNo", ""); //会员编号
        param.put("relatedCardNo", ""); //关联卡编号
        invoke(param, SandMethodEnum.CEAS_ELEC_UNBIND_CARD);
    }

    /**
     * 协议签约
     */
    public void protocolSign() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo()); //会员编号
        param.put("bizUserNo", ""); //会员编号
        JSONObject signProtocol = new JSONObject();//签约信息域 json格式
        signProtocol.put("protocolNo","XY001");//XY001：开户协议 XY002：商户代扣协议
        param.put("signProtocol", signProtocol);
        param.put("frontUrl","");//前端跳转地址
        param.put("notifyUrl","");//异步通知地址
        invoke(param, SandMethodEnum.CEAS_ELEC_ACCOUNT_PROTOCOL_SIGN);
    }


    /**
     * 密码状态查询
     * 查询会员是否设置密码
     *  00-未设置
     *  01-已设置
     */
    public void queryPasswordStatus() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo()); //商户订单号
        param.put("bizUserNo", ""); //会员编号
        invoke(param, SandMethodEnum.CEAS_ELEC_ACCOUNT_PAY_PASSWORD_QUERY);
    }


    /**
     * 密码管理
     *  --首次设置密码和忘记密码重置密码。
     *  --修改密码。
     *  --重置注册手机号。
     */
    public void passwordManage() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo()); //商户订单号
        param.put("bizUserNo", ""); //会员编号
        param.put("pageType", "01"); //页面类型 默认01标准页面
        param.put("managementType", "01"); //密码管理类型 01：设置/重置支付密码 02：修改支付密码 03: 重置会员手机号
        param.put("frontUrl", ""); //前台跳转地址
        param.put("notifyUrl", ""); //异步通知地址
        param.put("extend", ""); //扩展域
        param.put("merchExtendParams", ""); //商户扩展参数
        invoke(param, SandMethodEnum.CEAS_ELEC_ACCOUNT_PAY_PASSWORD_MANAGE);
    }


    /**
     * 账户余额查询
     * 个人账户余额查询
     */
    public void accountBalanceQuery() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo()); //商户订单号 可以和关联卡订单号一致
        param.put("bizUserNo", ""); //会员编号
        param.put("accountType", "01"); //账户类型 01：支付电子户 02：宝易付权益电子户 03：无资金权益户
        invoke(param, SandMethodEnum.CEAS_ELEC_QUERY_ACCOUNT_BALANCE);
    }

    /**
     * 账户变动明细查询
     *     a.查询会员账户变动明细
     *     b.支持查询资金账户、权益账户、奖励金账户
     */
    public void testQueryAccChange(){
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",DemoBase.getCustomerOrderNo()); //商户订单号
        param.put("bizUserNo", ""); //会员编号
        param.put("accountType", "01"); // 账户类型 01：支付电子户 02：权益账户 03：奖励金户
        param.put("beginDate", "20220901"); //变动起始日期
        param.put("endDate", "20220908"); //变动结束日期  最大7日
        param.put("IoFlag", "00"); //变动类型 00：全部（默认） 01: 出金 02：入金
        param.put("pageNo", "1"); //页码 必须从1开始
        param.put("pageSize", "10"); //每页条数
        invoke(param, SandMethodEnum.CEAS_ELEC_ACC_CHANGE_DETAILS);
    }


    /**
     * 个人转账申请
     * 转账申请（会员转出）
     */
    public void transferPerson() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());
        param.put("accountType", "01"); //账户类型 01：支付电子户 02：宝易付权益电子户 03：奖励金户
        param.put("orderAmt", "0.08");//订单金额
        param.put("payer", new AccountInfo("付款方会员编号", "会员真实姓名"));//付款方
        param.put("payee", new AccountInfo("收款方会员编号", "会员真实姓名"));//收款方
        param.put("postscript", "转账测试");//附言
        param.put("remark", "");//备注
        param.put("frontUrl", ""); //前台跳转地址
        param.put("notifyUrl", ""); //异步通知地址
//        param.put("receiveTimeOut", ""); //超时回退时间 yyyyMMddHHmmss （只在bizType＝NON_REALTIME时生效）最多传7天
//        param.put("orderTimeOut", ""); //订单支付超时时间 yyyyMMddHHmmss
//        param.put("agreementNo", "");//签约编号 仅协议扣款交易上送
        param.put("bizType", "NON_REALTIME");//转账类型 NON_REALTIME：转账确认模式 REALTIME：实时转账模式
//        param.put("userFeeAmt", "");//用户服务费
        invoke(param, SandMethodEnum.CEAS_SERVER_TRANSFER_PERSON);
    }

    /**
     * 云账户后台充值
     * 采用后台接口模式，无需跳转杉德页面。
     * 通过充值接口下单获得支付凭证，通过支付凭证可以调用支付工具付款。
     * 需要再调用资金操作确认接口中输入短信验证码确认付款
     */
    public void backgroundPaymentDeposit() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());//商户订单号
//        param.put("orderTimeOut", "");//订单超时时间 格式：yyyymmddHHmmss默认2小时
        param.put("payTool", "QUICKPAY");//QUICKPAY快捷充值  UNION_PAY_H5 银联H5快捷
        JSONObject payExtend = new JSONObject();
        payExtend.put("relatedCardNo","");//关联卡编号
        param.put("payExtend",payExtend);//充值扩展域 //QUICKPAY的是关联卡编号 relatedCardNo
        param.put("bizUserNo","");//会员编号
        param.put("walletAmt","0.1");//充值金额
        param.put("frontUrl", ""); //前台跳转地址
        param.put("notifyUrl", ""); //异步通知地址
        param.put("extend", "");//扩展域
//        JSONObject marketingInfo = new JSONObject();营销域
//        marketingInfo.put("activityNo","");//活动编号
//        marketingInfo.put("mktAmt","");//奖励金金额
//        param.put("userFeeAmt", "");//用户服务费 扩暂不支持，预留
        invoke(param, SandMethodEnum.CEAS_SERVER_PAYMENT_DEPOSIT);
    }

    /**
     * 资金操作确认
     * 后台充值、发放红包后确认付款
     */
    public void orderConfirm() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());
        param.put("oriCustomerOrderNo", "");//原交易订单号
        param.put("oriOrderAmt", "0.01");//原订单金额
        param.put("smsCode", "");//短信验证码
        invoke(param, SandMethodEnum.CEAS_SERVER_ORDER_CONFIRM);
    }


    /**
     * 提现申请
     */
    public void withDraw() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());//商户订单号
        param.put("bizUserNo", "");//会员编号
        param.put("accountType", "01");//账户类型 01：支付电子户 02：宝易付权益电子户
        param.put("orderAmt", "0.01"); //订单金额
        param.put("relatedCardNo", "");//关联卡编号
        param.put("postscript", "提现测试");//附言
        param.put("remark", "");//备注
        param.put("userFeeAmt", "0.00");//用户服务费
        param.put("frontUrl", ""); //前台跳转地址
        param.put("notifyUrl", ""); //异步通知地址
        invoke(param, SandMethodEnum.CEAS_SERVER_WITHDRAW_APPLY);
    }


    /**
     * 付款申请
     */
    public SandPaymentApplyResp paymentApply(SandPaymentApplyParams applyParams) {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());//商户订单号
        param.put("orderAmt",applyParams.getOrderAmt());//订单金额
//        param.put("orderTimeOut", "");//订单支付超时时间//yyyymmddHHmmss 不填默认30分钟 最大2小时
        JSONObject payer = new JSONObject();//付款方信息域
        payer.put("bizUserNo",applyParams.getBizUserNo());//会员编号
        payer.put("remark","支付");//付款备注
        param.put("payer", payer);//付款方
        JSONObject payeeList = new JSONObject();
        payeeList.put("bizUserNo",applyParams.getPayeeBizUserNo());//会员编号
        payeeList.put("payeeCustomerOrderNo",IdUtil.objectId());//收款订单号
        payeeList.put("payeeAmt","0.01");//收款订单金额
        payeeList.put("remark","收款");//备注
        param.put("payeeList", payeeList);//收款方
        param.put("accountingMode", "IMMEDIATELY");//入账模式 IMMEDIATELY 即时模式（默认） GUARANTEE 担保模式
        param.put("payeeBizUserNo", applyParams.getPayeeBizUserNo());//主收款人编号
        param.put("orderSubject", "test subject");//订单标题 不能包含特殊字符
        param.put("orderDesc", "支付申请");//订单描述 不能包含特殊字符
        param.put("frontUrl", applyParams.getFrontUrl()); //前台跳转地址
        param.put("notifyUrl", applyParams.getNotifyUrl()); //异步通知地址
        JSONObject invoke = invoke(param, SandMethodEnum.CEAS_SERVER_PAYMENT_APPLY);
        SandPaymentApplyResp sandPaymentApplyResp = new SandPaymentApplyResp();
        sandPaymentApplyResp.setAuthWay(invoke.getString("authWay"));
        sandPaymentApplyResp.setFeeAmt(invoke.getString("feeAmt"));
        sandPaymentApplyResp.setPasswordURL(StrUtil.isNotBlank(invoke.getString("passwordURL")) ? invoke.getString("passwordURL") : null);

        return sandPaymentApplyResp;
    }


    /**
     * 交易订单查询
     * 查询充值转账提现交易订单状态和订单信息
     */
    public void testTransInfoQuery() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());//商户订单号
        param.put("oriCustomerOrderNo","");//原交易订单号
//        param.put("oriPayeeCustomerOrderNo", "");//原交易子订单号
        invoke(param, SandMethodEnum.CEAS_SERVER_TRANS_INFO_QUERY);
    }

    /**
     * 订单状态查询
     */
    public void testQueryOrder() {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo", DemoBase.getCustomerOrderNo());
        param.put("oriCustomerOrderNo", "");//原交易订单号
        invoke(param, SandMethodEnum.CEAS_SERVER_ORDER_QUERY);
    }



    /**
     * 附件上传
     */
    public void fileUpload(MultipartFile file) {
        JSONObject param = new JSONObject();
        //param.put("customerOrderNo",  DemoBase.getCustomerOrderNo()); //商户订单号
        param.put("bizUserNo", ""); //会员编号
        param.put("fileContent", getImgStr(file)); // 附件base64
        param.put("fileType", "01"); //附件类型 01—身份证国徽面 02—身份证人像面
        invoke(param, SandMethodEnum.CEAS_ELEC_PAPERS_UPLOAD);
    }



    /**
     * 通用调用方法
     */
    private JSONObject invoke(JSONObject param, SandMethodEnum sandMethodEnum) {
        JSONObject resp = null;
        try {
            resp = CeasHttpUtil.doPost(param, sandMethodEnum);
            String jsonResp = JSONObject.toJSONString(resp, true);
            log.info("响应报文：\n"+jsonResp);
        } catch (Exception e) {
            log.error("invoke ", e);
        }
        return resp;
    }

    /**
     * 文件转base64
     * @return
     */
    private String getImgStr(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("图片不能为空！");
        }
        //String fileName = file.getOriginalFilename();
        //String fileType = fileName.substring(fileName.lastIndexOf("."));
        String contentType = file.getContentType();
        byte[] imageBytes = null;
        String base64EncoderImg="";
        try {
            imageBytes = file.getBytes();
            BASE64Encoder base64Encoder =new BASE64Encoder();
            /**
             * 1.Java使用BASE64Encoder 需要添加图片头（"data:" + contentType + ";base64,"），
             *   其中contentType是文件的内容格式。
             * 2.Java中在使用BASE64Enconder().encode()会出现字符串换行问题，这是因为RFC 822中规定，
             *   每72个字符中加一个换行符号，这样会造成在使用base64字符串时出现问题，
             *   所以我们在使用时要先用replaceAll("[\\s*\t\n\r]", "")解决换行的问题。
             */
            base64EncoderImg = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
            base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return base64EncoderImg;
    }

    private InputStream getImageStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            log.info("获取网络图片出现异常，图片路径为：" + url);
            e.printStackTrace();
        }
        return null;

    }
}
