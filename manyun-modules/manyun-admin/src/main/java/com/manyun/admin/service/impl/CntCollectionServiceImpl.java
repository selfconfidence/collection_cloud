package com.manyun.admin.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.*;
import com.manyun.admin.domain.excel.BachAirdopExcel;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.admin.service.*;
import com.manyun.comm.api.MyChainxSystemService;
import com.manyun.comm.api.domain.redis.CreationdRedisVo;
import com.manyun.comm.api.domain.redis.MediaRedisVo;
import com.manyun.comm.api.domain.redis.collection.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
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
import org.springframework.transaction.annotation.Transactional;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.enums.BoxStatus.*;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

/**
 * ??????Service???????????????
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntCollectionServiceImpl extends ServiceImpl<CntCollectionMapper,CntCollection> implements ICntCollectionService
{
    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntCollectionInfoService collectionInfoService;

    @Autowired
    private ICntCollectionLableService collectionLableService;

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private MyChainxSystemService myChainxSystemService;

    @Autowired
    private ICnfIssuanceService issuanceService;

    @Autowired
    private ICntAirdropRecordService airdropRecordService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICntTarService cntTarService;

    @Autowired
    private ICnfCreationdService cntCreationdService;

    @Autowired
    private ICntCateService cateService;

    @Autowired
    private ICntLableService lableService;

    @Autowired
    private BuiCronService buiCronService;

    /**
     * ??????????????????
     *
     * @param id ????????????
     * @return ??????
     */
    @Override
    public CntCollectionDetailsVo selectCntCollectionById(String id)
    {
        CntCollectionDetailsVo cntCollectionDetailsVo = cntCollectionMapper.selectCntCollectionDetailsById(id);
        cntCollectionDetailsVo.setLableIds(collectionLableService.list(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, id)).stream().map(CntCollectionLable::getLableId).collect(Collectors.toList()));
        cntCollectionDetailsVo.setMediaVos(mediaService.initMediaVos(id, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        cntCollectionDetailsVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(id, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        cntCollectionDetailsVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(id, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        return cntCollectionDetailsVo;
    }

    /**
     * ??????????????????
     *
     * @param collectionQuery
     * @return ??????
     */
    @Override
    public TableDataInfo<CntCollectionVo> selectCntCollectionList(CollectionQuery collectionQuery)
    {
        PageHelper.startPage(collectionQuery.getPageNum(),collectionQuery.getPageSize());
        List<CntCollection> cntCollectionList = cntCollectionMapper.selectSearchCollectionList(collectionQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntCollectionList.parallelStream().map(m->{
            CntCollectionVo cntCollectionVo = new CntCollectionVo();
            BeanUtil.copyProperties(m, cntCollectionVo);
            cntCollectionVo.setTotalBalance(m.getBalance().intValue() + m.getSelfBalance().intValue());
            cntCollectionVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(m.getId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(COLLECTION_MODEL_TYPE, m.getId());
            cntCollectionVo.setRedisBalance(typeBalanceCache.getBalance());
            cntCollectionVo.setRedisSelfBalance(typeBalanceCache.getSelfBalance());
            return cntCollectionVo;
        }).collect(Collectors.toList()), cntCollectionList);
    }


    /**
     * ????????????
     *
     * @param collectionAlterCombineDto
     * @return ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //??????
        String idStr = IdUtils.getSnowflakeNextIdStr();
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo), "??????????????????");
        //?????????????????????????????????
        List<CntCollection> cntCollectionList = list(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getCollectionName, collectionAlterVo.getCollectionName()));
        String info = StrUtil.format("???????????????:{}?????????!", collectionAlterVo.getCollectionName());
        Assert.isFalse(cntCollectionList.size()>0,info);
        //??????????????????????????????????????????
        //???????????????????????????????????? = -1??? ?????? =0???????????? = 1
        Date publishTime = collectionAlterVo.getPublishTime();
        if(publishTime!=null){
            if (DateUtils.compareTo(new Date(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                return R.fail("????????????????????????????????????!");
            }
        }
        if(null == collectionAlterVo.getLimitNumber() || collectionAlterVo.getLimitNumber()==0){
            return R.fail("?????????????????????1!");
        }
        if(collectionAlterVo.getLimitNumber()>collectionAlterVo.getBalance()){
            return R.fail("????????????????????????????????????!");
        }
        //??????
        R check = check(collectionAlterVo,collectionAlterCombineDto.getCntLableAlterVo(),collectionAlterCombineDto.getMediaAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntCollection cntCollection = new CntCollection();
        BeanUtil.copyProperties(collectionAlterVo, cntCollection);
        cntCollection.setId(idStr);
        cntCollection.setCreatedBy(SecurityUtils.getUsername());
        cntCollection.setCreatedTime(DateUtils.getNowDate());
        if (!save(cntCollection)) {
            return R.fail();
        }

        //?????????
        String issuanceId = collectionAlterCombineDto.getIssuanceId();
        //????????????
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineDto.getCntCollectionInfoAlterVo();
        CntCollectionInfo cntCollectionInfo = new CntCollectionInfo();
        cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
        cntCollectionInfo.setCollectionId(idStr);
        cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
        cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
        if(Objects.nonNull(collectionInfoAlterVo)){
            BeanUtil.copyProperties(collectionInfoAlterVo, cntCollectionInfo);
        }
        if(StringUtils.isNotBlank(issuanceId)){
            CnfIssuance cnfIssuance = issuanceService.getById(issuanceId);
            if(Objects.nonNull(cnfIssuance)){
                cntCollectionInfo.setPublishId(issuanceId);
                cntCollectionInfo.setPublishOther(cnfIssuance.getPublishOther());
                cntCollectionInfo.setPublishAuther(cnfIssuance.getPublishAuther());
                cntCollectionInfo.setPublishInfo(cnfIssuance.getPublishInfo());
            }
        }
        collectionInfoService.save(cntCollectionInfo);

        //??????
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        List<CntCollectionLable> cntCollectionLables = new ArrayList<>();
        if (cntLableAlterVo!=null && cntLableAlterVo.getLableIds()!=null) {
            String[] lableIds = cntLableAlterVo.getLableIds();
            if (lableIds.length>0) {
                cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntCollectionLable::setCollectionId, idStr)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            }
        }
        //??????
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        List<CntMedia> mediaList = new ArrayList<>();
        List<MediaRedisVo> mediaVos = new ArrayList<>();
        List<MediaRedisVo> thumbnailImgMediaVos = new ArrayList<>();
        List<MediaRedisVo> threeDimensionalMediaVos = new ArrayList<>();
        if (Objects.nonNull(mediaAlterVo)) {
            if(StringUtils.isNotBlank(mediaAlterVo.getImg())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                mediaVos.add(mediaVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThumbnailImg())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThumbnailImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.THUMBNAIL_IMG)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                thumbnailImgMediaVos.add(mediaVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThreeDimensional())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThreeDimensional())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.GLB)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                threeDimensionalMediaVos.add(mediaVo);
            }
            if(mediaList.size()>0)mediaService.saveBatch(mediaList);
        }
        List<Integer> list = autoNum(collectionAlterCombineDto.getCntCollectionAlterVo().getBalance());

        redisService.setCacheList(BusinessConstants.RedisDict.COLLECTION_RANDOM_NUM.concat(cntCollectionInfo.getCollectionId()),list);

        //??????redis ???????????????????????????????????????????????????
        if(cntCollection.getStatusBy()!=null && cntCollection.getStatusBy()==1){
            CollectionAllRedisVo collectionAllVo = new CollectionAllRedisVo();
            //??????????????????
            CollectionRedisVo collectionVo = new CollectionRedisVo();
            collectionVo.setId(idStr);
            collectionVo.setCollectionName(cntCollection.getCollectionName());
            collectionVo.setMediaVos(mediaVos);
            collectionVo.setThumbnailImgMediaVos(thumbnailImgMediaVos);
            collectionVo.setThreeDimensionalMediaVos(threeDimensionalMediaVos);
            collectionVo.setCateVo(initCateVo(cntCollection.getCateId()));
            collectionVo.setCnfCreationdVo(initCnfCreationVo(cntCollection.getBindCreation()));
            collectionVo.setSourcePrice(cntCollection.getSourcePrice());
            collectionVo.setStatusBy(cntCollection.getStatusBy());
            collectionVo.setSelfBalance(cntCollection.getSelfBalance()==null?0:cntCollection.getSelfBalance());
            collectionVo.setBalance(cntCollection.getBalance()==null?0:cntCollection.getBalance());
            collectionVo.setRealPrice(cntCollection.getRealPrice());
            if(cntCollectionLables.size()>0){
                List<CntLable> lableList = lableService.list(Wrappers.<CntLable>lambdaQuery().in(CntLable::getId, cntCollectionLables.stream().map(item -> item.getLableId()).collect(Collectors.toList())));
                if(lableList.size()>0){
                    collectionVo.setLableVos(lableList.parallelStream().map(item -> {
                        LableRedisVo lableVo = Builder.of(LableRedisVo::new).build();
                        BeanUtil.copyProperties(item, lableVo);
                        return lableVo;
                    }).collect(Collectors.toList()));
                }
            }
            collectionVo.setPublishTime(DateUtils.toLocalDateTime(cntCollection.getPublishTime()));
            collectionVo.setCreatedTime(DateUtils.toLocalDateTime(cntCollection.getCreatedTime()));
            if (DateUtils.toLocalDateTime(cntCollection.getPublishTime()).isAfter(LocalDateTime.now())) {
                collectionVo.setPreStatus(1);
            } else {
                collectionVo.setPreStatus(2);
            }
            //??????????????????
            CollectionInfoRedisVo collectionInfoVo = new CollectionInfoRedisVo();
            BeanUtil.copyProperties(cntCollectionInfo,collectionInfoVo);
            collectionAllVo.setCollectionVo(collectionVo);
            collectionAllVo.setCollectionInfoVo(collectionInfoVo);
            redisService.setCacheMapValue(BusinessConstants.RedisDict.COLLECTION_INFO,idStr,collectionAllVo);
        }
        //?????????redis??????
        buiCronService.initBuiBalanceCache(COLLECTION_MODEL_TYPE,idStr,cntCollection.getBalance()==null?0:cntCollection.getBalance(),cntCollection.getSelfBalance()==null?0:cntCollection.getSelfBalance());
        return R.ok();
     }

    private List<Integer> autoNum(Integer total) {
         Integer[] array = new Integer[total];
         for (int i = 0; i < total; i++) {
             array[i] = i + 1;
         }
         List<Integer> list = new ArrayList<>();
         Collections.addAll(list,array);
         Collections.shuffle(list);
         return list;
     }

    /**
     * ????????????
     *
     * @param collectionAlterCombineDto
     * @return ??????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //??????
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo), "??????????????????");
        String collectionId = collectionAlterVo.getId();
        Assert.isTrue(StringUtils.isNotBlank(collectionId), "??????????????????");
        //?????????????????????????????????
        List<CntCollection> cntCollectionList = list(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getCollectionName, collectionAlterVo.getCollectionName()).ne(CntCollection::getId,collectionId));
        String info = StrUtil.format("???????????????:{}?????????!", collectionAlterVo.getCollectionName());
        Assert.isFalse(cntCollectionList.size()>0,info);
        //??????
        R check = check(collectionAlterVo,collectionAlterCombineDto.getCntLableAlterVo(),collectionAlterCombineDto.getMediaAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntCollection cntCollection = getById(collectionAlterVo.getId());
        Assert.isTrue(Objects.nonNull(cntCollection), "???????????????!");
        BeanUtil.copyProperties(collectionAlterVo, cntCollection);
        cntCollection.setUpdatedBy(SecurityUtils.getUsername());
        cntCollection.setUpdatedTime(DateUtils.getNowDate());
        if (!updateById(cntCollection)) {
            return R.fail();
        }

        //?????????
        String issuanceId = collectionAlterCombineDto.getIssuanceId();
        //????????????
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineDto.getCntCollectionInfoAlterVo();
        CntCollectionInfo cntCollectionInfo = new CntCollectionInfo();
        if (Objects.nonNull(collectionInfoAlterVo)) {
            List<CntCollectionInfo> cntCollectionInfos = collectionInfoService.list(Wrappers.<CntCollectionInfo>lambdaQuery().eq(CntCollectionInfo::getCollectionId, collectionId));
            CnfIssuance cnfIssuance = issuanceService.getById(issuanceId);
            if (cntCollectionInfos.size() == 0) {
                BeanUtil.copyProperties(collectionInfoAlterVo, cntCollectionInfo);
                cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
                cntCollectionInfo.setCollectionId(collectionId);
                if(Objects.nonNull(cnfIssuance)){
                    cntCollectionInfo.setPublishId(issuanceId);
                    cntCollectionInfo.setPublishOther(cnfIssuance.getPublishOther());
                    cntCollectionInfo.setPublishAuther(cnfIssuance.getPublishAuther());
                    cntCollectionInfo.setPublishInfo(cnfIssuance.getPublishInfo());
                }
                cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
                cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
                collectionInfoService.save(cntCollectionInfo);
            } else {
                BeanUtil.copyProperties(cntCollectionInfos.get(0), cntCollectionInfo);
                cntCollectionInfo.setId(cntCollectionInfos.get(0).getId());
                if(Objects.nonNull(cnfIssuance)){
                    cntCollectionInfo.setPublishId(issuanceId);
                    cntCollectionInfo.setPublishOther(cnfIssuance.getPublishOther());
                    cntCollectionInfo.setPublishAuther(cnfIssuance.getPublishAuther());
                }else {
                    cntCollectionInfo.setPublishId("");
                    cntCollectionInfo.setPublishOther("");
                    cntCollectionInfo.setPublishAuther("");
                    cntCollectionInfo.setPublishInfo("");
                }
                cntCollectionInfo.setLookInfo(collectionInfoAlterVo.getLookInfo());
                cntCollectionInfo.setUpdatedBy(SecurityUtils.getUsername());
                cntCollectionInfo.setUpdatedTime(DateUtils.getNowDate());
                collectionInfoService.updateById(cntCollectionInfo);
            }
        }
        //??????
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        String[] lableIds = cntLableAlterVo.getLableIds();
        List<CntCollectionLable> cntCollectionLables = new ArrayList<>();
        if (Objects.nonNull(collectionInfoAlterVo)) {
            if (lableIds.length>0) {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, collectionId));
                cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntCollectionLable::setCollectionId, collectionId)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            } else {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, collectionId));
            }
        }
        //??????
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        List<CntMedia> saveMediaList = new ArrayList<>();
        List<CntMedia> updateMediaList = new ArrayList<>();
        List<MediaRedisVo> mediaVos1 = new ArrayList<>(); //??????
        List<MediaRedisVo> mediaVos2 = new ArrayList<>(); //?????????
        List<MediaRedisVo> mediaVos3 = new ArrayList<>(); //glb
        if (Objects.nonNull(mediaAlterVo)) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(collectionId, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(collectionId, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            if(mediaVos.size()==0){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, collectionId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                mediaVos1.add(mediaVo);
            }else {
                    CntMedia cntMedia = Builder.of(CntMedia::new)
                            .with(CntMedia::setId, mediaVos.get(0).getId())
                            .with(CntMedia::setMediaUrl, StringUtils.isBlank(mediaAlterVo.getImg())==true?"":mediaAlterVo.getImg())
                            .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                            .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                            .build();
                    updateMediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                    BeanUtil.copyProperties(mediaVos.get(0),mediaVo);
                    mediaVo.setMediaUrl(StringUtils.isBlank(mediaAlterVo.getImg())==true?"":mediaAlterVo.getImg());
                    mediaVos1.add(mediaVo);
            }
            if(thumbnailImgMediaVos.size()==0){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, collectionId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThumbnailImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.THUMBNAIL_IMG)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                mediaVos2.add(mediaVo);
            }else {
                    CntMedia cntMedia = Builder.of(CntMedia::new)
                            .with(CntMedia::setId, thumbnailImgMediaVos.get(0).getId())
                            .with(CntMedia::setMediaUrl, StringUtils.isBlank(mediaAlterVo.getThumbnailImg())==true?"":mediaAlterVo.getThumbnailImg())
                            .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                            .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                            .build();
                    updateMediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                    BeanUtil.copyProperties(thumbnailImgMediaVos.get(0),mediaVo);
                    mediaVo.setMediaUrl(StringUtils.isBlank(mediaAlterVo.getThumbnailImg())==true?"":mediaAlterVo.getThumbnailImg());
                    mediaVos2.add(mediaVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThreeDimensional())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, collectionId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThreeDimensional())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.GLB)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                mediaVos3.add(mediaVo);
            }
            mediaService.remove(Wrappers.<CntMedia>lambdaQuery().eq(CntMedia::getBuiId,collectionId).eq(CntMedia::getModelType,BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE).eq(CntMedia::getMediaType,BusinessConstants.ModelTypeConstant.GLB));
            if(saveMediaList.size()>0)mediaService.saveBatch(saveMediaList);
            if(updateMediaList.size()>0)mediaService.updateBatchById(updateMediaList);
        }

        if(cntCollection.getStatusBy()!=null && cntCollection.getStatusBy()==1){
            //??????redis
            CollectionAllRedisVo collectionAllVo = new CollectionAllRedisVo();
            //??????????????????
            CollectionRedisVo collectionVo = new CollectionRedisVo();
            collectionVo.setId(collectionId);
            collectionVo.setCollectionName(cntCollection.getCollectionName());
            collectionVo.setMediaVos(mediaVos1);
            collectionVo.setThumbnailImgMediaVos(mediaVos2);
            collectionVo.setThreeDimensionalMediaVos(mediaVos3);
            collectionVo.setCateVo(initCateVo(cntCollection.getCateId()));
            collectionVo.setCnfCreationdVo(initCnfCreationVo(cntCollection.getBindCreation()));
            collectionVo.setSourcePrice(cntCollection.getSourcePrice());
            collectionVo.setStatusBy(cntCollection.getStatusBy());
            collectionVo.setSelfBalance(cntCollection.getSelfBalance()==null?0:cntCollection.getSelfBalance());
            collectionVo.setBalance(cntCollection.getBalance()==null?0:cntCollection.getBalance());
            collectionVo.setRealPrice(cntCollection.getRealPrice());
            if(cntCollectionLables.size()>0){
                List<CntLable> lableList = lableService.list(Wrappers.<CntLable>lambdaQuery().in(CntLable::getId, cntCollectionLables.stream().map(item -> item.getLableId()).collect(Collectors.toList())));
                if(lableList.size()>0){
                    collectionVo.setLableVos(lableList.parallelStream().map(item -> {
                        LableRedisVo lableVo = Builder.of(LableRedisVo::new).build();
                        BeanUtil.copyProperties(item, lableVo);
                        return lableVo;
                    }).collect(Collectors.toList()));
                }else {
                    collectionVo.setLableVos(new ArrayList<LableRedisVo>());
                }
            }else {
                collectionVo.setLableVos(new ArrayList<LableRedisVo>());
            }
            collectionVo.setPublishTime(DateUtils.toLocalDateTime(cntCollection.getPublishTime()));
            collectionVo.setCreatedTime(DateUtils.toLocalDateTime(cntCollection.getCreatedTime()));
            if (DateUtils.toLocalDateTime(cntCollection.getPublishTime()).isAfter(LocalDateTime.now())) {
                collectionVo.setPreStatus(1);
            } else {
                collectionVo.setPreStatus(2);
            }
            //??????????????????
            CollectionInfoRedisVo collectionInfoVo = new CollectionInfoRedisVo();
            BeanUtil.copyProperties(cntCollectionInfo,collectionInfoVo);
            collectionAllVo.setCollectionVo(collectionVo);
            collectionAllVo.setCollectionInfoVo(collectionInfoVo);
            redisService.setCacheMapValue(BusinessConstants.RedisDict.COLLECTION_INFO,collectionId,collectionAllVo);
        }else {
            redisService.hashDelete(BusinessConstants.RedisDict.COLLECTION_INFO,collectionId);
        }
        return R.ok();
    }


    public R check(CntCollectionAlterVo collectionAlterVo,CntLableAlterVo lableAlterVo ,MediaAlterVo mediaAlterVo){
        Date publishTime = collectionAlterVo.getPublishTime();
        String tarId = collectionAlterVo.getTarId();
        if(StringUtils.isNotBlank(tarId)){
            CntTar tar = cntTarService.getById(tarId);
            if(Objects.nonNull(tar)){
                //???????????????????????????????????? = -1??? ?????? =0???????????? = 1
                if(tar.getEndFlag()==1){
                    if (DateUtils.compareTo(tar.getOpenTime(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                        return R.fail("????????????????????????,???????????????: "+DateUtils.getDateToStr(tar.getOpenTime(),DateUtils.YYYY_MM_DD_HH_MM_SS)+" ????????????????????????????????????!");
                    }
                }
            }else {
                return R.fail("??????????????????????????????!");
            }

        }
        //???????????????????????????????????????
        Integer postTime = collectionAlterVo.getPostTime();
        if(postTime!=null){
            if(postTime<10 || postTime>1000){
                return R.fail("????????????????????????????????????10,??????1000?????????!");
            }
        }
        //??????????????????????????????
        if(lableAlterVo!=null && lableAlterVo.getLableIds()!=null){
            if(lableAlterVo.getLableIds().length>3){
                return R.fail("?????????????????????????????????!");
            }
        }
        //????????????
        if(Objects.isNull(mediaAlterVo) || StringUtils.isBlank(mediaAlterVo.getImg())){
            return R.fail("????????????????????????!");
        }
        return R.ok();
    }

    /***
     * ????????????
     * @param airdropBalanceDto
     * @return
     */
    @Override
    public R airdropBalance(AirdropBalanceDto airdropBalanceDto) {
        CntCollection collection = getById(airdropBalanceDto.getGoodsId());
        boolean balanceCache = buiCronService.doBuiDegressionBalanceCache(COLLECTION_MODEL_TYPE, airdropBalanceDto.getGoodsId(), airdropBalanceDto.getAirdropBalance());
        Assert.isTrue(balanceCache,"????????????????????????,????????????????????????!");
        collection.setAirdropBalance(airdropBalanceDto.getAirdropBalance());
        Assert.isTrue(updateById(collection),"????????????????????????!");
        //??????redis
        if(collection.getStatusBy()!=null && collection.getStatusBy()==1){
            CollectionAllRedisVo collectionAllVo = Builder.of(CollectionAllRedisVo::new).with(CollectionAllRedisVo::setCollectionVo, providerCollectionVo(collection)).with(CollectionAllRedisVo::setCollectionInfoVo, providerCollectionInfoVo(collection.getId())).build();
            redisService.setCacheMapValue(BusinessConstants.RedisDict.COLLECTION_INFO,collection.getId(),collectionAllVo);
        }
        return R.ok("????????????????????????!");
    }

    /***
     * ??????
     * @param airdropDto ??????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R airdrop(AirdropDto airdropDto) {
        //????????????
        List<CntUser> cntUsers = userService.list(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, airdropDto.getPhone()));
        CntCollection collection = getById(airdropDto.getCollectionId());
        Assert.isFalse(collection.getAirdropBalance()==0 && collection.getAirdropSelfBalance()==0,"????????????????????????!");
        String collectionId = collection.getId();
        Integer selfBalance = collection.getSelfBalance();
        Integer balance = collection.getBalance();
        if(cntUsers.size()==0 || Objects.isNull(collection) || cntUsers.get(0).getIsReal()==1 || Integer.valueOf(0)==collection.getBalance()){
            //??????????????????
            airdropRecordService.save(
                    Builder.of(CntAirdropRecord::new)
                            .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                            .with(CntAirdropRecord::setUserId,cntUsers.size()==0?"":cntUsers.get(0).getId())
                            .with(CntAirdropRecord::setNickName,cntUsers.size()==0?"":cntUsers.get(0).getNickName())
                            .with(CntAirdropRecord::setUserPhone,cntUsers.size()==0?airdropDto.getPhone():cntUsers.get(0).getPhone())
                            .with(CntAirdropRecord::setGoodsId,Objects.isNull(collection)==true?"":collection.getId())
                            .with(CntAirdropRecord::setGoodsName,Objects.isNull(collection)==true?"":collection.getCollectionName())
                            .with(CntAirdropRecord::setGoodsType,0)
                            .with(CntAirdropRecord::setDeliveryStatus,1)
                            .with(CntAirdropRecord::setDeliveryType,0)
                            .with(CntAirdropRecord::setDeliveryInfo,cntUsers.size()==0?"???????????????!":Objects.isNull(collection)==true?"???????????????!":cntUsers.get(0).getIsReal()==1?"?????????????????????!":(Integer.valueOf(0)==collection.getBalance())==true?"????????????!":"")
                            .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                            .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                            .build()
            );
            if(cntUsers.size() == 0){
                return R.fail("???????????????!");
            }

            if(Objects.isNull(collection)){
                return R.fail("???????????????!");
            }

            if(cntUsers.get(0).getIsReal()==1){
                return R.fail("?????????????????????,??????????????????!");
            }

            if(Integer.valueOf(0)==collection.getBalance()){
                return R.fail("????????????!");
            }
        }
        int rows = cntCollectionMapper.updateLock(collectionId, 1,1);
        if(rows==0){
            return R.fail("????????????!");
        }
        //??????????????????
        String idStr = IdUtils.getSnowflakeNextIdStr();
        userCollectionService.save(
                Builder
                        .of(CntUserCollection::new)
                        .with(CntUserCollection::setId, idStr)
                        .with(CntUserCollection::setUserId, cntUsers.get(0).getId())
                        .with(CntUserCollection::setCollectionId, collection.getId())
                        .with(CntUserCollection::setCollectionName, collection.getCollectionName())
                        .with(CntUserCollection::setLinkAddr, IdUtils.getSnowflake().nextIdStr())
                        .with(CntUserCollection::setSourceInfo, "??????")
                        .with(CntUserCollection::setIsExist,USE_EXIST.getCode())
                        .with(CntUserCollection::setIsLink,NOT_LINK.getCode())
                        .with(CntUserCollection::setCreatedBy,SecurityUtils.getUsername())
                        .with(CntUserCollection::setCreatedTime,DateUtils.getNowDate())
                        .build()
        );
        //??????????????????
        airdropRecordService.save(
                Builder.of(CntAirdropRecord::new)
                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                .with(CntAirdropRecord::setUserId,cntUsers.get(0).getId())
                .with(CntAirdropRecord::setNickName,cntUsers.get(0).getNickName())
                .with(CntAirdropRecord::setUserPhone,cntUsers.get(0).getPhone())
                .with(CntAirdropRecord::setGoodsId,Objects.isNull(collection)==true?"":collection.getId())
                .with(CntAirdropRecord::setGoodsName,Objects.isNull(collection)==true?"":collection.getCollectionName())
                .with(CntAirdropRecord::setGoodsType,0)
                .with(CntAirdropRecord::setDeliveryStatus,0)
                .with(CntAirdropRecord::setDeliveryType,0)
                .with(CntAirdropRecord::setDeliveryInfo,"??????????????????!")
                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                .build()
        );
        return R.ok(Builder.of(AirdropVo::new).with(AirdropVo::setUserId,cntUsers.get(0).getId()).with(AirdropVo::setUsercollectionId,idStr).build());
    }

    public static void main(String[] args) {
        Set<String> set=new HashSet<>();
        set.add("asdada");
        System.out.println(set.parallelStream().findFirst().get());
    }

    /**
     * ????????????
     * @param bachAirdopExcels ????????????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R postExcelList(List<BachAirdopExcel> bachAirdopExcels)
    {
        if (StringUtils.isNull(bachAirdopExcels) || bachAirdopExcels.size() == 0)
        {
           return R.fail("????????????????????????????????????!");
         }
        List<CntAirdropRecord> errorAirdropRecord = new ArrayList<CntAirdropRecord>();
        //????????????id????????????
        Set<String> collectionIds = bachAirdopExcels.parallelStream().map(BachAirdopExcel::getCollectionId).collect(Collectors.toSet());
        if(collectionIds.size()!=1){
            bachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getCollectionId())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"?????????????????????!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //??????????????????
            airdropRecordService.saveBatch(errorAirdropRecord);
            return R.fail("?????????????????????!");
        }
        //????????????
        List<CntUser> cntUsers = userService.list(
                Wrappers
                        .<CntUser>lambdaQuery().eq(CntUser::getIsReal,2)
                        .in(CntUser::getPhone, bachAirdopExcels.parallelStream()
                                .map(BachAirdopExcel::getPhone).collect(Collectors.toList()))
        );
        //????????????
        CntCollection cntCollections = getOne(
                Wrappers
                .<CntCollection>lambdaQuery()
                .eq(CntCollection::getId,collectionIds.stream().findFirst().get())
                .gt(CntCollection::getBalance, 0)
        );

        if(cntUsers.size()==0 || Objects.isNull(cntCollections)){
            bachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getCollectionId())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,cntUsers.size()==0?"?????????????????????????????????!":Objects.isNull(cntCollections)==true?"??????????????????????????????!":"")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //??????????????????
            airdropRecordService.saveBatch(errorAirdropRecord);

            if(cntUsers.size()==0){
                return R.fail("?????????????????????????????????!");
            }

            if(Objects.isNull(cntCollections)){
                return R.fail("??????????????????????????????!");
            }
        }

        Assert.isFalse(cntCollections.getAirdropBalance()==0 && cntCollections.getAirdropSelfBalance()==0,"????????????????????????!");

        //???????????????????????????
        HashSet<String> errorList = new HashSet<String>();
        List<CntUserCollection> successList = new ArrayList<CntUserCollection>();
        List<CntAirdropRecord> airdropRecordList = new ArrayList<CntAirdropRecord>();
        bachAirdopExcels.stream().forEach(e->{
            Optional<CntUser> user = cntUsers.parallelStream().filter(f -> f.getPhone().equals(e.getPhone())).findFirst();
            if(user.isPresent()){
                successList.add(
                        Builder
                                .of(CntUserCollection::new)
                                .with(CntUserCollection::setId, IdUtils.getSnowflakeNextIdStr())
                                .with(CntUserCollection::setUserId, user.get().getId())
                                .with(CntUserCollection::setCollectionId, cntCollections.getId())
                                .with(CntUserCollection::setCollectionName, cntCollections.getCollectionName())
                                .with(CntUserCollection::setLinkAddr, IdUtils.getSnowflake().nextIdStr())
                                .with(CntUserCollection::setSourceInfo, "????????????")
                                .with(CntUserCollection::setIsExist,USE_EXIST.getCode())
                                .with(CntUserCollection::setIsLink,NOT_LINK.getCode())
                                .with(CntUserCollection::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntUserCollection::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
                airdropRecordList.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserId,user.get().getId())
                                .with(CntAirdropRecord::setNickName,user.get().getNickName())
                                .with(CntAirdropRecord::setUserPhone,user.get().getPhone())
                                .with(CntAirdropRecord::setGoodsId,cntCollections.getId())
                                .with(CntAirdropRecord::setGoodsName,cntCollections.getCollectionName())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,0)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"??????????????????!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            }else {
                errorList.add(e.getPhone());
                airdropRecordList.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,cntCollections.getId())
                                .with(CntAirdropRecord::setGoodsName,cntCollections.getCollectionName())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"?????????????????????????????????!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            }
        });
        int rows = cntCollectionMapper.updateLock(cntCollections.getId(), successList.size(),successList.size());
        if(!(rows >=1)){
            bachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getCollectionId())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"????????????!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //??????????????????
            airdropRecordService.saveBatch(errorAirdropRecord);

            return R.fail("????????????!");
        }
        //??????????????????
        userCollectionService.saveBatch(successList);
        //??????????????????
        airdropRecordService.saveBatch(airdropRecordList);
        //????????????????????????
        List<MyChainxDto> myChainxDtos = successList.parallelStream().map(m -> {
            MyChainxDto myChainxDto = new MyChainxDto();
            myChainxDto.setUserId(m.getUserId());
            myChainxDto.setId(m.getId());
            return myChainxDto;
        }).collect(Collectors.toList());
        return R.ok(myChainxDtos,(errorList.size()>0?"??????????????????:"+successList.size()+",????????????:"+errorList.size()+",???????????????????????????:"+StringUtils.join(errorList,","):"??????????????????:"+successList.size()+",????????????:"+errorList.size()));
    }

    /**
     * ????????????
     * @param collectionStateDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateState(CollectionStateDto collectionStateDto) {
        CntCollection collection = getById(collectionStateDto.getId());
        collection.setStatusBy(collectionStateDto.getStatusBy());
        collection.setUpdatedBy(SecurityUtils.getUsername());
        collection.setUpdatedTime(DateUtils.getNowDate());
        //??????redis
        if(collectionStateDto.getStatusBy()!=null && collectionStateDto.getStatusBy()==1){
            CollectionAllRedisVo collectionAllVo = Builder.of(CollectionAllRedisVo::new).with(CollectionAllRedisVo::setCollectionVo, providerCollectionVo(collection)).with(CollectionAllRedisVo::setCollectionInfoVo, providerCollectionInfoVo(collectionStateDto.getId())).build();
            redisService.setCacheMapValue(BusinessConstants.RedisDict.COLLECTION_INFO,collectionStateDto.getId(),collectionAllVo);
        }else {
            redisService.hashDelete(BusinessConstants.RedisDict.COLLECTION_INFO,collection.getId());
        }
        return updateById(collection)==true?1:0;
    }

    private CollectionRedisVo providerCollectionVo(CntCollection collection){
        CollectionRedisVo collectionVo = Builder.of(CollectionRedisVo::new).build();
        BeanUtil.copyProperties(collection,collectionVo);
        // ????????????
        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(COLLECTION_MODEL_TYPE, collection.getId());
        collectionVo.setBalance(typeBalanceCache.getBalance());
        collectionVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (DateUtils.toLocalDateTime(collection.getPublishTime()).isAfter(LocalDateTime.now())) {
            collectionVo.setPreStatus(1);
        } else {
            collectionVo.setPreStatus(2);
        }
        if (Integer.valueOf(0).equals(collection.getBalance())) {
            collectionVo.setStatusBy(2);
        }
        collectionVo.setLableVos(initLableVos(collection.getId()));
        collectionVo.setMediaVos(initMediaVos(collection.getId()));
        collectionVo.setThumbnailImgMediaVos(thumbnailImgMediaVos(collection.getId()));
        collectionVo.setThreeDimensionalMediaVos(threeDimensionalMediaVos(collection.getId()));
        collectionVo.setCnfCreationdVo(initCnfCreationVo(collection.getBindCreation()));
        collectionVo.setCateVo(initCateVo(collection.getCateId()));
        return collectionVo;
    }

    /**
     * ??????????????????  ???????????????????????????
     * @param collectionId
     * @return
     */
    private CollectionInfoRedisVo providerCollectionInfoVo(String collectionId) {
        CollectionInfoRedisVo collectionInfoVo = Builder.of(CollectionInfoRedisVo::new).build();
        CntCollectionInfo collectionInfo = collectionInfoService.getOne(Wrappers.<CntCollectionInfo>lambdaQuery().eq(CntCollectionInfo::getCollectionId, collectionId));
        BeanUtil.copyProperties(collectionInfo,collectionInfoVo);
        return collectionInfoVo;
    }

    /**
     * ?????????????????????.??????????????????
     * @param bindCreationId
     * @return
     */
    private CreationdRedisVo initCnfCreationVo(String bindCreationId) {
        CnfCreationd cnfCreationd = cntCreationdService.getById(bindCreationId);
        CreationdRedisVo creationdVo = Builder.of(CreationdRedisVo::new).build();
        BeanUtil.copyProperties(cnfCreationd,creationdVo);
        return creationdVo;
    }

    /**
     * ?????? ?????? ???????????? ????????????
     * @param cateId
     * @return
     */
    private CateRedisVo initCateVo(String cateId) {
        CateRedisVo cateVo =  Builder.of(CateRedisVo::new).build();
        CntCate cntCate = cateService.getById(cateId);
        BeanUtil.copyProperties(cntCate,cateVo);
        return cateVo;
    }

    /**
     * ??????????????????  ???????????????????????????
     * @param collectionId
     * @return
     */
    private List<LableRedisVo> initLableVos(String collectionId) {
        List<CntCollectionLable> collectionLables = collectionLableService.list(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, collectionId));
        if (collectionLables.isEmpty())return ListUtil.empty();

        List<CntLable> lableList = lableService.list(Wrappers.<CntLable>lambdaQuery().in(CntLable::getId, collectionLables.stream().map(item -> item.getLableId()).collect(Collectors.toList())));
        if (lableList.isEmpty())return ListUtil.empty();

        return lableList.parallelStream().map(item -> {
            LableRedisVo lableVo = Builder.of(LableRedisVo::new).build();
            BeanUtil.copyProperties(item, lableVo);
            return lableVo;
        }).collect(Collectors.toList());
    }

    /**
     * ?????????????????? ????????????????????????????????????
     * @param collectionId
     * @return
     */
    private List<MediaRedisVo> initMediaVos(String collectionId) {
        return  mediaService.initMediaVos(collectionId, COLLECTION_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList());
    }

    /**
     * ?????????????????? ?????????
     * @param collectionId
     * @return
     */
    private List<MediaRedisVo> thumbnailImgMediaVos(String collectionId) {
        return  mediaService.thumbnailImgMediaVos(collectionId, COLLECTION_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList());
    }

    /**
     * ?????????????????? 3D???
     * @param collectionId
     * @return
     */
    private List<MediaRedisVo> threeDimensionalMediaVos(String collectionId) {
        return  mediaService.threeDimensionalMediaVos(collectionId, COLLECTION_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskCheckStatus(){
        update(Wrappers.<CntCollection>lambdaUpdate().ne(CntCollection::getStatusBy, DOWN_ACTION.getCode()).eq(CntCollection::getBalance, Integer.valueOf(0)).set(CntCollection::getStatusBy, NULL_ACTION.getCode()));

    }

}
