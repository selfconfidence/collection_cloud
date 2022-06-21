package com.manyun.common.core.domain;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Author yanwei
 * @Date: 2018/5/16 16:47
 * @Description:
 * data 兼容器， T swagger-ui 才可以解析
 */
@Getter
public class IResult<T> implements Serializable {
    private final T data;

    public IResult(T data) {
        this.data = data;
    }

//    public <T> T getData() {
//
//        return (T) this.data;
//    }


}
