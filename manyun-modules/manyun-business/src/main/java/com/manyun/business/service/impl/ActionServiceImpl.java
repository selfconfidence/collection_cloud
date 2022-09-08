package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.manyun.business.design.mychain.MyChainService;
import com.manyun.business.domain.dto.CallCommitDto;
import com.manyun.business.domain.dto.UserCollectionCountDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.CntActionMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.CommDict.REAL_COMPANY;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CollectionLink.OK_LINK;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class ActionServiceImpl extends ServiceImpl<CntActionMapper, Action> implements IActionService {

    @Resource
    private CntActionMapper actionMapper;

    @Autowired
    private IActionRecordService actionRecordService;

    @Autowired
    private IActionTarService actionTarService;

    @Autowired
    private IActionCollectionService actionCollectionService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private ObjectFactory<ICollectionService> collectionServiceObjectFactory;

    @Autowired
    private MyChainService myChainService;

    /**
     * 查询活动合成列表
     * @return
     */
    @Override
    public TableDataInfo<SyntheticActivityVo> syntheticActivityList() {
        List<Action> actionList = list(Wrappers.<Action>lambdaQuery().orderByAsc(Action::getActionStatus));
        return TableDataInfoUtil.pageTableDataInfo(actionList.parallelStream()
                .map(this::syntheticActivityVo).collect(Collectors.toList()), actionList);
    }

    /**
     * 查询合成记录列表
     * @return
     */
    @Override
    public TableDataInfo<SyntheticRecordVo> syntheticRecordList(String userId) {
        List<ActionRecord> actionRecordList = actionRecordService.list(Wrappers.<ActionRecord>lambdaQuery().eq(ActionRecord::getUserId,userId).orderByDesc(ActionRecord::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(actionRecordList.parallelStream()
                .map(this::syntheticRecordVo).collect(Collectors.toList()), actionRecordList);
    }

    /**
     * 查询合成详情
     * @param userId
     * @param id
     * @return
     */
    @Override
    public R<SynthesisVo> synthesisInfo(String userId, String id) {
        //查询相关信息
        R<Map> relatedData = getRelatedData(id, userId);
        if(200!=relatedData.getCode()){
            return R.fail(relatedData.getCode(),relatedData.getMsg());
        }
        Action action = (Action) relatedData.getData().get("action");
        List<ActionTar> actionTarList = (List<ActionTar>)relatedData.getData().get("actionTarList");
        List<UserCollectionCountDto> userCollectionCountDtos = (List<UserCollectionCountDto>)relatedData.getData().get("userCollectionCountDtos");
        //组装合成材料数据
        List<MaterialVo> materialVoList = actionTarList.stream().map(m -> {
            MaterialVo materialVo=new MaterialVo();
            materialVo.setMaterialId(m.getActionId());
            materialVo.setMaterialName(m.getCollectionName());
            materialVo.setMaterialImage(m.getCollectionImage());
            materialVo.setReleaseNum(m.getReleaseNum());
            materialVo.setDeleteNum(m.getDeleteNum());
            if(userCollectionCountDtos.size()==0){
                materialVo.setNumCount(0);
            }else {
                userCollectionCountDtos.stream().filter(f -> m.getCollectionId().equals(f.getCollectionId())).forEach(mm -> {
                    materialVo.setNumCount(mm.getCount());
                });
            }
            return materialVo;
        }).collect(Collectors.toList());
        //组装目标藏品数据
        SynthesisVo synthesisVo=new SynthesisVo();
        synthesisVo.setActionId(action.getId());
        synthesisVo.setActionStatus(action.getActionStatus());
        synthesisVo.setActionTitle(action.getActionTitle());
        synthesisVo.setActionImage(action.getActionImage());
        synthesisVo.setMaterials(materialVoList);
        synthesisVo.setRuleContent(action.getRuleContent());
        //比较碎片是否充足
        int success = 1;
        if(actionTarList.size()==userCollectionCountDtos.size()) {
            a: for (int i = 0; i < actionTarList.size(); i++) {
                b: for (int j = 0; j < userCollectionCountDtos.size(); j++) {
                    if (userCollectionCountDtos.get(j).getCollectionId().equals(actionTarList.get(i).getCollectionId())) {
                        if (userCollectionCountDtos.get(j).getCount() < actionTarList.get(i).getReleaseNum()) {
                            success = 0;
                            break a;
                        }
                    }
                }
            }
        }else {
            success = 0;
        }
        synthesisVo.setMergeStatus(success==0?1:2);
        return R.ok(synthesisVo);
    }

    /**
     *  立即合成
     * @param userId
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<SynthesizeNowVo> synthesizeNow(String userId, String userName, String id) {
        //查询相关数据
        R<Map> relatedData = getRelatedData(id, userId);
        if(200!=relatedData.getCode()){
            return R.fail(relatedData.getCode(),relatedData.getMsg());
        }
        Action action = (Action) relatedData.getData().get("action");
        List<ActionTar> actionTarList = (List<ActionTar>)relatedData.getData().get("actionTarList");
        List<ActionCollection> actionCollectionList = (List<ActionCollection>)relatedData.getData().get("actionCollectionList");
        List<UserCollectionCountDto> userCollectionCountDtos = (List<UserCollectionCountDto>)relatedData.getData().get("userCollectionCountDtos");

        //比较两个时间大小，前者大 = -1， 相等 =0，后者大 = 1
        if (DateUtils.compareTo(new Date(), DateUtils.toDate(action.getStartTime()), DateUtils.YYYY_MM_DD_HH_MM_SS) == 1 || action.getActionStatus()==1) {
            return R.fail("合成暂未开始!");
        }
        if (DateUtils.compareTo(new Date(), DateUtils.toDate(action.getEndTime()), DateUtils.YYYY_MM_DD_HH_MM_SS) == -1 || action.getActionStatus()==3) {
            return R.fail("本轮合成已结束");
        }

        //比较碎片是否充足
        int success = 1;
        if(actionTarList.size()==userCollectionCountDtos.size()) {
           a: for (int i = 0; i < actionTarList.size(); i++) {
             b: for (int j = 0; j < userCollectionCountDtos.size(); j++) {
                    if (userCollectionCountDtos.get(j).getCollectionId().equals(actionTarList.get(i).getCollectionId())) {
                        if (userCollectionCountDtos.get(j).getCount() < actionTarList.get(i).getReleaseNum()) {
                            success = 0;
                            break a;
                        }
                    }
                }
            }
        }else {
            success = 0;
        }

        if(success == 0){
            return R.fail("合成材料不足");
        }

        ActionCollection luckCollection  = luckGetCollection(actionCollectionList.parallelStream().filter(f -> f.getActionQuantity()!=0).collect(Collectors.toList()));
        Assert.isTrue(actionCollectionService.update(Wrappers.<ActionCollection>lambdaUpdate().eq(ActionCollection::getId,luckCollection.getId()).ge(ActionCollection::getActionQuantity,Integer.valueOf(1)).set(ActionCollection::getActionQuantity, luckCollection.getActionQuantity() - 1 ).set(ActionCollection::getActionNumber,luckCollection.getActionNumber() + 1)),"合成有误,请重试!");
        //获取本次合成的藏品及图片信息
        CntCollection collection = collectionService.getById(luckCollection.getCollectionId());
        List<MediaVo> mediaVoList = mediaService.initMediaVos(luckCollection.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
        Assert.isTrue((Objects.nonNull(collection) || mediaVoList.size()>0),"合成有误,请重试!");

        //删除需消耗的合成材料
        actionTarList.stream().forEach(e ->{
            userCollectionService.deleteMaterial(userId,e.getCollectionId(),e.getDeleteNum());
        });

        //增加用户收藏
        LocalDateTime now = LocalDateTime.now();
        UserCollection userCollection=new UserCollection();
        userCollection.setId(IdUtil.getSnowflake().nextIdStr());
        userCollection.setUserId(userId);
        userCollection.setCollectionId(collection.getId());
        userCollection.setCollectionName(collection.getCollectionName());
        userCollection.setLinkAddr(IdUtil.getSnowflake().nextIdStr());
        userCollection.setIsExist(USE_EXIST.getCode());
        userCollection.setSourceInfo("合成藏品");
        userCollection.setIsLink(NOT_LINK.getCode());
        userCollection.setCreatedBy(userId);
        userCollection.setCreatedTime(now);
        userCollectionService.save(userCollection);

        //合成藏品上链
        BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();
        myChainService.accountCollectionUp(CallCommitDto.builder()
                .userCollectionId(userCollection.getId())
                .artId(userCollection.getLinkAddr())
                .artName(userCollection.getCollectionName())
                .artSize("80")
                .location(userCollection.getLinkAddr())
                .price(realPrice.toString())
                .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .sellway(userCollection.getSourceInfo())
                .owner(userCollection.getUserId())
                .build(), (hash)->{
            userCollection.setIsLink(OK_LINK.getCode());
            userCollection.setRealCompany(REAL_COMPANY);
            // 编号特殊生成
            userCollection.setCollectionNumber(StrUtil.format("CNT_{}",userCollectionService.autoCollectionNum(userCollection.getCollectionId())));
            userCollection.setCollectionHash(hash);
            userCollection.updateD(userCollection.getUserId());
            userCollectionService.updateById(userCollection);
        });

        //增加合成记录
            actionRecordService.save(
                    Builder
                        .of(ActionRecord::new)
                        .with(ActionRecord::setId,IdUtil.getSnowflake().nextIdStr())
                        .with(ActionRecord::setActionId,action.getId())
                        .with(ActionRecord::setUserId,userId)
                        .with(ActionRecord::setNickName,userName)
                        .with(ActionRecord::setCollectionId,collection.getId())
                        .with(ActionRecord::setCollectionName,collection.getCollectionName())
                        .with(ActionRecord::setCreatedBy,userId)
                        .with(ActionRecord::setCreatedTime,now)
                        .build()
            );
        return R.ok(
                Builder
                    .of(SynthesizeNowVo::new)
                    .with(SynthesizeNowVo::setCollectionName,collection.getCollectionName())
                    .with(SynthesizeNowVo::setMediaVos,mediaVoList)
                    .build()
            );
    }


    /**
     * 根据 组件 规则 获取到幸运藏品,进行绑定用户
     * @param actionCollectionList
     * @return
     */
    private ActionCollection luckGetCollection(List<ActionCollection> actionCollectionList) {
        // 在这里开始斟酌, actionCollectionList 数据阶段已经过滤掉 没有库存数量的藏品了。
        return nowTimeGave(actionCollectionList);
    }

    /**
     * 根据当前 概率 做时间戳 取余操作, 相当于有时间 节点才能开出来好盲盒
     * @param actionCollectionList
     * @return
     */
    private ActionCollection nowTimeGave(List<ActionCollection> actionCollectionList) {
        // tranSvg 为概率比
        int count = 0;
        HashMap<Integer, String> collectionMap = Maps.newHashMap();
        // 得到所有概率
        for (ActionCollection actionCollection : actionCollectionList) {
            // 向上取整
            int value = actionCollection.getTranSvg().setScale(0, RoundingMode.HALF_UP).intValue();
            value = count + value;
            for (int i = count; i < value; i++) {
                collectionMap.put(i,actionCollection.getId());
                count ++;
            }
        }
        Integer luckBum =  Long.valueOf(DateUtil.current() % (long)count).intValue();
        String id = collectionMap.get(luckBum);
        return actionCollectionList.parallelStream().filter(item -> item.getId().equals(id)).findAny().get();
    }

    private SyntheticRecordVo syntheticRecordVo(ActionRecord actionRecord) {
        SyntheticRecordVo syntheticRecordVo = Builder.of(SyntheticRecordVo::new).build();
        BeanUtil.copyProperties(actionRecord, syntheticRecordVo);
        List<MediaVo> mediaVoList = mediaService.initMediaVos(actionRecord.getCollectionId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
        syntheticRecordVo.setMediaVos(mediaVoList);
        return syntheticRecordVo;
    }

    private SyntheticActivityVo syntheticActivityVo(Action action) {
        SyntheticActivityVo synthesisVo = Builder.of(SyntheticActivityVo::new).build();
        BeanUtil.copyProperties(action, synthesisVo);
        return synthesisVo;
    }

    private R<Map> getRelatedData(String id,String userId) {
        Map map=new HashMap();
        //查询活动、活动材料、合成藏品、用户拥有的藏品数
        Action action = actionMapper.selectOne(Wrappers.<Action>lambdaQuery().eq(Action::getId, id));
        Assert.isTrue(Objects.nonNull(action),"活动藏品不存在!");
        List<ActionTar> actionTarList = actionTarService.list(Wrappers.<ActionTar>lambdaQuery().eq(ActionTar::getActionId, id));
        Assert.isTrue(actionTarList.size()>0,"获取活动材料信息失败!");
         List<ActionCollection> actionCollections = actionCollectionService.list(Wrappers.<ActionCollection>lambdaQuery().eq(ActionCollection::getActionId, id));
        Assert.isTrue(actionTarList.size()>0,"获取合成藏品信息失败!");
        List<ActionCollection> actionCollectionList = actionCollections.parallelStream().filter(f -> f.getActionQuantity() != Integer.valueOf(0)).collect(Collectors.toList());
        Assert.isTrue(actionCollectionList.size()>0,"本次所有合成藏品已经售罄,请联系客服人员!");
        List<String> collectionIds = actionTarList.stream().map(ActionTar::getCollectionId).collect(Collectors.toList());
        List<UserCollectionCountDto> userCollectionCountDtos=userCollectionService.selectUserCollectionCount(collectionIds,userId);
        map.put("action",action);
        map.put("actionTarList",actionTarList);
        map.put("actionCollectionList",actionCollectionList);
        map.put("userCollectionCountDtos",userCollectionCountDtos);
        return R.ok(map);
    }

}
