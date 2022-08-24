package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAgreementType;
import com.manyun.admin.domain.query.AgreementTypeQuery;

/**
 * 协议类型Mapper接口
 *
 * @author yanwei
 * @date 2022-08-23
 */
public interface CntAgreementTypeMapper extends BaseMapper<CntAgreementType>
{
    /**
     * 查询协议类型列表
     *
     * @param agreementTypeQuery 协议类型
     * @return 协议类型集合
     */
    public List<CntAgreementType> selectCntAgreementTypeList(AgreementTypeQuery agreementTypeQuery);
}
