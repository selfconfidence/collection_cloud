package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntWithdrawMapper;
import com.manyun.admin.domain.CntWithdraw;
import com.manyun.admin.service.ICntWithdrawService;

/**
 * 提现配置Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-19
 */
@Service
public class CntWithdrawServiceImpl extends ServiceImpl<CntWithdrawMapper,CntWithdraw> implements ICntWithdrawService
{
    @Autowired
    private CntWithdrawMapper cntWithdrawMapper;

    /**
     * 查询提现配置
     *
     * @param id 提现配置主键
     * @return 提现配置
     */
    @Override
    public CntWithdraw selectCntWithdrawById(String id)
    {
        return getById(id);
    }

    /**
     * 查询提现配置列表
     *
     * @param cntWithdraw 提现配置
     * @return 提现配置
     */
    @Override
    public List<CntWithdraw> selectCntWithdrawList(CntWithdraw cntWithdraw)
    {
        return cntWithdrawMapper.selectCntWithdrawList(cntWithdraw);
    }

    /**
     * 新增提现配置
     *
     * @param cntWithdraw 提现配置
     * @return 结果
     */
    @Override
    public int insertCntWithdraw(CntWithdraw cntWithdraw)
    {
        cntWithdraw.setId(IdUtils.getSnowflakeNextIdStr());
        cntWithdraw.setCreatedBy(SecurityUtils.getUsername());
        cntWithdraw.setCreatedTime(DateUtils.getNowDate());
        return save(cntWithdraw)==true?1:0;
    }

    /**
     * 修改提现配置
     *
     * @param cntWithdraw 提现配置
     * @return 结果
     */
    @Override
    public int updateCntWithdraw(CntWithdraw cntWithdraw)
    {
        cntWithdraw.setUpdatedBy(SecurityUtils.getUsername());
        cntWithdraw.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntWithdraw)==true?1:0;
    }

    /**
     * 批量删除提现配置
     *
     * @param ids 需要删除的提现配置主键
     * @return 结果
     */
    @Override
    public int deleteCntWithdrawByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除提现配置信息
     *
     * @param id 提现配置主键
     * @return 结果
     */
    @Override
    public int deleteCntWithdrawById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
