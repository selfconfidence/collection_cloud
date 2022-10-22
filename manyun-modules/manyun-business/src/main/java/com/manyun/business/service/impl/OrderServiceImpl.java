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
import com.manyun.common.core.enums.DelayLevelEnum;
import com.manyun.common.core.enums.LianLianPayEnum;
import com.manyun.common.core.enums.PayTypeEnum;
import com.manyun.common.core.enums.ShandePayEnum;
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
 * 订单 服务实现类
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
     * 创建订单 ,进行订单初始化
     * @param orderCreateDto  实际支付金额
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
        // 生成支付编号
        String idStr = IdUtil.getSnowflake().nextIdStr();
        order.setId(idStr);
        String orderNo = IdUtil.objectId();
        order.setOrderNo(orderNo);
        // 待支付状态
        order.setOrderStatus(WAIT_ORDER.getCode());
        order.setPayTime(LocalDateTime.now());
        order.setMoneyBln(NumberUtil.add(0D));
        // 截止到什么是否到达付款时间 分钟为单位
       // Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(serviceVal, DelayLevelEnum.LEVEL_6);
        order.setEndTime(LocalDateTime.now().plusMinutes(DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES)));
        deliverProducer.sendCancelOrder(idStr,Builder.of(DeliverMsg::new).with(DeliverMsg::setBuiId,idStr).with(DeliverMsg::setBuiName,StrUtil.format("订单取消了,订单编号为:{}",idStr)).with(DeliverMsg::setResetHost, idStr).build() ,defaultEnum );
        redisService.setCacheObject(ORDER_ORDINARY_STATUS.concat(orderNo) ,order,DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES),TimeUnit.MINUTES);
        save(order);
        consumer.accept(idStr);
        return orderNo;
    }


    /**
     * 完成订单操作
     * @param outHost
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyPaySuccess(String outHost) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("从平台购买,本次消费{},来源为平台发售", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());

        // redis 缓存订单状态优化
        Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(serviceVal, DelayLevelEnum.LEVEL_6);
        redisService.setCacheObject(ORDER_ORDINARY_STATUS.concat(outHost) ,order,DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES),TimeUnit.MINUTES);

        order.updateD(order.getUserId());
        // 开始绑定 根据购买的藏品/盲盒进行绑定
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // 盲盒
            String userBoxId = userBoxService.bindOrderBox(order.getUserId(), order.getBuiId(), info, order.getGoodsNum());
            order.setUserBuiId(userBoxId);
            updateById(order);
            // 提前购记录次数
            Box box = boxService.getObject().getById(order.getBuiId());
            // 如果已经过了 正常发售时间 就不用记录了!
            if (Objects.nonNull(box.getPostTime()) && LocalDateTime.now().compareTo(box.getPublishTime()) < 0)
            cntPostExcelService.orderExec(order.getUserId(),order.getBuiId());
            // 是否是抽签购的？
            if (StrUtil.isNotBlank(box.getTarId()))
                cntTarService.overSelf(order.getUserId(),box.getId());


            // 真实扣减库存操作
            boxService.getObject().checkBalance(order.getBuiId(),order.getGoodsNum());
            return;
        }

        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
           // 藏品
            String userCollectionId = userCollectionService.bindOrderCollection(order.getUserId(), order.getBuiId(), order.getCollectionName(), info, order.getGoodsNum());
            order.setUserBuiId(userCollectionId);
            updateById(order);
            // 提前购记录次数
            CntCollection collection = collectionService.getObject().getById(order.getBuiId());
            if (Objects.nonNull(collection.getPostTime()) && LocalDateTime.now().compareTo(collection.getPublishTime()) < 0)
                cntPostExcelService.orderExec(order.getUserId(),order.getBuiId());

            // 是否是抽签购的？
            if (StrUtil.isNotBlank(collection.getTarId()))
                cntTarService.overSelf(order.getUserId(),collection.getId());

            // 真实扣减库存操作
          collectionService.getObject().checkBalance(order.getBuiId(),order.getGoodsNum() );
            return;
        }
        throw new IllegalStateException("not fount order good_type = " + goodsType);

    }



    /**
     * 定时任务可调用,监视 锁仓时间 及时释放库存
     * 订单取消,将对应的库存 再次加上即可
     * 更改订单状态 为 已取消
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void timeCancel(){
        List<Order> orderList = list(Wrappers.<Order>lambdaQuery().eq(Order::getOrderStatus, WAIT_ORDER.getCode()).lt(Order::getEndTime, LocalDateTime.now()));
        if (orderList.isEmpty()) return;
        List<CntConsignment> cntConsignments = cntConsignmentServiceObjectFactory.getObject().list(Wrappers.<CntConsignment>lambdaQuery().in(CntConsignment::getOrderId, orderList.parallelStream().map(item -> item.getId()).collect(Collectors.toSet())).eq(CntConsignment::getConsignmentStatus, LOCK_CONSIGN.getCode()));
        Set<String> cntConsignmentOrderIds = Sets.newHashSet();
        if (!cntConsignments.isEmpty()){
            // 寄售相关判定
            cntConsignmentOrderIds.addAll(cntConsignments.parallelStream().map(item -> item.getOrderId()).collect(Collectors.toSet()));
            // 寄售信息修改
            cntConsignmentServiceObjectFactory.getObject().reLoadConsignments(cntConsignments);
        }
        // 优化进行批处理
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
                    // 寄售订单
                    cancelOrder(orderId,Boolean.FALSE);
                }else{
                    // 普通订单
                    cancelOrder(orderId,Boolean.TRUE);
                }
            }catch (Exception e){
                log.error("定时订单取消失败订单编号为:{},错误原因为:{}",orderId,e.getMessage());
            }

        }*/
        // 批量更改订单状态
     /*   List<Order> updateSOrder = orderList.parallelStream().map(item -> {
            item.setOrderStatus(CANCEL_ORDER.getCode());
            item.updateD(item.getUserId());
            return item;
        }).collect(Collectors.toList());
        updateBatchById(updateSOrder);
        // 对应的库存 ++
        // 缓存盲盒库存
        HashMap<String, Integer> cacheBoxMap = Maps.newHashMap();
        // 缓存藏品库存
        HashMap<String, Integer> cacheCollectionMap = Maps.newHashMap();
        for (Order order : orderList) {
            // 如果是寄售的额外计算
            if (cntConsignmentOrderIds.contains(order.getId()))continue;

            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // 盲盒
                cacheBoxMap.merge(order.getBuiId(),order.getGoodsNum(),(oldVal,newVal) ->oldVal + newVal);
            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //藏品
                cacheCollectionMap.merge(order.getBuiId(),order.getGoodsNum(),(oldVal,newVal) ->oldVal + newVal);
            }
        }
        // 开始批量更新库存
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
                moneyService.orderBack(commOrder.getUserId(),moneyBln,StrUtil.format("订单已取消,此产生的消费 {},已经退还余额!" , moneyBln));
            }
        }
        // 刷新对应库存
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
     * 寄售市场 回调订单成功项
     * @param outHost
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyPayConsignmentSuccess(String outHost) {
/*        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        updateById(order);*/
        // 1. 修改 当前订单状态  // 绑定到 买方
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("从平台购买,本次消费{},来源为平台寄售市场", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));

        Assert.isTrue(Objects.nonNull(cntConsignment), "找不到对应寄售交易信息,请核实!");
        boolean canTrade = false;
        if (Integer.valueOf(5).equals(order.getPayType())) {
            canTrade = moneyService.checkLlpayStatus(cntConsignment.getPayUserId()) && moneyService.checkLlpayStatus(cntConsignment.getSendUserId());
        }

        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        // 开始绑定 根据购买的藏品/盲盒进行绑定
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // 盲盒
            String userBoxId = userBoxService.bindOrderBox(order.getUserId(), order.getBuiId(), info, order.getGoodsNum());
            order.setUserBuiId(userBoxId);
            updateById(order);
        }
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
            // 藏品 寄售是转赠的意思

            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(order.getBuiId());
            userCollection.setUserId(order.getUserId());
            userCollection.setSourceInfo(info);
            userCollection.setIsExist(USE_EXIST.getCode());
            userCollection.setCollectionName(order.getCollectionName());
            // 初始化
            userCollection.setIsLink(OK_LINK.getCode());
            userCollection.createD(order.getUserId());
            UserCollection sourceUserCollection = userCollectionService.getById(cntConsignment.getBuiId());
            userCollection.setCollectionHash(sourceUserCollection.getCollectionHash());
            userCollection.setCollectionNumber(sourceUserCollection.getCollectionNumber());
            userCollection.setRealCompany(sourceUserCollection.getRealCompany());
            userCollection.setLinkAddr(sourceUserCollection.getLinkAddr());
            userCollectionService.save(userCollection);
            order.setUserBuiId(userCollection.getId());
            updateById(order);
            // 增加流转记录
            stepService.saveBatch(StepDto.builder().buiId(userCollection.getLinkAddr()).userId(cntConsignment.getSendUserId()).modelType(COLLECTION_MODEL_TYPE).reMark("转让方").formHash(userCollection.getCollectionHash()).formInfo("寄售市场已寄售成功").build()
                    ,StepDto.builder().buiId(userCollection.getLinkAddr()).userId(cntConsignment.getPayUserId()).modelType(COLLECTION_MODEL_TYPE).formHash(userCollection.getCollectionHash()).reMark("受让方").formInfo(info).build());

        }

        // 2. 寄售人已经解除绑定关系了 -- 无需判定

        // 3. 修改当前寄售信息，等待后台审核  打款
        cntConsignment.updateD(cntConsignment.getSendUserId());
        cntConsignment.setConsignmentStatus(OVER_CONSIGN.getCode());
        cntConsignment.setFormInfo("买方已经支付!");
        cntConsignment.setToPay(WAIT_TO_PAY.getCode());
        consignmentService.updateById(cntConsignment);
       // CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
        //remoteSmsService.sendCommPhone(Builder.<SmsCommDto>of(SmsCommDto::new).with(SmsCommDto::setTemplateCode, BusinessConstants.SmsTemplateNumber.ASSERT_OK).with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().build()).with(SmsCommDto::setPhoneNumber,cntUserDto.getPhone()).build());

        // 直接审核成功
        consignmentService.consignmentSuccess(cntConsignment.getId(), canTrade);


    }


    @Override
    public OrderInfoVo info(String id,String userId) {
        Order order = getById(id);
        Assert.isTrue(Objects.nonNull(order),"订单不存在,请核实!");
        Assert.isTrue(order.getUserId().equals(userId),"此订单不是当前用户订单,请核实!");
        OrderInfoVo orderInfoVo = Builder.of(OrderInfoVo::new).build();
        BeanUtil.copyProperties(order, orderInfoVo );
        OrderCollectionInfoVo collectionInfoVo = Builder.of(OrderCollectionInfoVo::new).build();
        // 根据状态区分查询不同的数据结构给移动端
        //盲盒始终唯一
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
     * 统一下单
     * @param orderPayForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    ///ShandePayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(orderPayForm.getReturnUrl())
    public PayVo unifiedOrder(OrderPayForm orderPayForm,String userId) {
        Order order = getById(orderPayForm.getOrderId());
        Assert.isFalse(Integer.valueOf(5).equals(orderPayForm.getPayType()) && order.getMoneyBln().compareTo(NumberUtil.add(0D)) >0, "当前状态暂不支持此支付方式");
        checkUnified(order,userId,orderPayForm.getPayPass(),orderPayForm.getPayType());
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));
        boolean canTrade = false;
        String sendUserId = null;
        BigDecimal serviceCharge = BigDecimal.ZERO;
        if (cntConsignment != null) {
            canTrade = moneyService.checkLlpayStatus(userId) && moneyService.checkLlpayStatus(cntConsignment.getSendUserId());
            sendUserId = cntConsignment.getSendUserId();
            if (Integer.valueOf(5).equals(orderPayForm.getPayType())) {
                Assert.isTrue(canTrade, "暂未开通连连支付，请选择其他支付方式");
                serviceCharge = cntConsignment.getServerCharge();
            }
        }

        ShandePayEnum shandePayEnum =  switchCase(order.getId(),orderPayForm.getReturnUrl(), orderPayForm.getReturnUrl());
        LianLianPayEnum lianLianPayEnum =  switchCaseLianLian(order.getId(),orderPayForm.getReturnUrl(), orderPayForm.getReturnUrl());

        // 判定用户的余额是否充足
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
                        .goodsName(order.getCollectionName())
                        .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                        .userId(userId).build());
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        order.setMoneyBln(order.getMoneyBln().add(payVo.getMoneyBln()));
        order.setPayType(orderPayForm.getPayType());
        updateById(order);
        if ( StrUtil.isBlank(payVo.getBody())){
            // 调用完成订单 是寄售还是普通呢?
            invokerSuccess(order, payVo);
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // 盲盒
                Box box = boxService.getObject().getById(order.getBuiId());
                 title = StrUtil.format("购买了 {} 盲盒!", box.getBoxTitle());
                 form = StrUtil.format("使用余额{};购买了 {} 盲盒!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //藏品
                CntCollection cntCollection = collectionService.getObject().getById(order.getBuiId());
                title = StrUtil.format("购买了 {} 藏品!", cntCollection.getCollectionName());
                 form = StrUtil.format("使用余额{};购买了 {} 藏品!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }

    private LianLianPayEnum switchCaseLianLian(String id, String returnUrl, String defaultUrl) {
        // 唯一验证 从寄售中获取这个订单
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, id));
        if (cntConsignment != null)
            return LianLianPayEnum.CONSIGNMENT_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
        else
            return LianLianPayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
    }

    private ShandePayEnum switchCase(String id,String returnUrl,String defaultUrl) {
        // 唯一验证 从寄售中获取这个订单
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, id));
        if (cntConsignment != null)
            return ShandePayEnum.CONSIGNMENT_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
        else
            return ShandePayEnum.COLLECTION_BOX_SHANDE_PAY.setReturnUrl(returnUrl,defaultUrl);
    }

    /**
     * 回调是 寄售，还是普通订单？
     * @param order
     * @param payVo
     */
    private void invokerSuccess(Order order, PayVo payVo) {
        // 唯一验证 从寄售中获取这个订单
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));
        if (cntConsignment != null)
            notifyPayConsignmentSuccess(order.getOrderNo());
        else
             notifyPaySuccess(payVo.getOutHost());
    }


    /**
     * 取消订单
     *
     * 1. 订单状态变更
     * 2. 订单余额直接退还
     * 3. 将 (盲盒|藏品库存重新加上)
     * 取消订单
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String id) {
        // 是寄售吗? 还是普通?
        ICntConsignmentService cntConsignmentServiceObjectFactoryObject = cntConsignmentServiceObjectFactory.getObject();
        CntConsignment cntConsignment = cntConsignmentServiceObjectFactoryObject.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId,id).eq(CntConsignment::getConsignmentStatus, LOCK_CONSIGN.getCode()));
        if (Objects.isNull(cntConsignment)){
            // 普通
            cancelOrder(id,Boolean.TRUE);
        }else{
            // 寄售
            cancelOrder(id,Boolean.FALSE);
            cntConsignmentServiceObjectFactoryObject.reLoadConsignments(Arrays.asList(cntConsignment));
        }

    }

    /**
     * 根据资产编号，查询用户已经完成的订单!
     * @param assertId
     * @param userId
     * @return
     */
    @Override
    public int overCount(String assertId, String userId) {
       return Long.valueOf(count(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, userId).eq(Order::getBuiId, assertId).eq(Order::getOrderStatus, OVER_ORDER.getCode()))).intValue();
    }

    /**
     * 寄售订单下单支付
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
        // 判定用户的余额是否充足
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(orderPayForm.getPayType())
                        .realPayMoney(order.getOrderAmount().subtract(order.getMoneyBln()))
                        .outHost(order.getOrderNo())
                        .aliPayEnum(BOX_ALI_PAY)
                        .wxPayEnum(BOX_WECHAT_PAY)
                        .userId(userId).build());
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        order.setMoneyBln(payVo.getMoneyBln());
        updateById(order);
        if ( StrUtil.isBlank(payVo.getBody())){
            // 调用完成订单
            notifyPayConsignmentSuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // 盲盒
                Box box = boxService.getObject().getById(order.getBuiId());
                title = StrUtil.format("购买了 {} 盲盒!", box.getBoxTitle());
                form = StrUtil.format("使用余额{};购买了 {} 盲盒!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //藏品
                CntCollection cntCollection = collectionService.getObject().getById(order.getBuiId());
                title = StrUtil.format("购买了 {} 藏品!", cntCollection.getCollectionName());
                form = StrUtil.format("使用余额{};购买了 {} 藏品!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }
*/

    private void cancelOrder(String id,Boolean reloadAssert) {
        Order order = getById(id);
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"待支付订单才可取消!");
        order.setOrderStatus(CANCEL_ORDER.getCode());
        order.updateD(order.getUserId());
        if (!reloadAssert) {
            order.setInnerNumber(1);
        }
        updateById(order);
        BigDecimal moneyBln = order.getMoneyBln();
        if (Objects.nonNull(moneyBln) && moneyBln.compareTo(NumberUtil.add(0D)) >=1){
            moneyService.orderBack(order.getUserId(),moneyBln,StrUtil.format("订单已取消,此产生的消费 {},已经退还余额!" , moneyBln));
        }
        // 刷新对应库存
        if (reloadAssert)
        batchUpdateAssert(order.getBuiId(),order.getGoodsType(),order.getGoodsNum());

        // no close exe
        try {
            if (order.getPayType().equals(5)){
                // 连连，进行关闭
                LLPayUtils.closePayment(order.getTxnSeqno());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void batchUpdateAssert(String buiId, Integer goodsType, Integer goodsNum) {

        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)){
            // 盲盒
           // IBoxService boxServiceReal = boxService.getObject();
           // Box box = boxServiceReal.getById(buiId);
            //boxServiceReal.update(Wrappers.<Box>lambdaUpdate().eq(Box::getId, buiId).set(!box.getStatusBy().equals(DOWN_ACTION.getCode()),Box::getStatusBy,UP_ACTION.getCode()).set(Box::getBalance, box.getBalance() + goodsNum).set(Box::getSelfBalance, box.getSelfBalance() - goodsNum));
            buiCronService.doBuiIcrementBalanceCache(BOX_MODEL_TYPE, buiId, goodsNum);
        }
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)){
            // 藏品
            //ICollectionService collectionServiceReal = collectionService.getObject();
           // CntCollection cntCollection = collectionServiceReal.getById(buiId);
            //collectionServiceReal.update(Wrappers.<CntCollection>lambdaUpdate().eq(CntCollection::getId, buiId).set(!cntCollection.getStatusBy().equals(DOWN_ACTION.getCode()),CntCollection::getStatusBy,UP_ACTION.getCode()).set(CntCollection::getBalance, cntCollection.getBalance() + goodsNum).set(CntCollection::getSelfBalance, cntCollection.getSelfBalance() - goodsNum));
            buiCronService.doBuiIcrementBalanceCache(COLLECTION_MODEL_TYPE, buiId, goodsNum);
        }
    }

    private void checkUnified(Order order, String userId,String payPass,Integer payType) {
        CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        Assert.isTrue(userId.equals(order.getUserId()), "订单被篡改,请联系平台!" );
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"待支付订单才可支付!");
        Assert.isTrue(order.getEndTime().compareTo(LocalDateTime.now()) >=0,"付款时间已截止,请核实订单状态!");
        if (MONEY_TAPE.getCode().equals(payType)) Assert.isTrue(SecurityUtils.matchesPassword(payPass, cntUserDto.getPayPass()),"支付密码错误,请核实!");
    }


}
