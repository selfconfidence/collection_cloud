package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.BannerJumpLinkQuery;
import com.manyun.admin.domain.vo.BannerJumpLinkVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBannerJumpLinkMapper;
import com.manyun.admin.domain.CntBannerJumpLink;
import com.manyun.admin.service.ICntBannerJumpLinkService;

/**
 * 轮播跳转链接Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-30
 */
@Service
public class CntBannerJumpLinkServiceImpl extends ServiceImpl<CntBannerJumpLinkMapper,CntBannerJumpLink> implements ICntBannerJumpLinkService
{
    @Autowired
    private CntBannerJumpLinkMapper cntBannerJumpLinkMapper;

    /**
     * 查询轮播跳转链接详情
     *
     * @param id 轮播跳转链接主键
     * @return 轮播跳转链接
     */
    @Override
    public CntBannerJumpLink selectCntBannerJumpLinkById(String id)
    {
        return getById(id);
    }

    /**
     * 查询轮播跳转链接列表
     *
     * @param bannerJumpLinkQuery
     * @return 轮播跳转链接
     */
    @Override
    public TableDataInfo<BannerJumpLinkVo> selectCntBannerJumpLinkList(BannerJumpLinkQuery bannerJumpLinkQuery)
    {
        PageHelper.startPage(bannerJumpLinkQuery.getPageNum(),bannerJumpLinkQuery.getPageSize());
        List<CntBannerJumpLink> cntBannerJumpLinks = cntBannerJumpLinkMapper.selectCntBannerJumpLinkList(bannerJumpLinkQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBannerJumpLinks.parallelStream().map(m->{
            BannerJumpLinkVo bannerJumpLinkVo=new BannerJumpLinkVo();
            BeanUtils.copyProperties(m,bannerJumpLinkVo);
            return bannerJumpLinkVo;
        }).collect(Collectors.toList()), cntBannerJumpLinks);
    }

    /**
     * 新增轮播跳转链接
     *
     * @param cntBannerJumpLink 轮播跳转链接
     * @return 结果
     */
    @Override
    public int insertCntBannerJumpLink(CntBannerJumpLink cntBannerJumpLink)
    {
        cntBannerJumpLink.setId(IdUtils.getSnowflakeNextIdStr());
        cntBannerJumpLink.setCreatedBy(SecurityUtils.getUsername());
        cntBannerJumpLink.setCreatedTime(DateUtils.getNowDate());
        return save(cntBannerJumpLink)==true?1:0;
    }

    /**
     * 修改轮播跳转链接
     *
     * @param cntBannerJumpLink 轮播跳转链接
     * @return 结果
     */
    @Override
    public int updateCntBannerJumpLink(CntBannerJumpLink cntBannerJumpLink)
    {
        cntBannerJumpLink.setUpdatedBy(SecurityUtils.getUsername());
        cntBannerJumpLink.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntBannerJumpLink)==true?1:0;
    }

    /**
     * 批量删除轮播跳转链接
     *
     * @param ids 需要删除的轮播跳转链接主键
     * @return 结果
     */
    @Override
    public int deleteCntBannerJumpLinkByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除轮播跳转链接信息
     *
     * @param id 轮播跳转链接主键
     * @return 结果
     */
    @Override
    public int deleteCntBannerJumpLinkById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
