package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntCustomerService;
import org.apache.ibatis.annotations.Param;

/**
 * 客服Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntCustomerServiceMapper
{
    /**
     * 查询客服
     *
     * @param menuId 客服主键
     * @return 客服
     */
    public CntCustomerService selectCntCustomerServiceByMenuId(Long menuId);

    /**
     * 查询客服列表
     *
     * @param cntCustomerService 客服
     * @return 客服集合
     */
    public List<CntCustomerService> selectCntCustomerServiceList(CntCustomerService cntCustomerService);

    /**
     * 新增客服
     *
     * @param cntCustomerService 客服
     * @return 结果
     */
    public int insertCntCustomerService(CntCustomerService cntCustomerService);

    /**
     * 修改客服
     *
     * @param cntCustomerService 客服
     * @return 结果
     */
    public int updateCntCustomerService(CntCustomerService cntCustomerService);

    /**
     * 删除客服
     *
     * @param menuId 客服主键
     * @return 结果
     */
    public int deleteCntCustomerServiceByMenuId(Long menuId);

    /**
     * 批量删除客服
     *
     * @param menuIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntCustomerServiceByMenuIds(Long[] menuIds);

    /**
     * 查询父菜单ID集
     *
     * @param menuIds 客服主键集
     * @return 客服集合
     */
    List<Long> selectParentIdByMenuIds(@Param("menuIds") Long[] menuIds);
}
