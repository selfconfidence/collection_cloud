package com.manyun.business.design.pay.bean.random;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 随机因子获取 响应参数
 */
@Data
@EqualsAndHashCode
public class GetRandomResult {
    private String ret_code;
    private String ret_msg;
    private String oid_partner;
    private String user_id;
    private String random_key;
    private String random_value;
    private String license;
    private String map_arr;
    private String rsa_public_content;
    private String sm2_key_hex;
}
