package com.manyun.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.dto.ConsignmentOpenDto;
import com.manyun.business.domain.entity.CntConsignment;

import java.util.List;

/**
 * <p>
 * 寄售市场主表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-30
 */
public interface CntConsignmentMapper extends BaseMapper<CntConsignment> {

    List<ConsignmentOpenDto> openConsignmentList();

}
