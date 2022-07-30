package com.manyun.business.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.CntTar;
import com.manyun.business.domain.entity.CntUserTar;
import com.manyun.business.mapper.CntTarMapper;
import com.manyun.business.service.ICntTarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.ICntUserTarService;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

import static com.manyun.common.core.enums.TarStatus.*;

/**
 * <p>
 * 抽签规则(盲盒,藏品) 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@Service
public class CntTarServiceImpl extends ServiceImpl<CntTarMapper, CntTar> implements ICntTarService {

    @Autowired
    private ICntUserTarService userTarService;




    /**
     * 判定对应的  buiId 抽签状态
     * 能进来的都是 必须抽签的 ！！！  需强制验证
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public Integer tarStatus(String userId, String buiId) {
        CntUserTar userTar = userTarService.getOne(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getUserId, userId).eq(CntUserTar::getBuiId, buiId));
        // 如果未null 代表从来没有抽过
        if (Objects.isNull(userTar))
            return CEN_NOT_TAR.getCode();
        return userTar.getIsFull();
    }

    @Override
    public Integer tarCollection(CntCollection cntCollection, String userId) {
        return tarComm(cntCollection.getId(),cntCollection.getTarId(),userId);
    }

    @Override
    public Integer tarBox(Box box, String userId) {
        return tarComm(box.getId(),box.getTarId(),userId);
    }

    private Integer tarComm(String buiId,String tarId,String userId){
        Assert.isTrue(CEN_NOT_TAR.getCode().equals(tarStatus(userId,buiId)),"您已经抽过签了!");
        // 开始抽签
        CntTar cntTar = getOne(Wrappers.<CntTar>lambdaQuery().eq(CntTar::getId,tarId));
        // 抽签算法 按照比例计算
        //1=已抽中,2=未抽中
        Integer isFull = nowTimeIndex(cntTar.getTarPro());
        CntUserTar cntUserTar = Builder.of(CntUserTar::new)
                .with(CntUserTar::setUserId,userId)
                .with(CntUserTar::setBuiId,buiId)
                .with(CntUserTar::setId, IdUtil.getSnowflakeNextIdStr())
                .with(CntUserTar::setIsFull,isFull)
                .with(CntUserTar::createD,userId)
                .build();
        userTarService.save(cntUserTar);
        return isFull;
    }

    /**
     * 1=已抽中,2=未抽中
     * @param tarPro
     * @return
     */
    private Integer nowTimeIndex(BigDecimal tarPro) {
        int range = tarPro.intValue();
        HashMap<Integer, Integer> objectObjectHashMap = Maps.newHashMap();
        for (int i = 0; i < 100; i++) {
             if (range >=i){
                 objectObjectHashMap.put(i,Integer.valueOf(CEN_YES_TAR.getCode()));
                 continue;
             }
            objectObjectHashMap.put(i,Integer.valueOf(CEN_NO_TAR.getCode()));
        }
        return objectObjectHashMap.get( Long.valueOf(DateUtil.current() % 100).intValue());

    }
}
