package com.manyun.business.service;

import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
