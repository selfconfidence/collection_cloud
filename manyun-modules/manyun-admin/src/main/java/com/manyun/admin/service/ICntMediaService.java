package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntMedia;
import com.manyun.admin.domain.vo.MediaVo;

import java.util.List;

/**
 * 媒体存储器Service接口
 *
 * @author yanwei
 * @date 2022-07-25
 */
public interface ICntMediaService extends IService<CntMedia>
{
    /**
     * 查询盲盒藏品主图
     *
     * @param buiId 关联id
     * @param modelType 类型
     * @return 媒体存储器集合
     */
    public List<MediaVo> initMediaVos(String buiId,String modelType);

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
