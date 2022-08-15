package com.manyun.business.controller;

import com.manyun.business.domain.form.FeedbackForm;
import com.manyun.business.domain.form.OpinionForm;
import com.manyun.business.domain.vo.ArticleVo;
import com.manyun.business.domain.vo.CustomerServiceVo;
import com.manyun.business.service.ICustomerService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 客服中心;客服中心主体表 前端控制器
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@RestController
@RequestMapping("/customerService")
@Api(tags = "客服相关apis")
public class CustomerServiceController extends BaseController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping("/info")
    @ApiOperation("查询客服中心的详细信息")
    public R<List<CustomerServiceVo>> info(){
        return R.ok(customerService.info());
    }

    @GetMapping("/articleDetails/{id}")
    @ApiOperation("根据id查询客服中心问题文章")
    public R<ArticleVo> articleDetails(@PathVariable Integer id){
        return R.ok(customerService.articleDetails(id));
    }

    @PostMapping("/saveOpinion")
    @ApiOperation("保存产品建议信息")
    public R saveOpinion(@Valid @RequestBody OpinionForm opinionForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return customerService.saveOpinion(opinionForm,notNullLoginBusinessUser)==1?R.ok():R.fail();
    }

    @PostMapping("/saveFeedback")
    @ApiOperation("保存反馈举报信息")
    public R saveFeedback(@Valid @RequestBody FeedbackForm feedbackForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return customerService.saveFeedback(feedbackForm,notNullLoginBusinessUser)==1?R.ok():R.fail();
    }

}
