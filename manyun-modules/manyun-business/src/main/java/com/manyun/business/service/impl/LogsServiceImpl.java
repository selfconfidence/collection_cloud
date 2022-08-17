package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.domain.vo.BoxLogPageVo;
import com.manyun.business.domain.vo.CollectionLogPageVo;
import com.manyun.business.mapper.LogsMapper;
import com.manyun.business.service.ILogsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;

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

    @Override
    public TableDataInfo<BoxLogPageVo> logsBoxPage(PageQuery pageQuery, String userId) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<Logs> logsList = list(Wrappers.<Logs>lambdaQuery().eq(Logs::getBuiId, userId).eq(Logs::getModelType, BOX_MODEL_TYPE).orderByDesc(Logs::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(logsList.parallelStream().map(this::initLogsBoxVo).collect(Collectors.toList()), logsList);
    }

    @Override
    public TableDataInfo<CollectionLogPageVo> logsCollectionPage(PageQuery pageQuery, String userId) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<Logs> logsList = list(Wrappers.<Logs>lambdaQuery().eq(Logs::getBuiId, userId).eq(Logs::getModelType, COLLECTION_MODEL_TYPE).orderByDesc(Logs::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(logsList.parallelStream().map(this::initLogsCollectionVo).collect(Collectors.toList()), logsList);
    }

    private BoxLogPageVo initLogsBoxVo(Logs logs) {
        BoxLogPageVo boxPageVo = Builder.of(BoxLogPageVo::new).build();
        BeanUtil.copyProperties(logs, boxPageVo);
        return boxPageVo;
    }

    private CollectionLogPageVo initLogsCollectionVo(Logs logs) {
        CollectionLogPageVo collectionLogPageVo = Builder.of(CollectionLogPageVo::new).build();
        BeanUtil.copyProperties(logs, collectionLogPageVo);
        return collectionLogPageVo;
    }

    private Logs initLogs(LogInfoDto logInfoDto){
        Logs logs = Builder.of(Logs::new).build();
        BeanUtil.copyProperties(logInfoDto,logs);
        logs.setId(IdUtil.getSnowflake().nextIdStr());
        logs.createD(logInfoDto.getBuiId());
        return logs;
    }
}
