package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Media;
import com.manyun.business.domain.vo.MediaVo;
import com.manyun.business.mapper.MediaMapper;
import com.manyun.business.service.IMediaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 媒体存储器 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements IMediaService {

    /**
     * 根据藏品编号 将对应的媒体图片组合返回
     * @param buiId
     * @param modelType
     */
    @Override
    public List<MediaVo> initMediaVos(String buiId, String modelType) {
        List<String> list=new ArrayList<>();
        list.add(BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
        list.add(BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
        return list(Wrappers.<Media>lambdaQuery().eq(Media::getModelType,modelType).eq(Media::getBuiId,buiId).in(Media::getMediaType,list)).parallelStream().map(item ->{
            MediaVo mediaVo =  Builder.of(MediaVo::new).build();
            BeanUtil.copyProperties(item,mediaVo);
            return mediaVo;
        }).collect(Collectors.toList());
    }

    /**
     * 缩略图
     * @param buiId
     * @param modelType
     */
    @Override
    public List<MediaVo> thumbnailImgMediaVos(String buiId, String modelType) {
        return list(Wrappers.<Media>lambdaQuery().eq(Media::getModelType,modelType).eq(Media::getBuiId,buiId).eq(Media::getMediaType, BusinessConstants.ModelTypeConstant.THUMBNAIL_IMG)).parallelStream().map(item ->{
            MediaVo mediaVo =  Builder.of(MediaVo::new).build();
            BeanUtil.copyProperties(item,mediaVo);
            return mediaVo;
        }).collect(Collectors.toList());
    }

    /**
     * 3D图
     * @param buiId
     * @param modelType
     */
    @Override
    public List<MediaVo> threeDimensionalMediaVos(String buiId, String modelType) {
        return list(Wrappers.<Media>lambdaQuery().eq(Media::getModelType,modelType).eq(Media::getBuiId,buiId).eq(Media::getMediaType, BusinessConstants.ModelTypeConstant.GLB)).parallelStream().map(item ->{
            MediaVo mediaVo =  Builder.of(MediaVo::new).build();
            BeanUtil.copyProperties(item,mediaVo);
            return mediaVo;
        }).collect(Collectors.toList());
    }
}
