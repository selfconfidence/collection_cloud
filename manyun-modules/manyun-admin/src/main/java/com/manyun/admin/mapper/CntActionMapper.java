package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntAction;

/**
 * 活动Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionMapper 
{
    /**
     * 查询活动
     * 
     * @param id 活动主键
     * @return 活动
     */
    public CntAction selectCntActionById(String id);

    /**
     * 查询活动列表
     * 
     * @param cntAction 活动
     * @return 活动集合
     */
    public List<CntAction> selectCntActionList(CntAction cntAction);

    /**
     * 新增活动
     * 
     * @param cntAction 活动
     * @return 结果
     */
    public int insertCntAction(CntAction cntAction);

    /**
     * 修改活动
     * 
     * @param cntAction 活动
     * @return 结果
     */
    public int updateCntAction(CntAction cntAction);

    /**
     * 删除活动
     * 
     * @param id 活动主键
     * @return 结果
     */
    public int deleteCntActionById(String id);

    /**
     * 批量删除活动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntActionByIds(String[] ids);
}
