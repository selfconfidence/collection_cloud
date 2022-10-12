package com.manyun.business.service.impl;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import com.google.common.collect.Maps;
import com.manyun.business.domain.entity.*;
import com.manyun.business.mapper.TbPostConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.manyun.business.service.*;
import com.manyun.common.core.domain.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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
    @Autowired
    private ICntPostSellService postSellService;

/*    @Autowired
    private ICntPostConfigService postConfigService;*/

    @Autowired
    private IUserCollectionService userCollectionService;


    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private ICntPostConfigLogService postConfigLogService;


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

/*    @Override
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
    }*/
    @Override
    public boolean isConfigPostCustomer(String userId, String buiId) {
           // 1. 查询 sell 表中是否有当前 资产的记录，如果有的话，就按照集合的方式，整理下 config_id 然后，根据 新增字段排序按需验证即可
        List<CntPostSell> cntPostSells = getCntPostSells(buiId);
        if (cntPostSells.isEmpty()) return Boolean.FALSE;
        Set<String> configIds = cntPostSells.parallelStream().map(item -> item.getConfigId()).collect(Collectors.toSet());
        if (configIds.isEmpty()) return Boolean.FALSE;
        List<CntPostConfig> cntPostConfigs = list(Wrappers.<CntPostConfig>lambdaQuery().select(CntPostConfig::getId,CntPostConfig::getBuyFrequency).in(CntPostConfig::getId, configIds).orderByDesc(CntPostConfig::getCreatedTime));
        // 拿到当前 配置列表后，开始遍历 已经是有顺序得了
        for (CntPostConfig cntPostConfig : cntPostConfigs) {
            // 条件查出来之后，全部满足即可
            List<CntPostExist> cntPostExists = postExistService.list(Wrappers.<CntPostExist>lambdaQuery().eq(CntPostExist::getConfigId, cntPostConfig.getId()));
            if (cntPostExists.size() == 0){
                return Boolean.FALSE;
            }
            // userCollections 让它重复即可
            List<UserCollection> userCollections = userCollectionService.list(
                    Wrappers.<UserCollection>lambdaQuery().select(UserCollection::getId,UserCollection::getCollectionId)
                            .eq(UserCollection::getIsExist,USE_EXIST.getCode())
                            .eq(UserCollection::getUserId, userId)
                            .in(UserCollection::getCollectionId,cntPostExists.parallelStream().map(item -> item.getCollectionId()).collect(Collectors.toSet()) ));
            // 组合过滤条件
            Map<String, Integer> requiredQuantity = cntPostExists.parallelStream().collect(Collectors.toMap(CntPostExist::getCollectionId, CntPostExist::getRequiredQuantity));
            // 组合操作条件
            if (userCollections.size() >= cntPostExists.size()){
                // 当前用户对应的持有量是否满足！ 必须全部满足即可！
                Map<String, List<UserCollection>> userUseCollection = userCollections.parallelStream().collect(Collectors.groupingBy(UserCollection::getCollectionId));
                AtomicBoolean full = new AtomicBoolean(true);
                requiredQuantity.forEach((k,v)->{
                    // 有一个条件不成立 就变更状态
                    if (!(userUseCollection.get(k).size() >= v))
                        full.set(false);
                });
                if (full.get()){
                    // 如果满足，需要二次查询 自己的次数是否满足了.
                    CntPostConfigLog postConfigLogServiceOne = postConfigLogService.getOne(Wrappers.<CntPostConfigLog>lambdaQuery().eq(CntPostConfigLog::getUserId, userId).eq(CntPostConfigLog::getConfigId, cntPostConfig.getId()));
                    // 如果是 null 就直接返回 true
                    if (Objects.isNull(postConfigLogServiceOne))return Boolean.TRUE;
                    // 如果不是 NULL
                    return postConfigLogServiceOne.getBuyFrequency().compareTo(cntPostConfig.getBuyFrequency()) < 0;
                }
            }
        }
        return Boolean.FALSE;
    }

    private List<CntPostSell> getCntPostSells(String buiId) {
       return postSellService.list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getBuiId, buiId));
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

    /**
     * 顺序执行，没有新增，有的话 次数 ++ 即可
     * @param userId
     * @param buiId
     */
    @Override
    public void orderExec(String userId, String buiId) {
        List<CntPostSell> cntPostSells = getCntPostSells(buiId);
        Set<String> configIds = cntPostSells.parallelStream().map(item -> item.getConfigId()).collect(Collectors.toSet());
        List<CntPostConfig> cntPostConfigs = list(Wrappers.<CntPostConfig>lambdaQuery().select(CntPostConfig::getId,CntPostConfig::getBuyFrequency).in(CntPostConfig::getId, configIds).orderByDesc(CntPostConfig::getCreatedTime));
        for (CntPostConfig cntPostConfig : cntPostConfigs) {
            CntPostConfigLog postConfigLogServiceOne = postConfigLogService.getOne(Wrappers.<CntPostConfigLog>lambdaQuery().eq(CntPostConfigLog::getUserId, userId).eq(CntPostConfigLog::getConfigId, cntPostConfig.getId()));
            if (Objects.isNull(postConfigLogServiceOne)){
                postConfigLogServiceOne = Builder.of(CntPostConfigLog::new)
                        .with(CntPostConfigLog::setId, IdUtil.getSnowflake().nextIdStr())
                        .with(CntPostConfigLog::setConfigId, cntPostConfig.getId())
                        .with(CntPostConfigLog::setUserId, userId)
                        .with(CntPostConfigLog::setBuyFrequency, Integer.valueOf(1))
                        .build();
                postConfigLogService.save(postConfigLogServiceOne);
                return;
            }else{
                if (postConfigLogServiceOne.getBuyFrequency() <cntPostConfig.getBuyFrequency()){
                    postConfigLogServiceOne.setBuyFrequency(postConfigLogServiceOne.getBuyFrequency() +1);
                    postConfigLogService.updateById(postConfigLogServiceOne);
                }
                return;

            }
        }

    }

    @Override
    public int getSellNum(String userId, String id) {
        // 1. 查询 sell 表中是否有当前 资产的记录，如果有的话，就按照集合的方式，整理下 config_id 然后，根据 新增字段排序按需验证即可
        List<CntPostSell> cntPostSells = getCntPostSells(id);
        if (cntPostSells.isEmpty()) return 0;
        Set<String> configIds = cntPostSells.parallelStream().map(item -> item.getConfigId()).collect(Collectors.toSet());
        if (configIds.isEmpty()) return 0;
        List<CntPostConfig> cntPostConfigs = list(Wrappers.<CntPostConfig>lambdaQuery().select(CntPostConfig::getId,CntPostConfig::getBuyFrequency).in(CntPostConfig::getId, configIds).orderByDesc(CntPostConfig::getCreatedTime));
        // 拿到当前 配置列表后，开始遍历 已经是有顺序得了
        for (CntPostConfig cntPostConfig : cntPostConfigs) {
            // 条件查出来之后，全部满足即可
            List<CntPostExist> cntPostExists = postExistService.list(Wrappers.<CntPostExist>lambdaQuery().eq(CntPostExist::getConfigId, cntPostConfig.getId()));
            if (cntPostExists.size() == 0){
                return 0;
            }
            // userCollections 让它重复即可
            List<UserCollection> userCollections = userCollectionService.list(
                    Wrappers.<UserCollection>lambdaQuery().select(UserCollection::getId,UserCollection::getCollectionId)
                            .eq(UserCollection::getIsExist,USE_EXIST.getCode())
                            .eq(UserCollection::getUserId, userId)
                            .in(UserCollection::getCollectionId,cntPostExists.parallelStream().map(item -> item.getCollectionId()).collect(Collectors.toSet()) ));
            // 组合过滤条件
            Map<String, Integer> requiredQuantity = cntPostExists.parallelStream().collect(Collectors.toMap(CntPostExist::getCollectionId, CntPostExist::getRequiredQuantity));
            // 组合操作条件
            if (userCollections.size() >= cntPostExists.size()){
                // 当前用户对应的持有量是否满足！ 必须全部满足即可！
                Map<String, List<UserCollection>> userUseCollection = userCollections.parallelStream().collect(Collectors.groupingBy(UserCollection::getCollectionId));
                AtomicBoolean full = new AtomicBoolean(true);
                requiredQuantity.forEach((k,v)->{
                    // 有一个条件不成立 就变更状态
                    if (!(userUseCollection.get(k).size() >= v))
                        full.set(false);
                });
                if (full.get()){
                    // 如果满足，需要二次查询 自己的次数是否满足了.
                    CntPostConfigLog postConfigLogServiceOne = postConfigLogService.getOne(Wrappers.<CntPostConfigLog>lambdaQuery().eq(CntPostConfigLog::getUserId, userId).eq(CntPostConfigLog::getConfigId, cntPostConfig.getId()));
                    // 如果是 null 就直接返回 true
                    if (Objects.isNull(postConfigLogServiceOne))return 0;
                    // 如果不是 NULL
                    return  postConfigLogServiceOne.getBuyFrequency();
                }
            }
        }
        return 0;
    }

}
