package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntOrder;
import com.manyun.admin.domain.vo.CntOrderVo;

/**
 * 订单Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntOrderMapper extends BaseMapper<CntOrder>
{

    /**
     * 我的订单
     */
    List<CntOrderVo> myOrderList(String userId);

}
