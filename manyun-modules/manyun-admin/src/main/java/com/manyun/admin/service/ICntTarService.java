package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntTar;
import com.manyun.admin.domain.vo.CntTarVo;

/**
 * 抽签规则(盲盒,藏品)Service接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface ICntTarService extends IService<CntTar>
{
    /**
     * 查询抽签规则(盲盒,藏品)详情
     *
     * @param id 抽签规则(盲盒,藏品)主键
     * @return 抽签规则(盲盒,藏品)
     */
    public CntTar selectCntTarById(String id);

    /**
     * 查询抽签规则(盲盒,藏品)列表
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 抽签规则(盲盒,藏品)集合
     */
    public List<CntTarVo> selectCntTarList(CntTar cntTar);

    /**
     * 新增抽签规则(盲盒,藏品)
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 结果
     */
    public int insertCntTar(CntTar cntTar);

    /**
     * 修改抽签规则(盲盒,藏品)
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 结果
     */
    public int updateCntTar(CntTar cntTar);

    /**
     * 批量删除抽签规则(盲盒,藏品)
     *
     * @param ids 需要删除的抽签规则(盲盒,藏品)主键集合
     * @return 结果
     */
    public int deleteCntTarByIds(String[] ids);

    /**
     * 删除抽签规则(盲盒,藏品)信息
     *
     * @param id 抽签规则(盲盒,藏品)主键
     * @return 结果
     */
    public int deleteCntTarById(String id);
}
