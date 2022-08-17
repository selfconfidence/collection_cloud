package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.shaded.com.google.protobuf.Timestamp;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.config.AliRealConfig;
import com.manyun.business.config.AsyncUtil;
import com.manyun.business.config.InviteUtil.PosterUtil;
import com.manyun.business.config.UnionRealConfig;
import com.manyun.business.domain.form.*;
import com.manyun.business.domain.vo.InviteUserVo;
import com.manyun.business.domain.vo.UserInfoVo;
import com.manyun.business.domain.vo.UserLevelVo;
import com.manyun.business.domain.vo.UserPleaseBoxVo;
import com.manyun.business.mapper.CntUserMapper;
import com.manyun.business.service.ICntUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.IUserPleaseService;
import com.manyun.business.domain.entity.CntUser;
import com.manyun.comm.api.RemoteBuiMoneyService;
import com.manyun.comm.api.RemoteFileService;
import com.manyun.comm.api.RemoteSystemService;
import com.manyun.comm.api.domain.SysFile;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.form.JgLoginTokenForm;
import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.comm.api.model.LoginPhoneForm;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.jg.JgAuthLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.json.JsonObject;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;



import static com.manyun.common.core.constant.BusinessConstants.SystemTypeConstant.USER_DEFAULT_LINK;
import static com.manyun.common.core.enums.UserRealStatus.NO_REAL;
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

    @Autowired
    private RemoteBuiMoneyService remoteBuiMoneyService;

    @Autowired
    private AliRealConfig aliRealConfig;

    @Autowired
    private UnionRealConfig unionRealConfig;

    @Autowired
    private RemoteSystemService remoteSystemService;

    @Autowired
    private RemoteFileService fileService;

    @Autowired
    private JgAuthLoginUtil jgAuthLoginUtil;


    @Override
    public CntUser login(LoginPhoneForm loginPhoneForm) {
        CntUser cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, loginPhoneForm.getPhone()));
        Assert.isTrue(Objects.nonNull(cntUser),"暂未找到该手机号!");
        Assert.isTrue(loginPhoneForm.getPassword().equals(cntUser.getLoginPass()),"密码输入错误!");
        return cntUser;
    }

    @Override
    public CntUser codeLogin(String phone) {
        CntUser cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, phone));
        // 登录即注册
        if (Objects.isNull(cntUser)){
            regUser(new UserRegForm(phone));
            cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, phone));
        }
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
        if (Objects.nonNull(cntUser.getLoginPass()))
        Assert.isTrue(userChangeLoginForm.getOldPass().equals(cntUser.getLoginPass()),"旧密码输入错误,请核实!");

        cntUser.setLoginPass(userChangeLoginForm.getNewPass());
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

    /**
     * 手机号|区块链地址|ID|系统ID
     * @param commUni
     * @return 没有就返回  null 即可！！！
     */
    @Override
    public CntUser commUni(String commUni) {
        return getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getUserId, commUni).or().eq(CntUser::getLinkAddr, commUni).or().eq(CntUser::getId,commUni).or().eq(CntUser::getPhone, commUni));
    }

    /**
     * 用户注册 手机号验证码的方式
     * @param userRegForm
     */
    @Override
    public void regUser(UserRegForm userRegForm) {
        CntUser user = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, userRegForm.getPhone()));
        Assert.isTrue(Objects.isNull(user),"您已注册,请登录！");
        CntUser cntUser = Builder.of(CntUser::new).build();
        initUser(cntUser);
        cntUser.setPhone(userRegForm.getPhone());
        bindParentCode(cntUser,userRegForm.getPleaseCode());
        cntUser.setLoginPass(userRegForm.getLoginPassWord());
        // 初始化钱包
        remoteBuiMoneyService.initUserMoney(cntUser.getId(),SecurityConstants.INNER);
        save(cntUser);

    }

    /**
     * 实名认证
     * @param userRealForm
     * @return
     */
    @Override
    public R userRealName(UserRealForm userRealForm, String userId) {
        R r = unionRealConfig.unionReal(userRealForm);
        if (200 != r.getCode()) {
            return R.fail("实名认证失败");
        }
        UserRealMoneyForm userRealMoneyForm = Builder.of(UserRealMoneyForm::new).build();
        userRealMoneyForm.setUserId(userId);
        userRealMoneyForm.setBankcard(userRealForm.getBankCart());
        userRealMoneyForm.setCartNo(userRealForm.getCartNo());
        userRealMoneyForm.setUserRealName(userRealForm.getRealName());
        r = remoteBuiMoneyService.updateUserMoney(userRealMoneyForm, SecurityConstants.INNER);
        return r;
    }

    /**
     * 获取认证ID
     * @param cntUser
     * @return
     */
    @Override
    public String getCertifyId(CntUserDto cntUser, UserAliyunRealForm userAliyunRealForm) {
        return aliRealConfig.getCertifyId(userAliyunRealForm);
    }


    @Override
    public void checkCertifyIdStatus(String certifyId, CntUserDto cntUser) {
        aliRealConfig.checkCertifyIdStatus(certifyId);
        optimisticRealUser(cntUser.getId());
    }

    @Override
    public CntUser jgPhoneLogin(JgLoginTokenForm jgLoginTokenForm) {
        String phone = jgAuthLoginUtil.jgAuthAllPhone(jgLoginTokenForm.getLoginToken());
        CntUser cntUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, phone));
        if (Objects.nonNull(cntUser))return cntUser;
        // 新用户 /开始注册 没有上级！！！
        UserRegForm userRegForm = Builder.of(UserRegForm::new).build();
        userRegForm.setPhone(phone);
        regUser(userRegForm);
        return getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, phone));
    }

    /*
    绑定用户上级
     */
    private void bindParentCode(CntUser cntUser, String pleaseCode) {
        CntUser parentUser= null;
        if (StrUtil.isNotBlank(pleaseCode) && (parentUser = getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPleaseCode, pleaseCode))) != null){
            cntUser.setParentId(parentUser.getId());
        }

    }

    /**
     * 初始化用户信息
     * @param cntUser
     */
    private void initUser(CntUser cntUser) {
        String userId = IdUtil.getSnowflakeNextIdStr();
        cntUser.setId(userId);
        cntUser.setPleaseCode(genderPleaseCode());
        cntUser.createD(userId);
        // 就是做展示，无需复杂
        cntUser.setUserId(RandomUtil.randomNumbers(8));
        cntUser.setIsReal(NO_REAL.getCode());
        // 默认头像
        cntUser.setHeadImage(remoteSystemService.findType(USER_DEFAULT_LINK, SecurityConstants.INNER).getData());
    }

    private String genderPleaseCode(){
        String randomNumbers = RandomUtil.randomNumbers(6);
        long count = count(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPleaseCode, randomNumbers));
        return count == 0 ? randomNumbers: genderPleaseCode();
    }


    /**
     * 实名认证  乐观执行
     * @param userId
     * @return
     */
    public void optimisticRealUser(String userId){
        CntUser cntUser = getById(userId);
        cntUser.setIsReal(OK_REAL.getCode());
        // 用手机号当 模拟地址 无奈之举
        cntUser.setLinkAddr(SecureUtil.sha256(cntUser.getPhone()));
        cntUser.updateD(userId);
        updateById(cntUser);
    }


    private UserInfoVo initUserLevelVo(CntUser cntUser) {
        UserInfoVo userInfoVo = Builder.of(UserInfoVo::new).build();
        BeanUtil.copyProperties(cntUser,userInfoVo);
        if (StrUtil.isNotBlank(userInfoVo.getPhone()) && userInfoVo.getPhone().length() >= 11)
            userInfoVo.setPhone(DesensitizedUtil.mobilePhone(userInfoVo.getPhone()));
        return userInfoVo;
    }

    /**
     * 分享邀请海报
     * @param userId
     * @return
     */


    public void asyncInviteUser(String userId) {
        AsyncUtil.submit(() -> {
            R<InviteUserVo> inviteUserVoR = inviteUser(userId);
            inviteUserVoR.getData().getInviteUrl();
            if (inviteUserVoR.getCode() != 200) {
                log.error("异步生成图片失败");
            }
        });
    }


    @Override
    public R<InviteUserVo> inviteUser(String userId) {
        CntUser cntUser = getById(userId);
        InviteUserVo inviteUserVo = new InviteUserVo();
        inviteUserVo.setInviteCode(cntUser.getPleaseCode());
        if (StringUtils.isNotBlank(cntUser.getInviteUrl())) {
            inviteUserVo.setInviteUrl(cntUser.getInviteUrl());
            return R.ok(inviteUserVo);
        }
        //背景，海报图
        String background = remoteSystemService.findType(BusinessConstants.SystemTypeConstant.INVITE_URL, SecurityConstants.INNER).getData();
        int width = 0;
        int height = 0;
        try {
            URL backgroundUrl = new URL(background);
            BufferedImage read = ImageIO.read(backgroundUrl.openStream());
            width = read.getWidth();
            height = read.getHeight();

            BufferedImage bufferedImage = PosterUtil.drawInitAndChangeSize(background, backgroundUrl.openConnection().getInputStream(),width, height);

            // 画 二维码 并改变大小
            // 1. 先 获取二维码(二维码携带一个参数)

            String regUrl = remoteSystemService.findType(BusinessConstants.SystemTypeConstant.REG_URL, SecurityConstants.INNER).getData() + "?pleaseCode=" + cntUser.getPleaseCode();
            // + "?" + cntUser.getPleaseCode()
            // 生成二维码并指定宽高
            BufferedImage qrCode = QrCodeUtil.generate(regUrl, 300, 300);
            // 2. 初始化并的改变大小
            // 将二维码保存到本地
            // 3. 画二维码
            //PosterUtil.drawImage(bufferedImage, qrCode, 300, 300, (int) Math.round(width*0.37), (int) Math.round(height*0.75));
            PosterUtil.drawImage(bufferedImage, qrCode, 300, 300, (int) Math.round(width/2 - 300/2), (int) Math.round(height*0.75));

            // 海报 保存到本地/var/opt/  线上使用 "d:\\upload\\"
            String linuxPath = remoteSystemService.findType(BusinessConstants.SystemTypeConstant.LOCAL_PATH, SecurityConstants.INNER).getData();
            File file1 = new File(linuxPath);
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }
            String localPath = linuxPath + System.currentTimeMillis() + ".png";
            PosterUtil.save(bufferedImage, "png", localPath);
            HashMap<String, Object> paramMap = new HashMap<>();
            File file = FileUtil.file(localPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            paramMap.put("file", file);

            String gatewayUrl = remoteSystemService.findType(BusinessConstants.SystemTypeConstant.GATEWAY_URL, SecurityConstants.INNER).getData();

            String post = HttpUtil.post(gatewayUrl, paramMap);
            JSONObject responseJson = JSONUtil.toBean(post, JSONObject.class);
            Object data = responseJson.get("data");
            file.delete();
            cntUser.setInviteUrl(data.toString());
            updateById(cntUser);
            inviteUserVo.setInviteUrl(data.toString());
            return R.ok(inviteUserVo);


        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }

    }



    @Override
    public void checkPaySecure(String paySecure, String userId) {
        CntUser cntUser = getById(userId);
        Assert.isTrue(StrUtil.isNotBlank(cntUser.getPayPass()),"未设置支付密码,请核实!");
        Assert.isTrue(paySecure.equals(cntUser.getPayPass()),"支付密码校验失败!");

    }

    @Override
    public void saveJpush(String userId, String uuId) {
        CntUser cntUser = getById(userId);
        cntUser.updateD(userId);
        cntUser.setJgPush(uuId);
        updateById(cntUser);
    }
}
