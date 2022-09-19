package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntSystemWithdraw;
import com.manyun.admin.domain.dto.UpdateWithDrawDto;
import com.manyun.admin.domain.query.SystemWithdrawQuery;
import com.manyun.admin.domain.vo.SystemWithdrawVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 系统余额提现Service接口
 *
 * @author yanwei
 * @date 2022-09-17
 */
public interface ICntSystemWithdrawService extends IService<CntSystemWithdraw>
{

    /**
     * 查询系统余额提现列表
     *
     * @param systemWithdrawQuery 系统余额提现
     * @return 系统余额提现集合
     */
    public TableDataInfo<SystemWithdrawVo> selectCntSystemWithdrawList(SystemWithdrawQuery systemWithdrawQuery);

    /**
     * 修改打垮状况
     * @param withDrawDto
     * @return
     */
    int updateWithdrawStatus(UpdateWithDrawDto withDrawDto);

    /**
     * 查询系统余额提现列表
     */
    List<SystemWithdrawVo> selectExportList();
}
