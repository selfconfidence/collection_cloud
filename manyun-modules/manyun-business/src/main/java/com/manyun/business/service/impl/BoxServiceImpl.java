package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.Box;
import com.manyun.business.mapper.BoxMapper;
import com.manyun.business.service.IBoxService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盲盒;盲盒主体表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class BoxServiceImpl extends ServiceImpl<BoxMapper, Box> implements IBoxService {

}
