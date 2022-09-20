package com.manyun.common.redis.configure;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RLock;

@Getter
@Setter
public class LockResult {

    private LockResultStatus lockResultStatus;

    private RLock rLock;
}
