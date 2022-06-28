package com.manyun.business.domain.form;

import com.manyun.common.core.constant.MoneyConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@ApiModel("我的送拍提交表单")
public class AuctionSendForm {

    @ApiModelProperty(value = "商品id", required = true)
    @NotBlank
    private String goodsId;

    @ApiModelProperty(value = "商品类型,1藏品，2盲盒", required = true)
    @Range(min = 1,max = 2,message = "藏品类型错误")
    private Integer goodsType;

    @ApiModelProperty(value = "起拍价", required = true)
    @Digits(integer = MoneyConstants.MoneyPrecision, fraction = MoneyConstants.MoneyScale)
    private BigDecimal startPrice;

    @ApiModelProperty(value = "一口价", required = true)
    @Digits(integer = MoneyConstants.MoneyPrecision, fraction = MoneyConstants.MoneyScale)
    private BigDecimal soldPrice;

}
