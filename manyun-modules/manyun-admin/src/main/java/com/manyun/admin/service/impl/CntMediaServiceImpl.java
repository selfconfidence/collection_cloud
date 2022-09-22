package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.vo.MediaVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntMediaMapper;
import com.manyun.admin.domain.CntMedia;
import com.manyun.admin.service.ICntMediaService;

import java.util.List;

/**
 * 媒体存储器Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-25
 */
@Service
public class CntMediaServiceImpl extends ServiceImpl<CntMediaMapper,CntMedia> implements ICntMediaService
{
    @Autowired
    private CntMediaMapper cntMediaMapper;

    @Override
    public List<MediaVo> initMediaVos(String buiId, String modelType) {
        return cntMediaMapper.initMediaVos(buiId,modelType);
    }

    @Override
    public List<MediaVo> thumbnailImgMediaVos(String buiId, String modelType) {
        return cntMediaMapper.thumbnailImgMediaVos(buiId,modelType);
    }

    @Override
    public List<MediaVo> threeDimensionalMediaVos(String buiId, String modelType) {
        return cntMediaMapper.threeDimensionalMediaVos(buiId,modelType);
    }

}
