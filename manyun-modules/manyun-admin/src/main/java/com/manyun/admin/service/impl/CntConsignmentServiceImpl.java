package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.OrderInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.*;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.domain.vo.CollectionBusinessVo;
import com.manyun.admin.domain.vo.CollectionSalesStatisticsVo;
import com.manyun.admin.service.*;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntConsignmentMapper;
import org.springframework.transaction.annotation.Transactional;

import static com.manyun.common.core.enums.ConsignmentStatus.PUSH_CONSIGN;

/**
 * 寄售市场主_寄售订单Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntConsignmentServiceImpl extends ServiceImpl<CntConsignmentMapper, CntConsignment> implements ICntConsignmentService
{
    @Autowired
    private CntConsignmentMapper cntConsignmentMapper;

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private ICntUserBoxService userBoxService;

    @Autowired
    private ICntOrderService orderService;

    @Autowired
    private ICntMediaService mediaService;

    /**
     * 订单管理列表
     *
     */
    @Override
    public TableDataInfo<CntOrderVo> orderList(OrderListQuery orderListQuery) {
        PageHelper.startPage(orderListQuery.getPageNum(),orderListQuery.getPageSize());
        List<CntOrderVo> cntOrderVos = orderService.orderList(orderListQuery);
        List<CntUserCollection> userCollections = userCollectionService.list();
        return TableDataInfoUtil.pageTableDataInfo(cntOrderVos.parallelStream().map(m->{
            m.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(m.getBuiId(),m.getGoodsType()==0? BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            if(m.getGoodsType() == 0){
                Optional<CntUserCollection> optional = userCollections.parallelStream().filter(ff -> ff.getId().equals(m.getUserBuiId())).findFirst();
                if(optional.isPresent()){
                    m.setCollectionHash(optional.get().getCollectionHash());
                }
            }
            return m;
        }).collect(Collectors.toList()),cntOrderVos);
    }

    /**
     * 订单列表详情
     *
     */
    @Override
    public R<CntOrderVo> orderInfo(OrderInfoDto orderInfoDto) {
        CntOrderVo cntOrderVo = orderService.orderInfo(orderInfoDto);
        List<CntUserCollection> userCollections = userCollectionService.list();
        cntOrderVo.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(cntOrderVo.getBuiId(),cntOrderVo.getGoodsType()==0? BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
        if(cntOrderVo.getGoodsType() == 0){
            Optional<CntUserCollection> optional = userCollections.parallelStream().filter(ff -> ff.getId().equals(cntOrderVo.getUserBuiId())).findFirst();
            if(optional.isPresent()){
                cntOrderVo.setCollectionHash(optional.get().getCollectionHash());
            }
        }
        return R.ok(cntOrderVo);
    }

    /**
     * 藏品订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单
     */
    @Override
    public TableDataInfo<CntConsignmentVo> collectionList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());
        List<CntConsignmentVo> cntConsignmentVos = cntConsignmentMapper.selectCollectionOrderList(consignmentQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntConsignmentVos, cntConsignmentVos);
    }

    /**
     * 盲盒订单管理列表
     *
     * @param consignmentQuery
     * @return 寄售市场主_寄售订单
     */
    @Override
    public TableDataInfo<CntConsignmentVo> boxList(ConsignmentQuery consignmentQuery) {
        PageHelper.startPage(consignmentQuery.getPageNum(),consignmentQuery.getPageSize());
        List<CntConsignmentVo> cntConsignmentVos = cntConsignmentMapper.selectBoxOrderList(consignmentQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntConsignmentVos,cntConsignmentVos);
    }

    /**
     * 获取藏品订单管理详细信息
     * @param consignmentInfoDto
     * @return
     */
    @Override
    public CntConsignmentVo selectConsignmentOrderById(ConsignmentInfoDto consignmentInfoDto) {
        CntConsignmentVo cntConsignmentVo = cntConsignmentMapper.selectConsignmentOrderById(consignmentInfoDto);
        List<CntUserCollection> userCollections = userCollectionService.list();
        if(cntConsignmentVo.getIsType()==0){
            Optional<CntUserCollection> optional = userCollections.parallelStream().filter(f -> f.getId().equals(cntConsignmentVo.getBuiId())).findFirst();
            if(optional.isPresent()){
                cntConsignmentVo.setCollectionHash(optional.get().getCollectionHash());
            }
        }
        return cntConsignmentVo;
    }

    /**
     * 修改寄售状态
     * @param consignmentStatusQuery
     * @return
     */
    @Override
    public int consignmentStatus(ConsignmentStatusQuery consignmentStatusQuery) {
        CntConsignment consignment = getById(consignmentStatusQuery.getId());
        consignment.setConsignmentStatus(consignmentStatusQuery.getStatus()==1?2:1);
        consignment.setUpdatedBy(SecurityUtils.getUsername());
        consignment.setUpdatedTime(DateUtils.getNowDate());
        return updateById(consignment)==true?1:0;
    }

    /**
     * 规定时间藏品交易量及交易金额查询
     * @param collectionBusinessQuery
     * @return
     */
    @Override
    public List<CollectionBusinessVo> selectByTimeZones(CollectionBusinessQuery collectionBusinessQuery) {
        return cntConsignmentMapper.selectByTimeZones(collectionBusinessQuery);
    }

    /**
     * 规定时间指定藏品买卖查询
     * @param collectionSalesStatisticsQuery
     * @return
     */
    @Override
    public List<CollectionSalesStatisticsVo> selectCollectionSalesStatistics(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery) {
        return cntConsignmentMapper.selectCollectionSalesStatistics(collectionSalesStatisticsQuery);
    }

    /**
     * 根据寄售id寄售方取消寄售市场中的资产
     * @param consignmentOrderQuery
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelConsignmentById(CancelConsignmentOrderQuery consignmentOrderQuery) {
        CntConsignment consignment = getOne(
                Wrappers
                        .<CntConsignment>lambdaQuery()
                        .eq(CntConsignment::getId, consignmentOrderQuery.getId())
                        .eq(CntConsignment::getSendUserId, consignmentOrderQuery.getUserId())
                        .eq(CntConsignment::getConsignmentStatus, PUSH_CONSIGN.getCode()));
        // 判定条件是否符合
        Assert.isTrue(Objects.nonNull(consignment),"取消寄售资产有误,请核实当前寄售状态!");
        // 1. 回滚到 用户资产中间表
        Integer isType = consignment.getIsType();
        //验证是 藏品信息 还是 盲盒信息
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(isType))
            //藏品
            userCollectionService.showUserCollection(consignment.getSendUserId(),consignment.getBuiId(), StrUtil.format("寄售的藏品,已被退回!"));
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(isType))
            // 盲盒
            userBoxService.showUserBox(consignment.getBuiId(),consignment.getSendUserId(),StrUtil.format("寄售的盲盒,已被退回!"));

        // 2. 删除当前寄售订单
        removeById(consignment.getId());
    }

}
