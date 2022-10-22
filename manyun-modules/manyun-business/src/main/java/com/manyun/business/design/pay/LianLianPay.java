package com.manyun.business.design.pay;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.entity.Order;
import com.manyun.business.domain.query.LLGeneralConsumeQuery;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.business.service.IOrderService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.RemoteUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.PayTypeEnum;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.pays.utils.llpay.LLianPayDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Wrapper;
import java.util.Objects;

/*
 * 连连支付
 *
 * @author yanwei
 * @create 2022-09-14
 */
@Slf4j
@Component
public class LianLianPay implements RootPayServer {

    @Autowired
    private IMoneyService moneyService;

    @Resource
    private RemoteBuiUserService remoteBuiUserService;

    @Value("${open.url}")
    private String url;


    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto) {
        IOrderService orderService = SpringUtil.getBean(IOrderService.class);

        if (PayTypeEnum.LIANLIAN_TYPE.getCode().equals(payInfoDto.getPayType())){
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,payInfoDto.getUserId()));
            CntUserDto cntUserDto = remoteBuiUserService.commUni(payInfoDto.getUserId(), SecurityConstants.INNER).getData();
            String orderId = (payInfoDto.getOutHost() + "-" + LLianPayDateUtils.getTimestamp());
            Order orderInfo = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, payInfoDto.getOutHost()));
            Assert.isTrue(Objects.nonNull(orderInfo),"未查询到该订单信息!");
            if(!orderId.equals(orderInfo.getTxnSeqno())){
                orderInfo.setTxnSeqno(orderId);
                orderService.updateById(orderInfo);
            }
            String body = LLPayUtils.generalConsume(
                    Builder.of(LLGeneralConsumeQuery::new)
                            .with(LLGeneralConsumeQuery::setOrderId, orderId)
                            .with(LLGeneralConsumeQuery::setGoodsName, payInfoDto.getGoodsName())
                            .with(LLGeneralConsumeQuery::setAmount,payInfoDto.getRealPayMoney())
                            .with(LLGeneralConsumeQuery::setUserId,payInfoDto.getUserId())
                            .with(LLGeneralConsumeQuery::setRealName,money.getRealName())
                            .with(LLGeneralConsumeQuery::setPhone,cntUserDto.getPhone())
                            .with(LLGeneralConsumeQuery::setCartNo,money.getCartNo())
                            .with(LLGeneralConsumeQuery::setRegisterTime, DateUtils.getDateToStr(DateUtils.toDate(cntUserDto.getCreatedTime()),DateUtils.YYYYMMDDHHMMSS))
                            .with(LLGeneralConsumeQuery::setNotifyUrl, url+payInfoDto.getLianlianPayEnum().getNotifyUrl())
                            .with(LLGeneralConsumeQuery::setIpAddr,payInfoDto.getIpaddr())
                            .with(LLGeneralConsumeQuery::setReturnUrl,payInfoDto.getLianlianPayEnum().getReturnUrl())
                            .with(LLGeneralConsumeQuery::setPayeeUserId, payInfoDto.getReceiveUserId())
                            .with(LLGeneralConsumeQuery::setServiceCharge, payInfoDto.getServiceCharge())
                            .build(), payInfoDto.isCanTrade());
            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        throw new IllegalArgumentException("not fount pay_type = " + payInfoDto.getPayType());
    }
}
