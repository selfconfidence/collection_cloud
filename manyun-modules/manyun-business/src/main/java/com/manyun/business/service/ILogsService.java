package com.manyun.business.service;

import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.BoxLogPageVo;
import com.manyun.business.domain.vo.CollectionLogPageVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 业务收支日志记录表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface ILogsService extends IService<Logs> {

    void saveLogs(LogInfoDto ...logInfoDtos);


    TableDataInfo<BoxLogPageVo> logsBoxPage(PageQuery pageQuery, String userId);

    TableDataInfo<CollectionLogPageVo> logsCollectionPage(PageQuery pageQuery, String userId);
}
