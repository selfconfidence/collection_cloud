package com.manyun.business.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.manyun.business.domain.entity.CntPostConfig;
import com.manyun.business.domain.entity.CntPostExist;
import com.manyun.business.domain.entity.CntPostSell;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.mapper.TbPostConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.manyun.business.service.ICntPostConfigService;
import com.manyun.business.service.ICntPostExistService;
import com.manyun.business.service.ICntPostSellService;
import com.manyun.business.service.IUserCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 提前购配置表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@Service
public class TbPostConfigServiceImpl extends ServiceImpl<TbPostConfigMapper, CntPostConfig> implements ICntPostConfigService {

    /**
     * 已经拥有的
     */
    @Autowired
    private ICntPostExistService postExistService;


    /**
     * 能购买的
     */
    @Autowired
    private ICntPostSellService postSellService;

    @Autowired
    private IUserCollectionService userCollectionService;


    /**
     * 是否可以提前购
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public boolean isConfigPostCustomer(String userId, String buiId) {
        //1.查询 配置购买表中 有没有这个  buiId
        // 1.1 有的话，就查 拥有表中的藏品和目前用户拥有的藏品进行比较,存在 返回true ; 否则 false
        List<CntPostSell> tbPostSells = postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
        if (!tbPostSells.isEmpty()){
            // 此接口可以进行特别优化
            List<CntPostExist> tbPostExists = postExistService.list();
            List<UserCollection> userCollections = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).in(UserCollection::getCollectionId, tbPostExists.stream().map(item -> item.getCollectionId()).collect(Collectors.toSet())));
            return !userCollections.isEmpty();
        }
        // 1.2 没有的话，直接返回 false
        return Boolean.FALSE;
    }

}
