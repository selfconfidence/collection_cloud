package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.manyun.business.design.mychain.MyChainService;
import com.manyun.business.design.pay.LLPayUtils;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.design.pay.ShandePay;
import com.manyun.business.domain.dto.*;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.OrderPayForm;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.OrderMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.RemoteSmsService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.*;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.mq.producers.deliver.DeliverProducer;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import com.manyun.common.redis.service.BuiCronService;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.System;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.ORDER_ORDINARY_STATUS;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.BoxStatus.UP_ACTION;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CollectionLink.OK_LINK;
import static com.manyun.common.core.enums.CollectionStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;
import static com.manyun.common.core.enums.ConsignmentStatus.LOCK_CONSIGN;
import static com.manyun.common.core.enums.ConsignmentStatus.OVER_CONSIGN;
import static com.manyun.common.core.enums.ConsignmentToPayStatus.WAIT_TO_PAY;
import static com.manyun.common.core.enums.OrderStatus.*;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;

/**
 * <p>
 * ?????? ???????????????
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISystemService systemService;

    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private ICntCreationdService creationdService;

    @Autowired
    private ObjectFactory<IBoxService> boxService;

    @Autowired
    private ObjectFactory<ICollectionService> collectionService;

    @Autowired
    private ObjectFactory<ICntConsignmentService> cntConsignmentServiceObjectFactory;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private  RootPay rootPay;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private ICntPostExcelService cntPostExcelService;

    @Autowired
    private IStepService stepService;

    @Autowired
    private DeliverProducer deliverProducer;


    @Autowired
    private ICntTarService cntTarService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private BuiCronService buiCronService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MyChainService chainService;

    @Override
    public TableDataInfo<OrderVo> pageQueryList(OrderQuery orderQuery, String userId) {
        List<Order> orderList = list(Wrappers.<Order>lambdaQuery()
                .eq(StringUtils.isNotBlank(userId), Order::getUserId, userId)
                .eq(orderQuery.getOrderStatus() != null, Order::getOrderStatus, orderQuery.getOrderStatus())
                .orderByDesc(Order::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(orderList.parallelStream().map(this::providerOrderVo).collect(Collectors.toList()),orderList);

    }

    private OrderVo providerOrderVo(Order order) {
        OrderVo orderVo = Builder.of(OrderVo::new).build();
        BeanUtil.copyProperties(order, orderVo);
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())) {
            CntCollection collection = collectionService.getObject().getById(order.getBuiId());
            CntCreationd creation = creationdService.getById(collection.getBindCreation());
            List<MediaVo> mediaVos = mediaService.initMediaVos(order.getBuiId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(order.getBuiId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            List<MediaVo> threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(order.getBuiId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            orderVo.setGoodsImg(mediaVos.get(0).getMediaUrl());
            orderVo.setThumbnailImg(thumbnailImgMediaVos.size()>0?thumbnailImgMediaVos.get(0).getMediaUrl():"");
            orderVo.setThreeDimensionalImg(threeDimensionalMediaVos.size()>0?threeDimensionalMediaVos.get(0).getMediaUrl():"");
            orderVo.setBindCreation(creation.getCreationName());
            orderVo.setCreationImg(creation.getHeadImage());
        }
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())) {
            List<MediaVo> mediaVos = mediaService.initMediaVos(order.getBuiId(), BOX_MODEL_TYPE);
            List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(order.getBuiId(), BOX_MODEL_TYPE);
            List<MediaVo> threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(order.getBuiId(), BOX_MODEL_TYPE);
            orderVo.setGoodsImg(mediaVos.get(0).getMediaUrl());
            orderVo.setThumbnailImg(thumbnailImgMediaVos.size()>0?thumbnailImgMediaVos.get(0).getMediaUrl():"");
            orderVo.setThreeDimensionalImg(threeDimensionalMediaVos.size()>0?threeDimensionalMediaVos.get(0).getMediaUrl():"");
        }
        return orderVo;
    }

    @Override
    public List<Order> checkUnpaidOrder(String userId) {
        return list(Wrappers.<Order>lambdaQuery()
                .select(Order::getId)
                .eq(StringUtils.isNotBlank(userId), Order::getUserId, userId)
                .eq(Order::getOrderStatus, WAIT_ORDER.getCode()));
    }


    /**
     * ???????????? ,?????????????????????
     * @param orderCreateDto  ??????????????????
     * @return
     */
    @Override
    public String createOrder(OrderCreateDto orderCreateDto) {
        return createOrder(orderCreateDto,systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class),(string)->{});
    }

    @Override
    public String createConsignmentOrder(OrderCreateDto orderCreateDto, Consumer<String> consumer){
        return createOrder(orderCreateDto,systemService.getVal(BusinessConstants.SystemTypeConstant.CONSIGNMENT_ORDER_TIME, Integer.class),consumer);
    }

    private String createOrder(OrderCreateDto orderCreateDto,Integer serviceVal, Consumer<String> consumer){
        Order order = Builder.of(Order::new).build();
        BeanUtil.copyProperties(orderCreateDto,order);
        order.createD(orderCreateDto.getUserId());
        // ??????????????????
        String idStr = IdUtil.getSnowflake().nextIdStr();
        order.setId(idStr);
        String orderNo = IdUtil.objectId();
        order.setOrderNo(orderNo);
        // ???????????????
        order.setOrderStatus(WAIT_ORDER.getCode());
        order.setPayTime(LocalDateTime.now());
        order.setMoneyBln(NumberUtil.add(0D));
        // ??????????????????????????????????????? ???????????????
       // Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(serviceVal, DelayLevelEnum.LEVEL_6);
        order.setEndTime(LocalDateTime.now().plusMinutes(DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES)));
        deliverProducer.sendCancelOrder(idStr,Builder.of(DeliverMsg::new).with(DeliverMsg::setBuiId,idStr).with(DeliverMsg::setBuiName,StrUtil.format("???????????????,???????????????:{}",idStr)).with(DeliverMsg::setResetHost, idStr).build() ,defaultEnum );
        redisService.setCacheObject(ORDER_ORDINARY_STATUS.concat(orderNo) ,order,DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES),TimeUnit.MINUTES);
        save(order);
        consumer.accept(idStr);
        return orderNo;
    }


    /**
     * ??????????????????
     * @param outHost
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyPaySuccess(String outHost) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("???????????????,????????????{},?????????????????????", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"???????????????????????????!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"??????????????????,?????????!");
        // ??????????????????, ??????????????? ?????????/?????????
        order.setOrderStatus(OVER_ORDER.getCode());

        // redis ????????????????????????
        Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(serviceVal, DelayLevelEnum.LEVEL_6);
        redisService.setCacheObject(ORDER_ORDINARY_STATUS.concat(outHost) ,order,DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES),TimeUnit.MINUTES);

        order.updateD(order.getUserId());
        // ???????????? ?????????????????????/??????????????????
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // ??????
            String userBoxId = userBoxService.bindOrderBox(order.getUserId(), order.getBuiId(), info, order.getGoodsNum());
            order.setUserBuiId(userBoxId);
            updateById(order);
            // ?????????????????????
            Box box = boxService.getObject().getById(order.getBuiId());
            // ?????????????????? ?????????????????? ??????????????????!
            if (Objects.nonNull(box.getPostTime()) && LocalDateTime.now().compareTo(box.getPublishTime()) < 0)
            cntPostExcelService.orderExec(order.getUserId(),order.getBuiId());
            // ????????????????????????
            if (StrUtil.isNotBlank(box.getTarId()))
                cntTarService.overSelf(order.getUserId(),box.getId());


            // ????????????????????????
            boxService.getObject().checkBalance(order.getBuiId(),order.getGoodsNum());
            return;
        }

        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
           // ??????
            String userCollectionId = userCollectionService.bindOrderCollection(order.getUserId(), order.getBuiId(), order.getCollectionName(), info, order.getGoodsNum());
            order.setUserBuiId(userCollectionId);
            updateById(order);
            // ?????????????????????
            CntCollection collection = collectionService.getObject().getById(order.getBuiId());
            if (Objects.nonNull(collection.getPostTime()) && LocalDateTime.now().compareTo(collection.getPublishTime()) < 0)
                cntPostExcelService.orderExec(order.getUserId(),order.getBuiId());

            // ????????????????????????
            if (StrUtil.isNotBlank(collection.getTarId()))
                cntTarService.overSelf(order.getUserId(),collection.getId());

            // ????????????????????????
          collectionService.getObject().checkBalance(order.getBuiId(),order.getGoodsNum() );
            return;
        }
        throw new IllegalStateException("not fount order good_type = " + goodsType);

    }



    /**
     * ?????????????????????,?????? ???????????? ??????????????????
     * ????????????,?????????????????? ??????????????????
     * ?????????????????? ??? ?????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void timeCancel(){
        List<Order> orderList = list(Wrappers.<Order>lambdaQuery().eq(Order::getOrderStatus, WAIT_ORDER.getCode()).lt(Order::getEndTime, LocalDateTime.now()));
        if (orderList.isEmpty()) return;
        List<CntConsignment> cntConsignments = cntConsignmentServiceObjectFactory.getObject().list(Wrappers.<CntConsignment>lambdaQuery().in(CntConsignment::getOrderId, orderList.parallelStream().map(item -> item.getId()).collect(Collectors.toSet())).eq(CntConsignment::getConsignmentStatus, LOCK_CONSIGN.getCode()));
        Set<String> cntConsignmentOrderIds = Sets.newHashSet();
        if (!cntConsignments.isEmpty()){
            // ??????????????????
            cntConsignmentOrderIds.addAll(cntConsignments.parallelStream().map(item -> item.getOrderId()).collect(Collectors.toSet()));
            // ??????????????????
            cntConsignmentServiceObjectFactory.getObject().reLoadConsignments(cntConsignments);
        }
        // ?????????????????????
        List<Order> consignmentOrders = orderList.parallelStream().filter(item -> cntConsignmentOrderIds.contains(item.getId())).collect(Collectors.toList());
        if (!consignmentOrders.isEmpty()){
            cancelBatchOrder(consignmentOrders, Boolean.FALSE);
        }
        List<Order> orders = orderList.parallelStream().filter(item -> !cntConsignmentOrderIds.contains(item.getId())).collect(Collectors.toList());
       if (!orders.isEmpty()){
           cancelBatchOrder(orders,Boolean.TRUE);
       }
/*        for (Order order : orderList) {
            String orderId = order.getId();
            try {
                if (cntConsignmentOrderIds.contains(order.getId())){
                    // ????????????
                    cancelOrder(orderId,Boolean.FALSE);
                }else{
                    // ????????????
                    cancelOrder(orderId,Boolean.TRUE);
                }
            }catch (Exception e){
                log.error("???????????????????????????????????????:{},???????????????:{}",orderId,e.getMessage());
            }

        }*/
        // ????????????????????????
     /*   List<Order> updateSOrder = orderList.parallelStream().map(item -> {
            item.setOrderStatus(CANCEL_ORDER.getCode());
            item.updateD(item.getUserId());
            return item;
        }).collect(Collectors.toList());
        updateBatchById(updateSOrder);
        // ??????????????? ++
        // ??????????????????
        HashMap<String, Integer> cacheBoxMap = Maps.newHashMap();
        // ??????????????????
        HashMap<String, Integer> cacheCollectionMap = Maps.newHashMap();
        for (Order order : orderList) {
            // ??????????????????????????????
            if (cntConsignmentOrderIds.contains(order.getId()))continue;

            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // ??????
                cacheBoxMap.merge(order.getBuiId(),order.getGoodsNum(),(oldVal,newVal) ->oldVal + newVal);
            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //??????
                cacheCollectionMap.merge(order.getBuiId(),order.getGoodsNum(),(oldVal,newVal) ->oldVal + newVal);
            }
        }
        // ????????????????????????
        if (!cacheBoxMap.isEmpty()) {
            Set<String> boxIds = cacheBoxMap.keySet();
            List<Box> boxList = boxService.getObject().listByIds(boxIds);
            for (Box box : boxList) {
                box.setBalance(box.getBalance() + cacheBoxMap.get(box.getId()));
                box.setSelfBalance(box.getSelfBalance() - cacheBoxMap.get(box.getId()));
            }
            boxService.getObject().updateBatchById(boxList);
        }

        if (!cacheCollectionMap.isEmpty()) {
            Set<String> collectionIds = cacheCollectionMap.keySet();
            List<CntCollection> cntCollections = collectionService.getObject().listByIds(collectionIds);
            for (CntCollection cntCollection : cntCollections) {
                cntCollection.setBalance(cntCollection.getBalance() + cacheCollectionMap.get(cntCollection.getId()));
                cntCollection.setSelfBalance(cntCollection.getSelfBalance() - cacheCollectionMap.get(cntCollection.getId()));
            }
            collectionService.getObject().updateBatchById(cntCollections);
        }
*/
    }

    private void cancelBatchOrder(List<Order> commOrders, Boolean reloadAssert) {
        for (Order commOrder : commOrders) {
            commOrder.setOrderStatus(CANCEL_ORDER.getCode());
            commOrder.updateD(commOrder.getUserId());
            BigDecimal moneyBln = commOrder.getMoneyBln();
            if (Objects.nonNull(moneyBln) && moneyBln.compareTo(NumberUtil.add(0D)) >=1){
                moneyService.orderBack(commOrder.getUserId(),moneyBln,StrUtil.format("???????????????,?????????????????? {},??????????????????!" , moneyBln));
            }
        }
        // ??????????????????
        if (reloadAssert)
            batchUpdateAssertVersion(commOrders);
        updateBatchById(commOrders);

    }

    private void batchUpdateAssertVersion(List<Order> commOrders) {
        Map<String, List<Order>> buiMaps = commOrders.parallelStream().collect(Collectors.groupingBy(Order::getBuiId));
        buiMaps.forEach((k,v)->{
            Integer goodsType = v.get(0).getGoodsType();
            Integer goodNum = v.parallelStream().map(item -> item.getGoodsNum()).reduce((a, b) -> a + b).orElseGet(() -> Integer.valueOf(0));
            batchUpdateAssert(k, goodsType,goodNum);
        });
    }

    /**
     * ???????????? ?????????????????????
     * @param outHost
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyPayConsignmentSuccess(String outHost) {
/*        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        Assert.isTrue(Objects.nonNull(order),"???????????????????????????!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"??????????????????,?????????!");
        // ??????????????????, ??????????????? ?????????/?????????
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        updateById(order);*/
        // 1. ?????? ??????????????????  // ????????? ??????
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("???????????????,????????????{},???????????????????????????", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"???????????????????????????!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"??????????????????,?????????!");
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));

        Assert.isTrue(Objects.nonNull(cntConsignment), "?????????????????????????????????,?????????!");
        boolean canTrade = false;
        if (Integer.valueOf(5).equals(order.getPayType())) {
            canTrade = moneyService.checkLlpayStatus(cntConsignment.getPayUserId()) && moneyService.checkLlpayStatus(cntConsignment.getSendUserId());
        }
        if (Integer.valueOf(6).equals(order.getPayType())) {
            canTrade = moneyService.checkSandAccountStatus(cntConsignment.getPayUserId()) && moneyService.checkSandAccountStatus(cntConsignment.getSendUserId());
        }

        // ??????????????????, ??????????????? ?????????/?????????
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        // ???????????? ?????????????????????/??????????????????
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // ??????
            String userBoxId = userBoxService.bindOrderBox(order.getUserId(), order.getBuiId(), info, order.getGoodsNum());
            order.setUserBuiId(userBoxId);
            updateById(order);
        }
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
            // ?????? ????????????????????????

            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(order.getBuiId());
            userCollection.setUserId(order.getUserId());
            userCollection.setSourceInfo(info);
            userCollection.setIsExist(USE_EXIST.getCode());
            userCollection.setCollectionName(order.getCollectionName());
            // ?????????
            userCollection.setIsLink(OK_LINK.getCode());
            userCollection.createD(order.getUserId());
            UserCollection sourceUserCollection = userCollectionService.getById(cntConsignment.getBuiId());
            userCollection.setCollectionHash(sourceUserCollection.getCollectionHash());
            userCollection.setCollectionNumber(sourceUserCollection.getCollectionNumber());
            userCollection.setTokeId(sourceUserCollection.getTokeId());
            userCollection.setRealCompany(sourceUserCollection.getRealCompany());
            userCollection.setLinkAddr(sourceUserCollection.getLinkAddr());
            userCollectionService.save(userCollection);
            order.setUserBuiId(userCollection.getId());
            updateById(order);
            // ??????????????????
            //StepDto.builder().buiId(userCollection.getLinkAddr()).userId(cntConsignment.getSendUserId()).modelType(COLLECTION_MODEL_TYPE).reMark("?????????").formHash(userCollection.getCollectionHash()).formInfo("???????????????????????????").build()
            // ????????????????????????
            // ??????
            CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
            chainService.tranForm(CallTranDto.builder().form(cntConsignment.getSendUserId()).account(cntUserDto.getId()).userKey(cntUserDto.getUserKey()).tokenId(Integer.valueOf(sourceUserCollection.getTokeId())).to(cntConsignment.getPayUserId()).build(), (tranFormHash)->{
                stepService.saveBatch(StepDto.builder().formTranHash(tranFormHash).buiId(userCollection.getLinkAddr()).userId(cntConsignment.getPayUserId()).modelType(COLLECTION_MODEL_TYPE).formHash(userCollection.getCollectionHash()).reMark("?????????").formInfo(info).build());
            });


        }

        // 2. ???????????????????????????????????? -- ????????????

        // 3. ?????????????????????????????????????????????  ??????
        cntConsignment.updateD(cntConsignment.getSendUserId());
        cntConsignment.setConsignmentStatus(OVER_CONSIGN.getCode());
        cntConsignment.setFormInfo("??????????????????!");
        cntConsignment.setToPay(WAIT_TO_PAY.getCode());
        consignmentService.updateById(cntConsignment);
       // CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
        //remoteSmsService.sendCommPhone(Builder.<SmsCommDto>of(SmsCommDto::new).with(SmsCommDto::setTemplateCode, BusinessConstants.SmsTemplateNumber.ASSERT_OK).with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().build()).with(SmsCommDto::setPhoneNumber,cntUserDto.getPhone()).build());

        // ??????????????????
        consignmentService.consignmentSuccess(cntConsignment.getId(), canTrade);


    }


    @Override
    public OrderInfoVo info(String id,String userId) {
        Order order = getById(id);
        Assert.isTrue(Objects.nonNull(order),"???????????????,?????????!");
        Assert.isTrue(order.getUserId().equals(userId),"?????????????????????????????????,?????????!");
        OrderInfoVo orderInfoVo = Builder.of(OrderInfoVo::new).build();
        BeanUtil.copyProperties(order, orderInfoVo );
        OrderCollectionInfoVo collectionInfoVo = Builder.of(OrderCollectionInfoVo::new).build();
        // ?????????????????????????????????????????????????????????
        //??????????????????
        if (order.getGoodsType().equals(BusinessConstants.ModelTypeConstant.BOX_TAYPE)){
            orderInfoVo.setBoxVo(boxService.getObject().info(order.getBuiId(),null));
        }

        if (order.getGoodsType().equals(BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE)){
            CollectionAllVo collectionAllVo = collectionService.getObject().info(order.getBuiId());
            collectionInfoVo.setCollectionVo(collectionAllVo.getCollectionVo());
            collectionInfoVo.setCollectionInfoVo(collectionAllVo.getCollectionInfoVo());
            if (StrUtil.isNotBlank(order.getUserBuiId())){
                UserCollectionForVo userCollectionForVo = collectionService.getObject().userCollectionInfo(order.getUserBuiId());
                collectionInfoVo.setUserCollectionVo(userCollectionForVo.getUserCollectionVo());
                collectionInfoVo.setStepVos(userCollectionForVo.getStepVos());
            }

        }

        orderInfoVo.setOrderCollectionInfoVo(collectionInfoVo);
        return orderInfoVo;
    }


    /**
     * ????????????
     * @param orderPayForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    ///ShandePayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(orderPayForm.getReturnUrl())
    public PayVo unifiedOrder(OrderPayForm orderPayForm,String userId) {
        if (Integer.valueOf(5).equals(orderPayForm.getPayType())) {
            Assert.isTrue(moneyService.checkLlpayStatus(userId), "????????????????????????");
        }
        if (Integer.valueOf(6).equals(orderPayForm.getPayType())) {
            Assert.isTrue(moneyService.checkSandAccountStatus(userId), "???????????????????????????");
        }
        Order order = getById(orderPayForm.getOrderId());
        Assert.isFalse(Integer.valueOf(5).equals(orderPayForm.getPayType()) && order.getMoneyBln().compareTo(NumberUtil.add(0D)) >0, "???????????????????????????????????????");
        checkUnified(order,userId,orderPayForm.getPayPass(),orderPayForm.getPayType());
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));
        boolean canTrade = false;
        boolean canSandTrade = false;
        boolean c2c = false;
        String sendUserId = null;
        BigDecimal serviceCharge = BigDecimal.ZERO;
        if (cntConsignment != null) {
            canTrade = moneyService.checkLlpayStatus(userId) && moneyService.checkLlpayStatus(cntConsignment.getSendUserId());
            canSandTrade  = moneyService.checkSandAccountStatus(userId) && moneyService.checkSandAccountStatus(cntConsignment.getSendUserId());
            sendUserId = cntConsignment.getSendUserId();
            if (Integer.valueOf(5).equals(orderPayForm.getPayType())) {
                Assert.isTrue(canTrade, "???????????????????????????????????????????????????????????????");
                serviceCharge = cntConsignment.getServerCharge();
            }
            if (Integer.valueOf(6).equals(orderPayForm.getPayType())) {
                Assert.isTrue(canSandTrade, "????????????????????????????????????????????????????????????????????????");
                serviceCharge = cntConsignment.getServerCharge();
                c2c = true;
            }
        }

        ShandePayEnum shandePayEnum =  switchCase(order.getId(),orderPayForm.getReturnUrl(), orderPayForm.getReturnUrl());
        LianLianPayEnum lianLianPayEnum =  switchCaseLianLian(order.getId(),orderPayForm.getReturnUrl(), orderPayForm.getReturnUrl());
        SandAccountEnum sandAccountEnum = switchCaseSandAccount(order.getId(),orderPayForm.getReturnUrl(), orderPayForm.getReturnUrl());

        // ?????????????????????????????????
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(orderPayForm.getPayType())
                        .realPayMoney(order.getOrderAmount().subtract(order.getMoneyBln()))
                        .outHost(order.getOrderNo())
                        .shandePayEnum(shandePayEnum)
                        .canTrade(canTrade)
                        .receiveUserId(sendUserId)
                        .serviceCharge(serviceCharge)
                        .lianlianPayEnum(lianLianPayEnum)
                        .sandAccountEnum(sandAccountEnum)
                        .goodsName(order.getCollectionName())
                        .c2c(c2c)
                        .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                        .userId(userId).build());
        // ?????????????????? ??????????????? ????????????????????????????????????
        order.setMoneyBln(order.getMoneyBln().add(payVo.getMoneyBln()));
        order.setPayType(orderPayForm.getPayType());
        order.setTxnSeqno(payVo.getTxnSeqno());
        updateById(order);
        if ( StrUtil.isBlank(payVo.getBody())){
            // ?????????????????? ?????????????????????????
            invokerSuccess(order, payVo);
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // ??????
                Box box = boxService.getObject().getById(order.getBuiId());
                 title = StrUtil.format("????????? {} ??????!", box.getBoxTitle());
                 form = StrUtil.format("????????????{};????????? {} ??????!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //??????
                CntCollection cntCollection = collectionService.getObject().getById(order.getBuiId());
                title = StrUtil.format("????????? {} ??????!", cntCollection.getCollectionName());
                 form = StrUtil.format("????????????{};????????? {} ??????!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }

    private LianLianPayEnum switchCaseLianLian(String id, String returnUrl, String defaultUrl) {
        // ???????????? ??????????????????????????????
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, id));
        if (cntConsignment != null)
            return LianLianPayEnum.CONSIGNMENT_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
        else
            return LianLianPayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
    }

    private ShandePayEnum switchCase(String id,String returnUrl,String defaultUrl) {
        // ???????????? ??????????????????????????????
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, id));
        if (cntConsignment != null)
            return ShandePayEnum.CONSIGNMENT_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
        else
            return ShandePayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
    }

    private SandAccountEnum switchCaseSandAccount(String id, String returnUrl, String defaultUrl) {
        // ???????????? ??????????????????????????????
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, id));
        if (cntConsignment != null)
            return SandAccountEnum.CONSIGNMENT_SANDACCOUNT_PAY.setReturnUrl(returnUrl,defaultUrl);
        else
            return SandAccountEnum.COLLECTION_BOX_SANDACCOUNT_PAY.setReturnUrl(returnUrl,defaultUrl);
    }

    /**
     * ????????? ??????????????????????????????
     * @param order
     * @param payVo
     */
    private void invokerSuccess(Order order, PayVo payVo) {
        // ???????????? ??????????????????????????????
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));
        if (cntConsignment != null)
            notifyPayConsignmentSuccess(order.getOrderNo());
        else
             notifyPaySuccess(payVo.getOutHost());
    }


    /**
     * ????????????
     *
     * 1. ??????????????????
     * 2. ????????????????????????
     * 3. ??? (??????|????????????????????????)
     * ????????????
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String id) {
        // ????????????? ?????????????
        ICntConsignmentService cntConsignmentServiceObjectFactoryObject = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = cntConsignmentServiceObjectFactoryObject.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId,id).eq(CntConsignment::getConsignmentStatus, LOCK_CONSIGN.getCode()));
        if (Objects.isNull(cntConsignment)){
            // ??????
            cancelOrder(id,Boolean.TRUE);
        }else{
            // ??????
            cancelOrder(id,Boolean.FALSE);
            cntConsignmentServiceObjectFactoryObject.reLoadConsignments(Arrays.asList(cntConsignment));
        }

    }

    /**
     * ??????????????????????????????????????????????????????!
     * @param assertId
     * @param userId
     * @return
     */
    @Override
    public int overCount(String assertId, String userId) {
       return Long.valueOf(count(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, userId).eq(Order::getBuiId, assertId).eq(Order::getOrderStatus, OVER_ORDER.getCode()))).intValue();
    }

    /**
     * ????????????????????????
     * @param orderPayForm
     * @param userId
     * @return
     */
/*
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVo consignmentUnifiedOrder(OrderPayForm orderPayForm, String userId) {
        Order order = getById(orderPayForm.getOrderId());
        checkUnified(order,userId);
        // ?????????????????????????????????
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(orderPayForm.getPayType())
                        .realPayMoney(order.getOrderAmount().subtract(order.getMoneyBln()))
                        .outHost(order.getOrderNo())
                        .aliPayEnum(BOX_ALI_PAY)
                        .wxPayEnum(BOX_WECHAT_PAY)
                        .userId(userId).build());
        // ?????????????????? ??????????????? ????????????????????????????????????
        order.setMoneyBln(payVo.getMoneyBln());
        updateById(order);
        if ( StrUtil.isBlank(payVo.getBody())){
            // ??????????????????
            notifyPayConsignmentSuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // ??????
                Box box = boxService.getObject().getById(order.getBuiId());
                title = StrUtil.format("????????? {} ??????!", box.getBoxTitle());
                form = StrUtil.format("????????????{};????????? {} ??????!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //??????
                CntCollection cntCollection = collectionService.getObject().getById(order.getBuiId());
                title = StrUtil.format("????????? {} ??????!", cntCollection.getCollectionName());
                form = StrUtil.format("????????????{};????????? {} ??????!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }
*/

    private void cancelOrder(String id,Boolean reloadAssert) {
        Order order = getById(id);
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"???????????????????????????!");
        order.setOrderStatus(CANCEL_ORDER.getCode());
        order.updateD(order.getUserId());
        if (!reloadAssert) {
            order.setInnerNumber(1);
        }
        updateById(order);
        BigDecimal moneyBln = order.getMoneyBln();
        if (Objects.nonNull(moneyBln) && moneyBln.compareTo(NumberUtil.add(0D)) >=1){
            moneyService.orderBack(order.getUserId(),moneyBln,StrUtil.format("???????????????,?????????????????? {},??????????????????!" , moneyBln));
        }
        // ??????????????????
        if (reloadAssert)
        batchUpdateAssert(order.getBuiId(),order.getGoodsType(),order.getGoodsNum());

        // no close exe
        try {
            if (order.getPayType().equals(5)){
                // ?????????????????????
                LLPayUtils.closePayment(order.getTxnSeqno());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void batchUpdateAssert(String buiId, Integer goodsType, Integer goodsNum) {

        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)){
            // ??????
           // IBoxService boxServiceReal = boxService.getObject();
           // Box box = boxServiceReal.getById(buiId);
            //boxServiceReal.update(Wrappers.<Box>lambdaUpdate().eq(Box::getId, buiId).set(!box.getStatusBy().equals(DOWN_ACTION.getCode()),Box::getStatusBy,UP_ACTION.getCode()).set(Box::getBalance, box.getBalance() + goodsNum).set(Box::getSelfBalance, box.getSelfBalance() - goodsNum));
            buiCronService.doBuiIcrementBalanceCache(BOX_MODEL_TYPE, buiId, goodsNum);
        }
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)){
            // ??????
            //ICollectionService collectionServiceReal = collectionService.getObject();
           // CntCollection cntCollection = collectionServiceReal.getById(buiId);
            //collectionServiceReal.update(Wrappers.<CntCollection>lambdaUpdate().eq(CntCollection::getId, buiId).set(!cntCollection.getStatusBy().equals(DOWN_ACTION.getCode()),CntCollection::getStatusBy,UP_ACTION.getCode()).set(CntCollection::getBalance, cntCollection.getBalance() + goodsNum).set(CntCollection::getSelfBalance, cntCollection.getSelfBalance() - goodsNum));
            buiCronService.doBuiIcrementBalanceCache(COLLECTION_MODEL_TYPE, buiId, goodsNum);
        }
    }

    private void checkUnified(Order order, String userId,String payPass,Integer payType) {
        CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        Assert.isTrue(userId.equals(order.getUserId()), "???????????????,???????????????!" );
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"???????????????????????????!");
        Assert.isTrue(order.getEndTime().compareTo(LocalDateTime.now()) >=0,"?????????????????????,?????????????????????!");
        if (MONEY_TAPE.getCode().equals(payType)) Assert.isTrue(SecurityUtils.matchesPassword(payPass, cntUserDto.getPayPass()),"??????????????????,?????????!");
    }


}
