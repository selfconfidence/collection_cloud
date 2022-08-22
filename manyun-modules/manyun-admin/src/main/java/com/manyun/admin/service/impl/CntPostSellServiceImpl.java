package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.domain.dto.SavePostSellDto;
import com.manyun.admin.domain.query.PostSellQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;
import com.manyun.admin.domain.vo.CntPostSellVo;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.bean.BeanUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPostSellMapper;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.service.ICntPostSellService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 提前购配置可以购买Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-19
 */
@Service
public class CntPostSellServiceImpl extends ServiceImpl<CntPostSellMapper,CntPostSell> implements ICntPostSellService
{
    @Autowired
    private CntPostSellMapper cntPostSellMapper;

    /**
     * 查询提前购配置可以购买列表
     *
     * @param postSellQuery
     * @return 提前购配置可以购买
     */
    @Override
    public TableDataInfo<CntPostSellVo> selectCntPostSellList(PostSellQuery postSellQuery)
    {
        PageHelper.startPage(postSellQuery.getPageNum(),postSellQuery.getPageSize());
        List<CntPostSell> cntPostSells = list(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getConfigId,postSellQuery.getConfigId()).orderByDesc(CntPostSell::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(cntPostSells.parallelStream().map(m->{
            CntPostSellVo cntPostSellVo=new CntPostSellVo();
            BeanUtils.copyProperties(m,cntPostSellVo);
            return cntPostSellVo;
        }).collect(Collectors.toList()), cntPostSells);
    }

    /**
     * 新增提前购配置可以购买
     *
     * @param savePostSellDto 提前购配置可以购买
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCntPostSell(SavePostSellDto savePostSellDto)
    {
        List<CntPostSellVo> postSellVoList = savePostSellDto.getCntPostSellVoList();
        String configId = savePostSellDto.getConfigId();
        Assert.isTrue(Objects.nonNull(postSellVoList),"新增失败!");
        Set<String> set = postSellVoList.parallelStream().map(CntPostSellVo::getBuiId).collect(Collectors.toSet());
        Assert.isTrue(postSellVoList.size()==set.size(),"所选藏品不可重复,请重新选择!");
        remove(Wrappers.<CntPostSell>lambdaQuery().eq(CntPostSell::getConfigId,configId));
        if(postSellVoList.size()>0){
            List<CntPostSell> list = postSellVoList.parallelStream().map(m -> {
                return Builder
                        .of(CntPostSell::new)
                        .with(CntPostSell::setId,IdUtils.getSnowflake().nextIdStr())
                        .with(CntPostSell::setConfigId,configId)
                        .with(CntPostSell::setBuiId,m.getBuiId())
                        .with(CntPostSell::setIsType,m.getIsType())
                        .with(CntPostSell::setBuyFrequency,m.getBuyFrequency())
                        .with(CntPostSell::setCreatedBy,SecurityUtils.getUsername())
                        .with(CntPostSell::setCreatedTime,DateUtils.getNowDate())
                        .build();
            }).collect(Collectors.toList());
            saveBatch(list);
        }
        return 1;
    }

}
