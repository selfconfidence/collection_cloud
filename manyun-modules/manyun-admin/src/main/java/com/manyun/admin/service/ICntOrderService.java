package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.vo.CntOrderVo;

import java.util.List;

/**
 * 订单Service接口
 *
 * @author yanwei
 * @date 2022-07-26
 */
public interface ICntOrderService extends IService<CntOrder>
{

    /**
     * 我的订单
     */
    List<CntOrderVo> myOrderList(String userId);

}
