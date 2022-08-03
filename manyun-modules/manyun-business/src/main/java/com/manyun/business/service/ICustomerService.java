package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CustomerService;
import com.manyun.business.domain.form.FeedbackForm;
import com.manyun.business.domain.form.OpinionForm;
import com.manyun.business.domain.vo.*;
import com.manyun.comm.api.model.LoginBusinessUser;

import java.util.List;

/**
 * <p>
 * 客服中心;客服中心主体表 服务类
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
public interface ICustomerService extends IService<CustomerService> {

    List<CustomerServiceVo> info();

    int saveOpinion(OpinionForm opinionForm, LoginBusinessUser loginBusinessUser);

    int saveFeedback(FeedbackForm feedbackForm, LoginBusinessUser loginBusinessUser);

    ArticleVo articleDetails(Integer id);

}
