package com.manyun.common.security.service;

import com.manyun.comm.api.domain.CntUser;
import com.manyun.comm.api.domain.vo.AccTokenVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.comm.api.model.LoginUser;
import com.manyun.common.core.constant.CacheConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.enums.UserLoginSource;
import com.manyun.common.core.utils.JwtUtils;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.ip.IpUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.redis.service.RedisService;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 * 
 * @author yanwei
 */
@Component
public class UserTokenService
{
    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public AccTokenVo createToken(CntUser cntUser)
    {
        LoginBusinessUser loginBusinessUser=new LoginBusinessUser();
        String token = IdUtils.fastUUID();
        String userId = cntUser.getId();
        String nickName = cntUser.getNickName();
        loginBusinessUser.setToken(token);
        loginBusinessUser.setUserId(userId);
        loginBusinessUser.setUsername(nickName);
        loginBusinessUser.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        loginBusinessUser.setCntUser(cntUser);
        refreshToken(loginBusinessUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, nickName);
        claimsMap.put(SecurityConstants.DETAILS_LOGIN_SOURCE, UserLoginSource.APP.getInfo());
        return AccTokenVo.builder().access_token(JwtUtils.createToken(claimsMap)).expires_in(loginBusinessUser.getExpireTime()).build();
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginBusinessUser getLoginBusinessUser()
    {
        return getLoginBusinessUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginBusinessUser getLoginBusinessUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginBusinessUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginBusinessUser getLoginBusinessUser(String token)
    {
        LoginBusinessUser user = null;
        try
        {
            if (StringUtils.isNotEmpty(token))
            {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        }
        catch (Exception e)
        {
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginBusinessUser(LoginBusinessUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginBusinessUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginBusinessUser loginUser)
    {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }
}