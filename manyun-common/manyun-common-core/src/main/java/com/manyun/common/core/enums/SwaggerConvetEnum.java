package com.manyun.common.core.enums;

import lombok.Data;
import lombok.Getter;

/**
 * id转义符
 * 
 * @author yanwei
 */
@Getter
public enum SwaggerConvetEnum
{
    MANYUN_AUTH("manyun-auth", "认证中心(admin)(web)"),
/*    MANYUN_GEN("manyun-gen", "代码生成"),
    MANYUN_JOB("manyun-job", "定时任务"),*/
    MANYUN_FILE("manyun-base", "基础能力服务"),
    MANYUN_SYSTEM("manyun-admin", "后台管理相关模块"),
    MANYUN_DEMO("manyun-demo", "示例相关模块"),

    MANYUN_UNON("未知", "未知");

    private final String id;
    private final String name;

    SwaggerConvetEnum(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
