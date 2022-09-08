package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.CntTar;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 抽签规则(盲盒,藏品) 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
public interface ICntTarService extends IService<CntTar> {

    Integer tarStatus(String userId, String buiId);

    boolean isSell(String userId, String buiId);

    String tarCollection(CntCollection cntCollection, String userId);

    String tarBox(Box box, String userId);

    void overSelf(String userId, String id);

    // 开始公布抽签结果
    void openTarResult(String tarId);
}
