package com.manyun.business.mapper;

import com.manyun.business.domain.entity.UserBox;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.vo.UserBoxVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户购买盲盒中间表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface UserBoxMapper extends BaseMapper<UserBox> {

    List<UserBoxVo> pageUserBox(@Param("userId") String userId);
}
