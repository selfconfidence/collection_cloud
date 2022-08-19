package com.manyun.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.domain.*;
import com.manyun.admin.domain.query.ActionTarDictQuery;
import com.manyun.admin.domain.query.DrawRulesDictQuery;
import com.manyun.admin.domain.query.PostConfigDictQuery;
import com.manyun.admin.domain.vo.*;
import com.manyun.admin.service.*;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.enums.CateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-14
 */
@Service
public class CntDictServiceImpl implements CntDictService
{

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private ICntCateService cateService;

    @Autowired
    private ICnfCreationdService creationdService;

    @Autowired
    private ICnfIssuanceService issuanceService;

    @Autowired
    private ICntLableService lableService;

    @Autowired
    private ICntCustomerServiceService customerServiceService;

    @Autowired
    private ICntTarService cntTarService;

    @Autowired
    private ICntBoxService boxService;

    @Autowired
    private ICntPostConfigService postConfigService;

    @Autowired
    private ICntBoxScoreService boxScoreService;

    /***
     * 查询藏品字典
     */
    @Override
    public R collectionDict()
    {
        return R.ok(collectionService.list(Wrappers.<CntCollection>lambdaQuery().orderByDesc(CntCollection::getCreatedTime)).stream().map(m ->{
            CntCollectionDictVo cntCollectionDictVo=new CntCollectionDictVo();
            BeanUtil.copyProperties(m,cntCollectionDictVo);
            return cntCollectionDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询盲盒字典
     */
    @Override
    public R boxDict() {
        return R.ok(boxService.list(Wrappers.<CntBox>lambdaQuery().orderByDesc(CntBox::getCreatedTime)).stream().map(m ->{
            CntBoxDictVo boxDictVo=new CntBoxDictVo();
            BeanUtil.copyProperties(m,boxDictVo);
            return boxDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询藏品系列字典
     * @return
     */
    @Override
    public R collectionCateDict()
    {
        return R.ok(cateService.list(Wrappers.<CntCate>lambdaQuery().eq(CntCate::getCateType,Long.valueOf(CateType.COLLECTION_CATE.getCode())).ne(CntCate::getParentId,"0").orderByDesc(CntCate::getCreatedTime)).stream().map(m ->{
            CollectionCateDictVo collectionCateDictVo=new CollectionCateDictVo();
            BeanUtil.copyProperties(m,collectionCateDictVo);
            return collectionCateDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询盲盒系列字典
     * @return
     */
    @Override
    public R boxCateDict()
    {
        return R.ok(cateService.list(Wrappers.<CntCate>lambdaQuery().eq(CntCate::getCateType,Long.valueOf(CateType.BOX_CATE.getCode())).orderByDesc(CntCate::getCreatedTime)).stream().map(m ->{
            CollectionCateDictVo collectionCateDictVo=new CollectionCateDictVo();
            BeanUtil.copyProperties(m,collectionCateDictVo);
            return collectionCateDictVo;
        }).collect(Collectors.toList()));
    }


    /***
     * 查询创作者字典
     */
    @Override
    public R creationdDict()
    {
        return R.ok(creationdService.list(Wrappers.<CnfCreationd>lambdaQuery().orderByDesc(CnfCreationd::getCreatedTime)).stream().map(m ->{
            CreationdDictVo creationdDictVo=new CreationdDictVo();
            BeanUtil.copyProperties(m,creationdDictVo);
            return creationdDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询发行方字典
     */
    @Override
    public R issuanceDict()
    {
        return R.ok(issuanceService.list(Wrappers.<CnfIssuance>lambdaQuery().orderByDesc(CnfIssuance::getCreatedTime)).stream().map(m ->{
            CnfIssuanceDictVo issuanceDictVo=new CnfIssuanceDictVo();
            BeanUtil.copyProperties(m,issuanceDictVo);
            return issuanceDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询标签字典
     */
    @Override
    public R lableDict()
    {
        return R.ok(lableService.list(Wrappers.<CntLable>lambdaQuery().orderByDesc(CntLable::getCreatedTime)).stream().map(m ->{
            LableDictVo lableDictVo=new LableDictVo();
            BeanUtil.copyProperties(m,lableDictVo);
            return lableDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 查询客服字典
     */
    @Override
    public R customerServiceDict()
    {
        List<CustomerServiceDictVo> customerServiceDictVos=new ArrayList<>();
        customerServiceDictVos.add(Builder.of(CustomerServiceDictVo::new).with(CustomerServiceDictVo::setId,0).with(CustomerServiceDictVo::setMenuName,"父菜单").build());
        customerServiceDictVos.addAll(customerServiceService.list(Wrappers.<CntCustomerService>lambdaQuery().eq(CntCustomerService::getMenuStatus,"0").eq(CntCustomerService::getParentId,0).orderByDesc(CntCustomerService::getCreateTime)).stream().map(m ->{
            CustomerServiceDictVo customerServiceDictVo=new CustomerServiceDictVo();
            BeanUtil.copyProperties(m,customerServiceDictVo);
            return customerServiceDictVo;
        }).collect(Collectors.toList()));
        return R.ok(customerServiceDictVos);
    }

    /***
     * 抽签规则字典
     */
    @Override
    public R drawRulesDict(DrawRulesDictQuery drawRulesDictQuery)
    {
        return R.ok(cntTarService.list(Wrappers.<CntTar>lambdaQuery().eq(CntTar::getTarType,drawRulesDictQuery.getTarType()).orderByDesc(CntTar::getCreatedTime)).stream().map(m->{
            DrawRulesDictVo drawRulesDictVo=new DrawRulesDictVo();
            BeanUtil.copyProperties(m,drawRulesDictVo);
            return drawRulesDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 提前购配置字典
     */
    @Override
    public R postConfigDict()
    {
        return R.ok(postConfigService.list(Wrappers.<CntPostConfig>lambdaQuery().orderByDesc(CntPostConfig::getCreatedTime)).stream().map(m -> {
            PostConfigDictVo postConfigDictVo=new PostConfigDictVo();
            BeanUtil.copyProperties(m,postConfigDictVo);
            return postConfigDictVo;
        }));
    }


    /***
     * 提前购配置的商品字典
     */
    @Override
    public R postConfigGoodsDict(PostConfigDictQuery postConfigDictQuery)
    {
        return R.ok(
                postConfigDictQuery.getIsType()==0?collectionService
                        .list(
                                Wrappers.<CntCollection>lambdaQuery()
                                        .gt(CntCollection::getPublishTime, new Date())
                                        .isNotNull(CntCollection::getPostTime).orderByDesc(CntCollection::getCreatedTime)).stream().map(m -> {
                            TqgGoodsDictVo tqgGoodsDictVo = new TqgGoodsDictVo();
                            tqgGoodsDictVo.setId(m.getId());
                            tqgGoodsDictVo.setBuiName(m.getCollectionName());
                            return tqgGoodsDictVo;
                        }).collect(Collectors.toList()):boxService
                        .list(
                                Wrappers.<CntBox>lambdaQuery()
                                        .gt(CntBox::getPublishTime, new Date())
                                        .isNotNull(CntBox::getPostTime).orderByDesc(CntBox::getCreatedTime)).stream().map(m -> {
                            TqgGoodsDictVo tqgGoodsDictVo = new TqgGoodsDictVo();
                            tqgGoodsDictVo.setId(m.getId());
                            tqgGoodsDictVo.setBuiName(m.getBoxTitle());
                            return tqgGoodsDictVo;
                        }).collect(Collectors.toList())
                );
    }

    /***
     * 提前购已拥有的
     */
    @Override
    public R postExistDict()
    {
        return R.ok(collectionService
                .list(Wrappers.<CntCollection>lambdaQuery().orderByDesc(CntCollection::getCreatedTime)).stream().map(m -> {
                    TqgGoodsDictVo tqgGoodsDictVo = new TqgGoodsDictVo();
                    tqgGoodsDictVo.setId(m.getId());
                    tqgGoodsDictVo.setBuiName(m.getCollectionName());
                    return tqgGoodsDictVo;
                }).collect(Collectors.toList()));
    }

    /***
     * 活动合成材料字典
     */
    @Override
    public R actionTarDict(ActionTarDictQuery tarDictQuery)
    {
        return R.ok(collectionService.list(Wrappers.<CntCollection>lambdaQuery().ne(CntCollection::getId,tarDictQuery.getCollectionId()).orderByDesc(CntCollection::getCreatedTime)).parallelStream().map(m ->{
            CntCollectionDictVo cntCollectionDictVo=new CntCollectionDictVo();
            BeanUtil.copyProperties(m,cntCollectionDictVo);
            return cntCollectionDictVo;
        }).collect(Collectors.toList()));
    }

    /***
     * 藏品分类字典
     */
    @Override
    public R cateDict()
    {
        List<CateDictVo> cateDictVos=new ArrayList<>();
        cateDictVos.add(Builder.of(CateDictVo::new).with(CateDictVo::setId,"0").with(CateDictVo::setCateName,"顶级分类").build());
        cateDictVos.addAll(cateService.list(Wrappers.<CntCate>lambdaQuery().eq(CntCate::getCateType,1).eq(CntCate::getParentId,"0").orderByDesc(CntCate::getCreatedTime)).parallelStream().map(m ->{
            CateDictVo cateDictVo=new CateDictVo();
            BeanUtil.copyProperties(m,cateDictVo);
            return cateDictVo;
        }).collect(Collectors.toList()));
        return R.ok(cateDictVos);
    }

    /***
     * 盲盒评分字典
     */
    @Override
    public R boxScoreDict() {
        return R.ok(boxScoreService.list(Wrappers.<CntBoxScore>lambdaQuery().eq(CntBoxScore::getScoreStatus,0).orderByAsc(CntBoxScore::getScoreSort)).parallelStream().map(m->{
            CntBoxScoreDictVo boxScoreDictVo=new CntBoxScoreDictVo();
            BeanUtil.copyProperties(m,boxScoreDictVo);
            return boxScoreDictVo;
        }));
    }


}
