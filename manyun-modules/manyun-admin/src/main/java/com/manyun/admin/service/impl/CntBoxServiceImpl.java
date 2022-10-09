package com.manyun.admin.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.BoxAirdropDto;
import com.manyun.admin.domain.dto.BoxStateDto;
import com.manyun.admin.domain.dto.CntBoxAlterCombineDto;
import com.manyun.admin.domain.excel.BoxBachAirdopExcel;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.*;
import com.manyun.comm.api.domain.redis.MediaRedisVo;
import com.manyun.comm.api.domain.redis.box.BoxCollectionJoinRedisVo;
import com.manyun.comm.api.domain.redis.box.BoxListRedisVo;
import com.manyun.comm.api.domain.redis.box.BoxRedisVo;
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
import com.manyun.admin.mapper.CntBoxMapper;
import org.springframework.transaction.annotation.Transactional;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.enums.BoxStatus.*;


/**
 * 盲盒;盲盒主体Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CntBoxServiceImpl extends ServiceImpl<CntBoxMapper,CntBox> implements ICntBoxService
{
    @Autowired
    private CntBoxMapper cntBoxMapper;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntBoxCollectionService boxCollectionService;

    @Autowired
    private ICntCollectionLableService collectionLableService;

    @Autowired
    private ICntTarService cntTarService;

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntAirdropRecordService airdropRecordService;

    @Autowired
    private ICntUserBoxService userBoxService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICntBoxScoreService boxScoreService;

    @Autowired
    private BuiCronService buiCronService;

    /**
     * 查询盲盒;盲盒主体详情
     *
     * @param id 盲盒;盲盒主体主键
     * @return 盲盒;盲盒主体
     */
    @Override
    public CntBoxDetailsVo selectCntBoxById(String id)
    {
        CntBoxDetailsVo boxDetailsVo = Builder.of(CntBoxDetailsVo::new).build();
        CntBox cntBox = getById(id);
        BeanUtil.copyProperties(cntBox, boxDetailsVo);
        boxDetailsVo.setLableIds(collectionLableService.list(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, id)).stream().map(CntCollectionLable::getLableId).collect(Collectors.toList()));
        boxDetailsVo.setMediaVos(mediaService.initMediaVos(cntBox.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        boxDetailsVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(cntBox.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        boxDetailsVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(cntBox.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        return boxDetailsVo;
    }

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param boxQuery 盲盒;盲盒主体
     * @return 盲盒;盲盒主体
     */
    @Override
    public TableDataInfo<CntBoxVo> selectCntBoxList(BoxQuery boxQuery)
    {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<CntBox> cntBoxList = cntBoxMapper.selectSearchBoxList(boxQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxList.parallelStream().map(item -> {
                    CntBoxVo cntBoxVo = new CntBoxVo();
                    BeanUtil.copyProperties(item, cntBoxVo);
                    cntBoxVo.setTotalBalance(item.getBalance().intValue() + item.getSelfBalance().intValue());
                    cntBoxVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(item.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
                    BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(BOX_MODEL_TYPE, item.getId());
                    cntBoxVo.setRedisBalance(typeBalanceCache.getBalance());
                    cntBoxVo.setRedisSelfBalance(typeBalanceCache.getSelfBalance());
                    return cntBoxVo;
                }).collect(Collectors.toList()),cntBoxList);
    }

    /**
     * 新增盲盒;盲盒主体
     *
     * @param
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertCntBox(CntBoxAlterCombineDto boxAlterCombineDto)
    {
        //盲盒
        String idStr = IdUtils.getSnowflake().nextIdStr();
        CntBoxAlterVo cntBoxAlterVo = boxAlterCombineDto.getCntBoxAlterVo();
        Assert.isTrue(Objects.nonNull(cntBoxAlterVo), "新增盲盒失败");
        //验证盲盒名称是否已录入
        List<CntBox> boxList = list(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getBoxTitle,cntBoxAlterVo.getBoxTitle()));
        String info = StrUtil.format("盲盒名称为:{}已存在!", cntBoxAlterVo.getBoxTitle());
        Assert.isFalse(boxList.size()>0,info);
        //验证盲盒如果没有添加藏品不能上架
        Assert.isFalse(cntBoxAlterVo.getStatusBy()==1,"未添加盲盒中的藏品,不可上架该盲盒!");
        //验证发售时间是否小于当前时间
        //比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
        Date publishTime = cntBoxAlterVo.getPublishTime();
        if(publishTime!=null){
            if (DateUtils.compareTo(new Date(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                return R.fail("发售时间不能小于当前时间!");
            }
        }
        //校验
        R check = check(cntBoxAlterVo,boxAlterCombineDto.getCntLableAlterVo(),boxAlterCombineDto.getMediaAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntBox cntBox =new CntBox();
        BeanUtil.copyProperties(cntBoxAlterVo, cntBox);
        cntBox.setId(idStr);
        cntBox.setBoxOpen(1);
        cntBox.setCreatedBy(SecurityUtils.getUsername());
        cntBox.setCreatedTime(DateUtils.getNowDate());
        if (!save(cntBox)) {
            return R.fail();
        }
        //标签
        CntLableAlterVo cntLableAlterVo = boxAlterCombineDto.getCntLableAlterVo();
        if (cntLableAlterVo!=null && cntLableAlterVo.getLableIds()!=null) {
            String[] lableIds = cntLableAlterVo.getLableIds();
            if (lableIds.length>0) {
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflake().nextIdStr())
                            .with(CntCollectionLable::setCollectionId, idStr)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = boxAlterCombineDto.getMediaAlterVo();
        List<CntMedia> mediaList = new ArrayList<>();
        List<MediaVo> mediaVos = new ArrayList<>();
        List<MediaVo> thumbnailImgMediaVos = new ArrayList<>();
        List<MediaVo> threeDimensionalMediaVos = new ArrayList<>();
        if (Objects.nonNull(mediaAlterVo)) {
            if(StringUtils.isNotBlank(mediaAlterVo.getImg())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaVo mediaVo = new MediaVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                mediaVos.add(mediaVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThumbnailImg())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThumbnailImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.THUMBNAIL_IMG)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaVo mediaVo = new MediaVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                thumbnailImgMediaVos.add(mediaVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThreeDimensional())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntMedia::setBuiId, idStr)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThreeDimensional())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.GLB)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                mediaList.add(cntMedia);
                MediaVo mediaVo = new MediaVo();
                BeanUtil.copyProperties(cntMedia,mediaVo);
                threeDimensionalMediaVos.add(mediaVo);
            }
            if(mediaList.size()>0)mediaService.saveBatch(mediaList);
        }
        //初始化redis库存
        buiCronService.initBuiBalanceCache(BOX_MODEL_TYPE,idStr,cntBox.getBalance()==null?0:cntBox.getBalance(),cntBox.getSelfBalance()==null?0:cntBox.getSelfBalance());
        return R.ok();
    }


    /**
     * 修改盲盒;盲盒主体
     *
     * @param boxAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateCntBox(CntBoxAlterCombineDto boxAlterCombineDto)
    {
        //盲盒
        CntBoxAlterVo boxAlterVo = boxAlterCombineDto.getCntBoxAlterVo();
        Assert.isTrue(Objects.nonNull(boxAlterVo), "修改盲盒失败");
        String boxId = boxAlterVo.getId();
        Assert.isTrue(StringUtils.isNotBlank(boxId), "缺失必要参数");
        //验证盲盒名称是否已录入
        List<CntBox> boxList = list(Wrappers.<CntBox>lambdaQuery().eq(CntBox::getBoxTitle,boxAlterVo.getBoxTitle()).ne(CntBox::getId,boxId));
        String info = StrUtil.format("盲盒名称为:{}已存在!", boxAlterVo.getBoxTitle());
        Assert.isFalse(boxList.size()>0,info);
        //验证盲盒如果没有添加藏品不能上架
        if(boxAlterVo.getStatusBy()==1){
            List<CntBoxCollection> boxCollections = boxCollectionService.list(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxId));
            Assert.isTrue(boxCollections.size()>0, "未添加盲盒中的藏品,不可上架该盲盒!");
        }
        //校验
        R check = check(boxAlterVo,boxAlterCombineDto.getCntLableAlterVo(),boxAlterCombineDto.getMediaAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntBox cntBox = new CntBox();
        BeanUtil.copyProperties(boxAlterVo, cntBox);
        cntBox.setUpdatedBy(SecurityUtils.getUsername());
        cntBox.setUpdatedTime(DateUtils.getNowDate());
        if (!updateById(cntBox)) {
            return R.fail();
        }
        //标签
        CntLableAlterVo cntLableAlterVo = boxAlterCombineDto.getCntLableAlterVo();
        String[] lableIds = cntLableAlterVo.getLableIds();
        if (Objects.nonNull(cntLableAlterVo)) {
            if (lableIds.length>0) {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, boxId));
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
                    return Builder.of(CntCollectionLable::new)
                            .with(CntCollectionLable::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntCollectionLable::setCollectionId, boxId)
                            .with(CntCollectionLable::setLableId, m)
                            .with(CntCollectionLable::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntCollectionLable::setCreatedTime, DateUtils.getNowDate()).build();
                }).collect(Collectors.toList());
                collectionLableService.saveBatch(cntCollectionLables);
            } else {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, boxId));
            }
        }
        //图片
        MediaAlterVo mediaAlterVo = boxAlterCombineDto.getMediaAlterVo();
        List<CntMedia> saveMediaList = new ArrayList<>();
        List<CntMedia> updateMediaList = new ArrayList<>();
        List<MediaRedisVo> mediaVos1 = new ArrayList<>(); //主图
        List<MediaRedisVo> mediaVos2 = new ArrayList<>(); //缩略图
        List<MediaRedisVo> mediaVos3 = new ArrayList<>(); //glb
        if (Objects.nonNull(mediaAlterVo)) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(boxId, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
            List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(boxId, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);

            if (mediaVos.size() == 0) {
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                        .with(CntMedia::setBuiId, boxId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE.toString())
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaRedisVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaRedisVo);
                mediaVos1.add(mediaRedisVo);
            } else {
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, mediaVos.get(0).getId())
                        .with(CntMedia::setMediaUrl, StringUtils.isBlank(mediaAlterVo.getImg()) == true ? "" : mediaAlterVo.getImg())
                        .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                        .build();
                updateMediaList.add(cntMedia);
                MediaRedisVo mediaRedisVo = new MediaRedisVo();
                BeanUtil.copyProperties(mediaVos.get(0),mediaRedisVo);
                mediaRedisVo.setMediaUrl(StringUtils.isBlank(mediaAlterVo.getImg()) == true ? "" : mediaAlterVo.getImg());
                mediaVos1.add(mediaRedisVo);
            }
            if(thumbnailImgMediaVos.size()==0){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntMedia::setBuiId, boxId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThumbnailImg())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.THUMBNAIL_IMG)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaRedisVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaRedisVo);
                mediaVos2.add(mediaRedisVo);
            }else {
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, thumbnailImgMediaVos.get(0).getId())
                        .with(CntMedia::setMediaUrl, StringUtils.isBlank(mediaAlterVo.getThumbnailImg())==true?"":mediaAlterVo.getThumbnailImg())
                        .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                        .build();
                updateMediaList.add(cntMedia);
                MediaRedisVo mediaRedisVo = new MediaRedisVo();
                BeanUtil.copyProperties(thumbnailImgMediaVos.get(0),mediaRedisVo);
                mediaRedisVo.setMediaUrl(StringUtils.isBlank(mediaAlterVo.getThumbnailImg())==true?"":mediaAlterVo.getThumbnailImg());
                mediaVos2.add(mediaRedisVo);
            }
            if(StringUtils.isNotBlank(mediaAlterVo.getThreeDimensional())){
                CntMedia cntMedia = Builder.of(CntMedia::new)
                        .with(CntMedia::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntMedia::setBuiId, boxId)
                        .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE)
                        .with(CntMedia::setMediaUrl, mediaAlterVo.getThreeDimensional())
                        .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.GLB)
                        .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build();
                saveMediaList.add(cntMedia);
                MediaRedisVo mediaRedisVo = new MediaRedisVo();
                BeanUtil.copyProperties(cntMedia,mediaRedisVo);
                mediaVos3.add(mediaRedisVo);
            }
            mediaService.remove(Wrappers.<CntMedia>lambdaQuery().eq(CntMedia::getBuiId,boxId).eq(CntMedia::getModelType,BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE).eq(CntMedia::getMediaType,BusinessConstants.ModelTypeConstant.GLB));
            if(saveMediaList.size()>0)mediaService.saveBatch(saveMediaList);
            if(updateMediaList.size()>0)mediaService.updateBatchById(updateMediaList);
        }

       if(cntBox.getStatusBy()!=null && cntBox.getStatusBy()==1){
           //存储redis
           BoxRedisVo boxRedisVo = new BoxRedisVo();
           //盲盒基础信息
           BoxListRedisVo boxListRedisVo = new BoxListRedisVo();
           boxListRedisVo.setId(boxId);
           boxListRedisVo.setBoxTitle(cntBox.getBoxTitle());
           boxListRedisVo.setSelfBalance(cntBox.getSelfBalance()==null?0:cntBox.getSelfBalance());
           boxListRedisVo.setBalance(cntBox.getBalance()==null?0:cntBox.getBalance());
           boxListRedisVo.setStatusBy(cntBox.getStatusBy());
           boxListRedisVo.setRealPrice(cntBox.getRealPrice());
           boxListRedisVo.setSourcePrice(cntBox.getSourcePrice());
           boxListRedisVo.setMediaVos(mediaVos1);
           boxListRedisVo.setThumbnailImgMediaVos(mediaVos2);
           boxListRedisVo.setThreeDimensionalMediaVos(mediaVos3);
           boxListRedisVo.setBoxInfo(cntBox.getBoxInfo());
           boxListRedisVo.setPublishTime(DateUtils.toLocalDateTime(cntBox.getPublishTime()));
           boxListRedisVo.setCreatedTime(DateUtils.toLocalDateTime(cntBox.getCreatedTime()));
           if (DateUtils.toLocalDateTime(cntBox.getPublishTime()).isAfter(LocalDateTime.now())) {
               boxListRedisVo.setPreStatus(1);
           } else {
               boxListRedisVo.setPreStatus(2);
           }
           boxRedisVo.setBoxListVo(boxListRedisVo);
           boxRedisVo.setBoxCollectionJoinVos(findJoinCollections(boxId));
           redisService.setCacheMapValue(BusinessConstants.RedisDict.BOX_INFO,boxId,boxRedisVo);
       }else {
           redisService.hashDelete(BusinessConstants.RedisDict.BOX_INFO,boxId);
       }
        return R.ok();
    }

    public R check(CntBoxAlterVo boxAlterVo, CntLableAlterVo lableAlterVo, MediaAlterVo mediaAlterVo){
        Date publishTime = boxAlterVo.getPublishTime();
        String tarId = boxAlterVo.getTarId();
        if(StringUtils.isNotBlank(tarId)){
            CntTar tar = cntTarService.getById(tarId);
            if(Objects.nonNull(tar)){
                //比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
                if(tar.getEndFlag()==1){
                    if (DateUtils.compareTo(tar.getOpenTime(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                        return R.fail("改藏品已设置抽签,开奖时间为: "+DateUtils.getDateToStr(tar.getOpenTime(),DateUtils.YYYY_MM_DD_HH_MM_SS)+" 发售时间不能小于开奖时间!");
                    }
                }
            }else {
                return R.fail("未获取到抽签规则信息!");
            }

        }
        //验证提前购分钟是否在范围内
        Integer postTime = boxAlterVo.getPostTime();
        if(postTime!=null){
            if(postTime<10 || postTime>1000){
                return R.fail("提前购时间请输入大于等于10,小于1000的整数!");
            }
        }
        //验证标签是否超过三个
        if(lableAlterVo!=null && lableAlterVo.getLableIds()!=null){
            if(lableAlterVo.getLableIds().length>3){
                return R.fail("藏品标签最多可选中三个!");
            }
        }
        //验证图片
        if(Objects.isNull(mediaAlterVo) || StringUtils.isBlank(mediaAlterVo.getImg())){
            return R.fail("盲盒主图不能为空!");
        }
        return R.ok();
    }


    /**
     * 修改状态
     * @param boxStateDto
     * @return
     */
    @Override
    public int updateState(BoxStateDto boxStateDto) {
        CntBox cntBox = getById(boxStateDto.getId());
        cntBox.setStatusBy(boxStateDto.getStatusBy());
        cntBox.setUpdatedBy(SecurityUtils.getUsername());
        cntBox.setUpdatedTime(DateUtils.getNowDate());
        //验证盲盒如果没有添加藏品不能上架
        if(boxStateDto.getStatusBy()!=null && boxStateDto.getStatusBy()==1){
            List<CntBoxCollection> boxCollections = boxCollectionService.list(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxStateDto.getId()));
            Assert.isTrue(boxCollections.size()>0, "未添加盲盒中的藏品,不可上架该盲盒!");

            //更新redis
            BoxListRedisVo boxListRedisVo = initBoxListVo(cntBox);
            BoxRedisVo boxRedisVo = Builder.of(BoxRedisVo::new).build();
            boxRedisVo.setBoxListVo(boxListRedisVo);
            // 组合与藏品关联数据
            boxRedisVo.setBoxCollectionJoinVos(findJoinCollections(cntBox.getId()));
            redisService.setCacheMapValue(BusinessConstants.RedisDict.BOX_INFO,boxStateDto.getId(),boxRedisVo);
        }else {
            redisService.hashDelete(BusinessConstants.RedisDict.BOX_INFO,cntBox.getId());
        }
        return updateById(cntBox)==true?1:0;
    }


    /**
     * 查询盲盒订单列表
     */
    @Override
    public TableDataInfo<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery)
    {
        PageHelper.startPage(orderQuery.getPageNum(),orderQuery.getPageSize());
        List<CntBoxOrderVo> cntBoxOrderVos = cntBoxMapper.boxOrderList(orderQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxOrderVos,cntBoxOrderVos);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taskCheckStatus(){
        update(Wrappers.<CntBox>lambdaUpdate().ne(CntBox::getStatusBy, DOWN_ACTION.getCode()).eq(CntBox::getBalance, Integer.valueOf(0)).set(CntBox::getStatusBy, NULL_ACTION.getCode()));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R airdrop(BoxAirdropDto boxAirdropDto) {
        //验证用户
        List<CntUser> cntUsers = userService.list(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, boxAirdropDto.getPhone()));
        List<CntBoxCollection> boxCollectionList = boxCollectionService.list(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxAirdropDto.getBoxId()));
        CntBox box = getById(boxAirdropDto.getBoxId());
        String boxId = box.getId();
        Integer selfBalance = box.getSelfBalance();
        Integer balance = box.getBalance();
        if(cntUsers.size()==0 || Objects.isNull(box) || cntUsers.get(0).getIsReal()==1 || Integer.valueOf(0)==balance || boxCollectionList.size()==0){
            //增加空投记录
            airdropRecordService.save(
                    Builder.of(CntAirdropRecord::new)
                            .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                            .with(CntAirdropRecord::setUserId,cntUsers.size()==0?"":cntUsers.get(0).getId())
                            .with(CntAirdropRecord::setNickName,cntUsers.size()==0?"":cntUsers.get(0).getNickName())
                            .with(CntAirdropRecord::setUserPhone,cntUsers.size()==0?boxAirdropDto.getPhone():cntUsers.get(0).getPhone())
                            .with(CntAirdropRecord::setGoodsId,Objects.isNull(box)==true?"":box.getId())
                            .with(CntAirdropRecord::setGoodsName,Objects.isNull(box)==true?"":box.getBoxTitle())
                            .with(CntAirdropRecord::setGoodsType,1)
                            .with(CntAirdropRecord::setDeliveryStatus,1)
                            .with(CntAirdropRecord::setDeliveryType,0)
                            .with(CntAirdropRecord::setDeliveryInfo,cntUsers.size()==0?"用户不存在!":Objects.isNull(box)==true?"盲盒不存在!":cntUsers.get(0).getIsReal()==1?"当前用户未实名!":(Integer.valueOf(0)==balance)==true?"库存不足!":boxCollectionList.size()==0?"该盲盒未添加藏品!":"")
                            .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                            .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                            .build()
            );
            if(cntUsers.size() == 0){
                return R.fail("用户不存在!");
            }

            if(Objects.isNull(box)){
                return R.fail("盲盒不存在!");
            }

            if(boxCollectionList.size()==0){
                return R.fail("该盲盒未添加藏品!");
            }

            if(cntUsers.get(0).getIsReal()==1){
                return R.fail("当前用户未实名,请先实名认证!");
            }

            if(Integer.valueOf(0)==balance){
                return R.fail("库存不足!");
            }
        }

        //扣减库存
        updateById(
                Builder
                        .of(CntBox::new)
                        .with(CntBox::setId, boxId)
                        .with(CntBox::setSelfBalance, (selfBalance + 1))
                        .with(CntBox::setBalance, (balance - 1))
                        .build()
        );

        //增加用户盲盒
        String idStr = IdUtils.getSnowflakeNextIdStr();
        userBoxService.save(
                Builder
                        .of(CntUserBox::new)
                        .with(CntUserBox::setId, idStr)
                        .with(CntUserBox::setBoxTitle, box.getBoxTitle())
                        .with(CntUserBox::setBoxOpen, Long.valueOf(1))
                        .with(CntUserBox::setUserId, cntUsers.get(0).getId())
                        .with(CntUserBox::setBoxId, box.getId())
                        .with(CntUserBox::setSourceInfo, "空投!")
                        .with(CntUserBox::setCreatedBy, SecurityUtils.getUsername())
                        .with(CntUserBox::setCreatedTime, DateUtils.getNowDate())
                        .build()
        );
        //增加空投记录
        airdropRecordService.save(
                Builder.of(CntAirdropRecord::new)
                        .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                        .with(CntAirdropRecord::setUserId,cntUsers.get(0).getId())
                        .with(CntAirdropRecord::setNickName,cntUsers.get(0).getNickName())
                        .with(CntAirdropRecord::setUserPhone,cntUsers.get(0).getPhone())
                        .with(CntAirdropRecord::setGoodsId,Objects.isNull(box)==true?"":box.getId())
                        .with(CntAirdropRecord::setGoodsName,Objects.isNull(box)==true?"":box.getBoxTitle())
                        .with(CntAirdropRecord::setGoodsType,1)
                        .with(CntAirdropRecord::setDeliveryStatus,0)
                        .with(CntAirdropRecord::setDeliveryType,0)
                        .with(CntAirdropRecord::setDeliveryInfo,"投递盲盒成功!")
                        .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                        .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                        .build()
        );
        return R.ok();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R postExcelList(List<BoxBachAirdopExcel> boxBachAirdopExcels)
    {
        if (StringUtils.isNull(boxBachAirdopExcels) || boxBachAirdopExcels.size() == 0)
        {
            return R.fail("导入批量空投数据不能为空!");
        }
        List<CntAirdropRecord> errorAirdropRecord = new ArrayList<CntAirdropRecord>();
        //判断盲盒id是否一致
        Set<String> boxIds = boxBachAirdopExcels.parallelStream().map(BoxBachAirdopExcel::getBoxId).collect(Collectors.toSet());
        if(boxIds.size()!=1){
            boxBachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getBoxId())
                                .with(CntAirdropRecord::setGoodsType,1)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"所选盲盒不一致!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //增加空投记录
            airdropRecordService.saveBatch(errorAirdropRecord);
            return R.fail("所选盲盒不一致!");
        }
        //获取用户
        List<CntUser> cntUsers = userService.list(
                Wrappers
                        .<CntUser>lambdaQuery().eq(CntUser::getIsReal,2)
                        .in(CntUser::getPhone, boxBachAirdopExcels.parallelStream()
                                .map(BoxBachAirdopExcel::getPhone).collect(Collectors.toList()))
        );
        //获取盲盒
        CntBox cntBox = getOne(
                Wrappers
                        .<CntBox>lambdaQuery()
                        .eq(CntBox::getId,boxIds.stream().findFirst().get())
                        .gt(CntBox::getBalance, 0)
        );

        //获取盲盒藏品
        List<CntBoxCollection> boxCollections = boxCollectionService.list(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxIds.stream().findFirst().get()));

        if(cntUsers.size()==0 || Objects.isNull(cntBox) || boxCollections.size()==0){
            boxBachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getBoxId())
                                .with(CntAirdropRecord::setGoodsType,1)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,cntUsers.size()==0?"用户不存在或用户未实名!":Objects.isNull(cntBox)==true?"盲盒不存在或库存不足!":boxCollections.size()==0?"该盲盒未添加藏品!":"")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //增加空投记录
            airdropRecordService.saveBatch(errorAirdropRecord);

            if(cntUsers.size()==0){
                return R.fail("用户不存在或用户未实名!");
            }

            if(Objects.isNull(cntBox)){
                return R.fail("盲盒不存在或库存不足!");
            }

            if(boxCollections.size()==0){
                return R.fail("该盲盒未添加藏品!");
            }
        }

        //筛选成功的和失败的
        List<CntUserBox> successList = new ArrayList<CntUserBox>();
        List<CntAirdropRecord> airdropRecordList = new ArrayList<CntAirdropRecord>();
        boxBachAirdopExcels.stream().forEach(e->{
            Optional<CntUser> user = cntUsers.parallelStream().filter(f -> f.getPhone().equals(e.getPhone())).findFirst();
            if(user.isPresent()){
                successList.add(
                        Builder
                                .of(CntUserBox::new)
                                .with(CntUserBox::setId, IdUtils.getSnowflakeNextIdStr())
                                .with(CntUserBox::setBoxTitle, cntBox.getBoxTitle())
                                .with(CntUserBox::setBoxOpen, Long.valueOf(1))
                                .with(CntUserBox::setUserId, user.get().getId())
                                .with(CntUserBox::setBoxId, cntBox.getId())
                                .with(CntUserBox::setSourceInfo, "批量空投!")
                                .with(CntUserBox::setCreatedBy, SecurityUtils.getUsername())
                                .with(CntUserBox::setCreatedTime, DateUtils.getNowDate())
                                .build()
                );
                airdropRecordList.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserId,user.get().getId())
                                .with(CntAirdropRecord::setNickName,user.get().getNickName())
                                .with(CntAirdropRecord::setUserPhone,user.get().getPhone())
                                .with(CntAirdropRecord::setGoodsId,cntBox.getId())
                                .with(CntAirdropRecord::setGoodsName,cntBox.getBoxTitle())
                                .with(CntAirdropRecord::setGoodsType,1)
                                .with(CntAirdropRecord::setDeliveryStatus,0)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"投递盲盒成功!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            }else {
                airdropRecordList.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,cntBox.getId())
                                .with(CntAirdropRecord::setGoodsName,cntBox.getBoxTitle())
                                .with(CntAirdropRecord::setGoodsType,1)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"用户不存在或用户未实名!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            }
        });
        int rows = cntBoxMapper.updateLock(cntBox.getId(), successList.size());
        if(!(rows >=1)){
            boxBachAirdopExcels.parallelStream().forEach(e ->{
                errorAirdropRecord.add(
                        Builder.of(CntAirdropRecord::new)
                                .with(CntAirdropRecord::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntAirdropRecord::setUserPhone,e.getPhone())
                                .with(CntAirdropRecord::setGoodsId,e.getBoxId())
                                .with(CntAirdropRecord::setGoodsType,0)
                                .with(CntAirdropRecord::setDeliveryStatus,1)
                                .with(CntAirdropRecord::setDeliveryType,1)
                                .with(CntAirdropRecord::setDeliveryInfo,"库存不足!")
                                .with(CntAirdropRecord::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntAirdropRecord::setCreatedTime,DateUtils.getNowDate())
                                .build()
                );
            });
            //增加空投记录
            airdropRecordService.saveBatch(errorAirdropRecord);

            return R.fail("库存不足!");
        }
        //增加用户盲盒
        userBoxService.saveBatch(successList);
        //增加空投记录
        airdropRecordService.saveBatch(airdropRecordList);
        return R.ok();
    }


    /**
     * 转义数据
     * @param cntBox
     * @return
     */
    private BoxListRedisVo initBoxListVo(CntBox cntBox){
        BoxListRedisVo boxListVo = Builder.of(BoxListRedisVo::new).build();
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
        boxListVo.setMediaVos(mediaService.initMediaVos(cntBox.getId(), BOX_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList()));
        boxListVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(cntBox.getId(), BOX_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList()));
        boxListVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(cntBox.getId(), BOX_MODEL_TYPE).parallelStream().map(m->{
            MediaRedisVo mediaRedisVo = new MediaRedisVo();
            BeanUtil.copyProperties(m,mediaRedisVo);
            return mediaRedisVo;
        }).collect(Collectors.toList()));
        return boxListVo;
    }



    /**
     * 根据盲盒编号查询盲盒藏品
     * @param boxId
     * @return
     */
    private List<BoxCollectionJoinRedisVo> findJoinCollections(String boxId) {
        List<CntBoxCollection> boxCollections = boxCollectionService.list(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId, boxId).orderByDesc(CntBoxCollection::getTranSvg));
        return boxCollections.parallelStream().map(this::initBoxCollectionJoinVo).collect(Collectors.toList());
    }

    private BoxCollectionJoinRedisVo initBoxCollectionJoinVo(CntBoxCollection boxCollection){
        BoxCollectionJoinRedisVo collectionJoinVo = Builder.of(BoxCollectionJoinRedisVo::new).build();
        BeanUtil.copyProperties(boxCollection,collectionJoinVo);
        collectionJoinVo.setFlagScore(boxScoreService.getById(boxCollection.getFlagScore()).getScoreName());
        return collectionJoinVo;
    }


}
