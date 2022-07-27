package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntOpinion;
import com.manyun.admin.domain.vo.CntOpinionVo;

/**
 * 产品建议Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntOpinionService extends IService<CntOpinion>
{

    /**
     * 查询产品建议列表
     *
     * @param cntOpinion 产品建议
     * @return 产品建议集合
     */
    public List<CntOpinionVo> selectCntOpinionList(CntOpinion cntOpinion);

    /**
     * 修改产品建议
     *
     * @param cntOpinion 产品建议
     * @return 结果
     */
    public int updateCntOpinion(CntOpinion cntOpinion);

}
