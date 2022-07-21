package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntAction;
import com.manyun.admin.domain.vo.CntActionVo;

/**
 * 活动Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionService
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
    public List<CntActionVo> selectCntActionList(CntAction cntAction);

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
     * 批量删除活动
     *
     * @param ids 需要删除的活动主键集合
     * @return 结果
     */
    public int deleteCntActionByIds(String[] ids);

    /**
     * 删除活动信息
     *
     * @param id 活动主键
     * @return 结果
     */
    public int deleteCntActionById(String id);
}
