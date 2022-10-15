package com.manyun.business.design.pay.bean.changePayPass;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ChangePayPassParams {

    //时间戳
    private String timestamp;

    //商户号
    private String oid_partner;

    //用户id
    private String user_id;

    //绑定卡号
    private String linked_acctno;


}
