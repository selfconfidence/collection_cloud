package com.manyun.business.design.pay.bean.random;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 随机因子获取 请求参数
 */
@Data
@EqualsAndHashCode
public class GetRandomParams {
    private String timestamp;
    private String oid_partner;
    private String user_id;
    /*
    交易发起渠道。
    ANDROID
    IOS
    H5
    PC
     */
    private String flag_chnl;
    private String pkg_name;
    private String app_name;
    /*
    加密算法。
    RSA：国际通用RSA算法
    SM2 ：国密算法
    默认 RSA算法
     */
    private String encrypt_algorithm;
}
