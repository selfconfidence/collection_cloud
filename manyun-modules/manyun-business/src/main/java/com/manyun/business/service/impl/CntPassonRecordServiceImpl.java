package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.CntPassonRecord;
import com.manyun.business.mapper.CntPassonRecordMapper;
import com.manyun.business.service.ICntPassonRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 转赠记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-18
 */
@Service
public class CntPassonRecordServiceImpl extends ServiceImpl<CntPassonRecordMapper, CntPassonRecord> implements ICntPassonRecordService
{
    @Autowired
    private CntPassonRecordMapper cntPassonRecordMapper;

}
