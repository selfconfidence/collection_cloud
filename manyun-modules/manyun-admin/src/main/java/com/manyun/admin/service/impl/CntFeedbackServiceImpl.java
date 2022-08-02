package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.vo.CntFeedbackVo;
import com.manyun.common.core.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntFeedbackMapper;
import com.manyun.admin.domain.CntFeedback;
import com.manyun.admin.service.ICntFeedbackService;

/**
 * 产品举报反馈Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-26
 */
@Service
public class CntFeedbackServiceImpl extends ServiceImpl<CntFeedbackMapper,CntFeedback> implements ICntFeedbackService
{
    @Autowired
    private CntFeedbackMapper cntFeedbackMapper;

    /**
     * 查询产品举报反馈列表
     *
     * @param pageQuery
     * @return 产品举报反馈
     */
    @Override
    public TableDataInfo<CntFeedbackVo> selectCntFeedbackList(PageQuery pageQuery)
    {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<CntFeedback> cntFeedbacks = cntFeedbackMapper.selectCntFeedbackList(new CntFeedback());
        return TableDataInfoUtil.pageTableDataInfo(cntFeedbacks.parallelStream().map(m->{
            CntFeedbackVo cntFeedbackVo=new CntFeedbackVo();
            BeanUtil.copyProperties(m,cntFeedbackVo);
            return cntFeedbackVo;
        }).collect(Collectors.toList()),cntFeedbacks);
    }

    /**
     * 修改产品举报反馈
     *
     * @param cntFeedback 产品举报反馈
     * @return 结果
     */
    @Override
    public int updateCntFeedback(CntFeedback cntFeedback)
    {
        cntFeedback.setUpdatedBy(SecurityUtils.getUsername());
        cntFeedback.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntFeedback)==true?1:0;
    }

}
