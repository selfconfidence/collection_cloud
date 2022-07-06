package com.manyun.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.common.core.exception.base.BaseException;
import com.manyun.demo.domain.TbBui;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

/**
 * <p>
 * 测试表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-14
 */
public interface ITbBuiService extends IService<TbBui> {

    void tran();


    //默认情况下，会重试3次，间隔1秒
    // maxAttempts 重试次数
    @Retryable(maxAttempts = 5,value = BaseException.class,backoff = @Backoff(multiplier = 2,value = 2000))
    void reload();
}
