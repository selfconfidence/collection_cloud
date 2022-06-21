package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.mapper.MoneyMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;

/**
 * <p>
 * 钱包表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class MoneyServiceImpl extends ServiceImpl<MoneyMapper, Money> implements IMoneyService {

    @Autowired
    private ILogsService logsService;



    /**
     * 根据进行进行扣除余额操作
     * @param userId
     * @param realPayMoney
     * @param formInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ordePay(String outHost,String userId, BigDecimal realPayMoney, String formInfo) {
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney)>=0,"余额不足,请核实!");
        money.setMoneyBalance(NumberUtil.sub(money.getMoneyBalance(),realPayMoney));
        money.updateD(userId);
        updateById(money);
        logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo(realPayMoney.toString()).isType(POLL_SOURCE).modelType(MONEY_TYPE).build());

    }
}
