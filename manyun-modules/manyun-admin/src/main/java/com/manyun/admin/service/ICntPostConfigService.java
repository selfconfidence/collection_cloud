package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostConfig;
import com.manyun.admin.domain.query.PostConfigQuery;
import com.manyun.admin.domain.vo.CntPostConfigVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置-只能有一条Service接口
 *
 * @author yanwei
 * @date 2022-08-15
 */
public interface ICntPostConfigService extends IService<CntPostConfig>
{
    /**
     * 查询提前购配置-只能有一条详情
     *
     * @param id 提前购配置-只能有一条主键
     * @return 提前购配置-只能有一条
     */
    public CntPostConfig selectCntPostConfigById(String id);

    /**
     * 查询提前购配置-只能有一条列表
     *
     * @param postConfigQuery
     * @return 提前购配置-只能有一条集合
     */
    public TableDataInfo<CntPostConfigVo> selectCntPostConfigList(PostConfigQuery postConfigQuery);

    /**
     * 新增提前购配置-只能有一条
     *
     * @param cntPostConfig 提前购配置-只能有一条
     * @return 结果
     */
    public int insertCntPostConfig(CntPostConfig cntPostConfig);

    /**
     * 修改提前购配置-只能有一条
     *
     * @param cntPostConfig 提前购配置-只能有一条
     * @return 结果
     */
    public int updateCntPostConfig(CntPostConfig cntPostConfig);

    /**
     * 批量删除提前购配置-只能有一条
     *
     * @param ids 需要删除的提前购配置-只能有一条主键集合
     * @return 结果
     */
    public int deleteCntPostConfigByIds(String[] ids);

}
