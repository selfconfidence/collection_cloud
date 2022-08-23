package com.manyun.admin.domain.excel;

import com.manyun.common.core.annotation.Excel;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class UserPhoneExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "手机号")
    private String phone;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
