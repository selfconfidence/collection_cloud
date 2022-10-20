package com.manyun.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.CntUserBox;
import com.manyun.admin.domain.CntUserCollection;
import com.manyun.admin.domain.query.*;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.*;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 快照
 * @author yanwei
 * @date 2022-08-19
 */
@Service
public class SnapshotServiceImpl implements ISnapshotService {

    @Autowired
    private ICntUserService userService;

    @Autowired
    private ICntUserCollectionService userCollectionService;

    @Autowired
    private ICntUserBoxService userBoxService;

    @Autowired
    private ICntConsignmentService consignmentService;

    /**
     * 规定时间统计商品查询
     * @param statisticalGoodsQuery
     * @return
     */
    @Override
    public TableDataInfo<StatisticalGoodsVo> statisticalGoods(StatisticalGoodsQuery statisticalGoodsQuery) {
        List<GoodsQuery> collections = statisticalGoodsQuery.getCollections();
        List<GoodsQuery> boxs = statisticalGoodsQuery.getBoxs();
        if(collections.size()>0){
            List<GoodsQuery> collect = collections.parallelStream().filter(f -> StringUtils.isNotBlank(f.getGoodId()) && null != f.getCount() && 0 != f.getCount()).collect(Collectors.toList());
            Assert.isTrue(collect.size()==collections.size()
                    ,"请求参数有误,请检查!");
        }
        if(boxs.size()>0){
            List<GoodsQuery> collect = boxs.parallelStream().filter(f -> StringUtils.isNotBlank(f.getGoodId()) && null != f.getCount() && 0 != f.getCount()).collect(Collectors.toList());
            Assert.isTrue(collect.size()==boxs.size()
                    ,"请求参数有误,请检查!");
        }
        Assert.isFalse(collections.size()==0 && boxs.size()==0,"请选择藏品或盲盒!");
        //满足条件的藏品用户id
        List<String> userIdsCollection = new ArrayList<>();
        //满足条件的盲盒用户id
        List<String> userIdsBox = new ArrayList<>();
        if(collections.size()>0 && boxs.size()==0){
            for (int i = 0; i < collections.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userCollectionService.selectMeetTheConditionsUserIds(collections.get(i).getGoodId(),collections.get(i).getCount());
                if ( i == 0){
                    userIdsCollection.addAll(ids);
                }else {
                    userIdsCollection=userIdsCollection.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsCollection.size()==0)return TableDataInfoUtil.pageTableDataInfo(new ArrayList<StatisticalGoodsVo>(), new ArrayList<StatisticalGoodsVo>());
            //组装需要返回的数据
            PageHelper.startPage(statisticalGoodsQuery.getPageNum(),statisticalGoodsQuery.getPageSize());
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIdsCollection.size()>0,CntUser::getId,userIdsCollection));
            return TableDataInfoUtil.pageTableDataInfo(users.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m.getId());
                statisticalGoodsVo.setPhone(m.getPhone());
                statisticalGoodsVo.setLinkAddr(m.getLinkAddr());
                statisticalGoodsVo.setCollections(userCollectionService.selectMeetTheConditionsData(m.getId(),collections.parallelStream().map(GoodsQuery::getGoodId).collect(Collectors.toList())));
                return statisticalGoodsVo;
            }).collect(Collectors.toList()),users);
        }else if(boxs.size()>0 && collections.size()==0){
            for (int i = 0; i < boxs.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userBoxService.selectMeetTheConditionsUserIds(boxs.get(i).getGoodId(),boxs.get(i).getCount());
                if ( i == 0){
                    userIdsBox.addAll(ids);
                }else {
                    userIdsBox=userIdsBox.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsBox.size()==0)return TableDataInfoUtil.pageTableDataInfo(new ArrayList<StatisticalGoodsVo>(), new ArrayList<StatisticalGoodsVo>());
            //组装需要返回的数据
            //查询用户信息
            PageHelper.startPage(statisticalGoodsQuery.getPageNum(),statisticalGoodsQuery.getPageSize());
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIdsBox.size()>0,CntUser::getId,userIdsBox));
            return TableDataInfoUtil.pageTableDataInfo(users.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m.getId());
                statisticalGoodsVo.setPhone(m.getPhone());
                statisticalGoodsVo.setLinkAddr(m.getLinkAddr());
                statisticalGoodsVo.setBoxs(userBoxService.selectMeetTheConditionsData(m.getId(),boxs.parallelStream().map(GoodsQuery::getGoodId).collect(Collectors.toList())));
                return statisticalGoodsVo;
            }).collect(Collectors.toList()),users);
        }else if(collections.size()>0 && boxs.size()>0){
            for (int i = 0; i < collections.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userCollectionService.selectMeetTheConditionsUserIds(collections.get(i).getGoodId(),collections.get(i).getCount());
                if ( i == 0){
                    userIdsCollection.addAll(ids);
                }else {
                    userIdsCollection=userIdsCollection.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            for (int i = 0; i < boxs.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userBoxService.selectMeetTheConditionsUserIds(boxs.get(i).getGoodId(),boxs.get(i).getCount());
                if ( i == 0){
                    userIdsBox.addAll(ids);
                }else {
                    userIdsBox=userIdsBox.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsCollection.size()==0 && userIdsBox.size()==0)return TableDataInfoUtil.pageTableDataInfo(new ArrayList<StatisticalGoodsVo>(), new ArrayList<StatisticalGoodsVo>());
            //筛选出同时满足条件藏品和盲盒的用户id
            List<String> intersection = userIdsCollection.stream().filter(userIdsBox::contains).collect(Collectors.toList());
            //组装需要返回的数据
            //查询用户信息
            PageHelper.startPage(statisticalGoodsQuery.getPageNum(),statisticalGoodsQuery.getPageSize());
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(intersection.size()>0,CntUser::getId,intersection));
            return TableDataInfoUtil.pageTableDataInfo(users.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m.getId());
                statisticalGoodsVo.setPhone(m.getPhone());
                statisticalGoodsVo.setLinkAddr(m.getLinkAddr());
                statisticalGoodsVo.setCollections(userCollectionService.selectMeetTheConditionsData(m.getId(),collections.parallelStream().map(GoodsQuery::getGoodId).collect(Collectors.toList())));
                statisticalGoodsVo.setBoxs(userBoxService.selectMeetTheConditionsData(m.getId(),boxs.parallelStream().map(GoodsQuery::getGoodId).collect(Collectors.toList())));
                return statisticalGoodsVo;
            }).collect(Collectors.toList()), users);
        }else {
            Assert.isTrue(Boolean.FALSE,"数据查询错误!");
        }
        return null;
    }


    /**
     * 规定时间统计商品数据导出
     * @param statisticalGoodsQuery
     * @return
     */
    @Override
    public List<StatisticalGoodsVo> statisticalGoodsExcel(StatisticalGoodsQuery statisticalGoodsQuery) {
        List<GoodsQuery> collections = statisticalGoodsQuery.getCollections();
        List<GoodsQuery> boxs = statisticalGoodsQuery.getBoxs();
        if(collections.size()>0){
            List<GoodsQuery> collect = collections.parallelStream().filter(f -> StringUtils.isNotBlank(f.getGoodId()) && null != f.getCount() && 0 != f.getCount()).collect(Collectors.toList());
            Assert.isTrue(collect.size()==collections.size()
                    ,"请求参数有误,请检查!");
        }
        if(boxs.size()>0){
            List<GoodsQuery> collect = boxs.parallelStream().filter(f -> StringUtils.isNotBlank(f.getGoodId()) && null != f.getCount() && 0 != f.getCount()).collect(Collectors.toList());
            Assert.isTrue(collect.size()==boxs.size()
                    ,"请求参数有误,请检查!");
        }
        Assert.isFalse(collections.size()==0 && boxs.size()==0,"请选择藏品或盲盒!");
        //满足条件的藏品用户id
        List<String> userIdsCollection = new ArrayList<>();
        //满足条件的盲盒用户id
        List<String> userIdsBox = new ArrayList<>();
        if(collections.size()>0 && boxs.size()==0){
            for (int i = 0; i < collections.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userCollectionService.selectMeetTheConditionsUserIds(collections.get(i).getGoodId(),collections.get(i).getCount());
                if ( i == 0){
                    userIdsCollection.addAll(ids);
                }else {
                    userIdsCollection=userIdsCollection.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsCollection.size()==0)return new ArrayList<StatisticalGoodsVo>();
            //组装需要返回的数据
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIdsCollection.size()>0,CntUser::getId,userIdsCollection));
            List<StatisticalGoodsVo> list = userIdsCollection.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m);
                if (users.size() > 0) {
                    Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m)).findFirst();
                    if (optionalUser.isPresent()) {
                        statisticalGoodsVo.setPhone(optionalUser.get().getPhone());
                        statisticalGoodsVo.setLinkAddr(optionalUser.get().getLinkAddr());
                    }
                }
                return statisticalGoodsVo;
            }).collect(Collectors.toList());
            return list;
        }else if(boxs.size()>0 && collections.size()==0){
            for (int i = 0; i < boxs.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userBoxService.selectMeetTheConditionsUserIds(boxs.get(i).getGoodId(),boxs.get(i).getCount());
                if ( i == 0){
                    userIdsBox.addAll(ids);
                }else {
                    userIdsBox=userIdsBox.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsBox.size()==0)return new ArrayList<StatisticalGoodsVo>();
            //组装需要返回的数据
            //查询用户信息
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIdsBox.size()>0,CntUser::getId,userIdsBox));
            List<StatisticalGoodsVo> list = userIdsBox.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m);
                if (users.size() > 0) {
                    Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m)).findFirst();
                    if (optionalUser.isPresent()) {
                        statisticalGoodsVo.setPhone(optionalUser.get().getPhone());
                        statisticalGoodsVo.setLinkAddr(optionalUser.get().getLinkAddr());
                    }
                }
                return statisticalGoodsVo;
            }).collect(Collectors.toList());
            return list;
        }else if(collections.size()>0 && boxs.size()>0){
            for (int i = 0; i < collections.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userCollectionService.selectMeetTheConditionsUserIds(collections.get(i).getGoodId(),collections.get(i).getCount());
                if ( i == 0){
                    userIdsCollection.addAll(ids);
                }else {
                    userIdsCollection=userIdsCollection.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            for (int i = 0; i < boxs.size(); i++) {
                //查询第一次符合条件的用户信息
                List<String> ids = userBoxService.selectMeetTheConditionsUserIds(boxs.get(i).getGoodId(),boxs.get(i).getCount());
                if ( i == 0){
                    userIdsBox.addAll(ids);
                }else {
                    userIdsBox=userIdsBox.stream().filter(ids::contains).collect(Collectors.toList());
                }
            }
            if(userIdsCollection.size()==0 && userIdsBox.size()==0)return new ArrayList<StatisticalGoodsVo>();
            //筛选出同时满足条件藏品和盲盒的用户id
            List<String> intersection = userIdsCollection.stream().filter(userIdsBox::contains).collect(Collectors.toList());
            //组装需要返回的数据
            //查询用户信息
            List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(intersection.size()>0,CntUser::getId,intersection));
            List<StatisticalGoodsVo> list = intersection.stream().map(m -> {
                StatisticalGoodsVo statisticalGoodsVo = new StatisticalGoodsVo();
                statisticalGoodsVo.setUserId(m);
                if (users.size() > 0) {
                    Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m)).findFirst();
                    if (optionalUser.isPresent()) {
                        statisticalGoodsVo.setPhone(optionalUser.get().getPhone());
                        statisticalGoodsVo.setLinkAddr(optionalUser.get().getLinkAddr());
                    }
                }
                return statisticalGoodsVo;
            }).collect(Collectors.toList());
            return list;
        }else {
            Assert.isTrue(Boolean.FALSE,"数据查询错误!");
        }
        return null;
    }

    /**
     * 藏品编号查询
     * @param collectionNumberQuery
     * @return
     */
    @Override
    public TableDataInfo<CollectionNumberVo> collectionNumberQuery(CollectionNumberQuery collectionNumberQuery) {
        PageHelper.startPage(collectionNumberQuery.getPageNum(),collectionNumberQuery.getPageSize());
        List<CntUserCollection> userCollectionList = userCollectionService.list(Wrappers.<CntUserCollection>lambdaQuery().eq(CntUserCollection::getCollectionId,collectionNumberQuery.getCollectionId()));
        List<String> userIds = userCollectionList.stream().map(CntUserCollection::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return  TableDataInfoUtil.pageTableDataInfo(userCollectionList.parallelStream().map(m->{
            CollectionNumberVo collectionNumberVo=new CollectionNumberVo();
            collectionNumberVo.setUserId(m.getUserId());
            collectionNumberVo.setCollectionName(m.getCollectionName());
            collectionNumberVo.setCollectionNumber(m.getCollectionNumber());
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    collectionNumberVo.setPhone(optionalUser.get().getPhone());
                    collectionNumberVo.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return collectionNumberVo;
        }).collect(Collectors.toList()), userCollectionList);
    }

    /**
     * 藏品编号数据导出
     * @param collectionNumberQuery
     * @return
     */
    @Override
    public List<CollectionNumberVo> collectionNumberExcel(CollectionNumberQuery collectionNumberQuery) {
        List<CntUserCollection> userCollectionList = userCollectionService.list(Wrappers.<CntUserCollection>lambdaQuery().eq(CntUserCollection::getCollectionId,collectionNumberQuery.getCollectionId()));
        List<String> userIds = userCollectionList.stream().map(CntUserCollection::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return  userCollectionList.parallelStream().map(m->{
            CollectionNumberVo collectionNumberVo=new CollectionNumberVo();
            collectionNumberVo.setUserId(m.getUserId());
            collectionNumberVo.setCollectionName(m.getCollectionName());
            collectionNumberVo.setCollectionNumber(m.getCollectionNumber());
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    collectionNumberVo.setPhone(optionalUser.get().getPhone());
                    collectionNumberVo.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return collectionNumberVo;
        }).collect(Collectors.toList());
    }

    /**
     * 规定时间藏品交易量及交易金额查询
     * @param collectionBusinessQuery
     * @return
     */
    @Override
    public TableDataInfo<CollectionBusinessVo> collectionBusinessQuery(CollectionBusinessQuery collectionBusinessQuery) {
        Map<String, Object> params = collectionBusinessQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        PageHelper.startPage(collectionBusinessQuery.getPageNum(),collectionBusinessQuery.getPageSize());
        List<CollectionBusinessVo> collectionBusinessVos = consignmentService.selectByTimeZones(collectionBusinessQuery);
        List<String> userIds = collectionBusinessVos.stream().map(CollectionBusinessVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return  TableDataInfoUtil.pageTableDataInfo(collectionBusinessVos.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList()), collectionBusinessVos);
    }

    /**
     * 规定时间藏品交易量及交易金额数据导出
     * @param collectionBusinessQuery
     * @return
     */
    @Override
    public List<CollectionBusinessVo> collectionBusinessExcel(CollectionBusinessQuery collectionBusinessQuery) {
        Map<String, Object> params = collectionBusinessQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        List<CollectionBusinessVo> collectionBusinessVos = consignmentService.selectByTimeZones(collectionBusinessQuery);
        List<String> userIds = collectionBusinessVos.stream().map(CollectionBusinessVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return collectionBusinessVos.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 规定时间指定藏品买卖查询
     * @param collectionSalesStatisticsQuery
     * @return
     */
    @Override
    public TableDataInfo<CollectionSalesStatisticsVo> collectionSalesStatistics(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery) {
        Map<String, Object> params = collectionSalesStatisticsQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        PageHelper.startPage(collectionSalesStatisticsQuery.getPageNum(),collectionSalesStatisticsQuery.getPageSize());
        List<CollectionSalesStatisticsVo> collectionSalesStatisticsVos = consignmentService.selectCollectionSalesStatistics(collectionSalesStatisticsQuery);
        List<String> userIds = collectionSalesStatisticsVos.stream().map(CollectionSalesStatisticsVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return  TableDataInfoUtil.pageTableDataInfo(collectionSalesStatisticsVos.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList()), collectionSalesStatisticsVos);
    }

    /**
     * 规定时间指定藏品买卖数据导出
     * @param collectionSalesStatisticsQuery
     * @return
     */
    @Override
    public List<CollectionSalesStatisticsVo> collectionSalesStatisticsExcel(CollectionSalesStatisticsQuery collectionSalesStatisticsQuery) {
        Map<String, Object> params = collectionSalesStatisticsQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        List<CollectionSalesStatisticsVo> collectionSalesStatisticsVos = consignmentService.selectCollectionSalesStatistics(collectionSalesStatisticsQuery);
        List<String> userIds = collectionSalesStatisticsVos.stream().map(CollectionSalesStatisticsVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return collectionSalesStatisticsVos.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 规定时间指定一件或多件藏品的持有总量查询
     * @param collectionTotalNumberQuery
     * @return
     */
    @Override
    public TableDataInfo<CollectionTotalNumberVo> collectionTotalNumber(CollectionTotalNumberQuery collectionTotalNumberQuery) {
        Assert.isTrue(collectionTotalNumberQuery.getCollectionIds().length>0,"请选择藏品!");
        Map<String, Object> params = collectionTotalNumberQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        PageHelper.startPage(collectionTotalNumberQuery.getPageNum(),collectionTotalNumberQuery.getPageSize());
        List<CollectionTotalNumberVo> collectionTotalNumberVoList = userCollectionService.selectCollectionTotalNumber(collectionTotalNumberQuery);
        List<String> userIds = collectionTotalNumberVoList.stream().map(CollectionTotalNumberVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return  TableDataInfoUtil.pageTableDataInfo(collectionTotalNumberVoList.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList()), collectionTotalNumberVoList);
    }

    /**
     * 规定时间指定一件或多件藏品的持有总量数据导出
     * @param collectionTotalNumberQuery
     * @return
     */
    @Override
    public List<CollectionTotalNumberVo> collectionTotalNumberExcel(CollectionTotalNumberQuery collectionTotalNumberQuery) {
        Assert.isTrue(collectionTotalNumberQuery.getCollectionIds().length>0,"请选择藏品!");
        Map<String, Object> params = collectionTotalNumberQuery.getParams();
        Assert.isFalse(params.isEmpty() ||
                        null == params.get("beginTime") ||
                        "" == params.get("beginTime") ||
                        null == params.get("endTime") ||
                        "" == params.get("endTime")
                ,"请选择开始时间及结束时间!");
        List<CollectionTotalNumberVo> collectionTotalNumberVoList = userCollectionService.selectCollectionTotalNumber(collectionTotalNumberQuery);
        List<String> userIds = collectionTotalNumberVoList.stream().map(CollectionTotalNumberVo::getUserId)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(f -> f))), ArrayList::new));
        List<CntUser> users = userService.list(Wrappers.<CntUser>lambdaQuery().in(userIds.size()>0,CntUser::getId,userIds));
        return collectionTotalNumberVoList.parallelStream().map(m->{
            if(users.size()>0){
                Optional<CntUser> optionalUser = users.parallelStream().filter(ff -> ff.getId().equals(m.getUserId())).findFirst();
                if(optionalUser.isPresent()){
                    m.setPhone(optionalUser.get().getPhone());
                    m.setLinkAddr(optionalUser.get().getLinkAddr());
                }
            }
            return m;
        }).collect(Collectors.toList());
    }

    /**
     * 每个用户藏品总量查询
     * @return
     */
    @Override
    public TableDataInfo<UserCollectionNumberVo> userCollectionNumber(UserCollectionNumberQuery userCollectionNumberQuery) {
        PageHelper.startPage(userCollectionNumberQuery.getPageNum(),userCollectionNumberQuery.getPageSize());
        List<UserCollectionNumberVo> userCollectionNumbers = userCollectionService.userCollectionNumber(userCollectionNumberQuery);
        return  TableDataInfoUtil.pageTableDataInfo(userCollectionNumbers,userCollectionNumbers);
    }

    /**
     * 每个用户藏品总量数据导出
     * @return
     */
    @Override
    public List<UserCollectionNumberVo> userCollectionNumberExcel(UserCollectionNumberQuery userCollectionNumberQuery) {
        List<UserCollectionNumberVo> userCollectionNumbers = userCollectionService.userCollectionNumber(userCollectionNumberQuery);
        return userCollectionNumbers;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList<>();
        list1.add("ACCOUNT1");
        list1.add("ACCOUNT2");
        list1.add("ACCOUNT3");
        list1.add("ACCOUNT4");
        list1.add("ACCOUNT5");

        List<String> list2 = new ArrayList<>();
        list2.add("ACC1");
        list2.add("ACCOUNT2");
        list2.add("ACCOUNT3");
        list2.add("ACCOUNT6");
        list2.add("ACCOUNT7");
        list2.add("ACCOUNT8");
        list2.add("ACCOUNT9");
        List<String> intersection = list1.stream().filter(list2::contains).collect(Collectors.toList());
        System.out.println(intersection);
    }

}
