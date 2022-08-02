package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntMoneyMapper;
import com.manyun.admin.domain.CntMoney;
import com.manyun.admin.service.ICntMoneyService;

/**
 * 钱包Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-30
 */
@Service
public class CntMoneyServiceImpl extends ServiceImpl<CntMoneyMapper,CntMoney> implements ICntMoneyService
{
    @Autowired
    private CntMoneyMapper cntMoneyMapper;

}
