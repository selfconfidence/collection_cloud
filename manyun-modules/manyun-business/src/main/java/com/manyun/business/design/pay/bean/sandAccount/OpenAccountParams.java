package com.manyun.business.design.pay.bean.sandAccount;

import lombok.Data;

@Data
public class OpenAccountParams {

    private String userId;

    private String userName;

    private String returnUrl;

    private String notifyUrl;

}
