package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.domain.vo.CntPostSellVo;

/**
 * 提前购配置可以购买Service接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface ICntPostSellService extends IService<CntPostSell>
{
    /**
     * 查询提前购配置可以购买详情
     *
     * @param id 提前购配置可以购买主键
     * @return 提前购配置可以购买
     */
    public CntPostSell selectCntPostSellById(String id);

    /**
     * 查询提前购配置可以购买列表
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 提前购配置可以购买集合
     */
    public List<CntPostSellVo> selectCntPostSellList(CntPostSell cntPostSell);

    /**
     * 新增提前购配置可以购买
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 结果
     */
    public int insertCntPostSell(CntPostSell cntPostSell);

    /**
     * 修改提前购配置可以购买
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 结果
     */
    public int updateCntPostSell(CntPostSell cntPostSell);

    /**
     * 批量删除提前购配置可以购买
     *
     * @param ids 需要删除的提前购配置可以购买主键集合
     * @return 结果
     */
    public int deleteCntPostSellByIds(String[] ids);

    /**
     * 删除提前购配置可以购买信息
     *
     * @param id 提前购配置可以购买主键
     * @return 结果
     */
    public int deleteCntPostSellById(String id);
}
