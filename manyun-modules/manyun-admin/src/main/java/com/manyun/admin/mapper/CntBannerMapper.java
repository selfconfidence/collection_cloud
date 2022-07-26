package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntBanner;

/**
 * 轮播Mapper接口
 *
 * @author yanwei
 * @date 2022-07-12
 */
public interface CntBannerMapper extends BaseMapper<CntBanner>
{

    /**
     * 查询轮播列表
     *
     * @param cntBanner 轮播
     * @return 轮播集合
     */
    public List<CntBanner> selectCntBannerList(CntBanner cntBanner);

}
