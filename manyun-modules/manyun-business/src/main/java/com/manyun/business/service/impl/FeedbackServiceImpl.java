package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.Feedback;
import com.manyun.business.mapper.FeedbackMapper;
import com.manyun.business.service.IFeedbackService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 反馈举报;反馈举报表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

    @Resource
    private FeedbackMapper feedbackMapper;

}
