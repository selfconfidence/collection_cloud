package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.design.delay.DelayAbsAspect;
import com.manyun.business.design.delay.DelayQueue;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.AuctionOrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.AuctionOrder;
import com.manyun.business.domain.entity.AuctionPrice;
import com.manyun.business.domain.entity.AuctionSend;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.form.AuctionPayForm;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.vo.AuctionPriceVo;
import com.manyun.business.domain.vo.PayVo;
import com.manyun.business.mapper.AuctionPriceMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionSendStatus;
import com.manyun.common.core.enums.AuctionStatus;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.AliPayEnum.*;
import static com.manyun.common.core.enums.WxPayEnum.*;


/**
 * <p>
 * 竞价表 服务实现类
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
    private RemoteBuiUserService remoteBuiUserService;


    //判断是否出过价

    @Override
    public R checkPayMargin(AuctionPriceForm auctionPriceForm, String userId) {
        List<AuctionPrice> priceList = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionPriceForm.getAuctionSendId())
                .eq(AuctionPrice::getUserId, userId));

        //判断是否出过价
        if (priceList.isEmpty()) {
            //支付保证金
            return R.fail(CodeStatus.NO_PAY_MARGIN.getCode(),"尚未支付保证金，请先支付");
        } else {
            return R.ok("已支付保证金，可以出价");
        }
    }


    @Override
    public synchronized R myAuctionPrice(AuctionPriceForm auctionPriceForm, String userId) {
        LoginBusinessUser businessUser = SecurityUtils.getTestLoginBusinessUser();
        AuctionSend auctionSend = auctionSendService.getById(auctionPriceForm.getAuctionSendId());
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);
        if (LocalDateTime.now().isBefore(auctionSend.getStartTime())) {
            return R.fail("当前竞品尚未开拍，请稍后再试");
        }
        if (LocalDateTime.now().isAfter(auctionSend.getEndTime().plusMinutes(delayTime))) {
            return R.fail("当前拍卖已结束");
        }
        if (auctionSend.getUserId().equals(businessUser.getUserId())) {
            return R.fail("自己不可购买自己送拍的产品");
        }

        //判断是否延拍，延拍加时间
        if (LocalDateTime.now().isAfter(auctionSend.getEndTime())) {
            auctionSend.setEndTime(auctionSend.getEndTime().plusMinutes(delayTime));

            // 分钟为单位
            long l = TimeUnit.MINUTES.toSeconds(delayTime);
            delayQueue.put(auctionSend.getId(),l, new DelayAbsAspect<String>() {
                @Override
                public void invocationSuccess(String s) {
                    //回调成功,生成订单
                    String auctionOrderNo = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                            .goodsId(auctionSend.getGoodsId())
                            .goodsName(auctionSend.getGoodsName())
                            .goodsType(auctionSend.getGoodsType())
                            .nowPrice(auctionSend.getNowPrice())
                            .sendAuctionid(s)
                            .userId(businessUser.getUserId()).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));
                    auctionSend.setAuctionStatus(AuctionSendStatus.WAIT_PAY.getCode());

                    //成功后退还未拍中者保证金
                    List<AuctionPrice> list = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, s));
                    Set<String> collect = list.parallelStream().map(item -> item.getUserId()).collect(Collectors.toSet());
                    for (String userId : collect) {
                        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
                        money.setMoneyBalance(money.getMoneyBalance().add(auctionSend.getMargin()));
                        moneyService.updateById(money);
                    }
                }

                @Override
                public void invocationFail(String s) {

                }
            });


            auctionSendService.updateById(auctionSend);
        }
        List<AuctionOrder> auctionOrders = auctionOrderService.checkUnpaidOrder(businessUser.getUserId());
        if (auctionOrders.size() > 0 ) {
            return R.fail("您有未支付订单，暂不可出价");
        }
        //判断竞拍状态（竞拍中才可出价）
        if (!auctionSend.getAuctionStatus().equals(AuctionStatus.BID_BIDING.getCode())) {
            return R.fail("当前竞品不可出价");
        }
        List<AuctionPrice> list = this.list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(AuctionPrice::getAuctionSendId, auctionPriceForm.getAuctionSendId()).orderByDesc(AuctionPrice::getBidPrice));

        BigDecimal range = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_PRICE_RANGE, BigDecimal.class);

        AuctionPrice auctionPrice = new AuctionPrice();
        if (list.isEmpty()) {
            auctionPrice.setBidPrice(auctionSend.getStartPrice().add(range));
        }
        if (list.size() > 0) {
            auctionPrice.setBidPrice(list.get(0).getBidPrice().add(range));
        }
        auctionPrice.setUserId(businessUser.getUserId());
        auctionPrice.setAuctionSendId(auctionPriceForm.getAuctionSendId());
        auctionPrice.setUserName(businessUser.getUsername());
        auctionPrice.setCreatedTime(LocalDateTime.now());
        this.save(auctionPrice);

        auctionSendService.update(Wrappers.<AuctionSend>lambdaUpdate()
                .set(AuctionSend::getNowPrice, auctionPrice.getBidPrice()));

        return R.ok();
    }

    /**
     * 竞拍列表
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

    //倒计时结束，创建订单，发起支付

    //竞拍结束后支付
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized PayVo payAuction(String payUserId, AuctionPayForm auctionPayForm) {
        AuctionPrice auctionPrice = getById(auctionPayForm.getAuctionPriceId());
        AuctionSend auctionSend = auctionSendService.getById(auctionPrice.getAuctionSendId());
        AuctionOrder auctionOrder = auctionOrderService.getById(auctionSend.getAuctionOrderId());
        /*String auctionOrderHost = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                .goodsId(auctionSend.getGoodsId())
                .goodsName(auctionSend.getGoodsName())
                .goodsType(auctionSend.getGoodsType())
                .nowPrice(auctionSend.getNowPrice())
                .payType(auctionPayForm.getPayType())
                .sendAuctionid(auctionPayForm.getAuctionSendId())
                .userId(payUserId).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));*/

        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(auctionPayForm.getPayType())
                        .realPayMoney(auctionSend.getNowPrice())
                        .outHost(auctionOrder.getOrderNo())
                        .aliPayEnum(AUCTION_ALI_PAY)
                        .wxPayEnum(AUCTION_WECHAT_PAY)
                        .userId(payUserId).build());

        // 修改拍卖信息
        //auctionSend.setAuctionStatus(AuctionStatus.PAY_SUCCESS.getCode());
        auctionSendService.updateById(auctionSend);
        auctionOrder.setPayType(auctionPayForm.getPayType());
        auctionOrderService.updateById(auctionOrder);
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        /*if (MONEY_TAPE.getCode().equals(auctionPayForm.getPayType())){
            // 调用完成订单
            auctionOrderService.notifyPaySuccess(payVo.getOutHost());
        }*/

        return payVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized PayVo payMargin(String payUserId, AuctionPayForm auctionPayForm) {

        AuctionSend auctionSend = auctionSendService.getById(auctionPayForm.getAuctionSendId());
        //用户余额
        BigDecimal margin = auctionSend.getStartPrice().multiply(systemService.getVal(BusinessConstants.SystemTypeConstant.MARGIN_SCALE, BigDecimal.class)).setScale(2, RoundingMode.HALF_UP);
        Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, payUserId));
        BigDecimal moneyBalance = money.getMoneyBalance();

        String payMarginHost ="";

        PayVo payVo = rootPay.execPayVo(PayInfoDto.builder()
                .payType(auctionPayForm.getPayType())
                .realPayMoney(margin)
                .aliPayEnum(MARGIN_ALI_PAY)
                .wxPayEnum(MARGIN_WECHAT_PAY)
                .userId(payUserId).build());

        return payVo;
    }

    //一口价

    public synchronized PayVo payFixed(String userId, AuctionPayForm auctionPayForm) {

        LoginBusinessUser businessUser = SecurityUtils.getTestLoginBusinessUser();
        AuctionSend auctionSend = auctionSendService.getById(auctionPayForm.getAuctionSendId());
        Integer delayTime = systemService.getVal(BusinessConstants.SystemTypeConstant.AUCTION_DELAY_TIME, Integer.class);

        Assert.isFalse(LocalDateTime.now().isBefore(auctionSend.getStartTime()), "当前竞品尚未开拍，请稍后再试");
        Assert.isFalse(LocalDateTime.now().isAfter(auctionSend.getEndTime().plusMinutes(delayTime)),"当前拍卖已结束");
        Assert.isFalse(auctionSend.getUserId().equals(businessUser.getUserId()), "自己不可购买自己送拍的产品");
        Assert.isTrue(auctionSend.getSoldPrice().compareTo(auctionSend.getNowPrice()) > 0,
                "当前竞拍价已大于一口价，不可购买");

        //修改竞拍状态，相当于锁单
        auctionSend.setAuctionStatus(AuctionStatus.WAIT_PAY.getCode());
        auctionSendService.updateById(auctionSend);

        String auctionOrderHost = auctionOrderService.createAuctionOrder(AuctionOrderCreateDto.builder()
                .goodsId(auctionSend.getGoodsId())
                .goodsName(auctionSend.getGoodsName())
                .goodsType(auctionSend.getGoodsType())
                .nowPrice(auctionSend.getSoldPrice())
                .payType(auctionPayForm.getPayType())
                .sendAuctionid(auctionPayForm.getAuctionSendId())
                .userId(userId).build(), (idStr) -> auctionSend.setAuctionOrderId(idStr));

        PayVo payVo = rootPay.execPayVo(PayInfoDto.builder()
                .payType(auctionPayForm.getPayType())
                .realPayMoney(auctionSend.getSoldPrice())
                .outHost(auctionOrderHost)
                .aliPayEnum(FIXED_ALI_PAY)
                .wxPayEnum(FIXED_WECHAT_PAY)
                .userId(userId).build());

        return payVo;
    }


    /**
     *  竞品超过时间无人出价则流拍
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void checkAuctionEnd() {
        List<AuctionSend> sendList = auctionSendService.list(Wrappers.<AuctionSend>lambdaQuery().le(AuctionSend::getEndTime, LocalDateTime.now())
                .eq(AuctionSend::getAuctionStatus, AuctionStatus.BID_BIDING));
        if (sendList.isEmpty()) return;
        for (AuctionSend auctionSend : sendList) {
            List<AuctionPrice> priceList = list(Wrappers.<AuctionPrice>lambdaQuery().eq(AuctionPrice::getAuctionSendId, auctionSend.getId()));
            if (priceList.isEmpty()) {
                auctionSend.setAuctionStatus(AuctionStatus.BID_PASS.getCode());
                auctionSendService.updateById(auctionSend);
            }
        }

    }


}
