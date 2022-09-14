package com.manyun.business.domain.query;

import lombok.Data;

@Data
public class LLTiedCardQuery {

    //用户id
    private String userId;
    //绑定的银行卡账号
    private String linkedAcctno;
    //绑定的手机号
    private String linkedPhone;
    //绑定的支付密码
    private String password;

}
