package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.*;
import com.manyun.business.service.ICollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.CollectionStatus.DOWN_ACTION;

/**
 * <p>
 * 藏品表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CnfCollectionMapper, CnfCollection> implements ICollectionService {
    @Resource
    private CnfCollectionMapper cnfCollectionMapper;

    @Resource
    private CateMapper cateMapper;

    @Resource
    private CnfCreationdMapper cnfCreationdMapper;

    @Resource
    private LableMapper lableMapper;

    @Resource
    private MediaMapper mediaMapper;

    @Autowired
    private IMediaService mediaService;

    @Resource
    private CollectionLableMapper collectionLableMapper;

    @Resource
    private CollectionInfoMapper collectionInfoMapper;


    @Override
    public List<CollectionVo> pageQueryList(CollectionQuery collectionQuery) {
        // 查询条件部分
        List<CnfCollection> cnfCollections = list(Wrappers.<CnfCollection>lambdaQuery()
                .eq(StrUtil.isNotBlank(collectionQuery.getCateId()), CnfCollection::getCateId, collectionQuery.getCateId())
                .ne(CnfCollection::getStatusBy,DOWN_ACTION.getCode())
                .like(StrUtil.isNotBlank(collectionQuery.getCollectionName()), CnfCollection::getCollectionName, collectionQuery.getCollectionName())
                .eq(StrUtil.isNotBlank(collectionQuery.getBindCreationId()), CnfCollection::getBindCreation, collectionQuery.getBindCreationId())
                .orderByDesc(CnfCollection::getCreatedTime)
        );
        // 聚合数据
        return cnfCollections.parallelStream().map(this::providerCollectionVo).collect(Collectors.toList());
    }

    @Override
    public CollectionAllVo info(String id) {

        return Builder.of(CollectionAllVo::new).with(CollectionAllVo::setCollectionVo,providerCollectionVo(getById(id))).with(CollectionAllVo::setCollectionInfoVo,providerCollectionInfoVo(id)).build();
    }

    /**
     * 根据藏品编号  查询出藏品详细信息
     * @param collectionId
     * @return
     */
    private CollectionInfoVo providerCollectionInfoVo(String collectionId) {
        CollectionInfoVo collectionInfoVo = Builder.of(CollectionInfoVo::new).build();
        CollectionInfo collectionInfo = collectionInfoMapper.selectOne(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, collectionId));
        BeanUtil.copyProperties(collectionInfo,collectionInfoVo);
        return collectionInfoVo;
    }

    private CollectionVo providerCollectionVo(CnfCollection cnfCollection){
        CollectionVo collectionVo = Builder.of(CollectionVo::new).build();
        BeanUtil.copyProperties(cnfCollection,collectionVo);
        collectionVo.setLableVos(initLableVos(cnfCollection.getId()));
        collectionVo.setMediaVos(initMediaVos(cnfCollection.getId()));
        collectionVo.setCnfCreationdVo(initCnfCreationVo(cnfCollection.getBindCreation()));
        collectionVo.setCateVo(initCateVo(cnfCollection.getCateId()));
        return collectionVo;
    }

    /**
     * 根据 系列 分类编号 查询整体
     * @param cateId
     * @return
     */
    private CateVo initCateVo(String cateId) {
        CateVo cateVo =  Builder.of(CateVo::new).build();
        Cate cate = cateMapper.selectById(cateId);
        BeanUtil.copyProperties(cate,cateVo);
        return cateVo;
    }

    /**
     * 根据创作者编号.将创作者查出
     * @param bindCreationId
     * @return
     */
    private CnfCreationdVo initCnfCreationVo(String bindCreationId) {
        CnfCreationd cnfCreationd = cnfCreationdMapper.selectById(bindCreationId);
        CnfCreationdVo creationdVo = Builder.of(CnfCreationdVo::new).build();
        BeanUtil.copyProperties(cnfCreationd,creationdVo);
        return creationdVo;

    }

    /**
     * 根据藏品编号 将对应的媒体图片组合拼装
     * @param collectionId
     * @return
     */
    private List<MediaVo> initMediaVos(String collectionId) {
      return  mediaService.initMediaVos(collectionId, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
    }

    /**
     * 根据藏品编号  查询所有关联的标签
     * @param collectionId
     * @return
     */
    private List<LableVo> initLableVos(String collectionId) {
        List<CollectionLable> collectionLables = collectionLableMapper.selectList(Wrappers.<CollectionLable>lambdaQuery().eq(CollectionLable::getCollectionId, collectionId));
        if (collectionLables.isEmpty())return ListUtil.empty();

        List<Lable> lableList = lableMapper.selectList(Wrappers.<Lable>lambdaQuery().in(Lable::getId, collectionLables.stream().map(item -> item.getLableId()).collect(Collectors.toList())));
        if (lableList.isEmpty())return ListUtil.empty();

        return lableList.parallelStream().map(item -> {
            LableVo lableVo = Builder.of(LableVo::new).build();
            BeanUtil.copyProperties(item, lableVo);
            return lableVo;
        }).collect(Collectors.toList());
    }
}
