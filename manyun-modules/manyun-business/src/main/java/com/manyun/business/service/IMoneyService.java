package com.manyun.business.service;

import com.manyun.business.domain.entity.Money;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.common.core.web.page.TableDataInfo;

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

    BigDecimal ordePay(String outHost,String userId, BigDecimal realPayMoney, String formInfo);

    AccountInfoVo accountInfo(String userId);

    void updateAccountInfo(String userId, AccountInfoForm accountInfoForm);

    TableDataInfo<MoneyLogVo> pageMoneyLog(String userId, MoneyLogQuery moneyLogQuery);

}
