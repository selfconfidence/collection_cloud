package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.BoxScoreQuery;
import com.manyun.admin.domain.vo.CntBoxScoreVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxScoreMapper;
import com.manyun.admin.domain.CntBoxScore;
import com.manyun.admin.service.ICntBoxScoreService;

/**
 * 盲盒评分Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-19
 */
@Service
public class CntBoxScoreServiceImpl extends ServiceImpl<CntBoxScoreMapper,CntBoxScore> implements ICntBoxScoreService
{
    @Autowired
    private CntBoxScoreMapper cntBoxScoreMapper;

    /**
     * 查询盲盒评分详情
     *
     * @param id 盲盒评分主键
     * @return 盲盒评分
     */
    @Override
    public CntBoxScore selectCntBoxScoreById(String id)
    {
        return getById(id);
    }

    /**
     * 查询盲盒评分列表
     *
     * @param boxScoreQuery 盲盒评分
     * @return 盲盒评分
     */
    @Override
    public TableDataInfo<CntBoxScoreVo> selectCntBoxScoreList(BoxScoreQuery boxScoreQuery)
    {
        PageHelper.startPage(boxScoreQuery.getPageNum(),boxScoreQuery.getPageSize());
        List<CntBoxScore> cntBoxScoreList = cntBoxScoreMapper.selectCntBoxScoreList(boxScoreQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxScoreList.parallelStream().map(m->{
            CntBoxScoreVo cntBoxScoreVo=new CntBoxScoreVo();
            BeanUtil.copyProperties(m,cntBoxScoreVo);
            return cntBoxScoreVo;
        }).collect(Collectors.toList()), cntBoxScoreList);
    }

    /**
     * 新增盲盒评分
     *
     * @param cntBoxScore 盲盒评分
     * @return 结果
     */
    @Override
    public int insertCntBoxScore(CntBoxScore cntBoxScore)
    {
        cntBoxScore.setId(IdUtils.getSnowflakeNextIdStr());
        cntBoxScore.setCreatedBy(SecurityUtils.getUsername());
        cntBoxScore.setCreatedTime(DateUtils.getNowDate());
        return save(cntBoxScore)==true?1:0;
    }

    /**
     * 修改盲盒评分
     *
     * @param cntBoxScore 盲盒评分
     * @return 结果
     */
    @Override
    public int updateCntBoxScore(CntBoxScore cntBoxScore)
    {
        cntBoxScore.setUpdatedBy(SecurityUtils.getUsername());
        cntBoxScore.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntBoxScore)==true?1:0;
    }

    /**
     * 批量删除盲盒评分
     *
     * @param ids 需要删除的盲盒评分主键
     * @return 结果
     */
    @Override
    public int deleteCntBoxScoreByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除盲盒评分信息
     *
     * @param id 盲盒评分主键
     * @return 结果
     */
    @Override
    public int deleteCntBoxScoreById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
