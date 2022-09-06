package com.manyun.common.security.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanwei
 * @date 2022/7/25
 */
@Data
public class CommDto implements Serializable {

    private String projectName;

    private String data;

    private long time;
}
