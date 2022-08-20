package com.manyun.business.service.impl;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.manyun.business.domain.entity.*;
import com.manyun.business.mapper.TbPostConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.manyun.business.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

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
//    @Autowired
//    private ICntPostSellService postSellService;

/*    @Autowired
    private ICntPostConfigService postConfigService;*/

    @Autowired
    private IUserCollectionService userCollectionService;


    @Autowired
    private IUserBoxService userBoxService;


    /**
     * 是否可以提前购 藏品
     * @param userId
     * @param buiId
     * @return
     */
//    @Override
//    public boolean isConfigPostCustomer(String userId, String buiId) {
//        //1.查询 配置购买表中 有没有这个  buiId
//        // 1.1 有的话，就查 拥有表中的藏品和目前用户拥有的藏品进行比较,存在 返回true ; 否则 false
//        List<CntPostSell> tbPostSells = postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
//        if (!tbPostSells.isEmpty()){
//            // 此接口可以进行特别优化
//            List<CntPostExist> tbPostExists = postExistService.list();
//            List<UserCollection> userCollections = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).in(UserCollection::getCollectionId, tbPostExists.stream().map(item -> item.getCollectionId()).collect(Collectors.toSet())));
//            return !userCollections.isEmpty();
//        }
//        // 1.2 没有的话，直接返回 false
//        return Boolean.FALSE;
//    }

    @Override
    public boolean isConfigPostCustomer(String userId, String buiId) {
        ICntPostConfigService postConfigService = SpringUtil.getBean(ICntPostConfigService.class);
        //1.查询 配置购买表中 有没有这个  buiId
        // 1.1 有的话，就查 拥有表中的藏品和目前用户拥有的藏品进行比较,存在 返回true ; 否则 false
        CntPostConfig cntPostConfig = postConfigService.getOne(Wrappers.<CntPostConfig>lambdaQuery().select(CntPostConfig::getBuiId,CntPostConfig::getId).eq(CntPostConfig::getBuiId, buiId));
        //List<CntPostSell> tbPostSells = postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
        if (Objects.nonNull(cntPostConfig)){
            // 此接口可以进行特别优化
            List<CntPostExist> tbPostExists = postExistService.list(Wrappers.<CntPostExist>lambdaQuery().select(CntPostExist::getCollectionId).eq(CntPostExist::getConfigId, cntPostConfig.getId()));
            Set<String> collectionIds = tbPostExists.stream().map(item -> item.getCollectionId()).collect(Collectors.toSet());
            if (collectionIds.isEmpty())return Boolean.FALSE;
            List<UserCollection> userCollections = userCollectionService.list(Wrappers.<UserCollection>lambdaQuery().select(UserCollection::getId).eq(UserCollection::getIsExist,USE_EXIST.getCode()).eq(UserCollection::getUserId, userId).in(UserCollection::getCollectionId,collectionIds ));
            // 去重
            Set<String> userCollectionIds = userCollections.stream().map(item -> item.getId()).collect(Collectors.toSet());
            return userCollectionIds.size() >= collectionIds.size();
        }
        // 1.2 没有的话，直接返回 false
        return Boolean.FALSE;
    }


    /**
     * 是否可以提前购 盲盒
     * @param userId
     * @param buiId
     * @return
     */
//    @Override
//    public boolean isConfigPostBoxCustomer(String userId, String buiId) {
//        //1.查询 配置购买表中 有没有这个  buiId
//        // 1.1 有的话，就查 拥有表中的藏品和目前用户拥有的藏品进行比较,存在 返回true ; 否则 false
//        List<CntPostSell> tbPostSells = postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
//        if (!tbPostSells.isEmpty()){
//            // 此接口可以进行特别优化
//            List<CntPostExist> tbPostExists = postExistService.list();
//            List<UserBox> userBoxList = userBoxService.list(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getUserId, userId).in(UserBox::getBoxId, tbPostExists.stream().map(item -> item.getCollectionId()).collect(Collectors.toSet())));
//            return !userBoxList.isEmpty();
//        }
//        // 1.2 没有的话，直接返回 false
//        return Boolean.FALSE;
//    }
    @Override
    @Deprecated
    public boolean isConfigPostBoxCustomer(String userId, String buiId) {
        //1.查询 配置购买表中 有没有这个  buiId
        ICntPostConfigService postConfigService = SpringUtil.getBean(ICntPostConfigService.class);
        // 1.1 有的话，就查 拥有表中的藏品和目前用户拥有的藏品进行比较,存在 返回true ; 否则 false
        CntPostConfig cntPostConfig = postConfigService.getOne(Wrappers.<CntPostConfig>lambdaQuery().select(CntPostConfig::getBuiId,CntPostConfig::getId).eq(CntPostConfig::getBuiId, buiId));

        // List<CntPostSell> tbPostSells = postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
        if (Objects.nonNull(cntPostConfig)){
            // 此接口可以进行特别优化
            List<CntPostExist> tbPostExists = postExistService.list(Wrappers.<CntPostExist>lambdaQuery().select(CntPostExist::getCollectionId).eq(CntPostExist::getConfigId, cntPostConfig.getId()));
            List<UserBox> userBoxList = userBoxService.list(Wrappers.<UserBox>lambdaQuery().eq(UserBox::getUserId, userId).in(UserBox::getBoxId, tbPostExists.stream().map(item -> item.getCollectionId()).collect(Collectors.toSet())));
            return !userBoxList.isEmpty();
        }
        // 1.2 没有的话，直接返回 false
        return Boolean.FALSE;
    }
}
