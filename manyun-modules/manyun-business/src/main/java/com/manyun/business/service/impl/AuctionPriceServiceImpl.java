package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.design.delay.DelayAbsAspect;
import com.manyun.business.design.delay.DelayQueue;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.*;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.AuctionPayFixedForm;
import com.manyun.business.domain.form.AuctionPayForm;
import com.manyun.business.domain.form.AuctionPayMarginForm;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.query.MyAuctionPriceQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.AuctionPriceMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.*;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.mq.producers.deliver.DeliverProducer;
import com.manyun.common.mq.producers.msg.DeliverMsg;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.System;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;

import static com.manyun.common.core.enums.AliPayEnum.*;
import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;
import static com.manyun.common.core.enums.WxPayEnum.*;


/**
 * <p>
 * ????????? ???????????????
 * </p>
 *
 * @author
 * @since 2022-06-30
 */
@Service
public class AuctionPriceServiceImpl extends ServiceImpl<AuctionPriceMapper, AuctionPrice> implements IAuctionPriceService {

    @Autowired
    private IAuctionOrderService auctionOrderService;

    @Autowired
    private RootPay rootPay;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private IAuctionSendService auctionSendService;

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private DelayQueue delayQueue;

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IBoxService boxService;

    @Autowired
    private IBoxCollectionService boxCollectionService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private IAuctionMarginService auctionMarginService;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private ILogsService logsService;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private DeliverProducer deliverProducer;

    @Autowired
    private RemoteBuiUserService userService;


    //?????????????????????

    @Override
    public R checkPayMargin(AuctionPriceForm auctionPriceForm, String userId) {
        List<AuctionMargin> list = auctionMarginService.list(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getAuctionSendId, auctionPriceForm.getAuctionSendId())
                .eq(AuctionMargin::getUserId, userId).eq(AuctionMargin::getPayMarginStatus, Integer.valueOf(1)));
        //?????????????????????
        if (list.size() > 0) {
            //???????????????
            return R.ok("?????????????????????????????????");
        } else {
            return R.ok(CodeStatus.NO_PAY_MARGIN.getCode(),"????????????????????????????????????");
        }
    }


    private void realCheck(String userId) {
        R<CntUserDto> cntUserDtoR = userService.commUni(userId, SecurityConstants.INNER);
        CntUserDto data = cntUserDtoR.getData();
        cn.hutool.core.lang.Assert.isTrue(OK_REAL.getCode().equals(data.getIsReal()),"??????????????????,???????????????!");
    }


    @Override
    public R<BigDecimal> myAuctionPrice(AuctionPriceForm auctionPriceForm, String userId) {
        realCheck(userId);
        LoginBusinessUser businessUser = SecurityUtils.getNotNullLoginBusinessUser();
        AuctionSend auctionSend = auctionSendService.getById(auctionPriceForm.getAuctionSendId());
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        if (LocalDateTime.now().isBefore(auctionSend.getStartTime())) {
            return R.fail("??????????????????????????????????????????");
        }
        if (LocalDateTime.now().isAfter(auctionSend.getEndTime().plusMinutes(delayTime))) {
            return R.fail("?????????????????????");
        }
        if (auctionSend.getUserId().equals(userId)) {
            return R.fail("???????????????????????????????????????");
        }

        AuctionPrice auctionPrice = new AuctionPrice();
        auctionPrice.setId(IdUtil.getSnowflakeNextIdStr());
        auctionPrice.setAuctionStatus(AuctionStatus.BID_BIDING.getCode());

        //????????????????????????????????????
        if (LocalDateTime.now().isAfter(auctionSend.getEndTime().minusMinutes(delayTime)) && LocalDateTime.now().isBefore(auctionSend.getEndTime())) {
            //??????????????????
            auctionSend.setIsDelay(2);
            //auctionSend.setEndTime(auctionSend.getEndTime().plusMinutes(delayTime));
            auctionSend.setEndTime(LocalDateTime.now().plusMinutes(delayTime));

            // ???????????????
            /*long l = TimeUnit.MINUTES.toSeconds(delayTime);
            delayQueue.put(auctionSend.getId(),l, new DelayAbsAspect<String>() {
                @Override
                public void invocationSuccess(String s) {

                    System.out.println("-----------------------????????????");
                    winnerOperation(auctionSend);
                }

                @Override
                public void invocationFail(String s) {

                }
            });*/


            auctionSendService.updateById(auctionSend);
        }
        List<AuctionOrder> auctionOrders = auctionOrderService.checkUnpaidOrder(businessUser.getUserId());
        if (auctionOrders.size() > 0 ) {
            return R.fail("???????????????????????????????????????");
        }
        //?????????????????????????????????????????????
        if (!auctionSend.getAuctionSendStatus().equals(AuctionSendStatus.BID_BIDING.getCode())) {
            return R.fail("????????????????????????");
        }
        List<AuctionPrice> list = this.list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(AuctionPrice::getAuctionSendId, auctionPriceForm.getAuctionSendId()).orderByDesc(AuctionPrice::getBidPrice));

        BigDecimal range = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRICE_RANGE, BigDecimal.class);


        if (list.isEmpty()) {
            auctionPrice.setBidPrice(auctionSend.getStartPrice().add(range));
        }
        if (list.size() > 0) {
            auctionPrice.setBidPrice(list.get(0).getBidPrice().add(range));
        }
        auctionPrice.setUserId(businessUser.getUserId());
        auctionPrice.setAuctionSendId(auctionPriceForm.getAuctionSendId());
        auctionPrice.setUserName(remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData().getNickName());
        auctionPrice.setCreatedTime(LocalDateTime.now());
        //??????????????????
        List<AuctionPrice> oldPriceList = list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(AuctionPrice::getAuctionSendId, auctionPriceForm.getAuctionSendId()).eq(AuctionPrice::getUserId, userId));
        if (oldPriceList.size() > 0) {
            for (AuctionPrice oldPrice : oldPriceList) {
                oldPrice.setIsNew(2);
            }
            updateBatchById(oldPriceList);
        }
        this.save(auctionPrice);

        auctionSend.setNowPrice(auctionPrice.getBidPrice());

        auctionSendService.updateById(auctionSend);

        return R.ok(auctionPrice.getBidPrice());
    }

    /**
     * ??????????????????
     */
    private void winnerOperation(AuctionSend auctionSend) {
        List<AuctionPrice> list = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId()).orderByDesc(AuctionPrice::getBidPrice));
        //?????????
        if (list.isEmpty()) {
            return;
        }
        AuctionPrice winAuctionPrice = list.get(0);
        //????????????????????????????????????
        list.parallelStream().map(item -> {
            item.setAuctionStatus(AuctionStatus.BID_MISSED.getCode());
            return item;
        }).collect(Collectors.toList());
        //?????????????????????????????????
        winAuctionPrice.setAuctionStatus(AuctionStatus.WAIT_PAY.getCode());
        LocalDateTime endPayTime = LocalDateTime.now().plusMinutes(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_ORDER_TIME, Integer.class));

        winAuctionPrice.setEndPayTime(endPayTime);
        //????????????,????????????
        String auctionOrderNo = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                .goodsId(auctionSend.getGoodsId())
                .goodsName(auctionSend.getGoodsName())
                .goodsType(auctionSend.getGoodsType())
                .nowPrice(auctionSend.getNowPrice())
                .startPrice(auctionSend.getStartPrice())
                .sendAuctionId(auctionSend.getId())
                .orderAmount(winAuctionPrice.getBidPrice())
                .auctionPriceId(winAuctionPrice.getId())
                .fromUserId(auctionSend.getUserId())
                .toUserId(winAuctionPrice.getUserId()).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));
        auctionSend.setAuctionSendStatus(AuctionSendStatus.WAIT_PAY.getCode());
        auctionSend.setEndPayTime(endPayTime);

        auctionSendService.updateById(auctionSend);

        //????????????????????????????????????
        //??????????????????????????????????????????
        Set<String> collect = list.parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet());
        //???????????????
        collect.remove(winAuctionPrice.getUserId());
        //????????????
        for (String userId : collect) {
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
            money.setMoneyBalance(money.getMoneyBalance().add(auctionSend.getMargin()));
            moneyService.updateById(money);
            logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt("???????????????").formInfo(auctionSend.getMargin().toString()).isType(PULL_SOURCE).modelType(MONEY_TYPE).build());
        }
        updateBatchById(list);
    }

    /**
     * ????????????
     */
    @Override
    public TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery priceQuery) {
        List<AuctionPrice> priceList = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, priceQuery.getAuctionSendId())
                .orderByDesc(AuctionPrice::getBidPrice));
        return TableDataInfoUtil.pageTableDataInfo(priceList.parallelStream().map(this::providerAuctionPriceVo).collect(Collectors.toList()), priceList);
    }


    private AuctionPriceVo providerAuctionPriceVo(AuctionPrice auctionPrice) {
        R<CntUserDto> cntUserDtoR = remoteBuiUserService.commUni(auctionPrice.getUserId(), SecurityConstants.INNER);
        AuctionPriceVo auctionPriceVo = Builder.of(AuctionPriceVo::new).build();
        BeanUtil.copyProperties(auctionPrice, auctionPriceVo);
        auctionPriceVo.setHeadImage(cntUserDtoR.getData().getHeadImage());
        return auctionPriceVo;
    }

    //?????????????????????????????????
    @Override
    public TableDataInfo<MyAuctionPriceVo> myAuctionPriceList(MyAuctionPriceQuery myAuctionPriceQuery, String userId) {
        //???????????????????????????????????????
        List<AuctionPrice> auctionPriceList = list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(myAuctionPriceQuery.getAuctionPriceStatus() != null && myAuctionPriceQuery.getAuctionPriceStatus() != 0, AuctionPrice::getAuctionStatus, myAuctionPriceQuery.getAuctionPriceStatus())
                .eq(AuctionPrice::getUserId, userId)
                .eq(AuctionPrice::getIsNew, 1).orderByDesc(AuctionPrice::getCreatedTime));

        return TableDataInfoUtil.pageTableDataInfo(auctionPriceList.parallelStream().map(this::providerMyAuctionPriceVo).collect(Collectors.toList()), auctionPriceList);


        /*Set<String> auctionSendIds = list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(myAuctionPriceQuery.getAuctionPriceStatus() != null && myAuctionPriceQuery.getAuctionPriceStatus() != 0,AuctionPrice::getAuctionStatus, myAuctionPriceQuery.getAuctionPriceStatus())
                .eq(AuctionPrice::getUserId, userId))
                .parallelStream().map(item -> item.getAuctionSendId()).collect(Collectors.toSet());
        List<AuctionSend> sendList = new ArrayList<>();
        if (!auctionSendIds.isEmpty()) {
            sendList = auctionSendService.list(Wrappers.<AuctionSend>lambdaQuery().in(AuctionSend::getId, auctionSendIds));
        }
        return TableDataInfoUtil.pageTableDataInfo(sendList.parallelStream().map(this::providerMyAuctionPriceVo).collect(Collectors.toList()), sendList);*/
    }

    private MyAuctionPriceVo providerMyAuctionPriceVo(AuctionPrice auctionPrice) {
        MyAuctionPriceVo myAuctionPriceVo = Builder.of(MyAuctionPriceVo::new).build();
        AuctionSend auctionSend = auctionSendService.getById(auctionPrice.getAuctionSendId());
        myAuctionPriceVo.setAuctionVo(auctionSendService.getAuctionSendVo(auctionPrice.getAuctionSendId()));
        myAuctionPriceVo.setAuctionSendId(auctionPrice.getAuctionSendId());
        myAuctionPriceVo.setAuctionStatus(auctionPrice.getAuctionStatus());
        myAuctionPriceVo.setGoodsId(auctionSend.getGoodsId());
        myAuctionPriceVo.setGoodsType(auctionSend.getGoodsType());
        if (auctionPrice.getEndPayTime() != null) {
            myAuctionPriceVo.setEndPayTime(auctionPrice.getEndPayTime());
        }
        AuctionOrder auctionOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getSendAuctionId, auctionPrice.getAuctionSendId())
                .eq(AuctionOrder::getToUserId, auctionPrice.getUserId()));
        if (auctionOrder != null) {
            myAuctionPriceVo.setMoneyBln(auctionOrder.getMoneyBln());
        }
        String mediaUrl = "";
        List<MediaVo> thumbnailImgMediaVos = new ArrayList<>();
        List<MediaVo> threeDimensionalMediaVos = new ArrayList<>();
        if (auctionSend.getGoodsType() == 1) {
            mediaUrl = mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE).get(0).getMediaUrl();
            thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
            threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE);
        } else {
            mediaUrl = mediaService.initMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE).get(0).getMediaUrl();
            thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
            threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(auctionSend.getGoodsId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE);
        }
        myAuctionPriceVo.setGoodsName(auctionSend.getGoodsName());
        myAuctionPriceVo.setGoodsImg(mediaUrl);
        myAuctionPriceVo.setThumbnailImg(thumbnailImgMediaVos.size()>0?thumbnailImgMediaVos.get(0).getMediaUrl():"");
        myAuctionPriceVo.setThreeDimensionalImg(threeDimensionalMediaVos.size()>0?threeDimensionalMediaVos.get(0).getMediaUrl():"");
        return myAuctionPriceVo;
    }

    /**
     * ?????????????????????????????????
     * @param collectionId
     * @param auctionSendId
     * @return
     */
    @Override
    public AuctionCollectionAllVo priceCollectionInfo(String collectionId, String auctionSendId) {
        AuctionCollectionAllVo auctionCollectionAllVo = Builder.of(AuctionCollectionAllVo::new)
                .with(AuctionCollectionAllVo::setCollectionVo, collectionService.getBaseCollectionVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionInfoVo,auctionSendService.getBaseCollectionInfoVo(collectionId))
                .with(AuctionCollectionAllVo::setCollectionNumber, userCollectionService.getById(auctionSendService.getById(auctionSendId).getMyGoodsId()).getCollectionNumber())
                .with(AuctionCollectionAllVo::setAuctionVo,auctionSendService.getAuctionSendVo(auctionSendId)).build();

        return auctionCollectionAllVo;
    }

    /**
     * ?????????????????????????????????
     * @param boxId
     * @param auctionSendId
     * @return
     */
    @Override
    public AuctionBoxAllVo priceBoxInfo(String boxId, String auctionSendId) {
        //??????????????????????????????????????????
        AuctionBoxAllVo auctionBoxAllVo = Builder.of(AuctionBoxAllVo::new)
                .with(AuctionBoxAllVo::setBoxListVo,boxService.getBaseBoxListVo(boxId))
                .with(AuctionBoxAllVo::setBoxCollectionJoinVos,boxCollectionService.findJoinCollections(boxId))
                .with(AuctionBoxAllVo::setAuctionVo, auctionSendService.getAuctionSendVo(auctionSendId)).build();

        return auctionBoxAllVo;
    }


    //?????????????????????????????????????????????

    //?????????????????????
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock("payAuction")
    public PayVo payAuction(String payUserId, AuctionPayForm auctionPayForm) {
        CntUserDto cntUserDto = remoteBuiUserService.commUni(payUserId, SecurityConstants.INNER).getData();
        if (Integer.valueOf(0).equals(auctionPayForm.getPayType())) {
            Assert.isTrue(SecurityUtils.matchesPassword(auctionPayForm.getPayPass(),cntUserDto.getPayPass()),"??????????????????,?????????!");
        }
        AuctionSend auctionSend = auctionSendService.getById(auctionPayForm.getAuctionSendId());
        boolean canTrade = moneyService.checkLlpayStatus(payUserId) && moneyService.checkLlpayStatus(auctionSend.getUserId());
        boolean canSandTrade  = moneyService.checkSandAccountStatus(payUserId) && moneyService.checkSandAccountStatus(auctionSend.getUserId());
        BigDecimal commission = auctionSend.getCommission();
        if (Integer.valueOf(5).equals(auctionPayForm.getPayType())) {
            Assert.isTrue(canTrade, "??????????????????????????????????????????????????????");
        }
        if (Integer.valueOf(6).equals(auctionPayForm.getPayType())) {
            Assert.isTrue(canSandTrade, "???????????????????????????????????????????????????????????????");
        }

        Assert.isFalse(auctionSend.getUserId().equals(payUserId), "???????????????????????????????????????");
        AuctionOrder auctionOrder = auctionOrderService.getById(auctionSend.getAuctionOrderId());
        Assert.isFalse(Integer.valueOf(5).equals(auctionPayForm.getPayType()) && auctionOrder.getMoneyBln().compareTo(NumberUtil.add(0D)) >0, "???????????????????????????????????????");
        /*String auctionOrderHost = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                .goodsId(auctionSend.getGoodsId())
                .goodsName(auctionSend.getGoodsName())
                .goodsType(auctionSend.getGoodsType())
                .nowPrice(auctionSend.getNowPrice())
                .payType(auctionPayForm.getPayType())
                .sendAuctionId(auctionPayForm.getAuctionSendId())
                .userId(payUserId).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));*/
        ShandePayEnum shandePayEnum = ShandePayEnum.AUCTION_SHANDE_PAY.setReturnUrl(auctionPayForm.getReturnUrl(), auctionPayForm.getReturnUrl());
        LianLianPayEnum lianLianPayEnum = LianLianPayEnum.AUCTION_SHANDE_PAY.setReturnUrl(auctionPayForm.getReturnUrl(), auctionPayForm.getReturnUrl());
        SandAccountEnum sandAccountEnum = SandAccountEnum.AUCTION_SANDACCOUNT_PAY.setReturnUrl(auctionPayForm.getReturnUrl(),auctionPayForm.getReturnUrl());

        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(auctionPayForm.getPayType())
                        .realPayMoney(auctionOrder.getOrderAmount().subtract(auctionOrder.getMoneyBln()))
                        .outHost(auctionOrder.getOrderNo())
                        .aliPayEnum(AUCTION_ALI_PAY)
                        .wxPayEnum(AUCTION_WECHAT_PAY)
                        .shandePayEnum(shandePayEnum)
                        .lianlianPayEnum(lianLianPayEnum)
                        .sandAccountEnum(sandAccountEnum)
                        .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                        .goodsName(auctionOrder.getGoodsName())
                        .receiveUserId(auctionSend.getUserId())
                        .canTrade(canTrade)
                        .c2c(true)
                        .serviceCharge(commission)
                        .userId(payUserId).build());

        auctionOrder.setMoneyBln(auctionOrder.getMoneyBln().add(payVo.getMoneyBln()));

        // ??????????????????
        //auctionSend.setAuctionStatus(AuctionStatus.PAY_SUCCESS.getCode());
        auctionSendService.updateById(auctionSend);
        auctionOrder.setPayType(auctionPayForm.getPayType());
        auctionOrderService.updateById(auctionOrder);
        // ?????????????????? ??????????????? ????????????????????????????????????
        /*if (MONEY_TAPE.getCode().equals(auctionPayForm.getPayType())){
            // ??????????????????
            auctionOrderService.notifyPaySuccess(payVo.getOutHost(), payUserId);
        }*/

        if ( StrUtil.isBlank(payVo.getBody())){

            auctionOrderService.notifyPaySuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (Integer.valueOf(2).equals(auctionOrder.getGoodsType())){
                // ??????
                Box box = boxService.getById(auctionOrder.getGoodsId());
                title = StrUtil.format("????????? {} ??????!", box.getBoxTitle());
                form = StrUtil.format("????????????{};???????????????????????? {} ??????!",auctionOrder.getOrderAmount(), box.getBoxTitle());

            }
            if (Integer.valueOf(1).equals(auctionOrder.getGoodsType())){
                //??????
                CntCollection cntCollection = collectionService.getById(auctionOrder.getGoodsId());
                title = StrUtil.format("????????? {} ??????!", cntCollection.getCollectionName());
                form = StrUtil.format("????????????{};???????????????????????? {} ??????!",auctionOrder.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(payUserId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }
        return payVo;

    }

    /**
     * ???????????????
     * @param payUserId
     * @param auctionPayMarginForm
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVo payMargin(String payUserId, AuctionPayMarginForm auctionPayMarginForm) {
        Assert.isFalse(Integer.valueOf(5).equals(auctionPayMarginForm.getPayType()), "????????????????????????????????????");
        Assert.isFalse(Integer.valueOf(6).equals(auctionPayMarginForm.getPayType()), "????????????????????????????????????");
        CntUserDto cntUserDto = remoteBuiUserService.commUni(payUserId, SecurityConstants.INNER).getData();
        if (Integer.valueOf(0).equals(auctionPayMarginForm.getPayType())) {
            Assert.isTrue(SecurityUtils.matchesPassword(auctionPayMarginForm.getPayPass(),cntUserDto.getPayPass()),"??????????????????,?????????!");
        }
        AuctionSend auctionSend = auctionSendService.getById(auctionPayMarginForm.getAuctionSendId());
        Assert.isFalse(payUserId.equals(auctionSend.getUserId()), "???????????????????????????????????????");

        BigDecimal margin = auctionSend.getStartPrice().multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class)).setScale(2, RoundingMode.HALF_UP);
        AuctionMargin auctionMargin = auctionMarginService.getOne(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getUserId, payUserId)
                .eq(AuctionMargin::getAuctionSendId, auctionPayMarginForm.getAuctionSendId()));
        if (auctionMargin == null) {
            Integer serviceVal = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_ORDER_TIME, Integer.class);
            DelayLevelEnum defaultEnum = DelayLevelEnum.getDefaultEnum(serviceVal, DelayLevelEnum.LEVEL_6);
            String idStr = IdUtil.getSnowflakeNextIdStr();
            auctionMargin = Builder.of(AuctionMargin::new).build();
            auctionMargin.setId(idStr);
            auctionMargin.setAuctionSendId(auctionPayMarginForm.getAuctionSendId());
            auctionMargin.setPayType(auctionPayMarginForm.getPayType());
            auctionMargin.setUserId(payUserId);
            auctionMargin.setMargin(margin);
            auctionMargin.setEndTime(LocalDateTime.now().plusMinutes(DelayLevelEnum.getSourceConvertTime(defaultEnum, TimeUnit.MINUTES)));
            auctionMargin.createD(payUserId);
            deliverProducer.sendCancelAuctionMargin(idStr, Builder.of(DeliverMsg::new).with(DeliverMsg::setBuiId,idStr).with(DeliverMsg::setBuiName,StrUtil.format("????????????????????????,???????????????:{}",idStr)).with(DeliverMsg::setResetHost, idStr).build(), defaultEnum);
            auctionMarginService.save(auctionMargin);
        }
        //????????????
        AuctionMargin auctionMargin1 = auctionMarginService.getOne(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getUserId, payUserId)
                .eq(AuctionMargin::getAuctionSendId, auctionPayMarginForm.getAuctionSendId()));

        ShandePayEnum shandePayEnum = ShandePayEnum.AUCTION_MARGIN_SHANDE_PAY.setReturnUrl(auctionPayMarginForm.getReturnUrl(), auctionPayMarginForm.getReturnUrl());
        LianLianPayEnum lianLianPayEnum = LianLianPayEnum.AUCTION_MARGIN_SHANDE_PAY.setReturnUrl(auctionPayMarginForm.getReturnUrl(), auctionPayMarginForm.getReturnUrl());
        SandAccountEnum sandAccountEnum = SandAccountEnum.AUCTION_MARGIN_SANDACCOUNT_PAY.setReturnUrl(auctionPayMarginForm.getReturnUrl(), auctionPayMarginForm.getReturnUrl());
        PayVo payVo = rootPay.execPayVo(PayInfoDto.builder()
                .payType(auctionPayMarginForm.getPayType())
                .realPayMoney(auctionMargin1.getMargin().subtract(auctionMargin1.getMoneyBln()))
                .outHost(auctionMargin1.getId())
                .aliPayEnum(MARGIN_ALI_PAY)
                .wxPayEnum(MARGIN_WECHAT_PAY)
                .shandePayEnum(shandePayEnum)
                .lianlianPayEnum(lianLianPayEnum)
                .sandAccountEnum(sandAccountEnum)
                .c2c(false)
                .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                .goodsName(auctionSend.getGoodsName())
                .userId(payUserId).build());
        auctionMargin1.setMoneyBln(payVo.getMoneyBln());
        auctionMarginService.updateById(auctionMargin1);

        if (StrUtil.isBlank(payVo.getBody())) {
            notifyPayMarginSuccess(payVo.getOutHost());
            String title = StrUtil.format("???????????????!");
            String form = StrUtil.format("????????????{};??????????????????",auctionMargin1.getMargin());
            msgService.saveMsgThis(MsgThisDto.builder().userId(payUserId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }

        return payVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Lock("notifyPayMarginSuccess")
    public void notifyPayMarginSuccess(String outHost) {
        AuctionMargin auctionMargin = auctionMarginService.getById(outHost);
        Assert.isTrue(Objects.nonNull(auctionMargin),"???????????????????????????!");
        //????????????????????????????????????????????????
        auctionMargin.setPayMarginStatus(Integer.valueOf(1));
        auctionMarginService.updateById(auctionMargin);
    }





    //?????????

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVo payFixed(String userId, AuctionPayFixedForm auctionPayFixedForm) {
        realCheck(userId);
        CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        if (Integer.valueOf(0).equals(auctionPayFixedForm.getPayType())) {

            Assert.isTrue(SecurityUtils.matchesPassword(auctionPayFixedForm.getPayPass(),cntUserDto.getPayPass()),"??????????????????,?????????!");
        }

        AuctionSend auctionSend = auctionSendService.getById(auctionPayFixedForm.getAuctionSendId());
        boolean canTrade = moneyService.checkLlpayStatus(userId) && moneyService.checkLlpayStatus(auctionSend.getUserId());
        boolean canSandTrade  = moneyService.checkSandAccountStatus(userId) && moneyService.checkSandAccountStatus(auctionSend.getUserId());
        BigDecimal commission = auctionSend.getCommission();
        if (Integer.valueOf(5).equals(auctionPayFixedForm.getPayType())) {
            Assert.isTrue(canTrade, "??????????????????????????????????????????????????????");
        }
        if (Integer.valueOf(6).equals(auctionPayFixedForm.getPayType())) {
            Assert.isTrue(canSandTrade, "???????????????????????????????????????????????????????????????");
        }
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);

        Assert.isFalse(LocalDateTime.now().isBefore(auctionSend.getStartTime()), "??????????????????????????????????????????");
        Assert.isFalse(LocalDateTime.now().isAfter(auctionSend.getEndTime().plusMinutes(delayTime)),"?????????????????????");
        Assert.isFalse(auctionSend.getUserId().equals(userId), "???????????????????????????????????????");
        Assert.isTrue(auctionSend.getSoldPrice().compareTo(auctionSend.getNowPrice()) > 0,
                "????????????????????????????????????????????????");

        //????????????????????????????????????
        auctionSend.setAuctionSendStatus(AuctionSendStatus.WAIT_PAY.getCode());
        auctionSend.setNowPrice(auctionSend.getSoldPrice());
        LocalDateTime endPayTime = LocalDateTime.now().plusMinutes(systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_ORDER_TIME, Integer.class));
        auctionSend.setEndPayTime(endPayTime);
        auctionSendService.updateById(auctionSend);
        AuctionPrice one = getOne(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId())
                .eq(AuctionPrice::getUserId, userId).eq(AuctionPrice::getIsNew, 1));
        if (one != null) {
            one.setIsNew(2);
            updateById(one);
        }
        AuctionPrice auctionPrice = new AuctionPrice();
        auctionPrice.setId(IdUtil.getSnowflakeNextIdStr());
        auctionPrice.setAuctionStatus(AuctionStatus.WAIT_PAY.getCode());
        auctionPrice.setUserId(userId);
        auctionPrice.setAuctionSendId(auctionPayFixedForm.getAuctionSendId());
        auctionPrice.setUserName(remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData().getNickName());
        auctionPrice.setCreatedTime(LocalDateTime.now());
        auctionPrice.setEndPayTime(endPayTime);
        auctionPrice.setBidPrice(auctionSend.getSoldPrice());
        save(auctionPrice);

        AuctionOrder haveOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getSendAuctionId, auctionSend.getId())
                .eq(AuctionOrder::getToUserId, userId));
        if (haveOrder == null) {
            String auctionOrderHost = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                    .goodsId(auctionSend.getGoodsId())
                    .goodsName(auctionSend.getGoodsName())
                    .goodsType(auctionSend.getGoodsType())
                    .nowPrice(auctionSend.getSoldPrice())
                    .startPrice(auctionSend.getStartPrice())
                    .payType(auctionPayFixedForm.getPayType())
                    .orderAmount(auctionSend.getSoldPrice())
                    .auctionPriceId(auctionPrice.getId())
                    .sendAuctionId(auctionPayFixedForm.getAuctionSendId())
                    .fromUserId(auctionSend.getUserId())
                    .toUserId(userId).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));
        }

        AuctionOrder auctionOrder = auctionOrderService.getOne(Wrappers.<AuctionOrder>lambdaQuery().eq(AuctionOrder::getSendAuctionId, auctionSend.getId())
                .eq(AuctionOrder::getToUserId, userId));

        if (auctionOrder != null) {
            auctionSend.setAuctionOrderId(auctionOrder.getId());
            auctionSendService.updateById(auctionSend);
        }

        ShandePayEnum shandePayEnum = ShandePayEnum.AUCTION_FIX_SHANDE_PAY.setReturnUrl(auctionPayFixedForm.getReturnUrl(), auctionPayFixedForm.getReturnUrl());
        LianLianPayEnum lianLianPayEnum = LianLianPayEnum.AUCTION_FIX_SHANDE_PAY.setReturnUrl(auctionPayFixedForm.getReturnUrl(), auctionPayFixedForm.getReturnUrl());
        SandAccountEnum sandAccountEnum = SandAccountEnum.AUCTION_FIX_SANDACCOUNT_PAY.setReturnUrl(auctionPayFixedForm.getReturnUrl(), auctionPayFixedForm.getReturnUrl());
        Assert.isFalse(Integer.valueOf(5).equals(auctionPayFixedForm.getPayType()) && auctionOrder.getMoneyBln().compareTo(NumberUtil.add(0D)) >0, "???????????????????????????????????????");
        PayVo payVo = rootPay.execPayVo(PayInfoDto.builder()
                .payType(auctionPayFixedForm.getPayType())
                .realPayMoney(auctionOrder.getOrderAmount().subtract(auctionOrder.getMoneyBln()))
                .outHost(auctionOrder.getOrderNo())
                .aliPayEnum(FIXED_ALI_PAY)
                .wxPayEnum(FIXED_WECHAT_PAY)
                .shandePayEnum(shandePayEnum)
                .lianlianPayEnum(lianLianPayEnum)
                .sandAccountEnum(sandAccountEnum)
                .canTrade(canTrade)
                .c2c(true)
                .receiveUserId(auctionSend.getUserId())
                .serviceCharge(commission)
                .ipaddr(IpUtils.getIpAddr(ServletUtils.getRequest()))
                .goodsName(auctionOrder.getGoodsName())
                .userId(userId).build());

        auctionOrder.setMoneyBln(auctionOrder.getMoneyBln().add(payVo.getMoneyBln()));
        auctionOrderService.updateById(auctionOrder);


        if ( StrUtil.isBlank(payVo.getBody())){

            auctionOrderService.notifyPaySuccess(payVo.getOutHost());
            String title = "";
            String form = "";
            if (Integer.valueOf(2).equals(auctionOrder.getGoodsType())){
                // ??????
                Box box = boxService.getById(auctionOrder.getGoodsId());
                title = StrUtil.format("????????? {} ??????!", box.getBoxTitle());
                form = StrUtil.format("????????????{};????????????????????????????????? {} ??????!",auctionOrder.getOrderAmount(), box.getBoxTitle());

            }
            if (Integer.valueOf(1).equals(auctionOrder.getGoodsType())){
                //??????
                CntCollection cntCollection = collectionService.getById(auctionOrder.getGoodsId());
                title = StrUtil.format("????????? {} ??????!", cntCollection.getCollectionName());
                form = StrUtil.format("????????????{};????????????????????????????????? {} ??????!",auctionOrder.getOrderAmount(), cntCollection.getCollectionName());
            }
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }

        return payVo;
    }


    /**
     *  ???????????????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @Lock("checkAuctionEnd")
    public void checkAuctionEnd() {
        List<AuctionSend> sendList = auctionSendService.list(Wrappers.<AuctionSend>lambdaQuery().le(
                AuctionSend::getEndTime, LocalDateTime.now())
                .eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BIDING.getCode()));
        if (sendList.isEmpty()) return;
        for (AuctionSend auctionSend : sendList) {
            List<AuctionPrice> priceList = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId()));
            if (priceList.isEmpty()) {
                auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_PASS.getCode());
                List<AuctionMargin> list = auctionMarginService.list(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getAuctionSendId, auctionSend.getId()));
                if (!list.isEmpty()) {
                    for (AuctionMargin auctionMargin : list) {
                        Money buyerMoney = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, auctionMargin.getUserId()));
                        buyerMoney.setMoneyBalance(buyerMoney.getMoneyBalance().add(auctionMargin.getMargin()));
                        moneyService.updateById(buyerMoney);
                        logsService.saveLogs(LogInfoDto.builder().buiId(auctionMargin.getUserId()).jsonTxt("???????????????").formInfo(auctionMargin.getMargin().toString()).isType(PULL_SOURCE).modelType(MONEY_TYPE).build());
                    }
                }


                String info = "?????????????????????????????????";

                //?????????????????????
                if (auctionSend.getGoodsType() == 1) {
                    userCollectionService.showUserCollection(auctionSend.getUserId(), auctionSend.getMyGoodsId(),info);
                }
                if (auctionSend.getGoodsType() == 2) {
                    userBoxService.showUserBox(auctionSend.getMyGoodsId(), auctionSend.getUserId(), info);
                }
            }
        }
        auctionSendService.updateBatchById(sendList);

    }

    /**
     * ????????????????????????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @Lock("checkWinner")
    public void checkWinner() {
        List<AuctionSend> sendList = auctionSendService.list(Wrappers.<AuctionSend>lambdaQuery().le(
                AuctionSend::getEndTime, LocalDateTime.now())
                .eq(AuctionSend::getIsDelay, 1)
                .eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BIDING.getCode()));
        if (sendList.isEmpty()) return;
        for (AuctionSend auctionSend: sendList) {
            winnerOperation(auctionSend);
        }
        auctionSendService.updateBatchById(sendList);

    }

    /**
     * ????????????????????????????????????delayque???????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @Lock("checkDelayWinner")
    public void checkDelayWinner() {
        List<AuctionSend> sendList = auctionSendService.list(Wrappers.<AuctionSend>lambdaQuery().le(
                AuctionSend::getEndTime, LocalDateTime.now())
                .eq(AuctionSend::getIsDelay, 2)
                .eq(AuctionSend::getAuctionSendStatus, AuctionSendStatus.BID_BIDING.getCode()));
        if (sendList.isEmpty()) return;
        for (AuctionSend auctionSend : sendList) {
            winnerOperation(auctionSend);
        }
        auctionSendService.updateBatchById(sendList);
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @Lock("checkPayMarginFail")
    public  void checkPayMarginFail() {
        List<AuctionMargin> list = auctionMarginService.list(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getPayMarginStatus, Integer.valueOf(0))
                .lt(AuctionMargin::getEndTime, LocalDateTime.now()));
        if (list.isEmpty()) {
            return;
        }
        list.parallelStream().map(item -> {
            BigDecimal moneyBln = item.getMoneyBln();
            if (Objects.nonNull(moneyBln) && moneyBln.compareTo(NumberUtil.add(0D)) >=1){
                moneyService.orderBack(item.getUserId(),moneyBln,StrUtil.format("??????????????????,?????????????????? {},??????????????????!" , moneyBln));
            }
            return item;
        }).collect(Collectors.toList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void auctionEnd(String id) {
        AuctionSend auctionSend = auctionSendService.getById(id);
        Assert.isTrue(AuctionSendStatus.BID_BIDING.getCode().equals(auctionSend.getAuctionSendStatus()), "??????????????????????????????");
        List<AuctionPrice> priceList = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId()));
        //??????????????????????????????
        if (priceList.isEmpty()) {
            auctionSend.setAuctionSendStatus(AuctionSendStatus.BID_PASS.getCode());
            //?????????????????????????????????????????????????????????
            List<AuctionMargin> list = auctionMarginService.list(Wrappers.<AuctionMargin>lambdaQuery().eq(AuctionMargin::getAuctionSendId, auctionSend.getId()));
            if (!list.isEmpty()) {
                for (AuctionMargin auctionMargin : list) {
                    Money buyerMoney = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, auctionMargin.getUserId()));
                    buyerMoney.setMoneyBalance(buyerMoney.getMoneyBalance().add(auctionMargin.getMargin()));
                    moneyService.updateById(buyerMoney);
                    logsService.saveLogs(LogInfoDto.builder().buiId(auctionMargin.getUserId()).jsonTxt("???????????????").formInfo(auctionMargin.getMargin().toString()).isType(PULL_SOURCE).modelType(MONEY_TYPE).build());
                }
            }

            String info = "?????????????????????????????????";

            //?????????????????????
            if (auctionSend.getGoodsType() == 1) {
                userCollectionService.showUserCollection(auctionSend.getUserId(), auctionSend.getMyGoodsId(),info);
            }
            if (auctionSend.getGoodsType() == 2) {
                userBoxService.showUserBox(auctionSend.getMyGoodsId(), auctionSend.getUserId(), info);
            }
            auctionSendService.updateById(auctionSend);
        } else {
            //???????????????
            winnerOperation(auctionSend);
        }
    }

    @Override
    public void cancelAuctionMargin(String id) {
        AuctionMargin auctionMargin = auctionMarginService.getById(id);
        Assert.isTrue(Objects.nonNull(auctionMargin) && Integer.valueOf(0).equals(auctionMargin.getPayMarginStatus()), "?????????????????????????????????");
        BigDecimal moneyBln = auctionMargin.getMoneyBln();
        if (Objects.nonNull(moneyBln) && moneyBln.compareTo(NumberUtil.add(0D)) >=1){
            moneyService.orderBack(auctionMargin.getUserId(),moneyBln,StrUtil.format("??????????????????,?????????????????? {},??????????????????!" , moneyBln));
        }
    }



}
