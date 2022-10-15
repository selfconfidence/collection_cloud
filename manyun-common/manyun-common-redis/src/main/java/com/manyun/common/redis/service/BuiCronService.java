package com.manyun.common.redis.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.redis.domian.dto.BuiCronDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.*;

/*
 * 藏品盲盒库存缓存业务服务
 *
 * @author yanwei
 * @create 2022-10-07
 */
@Component
public class BuiCronService {
    @Autowired
    private RedisTemplate redisTemplate;


    // 加库存
    private final static String ICREMENT_LUA = "local bui_num = tonumber(ARGV[1]);\n" +
            "if redis.call('HEXISTS',KEYS[1],'balance') == 0 or bui_num == 0 then\n" +
            "    return nil;\n" +
            "elseif tonumber(redis.call('hget',KEYS[1],'selfBalance')) - bui_num >= 0    then\n" +
            "    redis.call('HINCRBY',KEYS[1],'balance',bui_num);\n" +
            "    redis.call('HINCRBY',KEYS[1],'selfBalance',-bui_num);\n" +
            "    return 1;\n" +
            "else\n" +
            "    return 0;\n" +
            "end";

    // 减库存
    private final static String DCREMENT_LUA = "local bui_num = tonumber(ARGV[1]);\n" +
            "if redis.call('HEXISTS',KEYS[1],'balance') == 0 or bui_num == 0 then\n" +
            "    return nil;\n" +
            "elseif tonumber(redis.call('hget',KEYS[1],'balance')) >= bui_num    then\n" +
            "    redis.call('HINCRBY',KEYS[1],'balance',-bui_num);\n" +
            "    redis.call('HINCRBY',KEYS[1],'selfBalance',bui_num);\n" +
            "    return 1;\n" +
            "else\n" +
            "    return 0;\n" +
            "end";

    /**
     * @param type
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#BOX_MODEL_TYPE
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#COLLECTION_MODEL_TYPE
     * @param id
     * @return 获取藏品盲盒库存 已售
     */
    public BuiCronDto getTypeBalanceCache(String type,String id){
       String bigKey =  doCaseKey(type);
       return doGetBalanceCache(bigKey,id );
    }
    /**
     * @param type
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#BOX_MODEL_TYPE
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#COLLECTION_MODEL_TYPE
     * @param id
     * @return 获取藏品盲盒库存 已售
     */
    public void initBuiBalanceCache(String type,String id,Integer balance,Integer selfBalance){
        String bigKey =  doCaseKey(type);
        doInitBuiCache(bigKey, id, balance, selfBalance);
    }

    /**
     * @param type
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#BOX_MODEL_TYPE
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#COLLECTION_MODEL_TYPE
     * @param id
         * 对指定盲盒藏品库存进行加
           * true 正常
           * false 已售在扣就负了....
     **/
    public boolean doBuiIcrementBalanceCache(String type,String id,Integer balance){
        String bigKey =  doCaseKey(type);
        return doIcrement(bigKey, id, balance);
    }

    /**
     * @param type
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#BOX_MODEL_TYPE
     *             @see com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant#COLLECTION_MODEL_TYPE
     * @param id
     * 对指定盲盒藏品库存进行减
     * true 正常
     * false 库存不足
     **/
    public boolean doBuiDegressionBalanceCache(String type,String id,Integer balance){
        String bigKey =  doCaseKey(type);
        return doDegression(bigKey, id, balance);
    }

    private String doCaseKey(String type) {
        String bigKey =  BOX_MODEL_TYPE.equals(type) ? BOX_POINT_BUI : COLLECTION_MODEL_TYPE.equals(type) ? COLLECTION_POINT_BUI : null ;
        Assert.isTrue(StrUtil.isNotBlank(bigKey),"类型错误,请核实传递参数!");
        return bigKey;
    }

    /**
     * 获取藏品盲盒库存 已售
     */
    private BuiCronDto  doGetBalanceCache(String bigKey,String id){
        return Builder.of(BuiCronDto::new)
                .with(BuiCronDto::setBalance, Convert.convert(Integer.class, redisTemplate.boundHashOps(bigKey.concat(id)).get(BALANCE),0))
                .with(BuiCronDto::setSelfBalance,Convert.convert(Integer.class,  redisTemplate.boundHashOps(bigKey.concat(id)).get(SELFBALANCE),0))
                .build();
    }


    /**
     * 初始化藏品盲盒的库存
     */
    private void doInitBuiCache(String bigKey,String id,Integer balance,Integer selfBalance){
        redisTemplate.boundHashOps(bigKey.concat(id)).put(BALANCE,balance);
        redisTemplate.boundHashOps(bigKey.concat(id)).put(SELFBALANCE,selfBalance);
    }


    /**
     * 对指定盲盒藏品库存进行加
     * true 正常
     * false 已售在扣就负了....
     */
    private boolean doIcrement(String bigKey,String id,Integer balance){
        Object result = redisTemplate.execute(new DefaultRedisScript<Long>(ICREMENT_LUA, Long.class), Arrays.asList(bigKey.concat(id)), balance);
        Assert.isTrue(Objects.nonNull(result),"当前资产库存不存在,请核实!");
        return Long.valueOf(1).equals(Convert.convert(Long.class, result));
    }


    /**
     * 对指定盲盒藏品库存进行减
     * true 正常
     * false 库存不足
     */
    private boolean doDegression(String bigKey,String id,Integer balance){
        Object result = redisTemplate.execute(new DefaultRedisScript<Long>(DCREMENT_LUA, Long.class), Arrays.asList(bigKey.concat(id)), balance);
        Assert.isTrue(Objects.nonNull(result),"当前资产库存不存在,请核实!");
        return Long.valueOf(1).equals(Convert.convert(Long.class, result));
    }

}
