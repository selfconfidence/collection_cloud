package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntUserTarLog;
import com.manyun.admin.domain.query.UserTarLogQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 抽签记录日志Service接口
 *
 * @author yanwei
 * @date 2022-10-09
 */
public interface ICntUserTarLogService extends IService<CntUserTarLog>
{

    /**
     * 查询抽签记录日志列表
     *
     * @param userTarLogQuery 抽签记录日志
     * @return 抽签记录日志集合
     */
    public TableDataInfo selectCntUserTarLogList(UserTarLogQuery userTarLogQuery);

}
