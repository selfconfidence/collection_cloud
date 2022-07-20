package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntWithdraw;

/**
 * 提现配置Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntWithdrawMapper 
{
    /**
     * 查询提现配置
     * 
     * @param id 提现配置主键
     * @return 提现配置
     */
    public CntWithdraw selectCntWithdrawById(String id);

    /**
     * 查询提现配置列表
     * 
     * @param cntWithdraw 提现配置
     * @return 提现配置集合
     */
    public List<CntWithdraw> selectCntWithdrawList(CntWithdraw cntWithdraw);

    /**
     * 新增提现配置
     * 
     * @param cntWithdraw 提现配置
     * @return 结果
     */
    public int insertCntWithdraw(CntWithdraw cntWithdraw);

    /**
     * 修改提现配置
     * 
     * @param cntWithdraw 提现配置
     * @return 结果
     */
    public int updateCntWithdraw(CntWithdraw cntWithdraw);

    /**
     * 删除提现配置
     * 
     * @param id 提现配置主键
     * @return 结果
     */
    public int deleteCntWithdrawById(String id);

    /**
     * 批量删除提现配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntWithdrawByIds(String[] ids);
}
