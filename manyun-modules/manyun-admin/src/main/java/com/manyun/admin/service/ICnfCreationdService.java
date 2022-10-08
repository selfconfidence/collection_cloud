package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CnfCreationd;
import com.manyun.admin.domain.query.CreationdQuery;
import com.manyun.admin.domain.vo.CnfCreationdVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 创作者Service接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface ICnfCreationdService extends IService<CnfCreationd>
{
    /**
     * 查询创作者
     *
     * @param id 创作者主键
     * @return 创作者
     */
    public CnfCreationd selectCnfCreationdById(String id);

    /**
     * 查询创作者列表
     *
     * @param creationdQuery
     * @return 创作者集合
     */
    public TableDataInfo<CnfCreationdVo> selectCnfCreationdList(CreationdQuery creationdQuery);

    /**
     * 新增创作者
     *
     * @param cnfCreationd 创作者
     * @return 结果
     */
    public int insertCnfCreationd(CnfCreationd cnfCreationd);

    /**
     * 修改创作者
     *
     * @param cnfCreationd 创作者
     * @return 结果
     */
    public int updateCnfCreationd(CnfCreationd cnfCreationd);

    /**
     * 批量删除创作者
     *
     * @param ids 需要删除的创作者主键集合
     * @return 结果
     */
    public int deleteCnfCreationdByIds(String[] ids);

    /**
     * 删除创作者信息
     *
     * @param id 创作者主键
     * @return 结果
     */
    public int deleteCnfCreationdById(String id);
}
