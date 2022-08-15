package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.MsgCommDto;
import com.manyun.business.domain.dto.MsgThisDto;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.BoxMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.BoxStatus;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.BoxOpenType.NO_OPEN;
import static com.manyun.common.core.enums.BoxOpenType.OK_OPEN;
import static com.manyun.common.core.enums.BoxStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.TarStatus.CEN_YES_TAR;
import static com.manyun.common.core.enums.TarStatus.NO_TAR;
import static com.manyun.common.core.enums.WxPayEnum.BOX_WECHAT_PAY;

/**
 * <p>
 * 盲盒;盲盒主体表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class BoxServiceImpl extends ServiceImpl<BoxMapper, Box> implements IBoxService {

    @Autowired
    private IMediaService mediaService;

    @Autowired
    private IBoxCollectionService  boxCollectionService;

    @Autowired
    private IUserBoxService userBoxService;
    @Resource
    private BoxMapper boxMapper;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IMoneyService moneyService;

    @Autowired
    private ICntPostExcelService cntPostExcelService;

    @Autowired
    private RootPay rootPay;

    @Autowired
    private IUserCollectionService userCollectionService;

    @Autowired
    private ICntTarService tarService;

    @Autowired
    private ICntPostConfigService postConfigService;

    @Autowired
    private  ICntTarService cntTarService;

    @Autowired
    private IMsgService msgService;



    /**
     * 分页查询盲盒列表
     * @param boxQuery
     * @return
     */
    @Override
    public TableDataInfo<BoxListVo> pageList(BoxQuery boxQuery) {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<Box> boxList = list(Wrappers.<Box>lambdaQuery().eq(StrUtil.isNotBlank(boxQuery.getCateId()),Box::getCateId,boxQuery.getCateId()).like(StrUtil.isNotBlank(boxQuery.getBoxName()), Box::getBoxTitle, boxQuery.getBoxName()).ne(Box::getStatusBy,DOWN_ACTION.getCode()).orderByDesc(Box::getCreatedTime));
        // 数据组合查询
        return TableDataInfoUtil.pageTableDataInfo(boxList.parallelStream().map(this::initBoxListVo).collect(Collectors.toList()),boxList);
    }

    /**
     * 根据盲盒编号 查询详细信息
     * @param id
     * @return
     */
    @Override
    public BoxVo info(String id,String userId) {
        Box box = getById(id);
        BoxListVo boxListVo = initBoxListVo(box);
        BoxVo boxVo = Builder.of(BoxVo::new).build();
        boxVo.setBoxListVo(boxListVo);
        // 组合与藏品关联数据
        boxVo.setBoxCollectionJoinVos(boxCollectionService.findJoinCollections(id));
        // 是否能够购买? 预先状态判定
        boxVo.setTarStatus(NO_TAR.getCode());
        // 低耦性校验
        if (StrUtil.isNotBlank(box.getTarId()) && StringUtils.isNotBlank(userId))
            boxVo.setTarStatus(tarService.tarStatus(userId,id));


        Integer postTime = null;
        //提前购?
        if (Objects.nonNull(box.getPostTime())  && StringUtils.isNotBlank(userId)){
            // 两种验证,一种 excel , 一种 关联性 方式
            //1. excel true 可以提前购, false 不可以
            //2. 配置模板 true 可以提前购 false 不可以
            if (cntPostExcelService.isExcelPostCustomer(userId,id) || postConfigService.isConfigPostCustomer(userId,id)){
                postTime = box.getPostTime();
            }
        }
        boxVo.setPostTime(postTime);
        return boxVo;
    }



    /**
     * 购买盲盒
     * @param boxSellForm
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayVo sellBox(BoxSellForm boxSellForm,String userId) {

        // 总结校验 —— 支付方式
        Box box = getById(boxSellForm.getBoxId());
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(boxSellForm.getSellNum(), box.getRealPrice());
        checkSell(box,userId,boxSellForm.getPayType(),realPayMoney);
        // 锁优化
        int rows = boxMapper.updateLock(box.getId(),boxSellForm.getSellNum());
         Assert.isTrue(rows>=1,"您来晚了!");

        // 根据支付方式创建订单  通用适配方案 余额直接 减扣操作
        //1. 先创建对应的订单
       String outHost =   orderService.createOrder(OrderCreateDto.builder()
               .orderAmount(realPayMoney)
               .buiId(box.getId())
               .payType(boxSellForm.getPayType())
               .goodsType(BusinessConstants.ModelTypeConstant.BOX_TAYPE)
               .collectionName(box.getBoxTitle())
               .goodsNum(boxSellForm.getSellNum())
               .userId(userId)
               .build());
        //2. 然后调取对应支付即可
        /**
         * 根据类型  发起支付订单
         * 余额支付直接扣除 订单状态做变更即可
         **/
        PayVo payVo =  rootPay.execPayVo(
                PayInfoDto.builder()
                        .payType(boxSellForm.getPayType())
                        .realPayMoney(realPayMoney)
                        .outHost(outHost)
                        .aliPayEnum(BOX_ALI_PAY)
                        .wxPayEnum(BOX_WECHAT_PAY)
                        .userId(userId).build());
        // 走这一步如果 是余额支付 那就说明扣款成功了！！！
        if (MONEY_TAPE.getCode().equals(boxSellForm.getPayType()) && StrUtil.isBlank(payVo.getBody())){
            // 调用完成订单
            orderService.notifyPaySuccess(payVo.getOutHost());
            // 没有用户信息的记录
            String title = StrUtil.format("购买了 {} 盲盒!", box.getBoxTitle());
            String form = StrUtil.format("使用余额{};购买了 {} 盲盒!",realPayMoney.toString(), box.getBoxTitle());
            msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(form).msgTitle(title).build());
            msgService.saveCommMsg(MsgCommDto.builder().msgTitle(title).msgForm(form).build());
        }

        return payVo;



    }

    /**
     * 查询我的盲盒分页信息
     * @param pageQuery
     * @param userId
     * @return
     */
    @Override
    public TableDataInfo<UserBoxVo> userBoxPageList(UseAssertQuery useAssertQuery, String userId) {
        PageHelper.startPage(useAssertQuery.getPageNum(),useAssertQuery.getPageSize());
        List<UserBoxVo> userBoxList = userBoxService.pageUserBox(userId,useAssertQuery.getCommName());
        // 数据组合
        List<UserBoxVo> tarData = userBoxList.parallelStream().map(item -> {
            item.setMediaVos(mediaService.initMediaVos(item.getBoxId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            return item;
        }).collect(Collectors.toList());
        return TableDataInfoUtil.pageTableDataInfo(tarData,userBoxList);

    }

    /**
     * 开启盲盒
     * @param userBoxId
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized String openBox(String userBoxId, String userId) {
        UserBox userBox = userBoxService.getOne(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getId, userBoxId).eq(UserBox::getUserId, userId).eq(UserBox::getBoxOpen, NO_OPEN.getCode()));
        Assert.isTrue(Objects.nonNull(userBox),"盲盒已被开启,请核实!");
       // 什么样的随机算法 去得到概率性的藏品？
        // 1. 将所有藏品的概率比例拿到
        List<BoxCollection> boxCollections = boxCollectionService.list(Wrappers.<BoxCollection>lambdaQuery().eq(BoxCollection::getBoxId, userBox.getBoxId()));
        // 2. 进行分流 容放
        BoxCollection luckCollection  = luckGetCollection(boxCollections);
        // 3. 得到后开始绑定了
        String info = StrUtil.format("恭喜您,开启盲盒,得到 {} 藏品,请注意查收!", luckCollection.getCollectionName());
        userCollectionService.bindCollection(userId,luckCollection.getCollectionId(),luckCollection.getCollectionName(),info,Integer.valueOf(1));
        userBox.setBoxOpen(OK_OPEN.getCode());
        userBoxService.updateById(userBox);
        Box box = boxMapper.selectById(userBox.getBoxId());
        String format = StrUtil.format("开启{}盲盒,得到{}藏品!",box.getBoxTitle() ,luckCollection.getCollectionName());

        msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(info).msgTitle(format).build());
        msgService.saveCommMsg(MsgCommDto.builder().msgTitle(format).msgForm(format).build());
        return info;
    }

    @Override
    public List<String> queryDict(String keyword) {
        return  list(Wrappers.<Box>lambdaQuery().select(Box::getBoxTitle).like(Box::getBoxTitle,keyword).orderByDesc(Box::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getBoxTitle()).collect(Collectors.toList());
    }

    @Override
    public Integer tarBox(String id, String userId) {
        return cntTarService.tarBox(getById(id),userId);
    }

    /**
     * 根据 组件 规则 获取到幸运藏品,进行绑定用户
     * @param boxCollections
     * @return
     */
    private  BoxCollection luckGetCollection(List<BoxCollection> boxCollections) {
       return nowTimeGave(boxCollections);
    }

    /**
     * 根据当前 概率 做时间戳 取余操作, 相当于有时间 节点才能开出来好盲盒
     * @param boxCollections
     * @return
     */
    private BoxCollection nowTimeGave(List<BoxCollection> boxCollections){
        // tranSvg 为概率比
        int count = 0;
        HashMap<Integer, String> collectionMap = Maps.newHashMap();
        // 得到所有概率
        for (BoxCollection boxCollection : boxCollections) {
            // 向上取整
            int value = boxCollection.getTranSvg().setScale(0, RoundingMode.HALF_UP).intValue();
            value = count + value;
            for (int i = count; i < value; i++) {
                collectionMap.put(i,boxCollection.getId());
                count ++;
            }
        }
        Integer luckBum =  Long.valueOf(DateUtil.current() % (long)count).intValue();
        String id = collectionMap.get(luckBum);
        return boxCollections.parallelStream().filter(item -> item.getId().equals(id)).findAny().get();
    }


    private void checkSell(Box box,String userId,Integer payType,BigDecimal realPayMoney) {
        // 校验盲盒主体是否存在
        Assert.isTrue(Objects.nonNull(box),"盲盒编号有误,请核实!");

        // 校验是否库存不够了
        Assert.isFalse(Integer.valueOf(0).equals(box.getBalance()),"库存不足了!");
        // 重复校验状态
        Assert.isTrue(BoxStatus.UP_ACTION.getCode().equals(box.getStatusBy()),"您来晚了,售罄了!");

        //是否有未支付订单
        List<Order> orders = orderService.checkUnpaidOrder(userId);
        Assert.isFalse(orders.size() > 0 ,"您有未支付订单，暂不可购买");

       // 如果是余额支付,需要验证下是否充足
        if (MONEY_TAPE.getCode().equals(payType)) {
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
           Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney) >=0,"余额不足,请核实!");
        }

        // 是否需要抽？
        // 是否需要抽？
        if (StrUtil.isNotBlank(box.getTarId()))
            if (!CEN_YES_TAR.getCode().equals(tarService.tarStatus(userId,box.getId())))
                Assert.isFalse(Boolean.TRUE,"未参与抽签,或暂未购买资格!");

        // 是否能够提前购？
        Boolean publishTimeFlag = Boolean.TRUE;
        // 如果是提前购 并且 购买的时机符合判定条件就进去判定 是否有资格提前购买
        if (Objects.nonNull(box.getPostTime()) && LocalDateTime.now().compareTo(box.getPublishTime()) < 0){
            // 是提前购的属性
            //  表格是否满足提前购 && 配置是否满足提前购
            // 1.1 满足就 根据发售时间 - postTime(分钟) ;
               //1.1.1 判定下当前时间 就可以购买了 publishTimeFlag = false
               // 1.1.2 如果当前时间还不满足 就拦截提示 好了
            // 1.2 不满足就 直接拦截,提示.
            if (cntPostExcelService.isExcelPostCustomer(userId, box.getId()) || postConfigService.isConfigPostCustomer(userId, box.getId())){
                if (LocalDateTime.now().compareTo(box.getPublishTime().minusMinutes(box.getPostTime())) >=0){
                    publishTimeFlag = Boolean.FALSE;
                }
            }
        }

        // 校验盲盒是否到了发行时间
        if (publishTimeFlag)Assert.isTrue(LocalDateTime.now().compareTo(box.getPublishTime()) > 0,"发行时间未到,请核实!");

    }


    /**
     * 获取盲盒基本数据
     * @param boxId
     * @return
     */
    @Override
    public BoxListVo getBaseBoxListVo(String boxId){
        return initBoxListVo(getById(boxId));
    }

    /**
     * 转义数据
     * @param box
     * @return
     */
    private BoxListVo initBoxListVo(Box box){
        BoxListVo boxListVo = Builder.of(BoxListVo::new).build();
        BeanUtil.copyProperties(box,boxListVo);
        // 需要集成图片服务
        boxListVo.setMediaVos(mediaService.initMediaVos(box.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        return boxListVo;

    }

}
