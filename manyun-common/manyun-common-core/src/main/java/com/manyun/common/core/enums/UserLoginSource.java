package com.manyun.common.core.enums;

import lombok.Getter;

/**
 * 用户登录来源
 * 
 * @author ruoyi
 */
public enum UserLoginSource
{
    APP("app"), PC("admin"), H5("h5");


    private  String info;

    UserLoginSource(String info)
    {

        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
