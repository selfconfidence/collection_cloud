package com.manyun.comm.api.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/*
 * 阿里云短信内部透传
 *
 * @author yanwei
 * @create 2022-08-30
 */
@Data
public class SmsCommDto implements Serializable {

    private String phoneNumber;

    private String templateCode;

    private Map<String,String> paramsMap;
}
