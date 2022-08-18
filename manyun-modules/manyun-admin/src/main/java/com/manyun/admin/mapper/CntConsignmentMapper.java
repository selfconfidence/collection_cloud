package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
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
     * 查询藏品订单管理列表
     * @param consignmentQuery
     * @return
     */
    List<CntConsignmentVo> selectCollectionOrderList(ConsignmentQuery consignmentQuery);

    /***
     * 查询盲盒订单管理列表
     * @param consignmentQuery
     * @return
     */
    List<CntConsignmentVo> selectBoxOrderList(ConsignmentQuery consignmentQuery);

    /**
     * 获取藏品订单管理详细信息
     * @param consignmentInfoDto
     * @return
     */
    CntConsignmentVo selectConsignmentOrderById(ConsignmentInfoDto consignmentInfoDto);
}
