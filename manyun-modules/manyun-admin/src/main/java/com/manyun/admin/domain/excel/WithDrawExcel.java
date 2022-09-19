package com.manyun.admin.domain.excel;

import com.manyun.common.core.annotation.Excel;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class WithDrawExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "手机号")
    private String phone;



}
