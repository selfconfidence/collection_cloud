package com.manyun.admin.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.domain.dto.ActionCollectionDto;
import com.manyun.admin.domain.dto.SaveActionCollectionDto;
import com.manyun.admin.domain.query.ActionCollectionQuery;
import com.manyun.admin.domain.vo.ActionCollectionVo;
import com.manyun.admin.domain.vo.CnfIssuanceVo;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntActionCollectionMapper;
import com.manyun.admin.domain.CntActionCollection;
import com.manyun.admin.service.ICntActionCollectionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动合成目标藏品Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-06
 */
@Service
public class CntActionCollectionServiceImpl extends ServiceImpl<CntActionCollectionMapper,CntActionCollection> implements ICntActionCollectionService
{
    @Autowired
    private CntActionCollectionMapper cntActionCollectionMapper;

    /**
     * 查询活动合成目标藏品列表
     *
     * @param actionCollectionQuery
     * @return 活动合成目标藏品
     */
    @Override
    public TableDataInfo<ActionCollectionVo> selectCntActionCollectionList(ActionCollectionQuery actionCollectionQuery)
    {
        PageHelper.startPage(actionCollectionQuery.getPageNum(),actionCollectionQuery.getPageSize());
        List<CntActionCollection> actionCollections = cntActionCollectionMapper.selectCntActionCollectionList(actionCollectionQuery);
        return TableDataInfoUtil.pageTableDataInfo(actionCollections.parallelStream().map(m ->{
            ActionCollectionVo actionCollectionVo=new ActionCollectionVo();
            BeanUtil.copyProperties(m,actionCollectionVo);
            return actionCollectionVo;
        }).collect(Collectors.toList()),actionCollections);
    }

    /**
     * 新增活动合成目标藏品
     *
     * @param saveActionCollectionDto 活动合成目标藏品
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCntActionCollection(SaveActionCollectionDto saveActionCollectionDto)
    {
        String actionId = saveActionCollectionDto.getActionId();
        List<ActionCollectionVo> actionCollectionVos = saveActionCollectionDto.getActionCollectionVos();
        Assert.isTrue(Objects.nonNull(actionCollectionVos),"新增失败!");
        Assert.isTrue(actionCollectionVos.size()>0,"请添加活动目标藏品!");
        BigDecimal tranSvgSum = actionCollectionVos.stream().map(ActionCollectionVo::getTranSvg).reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.isFalse(tranSvgSum.compareTo(BigDecimal.valueOf(100.00))==1,"概率总和不得超过100%!");
        Set<String> set = actionCollectionVos.parallelStream().map(ActionCollectionVo::getCollectionId).collect(Collectors.toSet());
        Assert.isTrue(set.size()==actionCollectionVos.size(),"所选活动目标藏品不可重复!");
        //删除不需要
        remove(Wrappers.<CntActionCollection>lambdaQuery().notIn(CntActionCollection::getCollectionId,actionCollectionVos.parallelStream().map(ActionCollectionVo::getCollectionId).collect(Collectors.toList())).eq(CntActionCollection::getActionId,actionId));
        //组装需修改或新增的集合 并执行
        List<CntActionCollection> saveBatchList=new ArrayList<>();
        List<CntActionCollection> updateBatchList=new ArrayList<>();
        actionCollectionVos.parallelStream().forEach(e -> {
            if( StringUtils.isBlank(e.getId()) || e.getId() == null ){
                saveBatchList.add(
                        Builder.of(CntActionCollection::new)
                                .with(CntActionCollection::setId,IdUtils.getSnowflakeNextIdStr())
                                .with(CntActionCollection::setActionId,actionId)
                                .with(CntActionCollection::setCollectionId,e.getCollectionId())
                                .with(CntActionCollection::setTranSvg,e.getTranSvg())
                                .with(CntActionCollection::setActionQuantity,e.getActionQuantity())
                                .with(CntActionCollection::setCreatedBy,SecurityUtils.getUsername())
                                .with(CntActionCollection::setCreatedTime,DateUtils.getNowDate())
                        .build()
                        );
            }else {
                CntActionCollection actionCollection = getOne(Wrappers.<CntActionCollection>lambdaQuery().eq(CntActionCollection::getActionId, actionId).eq(CntActionCollection::getId, e.getId()));
                if(Objects.nonNull(actionCollection)){
                    BeanUtil.copyProperties(e,actionCollection);
                    actionCollection.setUpdatedBy(SecurityUtils.getUsername());
                    actionCollection.setUpdatedTime(DateUtils.getNowDate());
                    updateBatchList.add(actionCollection);
                }else {
                    saveBatchList.add(
                            Builder.of(CntActionCollection::new)
                                    .with(CntActionCollection::setId,IdUtils.getSnowflakeNextIdStr())
                                    .with(CntActionCollection::setActionId,actionId)
                                    .with(CntActionCollection::setCollectionId,e.getCollectionId())
                                    .with(CntActionCollection::setTranSvg,e.getTranSvg())
                                    .with(CntActionCollection::setActionQuantity,e.getActionQuantity())
                                    .with(CntActionCollection::setCreatedBy,SecurityUtils.getUsername())
                                    .with(CntActionCollection::setCreatedTime,DateUtils.getNowDate())
                                    .build()
                    );
                }
            }
        });
        if(saveBatchList.size()>0){
            saveBatch(saveBatchList);
        }
        if(updateBatchList.size()>0){
            updateBatchById(updateBatchList);
        }
        return 1;
    }

    /**
     * 查询当前活动目标藏品总库存
     * @param
     * @return
     */
    @Override
    public List<ActionCollectionDto> totalQuantity(List<String> ids) {
        return cntActionCollectionMapper.totalQuantity(ids);
    }

}
