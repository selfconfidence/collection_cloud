package com.manyun.business.design.pay;

import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 总支付链
 */
@Component
public class RootPay implements RootPayServer {

    @Autowired
    private AliPay aliPay;

    /**
     *
     * @param payInfoDto 支付信息
     * @return
     */
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        return aliPay.execPayVo(payInfoDto);
    }



}
