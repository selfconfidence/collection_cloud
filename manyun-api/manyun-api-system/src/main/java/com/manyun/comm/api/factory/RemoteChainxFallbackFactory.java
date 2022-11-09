package com.manyun.comm.api.factory;

import com.manyun.comm.api.MyChainxSystemService;
import com.manyun.comm.api.RemoteSystemService;
import com.manyun.comm.api.domain.dto.CallAccountDto;
import com.manyun.comm.api.domain.vo.ChainAccountVo;
import com.manyun.common.core.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 业务系统配置参数服务降级处理
 * 
 * @author yanwei
 */
@Component
public class RemoteChainxFallbackFactory implements FallbackFactory<MyChainxSystemService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteChainxFallbackFactory.class);

    @Override
    public MyChainxSystemService create(Throwable throwable)
    {
        log.error("业务系统参数配置服务调用失败:{}", throwable.getMessage());
        return new MyChainxSystemService() {
            @Override
            public R resetUpLink(String userId, String userCollectionId, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }

            @Override
            public R<String> accountCreate(CallAccountDto callAccountDto, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }

            @Override
            public R<ChainAccountVo> createInit(String account, String source) {
                return R.fail("操作失败:" + throwable.getMessage());
            }

        };
    }
}
