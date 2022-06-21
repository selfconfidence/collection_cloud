package com.manyun.business.service;

import com.manyun.business.domain.entity.System;
import com.baomidou.mybatisplus.extension.service.IService;
/**
 * <p>
 * 平台规则表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface ISystemService extends IService<System> {


     <T> T  getVal(String type, Class<T> classType);

}
