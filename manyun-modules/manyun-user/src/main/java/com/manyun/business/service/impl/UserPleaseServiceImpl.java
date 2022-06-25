package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.domain.entity.PleaseBox;
import com.manyun.business.domain.entity.UserPlease;
import com.manyun.business.domain.vo.UserPleaseBoxVo;
import com.manyun.business.mapper.PleaseBoxMapper;
import com.manyun.business.mapper.UserPleaseMapper;
import com.manyun.business.service.IPleaseBoxService;
import com.manyun.business.service.IUserPleaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBoxService;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.CodeStatus;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.exception.ServiceException;
import com.manyun.common.core.text.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.PleaseBoxProcessStatus.*;
import static com.manyun.common.core.enums.PleaseBoxStatus.OK_USE;

/**
 * <p>
 * 用户邀请奖励历程 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-24
 */
@Service
public class UserPleaseServiceImpl extends ServiceImpl<UserPleaseMapper, UserPlease> implements IUserPleaseService {

    @Resource
    private PleaseBoxMapper pleaseBoxMapper;

    @Autowired
    private RemoteBoxService remoteBoxService;



    /**
     * 根据用户编号 以及推广人数 绘制 规则列表
     * @param userId
     * @param userRealCount
     * @return
     */
    @Override
    public List<UserPleaseBoxVo> userPleaseBoxVo(String userId, long userRealCount) {
        Integer userIntRealCount = Convert.toInt(userRealCount);
        LinkedList<UserPleaseBoxVo> arrayList = Lists.newLinkedList();
        List<UserPlease> userPleases = list(Wrappers.<UserPlease>lambdaQuery().eq(UserPlease::getUserId, userId));
        Map<String, UserPlease> pleaseMap = userPleases.stream().collect(Collectors.toMap(UserPlease::getPleaseId, Function.identity()));
        List<PleaseBox> pleaseBoxes = pleaseBoxMapper.selectList(Wrappers.<PleaseBox>lambdaQuery().eq(PleaseBox::getIsUse,OK_USE.getCode()).orderByAsc(PleaseBox::getPleaseNumber));
        // 得到所有规则信息
        if (pleaseBoxes.isEmpty())return arrayList;
        // 填充基础数据
        for (PleaseBox pleaseBox : pleaseBoxes) {
            // 前置数据填充完善
            UserPleaseBoxVo pleaseBoxVo = Builder.of(UserPleaseBoxVo::new).build();
            pleaseBoxVo.setId(pleaseBox.getId());
            pleaseBoxVo.setBoxName(pleaseBoxVo.getBoxName());
            pleaseBoxVo.setBlane(pleaseBox.getBalance());
            pleaseBoxVo.setPleaseNumber(pleaseBox.getPleaseNumber());
            pleaseBoxVo.setIsProcess(OK_PRO.getCode());
            // 判定状态
            String pleaseId = pleaseBox.getId();
            // 是否是空
            UserPlease userPlease = pleaseMap.get(pleaseId);
            // 1. 如果是空,就代表 还没有领取
            // 1.1 判定是否满足条件  -修改状态
            if (Objects.isNull(userPlease)){
                  if (userIntRealCount.compareTo(pleaseBox.getPleaseNumber()) >= 0 ){
                      // 可以领取
                      pleaseBoxVo.setIsProcess(WAIT_PRO.getCode());
                  }else{
                      // 不可以领取
                      pleaseBoxVo.setIsProcess(NO_PRO.getCode());
                  }
            }
            arrayList.addLast(pleaseBoxVo);
        }

        return arrayList;
    }


    /**
     * 用户领取邀请奖励
     * @param userRealCount
     * @param pleaseId
     * @return
     */
    @Override
    public String openPleaseBox(long userRealCount, String pleaseId,String userId) {
        Integer userRealIntCount = Convert.toInt(userRealCount);
        PleaseBox pleaseBox = pleaseBoxMapper.selectById(pleaseId);
        Assert.isTrue(Objects.nonNull(pleaseBox),"此领取编号有误!");
        Assert.isTrue(userRealIntCount.compareTo(pleaseBox.getPleaseNumber()) >=0,"推荐实名人数不够,请核实!");
        // 开始领取
        UserPlease userPlease = Builder.of(UserPlease::new).build();
        userPlease.setId(IdUtil.getSnowflake().nextIdStr());
        userPlease.setPleaseId(pleaseId);
        userPlease.setIsProcess(OK_PRO.getCode());
        userPlease.createD(userId);
        userPlease.setUserId(userId);
        save(userPlease);
        // 进行绑定
        String source = StrUtil.format("推荐人数满{}领取获得盲盒", userRealCount);
        R<String> stringR = remoteBoxService.openPleaseBox(OpenPleaseBoxDto.builder().goodsNum(Integer.valueOf(1)).boxId(pleaseBox.getBoxId()).sourceInfo(source).userId(userId).build());
        if (CodeStatus.SUCCESS.getCode().equals(stringR.getCode()))
            return source;
        throw new ServiceException(stringR.getMsg());

    }
}
