package com.manyun.business.service;

import com.manyun.business.domain.entity.Media;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.MediaVo;

import java.util.List;

/**
 * <p>
 * 媒体存储器 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IMediaService extends IService<Media> {

    /**
     * 查询盲盒或藏品主图
     * @param buiId
     * @param modelType
     * @return
     */
    List<MediaVo> initMediaVos(String buiId, String modelType);

    /**
     * 查询盲盒藏品缩略图
     *
     * @param buiId 关联id
     * @param modelType 类型
     * @return 媒体存储器集合
     */
    public List<MediaVo> thumbnailImgMediaVos(String buiId,String modelType);

    /**
     * 查询盲盒或藏品3D图
     * @param buiId
     * @param modelType
     * @return
     */
    List<MediaVo> threeDimensionalMediaVos(String buiId, String modelType);

}
