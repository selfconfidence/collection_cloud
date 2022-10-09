package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserTarLog;
import com.manyun.admin.domain.query.UserTarLogQuery;

/**
 * 抽签记录日志Mapper接口
 *
 * @author yanwei
 * @date 2022-10-09
 */
public interface CntUserTarLogMapper extends BaseMapper<CntUserTarLog>
{
    /**
     * 查询抽签记录日志列表
     *
     * @param userTarLogQuery 抽签记录日志
     * @return 抽签记录日志集合
     */
    public List<CntUserTarLog> selectCntUserTarLogList(UserTarLogQuery userTarLogQuery);
}
