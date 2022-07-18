package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.*;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.business.domain.vo.UserLevelVo;
import com.manyun.business.domain.vo.UserPleaseBoxVo;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.model.LoginPhoneForm;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface ICntUserService extends IService<CntUser> {

    CntUser login(LoginPhoneForm loginPhoneForm);

    CntUser codeLogin(String phone);

    void changeUser(UserChangeForm userChangeForm,String userId);

    UserInfoVo info(String userId);

    void changeLogin(String userId, UserChangeLoginForm userChangeLoginForm);

    void changePayPass(String userId, UserChangePayPass userChangePayPass);

    UserLevelVo userLevel(String userId);

    List<UserPleaseBoxVo> userPleaseBoxVo(String userId);

    String openPleaseBox(String userId, String pleaseId);

    CntUser commUni(String commUni);

    void regUser(UserRegForm userRegForm);

    String getCertifyId(CntUserDto cntUser, UserAliyunRealForm userAliyunRealForm);

    void checkCertifyIdStatus(String certifyId, CntUserDto cntUser);
}
