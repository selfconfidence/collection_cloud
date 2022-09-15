package com.manyun.business.design.pay;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.query.LLGeneralConsumeQuery;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.RemoteUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.PayTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Wrapper;

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

    @Override
    public PayVo execPayVo(PayInfoDto payInfoDto) {

        if (PayTypeEnum.LIANLIAN_TYPE.getCode().equals(payInfoDto.getPayType())){
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId,payInfoDto.getUserId()));
            CntUserDto cntUserDto = remoteBuiUserService.commUni(payInfoDto.getUserId(), SecurityConstants.INNER).getData();
            String body = LLPayUtils.generalConsume(
                    Builder.of(LLGeneralConsumeQuery::new)
                            .with(LLGeneralConsumeQuery::setOrderId, payInfoDto.getOutHost())
                            .with(LLGeneralConsumeQuery::setGoodsName, payInfoDto.getGoodsName())
                            .with(LLGeneralConsumeQuery::setAmount,payInfoDto.getRealPayMoney())
                            .with(LLGeneralConsumeQuery::setUserId,payInfoDto.getUserId())
                            .with(LLGeneralConsumeQuery::setRealName,money.getRealName())
                            .with(LLGeneralConsumeQuery::setPhone,cntUserDto.getPhone())
                            .with(LLGeneralConsumeQuery::setNotifyUrl, payInfoDto.getLianlianPayEnum().getNotifyUrl())
                            .with(LLGeneralConsumeQuery::setIpAddr,payInfoDto.getIpaddr())
                            .with(LLGeneralConsumeQuery::setReturnUrl,payInfoDto.getLianlianPayEnum().getReturnUrl())
                            .with(LLGeneralConsumeQuery::setPayeeUserId, payInfoDto.getReceiveUserId())
                            .build(), payInfoDto.isCanTrade());
            return Builder.of(PayVo::new).with(PayVo::setBody, body).with(PayVo::setPayType, payInfoDto.getPayType()).with(PayVo::setOutHost, payInfoDto.getOutHost()).build();
        }
        throw new IllegalArgumentException("not fount pay_type = " + payInfoDto.getPayType());
    }
}
