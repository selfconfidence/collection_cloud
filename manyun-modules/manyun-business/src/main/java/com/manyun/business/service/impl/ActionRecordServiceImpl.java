package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.ActionRecord;
import com.manyun.business.mapper.ActionRecordMapper;
import com.manyun.business.service.IActionRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 活动合成记录表 服务实现类
 * </p>
 *
 * @author
 * @since 2022-06-29
 */
@Service
public class ActionRecordServiceImpl extends ServiceImpl<ActionRecordMapper, ActionRecord> implements IActionRecordService {

    @Resource
    private ActionRecordMapper actionRecordMapper;


}
