package com.manyun.business.design.pay;

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

import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.PayTypeEnum.WECHAT_TYPE;

/**
 * 余额支付
 */
@Component
public class MoneyPay implements RootPayServer {

    @Autowired
    private IMoneyService moneyService;



    /**
     *
     * @param payInfoDto 相关项
     * @return
     */
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        if (MONEY_TAPE.getCode().equals(payInfoDto.getPayType())){
            // 是自己执行的
            // 钱包自行扣除进行支付
            String formInfo = StrUtil.format("消费-{}", payInfoDto.getRealPayMoney().toString());
            moneyService.ordePay(payInfoDto.getOutHost(),payInfoDto.getUserId(),payInfoDto.getRealPayMoney(),formInfo);
        }
        throw new IllegalArgumentException("not fount pay_type = " + payInfoDto.getPayType());
    }



}
