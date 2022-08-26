package com.manyun.business.domain.dto;

import lombok.Data;

@Data
public class UserMoneyDto {

    /**
     * 用户id;平台内部生成,短编号(不是主键)
     */
    private String userNumber;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String cartNo;

}
