package com.manyun.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.admin.service.*;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.CateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ICntCollectionService collectionService;

    @Autowired
    private ICntCateService cateService;

    @Autowired
    private ICnfCreationdService creationdService;

    @Autowired
    private ICntLableService lableService;

    @Autowired
    private ICntCustomerServiceService customerServiceService;

    /***
     * 查询藏品字典
     */
    @Override
    public List<CntCollectionDictVo> collectionDict() {
        return collectionService.list().stream().map(m ->{
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
        return cateService.list(Wrappers.<CntCate>lambdaQuery().eq(CntCate::getCateType,Long.valueOf(CateType.COLLECTION_CATE.getCode()))).stream().map(m ->{
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
        return creationdService.list().stream().map(m ->{
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
        return lableService.list().stream().map(m ->{
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
        List<CustomerServiceDictVo> customerServiceDictVos=customerServiceService.list(Wrappers.<CntCustomerService>lambdaQuery().eq(CntCustomerService::getMenuStatus,"0").eq(CntCustomerService::getParentId,0)).stream().map(m ->{
            CustomerServiceDictVo customerServiceDictVo=new CustomerServiceDictVo();
            BeanUtil.copyProperties(m,customerServiceDictVo);
            return customerServiceDictVo;
        }).collect(Collectors.toList());
        customerServiceDictVos.add(Builder.of(CustomerServiceDictVo::new).with(CustomerServiceDictVo::setMenuId,Long.valueOf(0)).with(CustomerServiceDictVo::setMenuName,"父菜单").build());
        return customerServiceDictVos.stream().sorted(Comparator.comparing(CustomerServiceDictVo::getMenuId)).collect(Collectors.toList());
    }


}
