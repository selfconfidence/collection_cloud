package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.manyun.business.design.pay.RootPay;
import com.manyun.business.domain.dto.OrderCreateDto;
import com.manyun.business.domain.dto.PayInfoDto;
import com.manyun.business.domain.entity.*;
import com.manyun.business.domain.form.CollectionSellForm;
import com.manyun.business.domain.query.CollectionQuery;
import com.manyun.business.domain.vo.*;
import com.manyun.business.mapper.*;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.BoxStatus;
import com.manyun.common.core.enums.CollectionStatus;
import com.manyun.common.core.web.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.enums.AliPayEnum.BOX_ALI_PAY;
import static com.manyun.common.core.enums.CateType.COLLECTION_CATE;
import static com.manyun.common.core.enums.CollectionStatus.DOWN_ACTION;
import static com.manyun.common.core.enums.PayTypeEnum.MONEY_TAPE;
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


    private final IMediaService mediaService;

    private final CollectionLableMapper collectionLableMapper;

    private final CollectionInfoMapper collectionInfoMapper;

    private final IMoneyService moneyService;

    private final IOrderService orderService;

    private final RootPay rootPay;


    @Override
    public List<CollectionVo> pageQueryList(CollectionQuery collectionQuery) {
        // 查询条件部分
        List<CntCollection> CntCollections = list(Wrappers.<CntCollection>lambdaQuery()
                .eq(StrUtil.isNotBlank(collectionQuery.getCateId()), CntCollection::getCateId, collectionQuery.getCateId())
                .ne(CntCollection::getStatusBy,DOWN_ACTION.getCode())
                .like(StrUtil.isNotBlank(collectionQuery.getCollectionName()), CntCollection::getCollectionName, collectionQuery.getCollectionName())
                .eq(StrUtil.isNotBlank(collectionQuery.getBindCreationId()), CntCollection::getBindCreation, collectionQuery.getBindCreationId())
                .orderByDesc(CntCollection::getCreatedTime)
        );
        // 聚合数据
        return CntCollections.parallelStream().map(this::providerCollectionVo).collect(Collectors.toList());
    }

    @Override
    public CollectionAllVo info(String id) {
        return Builder.of(CollectionAllVo::new).with(CollectionAllVo::setCollectionVo,providerCollectionVo(getById(id))).with(CollectionAllVo::setCollectionInfoVo,providerCollectionInfoVo(id)).build();
    }

    /**
     * 购买藏品
     * @param userId
     * @param collectionSellForm
     * @return
     */
    @Override
    public PayVo sellCollection(String userId, CollectionSellForm collectionSellForm) {
        // 总结校验 —— 支付方式
        CntCollection  cntCollection = getById(collectionSellForm.getCollectionId());

        // 实际需要支付的金额
        BigDecimal realPayMoney = NumberUtil.mul(collectionSellForm.getSellNum(), cntCollection.getRealPrice());
        checkCollection(cntCollection,userId,collectionSellForm.getPayType(),realPayMoney);
        // 锁优化
        int rows = cntCollectionMapper.updateLock(cntCollection.getId(),collectionSellForm.getSellNum());
        Assert.isTrue(rows>=1,"您来晚了!");

        // 根据支付方式创建订单  通用适配方案 余额直接 减扣操作
        //1. 先创建对应的订单
        String outHost =   orderService.createOrder(OrderCreateDto.builder()
                .orderAmount(realPayMoney)
                .buiId(cntCollection.getId())
                .payType(collectionSellForm.getPayType())
                .goodsType(BusinessConstants.ModelTypeConstant.BOX_TAYPE)
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
        return payVo;
    }

    /**
     * 分页查询用户的 所有藏品信息
     * @param pageQuery
     * @param userId
     * @return
     */
    @Override
    public List<UserCollectionVo> userCollectionPageList(PageQuery pageQuery, String userId) {
        PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
        List<UserCollectionVo> userCollectionVos =  userCollectionService.userCollectionPageList(userId);
        // 组合数据
        return userCollectionVos.parallelStream().map(item -> {item.setMediaVos(mediaService.initMediaVos(item.getCollectionId(),COLLECTION_MODEL_TYPE)); return item;}).collect(Collectors.toList());
    }

    /**
     * 查询用户下 所有藏品 组系列
     * @param userId
     * @return
     */
    @Override
    public List<UserCateVo> cateCollectionByUserId(String userId) {
        List<UserCateVo> userCateVoList = Lists.newLinkedList();

        List<UserCollection> userCollections = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId));
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
        cateLists.forEach((cateId,cntCollections)->{
            Cate cate = cateMap.get(cateId);
            UserCateVo userCateVo = Builder.of(UserCateVo::new).build();
            BeanUtil.copyProperties(cate,userCateVo,"userCateCollectionVos");
            userCateVo.setUserCateCollectionVos(cntCollections.parallelStream().map(this::initUserCateCollectionVo).collect(Collectors.toList()));
            userCateVoList.add(userCateVo);
        });
        return userCateVoList;
    }

    @Override
    public List<String> queryDict(String keyword) {
        return  list(Wrappers.<CntCollection>lambdaQuery().select(CntCollection::getCollectionName).like(CntCollection::getCollectionName,keyword).orderByDesc(CntCollection::getCreatedTime).last(" limit 10")).parallelStream().map(item -> item.getCollectionName()).collect(Collectors.toList());
    }

    private UserCateCollectionVo initUserCateCollectionVo(CntCollection cntCollection) {
        UserCateCollectionVo userCateCollectionVo = Builder.of(UserCateCollectionVo::new).build();
        userCateCollectionVo.setCollectionName(cntCollection.getCollectionName());
        userCateCollectionVo.setId(cntCollection.getId());
        userCateCollectionVo.setMediaVos(mediaService.initMediaVos(cntCollection.getId(),COLLECTION_MODEL_TYPE));
        return userCateCollectionVo;
    }


    private void checkCollection(CntCollection cntCollection,String userId,Integer payType,BigDecimal realPayMoney) {
        // 校验盲盒主体是否存在
        Assert.isTrue(Objects.nonNull(cntCollection),"藏品编号有误,请核实!");
        // 校验盲盒是否到了发行时间
        Assert.isTrue(LocalDateTime.now().compareTo(cntCollection.getPublishTime()) >= 0,"发行时间未到,请核实!");
        // 校验是否库存不够了
        Assert.isFalse(Integer.valueOf(0).equals(cntCollection.getBalance()),"库存不足了!");
        // 重复校验状态
        Assert.isTrue(CollectionStatus.UP_ACTION.getCode().equals(cntCollection.getStatusBy()),"您来晚了,售罄了!");

        //是否有未支付订单
        List<Order> orders = orderService.checkUnpaidOrder(userId);
        Assert.isFalse(orders.size() > 0 ,"您有未支付订单，暂不可购买");

        // 如果是余额支付,需要验证下是否充足
        if (MONEY_TAPE.getCode().equals(payType)) {
            Money money = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
            Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney) >=0,"余额不足,请核实!");
        }

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

    private CollectionVo providerCollectionVo(CntCollection CntCollection){
        CollectionVo collectionVo = Builder.of(CollectionVo::new).build();
        BeanUtil.copyProperties(CntCollection,collectionVo);
        collectionVo.setLableVos(initLableVos(CntCollection.getId()));
        collectionVo.setMediaVos(initMediaVos(CntCollection.getId()));
        collectionVo.setCnfCreationdVo(initCnfCreationVo(CntCollection.getBindCreation()));
        collectionVo.setCateVo(initCateVo(CntCollection.getCateId()));
        return collectionVo;
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
