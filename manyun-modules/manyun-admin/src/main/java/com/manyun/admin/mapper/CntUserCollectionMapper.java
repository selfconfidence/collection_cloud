package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntUserCollection;

/**
 * 用户购买藏品中间Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-22
 */
public interface CntUserCollectionMapper 
{
    /**
     * 查询用户购买藏品中间
     * 
     * @param id 用户购买藏品中间主键
     * @return 用户购买藏品中间
     */
    public CntUserCollection selectCntUserCollectionById(String id);

    /**
     * 查询用户购买藏品中间列表
     * 
     * @param cntUserCollection 用户购买藏品中间
     * @return 用户购买藏品中间集合
     */
    public List<CntUserCollection> selectCntUserCollectionList(CntUserCollection cntUserCollection);

    /**
     * 新增用户购买藏品中间
     * 
     * @param cntUserCollection 用户购买藏品中间
     * @return 结果
     */
    public int insertCntUserCollection(CntUserCollection cntUserCollection);

    /**
     * 修改用户购买藏品中间
     * 
     * @param cntUserCollection 用户购买藏品中间
     * @return 结果
     */
    public int updateCntUserCollection(CntUserCollection cntUserCollection);

    /**
     * 删除用户购买藏品中间
     * 
     * @param id 用户购买藏品中间主键
     * @return 结果
     */
    public int deleteCntUserCollectionById(String id);

    /**
     * 批量删除用户购买藏品中间
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntUserCollectionByIds(String[] ids);
}
