package com.manyun.admin.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntConsignmentMapper;
import com.manyun.admin.service.ICntConsignmentService;

/**
 * 寄售市场主_寄售订单Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntConsignmentServiceImpl extends ServiceImpl<CntConsignmentMapper, CntConsignment> implements ICntConsignmentService
{
    @Autowired
    private CntConsignmentMapper cntConsignmentMapper;

    /**
     * 订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单
     */
    @Override
    public List<CntConsignmentVo> selectCntConsignmentList(ConsignmentQuery consignmentQuery)
    {
        return cntConsignmentMapper.selectOrderList(consignmentQuery);
    }

}
