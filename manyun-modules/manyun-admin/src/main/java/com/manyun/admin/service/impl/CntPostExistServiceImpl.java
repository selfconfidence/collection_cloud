package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.PostExistQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostExistMapper;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.service.ICntPostExistService;

/**
 * 提前购配置已经拥有Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-27
 */
@Service
public class CntPostExistServiceImpl extends ServiceImpl<CntPostExistMapper,CntPostExist> implements ICntPostExistService
{
    @Autowired
    private CntPostExistMapper cntPostExistMapper;

    /**
     * 查询提前购配置已经拥有详情
     *
     * @param id 提前购配置已经拥有主键
     * @return 提前购配置已经拥有
     */
    @Override
    public CntPostExist selectCntPostExistById(String id)
    {
        return getById(id);
    }

    /**
     * 查询提前购配置已经拥有列表
     *
     * @param postExistQuery
     * @return 提前购配置已经拥有
     */
    @Override
    public TableDataInfo<CntPostExistVo> selectCntPostExistList(PostExistQuery postExistQuery)
    {
        PageHelper.startPage(postExistQuery.getPageNum(),postExistQuery.getPageSize());
        List<CntPostExistVo> cntPostExists = cntPostExistMapper.selectCntPostExistList(postExistQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntPostExists,cntPostExists);
    }

    /**
     * 新增提前购配置已经拥有
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 结果
     */
    @Override
    public int insertCntPostExist(CntPostExist cntPostExist)
    {
        cntPostExist.setId(IdUtils.getSnowflakeNextIdStr());
        cntPostExist.setCreatedBy(SecurityUtils.getUsername());
        cntPostExist.setCreatedTime(DateUtils.getNowDate());
        return save(cntPostExist)==true?1:0;
    }

    /**
     * 修改提前购配置已经拥有
     *
     * @param cntPostExist 提前购配置已经拥有
     * @return 结果
     */
    @Override
    public int updateCntPostExist(CntPostExist cntPostExist)
    {
        cntPostExist.setUpdatedBy(SecurityUtils.getUsername());
        cntPostExist.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntPostExist)==true?1:0;
    }

    /**
     * 批量删除提前购配置已经拥有
     *
     * @param ids 需要删除的提前购配置已经拥有主键
     * @return 结果
     */
    @Override
    public int deleteCntPostExistByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除提前购配置已经拥有信息
     *
     * @param id 提前购配置已经拥有主键
     * @return 结果
     */
    @Override
    public int deleteCntPostExistById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
