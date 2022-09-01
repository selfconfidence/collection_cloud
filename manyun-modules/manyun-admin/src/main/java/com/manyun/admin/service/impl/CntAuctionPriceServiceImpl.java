package com.manyun.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntAuctionPriceMapper;
import com.manyun.admin.domain.CntAuctionPrice;
import com.manyun.admin.service.ICntAuctionPriceService;

/**
 * 竞价Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-01
 */
@Service
public class CntAuctionPriceServiceImpl extends ServiceImpl<CntAuctionPriceMapper,CntAuctionPrice> implements ICntAuctionPriceService
{
    @Autowired
    private CntAuctionPriceMapper cntAuctionPriceMapper;

}
