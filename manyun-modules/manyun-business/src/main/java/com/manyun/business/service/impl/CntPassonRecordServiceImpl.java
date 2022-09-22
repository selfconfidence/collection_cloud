package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.domain.entity.CntPassonRecord;
import com.manyun.business.domain.vo.MediaVo;
import com.manyun.business.domain.vo.PassonRecordVo;
import com.manyun.business.mapper.CntPassonRecordMapper;
import com.manyun.business.service.ICntPassonRecordService;
import com.manyun.business.service.IMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.PageUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 转赠记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-18
 */
@Service
public class CntPassonRecordServiceImpl extends ServiceImpl<CntPassonRecordMapper, CntPassonRecord> implements ICntPassonRecordService
{
    @Autowired
    private CntPassonRecordMapper cntPassonRecordMapper;

    @Autowired
    private IMediaService mediaService;

    @Override
    public TableDataInfo<PassonRecordVo> passonList(String userId, PageQuery pageQuery) {
        PageUtils.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<CntPassonRecord> list = list(Wrappers.<CntPassonRecord>lambdaQuery()
                .eq(CntPassonRecord::getOldUserId, userId).or()
                .eq(CntPassonRecord::getNewUserId, userId).orderByDesc(CntPassonRecord::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(list.parallelStream().map(this::providerPassonRecordVo).collect(Collectors.toList()),list);
    }

    private PassonRecordVo providerPassonRecordVo(CntPassonRecord cntPassonRecord) {
        PassonRecordVo passonRecordVo = Builder.of(PassonRecordVo::new).build();
        passonRecordVo.setFromPhoneNo(cntPassonRecord.getOldUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        passonRecordVo.setToPhoneNo(cntPassonRecord.getNewUserPhone());
        List<MediaVo> mediaVos = mediaService.initMediaVos(cntPassonRecord.getPictureId(), Integer.valueOf(0).toString()
                .equals(cntPassonRecord.getPictureType()) ? BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE : BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
        List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(cntPassonRecord.getPictureId(), Integer.valueOf(0).toString()
                .equals(cntPassonRecord.getPictureType()) ? BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE : BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
        List<MediaVo> threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(cntPassonRecord.getPictureId(), Integer.valueOf(0).toString()
                .equals(cntPassonRecord.getPictureType()) ? BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE : BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
        passonRecordVo.setMediaUrl(mediaVos.get(0).getMediaUrl());
        passonRecordVo.setThumbnailImg(thumbnailImgMediaVos.size()>0?thumbnailImgMediaVos.get(0).getMediaUrl():"");
        passonRecordVo.setThreeDimensionalImg(threeDimensionalMediaVos.size()>0?threeDimensionalMediaVos.get(0).getMediaUrl():"");
        passonRecordVo.setGoodsName(cntPassonRecord.getGoodsName());
        passonRecordVo.setCreateTime(cntPassonRecord.getCreatedTime());
        return passonRecordVo;
    }

}
