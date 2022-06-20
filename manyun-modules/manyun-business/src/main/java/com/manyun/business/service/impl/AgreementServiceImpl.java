package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.Agreement;
import com.manyun.business.mapper.AgreementMapper;
import com.manyun.business.service.IAgreementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议相关 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, Agreement> implements IAgreementService {

}
