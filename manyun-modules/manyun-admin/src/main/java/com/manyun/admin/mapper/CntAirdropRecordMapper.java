package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAirdropRecord;
import com.manyun.admin.domain.query.AirdropRecordQuery;
import com.manyun.admin.domain.vo.CntAirdropRecordVo;

/**
 * 空投记录Mapper接口
 *
 * @author yanwei
 * @date 2022-09-02
 */
public interface CntAirdropRecordMapper extends BaseMapper<CntAirdropRecord>
{
    /**
     * 查询空投记录列表
     *
     * @param airdropRecordQuery 空投记录
     * @return 空投记录集合
     */
    public List<CntAirdropRecord> selectCntAirdropRecordList(AirdropRecordQuery airdropRecordQuery);
}
