package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.MsgCommDto;
import com.manyun.business.domain.dto.MsgThisDto;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.CollectionOrderSellForm;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.*;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.redis.collection.CollectionAllRedisVo;
import com.manyun.comm.api.domain.redis.collection.CollectionRedisVo;
import com.manyun.comm.api.domain.redis.collection.LableRedisVo;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.BoxStatus;
import com.manyun.common.core.enums.CollectionStatus;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.redis.domian.dto.BuiCronDto;
import com.manyun.common.redis.service.BuiCronService;
import com.manyun.common.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.lang.System;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.*;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.BoxStatus.NULL_ACTION;
import static com.manyun.common.core.enums.CollectionStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.TarStatus.CEN_YES_TAR;
import static com.manyun.common.core.enums.TarStatus.NO_TAR;
import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;

/**
 * <p>
 * 藏品表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor(onConstructor_= {@Autowired})
public class CollectionServiceImpl extends ServiceImpl<CntCollectionMapper, CntCollection> implements ICollectionService {
    private final CntCollectionMapper cntCollectionMapper;

    private final CateMapper cateMapper;

    private final IUserCollectionService userCollectionService;

    private final ICntCreationdService cntCreationdService;

    private final LableMapper lableMapper;

    private final ICntTarService tarService;


    private final IMediaService mediaService;

    private final CollectionLableMapper collectionLableMapper;

    private final CollectionInfoMapper collectionInfoMapper;

    private final IMoneyService moneyService;

    private final IOrderService orderService;

    private final RootPay rootPay;

    private final RedisService redisService;


    private final ICntPostExcelService cntPostExcelService;
    private final ICntPostConfigService postConfigService;

    private final IMsgService msgService;

    private final IBoxService boxService;

    private final IUserBoxService userBoxService;

    private final IStepService stepService;

    private final RemoteBuiUserService userService;

    private final BuiCronService buiCronService;

    @Override
    public TableDataInfo<CollectionVo> pageQueryList(CollectionQuery collectionQuery) {
        // 查询条件部分
        List<CntCollection> cntCollections = list(Wrappers.<CntCollection>lambdaQuery()
                .eq(StrUtil.isNotBlank(collectionQuery.getCateId()), CntCollection::getCateId, collectionQuery.getCateId())
                .ne(CntCollection::getStatusBy,DOWN_ACTION.getCode())
                .like(StrUtil.isNotBlank(collectionQuery.getCollectionName()), CntCollection::getCollectionName, collectionQuery.getCollectionName())
                .eq(StrUtil.isNotBlank(collectionQuery.getBindCreationId()), CntCollection::getBindCreation, collectionQuery.getBindCreationId())
                .orderByDesc(CntCollection::getCreatedTime)
        );
        // 聚合数据
        return TableDataInfoUtil.pageTableDataInfo(cntCollections.parallelStream().map(this::providerCollectionVo).collect(Collectors.toList()),cntCollections);
    }

    @Override
    public CollectionAllRedisVo infoCache(String id, String userId){
        CollectionAllRedisVo collectionAllVo = redisService.getCacheMapValue(BusinessConstants.RedisDict.COLLECTION_INFO, id);
        Assert.isTrue(Objects.nonNull(collectionAllVo),"此藏品未找到,请核实!");
        // 需找库
       // CntCollection byId = getById(id);
        CntCollection cntCollection = getOne(Wrappers.<CntCollection>lambdaQuery().select(CntCollection::getId,CntCollection::getTarId, CntCollection::getPostTime).eq(CntCollection::getId, id));
        CollectionRedisVo collectionRedisVo = collectionAllVo.getCollectionVo();
        // 是否能够购买? 预先状态判定
        collectionAllVo.setTarStatus(NO_TAR.getCode());
        // 低耦性校验
        if (StrUtil.isNotBlank(cntCollection.getTarId()) && StringUtils.isNotBlank(userId)){
            collectionAllVo.setTarStatus(tarService.tarStatus(userId,id));
            collectionAllVo.setOpenTime(tarService.getById(cntCollection.getTarId()).getOpenTime());
        }


        Integer postTime = null;
        //提前购?
        if (Objects.nonNull(cntCollection.getPostTime()) && StringUtils.isNotBlank(userId)){
            // 两种验证,一种 excel , 一种 关联性 方式
            //1. excel true 可以提前购, false 不可以
            //2. 配置模板 true 可以提前购 false 不可以
            if (cntPostExcelService.isExcelPostCustomer(userId,id) || postConfigService.isConfigPostCustomer(userId,id)){
                postTime = cntCollection.getPostTime();
            }
        }
        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(COLLECTION_MODEL_TYPE, id);
       collectionRedisVo.setBalance(typeBalanceCache.getBalance());
       collectionRedisVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (collectionRedisVo.getPublishTime().isAfter(LocalDateTime.now())) {
            collectionRedisVo.setPreStatus(1);
        } else {
            collectionRedisVo.setPreStatus(2);
        }
        if (Integer.valueOf(0).equals(typeBalanceCache.getBalance())) {
            collectionRedisVo.setStatusBy(2);
        }
        collectionAllVo.setPostTime(postTime);
         return collectionAllVo;
    }

    @Override
    public CollectionAllVo info(String id,String userId) {
        CntCollection cntCollection = getById(id);
        CollectionAllVo collectionAllVo = Builder.of(CollectionAllVo::new).with(CollectionAllVo::setCollectionVo, providerCollectionVo(cntCollection)).with(CollectionAllVo::setCollectionInfoVo, providerCollectionInfoVo(id)).build();
        // 是否能够购买? 预先状态判定
        collectionAllVo.setTarStatus(NO_TAR.getCode());
        // 低耦性校验
        if (StrUtil.isNotBlank(cntCollection.getTarId()) && StringUtils.isNotBlank(userId)){
            collectionAllVo.setTarStatus(tarService.tarStatus(userId,id));
            collectionAllVo.setOpenTime(tarService.getById(cntCollection.getTarId()).getOpenTime());
        }


        Integer postTime = null;
        //提前购?
        if (Objects.nonNull(cntCollection.getPostTime()) && StringUtils.isNotBlank(userId)){
            // 两种验证,一种 excel , 一种 关联性 方式
            //1. excel true 可以提前购, false 不可以
            //2. 配置模板 true 可以提前购 false 不可以
            if (cntPostExcelService.isExcelPostCustomer(userId,id) || postConfigService.isConfigPostCustomer(userId,id)){
                postTime = cntCollection.getPostTime();
            }
        }
        collectionAllVo.setPostTime(postTime);

        return collectionAllVo;
    }

    @Override
    public CollectionAllVo info(String id) {
        return info(id,null);
    }

    /**
     * 购买藏品
     * @param userId
     * @param collectionSellForm
     * @return
     */
    @Override
    @Transactional
    @Deprecated
    public PayVo sellCollection(String userId, CollectionSellForm collectionSellForm) {
        // 总结校验 —— 支付方式
        CntCollection  cntCollection = getById(collectionSellForm.getCollectionId());
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(collectionSellForm.getSellNum(), cntCollection.getRealPrice());
        checkCollection(cntCollection,userId,collectionSellForm.getPayType(),realPayMoney);
        // 锁优化
        checkBalance(cntCollection.getId(),collectionSellForm.getSellNum());

        // 根据支付方式创建订单  通用适配方案 余额直接 减扣操作
        //1. 先创建对应的订单
        String outHost =   orderService.createOrder(OrderCreateDto.builder()
                .orderAmount(realPayMoney)
                .buiId(cntCollection.getId())
                .payType(collectionSellForm.getPayType())
                .goodsType(COLLECTION_TAYPE)
                .collectionName(cntCollection.getCollectionName())
                .goodsNum(collectionSellForm.getSellNum())
                .userId(userId)
                .build());
        //2. 然后调取对应支付即可
        /**
         * 根据类型  发起支付订单
         * 余额支付直接扣除 订单状态做变更即可
         **/
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(collectionSellForm.getPayType())
                        .realPayMoney(realPayMoney)
                        .outHost(outHost)
                        .aliPayEnum(BOX_ALI_PAY)
                        .wxPayEnum(BOX_WECHAT_PAY)
                        .userId(userId).build());
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        if (MONEY_TAPE.getCode().equals(collectionSellForm.getPayType()) && StrUtil.isBlank(payVo.getBody())){
            // 调用完成订单
            orderService.notifyPaySuccess(payVo.getOutHost());
            String title = StrUtil.format("购买了 {} 藏品!", cntCollection.getCollectionName());
            String form = StrUtil.format("使用余额{};购买了 {} 藏品!",realPayMoney.toString(), cntCollection.getCollectionName());
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sellOrderCollection(String userId, CollectionOrderSellForm collectionOrderSellForm) {
        // 记录抢购链路

        // 总结校验 —— 支付方式
        CntCollection  cntCollection = getById(collectionOrderSellForm.getCollectionId());
        // 实际需要支付的金额
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(collectionOrderSellForm.getSellNum(), cntCollection.getRealPrice());
        checkPerOrderCollection(cntCollection,userId);
        // 锁优化
        checkCacheBalance(cntCollection,collectionOrderSellForm.getSellNum());

        // 根据支付方式创建订单  通用适配方案 余额直接 减扣操作
        //1. 先创建对应的订单
        String outHost =   orderService.createOrder(OrderCreateDto.builder()
                .orderAmount(realPayMoney)
                .buiId(cntCollection.getId())
                .goodsType(COLLECTION_TAYPE)
                .collectionName(cntCollection.getCollectionName())
                .goodsNum(collectionOrderSellForm.getSellNum())
                .userId(userId)
                .build());
        return orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost)).getId();
    }

    private void checkCacheBalance(CntCollection cntCollection,Integer sellNum){
        boolean flag = buiCronService.doBuiDegressionBalanceCache(COLLECTION_MODEL_TYPE, cntCollection.getId(), sellNum);
        Assert.isTrue(flag,"您来晚了,已售罄了!");
    }
    @Override
    public void checkBalance(String cntCollectionId, Integer sellNum) {
        int rows = cntCollectionMapper.updateLock(cntCollectionId,sellNum);
        if (!(rows >=1)){
            update(Wrappers.<CntCollection>lambdaUpdate().eq(CntCollection::getId, cntCollectionId).set(CntCollection::getStatusBy,NULL_ACTION.getCode()));
/*            CntCollection cntCollection = cntCollectionMapper.selectById(cntCollectionId);
            cntCollection.setStatusBy(NULL_ACTION.getCode());
            cntCollectionMapper.updateById(cntCollection);*/
          //  Assert.isTrue(Boolean.FALSE,"您来晚了,已售罄了!");
        }

    }

    @Override
    public List<KeywordVo> thisAssertQueryDict(String userId, String keyword) {
        List<String> collectIonNames = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().select(UserCollection::getCollectionName).like(UserCollection::getCollectionName,keyword).eq(UserCollection::getUserId,userId).eq(UserCollection::getIsExist,USE_EXIST.getCode()).orderByDesc(UserCollection::getCreatedTime).last(" limit 10"  )).parallelStream().map(item -> item.getCollectionName()).collect(Collectors.toList());
        List<String> boxNames = userBoxService.list(Wrappers.<UserBox>lambdaQuery().select(UserBox::getBoxTitle).like(UserBox::getBoxTitle, keyword).eq(UserBox::getUserId, userId).eq(UserBox::getIsExist, USE_EXIST.getCode()).orderByDesc(UserBox::getCreatedTime).last(" limit 10 ")).parallelStream().map(item -> item.getBoxTitle()).collect(Collectors.toList());
        return  initKeywordVo(Lists.newArrayList(Sets.newHashSet(collectIonNames)),Lists.newArrayList(Sets.newHashSet(boxNames)));
    }

    @Override
    public TableDataInfo<CollectionVo> homeCacheList() {
        // 缓存拿取数据
        Map<String, CollectionAllRedisVo> entries = redisService.redisTemplate.boundHashOps(BusinessConstants.RedisDict.COLLECTION_INFO).entries();
        Collection<CollectionAllRedisVo> cacheList = entries.values();
        List<CollectionRedisVo> collectionVoList = cacheList.parallelStream().map(item ->  {
            CollectionRedisVo collectionRedisVo = item.getCollectionVo();
            // 库存需要实时变动
            BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(COLLECTION_MODEL_TYPE, collectionRedisVo.getId());
            collectionRedisVo.setBalance(typeBalanceCache.getBalance());
            collectionRedisVo.setSelfBalance(typeBalanceCache.getSelfBalance());
            if (collectionRedisVo.getPublishTime().isAfter(LocalDateTime.now())) {
                collectionRedisVo.setPreStatus(1);
            } else {
                collectionRedisVo.setPreStatus(2);
            }
            if (Integer.valueOf(0).equals(collectionRedisVo.getBalance())) {
                collectionRedisVo.setStatusBy(2);
            }
            return collectionRedisVo;
        }).collect(Collectors.toList());
        // 二次包裹
        return TableDataInfoUtil.pageCacheData(ListUtil.reverse(collectionVoList), collectionVoList.size());
    }


    /**
     * 预先生成订单检测方法
     * @param cntCollection
     * @param userId
     */
    private void checkPerOrderCollection(CntCollection cntCollection, String userId) {
        commCheckCollection(cntCollection, userId);
        tarCheckCollection(cntCollection, userId);
        postCheckCollection(cntCollection, userId);
        realCheckCollection(userId);
        conditionOrder(cntCollection,userId);
    }

    /**
     * 限购判定
     * @param cntCollection
     * @param userId
     */
    private void conditionOrder(CntCollection cntCollection, String userId) {
        Integer limitNumber = cntCollection.getLimitNumber();
        // 限购逻辑生效
        if (Objects.nonNull(limitNumber) && limitNumber >0){
            // 当前资产是否是提前购得资产?
            // 查询用户所有已经完成的订单!
            int sellNumber = orderService.overCount(cntCollection.getId(),userId);
            if (Objects.nonNull(cntCollection.getPostTime()) ){
                // 是提前购得资产 并且已经到了发布时间
                if (LocalDateTime.now().compareTo(cntCollection.getPublishTime()) > 0){
                    // 如果到了发布时间，就将已经参与提前购的次数，直接 - 整个订单的数量即可
                    int excelSellNum = cntPostExcelService.getSellNum(userId, cntCollection.getId());
                    int postConfigSellNum = postConfigService.getSellNum(userId, cntCollection.getId());
                     sellNumber =  ((excelSellNum + postConfigSellNum) - sellNumber);
                }else{
                    // 中肯写法，不可能没到发售时间!
                }

            }
            Assert.isTrue(limitNumber > sellNumber,"抢的太多了,被限购了哦!");

        }
    }

    private void realCheckCollection(String userId) {

        R<CntUserDto> cntUserDtoR = userService.commUni(userId, SecurityConstants.INNER);
        CntUserDto data = cntUserDtoR.getData();
        Assert.isTrue(OK_REAL.getCode().equals(data.getIsReal()),"暂未实名认证,请实名认证!");
    }

    /**
     * 分页查询用户的 所有藏品信息
     * @param useAssertQuery
     * @param userId
     * @return
     */
    @Override
    public TableDataInfo<UserCollectionVo> userCollectionPageList(UseAssertQuery useAssertQuery, String userId) {
        PageHelper.startPage(useAssertQuery.getPageNum(),useAssertQuery.getPageSize());
        List<UserCollectionVo> userCollectionVos =  userCollectionService.userCollectionPageList(userId,useAssertQuery.getCommName());
        // 组合数据
        return TableDataInfoUtil
                .pageTableDataInfo(
                        userCollectionVos
                                .parallelStream()
                                .map(item ->
                                {
                                    item.setMediaVos(mediaService.initMediaVos(item.getCollectionId(),COLLECTION_MODEL_TYPE));
                                    item.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(item.getCollectionId(),COLLECTION_MODEL_TYPE));
                                    item.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(item.getCollectionId(),COLLECTION_MODEL_TYPE));
                                    return item;
                                }
                                ).collect(Collectors.toList()),userCollectionVos);
    }

    /**
     * 查询用户下 所有藏品 组系列
     * @param userId
     * @return
     */
    @Override
    public List<UserCateVo> cateCollectionByUserId(String userId) {
        List<UserCateVo> userCateVoList = Lists.newLinkedList();

        List<UserCollection> userCollections = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).eq(UserCollection::getIsExist, USE_EXIST.getCode()));
        // 如果没有,直接返回
       if (userCollections.isEmpty()) return userCateVoList;
        // 开始得到分类
        Set<String> collectionIds = userCollections.parallelStream().map(item -> item.getCollectionId()).collect(Collectors.toSet());
        // 三个字段足以
        List<CntCollection> cateCollectionList = list(Wrappers.<CntCollection>lambdaQuery().select(CntCollection::getId, CntCollection::getCateId, CntCollection::getCollectionName).in(CntCollection::getId, collectionIds));
        // 并行执行
        Set<String> cateIds = cateCollectionList.parallelStream().map(item -> item.getCateId()).collect(Collectors.toSet());
        if (cateIds.isEmpty()) return userCateVoList;
        Map<String, List<CntCollection>> cateLists = cateCollectionList.parallelStream().collect(Collectors.groupingBy(CntCollection::getCateId));
        List<Cate> cates = cateMapper.selectBatchIds(cateIds);
        Map<String, Cate> cateMap = cates.parallelStream().collect(Collectors.toMap(Cate::getId, Function.identity()));
        // 创作者
        List<CntCreationd> cntCreationds = cntCreationdService.list(Wrappers.<CntCreationd>lambdaQuery().in(CntCreationd::getId, cates.parallelStream().map(item -> item.getBindCreation()).collect(Collectors.toList())));
        Map<String, CntCreationd> creationdMap = cntCreationds.parallelStream().collect(Collectors.toMap(CntCreationd::getId, Function.identity()));
        cateLists.forEach((cateId,cntCollections)->{
            // 需要对 cntCollections 进行重构结构
            Map<String,List<CntCollection>> userCollectionMaps  = cateUserCollection(cntCollections,userCollections);
            Cate cate = cateMap.get(cateId);
            UserCateVo userCateVo = Builder.of(UserCateVo::new).build();
            BeanUtil.copyProperties(cate,userCateVo,"userCateCollectionVos");
            userCateVo.setUserCateCollectionVos(initUserCateCollectionVo(userCollectionMaps));
            CntCreationd cntCreationd = creationdMap.get(cate.getBindCreation());
            if (Objects.nonNull(cntCreationd)){
                userCateVo.setHeadImage(cntCreationd.getHeadImage());
                userCateVo.setCreationName(cntCreationd.getCreationName());
            }

            userCateVoList.add(userCateVo);
        });
        return userCateVoList;
    }

    /**
     * 用户编号key
     * @param cntCollections
     * @param userCollections
     * @return
     */
    private Map<String,List<CntCollection>> cateUserCollection(List<CntCollection> cntCollections, List<UserCollection> userCollections) {
        HashMap<String,List<CntCollection>> userCntMap = Maps.newHashMap();
        for (UserCollection userCollection : userCollections) {
            String collectionId = userCollection.getCollectionId();
            List<CntCollection> collectionList = cntCollections.parallelStream().filter(item -> item.getId().equals(collectionId)).collect(Collectors.toList());
            userCntMap.merge(userCollection.getId(),collectionList , (oldValue, newValue) -> {
                oldValue.addAll(newValue);
                return oldValue;
            });
        }
        return userCntMap;
    }

    @Override
    public List<KeywordVo> queryDict(String keyword) {
        List<String> collectIonNames = list(Wrappers.<CntCollection>lambdaQuery().like(CntCollection::getCollectionName, keyword).ne(CntCollection::getStatusBy,DOWN_ACTION.getCode()).select(CntCollection::getCollectionName).orderByDesc(CntCollection::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getCollectionName()).collect(Collectors.toList());
        List<String> boxNames = boxService.list(Wrappers.<Box>lambdaQuery().select(Box::getBoxTitle).like(Box::getBoxTitle, keyword).ne(Box::getStatusBy, BoxStatus.DOWN_ACTION.getCode()).orderByDesc(Box::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getBoxTitle()).collect(Collectors.toList());
        return  initKeywordVo(collectIonNames,boxNames);
    }

    private List<KeywordVo> initKeywordVo(List<String> collectIonNames,List<String> boxNames){
        List<KeywordVo> keywordVos = Lists.newArrayList();
        for (String collectIonName : collectIonNames) {
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setCommTitle(collectIonName);
            keywordVo.setType(COLLECTION_TAYPE);
            keywordVos.add(keywordVo);

        }

        for (String boxName : boxNames) {
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setCommTitle(boxName);
            keywordVo.setType(BOX_TAYPE);
            keywordVos.add(keywordVo);
        }
        Collections.shuffle(keywordVos);
        return keywordVos;
    }

    /**
     *
     * @param id     藏品编号
     * @param userId 用户编号
     * @return
     */
    @Override
    public String tarCollection(String id, String userId) {
        // 开始抽签
        return tarService.tarCollection(getById(id),userId);
    }

    private List<UserCateCollectionVo> initUserCateCollectionVo(Map<String,List<CntCollection>> cntCollectionMaps) {
        List<UserCateCollectionVo> userCateCollectionVoList = Lists.newArrayList();
        Map<String,List<MediaVo>>  meMaps  = Maps.newHashMap();
        cntCollectionMaps.forEach((key,val)->{
            for (CntCollection cntCollection : val) {
                List<MediaVo> tempMediaVoList = null;
                List<MediaVo> thumbnailImgMediaVos = null;
                List<MediaVo> threeDimensionalMediaVos = null;
                if ((tempMediaVoList = meMaps.get(cntCollection.getId())) == null){
                    tempMediaVoList = mediaService.initMediaVos(cntCollection.getId(),COLLECTION_MODEL_TYPE);
                    meMaps.put(cntCollection.getId(), tempMediaVoList);
                }
                if ((thumbnailImgMediaVos = meMaps.get(cntCollection.getId())) == null){
                    thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(cntCollection.getId(),COLLECTION_MODEL_TYPE);
                    meMaps.put(cntCollection.getId(), thumbnailImgMediaVos);
                }
                if ((threeDimensionalMediaVos = meMaps.get(cntCollection.getId())) == null){
                    threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(cntCollection.getId(),COLLECTION_MODEL_TYPE);
                    meMaps.put(cntCollection.getId(), threeDimensionalMediaVos);
                }
                UserCateCollectionVo userCateCollectionVo = Builder.of(UserCateCollectionVo::new).build();
                userCateCollectionVo.setCollectionName(cntCollection.getCollectionName());
                userCateCollectionVo.setId(cntCollection.getId());
                userCateCollectionVo.setUserCollectionId(key);
                userCateCollectionVo.setMediaVos(tempMediaVoList);
                userCateCollectionVo.setThumbnailImgMediaVos(thumbnailImgMediaVos);
                userCateCollectionVo.setThreeDimensionalMediaVos(threeDimensionalMediaVos);
                userCateCollectionVoList.add(userCateCollectionVo);
            }
        });

        return userCateCollectionVoList;
    }


    /**
     * 通用检测方法
     * @param cntCollection
     * @param userId
     */
    private void commCheckCollection(CntCollection cntCollection,String userId){
        // 校验盲盒主体是否存在
        Assert.isTrue(Objects.nonNull(cntCollection),"藏品编号有误,请核实!");
        // 校验是否库存不够了
        Assert.isFalse(Integer.valueOf(0).equals(cntCollection.getBalance()),"库存不足了!");
        // 重复校验状态
        Assert.isTrue(CollectionStatus.UP_ACTION.getCode().equals(cntCollection.getStatusBy()),"您来晚了,售罄了!");

        //是否有未支付订单
        List<Order> orders = orderService.checkUnpaidOrder(userId);
        Assert.isFalse(orders.size() > 0 ,"您有未支付订单，暂不可购买");

    }

    /**
     * 余额检测
     * @param
     * @param userId
     */
    private void moneyCheckCollection(String userId,Integer payType,BigDecimal realPayMoney){
        // 如果是余额支付,需要验证下是否充足
        if (MONEY_TAPE.getCode().equals(payType)) {
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
            Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney) >=0,"余额不足,请核实!");
        }

    }


    /**
     * 抽签检测
     * @param
     * @param userId
     */
    private void tarCheckCollection(CntCollection cntCollection,String userId){
        // 是否需要抽？
        if (StrUtil.isNotBlank(cntCollection.getTarId())){
            if (!CEN_YES_TAR.getCode().equals(tarService.tarStatus(userId,cntCollection.getId())))
                Assert.isFalse(Boolean.TRUE,"未参与抽签,或暂未购买资格!");

            Assert.isFalse(tarService.isSell(userId,cntCollection.getId()),"不可复购,只可购买一次!");
        }





    }

    /**
     * 提前购检测 发售时间检测
     * @param
     * @param userId
     */
    private void postCheckCollection(CntCollection cntCollection,String userId){
        // 是否能够提前购？
        Boolean publishTimeFlag = Boolean.TRUE;
        // 如果是提前购 并且 购买的时机符合判定条件就进去判定 是否有资格提前购买
        if (Objects.nonNull(cntCollection.getPostTime()) && LocalDateTime.now().compareTo(cntCollection.getPublishTime()) < 0){
            // 是提前购的属性
            //  表格是否满足提前购 && 配置是否满足提前购
            // 1.1 满足就 根据发售时间 - postTime(分钟) ;
            //1.1.1 判定下当前时间 就可以购买了 publishTimeFlag = false
            // 1.1.2 如果当前时间还不满足 就拦截提示 好了
            // 1.2 不满足就 直接拦截,提示.
            if (cntPostExcelService.isExcelPostCustomer(userId, cntCollection.getId()) || postConfigService.isConfigPostCustomer(userId, cntCollection.getId())){
                if (LocalDateTime.now().compareTo(cntCollection.getPublishTime().minusMinutes(cntCollection.getPostTime())) >=0){
                    publishTimeFlag = Boolean.FALSE;
                }
            }
        }

        // 校验盲盒是否到了发行时间
        if (publishTimeFlag)Assert.isTrue(LocalDateTime.now().compareTo(cntCollection.getPublishTime()) >= 0,"发行时间未到,请核实!");


    }

    private void checkCollection(CntCollection cntCollection,String userId,Integer payType,BigDecimal realPayMoney) {
         commCheckCollection(cntCollection, userId);
         moneyCheckCollection(userId, payType, realPayMoney);
         tarCheckCollection(cntCollection, userId);
         postCheckCollection(cntCollection, userId);
         realCheckCollection(userId);
    }

    /**
     * 根据藏品编号  查询出藏品详细信息
     * @param collectionId
     * @return
     */
    private CollectionInfoVo providerCollectionInfoVo(String collectionId) {
        CollectionInfoVo collectionInfoVo = Builder.of(CollectionInfoVo::new).build();
        CollectionInfo collectionInfo = collectionInfoMapper.selectOne(Wrappers.<CollectionInfo>lambdaQuery().eq(CollectionInfo::getCollectionId, collectionId));
        BeanUtil.copyProperties(collectionInfo,collectionInfoVo);
        return collectionInfoVo;
    }

    private CollectionVo providerCollectionVo(CntCollection cntCollection){
        CollectionVo collectionVo = Builder.of(CollectionVo::new).build();
        BeanUtil.copyProperties(cntCollection,collectionVo);
        // 数据隔离
        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(COLLECTION_MODEL_TYPE, cntCollection.getId());
        collectionVo.setBalance(typeBalanceCache.getBalance());
        collectionVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (cntCollection.getPublishTime().isAfter(LocalDateTime.now())) {
            collectionVo.setPreStatus(1);
        } else {
            collectionVo.setPreStatus(2);
        }
        if (Integer.valueOf(0).equals(cntCollection.getBalance())) {
            collectionVo.setStatusBy(2);
        }
        collectionVo.setLableVos(initLableVos(cntCollection.getId()));
        collectionVo.setMediaVos(initMediaVos(cntCollection.getId()));
        collectionVo.setThumbnailImgMediaVos(thumbnailImgMediaVos(cntCollection.getId()));
        collectionVo.setThreeDimensionalMediaVos(threeDimensionalMediaVos(cntCollection.getId()));
        collectionVo.setCnfCreationdVo(initCnfCreationVo(cntCollection.getBindCreation()));
        collectionVo.setCateVo(initCateVo(cntCollection.getCateId()));
        return collectionVo;
    }

    /**
     * 封装基础藏品信息
     * @param collectionId
     * @return
     */
    @Override
    public CollectionVo getBaseCollectionVo(@NotNull String collectionId){
        return providerCollectionVo(getById(collectionId));
    }

    @Override
    public UserCollectionForVo userCollectionInfo(String id) {
        UserCollectionForVo userCollectionForVo = Builder.of(UserCollectionForVo::new).build();
        UserCollectionVo userCollectionVo = userCollectionService.userCollectionById(id);
        Assert.isTrue(Objects.nonNull(userCollectionVo),"未找到相关藏品,请核实!");
        userCollectionVo.setMediaVos(initMediaVos(userCollectionVo.getCollectionId()));
        userCollectionVo.setThumbnailImgMediaVos(thumbnailImgMediaVos(userCollectionVo.getCollectionId()));
        userCollectionVo.setThreeDimensionalMediaVos(threeDimensionalMediaVos(userCollectionVo.getCollectionId()));
        // 增加流转记录信息
        userCollectionForVo.setUserCollectionVo(userCollectionVo);
        userCollectionForVo.setStepVos(initStepVo(userCollectionVo.getLinkAddr(),COLLECTION_MODEL_TYPE));
        userCollectionForVo.setCollectionInfoVo(providerCollectionInfoVo(userCollectionVo.getCollectionId()));

        userCollectionForVo.setLableVos(initLableVos(userCollectionVo.getCollectionId()));
        return userCollectionForVo;
    }

    @Override
    public CollectionOrderAllVo orderInfo(String id) {
        CollectionAllVo collectionAllVo = info(id);
        CollectionOrderAllVo collectionOrderAllVo = Builder.of(CollectionOrderAllVo::new).build();
        BeanUtil.copyProperties(collectionAllVo,collectionOrderAllVo);
        // 这个用户的上链信息
        return collectionOrderAllVo;
    }

    @Override
    public List<CateCollectionVo> cateCollectionChildList(String userId,String cateParentId) {
        List<Cate> cateList = cateMapper.selectList(Wrappers.<Cate>lambdaQuery().eq(Cate::getParentId, cateParentId).eq(Cate::getCateType,Integer.valueOf(1)).orderByDesc(Cate::getCreatedTime));
        List<CateCollectionVo> cateCollectionVoList = Lists.newArrayList();
        if (cateList.isEmpty())return cateCollectionVoList;
        for (Cate cate : cateList) {
            CateCollectionVo cateCollectionVo = Builder.of(CateCollectionVo::new).build();
            BeanUtil.copyProperties(cate, cateCollectionVo);
            List<CntCollection> cateCollectionList = list(Wrappers.<CntCollection>lambdaQuery().ne(CntCollection::getStatusBy, DOWN_ACTION.getCode()).eq(CntCollection::getCateId,cate.getId()));
            List<CollectionVo> collectionVos = Lists.newArrayList();
            for (CntCollection cntCollection : cateCollectionList) {
                collectionVos.add(providerCollectionVo(cntCollection));
            }
            cateCollectionVo.setCollectionVos(collectionVos);
            cateCollectionVoList.add(cateCollectionVo);
        }
        return cateCollectionVoList;
    }



    private List<StepVo> initStepVo(String linkAddr, String collectionModelType) {
        List<Step> stepList = stepService.list(Wrappers.<Step>lambdaQuery().eq(Step::getBuiId, linkAddr).eq(Step::getModelType, collectionModelType).orderByDesc(Step::getCreatedTime));
       return stepList.parallelStream().map(item ->{
           StepVo stepVo = Builder.of(StepVo::new).build();
           BeanUtil.copyProperties(item, stepVo );
           CntUserDto cntUserDto = userService.commUni(item.getUserId(), SecurityConstants.INNER).getData();
           stepVo.setHeadImage(cntUserDto.getHeadImage());
           stepVo.setUserHostId(cntUserDto.getUserId());
           stepVo.setNickName(cntUserDto.getNickName());
           return stepVo;
       } ).collect(Collectors.toList());

    }

    /**
     * 根据 系列 分类编号 查询整体
     * @param cateId
     * @return
     */
    private CateVo initCateVo(String cateId) {
        CateVo cateVo =  Builder.of(CateVo::new).build();
        Cate cate = cateMapper.selectById(cateId);
        BeanUtil.copyProperties(cate,cateVo);
        return cateVo;
    }

    /**
     * 根据创作者编号.将创作者查出
     * @param bindCreationId
     * @return
     */
    private CnfCreationdVo initCnfCreationVo(String bindCreationId) {
        CntCreationd cnfCreationd = cntCreationdService.getById(bindCreationId);
        CnfCreationdVo creationdVo = Builder.of(CnfCreationdVo::new).build();
        BeanUtil.copyProperties(cnfCreationd,creationdVo);
        return creationdVo;

    }

    /**
     * 根据藏品编号 将对应的媒体图片组合拼装
     * @param collectionId
     * @return
     */
    private List<MediaVo> initMediaVos(String collectionId) {
      return  mediaService.initMediaVos(collectionId, COLLECTION_MODEL_TYPE);
    }

    /**
     * 根据藏品编号 缩略图
     * @param collectionId
     * @return
     */
    private List<MediaVo> thumbnailImgMediaVos(String collectionId) {
        return  mediaService.thumbnailImgMediaVos(collectionId, COLLECTION_MODEL_TYPE);
    }

    /**
     * 根据藏品编号 3D图
     * @param collectionId
     * @return
     */
    private List<MediaVo> threeDimensionalMediaVos(String collectionId) {
        return  mediaService.threeDimensionalMediaVos(collectionId, COLLECTION_MODEL_TYPE);
    }

    /**
     * 根据藏品编号  查询所有关联的标签
     * @param collectionId
     * @return
     */
    private List<LableVo> initLableVos(String collectionId) {
        List<CollectionLable> collectionLables = collectionLableMapper.selectList(Wrappers.<CollectionLable>lambdaQuery().eq(CollectionLable::getCollectionId, collectionId));
        if (collectionLables.isEmpty())return ListUtil.empty();

        List<Lable> lableList = lableMapper.selectList(Wrappers.<Lable>lambdaQuery().in(Lable::getId, collectionLables.stream().map(item -> item.getLableId()).collect(Collectors.toList())));
        if (lableList.isEmpty())return ListUtil.empty();

        return lableList.parallelStream().map(item -> {
            LableVo lableVo = Builder.of(LableVo::new).build();
            BeanUtil.copyProperties(item, lableVo);
            return lableVo;
        }).collect(Collectors.toList());
    }
}
