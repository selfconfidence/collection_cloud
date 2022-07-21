package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.query.QueryConsignmentVo;
import com.manyun.admin.domain.vo.CntConsignmentVo;

/**
 * 寄售市场主_寄售订单Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntConsignmentMapper
{
    /**
     * 查询寄售市场主_寄售订单
     *
     * @param id 寄售市场主_寄售订单主键
     * @return 寄售市场主_寄售订单
     */
    public CntConsignment selectCntConsignmentById(String id);

    /**
     * 查询寄售市场主_寄售订单列表
     *
     * @param cntConsignment 寄售市场主_寄售订单
     * @return 寄售市场主_寄售订单集合
     */
    public List<CntConsignment> selectCntConsignmentList(CntConsignment cntConsignment);

    /**
     * 新增寄售市场主_寄售订单
     *
     * @param cntConsignment 寄售市场主_寄售订单
     * @return 结果
     */
    public int insertCntConsignment(CntConsignment cntConsignment);

    /**
     * 修改寄售市场主_寄售订单
     *
     * @param cntConsignment 寄售市场主_寄售订单
     * @return 结果
     */
    public int updateCntConsignment(CntConsignment cntConsignment);

    /**
     * 删除寄售市场主_寄售订单
     *
     * @param id 寄售市场主_寄售订单主键
     * @return 结果
     */
    public int deleteCntConsignmentById(String id);

    /**
     * 批量删除寄售市场主_寄售订单
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntConsignmentByIds(String[] ids);

    /***
     * 查询订单管理列表
     * @param queryConsignmentVo
     * @return
     */
    List<CntConsignmentVo> selectOrderList(QueryConsignmentVo queryConsignmentVo);

}
