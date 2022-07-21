package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.admin.domain.vo.CntCateVo;
import com.manyun.admin.mapper.CntBoxMapper;
import com.manyun.admin.mapper.CntCollectionMapper;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.domain.AjaxResult;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCateMapper;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.service.ICntCateService;

/**
 * 藏品系列_分类Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CntCateServiceImpl implements ICntCateService
{
    @Autowired
    private CntCateMapper cntCateMapper;

    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    @Autowired
    private CntBoxMapper cntBoxMapper;

    /**
     * 查询藏品系列_分类
     *
     * @param id 藏品系列_分类主键
     * @return 藏品系列_分类
     */
    @Override
    public CntCate selectCntCateById(String id)
    {
        return cntCateMapper.selectCntCateById(id);
    }

    /**
     * 查询藏品系列_分类列表
     *
     * @param cntCate 藏品系列_分类
     * @return 藏品系列_分类
     */
    @Override
    public List<CntCateVo> selectCntCateList(CntCate cntCate)
    {
        List<CntCate> cntCates = cntCateMapper.selectCntCateList(cntCate);
        return cntCates
                .parallelStream()
                .filter(f -> f.getCateType()==1)
                .map( e -> {
                    CntCateVo cntCateVo=new CntCateVo();
                    BeanUtil.copyProperties(e,cntCateVo);
                    return cntCateVo;
                }).collect(Collectors.toList());
    }

    /**
     * 新增藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    @Override
    public int insertCntCate(CntCate cntCate)
    {
        cntCate.setId(IdUtils.getSnowflakeNextIdStr());
        cntCate.setCreatedBy(SecurityUtils.getUsername());
        cntCate.setCreatedTime(DateUtils.getNowDate());
        return cntCateMapper.insertCntCate(cntCate);
    }

    /**
     * 修改藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    @Override
    public int updateCntCate(CntCate cntCate)
    {
        cntCate.setUpdatedBy(SecurityUtils.getUsername());
        cntCate.setUpdatedTime(DateUtils.getNowDate());
        return cntCateMapper.updateCntCate(cntCate);
    }

    /**
     * 批量删除藏品系列_分类
     *
     * @param ids 需要删除的藏品系列_分类主键
     * @return 结果
     */
    @Override
    public R deleteCntCateByIds(String[] ids)
    {
        List<CntCollection> cntCollectionList = cntCollectionMapper.selectCntCollectionByCateIds(ids);
        if(cntCollectionList.size()>0){
            List<String> cateIdList = cntCollectionList.stream().map(CntCollection::getCateId).collect(Collectors.toList());
            return R.fail("藏品系列id为: "+ StringUtils.join(cateIdList,",")+" 的已被引用,不允许删除!");
        }
        return cntCateMapper.deleteCntCateByIds(ids)==0?R.fail():R.ok();
    }

    /**
     * 删除藏品系列_分类信息
     *
     * @param id 藏品系列_分类主键
     * @return 结果
     */
    @Override
    public R deleteCntCateById(String id)
    {
        CntCollection cntCollection = cntCollectionMapper.selectCntCollectionById(id);
        if(cntCollection!=null){
            return R.fail("藏品系列id为: "+ cntCollection.getCateId() +" 的已被引用,不允许删除!");
        }
        return cntCateMapper.deleteCntCateById(id)==0?R.fail():R.ok();
    }
}
