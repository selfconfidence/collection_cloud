package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntLable;

/**
 * 藏品标签Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntLableMapper 
{
    /**
     * 查询藏品标签
     * 
     * @param id 藏品标签主键
     * @return 藏品标签
     */
    public CntLable selectCntLableById(String id);

    /**
     * 查询藏品标签列表
     * 
     * @param cntLable 藏品标签
     * @return 藏品标签集合
     */
    public List<CntLable> selectCntLableList(CntLable cntLable);

    /**
     * 新增藏品标签
     * 
     * @param cntLable 藏品标签
     * @return 结果
     */
    public int insertCntLable(CntLable cntLable);

    /**
     * 修改藏品标签
     * 
     * @param cntLable 藏品标签
     * @return 结果
     */
    public int updateCntLable(CntLable cntLable);

    /**
     * 删除藏品标签
     * 
     * @param id 藏品标签主键
     * @return 结果
     */
    public int deleteCntLableById(String id);

    /**
     * 批量删除藏品标签
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntLableByIds(String[] ids);
}
