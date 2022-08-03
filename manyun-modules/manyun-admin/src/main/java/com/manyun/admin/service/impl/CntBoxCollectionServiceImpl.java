package com.manyun.admin.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.dto.SaveBoxCollectionDto;
import com.manyun.admin.domain.query.BoxCollectionQuery;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxCollectionMapper;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.service.ICntBoxCollectionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 盲盒与藏品中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-15
 */
@Service
public class CntBoxCollectionServiceImpl extends ServiceImpl<CntBoxCollectionMapper,CntBoxCollection> implements ICntBoxCollectionService
{
    @Autowired
    private CntBoxCollectionMapper cntBoxCollectionMapper;

    @Autowired
    private ICntCollectionService collectionService;

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param boxCollectionQuery 盲盒与藏品中间
     * @return 盲盒与藏品中间
     */
    @Override
    public TableDataInfo<CntBoxCollectionVo> selectCntBoxCollectionList(BoxCollectionQuery boxCollectionQuery)
    {
        PageHelper.startPage(boxCollectionQuery.getPageNum(),boxCollectionQuery.getPageSize());
        List<CntBoxCollection> cntBoxCollections = cntBoxCollectionMapper.selectSearchBoxCollectionList(boxCollectionQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntBoxCollections.parallelStream().map(m ->{
            CntBoxCollectionVo cntBoxCollectionVo=new CntBoxCollectionVo();
            BeanUtil.copyProperties(m,cntBoxCollectionVo);
            return cntBoxCollectionVo;
        }).collect(Collectors.toList()),cntBoxCollections);
    }

    /**
     * 新增盲盒与藏品中间
     *
     * @param boxCollectionDto
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntBoxCollection(SaveBoxCollectionDto boxCollectionDto)
    {
        String boxId = boxCollectionDto.getBoxId();
        List<CntBoxCollectionVo> cntBoxCollectionVoList = boxCollectionDto.getCntBoxCollectionVos();
        Assert.isTrue(Objects.nonNull(cntBoxCollectionVoList),"新增失败!");
        BigDecimal tranSvgSum = cntBoxCollectionVoList.stream().map(CntBoxCollectionVo::getTranSvg).reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.isFalse(tranSvgSum.compareTo(BigDecimal.valueOf(100.00))==1,"概率总和不得超过100%!");
        remove(Wrappers.<CntBoxCollection>lambdaQuery().eq(CntBoxCollection::getBoxId,boxId));
        if(cntBoxCollectionVoList.size()>0){
            List<String> collectionIds = cntBoxCollectionVoList.stream().map(CntBoxCollectionVo::getCollectionId).collect(Collectors.toList());
            List<CntCollection> collectionList = collectionService.listByIds(collectionIds);
            List<CntBoxCollection> boxCollectionList = cntBoxCollectionVoList.stream().map(m -> {
                CntBoxCollection cntBoxCollection = new CntBoxCollection();
                BeanUtil.copyProperties(m, cntBoxCollection);
                cntBoxCollection.setId(IdUtils.getSnowflakeNextIdStr());
                cntBoxCollection.setBoxId(boxId);
                Optional<CntCollection> first = collectionList.stream().filter(ff -> ff.getId().equals(m.getCollectionId())).findFirst();
                if(first.isPresent()){
                    cntBoxCollection.setCollectionName(first.get().getCollectionName());
                }
                cntBoxCollection.setCreatedBy(SecurityUtils.getUsername());
                cntBoxCollection.setCreatedTime(DateUtils.getNowDate());
                return cntBoxCollection;
            }).collect(Collectors.toList());
            saveBatch(boxCollectionList);
        }
        return 1;
    }

}
