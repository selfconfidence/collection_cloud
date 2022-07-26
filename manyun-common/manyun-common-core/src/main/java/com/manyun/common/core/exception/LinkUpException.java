package com.manyun.common.core.exception;

import lombok.Data;

/**
 * 上链异常
 * 
 * @author yanwei
 */
@Data
public final class LinkUpException extends RuntimeException
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
    public LinkUpException(String userCollectionId,String errorMsg){
        this.userCollectionId = userCollectionId;
        this.errorMsg = errorMsg;
    }
    public LinkUpException(String userCollectionId,String errorMsg,String infoMessage){
        this.userCollectionId = userCollectionId;
        this.errorMsg = errorMsg;
        this.infoMessage = infoMessage;
    }

}