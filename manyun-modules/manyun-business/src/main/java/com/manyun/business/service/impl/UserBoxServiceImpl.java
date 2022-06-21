package com.manyun.business.service.impl;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.UserBox;
import com.manyun.business.mapper.UserBoxMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IUserBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;

/**
 * <p>
 * 用户购买盲盒中间表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@Service
public class UserBoxServiceImpl extends ServiceImpl<UserBoxMapper, UserBox> implements IUserBoxService {


    @Autowired
    private ILogsService logsService;

    /**
     * 绑定盲盒信息, 新增盲盒日志
     * @param userId
     * @param buiId
     * @param sourceInfo
     * @param goodsNum 数量
     */
    @Override
    public void bindBox(String userId, String buiId, String sourceInfo, Integer goodsNum) {
        ArrayList<UserBox> userBoxList = Lists.newArrayList();
        for (Integer i = 0; i < goodsNum; i++) {
            UserBox userBox = Builder.of(UserBox::new).build();
            userBox.setUserId(userId);
            userBox.setBoxId(buiId);
            userBox.setId(IdUtil.getSnowflake().nextIdStr());
            userBox.createD(userId);
            userBox.setSourceInfo(sourceInfo);
            userBoxList.add(userBox);
            //save(userBox);
        }
        saveBatch(userBoxList);

        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(sourceInfo).buiId(userId).modelType(BOX_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
    }
}
