package com.manyun.common.core.exception.user;

public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    public BizException()
    {
    }

    public BizException(String message)
    {
        this.message = message;
    }
}
