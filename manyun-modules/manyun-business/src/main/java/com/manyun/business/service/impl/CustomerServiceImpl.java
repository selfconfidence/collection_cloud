package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.CustomerService;
import com.manyun.business.domain.entity.Feedback;
import com.manyun.business.domain.entity.Opinion;
import com.manyun.business.domain.form.FeedbackForm;
import com.manyun.business.domain.form.OpinionForm;
import com.manyun.business.domain.vo.ArticleVo;
import com.manyun.business.domain.vo.CustomerServiceVo;
import com.manyun.business.mapper.CustomerServiceMapper;
import com.manyun.business.service.*;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 客服中心;客服中心主体表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerServiceMapper, CustomerService> implements ICustomerService {

    @Resource
    private CustomerServiceMapper customerServiceMapper;

    @Autowired
    private IOpinionService  opinionService;

    @Autowired
    private IFeedbackService  feedbackService;

    /**
     *  查询客服中心的详细信息
     * @return
     */
    @Override
    public List<CustomerServiceVo> info() {
        List<CustomerService> customerServiceList = list(
                Wrappers.
                        <CustomerService>lambdaQuery()
                        .eq(CustomerService::getMenuStatus,0)
                        .orderByAsc(CustomerService::getOrderNum));
        return getChildPerms(customerServiceList.parallelStream().map(this::customerServiceVo).collect(Collectors.toList()), 0);
    }

    /**
     *  根据id查询客服中心的问题文章
     * @return
     */
    @Override
    public ArticleVo articleDetails(Integer id) {
        ArticleVo articleVo = Builder.of(ArticleVo::new).build();
        CustomerService customerService = customerServiceMapper.selectOne(Wrappers.
                <CustomerService>lambdaQuery()
                .ne(CustomerService::getParentId,0)
                .eq(CustomerService::getMenuStatus,0)
                .eq(CustomerService::getId,id));
        BeanUtil.copyProperties(customerService,articleVo);
        return articleVo;
    }

    /**
     * 保存产品建议
     * @param opinionForm
     * @param loginBusinessUser
     * @return
     */
    @Override
    public int saveOpinion(OpinionForm opinionForm, LoginBusinessUser loginBusinessUser) {
        CntUserDto cntUser = loginBusinessUser.getCntUser();
        Opinion opinion = Builder
                .of(Opinion::new)
                .with(Opinion::setId, IdUtil.getSnowflake().nextIdStr())
                .with(Opinion::setOpinionUserId, cntUser.getUserId())
                .with(Opinion::setOpinionUserName,loginBusinessUser.getUsername())
                .with(Opinion::setOpinionUserPhone,cntUser.getPhone())
                .with(Opinion::setOpinionContent, opinionForm.getOpinionContent())
                .with(Opinion::setImg, opinionForm.getImg())
                .with(Opinion::setCreatedBy, cntUser.getUserId())
                .with(Opinion::setCreatedTime, LocalDateTime.now()).build();
        return opinionService.save(opinion)==true?1:0;
    }


    /**
     * 保存反馈举报信息
     * @param feedbackForm
     * @param loginBusinessUser
     * @return
     */
    @Override
    public int saveFeedback(FeedbackForm feedbackForm, LoginBusinessUser loginBusinessUser) {
        CntUserDto cntUser = loginBusinessUser.getCntUser();
        Feedback feedback = Builder
                .of(Feedback::new)
                .with(Feedback::setId, IdUtil.getSnowflake().nextIdStr())
                .with(Feedback::setFeedbackUserId, cntUser.getUserId())
                .with(Feedback::setFeedbackUserName,loginBusinessUser.getUsername())
                .with(Feedback::setFeedbackUserPhone,cntUser.getPhone())
                .with(Feedback::setLinkAddr,feedbackForm.getLinkAddr())
                .with(Feedback::setFeedbackType,feedbackForm.getFeedbackType())
                .with(Feedback::setFeedbackContent, feedbackForm.getOpinionContent())
                .with(Feedback::setFeedbackImg, feedbackForm.getFeedbackImg())
                .with(Feedback::setCreatedBy, cntUser.getUserId())
                .with(Feedback::setCreatedTime, LocalDateTime.now()).build();
        return feedbackService.save(feedback)==true?1:0;
    }

    private CustomerServiceVo customerServiceVo(CustomerService customerService) {
        CustomerServiceVo customerServiceVo = Builder.of(CustomerServiceVo::new).build();
        BeanUtil.copyProperties(customerService,customerServiceVo);
        customerServiceVo.setMenuId(customerService.getId());
        return customerServiceVo;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<CustomerServiceVo> getChildPerms(List<CustomerServiceVo> list, int parentId)
    {
        List<CustomerServiceVo> returnList = new ArrayList<CustomerServiceVo>();
        for (Iterator<CustomerServiceVo> iterator = list.iterator(); iterator.hasNext();)
        {
            CustomerServiceVo t = (CustomerServiceVo) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<CustomerServiceVo> list, CustomerServiceVo t)
    {
        // 得到子节点列表
        List<CustomerServiceVo> childList = getChildList(list, t);
        t.setChildren(childList);
        for (CustomerServiceVo tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<CustomerServiceVo> getChildList(List<CustomerServiceVo> list, CustomerServiceVo t)
    {
        List<CustomerServiceVo> tlist = new ArrayList<CustomerServiceVo>();
        Iterator<CustomerServiceVo> it = list.iterator();
        while (it.hasNext())
        {
            CustomerServiceVo n = (CustomerServiceVo) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<CustomerServiceVo> list, CustomerServiceVo t)
    {
        return getChildList(list, t).size() > 0;
    }

}
