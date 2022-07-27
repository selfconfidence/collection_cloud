package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntFeedback;
import com.manyun.admin.domain.vo.CntFeedbackVo;

/**
 * 产品举报反馈Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntFeedbackService extends IService<CntFeedback>
{

    /**
     * 查询产品举报反馈列表
     *
     * @param cntFeedback 产品举报反馈
     * @return 产品举报反馈集合
     */
    public List<CntFeedbackVo> selectCntFeedbackList(CntFeedback cntFeedback);

    /**
     * 修改产品举报反馈
     *
     * @param cntFeedback 产品举报反馈
     * @return 结果
     */
    public int updateCntFeedback(CntFeedback cntFeedback);

}
