package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.UserTarLogQuery;
import com.manyun.admin.domain.vo.CntTarVo;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntUserTarLogMapper;
import com.manyun.admin.domain.CntUserTarLog;
import com.manyun.admin.service.ICntUserTarLogService;

/**
 * 抽签记录日志Service业务层处理
 *
 * @author yanwei
 * @date 2022-10-09
 */
@Service
public class CntUserTarLogServiceImpl extends ServiceImpl<CntUserTarLogMapper,CntUserTarLog> implements ICntUserTarLogService
{
    @Autowired
    private CntUserTarLogMapper cntUserTarLogMapper;

    /**
     * 查询抽签记录日志列表
     *
     * @param userTarLogQuery 抽签记录日志
     * @return 抽签记录日志
     */
    @Override
    public TableDataInfo<CntUserTarLog> selectCntUserTarLogList(UserTarLogQuery userTarLogQuery)
    {
        PageHelper.startPage(userTarLogQuery.getPageNum(),userTarLogQuery.getPageSize());
        List<CntUserTarLog> cntUserTarLogs = cntUserTarLogMapper.selectCntUserTarLogList(userTarLogQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntUserTarLogs,cntUserTarLogs);
    }

}
