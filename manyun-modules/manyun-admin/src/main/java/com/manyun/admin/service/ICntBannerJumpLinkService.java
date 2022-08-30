package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBannerJumpLink;
import com.manyun.admin.domain.query.BannerJumpLinkQuery;
import com.manyun.admin.domain.vo.BannerJumpLinkVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 轮播跳转链接Service接口
 *
 * @author yanwei
 * @date 2022-08-30
 */
public interface ICntBannerJumpLinkService extends IService<CntBannerJumpLink>
{
    /**
     * 查询轮播跳转链接详情
     *
     * @param id 轮播跳转链接主键
     * @return 轮播跳转链接
     */
    public CntBannerJumpLink selectCntBannerJumpLinkById(String id);

    /**
     * 查询轮播跳转链接列表
     *
     * @param bannerJumpLinkQuery
     * @return 轮播跳转链接集合
     */
    public TableDataInfo<BannerJumpLinkVo> selectCntBannerJumpLinkList(BannerJumpLinkQuery bannerJumpLinkQuery);

    /**
     * 新增轮播跳转链接
     *
     * @param cntBannerJumpLink 轮播跳转链接
     * @return 结果
     */
    public int insertCntBannerJumpLink(CntBannerJumpLink cntBannerJumpLink);

    /**
     * 修改轮播跳转链接
     *
     * @param cntBannerJumpLink 轮播跳转链接
     * @return 结果
     */
    public int updateCntBannerJumpLink(CntBannerJumpLink cntBannerJumpLink);

    /**
     * 批量删除轮播跳转链接
     *
     * @param ids 需要删除的轮播跳转链接主键集合
     * @return 结果
     */
    public int deleteCntBannerJumpLinkByIds(String[] ids);

    /**
     * 删除轮播跳转链接信息
     *
     * @param id 轮播跳转链接主键
     * @return 结果
     */
    public int deleteCntBannerJumpLinkById(String id);
}
