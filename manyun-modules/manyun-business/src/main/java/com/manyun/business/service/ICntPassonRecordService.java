package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntPassonRecord;
import com.manyun.business.domain.vo.PassonRecordVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 转赠记录Service接口
 *
 * @author yanwei
 * @date 2022-08-18
 */
public interface ICntPassonRecordService extends IService<CntPassonRecord>
{
    TableDataInfo<PassonRecordVo> passonList(String userId, PageQuery pageQuery);
}
