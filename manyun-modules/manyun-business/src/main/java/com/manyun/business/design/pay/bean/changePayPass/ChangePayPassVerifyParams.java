package com.manyun.business.design.pay.bean.changePayPass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ChangePayPassVerifyParams {

    //时间戳
    private String timestamp;

    //商户号
    private String oid_partner;

    //用户id
    private String user_id;

    //授权令牌，有效期为30分钟。
    private String token;

    //短信验证码
    private String verify_code;

    //密码随机因子key
    private String random_key;

    //新支付密码
    private String password;
}
