package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.domain.vo.CntPostExistVo;

import java.util.List;

/**
 * 提前购配置已经拥有Service接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface ICntPostExistService extends IService<CntPostExist>
{
    /**
     * 查询提前购配置已经拥有详情
     *
     * @param id 提前购配置已经拥有主键
     * @return 提前购配置已经拥有
     */
    public CntPostExist selectCntPostExistById(String id);

    /**
     * 查询提前购配置已经拥有列表
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 提前购配置已经拥有集合
     */
    public List<CntPostExistVo> selectCntPostExistList(CntPostExist cntPostExist);

    /**
     * 新增提前购配置已经拥有
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 结果
     */
    public int insertCntPostExist(CntPostExist cntPostExist);

    /**
     * 修改提前购配置已经拥有
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 结果
     */
    public int updateCntPostExist(CntPostExist cntPostExist);

    /**
     * 批量删除提前购配置已经拥有
     *
     * @param ids 需要删除的提前购配置已经拥有主键集合
     * @return 结果
     */
    public int deleteCntPostExistByIds(String[] ids);

    /**
     * 删除提前购配置已经拥有信息
     *
     * @param id 提前购配置已经拥有主键
     * @return 结果
     */
    public int deleteCntPostExistById(String id);
}
