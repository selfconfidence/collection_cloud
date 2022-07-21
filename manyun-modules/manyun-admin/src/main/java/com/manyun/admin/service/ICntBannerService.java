package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntBanner;

/**
 * 轮播Service接口
 * 
 * @author yanwei
 * @date 2022-07-12
 */
public interface ICntBannerService 
{
    /**
     * 查询轮播
     * 
     * @param id 轮播主键
     * @return 轮播
     */
    public CntBanner selectCntBannerById(String id);

    /**
     * 查询轮播列表
     * 
     * @param cntBanner 轮播
     * @return 轮播集合
     */
    public List<CntBanner> selectCntBannerList(CntBanner cntBanner);

    /**
     * 新增轮播
     * 
     * @param cntBanner 轮播
     * @return 结果
     */
    public int insertCntBanner(CntBanner cntBanner);

    /**
     * 修改轮播
     * 
     * @param cntBanner 轮播
     * @return 结果
     */
    public int updateCntBanner(CntBanner cntBanner);

    /**
     * 批量删除轮播
     * 
     * @param ids 需要删除的轮播主键集合
     * @return 结果
     */
    public int deleteCntBannerByIds(String[] ids);

    /**
     * 删除轮播信息
     * 
     * @param id 轮播主键
     * @return 结果
     */
    public int deleteCntBannerById(String id);
}
