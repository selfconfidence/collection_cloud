package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntSystem;
import com.manyun.admin.domain.query.SystemQuery;
import com.manyun.admin.domain.vo.CntSystemVo;

/**
 * 平台规则Service接口
 *
 * @author yanwei
 * @date 2022-07-28
 */
public interface ICntSystemService extends IService<CntSystem>
{
    /**
     * 查询平台规则详情
     *
     * @param id 平台规则主键
     * @return 平台规则
     */
    public CntSystemVo selectCntSystemById(String id);

    /**
     * 查询平台规则列表
     *
     * @param SystemQuery
     * @return 平台规则集合
     */
    public List<CntSystemVo> selectCntSystemList(SystemQuery SystemQuery);

    /**
     * 修改平台规则
     *
     * @param cntSystemVo
     * @return 结果
     */
    public int updateCntSystem(CntSystemVo cntSystemVo);

}
