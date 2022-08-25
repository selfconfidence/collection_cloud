package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.*;
import com.manyun.business.domain.vo.*;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.R;

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

    CntUser jgPhoneLogin(JgLoginTokenForm jgLoginTokenForm);

    R userRealName(UserRealForm userRealForm, String userId);

    void checkCertifyIdH5Status(String certifyId, String userId);

    R<InviteUserVo> inviteUser(String userId);

    void checkPaySecure(String paySecure, String userId);

    void saveJpush(String userId, String uuId);

    void asyncInviteUser(String userId);

    AliRealVo getH5CertifyId(UserAliyunRealForm userAliyunRealForm,String userId);

}
