package com.manyun.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntAuctionPrice;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.query.AuctionOrderQuery;
import com.manyun.admin.domain.query.AuctionPriceQuery;
import com.manyun.admin.domain.vo.AuctionPriceVo;
import com.manyun.admin.domain.vo.CntAuctionOrderVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.service.ICntAuctionPriceService;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.admin.service.ICntUserService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.PageUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntAuctionOrderMapper;
import com.manyun.admin.domain.CntAuctionOrder;
import com.manyun.admin.service.ICntAuctionOrderService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 拍卖订单Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-01
 */
@Service
public class CntAuctionOrderServiceImpl extends ServiceImpl<CntAuctionOrderMapper,CntAuctionOrder> implements ICntAuctionOrderService
{
    @Autowired
    private CntAuctionOrderMapper cntAuctionOrderMapper;

    @Autowired
    private ICntAuctionPriceService auctionPriceService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntUserService userService;

    /**
     * 查询拍卖订单列表
     *
     * @param auctionOrderQuery
     * @return 拍卖订单
     */
    @Override
    public TableDataInfo<CntAuctionOrderVo> selectCntAuctionOrderList(AuctionOrderQuery auctionOrderQuery)
    {
        PageHelper.startPage(auctionOrderQuery.getPageNum(),auctionOrderQuery.getPageSize());
        List<CntAuctionOrderVo> cntAuctionOrderVos = cntAuctionOrderMapper.selectCntAuctionOrderList(auctionOrderQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntAuctionOrderVos.parallelStream().map(m->{
            m.setThumbnailImgMediaVos(mediaService.thumbnailImgMediaVos(m.getGoodsId(), m.getGoodsType()==1?BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            return m;
        }).collect(Collectors.toList()), cntAuctionOrderVos);
    }

    /**
     *  竞价列表
     * @param auctionPriceQuery
     * @return
     */
    @Override
    public TableDataInfo<AuctionPriceVo> auctionPriceList(AuctionPriceQuery auctionPriceQuery) {
        PageUtils.startPage(auctionPriceQuery.getPageNum(), auctionPriceQuery.getPageSize());
        List<CntAuctionPrice> priceList = auctionPriceService.list(Wrappers.<CntAuctionPrice>lambdaQuery().eq(CntAuctionPrice::getAuctionSendId, auctionPriceQuery.getAuctionSendId())
                .orderByDesc(CntAuctionPrice::getBidPrice));
        return TableDataInfoUtil.pageTableDataInfo(priceList.parallelStream().map(this::providerAuctionPriceVo).collect(Collectors.toList()), priceList);
    }

    private AuctionPriceVo providerAuctionPriceVo(CntAuctionPrice auctionPrice) {
        CntUser user = userService.getById(auctionPrice.getUserId());
        AuctionPriceVo auctionPriceVo = Builder.of(AuctionPriceVo::new).build();
        BeanUtil.copyProperties(auctionPrice, auctionPriceVo);
        auctionPriceVo.setHeadImage(user==null?"":user.getHeadImage());
        return auctionPriceVo;
    }

}
