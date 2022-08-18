package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPassonRecord;
import com.manyun.admin.domain.query.PassonRecordQuery;

/**
 * 转赠记录Mapper接口
 *
 * @author yanwei
 * @date 2022-08-18
 */
public interface CntPassonRecordMapper extends BaseMapper<CntPassonRecord>
{
    /**
     * 查询转赠记录列表
     *
     * @param passonRecordQuery 转赠记录
     * @return 转赠记录集合
     */
    public List<CntPassonRecord> selectCntPassonRecordList(PassonRecordQuery passonRecordQuery);
}
