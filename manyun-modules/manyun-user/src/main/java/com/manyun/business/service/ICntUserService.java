package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.UserChangeForm;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.model.LoginPhoneForm;

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
}
