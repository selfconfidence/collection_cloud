package com.manyun.business.design.pay;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.design.pay.bean.sandAccount.SandWalletPayParamsForApp;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.business.service.IOrderService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.PayTypeEnum;
import com.manyun.common.pays.utils.llpay.LLianPayDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
public class SandWalletPay implements RootPayServer {

    @Autowired
    private IMoneyService moneyService;

    @Value("${open.url}")
    private String url;


    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto) {
        IOrderService orderService = SpringUtil.getBean(IOrderService.class);
        if (PayTypeEnum.SANDWALLET_TYPE.getCode().equals(payInfoDto.getPayType())) {
            String sandOrderNo = (payInfoDto.getOutHost() + "-" + RandomUtil.randomNumbers(7));
            Order orderInfo = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, payInfoDto.getOutHost()));
            LocalDateTime endTime = orderInfo.getEndTime();
            if(Objects.nonNull(orderInfo) && !sandOrderNo.equals(orderInfo.getSandOrderNo())){
                orderInfo.setSandOrderNo(sandOrderNo);
                orderService.updateById(orderInfo);
            }
            String body = "";
            SandWalletPayParamsForApp payParamsForApp = null;
            if (payInfoDto.isC2c()) {
                body = SandPayUtil.sandWalletPay(payInfoDto, sandOrderNo, url, endTime);
                payParamsForApp = SandPayUtil.sandWalletPayForApp(payInfoDto, sandOrderNo, url, endTime);
            } else {
                UserMoneyDto userMoneyDto = moneyService.userMoneyInfo(payInfoDto.getUserId());
                body = SandPayUtil.sanAccountPayC2B(payInfoDto, userMoneyDto.getRealName(), sandOrderNo, url, endTime);
                payParamsForApp = SandPayUtil.sanAccountPayC2BForApp(payInfoDto, userMoneyDto.getRealName(), sandOrderNo, url, endTime);
            }

            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayParamsForApp, payParamsForApp).with(PayVo::setSandOrderNo, sandOrderNo).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        throw new IllegalArgumentException("not fount pay_type = " + payInfoDto.getPayType());
    }

}
