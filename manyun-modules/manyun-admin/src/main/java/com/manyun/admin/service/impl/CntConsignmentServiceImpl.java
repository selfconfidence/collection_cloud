package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.OrderInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.query.ConsignmentStatusQuery;
import com.manyun.admin.domain.query.OrderListQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.admin.domain.vo.CntOrderVo;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.admin.service.ICntOrderService;
import com.manyun.admin.service.ICntUserCollectionService;
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
import com.manyun.admin.service.ICntConsignmentService;

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
        consignment.setConsignmentStatus(2);
        consignment.setUpdatedBy(SecurityUtils.getUsername());
        consignment.setUpdatedTime(DateUtils.getNowDate());
        return updateById(consignment)==true?1:0;
    }

}
