package com.manyun.common.security.utils;

import javax.servlet.http.HttpServletRequest;

import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.exception.auth.NotLoginException;
import com.manyun.common.core.text.Convert;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.constant.TokenConstants;
import com.manyun.common.core.context.SecurityContextHolder;
import com.manyun.common.core.utils.ServletUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.comm.api.model.LoginUser;

/**
 * 权限获取工具类
 * 
 * @author yanwei
 */
public class SecurityUtils
{
    /**
     *  后台管理获取用户ID
     */
    public static Long getUserId()
    {
        return SecurityContextHolder.getUserId();
    }

    /**
     * 用户端 获取用户ID
     */
    public static String getBuiUserId()
    {
        return SecurityContextHolder.getBuiUserId();
    }

    /**
     * 获取用户名称
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getUserName();
    }

    /**
     * 获取用户key
     */
    public static String getUserKey()
    {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * 获取用户来源
     */
    public static String getSource()
    {
        return SecurityContextHolder.getSource();
    }


    /**
     * 获取登录用户信息
     */
    public static LoginUser getLoginUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * 获取登录用户信息
     * 可以为 null
     */
    public static LoginBusinessUser getLoginBusinessUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginBusinessUser.class);
    }


    /**
     * 获取登录用户信息
     * 可以为 null
     */
    public static LoginBusinessUser getTestLoginBusinessUser()
    {
        if (true)
            return getNotNullLoginBusinessUser();
        //  测试用户
        LoginBusinessUser loginBusinessUser = new LoginBusinessUser();
        loginBusinessUser.setUserId("1");
        return loginBusinessUser;
    }


    /**
     * 获取登录用户信息
     * 必须有用户
     * 缓存数据,不可为实时数据使用！！！
     */
    public static LoginBusinessUser getNotNullLoginBusinessUser()
    {
        LoginBusinessUser loginBusinessUser= null;
        if ((loginBusinessUser = SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginBusinessUser.class)) != null) {
            return loginBusinessUser;
        }
        throw new NotLoginException("暂未登录");
    }

    /**
     * 获取请求token
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * 根据request获取请求token
     */
    public static String getToken(HttpServletRequest request)
    {
        // 从header获取token标识
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * 裁剪token前缀
     */
    public static String replaceTokenPrefix(String token)
    {
        // 如果前端设置了令牌前缀，则裁剪掉前缀
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * 是否为管理员
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static boolean upgradeEncoding(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.upgradeEncoding(password);
    }
    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
