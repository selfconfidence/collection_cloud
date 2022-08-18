package com.manyun.business.controller;


import com.manyun.business.design.delay.DelayAbsAspect;
import com.manyun.business.design.delay.DelayQueue;
import com.manyun.business.domain.vo.CateVo;
import com.manyun.business.service.ICateService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 藏品系列_分类 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/cate")
@Api(tags = "系列相关Apis")
public class CateController extends BaseController {

    @Autowired
    private ICateService cateService;

    @Autowired
    private DelayQueue delayQueue;


    @GetMapping("/cateAll/{type}")
    @ApiOperation(value = "系列列表",notes = "无需分页,1=藏品系列，2=盲盒分类")
    //@ApiImplicitParam(name = "系列分类类型",value = "type",dataTypeClass = int.class,dataType = "int",required = true,paramType = "path")
    public R<List<CateVo>>  cateAll(@PathVariable Integer type){
        return R.ok(cateService.cateChildAll(type));
    }

    @GetMapping("/cateChildAll/{type}")
    @ApiOperation(value = "查询所有的二级分类",notes = "无需分页,1=藏品系列，2=盲盒分类\r version(1.0.1)")
    @Deprecated
    public R<List<CateVo>>  cateChildAll(@PathVariable Integer type){

        return R.ok(cateService.cateChildAll(type));
    }

    @GetMapping("/cateTopLevel/{type}")
    @ApiOperation(value = "(品牌馆)系列顶级分类列表",notes = "无需分页,1=藏品系列，2=盲盒分类\r version(1.0.1)")
    //@ApiImplicitParam(name = "系列分类类型",value = "type",dataTypeClass = int.class,dataType = "int",required = true,paramType = "path")
    public R<List<CateVo>>  cateTopLevel(@PathVariable Integer type){
        return R.ok(cateService.cateTopLevel(type));
    }

    @GetMapping("/childCate/{parentId}/{type}")
    @ApiOperation(value = "查询(品牌馆下面的子分类)列表",notes = "无需分页,1=藏品系列，2=盲盒分类\n" +
            " parentId是 夫节点的ID,不是夫节点的parentId \n  version(1.0.1)")
    @Deprecated
    public R<List<CateVo>> childCate(@PathVariable String parentId,@PathVariable Integer type){
        return R.ok(cateService.childCate(parentId,type));
    }



    @GetMapping("/msg/{id}/{time}/{name}")
    public R msg(@PathVariable String id,@PathVariable Long time,@PathVariable String name){
        // 分钟为单位
        long l = TimeUnit.MINUTES.toSeconds(time);

        // 转化秒
        delayQueue.put(id,l, new DelayAbsAspect<String>() {
            @Override
            public void invocationSuccess(String s) {
                logger.info(Thread.currentThread().getName());
                logger.info("回调成功！！！{}:{}",s,name);
            }

            @Override
            public void invocationFail(String s) {
                logger.info(Thread.currentThread().getName());
                logger.info("回调失败！！！{}:{}",s,name);
            }
        });

        return R.ok();
    }
}

