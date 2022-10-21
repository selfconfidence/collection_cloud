package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntUserBox;
import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.vo.GoodsVo;
import com.manyun.admin.domain.vo.UserBoxVo;

import java.util.List;

/**
 * 用户购买盲盒中间Service接口
 *
 * @author yanwei
 * @date 2022-08-05
 */
public interface ICntUserBoxService extends IService<CntUserBox>
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
    List<String> selectMeetTheConditionsUserIds(String goodId, Integer count);

    /**
     * 查询满足条件的数据
     * @return
     */
    List<GoodsVo> selectMeetTheConditionsData(String userId,List<String> goodIds);

    String showUserBox(String buiId, String userId, String info);

}
