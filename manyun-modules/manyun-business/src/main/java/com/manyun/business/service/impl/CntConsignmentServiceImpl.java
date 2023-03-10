package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.*;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.ConsignmentOrderSellForm;
import com.manyun.business.domain.form.ConsignmentSellForm;
import com.manyun.business.domain.form.UserConsignmentForm;
import com.manyun.business.domain.query.ConsignmentOrderQuery;
import com.manyun.business.domain.query.ConsignmentQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.CntConsignmentMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.RemoteSmsService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.ShandePayEnum;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.CONSIGNMENT_DE_TIME;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.AliPayEnum.CONSIGNMENT_ALI_PAY;
import static com.manyun.common.core.enums.AssertConsignmentStatus.OK_PUSH_CONSIGNMENT;
import static com.manyun.common.core.enums.ConsignmentStatus.*;
import static com.manyun.common.core.enums.ConsignmentToPayStatus.OK_TO_PAY;
import static com.manyun.common.core.enums.ConsignmentToPayStatus.WAIT_TO_PAY;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;
import static com.manyun.common.core.enums.WxPayEnum.CONSIGNMENT_WECHAT_PAY;

/**
 * <p>
 * ?????????????????? ???????????????
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
@Service
@Slf4j
public class CntConsignmentServiceImpl extends ServiceImpl<CntConsignmentMapper, CntConsignment> implements ICntConsignmentService {


    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private IBoxService boxService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RootPay rootPay;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private RemoteSmsService remoteSmsService;

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private IMediaService mediaService;


    /**
     * ????????????????????????
     * @param userConsignmentForm
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consignmentAssets(UserConsignmentForm userConsignmentForm, String userId) {
        //?????? ?????????????????????  ?????? | ??????
        checkAll(userId,userConsignmentForm);
        //?????????????????? ?????? ?????? ????????????,?????????????????? ??????
        aspect(userConsignmentForm,userId);

    }

    /**
     * ???????????????????????????????????????
     * @param consignmentQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentCollectionListVo> pageConsignmentList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());

        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = getCntConsignmentLambdaQueryWrapper(consignmentQuery,BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE);
        List<CntConsignment> cntConsignments = list(lambdaQueryWrapper);
        // ??????????????????
        return TableDataInfoUtil.pageTableDataInfo(encapsulationConsignmentListVo(cntConsignments),cntConsignments);
    }

    /**
     * ???????????????????????????  ?????? ????????? ????????????
     * @param consignmentQuery
     * @param isType
     * @return
     */
    private LambdaQueryWrapper<CntConsignment> getCntConsignmentLambdaQueryWrapper(ConsignmentQuery consignmentQuery,Integer isType) {
        log.info(JSON.toJSONString(consignmentQuery));
        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = Wrappers.<CntConsignment>lambdaQuery();
        // ??????????????????
        lambdaQueryWrapper.eq(CntConsignment::getIsType,isType);
        lambdaQueryWrapper.ne(CntConsignment::getConsignmentStatus, OVER_CONSIGN.getCode());
        // ??????????????????
        lambdaQueryWrapper.eq(StrUtil.isNotBlank(consignmentQuery.getCateId()), CntConsignment::getCateId, consignmentQuery.getCateId());
        lambdaQueryWrapper.like(StrUtil.isNotBlank(consignmentQuery.getCommName()), CntConsignment::getBuiName, consignmentQuery.getCommName());
        // ?????????????????? ???????????? ????????????
//        if (Integer.valueOf(1).equals(consignmentQuery.getIsNew()))
//            lambdaQueryWrapper.orderByDesc(CntConsignment::getUpdatedTime);

        /*lambdaQueryWrapper.orderBy(true, Integer.valueOf(1).equals(consignmentQuery.getPriceOrder()),CntConsignment::getConsignmentPrice)
                .orderBy(true, Integer.valueOf(1).equals(consignmentQuery.getTimeOrder()), CntConsignment::getCreatedTime);*/

        if (!Integer.valueOf(-1).equals(consignmentQuery.getPriceOrder())){
            if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(0).equals(consignmentQuery.getPriceOrder()))
                lambdaQueryWrapper.orderByDesc(CntConsignment::getConsignmentPrice);
            if (Objects.nonNull(consignmentQuery.getPriceOrder()) && Integer.valueOf(1).equals(consignmentQuery.getPriceOrder()))
                lambdaQueryWrapper.orderByAsc(CntConsignment::getConsignmentPrice);
            if (Objects.isNull(consignmentQuery.getPriceOrder()))
                lambdaQueryWrapper.orderByAsc(CntConsignment::getConsignmentPrice);
        }


        if (!Integer.valueOf(-1).equals(consignmentQuery.getTimeOrder())){
            if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(0).equals(consignmentQuery.getTimeOrder()))
                lambdaQueryWrapper.orderByDesc(CntConsignment::getCreatedTime);
            if (Objects.nonNull(consignmentQuery.getTimeOrder()) && Integer.valueOf(1).equals(consignmentQuery.getTimeOrder()))
                lambdaQueryWrapper.orderByAsc(CntConsignment::getCreatedTime);
        }

        return lambdaQueryWrapper;
    }

    /**
     * ???????????????????????????????????????
     * @param consignmentQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentBoxListVo> pageConsignmentBoxList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());
        LambdaQueryWrapper<CntConsignment> lambdaQueryWrapper = getCntConsignmentLambdaQueryWrapper(consignmentQuery,BusinessConstants.ModelTypeConstant.BOX_TAYPE);
        List<CntConsignment> cntConsignments = list(lambdaQueryWrapper);
        // ????????????
        return TableDataInfoUtil.pageTableDataInfo(encapsulationBoxListVo(cntConsignments),cntConsignments);
    }

    /**
     * ??????????????????????????????
     * @param userId
     * @param consignmentOrderQuery
     * @return
     */
    @Override
    public TableDataInfo<ConsignmentOrderVo> consignmentPageOrder(String userId, ConsignmentOrderQuery consignmentOrderQuery) {
        PageHelper.startPage(consignmentOrderQuery.getPageNum(),consignmentOrderQuery.getPageSize());
        LambdaQueryWrapper<CntConsignment> queryWrapper = Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getSendUserId, userId).eq(Objects.nonNull(consignmentOrderQuery.getConsignmentStatus()), CntConsignment::getConsignmentStatus, consignmentOrderQuery.getConsignmentStatus()).orderByDesc(CntConsignment::getCreatedTime);
        List<CntConsignment> cntConsignments = list(queryWrapper);
        return TableDataInfoUtil.pageTableDataInfo(cntConsignments.parallelStream().map(this::initConsignmentOrderVo).collect(Collectors.toList()), cntConsignments);
    }

    /**
     *
     * @param payUserId  ?????????????????????
     * @param consignmentSellForm  ??????????????????
     *
     * ???????????????????????? ????????????!!!
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock("businessConsignment")
    public PayVo businessConsignment(String payUserId, ConsignmentSellForm consignmentSellForm) {
        CntConsignment consignment = getById(consignmentSellForm.getBuiId());
        // ?????? ?????????????????????????????????
        checkBusinessConsignment(payUserId,consignment);

        // ??????????????????????????????
        String hostOut = orderService.createConsignmentOrder(OrderCreateDto.builder()
                .orderAmount(consignment.getConsignmentPrice())
                .buiId(consignment.getRealBuiId())
                .payType(consignmentSellForm.getPayType())
                .goodsType(consignment.getIsType())
                .collectionName(consignment.getBuiName())
                .goodsNum(Integer.valueOf(1))
                .userId(payUserId).build(),(idStr)-> consignment.setOrderId(idStr));


        /**
         * ????????????  ??????????????????
         * ???????????????????????? ???????????????????????????
         **/
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(consignmentSellForm.getPayType())
                        .realPayMoney(consignment.getConsignmentPrice())
                        .outHost(hostOut)
                        .goodsName(consignment.getBuiName())
                        .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                        .shandePayEnum(ShandePayEnum.CONSIGNMENT_SHANDE_PAY)
                        .userId(payUserId).build());
        Order order = orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, hostOut));
        // ?????????????????? ??????????????? ????????????????????????????????????
        order.setMoneyBln(order.getMoneyBln().add(payVo.getMoneyBln()));
        orderService.updateById(order);
        // ??????????????????
        consignment.updateD(payUserId);
        consignment.setPayUserId(payUserId);
        consignment.setFormInfo("??????????????????,??????????????????.");
        consignment.setConsignmentStatus(LOCK_CONSIGN.getCode());
        updateById(consignment);

        // ???????????? ???????????????,?????? ????????????????????????????????????
        if ( StrUtil.isBlank(payVo.getBody())){
            // ??????????????????
            orderService.notifyPayConsignmentSuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(order.getGoodsType())){
                // ??????
                Box box = boxService.getById(order.getBuiId());
                title = StrUtil.format("????????? {} ??????!", box.getBoxTitle());
                form = StrUtil.format("????????????{};????????? {} ??????!",order.getOrderAmount(), box.getBoxTitle());

            }
            if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(order.getGoodsType())){
                //??????
                CntCollection cntCollection = collectionService.getById(order.getBuiId());
                title = StrUtil.format("???????????????????????? {} ??????!", cntCollection.getCollectionName());
                form = StrUtil.format("????????????{};???????????????????????? {} ??????!",order.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(order.getUserId()).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;
    }

    /**
     * ????????????????????????  ???????????????,??????????????????
     * @param cntConsignments
     */
    @Override
    public void reLoadConsignments(List<CntConsignment> cntConsignments) {
        if (cntConsignments.isEmpty())return;
        String format = StrUtil.format("???????????????");
        for (CntConsignment cntConsignment : cntConsignments) {
            cntConsignment.setConsignmentStatus(PUSH_CONSIGN.getCode());
            cntConsignment.setFormInfo(format);
            cntConsignment.updateD(cntConsignment.getPayUserId());

        }
           updateBatchById(cntConsignments);
    }

    /**
     * ??????????????????????????????
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock("cancelSchedulingConsignment")
    public void  cancelSchedulingConsignment() {
        // ???????????????
        Integer time = systemService.getVal(CONSIGNMENT_DE_TIME, Integer.class);
        List<CntConsignment> cntConsignments = list(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getConsignmentStatus,PUSH_CONSIGN.getCode()).lt(CntConsignment::getCreatedTime, LocalDateTime.now().minusMinutes(time)));
        // ?????????????????????????????????????????????
        for (CntConsignment cntConsignment : cntConsignments) {
            try {
                cancelConsignment(cntConsignment);
                // ??????????????????.
                CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
                remoteSmsService.sendCommPhone(Builder.<SmsCommDto>of(SmsCommDto::new).with(SmsCommDto::setTemplateCode, BusinessConstants.SmsTemplateNumber.ASSERT_BACK).with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().put("buiName",cntConsignment.getBuiName() ).build()).with(SmsCommDto::setPhoneNumber,cntUserDto.getPhone()).build());
            }catch (Exception e){
                log.error("??????????????????????????????????????????:{},????????????:{}",e.getMessage(),cntConsignment.getId());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelConsignmentById(String id,String sendUserId) {
        CntConsignment consignment = getOne(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getId, id).eq(CntConsignment::getSendUserId, sendUserId).eq(CntConsignment::getConsignmentStatus, PUSH_CONSIGN.getCode()));
        // ????????????????????????
        Assert.isTrue(Objects.nonNull(consignment),"????????????????????????,???????????????????????????!");
        cancelConsignment(consignment);
    }

    @Override
    public List<KeywordVo> queryDict(String keyword) {
        List<CntConsignment> cntConsignments = list(Wrappers.<CntConsignment>lambdaQuery().select(CntConsignment::getBuiName,CntConsignment::getIsType).like(CntConsignment::getBuiName, keyword).eq(CntConsignment::getConsignmentStatus, PUSH_CONSIGN.getCode()).orderByDesc(CntConsignment::getCreatedTime).last(" limit 10"));
       return cntConsignments.parallelStream().map(item ->{
            KeywordVo keywordVo = Builder.of(KeywordVo::new).build();
            keywordVo.setType(item.getIsType());
            keywordVo.setCommTitle(item.getBuiName());
            return keywordVo;
        }).collect(Collectors.toList());
    }

    /**
     * ?????????????????? ?????????????????????
     * @param payUserId
     * @param consignmentOrderSellForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String consignmentCreateOrder(String payUserId, ConsignmentOrderSellForm consignmentOrderSellForm) {
        CntConsignment consignment = getById(consignmentOrderSellForm.getBuiId());
        // ?????? ?????????????????????????????????
        checkBusinessConsignment(payUserId,consignment);

        // ??????????????????????????????
        String hostOut = orderService.createConsignmentOrder(OrderCreateDto.builder()
                .orderAmount(consignment.getConsignmentPrice())
                .buiId(consignment.getRealBuiId())
               // .payType(consignmentOrderSellForm.getPayType())
                .goodsType(consignment.getIsType())
                .collectionName(consignment.getBuiName())
                .goodsNum(Integer.valueOf(1))
                .userId(payUserId).build(),(idStr)-> consignment.setOrderId(idStr));
        // ??????????????????
        consignment.updateD(payUserId);
        consignment.setPayUserId(payUserId);
        consignment.setFormInfo("??????????????????,??????????????????.");
        consignment.setConsignmentStatus(LOCK_CONSIGN.getCode());
        updateById(consignment);

        return orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, hostOut)).getId();
    }

    /**
     * ????????????????????????????????????
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consignmentSuccess(String id, boolean canTrade) {
        // 1. ????????????????????????
        CntConsignment cntConsignment = getById(id);
        Assert.isTrue(WAIT_TO_PAY.getCode().equals(cntConsignment.getToPay()),"????????????,?????????!");
        // ??????????????????
        cntConsignment.setToPay(OK_TO_PAY.getCode());
        cntConsignment.updateD(cntConsignment.getSendUserId());
        updateById(cntConsignment);
        // ????????????????????????
        BigDecimal realMoney = cntConsignment.getConsignmentPrice().subtract(cntConsignment.getServerCharge());
        if (!canTrade) {
            moneyService.addMoney(cntConsignment.getSendUserId(),realMoney,StrUtil.format("????????????????????????!"));
        }
        // ????????????
        CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
        remoteSmsService.sendCommPhone(Builder.<SmsCommDto>of(SmsCommDto::new).with(SmsCommDto::setTemplateCode, BusinessConstants.SmsTemplateNumber.ASSERT_SUCCESS).with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().put("money", realMoney.toString()).put("buiName",cntConsignment.getBuiName() ).build()).with(SmsCommDto::setPhoneNumber,cntUserDto.getPhone()).build());
    }

    @Override
    public List<ConsignmentOpenListVo> openConsignmentList() {
        List<ConsignmentOpenListVo> listVoList = Lists.newArrayList();
       List<ConsignmentOpenDto> consignmentOpenDtos =  baseMapper.openConsignmentList();
        for (ConsignmentOpenDto consignmentOpenDto : consignmentOpenDtos) {
            ConsignmentOpenListVo consignmentOpenListVo = new ConsignmentOpenListVo();
            consignmentOpenListVo.setName(consignmentOpenDto.getName());
            List<MediaVo> mediaVos = mediaService.initMediaVos(consignmentOpenDto.getBuiId(), COLLECTION_MODEL_TYPE);
            if (!mediaVos.isEmpty()) {
                consignmentOpenListVo.setImage(mediaVos.get(0).getMediaUrl());
            }
            consignmentOpenListVo.setPrice(consignmentOpenDto.getPrice());

            listVoList.add(consignmentOpenListVo);
        }
        return listVoList;
    }

    private void cancelConsignment(CntConsignment cntConsignment) {
       // 1. ????????? ?????????????????????
        Integer isType = cntConsignment.getIsType();
        //????????? ???????????? ?????? ????????????
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(isType))
            //??????
            userCollectionService.showUserCollection(cntConsignment.getSendUserId(),cntConsignment.getBuiId(),StrUtil.format("???????????????,????????????!"));
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(isType))
            // ??????
            userBoxService.showUserBox(cntConsignment.getBuiId(),cntConsignment.getSendUserId(),StrUtil.format("???????????????,????????????!"));

        // 2. ????????????????????????
        removeById(cntConsignment.getId());
       // 3. ????????????

       // 4. ??????
    }

    /**
     * ???????????? ??????????????????
     * @param payUserId
     * @param consignment
     */
    private void checkBusinessConsignment(String payUserId, CntConsignment consignment) {
        // 1.?????????????????????????????????????????????
        //????????????????????????
        List<Order> orders = orderService.checkUnpaidOrder(payUserId);
        Assert.isFalse(orders.size() > 0 ,"???????????????????????????????????????");
        // ??????????????? ???????????????????????????
        Assert.isTrue(PUSH_CONSIGN.getCode().equals(consignment.getConsignmentStatus()),"??????????????????????????????,???????????????!");
        Assert.isFalse(payUserId.equals(consignment.getSendUserId()),"???????????????????????????????????????!");
        // ??????????????????
        long count = orderService.count(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, payUserId).eq(Order::getInnerNumber, 1).last(" AND DATE_FORMAT( created_time, '%Y%m%d' ) = DATE_FORMAT( CURDATE( ) , '%Y%m%d' ) "));
        Assert.isTrue(count < 3,"???????????????????????????????????????");
        //long count = count(Wrappers.<CntConsignment>lambdaQuery().eq(CntConsignment::getPayUserId, payUserId).last(" AND DATE_FORMAT( created_time, '%Y%m%d' ) = DATE_FORMAT( CURDATE( ) , '%Y%m%d' ) "));
       // Assert.isTrue(count < 3,"???????????????????????????");

    }

    /**
     * ?????? ????????????????????????
     * @param cntConsignment
     * @return
     */
    private ConsignmentOrderVo initConsignmentOrderVo(CntConsignment cntConsignment) {
        ConsignmentOrderVo consignmentOrderVo = Builder.of(ConsignmentOrderVo::new).build();
        consignmentOrderVo.setCntUserDto(remoteBuiUserService.commUni(cntConsignment.getSendUserId(),  SecurityConstants.INNER).getData());
        consignmentOrderVo.setId(cntConsignment.getId());
        consignmentOrderVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        consignmentOrderVo.setCreatedTime(cntConsignment.getCreatedTime());
        consignmentOrderVo.setType(cntConsignment.getIsType());
        consignmentOrderVo.setServerCharge(cntConsignment.getServerCharge());
        consignmentOrderVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
        consignmentOrderVo.setBuiId(cntConsignment.getBuiId());
        // ?????????????????? ?????????????????????
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // ???????????? ?????????????????????????????????
            Order order = orderService.getById(cntConsignment.getOrderId());
            consignmentOrderVo.setEndPayTime(order.getEndTime());
        }
        //????????? ???????????? ?????? ????????????
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(cntConsignment.getIsType()))
            //??????
            consignmentOrderVo.setCollectionVo(collectionService.getBaseCollectionVo(cntConsignment.getRealBuiId()));

        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(cntConsignment.getIsType()))
            // ??????
            consignmentOrderVo.setBoxListVo(boxService.getBaseBoxListVo(cntConsignment.getRealBuiId()));

        return consignmentOrderVo;
    }

    private List<ConsignmentBoxListVo> encapsulationBoxListVo(List<CntConsignment> cntConsignments) {
        return cntConsignments.parallelStream().map(this::initBoxListVo).collect(Collectors.toList());
    }

    private ConsignmentBoxListVo initBoxListVo(CntConsignment cntConsignment) {
        ConsignmentBoxListVo consignmentBoxListVo = Builder.of(ConsignmentBoxListVo::new).build();
        consignmentBoxListVo.setBoxListVo(boxService.getBaseBoxListVo(cntConsignment.getRealBuiId()));
        consignmentBoxListVo.setCntUserDto(remoteBuiUserService.commUni(cntConsignment.getSendUserId(),  SecurityConstants.INNER).getData());
        consignmentBoxListVo.setId(cntConsignment.getId());
        consignmentBoxListVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
        consignmentBoxListVo.setCreatedTime(cntConsignment.getCreatedTime());
        consignmentBoxListVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
        consignmentBoxListVo.setServerCharge(cntConsignment.getServerCharge());
        // ?????????????????? ?????????????????????
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // ???????????? ?????????????????????????????????
            Order order = orderService.getById(cntConsignment.getOrderId());
            consignmentBoxListVo.setEndPayTime(order.getEndTime());
        }
        return consignmentBoxListVo;
    }

    private List<ConsignmentCollectionListVo> encapsulationConsignmentListVo(List<CntConsignment> cntConsignments) {
        if (cntConsignments.isEmpty()) return Collections.emptyList();
        return cntConsignments.parallelStream().map(this::initCollectionListVo).collect(Collectors.toList());
    }

    /**
     * ????????????????????????
     * @param cntConsignment
     * @return
     */
    private ConsignmentCollectionListVo initCollectionListVo(CntConsignment cntConsignment) {
        ConsignmentCollectionListVo collectionListVo = Builder.of(ConsignmentCollectionListVo::new).build();
        collectionListVo.setCollectionVo(collectionService.getSoleBaseCollectionVo(cntConsignment.getRealBuiId()));
        // ??????
//        CntUserDto cntUserDto = remoteBuiUserService.commUni(cntConsignment.getSendUserId(), SecurityConstants.INNER).getData();
//        CntUserDto newCntUserDto = new CntUserDto();
//        newCntUserDto.setId(cntUserDto.getId());
//        newCntUserDto.setHeadImage(cntUserDto.getHeadImage());
//        newCntUserDto.setNickName(cntUserDto.getNickName());
//        newCntUserDto.setPhone(cntUserDto.getPhone());
//        newCntUserDto.setLinkAddr(cntUserDto.getLinkAddr());
//
//        collectionListVo.setCntUserDto(newCntUserDto);
        collectionListVo.setId(cntConsignment.getId());
        collectionListVo.setConsignmentStatus(cntConsignment.getConsignmentStatus());
       // collectionListVo.setCreatedTime(cntConsignment.getCreatedTime());
        collectionListVo.setConsignmentPrice(cntConsignment.getConsignmentPrice());
       // collectionListVo.setServerCharge(cntConsignment.getServerCharge());

        UserCollection userCollection = userCollectionService.getById(cntConsignment.getBuiId());
        collectionListVo.setCollectionNumber(userCollection.getCollectionNumber());
        collectionListVo.setBuiId(userCollection.getId());
        // ?????????????????? ?????????????????????
        if (LOCK_CONSIGN.getCode().equals(cntConsignment.getConsignmentStatus()) && StrUtil.isNotBlank(cntConsignment.getOrderId())){
            // ???????????? ?????????????????????????????????
            Order order = orderService.getById(cntConsignment.getOrderId());
            collectionListVo.setEndPayTime(order.getEndTime());
        }

        return collectionListVo;
    }

    /**
     * ???????????? ?????? | ???????????? ??????
     * @param userConsignmentForm
     * @param userId
     */
    private void aspect(@NotNull UserConsignmentForm userConsignmentForm,@NotNull String userId) {
        // ??????????????????,??????????????????,?????? ???????????????????????????????????????
        Integer type = userConsignmentForm.getType();
        String buiId = userConsignmentForm.getBuiId();
        String realBuiId = null;
        String info = null;
        String cateId = null;
        String buiName= null;
        Integer pushConsignment = 0;
        // ??????
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(type)){
            // ????????????????????????,????????????????????????
             info = StrUtil.format("?????????????????????,?????????????????????????????????!");
            realBuiId =  userCollectionService.hideUserCollection(buiId,userId,info);
            CntCollection cntCollection = collectionService.getById(realBuiId);
            cateId = cntCollection.getCateId();
            buiName = cntCollection.getCollectionName();
             pushConsignment = cntCollection.getPushConsignment();
            // return;
        }
       // ??????
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(type)){
            // ?????????????????? ??????,?????????????????????
             info = StrUtil.format("?????????????????????,?????????????????????????????????!");
            realBuiId = userBoxService.hideUserBox(buiId,userId,info);
            Box box = boxService.getById(realBuiId);
            cateId = box.getCateId();
            buiName= box.getBoxTitle();
            pushConsignment = box.getPushConsignment();

            // return;
        }
        if (StrUtil.isBlank(info) && StrUtil.isBlank(realBuiId))
           throw new ServiceException("not fount type [0-1] now type is "+type+"");
        Assert.isTrue(OK_PUSH_CONSIGNMENT.getCode().equals(pushConsignment),"?????????????????????!");
        pushConsignment(userId,userConsignmentForm.getConsignmentMoney(), type, buiId, realBuiId, info,cateId,buiName);

    }

    private void checkAll(String userId, UserConsignmentForm userConsignmentForm) {
        // ????????????????????????
        Money serviceOne = moneyService.getOne(Wrappers.<Money>lambdaQuery().select(Money::getLlAccountStatus).eq(Money::getUserId, userId));
        Assert.isTrue("1".equals(serviceOne.getLlAccountStatus()) || Integer.valueOf(1).equals(serviceOne.getSandAccountStatus()),"???????????????????????????????????????????????????");
        Integer type = userConsignmentForm.getType();
        // ??????
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(type))
            Assert.isTrue(userCollectionService.existUserCollection(userId,userConsignmentForm.getBuiId()),"?????????????????????,???????????????????????????!");

        // ??????
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(type))
            Assert.isTrue(userBoxService.existUserBox(userId,userConsignmentForm.getBuiId()),"?????????????????????,???????????????????????????!");


    }


    /**
     * ?????????????????????
     */
    private void pushConsignment(String userId, BigDecimal consignmentPrice , Integer type, String buiId, String realBuiId,String info,String cateId,String buiName){
        CntConsignment cntConsignment = Builder.of(CntConsignment::new).build();
        cntConsignment.setId(IdUtil.getSnowflakeNextIdStr());
        cntConsignment.setConsignmentPrice(consignmentPrice);
        // ???????????????
        BigDecimal systemServiceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.CONSIGNMENT_SERVER_CHARGE, BigDecimal.class);
        // ?????????????????????
/*        cntConsignment.setServerCharge(NumberUtil.add(0D));
        if (systemServiceVal.compareTo(NumberUtil.add(0D)) >=1)*/
        cntConsignment.setServerCharge(consignmentPrice.multiply(systemServiceVal).divide(NumberUtil.add(100D),2,BigDecimal.ROUND_DOWN));
        cntConsignment.setConsignmentStatus(PUSH_CONSIGN.getCode());
        cntConsignment.setBuiId(buiId);
        cntConsignment.setSendUserId(userId);
        cntConsignment.setRealBuiId(realBuiId);
        cntConsignment.setIsType(type);
        cntConsignment.createD(userId);
        cntConsignment.setCateId(cateId);
        cntConsignment.setFormInfo(info);
        cntConsignment.setBuiName(buiName);
        save(cntConsignment);
        // ??????????????????.
        //CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        //remoteSmsService.sendCommPhone(Builder.<SmsCommDto>of(SmsCommDto::new).with(SmsCommDto::setTemplateCode, BusinessConstants.SmsTemplateNumber.ASSERT_UP).with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().put("buiName",cntConsignment.getBuiName() ).build()).with(SmsCommDto::setPhoneNumber,cntUserDto.getPhone()).build());
    }


}
