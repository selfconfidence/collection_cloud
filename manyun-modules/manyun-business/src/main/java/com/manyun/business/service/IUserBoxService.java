package com.manyun.business.service;

import com.manyun.business.domain.entity.UserBox;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.UserBoxVo;
import com.manyun.comm.api.domain.dto.OpenPleaseBoxDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户购买盲盒中间表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
public interface IUserBoxService extends IService<UserBox> {

    void bindPleaseBoxDto(OpenPleaseBoxDto pleaseBoxDto);

    String bindOrderBox(String userId, String buiId, String sourceInfo, Integer goodsNum);

    void bindBox(String userId, String buiId, String sourceInfo, Integer goodsNum);

    List<UserBoxVo> pageUserBox(String userId,String commName);

    Boolean existUserBox(String userId, String id);

    void tranBox(String tranUserId, String toUserId, String buiId);

    String hideUserBox(String buiId, String userId, String info);

    String showUserBox(String buiId, String sendUserId, String format);
}
