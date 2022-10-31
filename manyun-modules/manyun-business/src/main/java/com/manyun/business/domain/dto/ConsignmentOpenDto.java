package com.manyun.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ConsignmentOpenDto {

    private String name;


    private String buiId;

    private BigDecimal price;




}
