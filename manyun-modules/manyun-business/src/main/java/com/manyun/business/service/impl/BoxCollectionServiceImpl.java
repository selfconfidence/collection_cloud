package com.manyun.business.service.impl;

import com.manyun.business.domain.entity.BoxCollection;
import com.manyun.business.mapper.BoxCollectionMapper;
import com.manyun.business.service.IBoxCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盲盒与藏品中间表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class BoxCollectionServiceImpl extends ServiceImpl<BoxCollectionMapper, BoxCollection> implements IBoxCollectionService {

}
