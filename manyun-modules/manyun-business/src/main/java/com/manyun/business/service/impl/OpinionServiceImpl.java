package com.manyun.business.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.Opinion;
import com.manyun.business.mapper.OpinionMapper;
import com.manyun.business.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 建议;建议主体表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-06-28
 */
@Service
public class OpinionServiceImpl extends ServiceImpl<OpinionMapper, Opinion> implements IOpinionService {

    @Resource
    private OpinionMapper opinionMapper;

}
