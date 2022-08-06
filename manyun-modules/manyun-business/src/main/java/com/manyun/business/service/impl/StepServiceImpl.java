package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.manyun.business.domain.dto.StepDto;
import com.manyun.business.domain.entity.Step;
import com.manyun.business.mapper.StepMapper;
import com.manyun.business.service.IStepService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 * 藏品盲盒流转信息 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class StepServiceImpl extends ServiceImpl<StepMapper, Step> implements IStepService {

    @Override
    public void saveBatch(StepDto...stepDtos){
        Assert.isTrue(stepDtos.length >=1 ,"NOT ELEMENT => []");
        ArrayList<Step> steps = Lists.newArrayList();
        for (StepDto stepDto : stepDtos) {
            Step step = Builder.of(Step::new).build();
            BeanUtil.copyProperties(stepDto,step);
            step.setId(IdUtil.getSnowflakeNextIdStr());
            step.createD(stepDto.getUserId());
            steps.add(step);
        }
        saveBatch(steps);
    }

}
