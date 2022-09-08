package com.manyun.admin.domain.excel;

import com.manyun.common.core.annotation.Excel;
import java.io.Serializable;

public class UserTarExcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "抽签编号",width = 40)
    private String tarId;

    @Excel(name = "用户id",width = 40)
    private String userId;

    @Excel(name = "中奖状态",readConverterExp = "1=已抽中,2=未抽中,4=等待开奖",combo = "已抽中,未抽中,等待开奖",width = 30)
    private Integer isFull;

    public String getTarId() {
        return tarId;
    }

    public void setTarId(String tarId) {
        this.tarId = tarId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getIsFull() {
        return isFull;
    }

    public void setIsFull(Integer isFull) {
        this.isFull = isFull;
    }
}
