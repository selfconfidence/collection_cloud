package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntPostConfig;

/**
 * <p>
 * 提前购配置表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
public interface ICntPostConfigService extends IService<CntPostConfig> {

    boolean isConfigPostCustomer(String userId, String buiId);

    boolean isConfigPostBoxCustomer(String userId, String buiId);
}
