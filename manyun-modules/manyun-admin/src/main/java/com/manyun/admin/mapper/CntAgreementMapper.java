package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAgreement;
import com.manyun.admin.domain.vo.CntAgreementVo;

/**
 * 协议相关Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntAgreementMapper extends BaseMapper<CntAgreement>
{

    /**
     * 查询协议相关列表
     *
     * @param cntAgreement 协议相关
     * @return 协议相关集合
     */
    public List<CntAgreementVo> selectCntAgreementList(CntAgreement cntAgreement);

}
