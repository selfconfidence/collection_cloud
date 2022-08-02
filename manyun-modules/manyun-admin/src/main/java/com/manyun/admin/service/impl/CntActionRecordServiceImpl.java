package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.ActionRecordQuery;
import com.manyun.admin.domain.vo.CntActionRecordVo;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntActionRecordMapper;
import com.manyun.admin.domain.CntActionRecord;
import com.manyun.admin.service.ICntActionRecordService;

/**
 * 活动合成记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-21
 */
@Service
public class CntActionRecordServiceImpl extends ServiceImpl<CntActionRecordMapper,CntActionRecord> implements ICntActionRecordService
{
    @Autowired
    private CntActionRecordMapper cntActionRecordMapper;

    /**
     * 查询活动合成记录列表
     *
     * @param recordQuery
     * @return 活动合成记录
     */
    @Override
    public TableDataInfo<CntActionRecordVo> selectCntActionRecordList(ActionRecordQuery recordQuery)
    {
        PageHelper.startPage(recordQuery.getPageNum(),recordQuery.getPageSize());
        List<CntActionRecordVo> cntActionRecordVos = cntActionRecordMapper.selectCntActionRecordList(recordQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntActionRecordVos,cntActionRecordVos);
    }

}
