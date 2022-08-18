package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.CntCustomerService;
import com.manyun.admin.domain.query.CateQuery;
import com.manyun.admin.domain.vo.CntCateVo;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntCateMapper;
import com.manyun.admin.domain.CntCate;
import com.manyun.admin.service.ICntCateService;

/**
 * 藏品系列_分类Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CntCateServiceImpl extends ServiceImpl<CntCateMapper,CntCate> implements ICntCateService
{
    @Autowired
    private CntCateMapper cntCateMapper;

    @Autowired
    private ICntCollectionService collectionService;

    @Autowired
    private ICntBoxService boxService;

    /**
     * 查询藏品系列_分类详情
     *
     * @param id 藏品系列_分类主键
     * @return 藏品系列_分类
     */
    @Override
    public CntCate selectCntCateById(String id)
    {
        return getById(id);
    }

    /**
     * 查询藏品系列_分类列表
     *
     * @param cateQuery
     * @return 藏品系列_分类
     */
    @Override
    public TableDataInfo<CntCateVo> selectCntCateList(CateQuery cateQuery)
    {
        PageHelper.startPage(cateQuery.getPageNum(),cateQuery.getPageSize());
        List<CntCate> cntCateList = cntCateMapper.selectSearchCateList(cateQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntCateList.parallelStream().map(e -> {
            CntCateVo cntCateVo=new CntCateVo();
            BeanUtil.copyProperties(e,cntCateVo);
            if("0".equals(e.getParentId())){
                cntCateVo.setParentName("顶级分类");
            }else {
                Optional<CntCate> optional = list().parallelStream().filter(f -> f.getId().equals(e.getParentId())).findFirst();
                if(optional.isPresent()){
                    cntCateVo.setParentName(optional.get().getCateName());
                }
            }
            return cntCateVo;
        }).collect(Collectors.toList()),cntCateList);
    }


    /**
     * 新增藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    @Override
    public int insertCntCate(CntCate cntCate)
    {
        cntCate.setId(IdUtils.getSnowflakeNextIdStr());
        cntCate.setCreatedBy(SecurityUtils.getUsername());
        cntCate.setCreatedTime(DateUtils.getNowDate());
        return save(cntCate)==true?1:0;
    }

    /**
     * 修改藏品系列_分类
     *
     * @param cntCate 藏品系列_分类
     * @return 结果
     */
    @Override
    public int updateCntCate(CntCate cntCate)
    {
        cntCate.setUpdatedBy(SecurityUtils.getUsername());
        cntCate.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntCate)==true?1:0;
    }

    /**
     * 批量删除藏品系列_分类
     *
     * @param ids 需要删除的藏品系列_分类主键
     * @return 结果
     */
    @Override
    public R deleteCntCateByIds(String[] ids)
    {
        List<String> collectionCateIdList = collectionService.list()
                .stream()
                .filter(f -> Arrays.asList(ids).contains(f.getCateId()))
                .map(CntCollection::getCateId)
                .distinct()
                .collect(Collectors.toList());
        List<String> cntBoxCateIdList = boxService.list()
                .stream()
                .filter(f -> Arrays.asList(ids).contains(f.getCateId()))
                .map(CntBox::getCateId)
                .distinct()
                .collect(Collectors.toList());
            if(collectionCateIdList.size()>0 && cntBoxCateIdList.size()>0){
                collectionCateIdList.addAll(cntBoxCateIdList);
                return R.fail("分类id为: "+ StringUtils.join(collectionCateIdList,",")+" 的已被引用,不允许删除!");
            }else if(collectionCateIdList.size()>0 || cntBoxCateIdList.size()>0){
                return R.fail("分类id为: "+ StringUtils.join((collectionCateIdList.size()>0?collectionCateIdList:cntBoxCateIdList),",")+" 的已被引用,不允许删除!");
            }
        return removeByIds(Arrays.asList(ids))==true?R.ok():R.fail();
    }

}
