package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAgreement;
import com.manyun.admin.domain.vo.CntAgreementVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 协议相关Service接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface ICntAgreementService extends IService<CntAgreement>
{
    /**
     * 查询协议相关
     *
     * @param id 协议相关主键
     * @return 协议相关
     */
    public CntAgreement selectCntAgreementById(String id);

    /**
     * 查询协议相关列表
     *
     * @param pageQuery
     * @return 协议相关集合
     */
    public TableDataInfo<CntAgreementVo> selectCntAgreementList(PageQuery pageQuery);

    /**
     * 新增协议相关
     *
     * @param cntAgreement 协议相关
     * @return 结果
     */
    public int insertCntAgreement(CntAgreement cntAgreement);

    /**
     * 修改协议相关
     *
     * @param cntAgreement 协议相关
     * @return 结果
     */
    public int updateCntAgreement(CntAgreement cntAgreement);

    /**
     * 批量删除协议相关
     *
     * @param ids 需要删除的协议相关主键集合
     * @return 结果
     */
    public int deleteCntAgreementByIds(String[] ids);

    /**
     * 删除协议相关信息
     *
     * @param id 协议相关主键
     * @return 结果
     */
    public int deleteCntAgreementById(String id);

    /**
     * 根据类型查询协议详情
     * @param agreementType
     * @return
     */
    public CntAgreement getInfoByType(String agreementType);
}
