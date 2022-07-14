package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.query.OrderQuery;
import com.manyun.business.domain.vo.OrderVo;
import com.manyun.business.mapper.OrderMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.manyun.common.core.enums.ConsignmentStatus.LOCK_CONSIGN;
import static com.manyun.common.core.enums.ConsignmentStatus.OVER_CONSIGN;
import static com.manyun.common.core.enums.ConsignmentToPayStatus.WAIT_TO_PAY;
import static com.manyun.common.core.enums.OrderStatus.*;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
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

    @Override
    public TableDataInfo<OrderVo> pageQueryList(OrderQuery orderQuery, String userId) {
        List<Order> orderList = list(Wrappers.<Order>lambdaQuery()
                .eq(StringUtils.isNotBlank(userId), Order::getUserId, userId)
                .eq(orderQuery.getOrderStatus() != null && orderQuery.getOrderStatus() != 0, Order::getOrderStatus, orderQuery.getOrderStatus())
                .orderByDesc(Order::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(orderList.parallelStream().map(this::providerOrderVo).collect(Collectors.toList()),orderList);

    }

    private OrderVo providerOrderVo(Order order) {
        OrderVo orderVo = Builder.of(OrderVo::new).build();
        BeanUtil.copyProperties(order, orderVo);
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())) {
            CntCollection collection = collectionService.getObject().getById(order.getBuiId());
            CntCreationd creation = creationdService.getById(collection.getBindCreation());
            orderVo.setBindCreation(creation.getCreationName());
            orderVo.setCreationImg(creation.getHeadImage());
        }
        return orderVo;
    }

    @Override
    public List<Order> checkUnpaidOrder(String userId) {
        return list(Wrappers.<Order>lambdaQuery()
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
        // 截止到什么是否到达付款时间 小时为单位
       // Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.ORDER_END_TIME, Integer.class);
        order.setEndTime(LocalDateTime.now().plusHours(serviceVal));
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
    public synchronized void notifyPaySuccess(String outHost) {
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        String info = StrUtil.format("从平台购买,本次消费{},来源为平台发售", order.getOrderAmount().toString());
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        updateById(order);
        // 开始绑定 根据购买的藏品/盲盒进行绑定
        Integer goodsType = order.getGoodsType();
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(goodsType)) {
            // 盲盒
            userBoxService.bindBox(order.getUserId(),order.getBuiId(),info,order.getGoodsNum());
            return;
        }

        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(goodsType)) {
           // 藏品
            userCollectionService.bindCollection(order.getUserId(),order.getCollectionName(),order.getBuiId(),info,order.getGoodsNum());
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
    public synchronized  void timeCancel(){
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
        // 批量更改订单状态
        List<Order> updateSOrder = orderList.parallelStream().map(item -> {
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
                cntCollection.setBalance(cntCollection.getBalance() + cacheBoxMap.get(cntCollection.getId()));
                cntCollection.setSelfBalance(cntCollection.getSelfBalance() - cacheBoxMap.get(cntCollection.getId()));
            }
            collectionService.getObject().updateBatchById(cntCollections);
        }

    }

    /**
     * 寄售市场 回调订单成功项
     * @param outHost
     */
    @Override
    public void notifyPayConsignmentSuccess(String outHost) {
/*        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        Assert.isTrue(Objects.nonNull(order),"找不到对应订单编号!");
        Assert.isTrue(WAIT_ORDER.getCode().equals(order.getOrderStatus()),"订单状态有误,请核实!");
        // 更改订单状态, 绑定对应的 （藏品/盲盒）
        order.setOrderStatus(OVER_ORDER.getCode());
        order.updateD(order.getUserId());
        updateById(order);*/
        // 1. 修改 当前订单状态  // 绑定到 买方
        notifyPaySuccess(outHost);

        // 2. 寄售人已经解除绑定关系了 -- 无需判定

        // 3. 修改当前寄售信息，等待后台审核  打款
        ICntConsignmentService consignmentService = cntConsignmentServiceObjectFactory.getObject();
        Order order = getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost));
        CntConsignment cntConsignment = consignmentService.getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getOrderId, order.getId()));
        Assert.isTrue(Objects.nonNull(cntConsignment), "找不到对应寄售交易信息,请核实!");
        cntConsignment.updateD(cntConsignment.getSendUserId());
        cntConsignment.setConsignmentStatus(OVER_CONSIGN.getCode());
        cntConsignment.setFormInfo("买方已经支付!");
        cntConsignment.setToPay(WAIT_TO_PAY.getCode());
        consignmentService.updateById(cntConsignment);

    }


}
