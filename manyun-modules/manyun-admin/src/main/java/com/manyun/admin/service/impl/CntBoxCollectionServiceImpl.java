package com.manyun.admin.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.dto.SaveBoxCollectionDto;
import com.manyun.admin.domain.query.BoxCollectionQuery;
import com.manyun.admin.domain.redis.box.BoxCollectionJoinVo;
import com.manyun.admin.domain.redis.box.BoxListVo;
import com.manyun.admin.domain.redis.box.BoxVo;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.redis.domian.dto.BuiCronDto;
import com.manyun.common.redis.service.BuiCronService;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxCollectionMapper;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.service.ICntBoxCollectionService;
import org.springframework.transaction.annotation.Transactional;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;

/**
 * 盲盒与藏品中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-15
 */
@Service
public class CntBoxCollectionServiceImpl extends ServiceImpl<CntBoxCollectionMapper,CntBoxCollection> implements ICntBoxCollectionService
{
    @Autowired
    private CntBoxCollectionMapper cntBoxCollectionMapper;

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICntBoxService boxService;

    @Autowired
    private BuiCronService buiCronService;

    @Autowired
    private ICntMediaService mediaService;

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param boxCollectionQuery 盲盒与藏品中间
     * @return 盲盒与藏品中间
     */
    @Override
    public TableDataInfo<CntBoxCollectionVo> selectCntBoxCollectionList(BoxCollectionQuery boxCollectionQuery)
    {
        PageHelper.startPage(boxCollectionQuery.getPageNum(),boxCollectionQuery.getPageSize());
        List<CntBoxCollection> cntBoxCollections = cntBoxCollectionMapper.selectSearchBoxCollectionList(boxCollectionQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxCollections.parallelStream().map(m ->{
            CntBoxCollectionVo cntBoxCollectionVo=new CntBoxCollectionVo();
            BeanUtil.copyProperties(m,cntBoxCollectionVo);
            cntBoxCollectionVo.setBoxCollectionId(m.getId());
            return cntBoxCollectionVo;
        }).collect(Collectors.toList()),cntBoxCollections);
    }

    /**
     * 新增盲盒与藏品中间
     *
     * @param boxCollectionDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntBoxCollection(SaveBoxCollectionDto boxCollectionDto)
    {
        BoxVo boxVo = Builder.of(BoxVo::new).build();
        String boxId = boxCollectionDto.getBoxId();
        CntBox cntBox = boxService.getById(boxId);
        List<BoxCollectionJoinVo> boxCollectionJoinVos = new ArrayList<>();
        List<CntBoxCollectionVo> cntBoxCollectionVoList = boxCollectionDto.getCntBoxCollectionVos();
        Assert.isTrue(Objects.nonNull(cntBoxCollectionVoList),"新增失败!");
        Assert.isTrue(cntBoxCollectionVoList.size()>0,"请添加藏品!");
        BigDecimal tranSvgSum = cntBoxCollectionVoList.stream().map(CntBoxCollectionVo::getTranSvg).reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.isFalse(tranSvgSum.compareTo(BigDecimal.valueOf(100.00))==1,"概率总和不得超过100%!");
        Set<String> set = cntBoxCollectionVoList.parallelStream().map(CntBoxCollectionVo::getCollectionId).collect(Collectors.toSet());
        Assert.isTrue(set.size()==cntBoxCollectionVoList.size(),"所选藏品不可重复!");
        //删除不需要
        remove(Wrappers.<CntBoxCollection>lambdaQuery().notIn(CntBoxCollection::getId,cntBoxCollectionVoList.parallelStream().map(CntBoxCollectionVo::getBoxCollectionId).collect(Collectors.toList())).eq(CntBoxCollection::getBoxId,boxId));
        //组装需修改的和新增的集合 并执行
        List<CntBoxCollection> saveBatchList=new ArrayList<>();
        List<CntBoxCollection> updateBatchList=new ArrayList<>();
        List<String> collectionIds = cntBoxCollectionVoList.stream().map(CntBoxCollectionVo::getCollectionId).collect(Collectors.toList());
        List<CntCollection> collectionList = collectionService.listByIds(collectionIds);
        cntBoxCollectionVoList.parallelStream().forEach(e -> {
            if(StringUtils.isBlank(e.getBoxCollectionId()) || e.getBoxCollectionId()==null){
                CntBoxCollection cntBoxCollection = new CntBoxCollection();
                cntBoxCollection.setCollectionId(e.getCollectionId());
                cntBoxCollection.setTranSvg(e.getTranSvg());
                cntBoxCollection.setFlagScore(e.getFlagScore());
                cntBoxCollection.setSourcePrice(e.getSourcePrice());
                cntBoxCollection.setOpenQuantity(e.getOpenQuantity());
                cntBoxCollection.setId(IdUtils.getSnowflakeNextIdStr());
                cntBoxCollection.setBoxId(boxId);
                Optional<CntCollection> first = collectionList.stream().filter(ff -> ff.getId().equals(e.getCollectionId())).findFirst();
                if(first.isPresent()){
                    cntBoxCollection.setCollectionName(first.get().getCollectionName());
                }
                cntBoxCollection.setCreatedBy(SecurityUtils.getUsername());
                cntBoxCollection.setCreatedTime(DateUtils.getNowDate());
                saveBatchList.add(cntBoxCollection);

                BoxCollectionJoinVo boxCollectionJoinVo = new BoxCollectionJoinVo();
                BeanUtil.copyProperties(cntBoxCollection,boxCollectionJoinVo);
                boxCollectionJoinVos.add(boxCollectionJoinVo);
            }else {
                CntBoxCollection boxCollection = getOne(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxId).eq(CntBoxCollection::getId, e.getBoxCollectionId()));
                if(Objects.nonNull(boxCollection)){
                    BeanUtil.copyProperties(e,boxCollection);
                    Optional<CntCollection> first = collectionList.stream().filter(ff -> ff.getId().equals(e.getCollectionId())).findFirst();
                    if(first.isPresent()){
                        boxCollection.setCollectionName(first.get().getCollectionName());
                    }
                    boxCollection.setUpdatedBy(SecurityUtils.getUsername());
                    boxCollection.setUpdatedTime(DateUtils.getNowDate());
                    updateBatchList.add(boxCollection);
                    BoxCollectionJoinVo boxCollectionJoinVo = new BoxCollectionJoinVo();
                    BeanUtil.copyProperties(boxCollection,boxCollectionJoinVo);
                    boxCollectionJoinVos.add(boxCollectionJoinVo);
                }else {
                    CntBoxCollection cntBoxCollection = new CntBoxCollection();
                    cntBoxCollection.setCollectionId(e.getCollectionId());
                    cntBoxCollection.setTranSvg(e.getTranSvg());
                    cntBoxCollection.setFlagScore(e.getFlagScore());
                    cntBoxCollection.setSourcePrice(e.getSourcePrice());
                    cntBoxCollection.setOpenQuantity(e.getOpenQuantity());
                    cntBoxCollection.setId(IdUtils.getSnowflakeNextIdStr());
                    cntBoxCollection.setBoxId(boxId);
                    Optional<CntCollection> first = collectionList.stream().filter(ff -> ff.getId().equals(e.getCollectionId())).findFirst();
                    if(first.isPresent()){
                        cntBoxCollection.setCollectionName(first.get().getCollectionName());
                    }
                    cntBoxCollection.setCreatedBy(SecurityUtils.getUsername());
                    cntBoxCollection.setCreatedTime(DateUtils.getNowDate());
                    saveBatchList.add(cntBoxCollection);
                    BoxCollectionJoinVo boxCollectionJoinVo = new BoxCollectionJoinVo();
                    BeanUtil.copyProperties(boxCollection,boxCollectionJoinVo);
                    boxCollectionJoinVos.add(boxCollectionJoinVo);
                }
            }
        });
        if(saveBatchList.size()>0){
            saveBatch(saveBatchList);
        }
        if(updateBatchList.size()>0){
            updateBatchById(updateBatchList);
        }

        //更新redis
        if(cntBox.getStatusBy()!=null && cntBox.getStatusBy()==1){
            BoxListVo boxListVo = initBoxListVo(cntBox);
            boxVo.setBoxListVo(boxListVo);
            boxVo.setBoxCollectionJoinVos(boxCollectionJoinVos);
            redisService.setCacheMapValue(BusinessConstants.RedisDict.BOX_INFO,boxId,boxVo);
        }else {
            redisService.hashDelete(BusinessConstants.RedisDict.BOX_INFO,boxId);
        }
        return 1;
    }


    /**
     * 转义数据
     * @param cntBox
     * @return
     */
    private BoxListVo initBoxListVo(CntBox cntBox){
        BoxListVo boxListVo = Builder.of(BoxListVo::new).build();
        BeanUtil.copyProperties(cntBox,boxListVo);
        // 缓存库存数据隔离
        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(BOX_MODEL_TYPE, cntBox.getId());
        boxListVo.setBalance(typeBalanceCache.getBalance());
        boxListVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (DateUtils.toLocalDateTime(cntBox.getPublishTime()).isAfter(LocalDateTime.now())) {
            boxListVo.setPreStatus(1);
        } else {
            boxListVo.setPreStatus(2);
        }
        // 需要集成图片服务
        boxListVo.setMediaVos(mediaService.initMediaVos(cntBox.getId(), BOX_MODEL_TYPE));
        boxListVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(cntBox.getId(), BOX_MODEL_TYPE));
        boxListVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(cntBox.getId(), BOX_MODEL_TYPE));
        return boxListVo;
    }

}
