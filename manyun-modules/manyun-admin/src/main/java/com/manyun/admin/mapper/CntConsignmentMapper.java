package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;

/**
 * 寄售市场主_寄售订单Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntConsignmentMapper extends BaseMapper<CntConsignment>
{

    /***
     * 查询订单管理列表
     * @param consignmentQuery
     * @return
     */
    List<CntConsignmentVo> selectOrderList(ConsignmentQuery consignmentQuery);

}
