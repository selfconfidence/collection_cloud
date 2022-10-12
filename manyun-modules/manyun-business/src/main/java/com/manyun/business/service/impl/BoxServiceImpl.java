package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
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
import com.manyun.business.domain.form.BoxOrderSellForm;
import com.manyun.business.domain.form.BoxSellForm;
import com.manyun.business.domain.query.BoxQuery;
import com.manyun.business.domain.query.UseAssertQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.BoxMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.redis.box.BoxListRedisVo;
import com.manyun.comm.api.domain.redis.box.BoxRedisVo;
import com.manyun.comm.api.domain.redis.collection.CollectionAllRedisVo;
import com.manyun.comm.api.domain.redis.collection.CollectionRedisVo;
import com.manyun.common.core.annotation.Lock;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.BoxStatus;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.redis.domian.dto.BuiCronDto;
import com.manyun.common.redis.service.BuiCronService;
import com.manyun.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.BOX_INFO;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.BoxOpenType.NO_OPEN;
import static com.manyun.common.core.enums.BoxOpenType.OK_OPEN;
import static com.manyun.common.core.enums.BoxStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.BoxStatus.NULL_ACTION;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
import static com.manyun.common.core.enums.TarStatus.CEN_YES_TAR;
import static com.manyun.common.core.enums.TarStatus.NO_TAR;
import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;
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

    @Autowired
    private BuiCronService buiCronService;


    @Autowired
    private  RemoteBuiUserService userService;

    @Autowired
    private RedisService redisService;



    /**
     * 分页查询盲盒列表
     * @param boxQuery
     * @return
     */
    @Override
    public TableDataInfo<BoxListVo> pageList(BoxQuery boxQuery) {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<Box> boxList = list(Wrappers.<Box>lambdaQuery().eq(StrUtil.isNotBlank(boxQuery.getCateId()),Box::getCateId,boxQuery.getCateId()).ne(Box::getStatusBy,DOWN_ACTION.getCode()).like(StrUtil.isNotBlank(boxQuery.getBoxName()), Box::getBoxTitle, boxQuery.getBoxName()).orderByDesc(Box::getCreatedTime));
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
        // 不在入库,查询redis 缓存池中
        Box box = getById(id);
        BoxListVo boxListVo = initBoxListVo(box);
        BoxVo boxVo = Builder.of(BoxVo::new).build();
        boxVo.setBoxListVo(boxListVo);
        // 组合与藏品关联数据
        boxVo.setBoxCollectionJoinVos(boxCollectionService.findJoinCollections(id));
        // 是否能够购买? 预先状态判定
        boxVo.setTarStatus(NO_TAR.getCode());
        // 低耦性校验
        if (StrUtil.isNotBlank(box.getTarId()) && StringUtils.isNotBlank(userId)){
            boxVo.setTarStatus(tarService.tarStatus(userId,id));
            CntTar cntTar = tarService.getById(box.getTarId());
            boxVo.setOpenTime(cntTar.getOpenTime());
        }

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
    public PayVo sellBox(BoxSellForm boxSellForm,String userId, String ipaddr) {

        // 总结校验 —— 支付方式
        Box box = getById(boxSellForm.getBoxId());
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(boxSellForm.getSellNum(), box.getRealPrice());
        checkSell(box,userId,boxSellForm.getPayType(),realPayMoney);
        // 锁优化
        checkBalance(box.getId(),boxSellForm.getSellNum());

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
                        .ipaddr(ipaddr)
                        .goodsName(box.getBoxTitle())
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

    private void checkBalanceCache(Box box,Integer sellNum){
        boolean flag = buiCronService.doBuiDegressionBalanceCache(BOX_MODEL_TYPE, box.getId(), sellNum);
        Assert.isTrue(flag,"您来晚了,已售罄了!");
    }

    @Override
    public void checkBalance(String boxId, Integer sellNum) {
        int rows = boxMapper.updateLock(boxId,sellNum);
        if (!(rows >=1)){
            update(Wrappers.<Box>lambdaUpdate().eq(Box::getId, boxId).set(Box::getStatusBy, NULL_ACTION.getCode()));
           // Assert.isTrue(Boolean.FALSE,"您来晚了,已售罄了!");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String sellOrderBox(BoxOrderSellForm boxOrderSellForm, String userId) {
        // 总结校验 —— 支付方式
        Box box = getById(boxOrderSellForm.getBoxId());
        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(boxOrderSellForm.getSellNum(), box.getRealPrice());
        checkOrderSell(box,userId);
        // 锁优化
        checkBalanceCache(box,boxOrderSellForm.getSellNum());
        //1. 先创建对应的订单
        String outHost =   orderService.createOrder(OrderCreateDto.builder()
                .orderAmount(realPayMoney)
                .buiId(box.getId())
                .goodsType(BusinessConstants.ModelTypeConstant.BOX_TAYPE)
                .collectionName(box.getBoxTitle())
                .goodsNum(boxOrderSellForm.getSellNum())
                .userId(userId)
                .build());
        return orderService.getOne(Wrappers.<Order>lambdaQuery().eq(Order::getOrderNo, outHost)).getId();
    }

    @Override
    public TableDataInfo<BoxListRedisVo> homeCacheList() {
        Map<String, BoxRedisVo> entries = redisService.redisTemplate.boundHashOps(BOX_INFO).entries();
        Collection<BoxRedisVo> cacheList = entries.values();
        List<BoxListRedisVo> collectionRedisVos = cacheList.parallelStream().map(item -> {
            BoxListRedisVo boxListVo = item.getBoxListVo();
            BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(BOX_MODEL_TYPE, boxListVo.getId());
            boxListVo.setBalance(typeBalanceCache.getBalance());
            boxListVo.setSelfBalance(typeBalanceCache.getSelfBalance());
            if (boxListVo.getPublishTime().isAfter(LocalDateTime.now())) {
                boxListVo.setPreStatus(1);
            } else {
                boxListVo.setPreStatus(2);
            }
            if (boxListVo.getBalance().equals(0)){
                boxListVo.setStatusBy(2);
            }
            return boxListVo;
        }).sorted((item1,item2)->{
            if (Objects.nonNull(item1) && Objects.nonNull(item2))
                return item2.getCreatedTime().compareTo(item1.getCreatedTime());
            return 0;
        }).collect(Collectors.toList());
        return TableDataInfoUtil.pageCacheData(collectionRedisVos, collectionRedisVos.size());
    }

    @Override
    public BoxRedisVo infoCache(String id, String userId) {
        // 不在入库,查询redis 缓存池中
      /*  Box box = getById(id);
        BoxListVo boxListVo = initBoxListVo(box);
        BoxVo boxVo = Builder.of(BoxVo::new).build();
        boxVo.setBoxListVo(boxListVo);
        // 组合与藏品关联数据
        boxVo.setBoxCollectionJoinVos(boxCollectionService.findJoinCollections(id));*/
        BoxRedisVo boxVo = redisService.getCacheMapValue(BOX_INFO, id);
        Assert.isTrue(Objects.nonNull(boxVo),"盲盒未找到,请核实!");
        BoxListRedisVo boxListVo = boxVo.getBoxListVo();
        // 是否能够购买? 预先状态判定
        boxVo.setTarStatus(NO_TAR.getCode());
        Box box = getOne(Wrappers.<Box>lambdaQuery().select(Box::getId, Box::getTarId, Box::getPostTime).eq(Box::getId, id));
        // 低耦性校验
        if (StrUtil.isNotBlank(box.getTarId()) && StringUtils.isNotBlank(userId)){
            boxVo.setTarStatus(tarService.tarStatus(userId,id));
            CntTar cntTar = tarService.getById(box.getTarId());
            boxVo.setOpenTime(cntTar.getOpenTime());
        }

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

        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(BOX_MODEL_TYPE, id);
        boxListVo.setBalance(typeBalanceCache.getBalance());
        boxListVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (boxListVo.getPublishTime().isAfter(LocalDateTime.now())) {
            boxListVo.setPreStatus(1);
        } else {
            boxListVo.setPreStatus(2);
        }
        if (Integer.valueOf(0).equals(typeBalanceCache.getBalance())) {
            boxListVo.setStatusBy(2);
        }
        boxVo.setPostTime(postTime);
        return boxVo;
    }

    private void checkOrderSell(Box box, String userId) {
        commCheckSell(box, userId);
        tarCheckBox(box,userId);
        postCheckBox(box, userId);
        realCheckCollection(userId);
        conditionOrder(box,userId);

    }

    private void conditionOrder(Box box, String userId) {
        Integer limitNumber = box.getLimitNumber();
        // 限购逻辑生效
        if (Objects.nonNull(limitNumber) && limitNumber >0){
            // 当前资产是否是提前购得资产?
            // 查询用户所有已经完成的订单!
            int sellNumber = orderService.overCount(box.getId(),userId);
            if (Objects.nonNull(box.getPostTime()) ){
                // 是提前购得资产 并且已经到了发布时间
                if (LocalDateTime.now().compareTo(box.getPublishTime()) > 0){
                    // 如果到了发布时间，就将已经参与提前购的次数，直接 - 整个订单的数量即可
                    int excelSellNum = cntPostExcelService.getSellNum(userId, box.getId());
                    int postConfigSellNum = postConfigService.getSellNum(userId, box.getId());
                    sellNumber =  ((excelSellNum + postConfigSellNum) - sellNumber);
                }else{
                    // 没有到发售时间的话就不管 中肯写法,能走到这里提前购逻辑是过了
                    return;
                }

            }
            Assert.isTrue(limitNumber > Math.abs(sellNumber),"抢的太多了,被限购了哦!");

        }
    }

    private void realCheckCollection(String userId) {

        R<CntUserDto> cntUserDtoR = userService.commUni(userId, SecurityConstants.INNER);
        CntUserDto data = cntUserDtoR.getData();
        Assert.isTrue(OK_REAL.getCode().equals(data.getIsReal()),"暂未实名认证,请实名认证!");
    }

    /**
     * 查询我的盲盒分页信息
     * @param
     * @param userId
     * @return
     */
    @Override
    public TableDataInfo<UserBoxVo> userBoxPageList(UseAssertQuery useAssertQuery, String userId) {
        PageHelper.startPage(useAssertQuery.getPageNum(),useAssertQuery.getPageSize());
        List<UserBoxVo> userBoxList = userBoxService.pageUserBox(userId,useAssertQuery.getCommName());
        // 数据组合
        List<UserBoxVo> tarData = userBoxList.parallelStream().map(item -> {
            item.setMediaVos(mediaService.initMediaVos(item.getBoxId(), BOX_MODEL_TYPE));
            item.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(item.getBoxId(), BOX_MODEL_TYPE));
            item.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(item.getBoxId(), BOX_MODEL_TYPE));
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
    @Lock("openBox")
    public OpenBoxCollectionVo openBox(String userBoxId, String userId) {
        UserBox userBox = userBoxService.getOne(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getId, userBoxId).eq(UserBox::getUserId, userId).eq(UserBox::getBoxOpen, NO_OPEN.getCode()));
        Assert.isTrue(Objects.nonNull(userBox),"盲盒已被开启,请核实!");
       // 什么样的随机算法 去得到概率性的藏品？
        // 1. 将所有藏品的概率比例拿到
        List<BoxCollection> boxCollections = boxCollectionService.list(Wrappers.<BoxCollection>lambdaQuery().eq(BoxCollection::getBoxId, userBox.getBoxId()).ne(BoxCollection::getOpenQuantity, Integer.valueOf(0)));
        // 2. 进行分流 容放
        Assert.isTrue(!boxCollections.isEmpty(),"此盲盒所有藏品已经售罄,沦为空盲盒,请联系客服人员!");
        BoxCollection luckCollection  = luckGetCollection(boxCollections);

        //最后一步验证
        Assert.isTrue(boxCollectionService.update(Wrappers.<BoxCollection>lambdaUpdate().eq(BoxCollection::getId,luckCollection.getId()).ge(BoxCollection::getOpenQuantity,Integer.valueOf(1)).set(BoxCollection::getOpenQuantity, luckCollection.getOpenQuantity() - 1 ).set(BoxCollection::getOpenNumber,luckCollection.getOpenNumber() + 1)),"开盲盒有误,请重试!");
        // 3. 得到后开始绑定了
        String info = StrUtil.format("恭喜您,开启盲盒,得到 {} 藏品,请注意查收!", luckCollection.getCollectionName());
        userCollectionService.bindCollection(userId,luckCollection.getCollectionId(),luckCollection.getCollectionName(),info);
        userBox.setBoxOpen(OK_OPEN.getCode());
        userBoxService.updateById(userBox);
        Box box = boxMapper.selectById(userBox.getBoxId());
        String format = StrUtil.format("开启{}盲盒,得到{}藏品!",box.getBoxTitle() ,luckCollection.getCollectionName());

        msgService.saveMsgThis(MsgThisDto.builder().userId(userId).msgForm(info).msgTitle(format).build());
        msgService.saveCommMsg(MsgCommDto.builder().msgTitle(format).msgForm(format).build());
        return OpenBoxCollectionVo
                .builder()
                .info(info)
                .collectionName(luckCollection.getCollectionName())
                .mediaVos(mediaService.initMediaVos(luckCollection.getCollectionId(),COLLECTION_MODEL_TYPE))
                .thumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(luckCollection.getCollectionId(),COLLECTION_MODEL_TYPE))
                .threeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(luckCollection.getCollectionId(),COLLECTION_MODEL_TYPE))
                .build();
    }

    @Override
    public List<String> queryDict(String keyword) {
        return  list(Wrappers.<Box>lambdaQuery().select(Box::getBoxTitle).like(Box::getBoxTitle,keyword).orderByDesc(Box::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getBoxTitle()).collect(Collectors.toList());
    }

    @Override
    public String tarBox(String id, String userId) {
        return cntTarService.tarBox(getById(id),userId);
    }

    /**
     * 根据 组件 规则 获取到幸运藏品,进行绑定用户
     * @param boxCollections
     * @return
     */
    private  BoxCollection luckGetCollection(List<BoxCollection> boxCollections) {
        // 在这里开始斟酌, boxCollections 数据阶段已经过滤掉 没有库存数量的藏品了。
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

    private void commCheckSell(Box box,String userId){
        // 校验盲盒主体是否存在
        Assert.isTrue(Objects.nonNull(box),"盲盒编号有误,请核实!");

        // 校验是否库存不够了
        Assert.isFalse(Integer.valueOf(0).equals(box.getBalance()),"库存不足了!");
        // 重复校验状态
        Assert.isTrue(BoxStatus.UP_ACTION.getCode().equals(box.getStatusBy()),"您来晚了,售罄了!");

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
    private void tarCheckBox(Box box,String userId){
        // 是否需要抽？
        if (StrUtil.isNotBlank(box.getTarId())){
            if (!CEN_YES_TAR.getCode().equals(tarService.tarStatus(userId,box.getId())))
                Assert.isFalse(Boolean.TRUE,"未参与抽签,或暂未购买资格!");
            Assert.isFalse(tarService.isSell(userId,box.getId()),"不可复购,只可购买一次!");

        }
    }

    /**
     * 提前购检测 发售时间检测
     * @param
     * @param userId
     */
    private void postCheckBox(Box box,String userId){
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
        if (publishTimeFlag)Assert.isTrue(LocalDateTime.now().compareTo(box.getPublishTime()) >= 0,"发行时间未到,请核实!");


    }
    private void checkSell(Box box,String userId,Integer payType,BigDecimal realPayMoney) {
         commCheckSell(box, userId);
         moneyCheckCollection(userId, payType, realPayMoney);
         tarCheckBox(box,userId);
         postCheckBox(box, userId);
         realCheckCollection(userId);
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
        // 缓存库存数据隔离
        BuiCronDto typeBalanceCache = buiCronService.getTypeBalanceCache(BOX_MODEL_TYPE, box.getId());
        boxListVo.setBalance(typeBalanceCache.getBalance());
        boxListVo.setSelfBalance(typeBalanceCache.getSelfBalance());
        if (box.getPublishTime().isAfter(LocalDateTime.now())) {
            boxListVo.setPreStatus(1);
        } else {
            boxListVo.setPreStatus(2);
        }
        if (box.getBalance().equals(0)){
            box.setStatusBy(2);
        }
        // 需要集成图片服务
        boxListVo.setMediaVos(mediaService.initMediaVos(box.getId(), BOX_MODEL_TYPE));
        boxListVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(box.getId(), BOX_MODEL_TYPE));
        boxListVo.setThreeDimensionalMediaVos(mediaService.threeDimensionalMediaVos(box.getId(), BOX_MODEL_TYPE));
        return boxListVo;

    }

}
