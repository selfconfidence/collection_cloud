package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntCustomerService;
import org.apache.ibatis.annotations.Param;

/**
 * 客服Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntCustomerServiceMapper extends BaseMapper<CntCustomerService>
{

    /**
     * 查询客服列表
     *
     * @param cntCustomerService 客服
     * @return 客服集合
     */
    public List<CntCustomerService> selectCntCustomerServiceList(CntCustomerService cntCustomerService);

}
