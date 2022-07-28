package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CntTarVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntTarMapper;
import com.manyun.admin.domain.CntTar;
import com.manyun.admin.service.ICntTarService;

/**
 * 抽签规则(盲盒,藏品)Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-27
 */
@Service
public class CntTarServiceImpl extends ServiceImpl<CntTarMapper,CntTar> implements ICntTarService
{
    @Autowired
    private CntTarMapper cntTarMapper;

    /**
     * 查询抽签规则(盲盒,藏品)详情
     *
     * @param id 抽签规则(盲盒,藏品)主键
     * @return 抽签规则(盲盒,藏品)
     */
    @Override
    public CntTar selectCntTarById(String id)
    {
        return getById(id);
    }

    /**
     * 查询抽签规则(盲盒,藏品)列表
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 抽签规则(盲盒,藏品)
     */
    @Override
    public List<CntTarVo> selectCntTarList(CntTar cntTar)
    {
        return cntTarMapper.selectCntTarList(cntTar).stream().map(m->{
            CntTarVo cntTarVo=new CntTarVo();
            BeanUtil.copyProperties(m,cntTarVo);
            return cntTarVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增抽签规则(盲盒,藏品)
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 结果
     */
    @Override
    public int insertCntTar(CntTar cntTar)
    {
        cntTar.setId(IdUtils.getSnowflakeNextIdStr());
        cntTar.setCreatedBy(SecurityUtils.getUsername());
        cntTar.setCreatedTime(DateUtils.getNowDate());
        return save(cntTar)==true?1:0;
    }

    /**
     * 修改抽签规则(盲盒,藏品)
     *
     * @param cntTar 抽签规则(盲盒,藏品)
     * @return 结果
     */
    @Override
    public int updateCntTar(CntTar cntTar)
    {
        cntTar.setUpdatedBy(SecurityUtils.getUsername());
        cntTar.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntTar)==true?1:0;
    }

    /**
     * 批量删除抽签规则(盲盒,藏品)
     *
     * @param ids 需要删除的抽签规则(盲盒,藏品)主键
     * @return 结果
     */
    @Override
    public int deleteCntTarByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除抽签规则(盲盒,藏品)信息
     *
     * @param id 抽签规则(盲盒,藏品)主键
     * @return 结果
     */
    @Override
    public int deleteCntTarById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
