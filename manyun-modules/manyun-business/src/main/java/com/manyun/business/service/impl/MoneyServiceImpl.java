package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.Money;
import com.manyun.business.mapper.MoneyMapper;
import com.manyun.business.service.IMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
