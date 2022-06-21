package com.manyun.business.design.pay;

import cn.hutool.json.JSONUtil;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.pays.abs.impl.AliComm;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.manyun.common.core.enums.PayTypeEnum.ALI_TYPE;

/**
 * 支付宝支付
 */
@Component
public class AliPay implements RootPayServer {

    @Autowired
    private WeChatPay weChatPay;

    @Autowired
    private AliComm aliComm;

    /**
     *
     * @param payInfoDto 支付相关信息项
     * @return
     */
    @SneakyThrows
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        if (ALI_TYPE.getCode().equals(payInfoDto.getPayType())){
            // 是自己执行的
            String body =  aliComm.appPay(payInfoDto.getOutHost(),payInfoDto.getRealPayMoney(),payInfoDto.getAliPayEnum(), JSONUtil.toJsonStr(payInfoDto));
            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        return weChatPay.execPayVo(payInfoDto);
    }



}
