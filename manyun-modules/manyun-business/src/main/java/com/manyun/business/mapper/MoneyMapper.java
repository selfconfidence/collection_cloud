package com.manyun.business.mapper;

import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.Money;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface MoneyMapper extends BaseMapper<Money> {

    /**
     * 杉德支付所需参数
     * @param userId
     * @return
     */
    UserMoneyDto userMoneyInfo(String userId);

    int updateLock(String userId, BigDecimal withdrawAmount);

}
