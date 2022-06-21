package com.manyun.common.pays.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayWxModel implements Serializable {




    /**
     * 回调baseURL
     */
    public String webUrl;
    /**
     * 微信 app ID
     */
    private String wxAppId;

    /**
     * 微信 商户ID
     */
    private String wxMchId;

    /**
     * 微信 证书地址
     */
    private String wxCertAddr;

    /**
     * 微信，商户KEY
     */
    private String wxMchKey;


    /**
     * 临时字段
     */
    private String spareTemp;

    /**
     * 临时字段
     */
    private String spareRemark;
}
