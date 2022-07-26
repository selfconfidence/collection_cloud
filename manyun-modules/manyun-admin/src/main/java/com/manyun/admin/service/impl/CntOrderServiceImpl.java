package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntOrderMapper;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.service.ICntOrderService;

/**
 * 订单Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-26
 */
@Service
public class CntOrderServiceImpl extends ServiceImpl<CntOrderMapper,CntOrder> implements ICntOrderService
{
    @Autowired
    private CntOrderMapper cntOrderMapper;

}
