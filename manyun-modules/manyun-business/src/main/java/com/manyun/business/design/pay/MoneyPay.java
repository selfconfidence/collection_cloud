package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.business.service.IOrderService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.pays.abs.impl.WxComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.PayTypeEnum.WECHAT_TYPE;

/**
 * 余额支付
 */
@Component
public class MoneyPay implements RootPayServer {

    @Autowired
    private IMoneyService moneyService;
    @Autowired
    private ShandePay shandePay;


    /**
     *
     * @param payInfoDto 相关项
     * @return
     */
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        if (MONEY_TAPE.getCode().equals(payInfoDto.getPayType())){
            String body = null;
            // 是自己执行的
            // 钱包自行扣除进行支付 - 可以组合支付..... 不够可以组合支付！！！ -银联
            String formInfo = StrUtil.format("消费");
            BigDecimal moneyPayMoney = moneyService.ordePay(payInfoDto.getOutHost(), payInfoDto.getUserId(), payInfoDto.getRealPayMoney(), formInfo);
            BigDecimal moneyBln = payInfoDto.getRealPayMoney();
            if (moneyPayMoney.compareTo(NumberUtil.add(0D)) >=1 ){
                moneyBln = payInfoDto.getRealPayMoney().subtract(moneyPayMoney).abs();
                //moneyPayMoney
                payInfoDto.setRealPayMoney(moneyPayMoney);
                body = shandePay.pay(payInfoDto);
                //Assert.isFalse(true,"银联未对接;余额不足,请核实！");
            }
            return Builder.of(PayVo::new).with(PayVo::setMoneyBln,moneyBln).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();

        }

        return shandePay.execPayVo(payInfoDto);
    }



}
