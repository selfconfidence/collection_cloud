package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntBannerJumpLink;
import com.manyun.admin.domain.query.BannerJumpLinkQuery;

/**
 * 轮播跳转链接Mapper接口
 *
 * @author yanwei
 * @date 2022-08-30
 */
public interface CntBannerJumpLinkMapper extends BaseMapper<CntBannerJumpLink>
{
    /**
     * 查询轮播跳转链接列表
     *
     * @param bannerJumpLinkQuery 轮播跳转链接
     * @return 轮播跳转链接集合
     */
    public List<CntBannerJumpLink> selectCntBannerJumpLinkList(BannerJumpLinkQuery bannerJumpLinkQuery);
}
