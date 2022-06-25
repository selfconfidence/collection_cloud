package com.manyun.comm.api.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OpenPleaseBoxDto implements Serializable {


    private String userId;

    private String boxId;

    private String sourceInfo;

    private Integer goodsNum;

}
