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


        // 钱包业务模块类型
        String MONEY_TYPE= "money_model_type";

        // 盲盒业务 标识码
        Integer BOX_TAYPE = Integer.valueOf(1);
        // 藏品业务 标识码
        Integer COLLECTION_TAYPE = Integer.valueOf(0);
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
         * 寄售手续费
         */
        String  CONSIGNMENT_SERVER_CHARGE = "consignment_server_chage";

        /**
         * 寄售订单到期时间
         */
        String  CONSIGNMENT_ORDER_TIME =  "consignment_order_time";


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
         * 保证金比例
         */
        String MARGIN_SCALE = "margin_scale";

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
        Integer EXP_TIME = Integer.valueOf(60);
    }


}
