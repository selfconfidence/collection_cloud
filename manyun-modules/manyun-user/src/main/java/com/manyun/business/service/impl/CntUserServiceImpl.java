package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.form.UserChangeForm;
import com.manyun.business.domain.form.UserChangeLoginForm;
import com.manyun.business.domain.form.UserChangePayPass;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.business.domain.vo.UserLevelVo;
import com.manyun.business.domain.vo.UserPleaseBoxVo;
import com.manyun.business.mapper.CntUserMapper;
import com.manyun.business.service.ICntUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IUserPleaseService;
import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;

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

    @Autowired
    private IUserPleaseService userPleaseService;


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

    @Override
    public void changeUser(UserChangeForm userChangeForm,String userId) {
   update(Wrappers.<CntUser>lambdaUpdate()
           .set(StrUtil.isNotBlank(userChangeForm.getHeadImage()),CntUser::getHeadImage,userChangeForm.getHeadImage())
           .set(StrUtil.isNotBlank(userChangeForm.getUserInfo()),CntUser::getUserInfo,userChangeForm.getUserInfo())
           .set(StrUtil.isNotBlank(userChangeForm.getNickName()),CntUser::getNickName,userChangeForm.getNickName())
           .eq(CntUser::getId,userId));

    }

    /**
     * 根据用户编号查询 用户详细信息
     * @param userId
     * @return
     */
    @Override
    public UserInfoVo info(String userId) {
        CntUser cntUser = getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(cntUser,userInfoVo);
        return userInfoVo;
    }

    /**
     * 修改密码
     * @param userId
     * @param userChangeLoginForm
     */
    @Override
    public void changeLogin(String userId, UserChangeLoginForm userChangeLoginForm) {
        CntUser cntUser = getById(userId);
        Assert.isTrue(userChangeLoginForm.getOldPass().equals(cntUser.getLoginPass()),"旧密码输出错误,请核实!");
        cntUser.updateD(userId);
        updateById(cntUser);

    }

    @Override
    public void changePayPass(String userId, UserChangePayPass userChangePayPass) {
        CntUser cntUser = getById(userId);
        cntUser.setPayPass(userChangePayPass.getNewPayPass());
        cntUser.updateD(userId);
        updateById(cntUser);
    }

    /**
     * 查询 用户下级
     * @param userId
     * @return
     */
    @Override
    public UserLevelVo userLevel(String userId) {
        List<CntUser> cntUsers = list(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getParentId, userId).orderByDesc(CntUser::getUpdatedTime));
        UserLevelVo userLevelVo = Builder.of(UserLevelVo::new).build();
        userLevelVo.setLevelCount(cntUsers.size());
        userLevelVo.setUserInfoVos(cntUsers.parallelStream().map(this::initUserLevelVo).collect(Collectors.toList()));
        return userLevelVo;
    }

    /**
     * 根据用户编号 查询当前用的邀请奖励进行到那一步了
     * @param userId
     * @return
     */
    @Override
    public List<UserPleaseBoxVo> userPleaseBoxVo(String userId) {
        // 1. 将当前用户下级查出 实名过的
        long userRealCount = count(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getParentId, userId).eq(CntUser::getIsReal,OK_REAL.getCode()));
        return  userPleaseService.userPleaseBoxVo(userId,userRealCount);
    }

    /**
     * 领取 邀请盲盒权益
     * @param userId
     * @param pleaseId
     * @return
     */
    @Override
    public String openPleaseBox(String userId, String pleaseId) {
        long userRealCount = count(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getParentId, userId).eq(CntUser::getIsReal,OK_REAL.getCode()));
        String msg = userPleaseService.openPleaseBox(userRealCount,pleaseId,userId);
        return msg;
    }

    private UserInfoVo initUserLevelVo(CntUser cntUser) {
        UserInfoVo userInfoVo = Builder.of(UserInfoVo::new).build();
        BeanUtil.copyProperties(cntUser,userInfoVo);
        return userInfoVo;
    }
}
