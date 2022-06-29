package com.manyun.comm.api.model;

import com.manyun.comm.api.domain.dto.CntUserDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息
 *
 * @author yanwei
 */
@Data
public class LoginBusinessUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户名id
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    /**
     * 角色列表
     */
    private Set<String> roles;

    /**
     * 用户信息
     */
    private CntUserDto cntUser;

}
