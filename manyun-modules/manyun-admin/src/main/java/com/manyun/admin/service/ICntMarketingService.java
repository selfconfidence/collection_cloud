package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntMarketing;

/**
 * 营销配置Service接口
 * 
 * @author yanwei
 * @date 2022-07-19
 */
public interface ICntMarketingService 
{
    /**
     * 查询营销配置
     * 
     * @param id 营销配置主键
     * @return 营销配置
     */
    public CntMarketing selectCntMarketingById(String id);

    /**
     * 查询营销配置列表
     * 
     * @param cntMarketing 营销配置
     * @return 营销配置集合
     */
    public List<CntMarketing> selectCntMarketingList(CntMarketing cntMarketing);

    /**
     * 新增营销配置
     * 
     * @param cntMarketing 营销配置
     * @return 结果
     */
    public int insertCntMarketing(CntMarketing cntMarketing);

    /**
     * 修改营销配置
     * 
     * @param cntMarketing 营销配置
     * @return 结果
     */
    public int updateCntMarketing(CntMarketing cntMarketing);

    /**
     * 批量删除营销配置
     * 
     * @param ids 需要删除的营销配置主键集合
     * @return 结果
     */
    public int deleteCntMarketingByIds(String[] ids);

    /**
     * 删除营销配置信息
     * 
     * @param id 营销配置主键
     * @return 结果
     */
    public int deleteCntMarketingById(String id);
}
