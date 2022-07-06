package com.manyun.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.exception.base.BaseException;
import com.manyun.demo.domain.TbBui;
import com.manyun.demo.mapper.TbBuiMapper;
import com.manyun.demo.service.ITbBuiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-14
 */
@Service
@Slf4j
public class TbBuiServiceImpl extends ServiceImpl<TbBuiMapper, TbBui> implements ITbBuiService {

    @Override
    public void tran() {
       // reload();
    }

    //默认情况下，会重试3次，间隔1秒
    // maxAttempts 重试次数
    //value 遇到此异常才会重试
    // Backoff multiplier 策略倍数, 每次 2秒 * 这个数, 相当于 第一次 执行后, 2n 次方相乘
    //        value  间隔 时间
    @Retryable(maxAttempts = 5,value = BaseException.class,backoff = @Backoff(multiplier = 2,value = 2000))
    @Override
    public void reload(){
     log.info("reload");
       throw new BaseException("业务错误！！！");
    }

    @Recover
    public void recoverReload(BaseException exception){
        log.info("重试结束{}",exception.getDefaultMessage());
    }
}
