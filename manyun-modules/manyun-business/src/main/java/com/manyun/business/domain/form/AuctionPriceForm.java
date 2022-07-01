package com.manyun.business.domain.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("我要出价提交参数")
public class AuctionPriceForm implements Serializable {

    @ApiModelProperty("送拍id")
    @NotBlank
    private String auctionSendId;
}
