package com.manyun.comm.api.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/*
 * 阿里云短信内部透传
 *
 * @author yanwei
 * @create 2022-08-30
 */
@Data
public class BatchSmsCommDto implements Serializable {

    private Set<String> phoneNumbers;

    private String templateCode;

    private Map<String,String> paramsMap;
}
