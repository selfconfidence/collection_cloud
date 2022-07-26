package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntCustomerService;
import com.manyun.admin.domain.vo.CntCustomerServiceVo;
import com.manyun.common.core.domain.R;

/**
 * 客服Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntCustomerServiceService extends IService<CntCustomerService>
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
    public List<CntCustomerServiceVo> selectCntCustomerServiceList(CntCustomerService cntCustomerService);

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
     * 批量删除客服
     *
     * @param menuIds 需要删除的客服主键集合
     * @return 结果
     */
    public R deleteCntCustomerServiceByMenuIds(Long[] menuIds);

}
