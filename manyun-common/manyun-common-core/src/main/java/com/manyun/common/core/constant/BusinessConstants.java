package com.manyun.common.core.constant;

/**
 * 业务常量类
 */
public interface BusinessConstants {


    /**
     * 全局modelType 常量类型
     */
    interface ModelTypeConstant{
        // 盲盒业务模块类型
        String BOX_MODEL_TYPE= "box_model_type";
        // 藏品业务模块类型
        String COLLECTION_MODEL_TYPE= "collection_model_type";

        // 媒体类型
        //3D图
        String GLB = "glb";

        //缩略图
        String THUMBNAIL_IMG = "thumbnail_img";

        // 钱包业务模块类型
        String MONEY_TYPE= "money_model_type";

        // 连连钱包业务模块类型
        String LL_MONEY_MODEL_TYPE= "ll_money_model_type";

        // 盲盒业务 标识码
        Integer BOX_TAYPE = Integer.valueOf(1);
        // 藏品业务 标识码
        Integer COLLECTION_TAYPE = Integer.valueOf(0);

        //分页默认顶级分类
        String CATE_PARENT_ID = "0";
    }

    /**
     * 系统编号常量类
     */
    interface SystemTypeConstant{

        /**
         *  订单到期时间
         */
        String ORDER_END_TIME= "ORDER_PAY_END";

        /**
         * 是否开启转增
         */
        String TRAN_ACC= "TRAN_ACC";

        /**
         * 转赠说明
         */
        String TRAN_INFO= "TRAN_INFO";

        /**
         * 寄售手续费
         */
        String  CONSIGNMENT_SERVER_CHARGE = "consignment_server_chage";

        /**
         * 寄售订单到期时间
         */
        String  CONSIGNMENT_ORDER_TIME =  "consignment_order_time";


        /**
         *  寄售订单超时时间 小时为单位
         */
        String  CONSIGNMENT_DE_TIME = "CONSIGNMENT_DE_TIME";

        /**
         * 是否开启拍卖（1开启，0关闭）
         */
        String AUCTION_ACC = "auction_acc";

        /**
         * 被邀请人是否购买过此商品
         */
        String INVITEPEOPLE_ISBUY_GOODS = "invitePeople_isBuy_goods";

        /**
         * 拍卖出售须知
         */
        String AUCTION_SELL_INFO = "auction_sell_info";

        /**
         * 拍卖订单到期时间
         */
        String AUCTION_ORDER_TIME = "auction_order_time";

        /**
         * 拍卖预售时间
         */
        String AUCTION_PRE_TIME = "auction_pre_time";

        /**
         * 拍卖时间
         */
        String AUCTION_BID_TIME = "auction_bid_time";

        /**
         * 延拍时间
         */
        String AUCTION_DELAY_TIME = "auction_delay_time";

        /**
         * 拍卖加价幅度
         */
        String AUCTION_PRICE_RANGE = "auction_price_range";

        /**
         * 保证金说明
         */
        String MARGIN_INFO = "margin_info";

        /**
         * 保证金比例
         */
        String MARGIN_SCALE = "margin_scale";

        /**
         * 佣金说明
         */
        String COMMISSION_INFO = "commission_info";

        /**
         * 佣金比例
         */
        String COMMISSION_SCALE = "commission_scale";

        /**
         * 购买须知
         */
        String  SELL_INFO =  "SELL_INFO";

        /**
         * 关于藏品
         */
        String  COLLECTION_INFO =  "COLLECTION_INFO";

        /**
         *  用户默认头像地址
         */
        String  USER_DEFAULT_LINK = "USER_DEFAULT_LINK";


        /**
         * 藏品编号生成位数
         */
        String COLLECTION_POT = "COLLECTION_POT";


        /**
         * 邀请海报链接
         */
        String INVITE_URL = "invite_url";

        /**
         * 注册链接
         */
        String REG_URL = "reg_url";

        /**
         * 网关地址
         */
        String GATEWAY_URL = "gateway_url";

        /**
         * 上传图片本地保存地址
         */
        String LOCAL_PATH = "local_path";

        /**
         * 开盲盒动图
         */
        String OPEN_BOX_GIF = "open_box_gif";

        /**
         * 合成动图
         */
        String SYNTHESIS_GIF = "synthesis_gif";

        /**
         * 连连余额提现规则
         */
        String WITHDRAW_INFO = "withdraw_info";

        /**
         * 平台余额提现规则
         */
        String SYSTEM_WITHDRAW_INFO = "system_withdraw_info";

        /**
         * 提现手续费比例
         */
        String WITHDRAW_CHARGE = "withdraw_charge";

        /**
         * 平台提现手续费
         */
        String SYSTEM_WITHDRAW_CHARGE = "system_withdraw_charge";

        /**
         * 寄售须知
         */
        String CONSIGNMENT_INFO = "consignment_info";

        /**
         *查询用户几级团队总人数
         */
        String USER_TERM_NUMBERS = "user_term_numbers";

        /**
         *查询用户几级团队总人数
         */
        String IS_REAL = "is_real";
    }


    /**
     * 通用日志相关
     * See Also: ModelTypeConstant
     */
    interface LogsTypeConstant{

        /**
         * (钱包/支出)
         * (盲盒/转赠)
         * (藏品/转赠)
         */
        Integer POLL_SOURCE = Integer.valueOf(1);


        /**
         * (钱包/收入)
         * (盲盒/购入)
         * (藏品/购入)
         */
        Integer PULL_SOURCE = Integer.valueOf(0);
    }



    interface RedisDict{
        String PHONE_CODE = "phone:code:";
        // 验证码过期时间 60 秒
        Integer EXP_TIME = Integer.valueOf(300);
        // 持续递增编号key
        String COLLECTION_AUTO_NUM = "collection:auto:numbers:";
        String  USER_ACTIVE_NUMBERS = "user:active:numbers";
        String COLLECTION_RANDOM_NUM = "collection:random:numbers:";
        String COLLECTION_ACTIVE_NUMBERS = "collection:inactive:numbers:";
        Integer COLLECTION_ACTIVE_EXPIRE = 1; // 一秒内
        Integer OPEN_INTERFACE_STOP = 6; // 停止时间 如果在一秒内 超过了访问次数 就会封禁 6秒 不可访问当前接口。
        Integer OPEN_MAX_URL = 3; // 同一接口访问次数
        String COLLECTION_ACTIVE_NUMBERS_ADD_LUA = "if redis.call('HINCRBY',KEYS[1],ARGV[1],1) >= 1 then\n" +
                "    redis.call('EXPIRE',KEYS[1],ARGV[2])\n" +
                "end";
        String COLLECTION_ACTIVE_NUMBERS_REL_LUA = "if redis.call('HINCRBY',KEYS[1],ARGV[1],-1) <= 0 then\n" +
                "    redis.call('HDEL',KEYS[1],ARGV[1])\n" +
                "end";

        // 业务系列
        // 创作者
        String CREATED_BUI_CACHE = "created:bui:cache:";

        // 发行方
        String PUBLISH_BUI_CACHE = "publish:bui:cache:";

        // 库存系列 hash 数据结构

        // 藏品 大KEY
        String COLLECTION_POINT_BUI = "collection:point:bui:";

        // 盲盒 大KEY
        String BOX_POINT_BUI = "box:point:bui:";

        // 藏品详情 大KEY
        String COLLECTION_INFO = "collection:info:";

        // 藏品详情 大KEY
        String BOX_INFO = "box:info:";

       //已售
        String SELFBALANCE = "selfBalance";
        //余量
        String BALANCE = "balance";

        // 普通订单支付状态返回值
        String ORDER_ORDINARY_STATUS = "order:ordinary:status:";


    }

    interface CommDict{
        String REAL_COMPANY = "阿里提供技术支持";
    }


    interface UserDict{
        //（0正常 1停用）
        String USER_ON = "0";
        String USER_OFF = "1";
    }

    /**
     * 短信通知
     */
    interface SmsTemplateNumber{

        //您寄售的 ${buiName} 到时间被下架了,注意查收!
        String ASSERT_BACK = "SMS_249815543";

        //您的 ${buiName} 已经成功寄售!
        String ASSERT_UP = "SMS_249960533";

        //您的藏品已被用户买入啦！收入${money}元将在${day}天后到账。开启全新的艺术收藏之旅，就在CNT。
        //String ASSERT_SUCCESS = "SMS_249970552";

       // 您的寄售资产已被用户买入啦！后台审核后即将到款。开启全新的艺术收藏之旅，就在CNT。
        String ASSERT_OK = "SMS_250450009";


        //String ASSERT_SUCCESS = "SMS_250320015";
        String ASSERT_SUCCESS = "SMS_250435282";

       //您已经参与了${buiName},结果将在${openTime}公布,尽请期待!
        String TAR_MSG = "SMS_250921176";

        //您的活动结果已经公布,已成功参与,快去参与吧!
        String TAR_SUCCESS = "SMS_250951390";

        // 您活动结果已经公布,未能参与成功,再接再厉!
        String TAR_FAIL = "SMS_250961367";

    }

    interface JgPushConstant{
        String PUSH_TITLE = "CNT";
    }


}
