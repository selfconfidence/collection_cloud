package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntFeedback;

/**
 * 产品举报反馈Mapper接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface CntFeedbackMapper extends BaseMapper<CntFeedback>
{
    /**
     * 查询产品举报反馈列表
     *
     * @param cntFeedback 产品举报反馈
     * @return 产品举报反馈集合
     */
    public List<CntFeedback> selectCntFeedbackList(CntFeedback cntFeedback);
}
