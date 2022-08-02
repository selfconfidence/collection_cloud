package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAction;
import com.manyun.admin.domain.query.ActionQuery;
import com.manyun.admin.domain.vo.CntActionVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionService extends IService<CntAction>
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
     * @param actionQuery 活动
     * @return 活动集合
     */
    public TableDataInfo<CntActionVo> selectCntActionList(ActionQuery actionQuery);

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
}
