package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserBox;
import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.vo.GoodsVo;
import com.manyun.admin.domain.vo.UserBoxVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户购买盲盒中间Mapper接口
 *
 * @author yanwei
 * @date 2022-08-05
 */
public interface CntUserBoxMapper extends BaseMapper<CntUserBox>
{

    /**
     * 我的盲盒
     * @param userId
     * @return
     */
    List<UserBoxVo> myBoxList(String userId);

    /**
     * 查询满足条件的用户ids
     * @param goodId
     * @param count
     * @return
     */
    List<String> selectMeetTheConditionsUserIds(@Param("goodId") String goodId,@Param("count") Integer count);

    /**
     * 查询满足条件的数据
     * @return
     */
    List<GoodsVo> selectMeetTheConditionsData(@Param("userId") String userId,@Param("goodIds")List<String> goodIds);
}
