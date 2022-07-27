package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CntOpinionVo;
import com.manyun.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntOpinionMapper;
import com.manyun.admin.domain.CntOpinion;
import com.manyun.admin.service.ICntOpinionService;

/**
 * 产品建议Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-26
 */
@Service
public class CntOpinionServiceImpl extends ServiceImpl<CntOpinionMapper,CntOpinion> implements ICntOpinionService
{
    @Autowired
    private CntOpinionMapper cntOpinionMapper;

    /**
     * 查询产品建议列表
     *
     * @param cntOpinion 产品建议
     * @return 产品建议
     */
    @Override
    public List<CntOpinionVo> selectCntOpinionList(CntOpinion cntOpinion)
    {
        return cntOpinionMapper.selectCntOpinionList(cntOpinion).stream().map(m->{
            CntOpinionVo cntOpinionVo=new CntOpinionVo();
            BeanUtil.copyProperties(m,cntOpinionVo);
            return cntOpinionVo;
        }).collect(Collectors.toList());
    }

    /**
     * 修改产品建议
     *
     * @param cntOpinion 产品建议
     * @return 结果
     */
    @Override
    public int updateCntOpinion(CntOpinion cntOpinion)
    {
        cntOpinion.setUpdatedBy(SecurityUtils.getUsername());
        cntOpinion.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntOpinion)==true?1:0;
    }

}
