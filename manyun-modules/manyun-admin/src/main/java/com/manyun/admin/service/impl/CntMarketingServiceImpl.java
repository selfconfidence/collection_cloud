package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntMarketingMapper;
import com.manyun.admin.domain.CntMarketing;
import com.manyun.admin.service.ICntMarketingService;

/**
 * 营销配置Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-19
 */
@Service
public class CntMarketingServiceImpl extends ServiceImpl<CntMarketingMapper,CntMarketing> implements ICntMarketingService
{
    @Autowired
    private CntMarketingMapper cntMarketingMapper;

    /**
     * 查询营销配置
     *
     * @param id 营销配置主键
     * @return 营销配置
     */
    @Override
    public CntMarketing selectCntMarketingById(String id)
    {
        return getById(id);
    }

    /**
     * 查询营销配置列表
     *
     * @param cntMarketing 营销配置
     * @return 营销配置
     */
    @Override
    public List<CntMarketing> selectCntMarketingList(CntMarketing cntMarketing)
    {
        return cntMarketingMapper.selectCntMarketingList(cntMarketing);
    }

    /**
     * 新增营销配置
     *
     * @param cntMarketing 营销配置
     * @return 结果
     */
    @Override
    public int insertCntMarketing(CntMarketing cntMarketing)
    {
        cntMarketing.setId(IdUtils.getSnowflakeNextIdStr());
        cntMarketing.setCreatedBy(SecurityUtils.getUsername());
        cntMarketing.setCreatedTime(DateUtils.getNowDate());
        return save(cntMarketing)==true?1:0;
    }

    /**
     * 修改营销配置
     *
     * @param cntMarketing 营销配置
     * @return 结果
     */
    @Override
    public int updateCntMarketing(CntMarketing cntMarketing)
    {
        cntMarketing.setUpdatedBy(SecurityUtils.getUsername());
        cntMarketing.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntMarketing)==true?1:0;
    }

    /**
     * 批量删除营销配置
     *
     * @param ids 需要删除的营销配置主键
     * @return 结果
     */
    @Override
    public int deleteCntMarketingByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除营销配置信息
     *
     * @param id 营销配置主键
     * @return 结果
     */
    @Override
    public int deleteCntMarketingById(String id)
    {
        return removeById(id)==true?1:0;
    }
}