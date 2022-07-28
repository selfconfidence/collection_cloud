package com.manyun.admin.domain.excel;

import com.manyun.common.core.annotation.Excel;
import com.manyun.common.core.xss.Xss;

import javax.validation.constraints.Size;
import java.io.Serializable;

public class PostExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "手机号")
    private String phone;

    @Excel(name = "商品名称")
    private String buiName;

    @Excel(name = "商品类型",readConverterExp = "1=盲盒,2=藏品",combo = "盲盒,藏品")
    private String typeName;

    @Excel(name = "备注",width=60)
    private String reMark;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Xss(message = "商品名称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "商品名称长度不能超过30个字符")
    public String getBuiName() {
        return buiName;
    }

    public void setBuiName(String buiName) {
        this.buiName = buiName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Xss(message = "备注不能包含脚本字符")
    @Size(min = 0, max = 500, message = "备注长度不能超过500个字符")
    public String getReMark() {
        return reMark;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
    }
}
