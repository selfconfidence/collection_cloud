package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserBox;
import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.vo.UserBoxVo;

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

}
