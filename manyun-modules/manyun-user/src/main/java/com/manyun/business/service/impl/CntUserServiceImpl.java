package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.mapper.CntUserMapper;
import com.manyun.business.service.ICntUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.model.LoginPhoneForm;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class CntUserServiceImpl extends ServiceImpl<CntUserMapper, CntUser> implements ICntUserService {

    @Override
    public CntUser login(LoginPhoneForm loginPhoneForm) {
        CntUser cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, loginPhoneForm.getPhone()));
        Assert.isTrue(Objects.nonNull(cntUser),"暂未找到该手机号!");
        Assert.isTrue(loginPhoneForm.getPassword().equals(cntUser.getLoginPass()),"密码输出错误!");
        return cntUser;
    }

    @Override
    public CntUser codeLogin(String phone) {
        CntUser cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, phone));
        Assert.isTrue(Objects.nonNull(cntUser),"暂未找到该手机号!");
        return cntUser;
    }
}
