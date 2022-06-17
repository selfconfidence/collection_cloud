package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.Action;
import com.manyun.business.mapper.CntActionMapper;
import com.manyun.business.service.IActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class ActionServiceImpl extends ServiceImpl<CntActionMapper, Action> implements IActionService {

}
