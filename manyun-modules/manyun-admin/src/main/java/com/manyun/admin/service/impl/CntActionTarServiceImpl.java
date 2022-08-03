package com.manyun.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.CntMedia;
import com.manyun.admin.domain.dto.SaveActionTarDto;
import com.manyun.admin.domain.query.ActionTarQuery;
import com.manyun.admin.domain.vo.CntActionTarVo;
import com.manyun.admin.domain.vo.MediaVo;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntActionTarMapper;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.service.ICntActionTarService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动合成附属信息Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-21
 */
@Service
public class CntActionTarServiceImpl extends ServiceImpl<CntActionTarMapper,CntActionTar> implements ICntActionTarService
{
    @Autowired
    private CntActionTarMapper cntActionTarMapper;

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private ICntMediaService mediaService;

    /**
     * 查询活动合成附属信息列表
     *
     * @param actionTarQuery
     * @return 活动合成附属信息
     */
    @Override
    public TableDataInfo<CntActionTarVo> selectCntActionTarList(ActionTarQuery actionTarQuery)
    {
        PageHelper.startPage(actionTarQuery.getPageNum(),actionTarQuery.getPageSize());
        List<CntActionTar> cntActionTars = cntActionTarMapper.selectSearchActionTarList(actionTarQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntActionTars.parallelStream().map(m ->{
            CntActionTarVo cntActionTarVo=new CntActionTarVo();
            BeanUtil.copyProperties(m,cntActionTarVo);
            return cntActionTarVo;
        }).collect(Collectors.toList()),cntActionTars);
    }

    /**
     * 新增活动合成附属信息
     *
     * @param saveActionTarDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntActionTar(SaveActionTarDto saveActionTarDto)
    {
        List<CntActionTarVo> cntActionTarVos = saveActionTarDto.getCntActionTarVos();
        String actionId = saveActionTarDto.getActionId();
        Assert.isTrue(Objects.nonNull(cntActionTarVos),"新增失败!");
        List<String> collect = cntActionTarVos.stream().map(CntActionTarVo::getCollectionId).collect(Collectors.toList());
        CntActionTar actionTar = getById(actionId);
        Assert.isFalse(collect.contains(actionTar.getCollectionId()),"合成材料不可与合成藏品一致!");
        remove(Wrappers.<CntActionTar>lambdaQuery().eq(CntActionTar::getActionId,actionId));
        if(cntActionTarVos.size()>0){
            List<String> collectionIds = cntActionTarVos.stream().map(CntActionTarVo::getCollectionId).collect(Collectors.toList());
            List<CntCollection> collectionList = collectionService.listByIds(collectionIds);
            List<MediaVo> mediaVoList = mediaService.list(Wrappers.<CntMedia>lambdaQuery().in(CntMedia::getBuiId,collectionIds).eq(CntMedia::getModelType,BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE))
                    .stream().map(m->{
                        MediaVo mediaVo=new MediaVo();
                        BeanUtil.copyProperties(m,mediaVo);
                        return mediaVo;
                    }).collect(Collectors.toList());
            List<CntActionTar> list = cntActionTarVos.stream().map(m -> {
                CntActionTar cntActionTar = new CntActionTar();
                BeanUtil.copyProperties(m, cntActionTar);
                cntActionTar.setId(IdUtils.getSnowflakeNextIdStr());
                cntActionTar.setActionId(actionId);
                Optional<CntCollection> cntCollection = collectionList.stream().filter(ff -> ff.getId().equals(m.getCollectionId())).findFirst();
                if(cntCollection.isPresent()){
                    cntActionTar.setCollectionName(cntCollection.get().getCollectionName());
                }
                Optional<MediaVo> first = mediaVoList.stream().filter(fff -> fff.getBuiId().equals(m.getCollectionId())).findFirst();
                if(first.isPresent()){
                    cntActionTar.setCollectionImage(first.get().getMediaUrl());
                }
                cntActionTar.setCreatedBy(SecurityUtils.getUsername());
                cntActionTar.setCreatedTime(DateUtils.getNowDate());
                return cntActionTar;
            }).collect(Collectors.toList());
            saveBatch(list);
        }
        return 1;
    }

}
