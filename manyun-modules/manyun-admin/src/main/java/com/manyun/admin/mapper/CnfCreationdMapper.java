package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CnfCreationd;

/**
 * 创作者Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-13
 */
public interface CnfCreationdMapper 
{
    /**
     * 查询创作者
     * 
     * @param id 创作者主键
     * @return 创作者
     */
    public CnfCreationd selectCnfCreationdById(String id);

    /**
     * 查询创作者列表
     * 
     * @param cnfCreationd 创作者
     * @return 创作者集合
     */
    public List<CnfCreationd> selectCnfCreationdList(CnfCreationd cnfCreationd);

    /**
     * 新增创作者
     * 
     * @param cnfCreationd 创作者
     * @return 结果
     */
    public int insertCnfCreationd(CnfCreationd cnfCreationd);

    /**
     * 修改创作者
     * 
     * @param cnfCreationd 创作者
     * @return 结果
     */
    public int updateCnfCreationd(CnfCreationd cnfCreationd);

    /**
     * 删除创作者
     * 
     * @param id 创作者主键
     * @return 结果
     */
    public int deleteCnfCreationdById(String id);

    /**
     * 批量删除创作者
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCnfCreationdByIds(String[] ids);
}
