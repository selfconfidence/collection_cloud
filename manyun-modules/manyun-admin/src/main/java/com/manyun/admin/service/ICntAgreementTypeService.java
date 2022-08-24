package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAgreementType;
import com.manyun.admin.domain.query.AgreementTypeQuery;
import com.manyun.admin.domain.vo.CntAgreementTypeVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 协议类型Service接口
 *
 * @author yanwei
 * @date 2022-08-23
 */
public interface ICntAgreementTypeService extends IService<CntAgreementType>
{
    /**
     * 查询协议类型详情
     *
     * @param id 协议类型主键
     * @return 协议类型
     */
    public CntAgreementType selectCntAgreementTypeById(String id);

    /**
     * 查询协议类型列表
     *
     * @param agreementTypeQuery 协议类型
     * @return 协议类型集合
     */
    public TableDataInfo<CntAgreementTypeVo> selectCntAgreementTypeList(AgreementTypeQuery agreementTypeQuery);

    /**
     * 新增协议类型
     *
     * @param cntAgreementType 协议类型
     * @return 结果
     */
    public int insertCntAgreementType(CntAgreementType cntAgreementType);

    /**
     * 修改协议类型
     *
     * @param cntAgreementType 协议类型
     * @return 结果
     */
    public int updateCntAgreementType(CntAgreementType cntAgreementType);

    /**
     * 批量删除协议类型
     *
     * @param ids 需要删除的协议类型主键集合
     * @return 结果
     */
    public int deleteCntAgreementTypeByIds(String[] ids);

    /**
     * 删除协议类型信息
     *
     * @param id 协议类型主键
     * @return 结果
     */
    public int deleteCntAgreementTypeById(String id);
}
