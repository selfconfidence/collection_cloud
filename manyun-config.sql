/*
 Navicat Premium Data Transfer

 Source Server         : cnt线上
 Source Server Type    : MySQL
 Source Server Version : 50738
 Source Host           : rm-2ze5st53a9v36x73xgo.mysql.rds.aliyuncs.com:3306
 Source Schema         : manyun-config

 Target Server Type    : MySQL
 Target Server Version : 50738
 File Encoding         : 65001

 Date: 17/11/2022 14:00:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (41, 'sentinel-manyun-gateway', 'SENTINEL_SECURITY', '[\n    {\n        \"resource\": \"manyun-auth\",\n        \"count\": 1700,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"manyun-admin\",\n        \"count\": 1600,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0\n    },\n	{\n        \"resource\": \"manyun-business\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"manyun-business_apis\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"manyun-admin\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"manyun-base\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"manyun-user\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '027d1c050840d8e63d2b5b4904377138', '2022-06-14 08:55:00', '2022-10-09 03:42:55', 'manyun', '125.41.204.224', '', '', '流量布控相关持久化', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (82, 'application-env.yml', 'DEFAULT_GROUP', 'spring:\n#  zipkin:\n#    base-url: http://8.131.68.152:9411/ #zipkin server的请求地址\n#    discovery-client-enabled: false #让nacos把它当成一个URL，而不要当做服务名\n  sleuth:\n    sampler:\n      probability: 1.0\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\nglobal:\n  is_open: true\n  request_time: 1000s\n  privateKey: MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALS/GOVJQGmBq8vrxZcql5Es1iW+t1rKJ6tLK+OhsB8gyM7mJdQAnxli8vpWy8yYAa+k2QuEsQuSledGPlsIhUUqc3R8TEooNBXSJTyyIQAnveXnXsbNPV/IqbHCgHKiKFEdabnXxxB2TZclqrmnowlzvmej6D+SjWvSsc3oCJhjAgMBAAECgYA8+DVSqsoCjQ5BhG+rlV95RjGam+HHy2dkPcA5UgJSDcIPIM1j3S3N53XlAkBO7HutHyNj4kfCipjsYeKI3K/vWfpkcMHYWepruv4bH66onuPKSbMqdi+omKMe8eSR8ZKnbHJPZHvx42aL/0U4HTye0WoTiMt70pLToBP9LdME+QJBAO8Xb9dtvNW/CkXJaFViNjfiOPu8uj8oT4X7FJ0w+gA/kmFFudZf1D3Wv7TX7P8uEhDm0HymmFWQLdgoCIT/E08CQQDBh151BG3ZrhS3AojT/8LMGxL7M/GyYmLPqSocBGxAcdFsh+ocKSxo6Ps/ki2opS9dFo0+CMDdPOgo+oXfW7StAkBiEDrPITNSeAi+lt00KBQU17SHi+DYBrCXQ2QxFOV1NJ3VR8PxKVzkADCg++84uqyuQf1BIxNYBMrI3aKg6f4BAkEAlBpulQrArnfkbnHIGMWAZAbMLeYAr81PZTBGg4528ZKo/G+/H7H6xIaX0Hyj6I4RgyHBEZpNToJQARxH2zkpOQJAMB0660vYYe9w0JNSWqS2FZL6DUKs7F27UTEnCNsGjpS+NZ0xBVh4Srg33KBnLbzBHwvGH7e++GEuZCJYIU/nFg==\n  publicKey: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0vxjlSUBpgavL68WXKpeRLNYlvrdayierSyvjobAfIMjO5iXUAJ8ZYvL6VsvMmAGvpNkLhLELkpXnRj5bCIVFKnN0fExKKDQV0iU8siEAJ73l517GzT1fyKmxwoByoihRHWm518cQdk2XJaq5p6MJc75no+g/ko1r0rHN6AiYYwIDAQAB\n  projectNames: \n   - cnt_ios\n   - cnt_h5\n   - cnt_android\n  errorMsg: Can t keep making mistakes!!!\nopen:\n  url: https://notify.dcalliance.com.cn', 'b09db64d0f6834ee1efd7526c4fe2878', '2022-09-20 06:48:31', '2022-10-14 08:44:48', 'manyun', '219.157.186.102', '', '', '通用配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (83, 'manyun-gateway-env.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  cloud:\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: manyun-auth\n          uri: lb://manyun-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        #- id: manyun-gen\n        #  uri: lb://manyun-gen\n        #  predicates:\n        #    - Path=/code/**\n        #  filters:\n        #    - StripPrefix=1\n        # 定时任务\n        #- id: manyun-job\n        #  uri: lb://manyun-job\n        #  predicates:\n        #    - Path=/schedule/**\n        #  filters:\n        #    - StripPrefix=1\n        # admin 相关apis\n        - id: manyun-admin\n          uri: lb://manyun-admin\n          predicates:\n            - Path=/admin/**\n          filters:\n            - StripPrefix=1\n            # 演示相关\n        - id: manyun-demo\n          uri: lb://manyun-demo\n          predicates:\n            - Path=/web/**\n          filters:\n            - StripPrefix=1            \n        # 基础能力相关apis\n        - id: manyun-base\n          uri: lb://manyun-base\n          predicates:\n            - Path=/base/**\n          filters:\n            - StripPrefix=1\n          # 用户能力相关apis\n        - id: manyun-user\n          uri: lb://manyun-user\n          predicates:\n            - Path=/user/**\n          filters:\n            - StripPrefix=1\n          # 业务相关apis\n        - id: manyun-business\n          uri: lb://manyun-business\n          predicates:\n            - Path=/business/**\n          filters:\n            - StripPrefix=1            \n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: false\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n        # 管理员\n    #  - /admin/**\n    # 基础能力 \n      - /base/sendPhone/**\n      - /base/sendPhoneToken/**\n      - /base/upload\n      - /auth/user/phpLogin\n    # auth 相关\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /auth/user/login\n      - /auth/user/phpLogin\n      - /auth/user/loginRSA\n      - /auth/user/codeLogin\n      - /auth/user/jgAuthPhoneLogin\n      # demo 相关\n      - /web/demo/tbBui/**\n      #business相关\n      - /business/cntNotice/**/**\n      - /business/msg/pageMsgList\n      - /business/cate/**/**\n      - /business/banner/list/**\n      - /business/collection/homeCacheList\n      - /business/box/homeCacheList\n      - /business/collection/pageList\n      - /business/collection/info/**\n      - /business/collection/queryDict/**\n      - /business/box/pageList\n      - /business/box/queryDict/**\n      - /business/box/info/**\n      - /business/system/sellInfo\n      - /business/system/collectionInfo\n      - /business/system/findType/**\n      - /business/announcement/list\n      - /business/agreement/info/*\n      - /business/actionTar/test\n      - /business/tar/**\n      - /business/notify_pay/ShandePay/**\n      - /business/notify_pay/LlPay/**\n      - /business/notify_pay/sandWallet/**\n      - /business/consignment/openConsignmentList\n    #  - /business/consignment/pageConsignmentCollectionList\n      - /business/version/**\n      # 用户相关\n      - /user/cntUser/regUser\n      - /user/notify_real/real\n      - /user/cntUser/payEncrypt/*\n      - /user/real/**\n\n      # 公众层相关 \n      - /*/v2/api-docs\n      - /csrf', 'fa3913b5cfae09edcbdb0028d21e5217', '2022-09-20 06:48:31', '2022-11-05 03:53:09', 'manyun', '115.60.150.227', '', '', '网关模块', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (84, 'manyun-auth-env.yml', 'DEFAULT_GROUP', 'spring: \n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@', 'ac0a71ab22c1800339bcf920d0058c34', '2022-09-20 06:48:31', '2022-09-20 07:07:42', 'manyun', '125.47.75.214', '', '', '认证中心', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (85, 'manyun-monitor-env.yml', 'DEFAULT_GROUP', '# spring\nspring: \n  security:\n    user:\n      name: manyun\n      password: Manyun2022@\n  boot:\n    admin:\n      ui:\n        title: 漫云服务状态监控', 'f525f044d8915dab08ad26c9e640c956', '2022-09-20 06:48:31', '2022-09-21 05:48:12', 'manyun', '219.157.189.176', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (86, 'manyun-base-env.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n\n# 本地文件上传    \nfile:\n    domain: http://8.131.68.152:9300\n    path: /home/workspace/manyun/uploadPath\n    prefix: /statics\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://8.129.231.12:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test\n  \n# 阿里云短信\nsms:\n  accessKey: LTAI5t8m7GGthpTmbyhMW4MU\n  secret: wOjfyavkD4GUZjwtZAaDMGjxeJxdXg\n  sing: CNT数藏\n  regionId: cn-chengdu\n  templateCode: SMS_249885533\n# 阿里云 oss 对象存储  \noss:\n  bucketName: cnt-images\n  endpoint: oss-cn-chengdu.aliyuncs.com\n  accessKey: LTAI5t8m7GGthpTmbyhMW4MU\n  secret: wOjfyavkD4GUZjwtZAaDMGjxeJxdXg\n  fileName: user/images\n  bucketDomain: zhangbiao19960117.oss-cn-chengdu.aliyuncs.com', 'e2d33c7179c5062a14f2063150040d03', '2022-09-20 06:48:31', '2022-09-20 07:08:08', 'manyun', '125.47.75.214', '', '', '文件服务', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (87, 'manyun-admin-env.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: manyun\n        loginPassword: manyun2022@\n        allow: \n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://rm-2ze5st53a9v36x73xgo.mysql.rds.aliyuncs.com:3306/manyun-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: manyun\n            password: Manyun2022@\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      manyun-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server}\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server}\n      namespace:\n\n# mybatis配置\n#mybatis:\n#    # 搜索指定包别名\n#    typeAliasesPackage: com.manyun.admin.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n#    mapperLocations: classpath:mapper/**/*.xml\nmybatis-plus:\n  mapper-locations:\n  - classpath:mapper/**/*.xml\n  - classpath*:com/manyun/comm/*.xml\n  type-aliases-package: com.manyun.admin.domain;com.manyun.comm.api.domain\n  global-config:\n    banner: false\n# swagger配置\nswagger:\n  title: 后台管理模块接口文档\n  license: Powered By yanwei\n  licenseUrl: xxx\n\n  # 代码生成\ngen: \n  # 作者\n  author: yanwei\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.manyun.admin\n  # 自动去除表前缀，默认是false\n  autoRemovePre: false\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: sys_', '94e159d7328d7a059dd2408c91628482', '2022-09-20 06:48:31', '2022-10-14 10:42:02', 'manyun', '219.157.186.102', '', '', '后台管理相关业务配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (90, 'application-alipay-env.yml', 'DEFAULT_GROUP', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nali:\n  pay:\n    webUrl: xxx\n    aliAppId: xxx\n    aliPublicKey: xxx\n    aliPrivateKey: xxx', 'd912c8f7db59303330e7640c2e3594de', '2022-09-20 06:50:12', '2022-09-20 06:50:12', NULL, '125.47.75.214', '', '', '共享配置 支付宝相关配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (91, 'application-wechatpay-env.yml', 'DEFAULT_GROUP', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\nwechat:\n  pay:\n    webUrl: xxx\n    wxAppId: xxx\n    wxMchId: xxx\n    wxCertAddr: xxx\n    wxMchKey: xxx', 'a5429913bb17bf45e165a73aa9ca6d10', '2022-09-20 06:50:12', '2022-09-20 06:50:12', NULL, '125.47.75.214', '', '', '共享配置 支付宝相关配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (92, 'application-alireal-env.yml', 'DEFAULT_GROUP', 'ali:\n  real:\n    sceneId: 3686001\n    h5_sceneId: 1000004565\n    h5_return_url: https://art.dcalliance.com.cn/#/pages/tabs/my/index\n    h5_redirection: https://apis.dcalliance.com.cn/prod-api/user/real/h5RealRedirection\n    h5_callback_url: https://www.baidu.com/\n    accessKey: LTAI5t8m7GGthpTmbyhMW4MU\n    accessKeySecret: wOjfyavkD4GUZjwtZAaDMGjxeJxdXg', 'cb677144fbb85ca8e36bf6ea0d87cc2f', '2022-09-20 06:50:12', '2022-10-13 02:57:18', 'manyun', '125.47.74.14', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (93, 'application-jiguang-env.yml', 'DEFAULT_GROUP', 'jiguang:\n  jpush:\n    masterSecret: 6187946e0ad26fe69d607a32\n    appKey: 844f360abd0653e2aed19cad \n  auth:\n    priKey: MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALzMUtKOBCt6R5UeZHEbhHKcPmdPyHE2JGYtn0bZ9ZQxThgyda9zI0k8C2kd2mExA8W4Fp0zzVpnKHVlmrU2uJGfTbpedSXL31Dih4CbbCr38j6l8qXaSkLS07MmtohtGC/wAx8FhFC7LeoyGO8A0x1a76vgvrMSK9D++HAQfPsnAgMBAAECgYBnlCWANL/e5ogtLG5oi3M/ua6W2XObgNu5XyA6K8wKkH5K0iw0pJNgU1vjQKiVl+F88QEfH9Ny3JOazLJy5uGcuVEqTA4KHP2PqOoPlkgzvxhXQYnI/YD0W6WYbyO8wvxiJyoaFEiGZRrlAt63e1puiIJXZeIvVC3gMXRkKjXNMQJBAOiAUd35+E48dgqsxq6o8fKQAoWEvQhLT3bGkndPW41HoQYKjcoikxUugoM/9XYeH/hCBIBit8G8JMQ9xVvyNJ8CQQDP4T19AuXtZOf4FrOg2bxrMTCSXt5EMbP65ijGf8aPyrY0lGpeQiii59DKT7ZzD+J45McQ44J03/7TiOEZDGR5AkEAmL1kCv3i4BLcQVsME0Yt3Ho7DdgMD1zaUV9WbUcXEBNUd5GLYJWiJrItT2g/K1/TBNbp+iXgLkgZp0olU6gkZQJBAIYElUqh8q/wjOhRYm8B0MpehQzoYm0eigToG0OVnuKW8o7FXCn2hxI3V1EGwP4/MGd2PqwxsKo+up+PsGcgqSkCQEXKmhIr+n/oodL6fmKuAGLf6hJB2gO9IjBdyfw+bQRISWWQ/2F5JDlSaCMGc9bJCAZ/CJM7K15nuOfkF1J4iEw=  \n', 'cb797b3efc5a12174fa2d83e00980591', '2022-09-20 06:50:12', '2022-09-20 06:50:12', NULL, '125.47.75.214', '', '', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (94, 'application-unionreal-env.yml', 'DEFAULT_GROUP', 'union:\n  real:\n    appId: 8a81c1bf81ee18410182247562170255\n    appKey: b711120b49aa4325a26a1866951beae4\n    priKey: 00e3406c43cd6359f1c0ecf0fc7bccb9056d699c20bd570217a933a31079de35ec  \n    url: https://api-mop.chinaums.com/v1/datacenter/smartverification/encrypted/bankcard/verify\n    pubKey: 0414bb7e9c3914bb65b85079b1dc6e1ca2a0ee04dd9bb55b2f8d31704a6dec0cca695739e128f9c931330e1c0f493be4d56244236434c9d344c4716ba64a4470ba', 'cf3ad56164236cdaab0466e941c41b63', '2022-09-20 06:50:12', '2022-09-20 06:50:12', NULL, '125.47.75.214', '', '', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (96, 'manyun-user-env.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: manyun\n        loginPassword: manyun2022@\n        allow: \n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://rm-2ze5st53a9v36x73xgo.mysql.rds.aliyuncs.com:3306/manyun-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: manyun\n            password: Manyun2022@\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      manyun-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server}\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server}\n      namespace:\n\nmybatis-plus:\n  mapper-locations:\n  - classpath:mapper/**/*.xml\n  - classpath*:com/manyun/comm/*.xml\n  type-aliases-package: com.manyun.user.domain\n  global-config:\n    banner: false\n\n# swagger配置\nswagger:\n  title: 移动端用户接口文档\n  license: Powered By yanwei\n  licenseUrl: xxx', '9b5bd18910fb10f28d98be02fe3b19dd', '2022-09-20 06:52:28', '2022-10-14 10:42:16', 'manyun', '219.157.186.102', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (98, 'manyun-business-env.yml', 'DEFAULT_GROUP', '# spring配置\nspring: \n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: manyun\n        loginPassword: manyun2022@\n        allow: \n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://rm-2ze5st53a9v36x73xgo.mysql.rds.aliyuncs.com:3306/manyun-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: manyun\n            password: Manyun2022@\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n      # seata: true    # 开启seata代理，开启后默认每个数据源都代理，如果某个不需要代理可单独关闭\n\n# seata配置\nseata:\n  # 默认关闭，如需启用spring.datasource.dynami.seata需要同时开启\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      manyun-system-group: default\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server}\n      group: SEATA_GROUP\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server}\n      namespace:\n\nmybatis-plus:\n  mapper-locations:\n  - classpath:mapper/**/*.xml\n  - classpath*:com/manyun/comm/*.xml\n  type-aliases-package: com.manyun.business.domain\n  global-config:\n    banner: false\n\n# swagger配置\nswagger:\n  title: 移动端用户接口文档\n  license: Powered By yanwei\n  licenseUrl: xxx\n  basePackage: com.manyun\n  enabled: true\n\nmychain:\n  publickey: MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGXtQu4fDLbR6er4Dq8ObaOuy3Sb2TI31hqbmKe4074ZdSP9yYmfOj8MIbZdryjeFAw8LV1uvrNp+gDyqsxUz3JsjXbtjA/1m1DjxNbkzhG3xWibGE69tjZDvInMhUOMT0f6o1sMkmOdfpsXLy/De0ylZLRj3njiQnkMswAaRSzwIDAQAB\n  linkHttp: http://172.21.240.40:8888\n  projectName: manyun_soft', '5683d6e7ca721fe162a80cf98696dff7', '2022-09-20 06:53:44', '2022-10-14 10:42:32', 'manyun', '219.157.186.102', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (126, 'sentinel-flow.yml', 'DEFAULT_GROUP', 'sentinel:\r\n  flow:\r\n    flowNodeLists:\r\n     - sourceName: manyun-business_apis\r\n       pathUrl:\r\n        - /business/collection/sellOrderCollection\r\n        - /business/collection/pageList\r\n     - sourceName: manyun-users_apis\r\n       pathUrl:\r\n         - /user/cntUser/login\r\n         - /user/cntUser/changeUser', '6681cae941c6e3c866d04b3a3a8c3abc', '2022-09-26 06:06:02', '2022-09-26 06:06:02', NULL, '125.41.205.222', '', '', '流控细颗粒管理', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (131, 'application-mq-env.yml', 'DEFAULT_GROUP', 'rocketmq:\n  name-server: 172.21.240.40:9876\n  producer:\n    group: ${spring.application.name}\n    max-message-size: 2097152  # 2m 消息量\n    access-key: ${spring.application.name}-access\n    secret-key: ${spring.application.name}-secret\n    enable-msg-trace: true\n    customized-trace-topic: ${spring.application.name}-trace-topic\n  access-channel: LOCAL\n  consumer:\n    access-key: ${spring.application.name}-access\n    secret-key: ${spring.application.name}-secret\n    enable-msg-trace: true\n    customized-trace-topic: ${spring.application.name}-trace-topic\nmqconfig:\n  delivers:\n   - topic: delivers_bui\n     tars: \n     - tar-name: cancelOrder\n       tar-value: cancelOrder\n     - tar-name: tarOpen\n       tar-value: tarOpen', 'ac71f7f66fc672a0655444cb55418894', '2022-10-12 08:05:38', '2022-10-12 08:10:06', 'manyun', '125.47.74.14', '', '', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id`, `group_id`, `tenant_id`, `datum_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 314 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (83, 312, 'manyun-gateway-env.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  cloud:\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: manyun-auth\n          uri: lb://manyun-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        #- id: manyun-gen\n        #  uri: lb://manyun-gen\n        #  predicates:\n        #    - Path=/code/**\n        #  filters:\n        #    - StripPrefix=1\n        # 定时任务\n        #- id: manyun-job\n        #  uri: lb://manyun-job\n        #  predicates:\n        #    - Path=/schedule/**\n        #  filters:\n        #    - StripPrefix=1\n        # admin 相关apis\n        - id: manyun-admin\n          uri: lb://manyun-admin\n          predicates:\n            - Path=/admin/**\n          filters:\n            - StripPrefix=1\n            # 演示相关\n        - id: manyun-demo\n          uri: lb://manyun-demo\n          predicates:\n            - Path=/web/**\n          filters:\n            - StripPrefix=1            \n        # 基础能力相关apis\n        - id: manyun-base\n          uri: lb://manyun-base\n          predicates:\n            - Path=/base/**\n          filters:\n            - StripPrefix=1\n          # 用户能力相关apis\n        - id: manyun-user\n          uri: lb://manyun-user\n          predicates:\n            - Path=/user/**\n          filters:\n            - StripPrefix=1\n          # 业务相关apis\n        - id: manyun-business\n          uri: lb://manyun-business\n          predicates:\n            - Path=/business/**\n          filters:\n            - StripPrefix=1            \n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: false\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n        # 管理员\n    #  - /admin/**\n    # 基础能力 \n      - /base/sendPhone/**\n      - /base/sendPhoneToken/**\n      - /base/upload\n      - /auth/user/phpLogin\n    # auth 相关\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /auth/user/login\n      - /auth/user/phpLogin\n      - /auth/user/loginRSA\n      - /auth/user/codeLogin\n      - /auth/user/jgAuthPhoneLogin\n      # demo 相关\n      - /web/demo/tbBui/**\n      #business相关\n      - /business/cntNotice/**/**\n      - /business/msg/pageMsgList\n      - /business/cate/**/**\n      - /business/banner/list/**\n      - /business/collection/homeCacheList\n      - /business/box/homeCacheList\n      - /business/collection/pageList\n      - /business/collection/info/**\n      - /business/collection/queryDict/**\n      - /business/box/pageList\n      - /business/box/queryDict/**\n      - /business/box/info/**\n      - /business/system/sellInfo\n      - /business/system/collectionInfo\n      - /business/system/findType/**\n      - /business/announcement/list\n      - /business/agreement/info/*\n      - /business/actionTar/test\n      - /business/tar/**\n      - /business/notify_pay/ShandePay/**\n      - /business/notify_pay/LlPay/**\n    #  - /business/consignment/pageConsignmentCollectionList\n      - /business/version/**\n      # 用户相关\n      - /user/cntUser/regUser\n      - /user/notify_real/real\n      - /user/cntUser/payEncrypt/*\n      - /user/real/**\n\n      # 公众层相关 \n      - /*/v2/api-docs\n      - /csrf', '2c3b621deffe263d800bce57962b9f75', '2022-11-03 16:24:06', '2022-11-03 08:24:07', 'manyun', '1.192.59.76', 'U', '', '');
INSERT INTO `his_config_info` VALUES (83, 313, 'manyun-gateway-env.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: r-2zehwcylujx9woddobpd.redis.rds.aliyuncs.com\n    port: 6379\n    database: 8\n    password: Manyun2022@\n  cloud:\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowedOriginPatterns: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n            allow-credentials: true\n            exposedHeaders: \"Content-Disposition,Content-Type,Cache-Control\"\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: manyun-auth\n          uri: lb://manyun-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        #- id: manyun-gen\n        #  uri: lb://manyun-gen\n        #  predicates:\n        #    - Path=/code/**\n        #  filters:\n        #    - StripPrefix=1\n        # 定时任务\n        #- id: manyun-job\n        #  uri: lb://manyun-job\n        #  predicates:\n        #    - Path=/schedule/**\n        #  filters:\n        #    - StripPrefix=1\n        # admin 相关apis\n        - id: manyun-admin\n          uri: lb://manyun-admin\n          predicates:\n            - Path=/admin/**\n          filters:\n            - StripPrefix=1\n            # 演示相关\n        - id: manyun-demo\n          uri: lb://manyun-demo\n          predicates:\n            - Path=/web/**\n          filters:\n            - StripPrefix=1            \n        # 基础能力相关apis\n        - id: manyun-base\n          uri: lb://manyun-base\n          predicates:\n            - Path=/base/**\n          filters:\n            - StripPrefix=1\n          # 用户能力相关apis\n        - id: manyun-user\n          uri: lb://manyun-user\n          predicates:\n            - Path=/user/**\n          filters:\n            - StripPrefix=1\n          # 业务相关apis\n        - id: manyun-business\n          uri: lb://manyun-business\n          predicates:\n            - Path=/business/**\n          filters:\n            - StripPrefix=1            \n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: false\n    excludeUrls:\n      - /system/notice\n  # 不校验白名单\n  ignore:\n    whites:\n        # 管理员\n    #  - /admin/**\n    # 基础能力 \n      - /base/sendPhone/**\n      - /base/sendPhoneToken/**\n      - /base/upload\n      - /auth/user/phpLogin\n    # auth 相关\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /auth/user/login\n      - /auth/user/phpLogin\n      - /auth/user/loginRSA\n      - /auth/user/codeLogin\n      - /auth/user/jgAuthPhoneLogin\n      # demo 相关\n      - /web/demo/tbBui/**\n      #business相关\n      - /business/cntNotice/**/**\n      - /business/msg/pageMsgList\n      - /business/cate/**/**\n      - /business/banner/list/**\n      - /business/collection/homeCacheList\n      - /business/box/homeCacheList\n      - /business/collection/pageList\n      - /business/collection/info/**\n      - /business/collection/queryDict/**\n      - /business/box/pageList\n      - /business/box/queryDict/**\n      - /business/box/info/**\n      - /business/system/sellInfo\n      - /business/system/collectionInfo\n      - /business/system/findType/**\n      - /business/announcement/list\n      - /business/agreement/info/*\n      - /business/actionTar/test\n      - /business/tar/**\n      - /business/notify_pay/ShandePay/**\n      - /business/notify_pay/LlPay/**\n      - /business/consignment/openConsignmentList\n    #  - /business/consignment/pageConsignmentCollectionList\n      - /business/version/**\n      # 用户相关\n      - /user/cntUser/regUser\n      - /user/notify_real/real\n      - /user/cntUser/payEncrypt/*\n      - /user/real/**\n\n      # 公众层相关 \n      - /*/v2/api-docs\n      - /csrf', '39f65e540ee509c805ef82046a81c23a', '2022-11-05 11:53:09', '2022-11-05 03:53:09', 'manyun', '115.60.150.227', 'U', '', '');

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `action` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'd4933111-bd4c-4c44-9d02-4a1c7ccfb28d', 'SENTINEL_SECURITY', 'sentinel的流量布控相关配置', 'nacos', 1655196730491, 1655196730491);

-- ----------------------------
-- Table structure for test_innodb_lock
-- ----------------------------
DROP TABLE IF EXISTS `test_innodb_lock`;
CREATE TABLE `test_innodb_lock`  (
  `id` int(11) NULL DEFAULT NULL,
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `idx_test_innodb_lock_id`(`id`) USING BTREE,
  INDEX `idx_test_innodb_lock_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of test_innodb_lock
-- ----------------------------
INSERT INTO `test_innodb_lock` VALUES (1, '100', '1');
INSERT INTO `test_innodb_lock` VALUES (3, '3', '0');
INSERT INTO `test_innodb_lock` VALUES (4, '400', '2');
INSERT INTO `test_innodb_lock` VALUES (5, '500', '1');
INSERT INTO `test_innodb_lock` VALUES (6, '600', '0');
INSERT INTO `test_innodb_lock` VALUES (7, '700', '0');
INSERT INTO `test_innodb_lock` VALUES (8, '800', '1');
INSERT INTO `test_innodb_lock` VALUES (9, '900', '1');
INSERT INTO `test_innodb_lock` VALUES (1, '200', '0');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('manyun', '$2a$10$ibdJmCFcG5M0XhCgegweVe3Hj3qW.6y3Kw7PVGSOXmrU9WizReh.a', 1);
INSERT INTO `users` VALUES ('nacos', '$2a$10$R8IrVIwebdoSzRfVZFRndO3Yjgk6oblI41i1YrGuqzhgmO8yp9kFO', 1);
INSERT INTO `users` VALUES ('nacos_admin', '$2a$10$GB.w81ySGhC32LAZyTtLZu4Qxy29sktrpMGK6YkbtdnZ35nqnnm7i', 1);

SET FOREIGN_KEY_CHECKS = 1;
