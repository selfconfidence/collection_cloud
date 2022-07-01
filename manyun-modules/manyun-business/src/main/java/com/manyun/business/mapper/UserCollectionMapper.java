package com.manyun.business.mapper;

import com.manyun.business.domain.dto.UserCollectionCountDto;
import com.manyun.business.domain.entity.UserCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.business.domain.vo.UserCollectionVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户购买藏品中间表 Mapper 接口
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface UserCollectionMapper extends BaseMapper<UserCollection> {

    List<UserCollectionVo> userCollectionPageList(@Param("userId") String userId);

    List<UserCollectionCountDto> selectUserCollectionCount(@Param("collectionIds")List<String> collectionIds, @Param("userId")String userId);

    void deleteMaterial(@Param("userId")String userId,@Param("collectionId")String collectionId,@Param("deleteNum")Integer deleteNum);

}
