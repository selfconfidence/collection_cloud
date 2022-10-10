package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.CntBoxOrderVo;
import org.apache.ibatis.annotations.Param;

/**
 * 盲盒;盲盒主体Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntBoxMapper extends BaseMapper<CntBox>
{

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param boxQuery
     * @return 盲盒;盲盒主体集合
     */
    public List<CntBox> selectSearchBoxList(BoxQuery boxQuery);

    /**
     * 查询盲盒订单列表
     */
    List<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery);

    int updateLock(@Param("id") String id,@Param("sellNum") Integer sellNum,@Param("airdopSellNum") Integer airdopSellNum);
}
