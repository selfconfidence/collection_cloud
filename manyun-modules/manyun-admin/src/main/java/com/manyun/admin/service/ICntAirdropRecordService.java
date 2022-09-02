package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAirdropRecord;
import com.manyun.admin.domain.query.AirdropRecordQuery;
import com.manyun.admin.domain.vo.CntAirdropRecordVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 空投记录Service接口
 *
 * @author yanwei
 * @date 2022-09-02
 */
public interface ICntAirdropRecordService extends IService<CntAirdropRecord>
{

    /**
     * 查询空投记录列表
     *
     * @param airdropRecordQuery 空投记录
     * @return 空投记录集合
     */
    public TableDataInfo<CntAirdropRecordVo> selectCntAirdropRecordList(AirdropRecordQuery airdropRecordQuery);

}
