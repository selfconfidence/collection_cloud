package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.vo.CntLableVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntLableMapper;
import com.manyun.admin.domain.CntLable;
import com.manyun.admin.service.ICntLableService;

/**
 * 藏品标签Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntLableServiceImpl extends ServiceImpl<CntLableMapper,CntLable> implements ICntLableService
{
    @Autowired
    private CntLableMapper cntLableMapper;

    /**
     * 查询藏品标签详情
     *
     * @param id 藏品标签主键
     * @return 藏品标签
     */
    @Override
    public CntLable selectCntLableById(String id)
    {
        return getById(id);
    }

    /**
     * 查询藏品标签列表
     *
     * @param cntLable 藏品标签
     * @return 藏品标签
     */
    @Override
    public List<CntLableVo> selectCntLableList(CntLable cntLable)
    {
        return cntLableMapper.selectCntLableList(cntLable).stream().map(m ->{
            CntLableVo cntLableVo=new CntLableVo();
            BeanUtil.copyProperties(m,cntLableVo);
            return cntLableVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增藏品标签
     *
     * @param cntLable 藏品标签
     * @return 结果
     */
    @Override
    public int insertCntLable(CntLable cntLable)
    {
        cntLable.setId(IdUtils.getSnowflakeNextIdStr());
        cntLable.setCreatedBy(SecurityUtils.getUsername());
        cntLable.setCreatedTime(DateUtils.getNowDate());
        return save(cntLable)==true?1:0;
    }

    /**
     * 修改藏品标签
     *
     * @param cntLable 藏品标签
     * @return 结果
     */
    @Override
    public int updateCntLable(CntLable cntLable)
    {
        cntLable.setUpdatedBy(SecurityUtils.getUsername());
        cntLable.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntLable)==true?1:0;
    }

    /**
     * 批量删除藏品标签
     *
     * @param ids 需要删除的藏品标签主键
     * @return 结果
     */
    @Override
    public int deleteCntLableByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除藏品标签信息
     *
     * @param id 藏品标签主键
     * @return 结果
     */
    @Override
    public int deleteCntLableById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
