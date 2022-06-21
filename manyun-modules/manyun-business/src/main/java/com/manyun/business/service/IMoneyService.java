package com.manyun.business.service;

import com.manyun.business.domain.entity.Money;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IMoneyService extends IService<Money> {

    void ordePay(String outHost,String userId, BigDecimal realPayMoney, String formInfo);
}
