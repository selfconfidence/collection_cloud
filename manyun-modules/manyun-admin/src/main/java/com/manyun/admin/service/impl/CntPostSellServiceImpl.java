package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CntPostSellVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntPostSellMapper;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.service.ICntPostSellService;

/**
 * 提前购配置可以购买Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-27
 */
@Service
public class CntPostSellServiceImpl extends ServiceImpl<CntPostSellMapper,CntPostSell> implements ICntPostSellService
{
    @Autowired
    private CntPostSellMapper cntPostSellMapper;

    /**
     * 查询提前购配置可以购买详情
     *
     * @param id 提前购配置可以购买主键
     * @return 提前购配置可以购买
     */
    @Override
    public CntPostSell selectCntPostSellById(String id)
    {
        return getById(id);
    }

    /**
     * 查询提前购配置可以购买列表
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 提前购配置可以购买
     */
    @Override
    public List<CntPostSellVo> selectCntPostSellList(CntPostSell cntPostSell)
    {
        return cntPostSellMapper.selectCntPostSellList(cntPostSell).stream().map(m->{
            CntPostSellVo cntPostSellVo=new CntPostSellVo();
            BeanUtil.copyProperties(m,cntPostSellVo);
            return cntPostSellVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增提前购配置可以购买
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 结果
     */
    @Override
    public int insertCntPostSell(CntPostSell cntPostSell)
    {
        cntPostSell.setId(IdUtils.getSnowflakeNextIdStr());
        cntPostSell.setCreatedBy(SecurityUtils.getUsername());
        cntPostSell.setCreatedTime(DateUtils.getNowDate());
        return save(cntPostSell)==true?1:0;
    }

    /**
     * 修改提前购配置可以购买
     *
     * @param cntPostSell 提前购配置可以购买
     * @return 结果
     */
    @Override
    public int updateCntPostSell(CntPostSell cntPostSell)
    {
        cntPostSell.setUpdatedBy(SecurityUtils.getUsername());
        cntPostSell.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntPostSell)==true?1:0;
    }

    /**
     * 批量删除提前购配置可以购买
     *
     * @param ids 需要删除的提前购配置可以购买主键
     * @return 结果
     */
    @Override
    public int deleteCntPostSellByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除提前购配置可以购买信息
     *
     * @param id 提前购配置可以购买主键
     * @return 结果
     */
    @Override
    public int deleteCntPostSellById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
