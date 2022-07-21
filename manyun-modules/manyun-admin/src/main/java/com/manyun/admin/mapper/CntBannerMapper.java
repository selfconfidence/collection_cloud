package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntBanner;

/**
 * 轮播Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-12
 */
public interface CntBannerMapper 
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
     * 删除轮播
     * 
     * @param id 轮播主键
     * @return 结果
     */
    public int deleteCntBannerById(String id);

    /**
     * 批量删除轮播
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntBannerByIds(String[] ids);
}
