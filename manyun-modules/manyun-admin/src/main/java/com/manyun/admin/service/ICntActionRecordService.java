package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntActionRecord;
import com.manyun.admin.domain.query.ActionRecordQuery;
import com.manyun.admin.domain.vo.CntActionRecordVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成记录Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionRecordService extends IService<CntActionRecord>
{

    /**
     * 查询活动合成记录列表
     *
     * @param recordQuery
     * @return 活动合成记录集合
     */
    public TableDataInfo<CntActionRecordVo> selectCntActionRecordList(ActionRecordQuery recordQuery);

}
