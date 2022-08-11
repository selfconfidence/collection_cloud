package com.manyun.admin.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.dto.AirdropDto;
import com.manyun.admin.domain.dto.CntCollectionAlterCombineDto;
import com.manyun.admin.domain.dto.CollectionStateDto;
import com.manyun.admin.domain.query.CollectionQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.mapper.*;
import com.manyun.admin.service.*;
import com.manyun.comm.api.MyChainxSystemService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

/**
 * 藏品Service业务层处理
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

    /**
     * 查询藏品详情
     *
     * @param id 藏品主键
     * @return 藏品
     */
    @Override
    public CntCollectionDetailsVo selectCntCollectionById(String id)
    {
        CntCollectionDetailsVo cntCollectionDetailsVo = cntCollectionMapper.selectCntCollectionDetailsById(id);
        cntCollectionDetailsVo.setLableIds(collectionLableService.list(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, id)).stream().map(CntCollectionLable::getLableId).collect(Collectors.toList()));
        cntCollectionDetailsVo.setMediaVos(mediaService.initMediaVos(id, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
        return cntCollectionDetailsVo;
    }

    /**
     * 查询藏品列表
     *
     * @param collectionQuery
     * @return 藏品
     */
    @Override
    public TableDataInfo<CntCollectionVo> selectCntCollectionList(CollectionQuery collectionQuery)
    {
        PageHelper.startPage(collectionQuery.getPageNum(),collectionQuery.getPageSize());
        List<CntCollection> cntCollectionList = cntCollectionMapper.selectSearchCollectionList(collectionQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntCollectionList.parallelStream().map(m->{
            CntCollectionVo cntCollectionVo = new CntCollectionVo();
            BeanUtil.copyProperties(m, cntCollectionVo);
            cntCollectionVo.setMediaVos(mediaService.initMediaVos(m.getId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE));
            return cntCollectionVo;
        }).collect(Collectors.toList()), cntCollectionList);
    }


    /**
     * 新增藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R insertCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //藏品
        String idStr = IdUtils.getSnowflakeNextIdStr();
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo), "新增藏品失败");
        //验证藏品名称是否已录入
        List<CntCollection> cntCollectionList = list(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getCollectionName, collectionAlterVo.getCollectionName()));
        String info = StrUtil.format("藏品名称为:{}已存在!", collectionAlterVo.getCollectionName());
        Assert.isFalse(cntCollectionList.size()>0,info);
        //校验
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

        //发行方
        String issuanceId = collectionAlterCombineDto.getIssuanceId();
        //藏品详情
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
            }
        }
        collectionInfoService.save(cntCollectionInfo);

        //标签
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        if (Objects.nonNull(cntLableAlterVo)) {
            String[] lableIds = cntLableAlterVo.getLableIds();
            if (lableIds.length>0) {
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
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
        //图片
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        if (Objects.nonNull(mediaAlterVo)) {
            mediaService.save(
                    Builder.of(CntMedia::new)
                            .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                            .with(CntMedia::setBuiId, idStr)
                            .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                            .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                            .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                            .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                            .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build()
            );
        }
        return R.ok();
     }

    /**
     * 修改藏品
     *
     * @param collectionAlterCombineDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateCntCollection(CntCollectionAlterCombineDto collectionAlterCombineDto)
    {
        //藏品
        CntCollectionAlterVo collectionAlterVo = collectionAlterCombineDto.getCntCollectionAlterVo();
        Assert.isTrue(Objects.nonNull(collectionAlterVo), "修改藏品失败");
        String collectionId = collectionAlterVo.getId();
        Assert.isTrue(StringUtils.isNotBlank(collectionId), "缺失必要参数");
        //验证藏品名称是否已录入
        List<CntCollection> cntCollectionList = list(Wrappers.<CntCollection>lambdaQuery().eq(CntCollection::getCollectionName, collectionAlterVo.getCollectionName()).ne(CntCollection::getId,collectionId));
        String info = StrUtil.format("藏品名称为:{}已存在!", collectionAlterVo.getCollectionName());
        Assert.isFalse(cntCollectionList.size()>0,info);
        //校验
        R check = check(collectionAlterVo,collectionAlterCombineDto.getCntLableAlterVo(),collectionAlterCombineDto.getMediaAlterVo());
        if(200!=check.getCode()){
            return R.fail(check.getCode(),check.getMsg());
        }
        CntCollection cntCollection = new CntCollection();
        BeanUtil.copyProperties(collectionAlterVo, cntCollection);
        cntCollection.setUpdatedBy(SecurityUtils.getUsername());
        cntCollection.setUpdatedTime(DateUtils.getNowDate());
        if (!updateById(cntCollection)) {
            return R.fail();
        }

        //发行方
        String issuanceId = collectionAlterCombineDto.getIssuanceId();
        //藏品详情
        CntCollectionInfoAlterVo collectionInfoAlterVo = collectionAlterCombineDto.getCntCollectionInfoAlterVo();
        if (Objects.nonNull(collectionInfoAlterVo)) {
            List<CntCollectionInfo> cntCollectionInfos = collectionInfoService.list(Wrappers.<CntCollectionInfo>lambdaQuery().eq(CntCollectionInfo::getCollectionId, collectionId));
            CnfIssuance cnfIssuance = issuanceService.getById(issuanceId);
            if (cntCollectionInfos.size() == 0) {
                CntCollectionInfo cntCollectionInfo = new CntCollectionInfo();
                BeanUtil.copyProperties(collectionInfoAlterVo, cntCollectionInfo);
                cntCollectionInfo.setId(IdUtils.getSnowflakeNextIdStr());
                cntCollectionInfo.setCollectionId(collectionId);
                if(Objects.nonNull(cnfIssuance)){
                    cntCollectionInfo.setPublishId(issuanceId);
                    cntCollectionInfo.setPublishOther(cnfIssuance.getPublishOther());
                    cntCollectionInfo.setPublishAuther(cnfIssuance.getPublishAuther());
                }
                cntCollectionInfo.setCreatedBy(SecurityUtils.getUsername());
                cntCollectionInfo.setCreatedTime(DateUtils.getNowDate());
                collectionInfoService.save(cntCollectionInfo);
            } else {
                CntCollectionInfo cntCollectionInfo = new CntCollectionInfo();
                cntCollectionInfo.setId(cntCollectionInfos.get(0).getId());
                if(Objects.nonNull(cnfIssuance)){
                    cntCollectionInfo.setPublishId(issuanceId);
                    cntCollectionInfo.setPublishOther(cnfIssuance.getPublishOther());
                    cntCollectionInfo.setPublishAuther(cnfIssuance.getPublishAuther());
                }else {
                    cntCollectionInfo.setPublishId("");
                    cntCollectionInfo.setPublishOther("");
                    cntCollectionInfo.setPublishAuther("");
                }
                cntCollectionInfo.setLookInfo(collectionInfoAlterVo.getLookInfo());
                cntCollectionInfo.setUpdatedBy(SecurityUtils.getUsername());
                cntCollectionInfo.setUpdatedTime(DateUtils.getNowDate());
                collectionInfoService.updateById(cntCollectionInfo);
            }
        }
        //标签
        CntLableAlterVo cntLableAlterVo = collectionAlterCombineDto.getCntLableAlterVo();
        String[] lableIds = cntLableAlterVo.getLableIds();
        if (Objects.nonNull(collectionInfoAlterVo)) {
            if (lableIds.length>0) {
                collectionLableService.remove(Wrappers.<CntCollectionLable>lambdaQuery().eq(CntCollectionLable::getCollectionId, collectionId));
                List<CntCollectionLable> cntCollectionLables = Arrays.asList(lableIds).stream().map(m -> {
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
        //图片
        MediaAlterVo mediaAlterVo = collectionAlterCombineDto.getMediaAlterVo();
        if (Objects.nonNull(mediaAlterVo)) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(collectionId, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            if (mediaVos.size() == 0) {
                mediaService.save(
                        Builder.of(CntMedia::new)
                                .with(CntMedia::setId, IdUtils.getSnowflakeNextIdStr())
                                .with(CntMedia::setBuiId, collectionId)
                                .with(CntMedia::setModelType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE)
                                .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                                .with(CntMedia::setMediaType, BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE.toString())
                                .with(CntMedia::setCreatedBy, SecurityUtils.getUsername())
                                .with(CntMedia::setCreatedTime, DateUtils.getNowDate()).build()
                );
            } else {
                mediaService.updateById(
                        Builder.of(CntMedia::new)
                                .with(CntMedia::setId, mediaVos.get(0).getId())
                                .with(CntMedia::setMediaUrl, mediaAlterVo.getImg())
                                .with(CntMedia::setUpdatedBy, SecurityUtils.getUsername())
                                .with(CntMedia::setUpdatedTime, DateUtils.getNowDate())
                                .build()
                );
            }
        }
        return R.ok();
    }

    public R check(CntCollectionAlterVo collectionAlterVo,CntLableAlterVo lableAlterVo ,MediaAlterVo mediaAlterVo){
        //验证提前购分钟是否在范围内
        Integer postTime = collectionAlterVo.getPostTime();
        Date publishTime = collectionAlterVo.getPublishTime();
        if(postTime!=null){
            if(postTime<10 || postTime>1000){
                return R.fail("提前购时间请输入大于等于10,小于1000的整数!");
            }
        }
        //验证发售时间是否小于当前时间
        //比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
        if(publishTime!=null){
            if (DateUtils.compareTo(new Date(), publishTime, DateUtils.YYYY_MM_DD_HH_MM_SS) == -1) {
                return R.fail("发售时间不能小于当前时间!");
            }
        }
        //验证标签是否超过三个
        if(Objects.nonNull(lableAlterVo)){
            if(lableAlterVo.getLableIds().length>3){
                return R.fail("藏品标签最多可选中三个!");
            }
        }
        //验证流通数量不能大于发售数量
        Integer balance = collectionAlterVo.getBalance();
        Integer selfBalance = collectionAlterVo.getSelfBalance();
        if(balance!=null && selfBalance!=null){
            if(selfBalance>balance){
                return R.fail("流通数量不能大于发售数量!");
            }
        }
        //验证图片
        if(Objects.isNull(mediaAlterVo) || StringUtils.isBlank(mediaAlterVo.getImg())){
            return R.fail("藏品主图不能为空!");
        }
        return R.ok();
    }

    /***
     * 空投
     * @param airdropDto 空投请求参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R airdrop(AirdropDto airdropDto) {
        //验证用户
        List<CntUser> cntUsers = userService.list(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, airdropDto.getPhone()));
        Assert.isTrue(cntUsers.size() > 0, "用户不存在!");
        CntCollection collection = getById(airdropDto.getCollectionId());
        Assert.isTrue(Objects.nonNull(collection), "藏品不存在!");
        String collectionId = collection.getId();
        Integer selfBalance = collection.getSelfBalance();
        Integer balance = collection.getBalance();
        Assert.isFalse(selfBalance >= balance, "已售空!");
        //扣减库存
        updateById(
                Builder
                        .of(CntCollection::new)
                        .with(CntCollection::setId, collectionId)
                        .with(CntCollection::setSelfBalance, (selfBalance + 1))
                        .with(CntCollection::setBalance, (balance - 1))
                        .build()
        );
        //增加用户藏品
        String idStr = IdUtils.getSnowflakeNextIdStr();
        userCollectionService.save(
                Builder
                        .of(CntUserCollection::new)
                        .with(CntUserCollection::setId, idStr)
                        .with(CntUserCollection::setUserId, cntUsers.get(0).getUserId())
                        .with(CntUserCollection::setCollectionId, collection.getId())
                        .with(CntUserCollection::setCollectionName, collection.getCollectionName())
                        .with(CntUserCollection::setLinkAddr, IdUtils.getSnowflake().nextIdStr())
                        .with(CntUserCollection::setSourceInfo, "空投")
                        .with(CntUserCollection::setIsExist,USE_EXIST.getCode())
                        .with(CntUserCollection::setIsLink,NOT_LINK.getCode())
                        .with(CntUserCollection::setCreatedBy,SecurityUtils.getUsername())
                        .with(CntUserCollection::setCreatedTime,DateUtils.getNowDate())
                        .build()
        );
        return R.ok(Builder.of(AirdropVo::new).with(AirdropVo::setUserId,cntUsers.get(0).getUserId()).with(AirdropVo::setUsercollectionId,idStr).build());
    }

    /**
     * 修改状态
     * @param collectionStateDto
     * @return
     */
    @Override
    public int updateState(CollectionStateDto collectionStateDto) {
        CntCollection cntCollection=new CntCollection();
        BeanUtil.copyProperties(collectionStateDto,cntCollection);
        cntCollection.setUpdatedBy(SecurityUtils.getUsername());
        cntCollection.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntCollection)==true?1:0;
    }

}
