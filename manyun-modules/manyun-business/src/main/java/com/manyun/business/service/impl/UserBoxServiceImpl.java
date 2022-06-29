package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.dto.StepDto;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.UserBox;
import com.manyun.business.domain.vo.UserBoxVo;
import com.manyun.business.mapper.BoxMapper;
import com.manyun.business.mapper.UserBoxMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IStepService;
import com.manyun.business.service.IUserBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.enums.BoxOpenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
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

    @Resource
    private UserBoxMapper userBoxMapper;

    @Resource
    private BoxMapper boxMapper;


    @Autowired
    private IStepService stepService;


    /**
     * 绑定盲盒子信息,新增盲盒子日志
     */

    @Override
    public void bindPleaseBoxDto(OpenPleaseBoxDto pleaseBoxDto){
        bindBox(pleaseBoxDto.getUserId(), pleaseBoxDto.getBoxId(), pleaseBoxDto.getSourceInfo(), pleaseBoxDto.getGoodsNum());
    }



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
            userBox.setBoxOpen(BoxOpenType.NO_OPEN.getCode());
            userBoxList.add(userBox);
            //save(userBox);
        }
        saveBatch(userBoxList);

        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(sourceInfo).buiId(userId).modelType(BOX_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
    }

    @Override
    public List<UserBoxVo> pageUserBox(String userId) {
        return userBoxMapper.pageUserBox(userId);
    }


    /**
     * 用户是否拥有这个盲盒
     * @param userId
     * @param id
     * @return true 有此盲盒子  反之 ~~~
     */
    @Override
    public Boolean existUserBox(String userId, String id){
        return Objects.nonNull(getOne(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getUserId,userId).eq(UserBox::getBoxId,id).eq(UserBox::getBoxOpen,BoxOpenType.NO_OPEN.getCode())));
    }


    /**
     *
     * @param tranUserId 转让方的用户编号
     * @param toUserId   收方的用户编号
     * @param buiId
     */
    @Override
    public void tranBox(String tranUserId, String toUserId, String buiId) {
        UserBox userBox = getById(buiId);
        Box box = boxMapper.selectById(userBox.getBoxId());
        String format = StrUtil.format("{}:用户赠送获得!",box.getBoxTitle());
        String formatTran = StrUtil.format("{}:盲盒被赠送!",box.getBoxTitle());
        boolean rows = update(Wrappers.<UserBox>lambdaUpdate()
                .set(UserBox::getUserId, toUserId)
                .set(UserBox::getBoxOpen, BoxOpenType.NO_OPEN.getCode())
                .set(UserBox::getSourceInfo, format)
                .set(UserBox::getUpdatedBy, toUserId)
                .set(UserBox::getUpdatedTime, LocalDateTime.now())
                .eq(UserBox::getId, buiId).eq(UserBox::getUserId, tranUserId));
        Assert.isTrue(rows ,"转让有误,请重试!");
        // 日志....
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(format).buiId(toUserId).modelType(BOX_MODEL_TYPE).isType(PULL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
        ,LogInfoDto.builder().jsonTxt(formatTran).buiId(tranUserId).modelType(BOX_MODEL_TYPE).isType(POLL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
        );

        // 流转记录
      stepService.saveBatch(StepDto.builder().buiId(buiId).userId(tranUserId).modelType(BOX_MODEL_TYPE).reMark("转让方").formInfo(formatTran).build()
      ,StepDto.builder().buiId(buiId).userId(toUserId).modelType(BOX_MODEL_TYPE).reMark("受让方").formInfo(format).build()
      );

    }
}
