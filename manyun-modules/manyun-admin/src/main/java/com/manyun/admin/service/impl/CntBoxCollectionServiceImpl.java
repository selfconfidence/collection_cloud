package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.vo.BoxCollectionDictVo;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.admin.mapper.CntCollectionMapper;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxCollectionMapper;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.service.ICntBoxCollectionService;

/**
 * 盲盒与藏品中间Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-15
 */
@Service
public class CntBoxCollectionServiceImpl implements ICntBoxCollectionService
{
    @Autowired
    private CntBoxCollectionMapper cntBoxCollectionMapper;

    @Autowired
    private CntCollectionMapper cntCollectionMapper;

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param cntBoxCollection 盲盒与藏品中间
     * @return 盲盒与藏品中间
     */
    @Override
    public List<CntBoxCollectionVo> selectCntBoxCollectionList(CntBoxCollection cntBoxCollection)
    {
        return cntBoxCollectionMapper.selectCntBoxCollectionList(cntBoxCollection).stream().map(m ->{
            CntBoxCollectionVo cntBoxCollectionVo=new CntBoxCollectionVo();
            BeanUtil.copyProperties(m,cntBoxCollectionVo);
            return cntBoxCollectionVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增盲盒与藏品中间
     *
     * @param cntBoxCollectionList
     * @return 结果
     */
    @Override
    public int insertCntBoxCollection(List<CntBoxCollection> cntBoxCollectionList)
    {
        if(cntBoxCollectionList.size()==0){
            return 0;
        }
        cntBoxCollectionList.stream().forEach(e -> {
            e.setId(IdUtils.getSnowflakeNextIdStr());
            e.setCreatedBy(SecurityUtils.getUsername());
            e.setCreatedTime(DateUtils.getNowDate());
        });
        return cntBoxCollectionMapper.insertCntBoxCollectionList(cntBoxCollectionList);
    }

    /**
     * 修改盲盒与藏品中间
     *
     * @param cntBoxCollectionList
     * @return 结果
     */
    @Override
    public int updateCntBoxCollection(List<CntBoxCollection> cntBoxCollectionList)
    {
        if(cntBoxCollectionList.isEmpty()){
            return 0;
        }

        List<Integer> list = cntBoxCollectionList.stream().map(m -> {
            m.setUpdatedBy(SecurityUtils.getUsername());
            m.setUpdatedTime(DateUtils.getNowDate());
            int boxCollection = cntBoxCollectionMapper.updateCntBoxCollection(m);
            return boxCollection;
        }).collect(Collectors.toList());

        return list.size()==cntBoxCollectionList.size()?1:0;
    }

    /**
     * 批量删除盲盒与藏品中间
     *
     * @param ids 需要删除的盲盒与藏品中间主键
     * @return 结果
     */
    @Override
    public int deleteCntBoxCollectionByIds(String[] ids)
    {
        return cntBoxCollectionMapper.deleteCntBoxCollectionByIds(ids);
    }

    /***
     * 查询藏品字典
     */
    @Override
    public List<BoxCollectionDictVo> boxCollectionDict() {
        return cntCollectionMapper.selectCntCollectionList(new CntCollection()).stream().map(m ->{
            BoxCollectionDictVo boxCollectionDictVo=new BoxCollectionDictVo();
            BeanUtil.copyProperties(m,boxCollectionDictVo);
            return boxCollectionDictVo;
        }).collect(Collectors.toList());
    }
}
