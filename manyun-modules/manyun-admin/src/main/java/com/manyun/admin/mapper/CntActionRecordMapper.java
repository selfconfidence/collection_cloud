package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntActionRecord;
import com.manyun.admin.domain.query.ActionRecordQuery;
import com.manyun.admin.domain.vo.CntActionRecordVo;

/**
 * 活动合成记录Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionRecordMapper extends BaseMapper<CntActionRecord>
{

    /**
     * 查询活动合成记录列表
     *
     * @param recordQuery
     * @return 活动合成记录集合
     */
    public List<CntActionRecordVo> selectCntActionRecordList(ActionRecordQuery recordQuery);

}
