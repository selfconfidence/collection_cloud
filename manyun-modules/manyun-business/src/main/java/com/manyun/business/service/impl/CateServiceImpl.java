package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Cate;
import com.manyun.business.domain.vo.CateVo;
import com.manyun.business.mapper.CateMapper;
import com.manyun.business.service.ICateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            return cateVo;
        }).collect(Collectors.toList());
    }
}
