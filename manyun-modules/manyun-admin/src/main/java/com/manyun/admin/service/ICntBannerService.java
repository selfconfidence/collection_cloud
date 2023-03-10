package com.manyun.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBanner;
import com.manyun.admin.domain.vo.CntBannerVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 轮播Service接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface ICntBannerService extends IService<CntBanner>
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
     * @param pageQuery
     * @return 轮播集合
     */
    public TableDataInfo<CntBannerVo> selectCntBannerList(PageQuery pageQuery);

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
