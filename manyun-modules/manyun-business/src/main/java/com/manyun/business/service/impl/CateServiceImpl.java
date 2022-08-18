package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Cate;
import com.manyun.business.domain.entity.CntCreationd;
import com.manyun.business.domain.vo.CateVo;
import com.manyun.business.domain.vo.CnfCreationdVo;
import com.manyun.business.mapper.CateMapper;
import com.manyun.business.service.ICateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.ICntCreationdService;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.CATE_PARENT_ID;

/**
 * <p>
 * 藏品系列_分类 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class CateServiceImpl extends ServiceImpl<CateMapper, Cate> implements ICateService {


    @Autowired
    private ICntCreationdService cntCreationdService;
    /**
     * 查询所有系列数据
     * @return
     */
    @Override
    public List<CateVo> cateAll(Integer type) {
        return  list(Wrappers.<Cate>lambdaQuery().eq(Cate::getCateType,type).orderByDesc(Cate::getCreatedTime))
            .parallelStream().map(item ->{
            CateVo cateVo = Builder.of(CateVo::new).build();
            BeanUtil.copyProperties(item,cateVo);
            cateVo.setCnfCreationdVo(initCnfCreationVo(item.getBindCreation()));
            return cateVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CateVo> cateTopLevel(Integer type) {
        return findByParentCateVoList(CATE_PARENT_ID,type);
    }

    @Override
    public List<CateVo> childCate(String parentId, Integer type) {
        return findByParentCateVoList(parentId,type);
    }

    @Override
    public List<CateVo> cateChildAll(Integer type) {
        return  list(Wrappers.<Cate>lambdaQuery().eq(Cate::getCateType,type).ne(Cate::getParentId,CATE_PARENT_ID).orderByDesc(Cate::getCreatedTime))
                .parallelStream().map(item ->{
                    CateVo cateVo = Builder.of(CateVo::new).build();
                    BeanUtil.copyProperties(item,cateVo);
                    cateVo.setCnfCreationdVo(initCnfCreationVo(item.getBindCreation()));
                    return cateVo;
                }).collect(Collectors.toList());
    }

    private List<CateVo> findByParentCateVoList(String parentId,Integer type){
        return  list(Wrappers.<Cate>lambdaQuery().eq(Cate::getCateType,type).eq(Cate::getParentId,parentId).orderByDesc(Cate::getCreatedTime))
                .parallelStream().map(item ->{
                    CateVo cateVo = Builder.of(CateVo::new).build();
                    BeanUtil.copyProperties(item,cateVo);
                    cateVo.setCnfCreationdVo(initCnfCreationVo(item.getBindCreation()));
                    return cateVo;
                }).collect(Collectors.toList());
    }

    /**
     * 根据创作者编号.将创作者查出
     * @param bindCreationId
     * @return
     */
    private CnfCreationdVo initCnfCreationVo(String bindCreationId) {
        CntCreationd cnfCreationd = cntCreationdService.getById(bindCreationId);
        CnfCreationdVo creationdVo = Builder.of(CnfCreationdVo::new).build();
        BeanUtil.copyProperties(cnfCreationd,creationdVo);
        return creationdVo;

    }
}
