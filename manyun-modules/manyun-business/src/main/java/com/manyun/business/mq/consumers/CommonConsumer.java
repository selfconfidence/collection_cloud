package com.manyun.business.mq.consumers;

/*
 * 组件式功能拼接
 *
 * @author yanwei
 * @create 2022-10-11
 */
import com.manyun.business.domain.entity.ReSet;
import com.manyun.business.mapper.ResetMapper;
import com.manyun.business.mq.consumers.function.ExecFailFunction;
import com.manyun.business.mq.consumers.function.ExecSuccessFunction;
import com.manyun.common.core.domain.Builder;

import javax.annotation.Resource;

public abstract class CommonConsumer {

    @Resource
    private ResetMapper resetMapper;


    protected void uniCheck(String uniHost, ExecSuccessFunction successFunction, ExecFailFunction failFunction){
        try {
            resetMapper.insert(Builder.of(ReSet::new).with(ReSet::setResetId,uniHost).build());
            successFunction.success();
        }catch (Throwable e){
           failFunction.fail(e);
        }
    }



}
