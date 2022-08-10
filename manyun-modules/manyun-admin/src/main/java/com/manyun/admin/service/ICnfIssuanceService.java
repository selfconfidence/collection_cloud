package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CnfIssuance;
import com.manyun.admin.domain.query.IssuanceQuery;
import com.manyun.admin.domain.vo.CnfIssuanceVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 发行方Service接口
 *
 * @author yanwei
 * @date 2022-08-09
 */
public interface ICnfIssuanceService extends IService<CnfIssuance>
{
    /**
     * 查询发行方详情
     *
     * @param id 发行方主键
     * @return 发行方
     */
    public CnfIssuance selectCnfIssuanceById(String id);

    /**
     * 查询发行方列表
     *
     * @param issuanceQuery 发行方
     * @return 发行方集合
     */
    public TableDataInfo<CnfIssuanceVo> selectCnfIssuanceList(IssuanceQuery issuanceQuery);

    /**
     * 新增发行方
     *
     * @param cnfIssuance 发行方
     * @return 结果
     */
    public int insertCnfIssuance(CnfIssuance cnfIssuance);

    /**
     * 修改发行方
     *
     * @param cnfIssuance 发行方
     * @return 结果
     */
    public int updateCnfIssuance(CnfIssuance cnfIssuance);

    /**
     * 批量删除发行方
     *
     * @param ids 需要删除的发行方主键集合
     * @return 结果
     */
    public int deleteCnfIssuanceByIds(String[] ids);

}
