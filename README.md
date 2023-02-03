
## 平台简介
* 采用前后端分离的模式，微服务版本 给予springCloud Alibaba
* 后端采用Spring Boot、Spring Cloud & Alibaba。
* 注册中心、配置中心选型Nacos，权限认证使用Redis + JWT。
* 流量控制框架选型Sentinel，分布式事务选型Seata。
* 解耦采用 RoketMq
* 分布式日志链路采集 使用 skyWalking 
## 系统模块
~~~
com.manyun     
├── manyun-ui              // 前端框架 [80]
├── manyun-gateway         // 网关模块 [8080]
├── manyun-auth            // 认证中心 [9200]
├── manyun-api             // 接口模块 全部fegin 采用中间件服务的方式,不能服务直接直接调用。应采用统一api 管理方式
│       └── manyun-api-system                          // 系统接口
├── manyun-common          // 通用模块
│       └── manyun-common-core                          // 核心模块
│       └── manyun-common-datascope                    // 权限范围
│       └── manyun-common-datasource                   // 多数据源
│       └── manyun-common-log                          // 日志记录
│       └── manyun-common-redis                        // 缓存服务
│       └── manyun-common-security                     // 安全模块
│       └── manyun-common-swagger                      // 系统接口
├── manyun-modules         // 业务模块 创建新模块按照一下顺序顺延即可
│       └── manyun-admin                               // 后台管理模块 [9201]                 
│       └── manyun-base                                 // 基础服务 [9202]
        └── manyun-demo                                // 样本 [9203]
├── manyun-visual          // 图形化管理模块                // 不常用
│       └── manyun-visual-monitor                      // 监控中心 [9100]
├──pom.xml                // 公共依赖
~~~


## 内置功能
1. 定时任务：在线（添加、修改、删除)任务调度包含执行结果日志。
2. 代码生成：前后端代码的生成（java、html、xml、sql）支持CRUD下载 。
3. 系统接口：根据业务代码自动生成相关的api接口文档。
4. 服务监控：监视当前系统CPU、内存、磁盘、堆栈等相关信息。
5. 连接池监视：监视当前系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。
                                 - yanwei
## 说明文档
https://www.yuque.com/docs/share/2d0cca3c-2592-4181-8bf5-d0eef3687fc3?# 《技术栈调研》
https://www.yuque.com/docs/share/00351490-c051-4f9c-97a8-3013a3dac178?# 《服务说明手册》

## 项目功能
参考 同级别目录下的pdf文档

## 联系方式
邮箱 forcontinue@163.com

## 项目来源
功能自研，22年疫情原因项目被迫关停,原团队人员进行对原有项目重构，合约的重构，形成有二级市场的数字藏品平台,支持3D展馆 ，obj图等。有需要请发邮箱联系开发人员。创作不易。