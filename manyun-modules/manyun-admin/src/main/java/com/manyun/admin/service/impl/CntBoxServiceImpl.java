package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.CntBoxOrderVo;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.admin.service.ICntBoxCollectionService;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntBoxMapper;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.service.ICntBoxService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 盲盒;盲盒主体Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-13
 */
@Service
public class CntBoxServiceImpl extends ServiceImpl<CntBoxMapper,CntBox> implements ICntBoxService
{
    @Autowired
    private CntBoxMapper cntBoxMapper;

    @Autowired
    private ICntMediaService mediaService;

    @Autowired
    private ICntBoxCollectionService boxCollectionService;

    /**
     * 查询盲盒;盲盒主体
     *
     * @param id 盲盒;盲盒主体主键
     * @return 盲盒;盲盒主体
     */
    @Override
    public CntBox selectCntBoxById(String id)
    {
        return getById(id);
    }

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param boxQuery 盲盒;盲盒主体
     * @return 盲盒;盲盒主体
     */
    @Override
    public List<CntBoxVo> selectCntBoxList(BoxQuery boxQuery)
    {
        List<CntBox> cntBoxList = cntBoxMapper.selectSearchBoxList(boxQuery);
        return cntBoxList
                .parallelStream()
                .map(item ->
                {
                    CntBoxVo cntBoxVo=new CntBoxVo();
                    BeanUtil.copyProperties(item,cntBoxVo);
                    cntBoxVo.setMediaVos(mediaService.initMediaVos(item.getId(), BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
                    return cntBoxVo;
                }).collect(Collectors.toList());
    }

    /**
     * 新增盲盒;盲盒主体
     *
     * @param cntBox 盲盒;盲盒主体
     * @return 结果
     */
    @Override
    public int insertCntBox(CntBox cntBox)
    {
        cntBox.setId(IdUtils.getSnowflakeNextIdStr());
        cntBox.setCreatedBy(SecurityUtils.getUsername());
        cntBox.setCreatedTime(DateUtils.getNowDate());
        return save(cntBox)==true?1:0;
    }

    /**
     * 修改盲盒;盲盒主体
     *
     * @param cntBox 盲盒;盲盒主体
     * @return 结果
     */
    @Override
    public int updateCntBox(CntBox cntBox)
    {
        cntBox.setUpdatedBy(SecurityUtils.getUsername());
        cntBox.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntBox)==true?1:0;
    }

    /**
     * 批量删除盲盒;盲盒主体
     *
     * @param ids 需要删除的盲盒;盲盒主体主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCntBoxByIds(String[] ids)
    {
        removeByIds(Arrays.asList(ids));
        boxCollectionService.remove(Wrappers.<CntBoxCollection>lambdaQuery().in(CntBoxCollection::getBoxId,ids));
        return 1;
    }

    /**
     * 查询盲盒订单列表
     */
    @Override
    public List<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery) {
        return cntBoxMapper.boxOrderList(orderQuery);
    }

}