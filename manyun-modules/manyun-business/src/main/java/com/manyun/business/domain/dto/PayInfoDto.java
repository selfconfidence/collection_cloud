package com.manyun.business.domain.dto;

import com.manyun.common.core.enums.AliPayEnum;
import com.manyun.common.core.enums.WxPayEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付内部参项
 */
@Data
@Builder
public class PayInfoDto implements Serializable {

    private String outHost;
    private BigDecimal realPayMoney;
    private String userId;
    private Integer payType;
    private AliPayEnum aliPayEnum;
    private WxPayEnum wxPayEnum;


}
