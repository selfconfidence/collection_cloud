package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.AuctionPrice;
import com.manyun.business.domain.entity.AuctionSend;
import com.manyun.business.domain.form.AuctionPriceForm;
import com.manyun.business.domain.query.AuctionPriceQuery;
import com.manyun.business.domain.vo.AuctionPriceVo;
import com.manyun.business.mapper.AuctionPriceMapper;
import com.manyun.business.service.IAuctionPriceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IAuctionSendService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.AuctionStatus;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    IAuctionSendService sendService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Override
    public synchronized R myAuctionPrice(AuctionPriceForm auctionPriceForm) {
        LoginBusinessUser businessUser = SecurityUtils.getTestLoginBusinessUser();
        AuctionSend auctionSend = sendService.getById(auctionPriceForm.getAuctionSendId());
        //判断竞拍状态（竞拍中才可出价）
        if (!auctionSend.getAuctionStatus().equals(AuctionStatus.BID_BIDING.getCode())) {
            return R.fail("当前竞品不可出价");
        }
        List<AuctionPrice> list = this.list(Wrappers.<AuctionPrice>lambdaQuery()
                .eq(AuctionPrice::getAuctionSendId, auctionPriceForm.getAuctionSendId()).orderByDesc(AuctionPrice::getBidPrice));
        AuctionPrice auctionPrice = new AuctionPrice();
        if (list.isEmpty()) {
            auctionPrice.setBidPrice(auctionSend.getStartPrice().add(BigDecimal.TEN));
        }
        if (list.size() > 0) {
            auctionPrice.setBidPrice(list.get(0).getBidPrice().add(BigDecimal.TEN));
        }
        auctionPrice.setUserId(businessUser.getUserId());
        auctionPrice.setAuctionSendId(auctionPriceForm.getAuctionSendId());
        auctionPrice.setUserName(businessUser.getUsername());
        auctionPrice.setCreatedTime(LocalDateTime.now());
        this.save(auctionPrice);

        sendService.update(Wrappers.<AuctionSend>lambdaUpdate()
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

}
