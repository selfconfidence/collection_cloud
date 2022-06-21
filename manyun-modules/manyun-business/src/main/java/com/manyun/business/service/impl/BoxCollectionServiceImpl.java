package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.BoxCollection;
import com.manyun.business.domain.vo.BoxCollectionJoinVo;
import com.manyun.business.mapper.BoxCollectionMapper;
import com.manyun.business.service.IBoxCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 盲盒与藏品中间表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class BoxCollectionServiceImpl extends ServiceImpl<BoxCollectionMapper, BoxCollection> implements IBoxCollectionService {

    /**
     * 盲盒编号
     * @param boxId
     * @return
     */
    @Override
    public List<BoxCollectionJoinVo> findJoinCollections(String boxId) {
        List<BoxCollection> boxCollections = list(Wrappers.<BoxCollection>lambdaQuery().eq(BoxCollection::getBoxId, boxId).orderByDesc(BoxCollection::getTranSvg));
        return boxCollections.parallelStream().map(this::initBoxCollectionJoinVo).collect(Collectors.toList());
    }


    private BoxCollectionJoinVo initBoxCollectionJoinVo(BoxCollection boxCollection){
        BoxCollectionJoinVo collectionJoinVo = Builder.of(BoxCollectionJoinVo::new).build();
        BeanUtil.copyProperties(boxCollection,collectionJoinVo);
        return collectionJoinVo;

    }
}
