package com.manyun.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.entity.CntUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface CntUserMapper extends BaseMapper<CntUser> {

    CntUser commUni(@Param("commUni") String commUni);
}
