package com.manyun.common.pays.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class PayAliModel implements Serializable {

    /**
     * 回调baseURL
     */
    public String webUrl;

    /**
     * 支付宝，appId
     */
    private String aliAppId;

    /**
     * 支付宝，公钥
     */
    private String aliPublicKey;

    /**
     * 支付宝，个人密钥
     */
    private String aliPrivateKey;

    /**
     * 临时字段
     */
    private String spareTemp;

    /**
     * 临时字段
     */
    private String spareRemark;
}
