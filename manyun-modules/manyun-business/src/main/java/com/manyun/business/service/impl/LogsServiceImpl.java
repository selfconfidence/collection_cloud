package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.mapper.LogsMapper;
import com.manyun.business.service.ILogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务收支日志记录表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@Service
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements ILogsService {


    /**
     * 批量新增日志表
     * @param logInfoDtos
     */
    @Override
    public void saveLogs(LogInfoDto... logInfoDtos) {
        Assert.isTrue(logInfoDtos.length >=1,"logInfoDtos length = 0 ?");
        saveBatch(Arrays.stream(logInfoDtos).parallel().map(this::initLogs).collect(Collectors.toList()));
    }
    private Logs initLogs(LogInfoDto logInfoDto){
        Logs logs = Builder.of(Logs::new).build();
        BeanUtil.copyProperties(logInfoDto,logs);
        logs.setId(IdUtil.getSnowflake().nextIdStr());
        logs.createD(logInfoDto.getBuiId());
        return logs;
    }
}
