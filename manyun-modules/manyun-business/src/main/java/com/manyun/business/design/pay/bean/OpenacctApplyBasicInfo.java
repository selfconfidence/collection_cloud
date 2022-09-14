package com.manyun.business.design.pay.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class OpenacctApplyBasicInfo {
    private String reg_phone;
    private String user_name;
    private String id_type;
    private String id_no;
    private String id_exp;
    private String reg_email;
    private String address;
    private String occupation;
}
