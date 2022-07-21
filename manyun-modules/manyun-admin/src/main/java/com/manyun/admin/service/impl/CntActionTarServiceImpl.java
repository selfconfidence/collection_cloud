package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.dto.SaveActionTarDto;
import com.manyun.admin.domain.vo.CntActionTarVo;
import com.manyun.admin.domain.vo.MediaVo;
import com.manyun.admin.mapper.CntCollectionMapper;
import com.manyun.admin.mapper.CntMediaMapper;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
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
public class CntActionTarServiceImpl implements ICntActionTarService
{
    @Autowired
    private CntActionTarMapper cntActionTarMapper;

    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    @Autowired
    private CntMediaMapper cntMediaMapper;

    /**
     * 查询活动合成附属信息列表
     *
     * @param cntActionTar 活动合成附属信息
     * @return 活动合成附属信息
     */
    @Override
    public List<CntActionTarVo> selectCntActionTarList(CntActionTar cntActionTar)
    {
        return cntActionTarMapper.selectCntActionTarList(cntActionTar).stream().map(m ->{
            CntActionTarVo cntActionTarVo=new CntActionTarVo();
            BeanUtil.copyProperties(m,cntActionTarVo);
            return cntActionTarVo;
        }).collect(Collectors.toList());
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
        cntActionTarMapper.deleteCntActionTarById(null,actionId);
        if(cntActionTarVos.size()>0){
            List<String> collectionIds = cntActionTarVos.stream().map(CntActionTarVo::getCollectionId).collect(Collectors.toList());
            List<CntCollection> collectionList = cntCollectionMapper.selectCntCollectionByIds(collectionIds);
            List<MediaVo> mediaVoList = cntMediaMapper.selectCntMediaByCollectionIds(collectionIds);
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
            cntActionTarMapper.insertCntActionTars(list);
        }
        return 1;
    }

}
