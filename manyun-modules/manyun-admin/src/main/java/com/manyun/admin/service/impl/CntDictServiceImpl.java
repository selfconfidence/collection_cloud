package com.manyun.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.CnfCreationd;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.CntLable;
import com.manyun.admin.domain.vo.CntCollectionDictVo;
import com.manyun.admin.domain.vo.CollectionCateDictVo;
import com.manyun.admin.domain.vo.CreationdDictVo;
import com.manyun.admin.domain.vo.LableDictVo;
import com.manyun.admin.mapper.CnfCreationdMapper;
import com.manyun.admin.mapper.CntCateMapper;
import com.manyun.admin.mapper.CntCollectionMapper;
import com.manyun.admin.mapper.CntLableMapper;
import com.manyun.admin.service.CntDictService;
import com.manyun.common.core.enums.CateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntDictServiceImpl implements CntDictService
{

    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    @Autowired
    private CntCateMapper cntCateMapper;

    @Autowired
    private CnfCreationdMapper cnfCreationdMapper;

    @Autowired
    private CntLableMapper cntLableMapper;

    /***
     * 查询藏品字典
     */
    @Override
    public List<CntCollectionDictVo> collectionDict() {
        return cntCollectionMapper.selectCntCollectionList(new CntCollection()).stream().map(m ->{
            CntCollectionDictVo cntCollectionDictVo=new CntCollectionDictVo();
            BeanUtil.copyProperties(m,cntCollectionDictVo);
            return cntCollectionDictVo;
        }).collect(Collectors.toList());
    }

    /***
     * 查询藏品系列字典
     * @return
     */
    @Override
    public List<CollectionCateDictVo> collectionCateDict() {
        CntCate cntCate=new CntCate();
        cntCate.setCateType(Long.valueOf(CateType.COLLECTION_CATE.getCode()));
        return cntCateMapper.selectCntCateList(cntCate).stream().map(m ->{
            CollectionCateDictVo collectionCateDictVo=new CollectionCateDictVo();
            BeanUtil.copyProperties(m,collectionCateDictVo);
            return collectionCateDictVo;
        }).collect(Collectors.toList());
    }

    /***
     * 查询创作者字典
     */
    @Override
    public List<CreationdDictVo> creationdDict() {
        return cnfCreationdMapper.selectCnfCreationdList(new CnfCreationd()).stream().map(m ->{
            CreationdDictVo creationdDictVo=new CreationdDictVo();
            BeanUtil.copyProperties(m,creationdDictVo);
            return creationdDictVo;
        }).collect(Collectors.toList());
    }

    /***
     * 查询标签字典
     */
    @Override
    public List<LableDictVo> lableDict() {
        return cntLableMapper.selectCntLableList(new CntLable()).stream().map(m ->{
            LableDictVo lableDictVo=new LableDictVo();
            BeanUtil.copyProperties(m,lableDictVo);
            return lableDictVo;
        }).collect(Collectors.toList());
    }


}
