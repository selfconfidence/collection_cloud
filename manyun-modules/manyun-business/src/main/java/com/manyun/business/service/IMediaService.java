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

    List<MediaVo> initMediaVos(String buiId, String modelType);
}
