package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntOrder;

/**
 * 订单Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntOrderMapper 
{
    /**
     * 查询订单
     * 
     * @param id 订单主键
     * @return 订单
     */
    public CntOrder selectCntOrderById(String id);

    /**
     * 查询订单列表
     * 
     * @param cntOrder 订单
     * @return 订单集合
     */
    public List<CntOrder> selectCntOrderList(CntOrder cntOrder);

    /**
     * 新增订单
     * 
     * @param cntOrder 订单
     * @return 结果
     */
    public int insertCntOrder(CntOrder cntOrder);

    /**
     * 修改订单
     * 
     * @param cntOrder 订单
     * @return 结果
     */
    public int updateCntOrder(CntOrder cntOrder);

    /**
     * 删除订单
     * 
     * @param id 订单主键
     * @return 结果
     */
    public int deleteCntOrderById(String id);

    /**
     * 批量删除订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntOrderByIds(String[] ids);
}
