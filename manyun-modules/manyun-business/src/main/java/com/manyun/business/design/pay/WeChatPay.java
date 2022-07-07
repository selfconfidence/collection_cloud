package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.pays.abs.impl.WxComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static com.manyun.common.core.enums.PayTypeEnum.WECHAT_TYPE;

/**
 * 微信支付
 */
@Component
public class WeChatPay implements RootPayServer {

    @Autowired
    private WxComm wxComm;

    @Autowired
    private MoneyPay moneyPay;

    /**
     *
     * @param payInfoDto 相关项
     * @return
     */
    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto){
        if (WECHAT_TYPE.getCode().equals(payInfoDto.getPayType())){
            Assert.isFalse(true,"暂未开放微信支付!");
            // 是自己执行的
            String body = wxComm.appPay(payInfoDto.getOutHost(), payInfoDto.getRealPayMoney(), payInfoDto.getWxPayEnum(), JSONUtil.toJsonStr(payInfoDto));
            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        return moneyPay.execPayVo(payInfoDto);
    }



}
