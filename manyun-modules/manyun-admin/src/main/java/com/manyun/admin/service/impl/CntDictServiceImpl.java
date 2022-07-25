package com.manyun.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.admin.service.CntDictService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.CateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private CntCustomerServiceMapper cntCustomerServiceMapper;

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

    /***
     * 查询客服字典
     */
    @Override
    public List<CustomerServiceDictVo> customerServiceDict() {
        List<CustomerServiceDictVo> customerServiceDictVos=cntCustomerServiceMapper.selectCntCustomerServiceList(Builder.of(CntCustomerService::new).build()).stream().filter(f -> (f.getMenuStatus().equals("0") && f.getParentId()==0)).map(m ->{
            CustomerServiceDictVo customerServiceDictVo=new CustomerServiceDictVo();
            BeanUtil.copyProperties(m,customerServiceDictVo);
            return customerServiceDictVo;
        }).collect(Collectors.toList());
        customerServiceDictVos.add(Builder.of(CustomerServiceDictVo::new).with(CustomerServiceDictVo::setMenuId,Long.valueOf(0)).with(CustomerServiceDictVo::setMenuName,"父菜单").build());
        return customerServiceDictVos.stream().sorted(Comparator.comparing(CustomerServiceDictVo::getMenuId)).collect(Collectors.toList());
    }


}
