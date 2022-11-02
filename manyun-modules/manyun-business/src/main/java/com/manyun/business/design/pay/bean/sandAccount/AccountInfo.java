package com.manyun.business.design.pay.bean.sandAccount;


/**
 * @desc 交易账户信息
 * @date 2021/03/12
 * @author: sandpay
 */
public class AccountInfo {
    /**
     * 账户号
     */
    private String accountNo;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 内部会员编号
     */
    private String bizUserNo;

    /**
     * 账户名称
     */
    private String name;

    public AccountInfo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public AccountInfo(String bizUserNo, String name) {
        this.bizUserNo = bizUserNo;
        this.name = name;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public String getBizUserNo() {
        return bizUserNo;
    }

    public void setBizUserNo(String bizUserNo) {
        this.bizUserNo = bizUserNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
