package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;

import com.manyun.admin.domain.dto.MyBoxDto;
import com.manyun.admin.domain.vo.GoodsVo;
import com.manyun.admin.domain.vo.UserBoxVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntUserBoxMapper;
import com.manyun.admin.domain.CntUserBox;
import com.manyun.admin.service.ICntUserBoxService;

/**
 * 用户购买盲盒中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-05
 */
@Service
public class CntUserBoxServiceImpl extends ServiceImpl<CntUserBoxMapper,CntUserBox> implements ICntUserBoxService
{
    @Autowired
    private CntUserBoxMapper cntUserBoxMapper;

    /**
     * 我的盲盒
     * @param userId
     * @return
     */
    @Override
    public List<UserBoxVo> myBoxList(String userId) {
        return cntUserBoxMapper.myBoxList(userId);
    }

    /**
     * 查询满足条件的用户ids
     * @param goodId
     * @param count
     * @return
     */
    @Override
    public List<String> selectMeetTheConditionsUserIds(String goodId, Integer count) {
        return cntUserBoxMapper.selectMeetTheConditionsUserIds(goodId,count);
    }

    /**
     * 查询满足条件的数据
     * @return
     */
    @Override
    public List<GoodsVo> selectMeetTheConditionsData(String userId,List<String> goodIds) {
        return cntUserBoxMapper.selectMeetTheConditionsData(userId,goodIds);
    }
}
