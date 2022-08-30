package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntConsignment;
import com.manyun.admin.domain.dto.ConsignmentInfoDto;
import com.manyun.admin.domain.dto.PaymentReviewDto;
import com.manyun.admin.domain.query.ConsignmentQuery;
import com.manyun.admin.domain.vo.CntConsignmentVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
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
        return cntConsignmentMapper.selectConsignmentOrderById(consignmentInfoDto);
    }

}
