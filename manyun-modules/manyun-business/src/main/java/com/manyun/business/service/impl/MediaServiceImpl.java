package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.Media;
import com.manyun.business.mapper.MediaMapper;
import com.manyun.business.service.IMediaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
