package com.manyun.common.core.exception;

import lombok.Data;

/**
 * 转换异常
 * 
 * @author yanwei
 */
@Data
public final class LinkTranException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户链 失败回调
     */
    private String userCollectionId;

    /**
     * 失败原因
     */
    private String errorMsg;

    /**
     * 额外错误信息
     *
     *
     */
    private String infoMessage;
    public LinkTranException(String userCollectionId, String errorMsg){
        this.userCollectionId = userCollectionId;
        this.errorMsg = errorMsg;
    }
    public LinkTranException(String userCollectionId, String errorMsg, String infoMessage){
        this.userCollectionId = userCollectionId;
        this.errorMsg = errorMsg;
        this.infoMessage = infoMessage;
    }

}