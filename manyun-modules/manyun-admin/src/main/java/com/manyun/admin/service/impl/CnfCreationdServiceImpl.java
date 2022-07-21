package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CnfCreationdVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CnfCreationdMapper;
import com.manyun.admin.domain.CnfCreationd;
import com.manyun.admin.service.ICnfCreationdService;

/**
 * 创作者Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CnfCreationdServiceImpl implements ICnfCreationdService
{
    @Autowired
    private CnfCreationdMapper cnfCreationdMapper;

    /**
     * 查询创作者
     *
     * @param id 创作者主键
     * @return 创作者
     */
    @Override
    public CnfCreationd selectCnfCreationdById(String id)
    {
        return cnfCreationdMapper.selectCnfCreationdById(id);
    }

    /**
     * 查询创作者列表
     *
     * @param cnfCreationd 创作者
     * @return 创作者
     */
    @Override
    public List<CnfCreationdVo> selectCnfCreationdList(CnfCreationd cnfCreationd)
    {
        return cnfCreationdMapper.selectCnfCreationdList(cnfCreationd).stream().map(m ->{
            CnfCreationdVo cnfCreationdVo=new CnfCreationdVo();
            BeanUtil.copyProperties(m,cnfCreationdVo);
            return cnfCreationdVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增创作者
     *
     * @param cnfCreationd 创作者
     * @return 结果
     */
    @Override
    public int insertCnfCreationd(CnfCreationd cnfCreationd)
    {
        cnfCreationd.setId(IdUtils.getSnowflakeNextIdStr());
        cnfCreationd.setCreatedBy(SecurityUtils.getUsername());
        cnfCreationd.setCreatedTime(DateUtils.getNowDate());
        return cnfCreationdMapper.insertCnfCreationd(cnfCreationd);
    }

    /**
     * 修改创作者
     *
     * @param cnfCreationd 创作者
     * @return 结果
     */
    @Override
    public int updateCnfCreationd(CnfCreationd cnfCreationd)
    {
        cnfCreationd.setUpdatedBy(SecurityUtils.getUsername());
        cnfCreationd.setUpdatedTime(DateUtils.getNowDate());
        return cnfCreationdMapper.updateCnfCreationd(cnfCreationd);
    }

    /**
     * 批量删除创作者
     *
     * @param ids 需要删除的创作者主键
     * @return 结果
     */
    @Override
    public int deleteCnfCreationdByIds(String[] ids)
    {
        return cnfCreationdMapper.deleteCnfCreationdByIds(ids);
    }

    /**
     * 删除创作者信息
     *
     * @param id 创作者主键
     * @return 结果
     */
    @Override
    public int deleteCnfCreationdById(String id)
    {
        return cnfCreationdMapper.deleteCnfCreationdById(id);
    }
}
