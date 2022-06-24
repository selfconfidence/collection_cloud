package com.manyun.business.service;

import com.manyun.business.domain.entity.UserPlease;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.UserPleaseBoxVo;

import java.util.List;

/**
 * <p>
 * 用户邀请奖励历程 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-24
 */
public interface IUserPleaseService extends IService<UserPlease> {

    List<UserPleaseBoxVo> userPleaseBoxVo(String userId, long userRealCount);

    String openPleaseBox(long userRealCount, String pleaseId);
}
