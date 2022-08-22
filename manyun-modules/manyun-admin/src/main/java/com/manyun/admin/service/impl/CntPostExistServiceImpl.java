package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.domain.dto.SavePostExistDto;
import com.manyun.admin.domain.query.PostExistQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.bean.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostExistMapper;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.service.ICntPostExistService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提前购配置已经拥有Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-27
 */
@Service
public class CntPostExistServiceImpl extends ServiceImpl<CntPostExistMapper,CntPostExist> implements ICntPostExistService
{
    @Autowired
    private CntPostExistMapper cntPostExistMapper;

    /**
     * 查询提前购配置已经拥有列表
     *
     * @param postExistQuery
     * @return 提前购配置已经拥有
     */
    @Override
    public TableDataInfo<CntPostExistVo> selectCntPostExistList(PostExistQuery postExistQuery)
    {
        PageHelper.startPage(postExistQuery.getPageNum(),postExistQuery.getPageSize());
        List<CntPostExist> postExists = list(Wrappers.<CntPostExist>lambdaQuery().eq(CntPostExist::getConfigId,postExistQuery.getConfigId()).orderByDesc(CntPostExist::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(postExists.parallelStream().map(m->{
            CntPostExistVo cntPostExistVo=new CntPostExistVo();
            BeanUtils.copyProperties(m,cntPostExistVo);
            return cntPostExistVo;
        }).collect(Collectors.toList()), postExists);
    }

    /**
     * 新增提前购配置已经拥有
     *
     * @param savePostExistDto 提前购配置已经拥有
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntPostExist(SavePostExistDto savePostExistDto)
    {
        List<CntPostExistVo> postExistVoList = savePostExistDto.getCntPostExistVoList();
        String configId = savePostExistDto.getConfigId();
        Assert.isTrue(Objects.nonNull(postExistVoList),"新增失败!");
        Set<String> set = postExistVoList.parallelStream().map(CntPostExistVo::getCollectionId).collect(Collectors.toSet());
        Assert.isTrue(postExistVoList.size()==set.size(),"所选藏品不可重复,请重新选择!");
        remove(Wrappers.<CntPostExist>lambdaQuery().eq(CntPostExist::getConfigId,configId));
        if(postExistVoList.size()>0){
            List<CntPostExist> list = postExistVoList.parallelStream().map(m -> {
                return Builder
                        .of(CntPostExist::new)
                        .with(CntPostExist::setId, IdUtils.getSnowflake().nextIdStr())
                        .with(CntPostExist::setConfigId,configId)
                        .with(CntPostExist::setCollectionId,m.getCollectionId())
                        .with(CntPostExist::setCreatedBy,SecurityUtils.getUsername())
                        .with(CntPostExist::setCreatedTime,DateUtils.getNowDate())
                        .build();
            }).collect(Collectors.toList());
            saveBatch(list);
        }
        return 1;
    }

}
