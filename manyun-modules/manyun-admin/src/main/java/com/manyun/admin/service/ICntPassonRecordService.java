package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPassonRecord;
import com.manyun.admin.domain.query.PassonRecordQuery;
import com.manyun.admin.domain.vo.CntPassonRecordVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 转赠记录Service接口
 *
 * @author yanwei
 * @date 2022-08-18
 */
public interface ICntPassonRecordService extends IService<CntPassonRecord>
{

    /**
     * 查询转赠记录列表
     *
     * @param passonRecordQuery 转赠记录
     * @return 转赠记录集合
     */
    public TableDataInfo<CntPassonRecordVo> selectCntPassonRecordList(PassonRecordQuery passonRecordQuery);

}
