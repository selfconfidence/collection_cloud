package com.manyun.business.mapper;

import com.manyun.business.domain.entity.Box;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 盲盒;盲盒主体表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface BoxMapper extends BaseMapper<Box> {

    int updateLock(@Param("id") String id,@Param("sellNum") Integer sellNum);
}
