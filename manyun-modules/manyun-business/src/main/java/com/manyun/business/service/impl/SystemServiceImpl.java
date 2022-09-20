package com.manyun.business.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.System;
import com.manyun.business.mapper.SystemMapper;
import com.manyun.business.service.ISystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.annotation.Lock;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.CONSIGNMENT_INFO;

/**
 * <p>
 * 平台规则表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System> implements ISystemService {

    @Override
    public <T> T getVal(String type, Class<T> classType) {
        System system = getOne(Wrappers.<System>lambdaQuery().eq(System::getSystemType, type));
        Assert.isTrue(Objects.nonNull(system),"not font system  type = " + type);
        String systemVal = system.getSystemVal();
        return (T)Convert.convert(classType,systemVal);
    }


    @Override
    @Lock("testRedisson")
    public String testRedisson() throws InterruptedException {
        Thread.sleep(3000);
      return getVal(CONSIGNMENT_INFO, String.class);
    }
}
