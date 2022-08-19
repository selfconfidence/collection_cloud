package com.manyun.comm.api.domain.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yanwei
 * @date 2022/8/19
 */
@Data
@ToString
public class CallAccountDto implements Serializable {
    private String userId;
    private String nickName;
    private String realName;
    private String realPhone;
    private String date;




}
