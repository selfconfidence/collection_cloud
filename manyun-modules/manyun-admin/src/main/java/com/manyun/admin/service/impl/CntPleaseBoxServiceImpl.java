package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;

import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.PleaseBoxQuery;
import com.manyun.admin.domain.vo.CntPleaseBoxVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntPleaseBoxMapper;
import com.manyun.admin.domain.CntPleaseBox;
import com.manyun.admin.service.ICntPleaseBoxService;

/**
 * 邀请好友送盲盒规则Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-06
 */
@Service
public class CntPleaseBoxServiceImpl extends ServiceImpl<CntPleaseBoxMapper,CntPleaseBox> implements ICntPleaseBoxService
{
    @Autowired
    private CntPleaseBoxMapper cntPleaseBoxMapper;

    /**
     * 查询邀请好友送盲盒规则详情
     *
     * @param id 邀请好友送盲盒规则主键
     * @return 邀请好友送盲盒规则
     */
    @Override
    public CntPleaseBox selectCntPleaseBoxById(String id)
    {
        return getById(id);
    }

    /**
     * 查询邀请好友送盲盒规则列表
     *
     * @param boxQuery
     * @return 邀请好友送盲盒规则
     */
    @Override
    public TableDataInfo<CntPleaseBoxVo> selectCntPleaseBoxList(PleaseBoxQuery boxQuery)
    {
        PageHelper.startPage(boxQuery.getPageNum(),boxQuery.getPageSize());
        List<CntPleaseBoxVo> cntPleaseBoxes = cntPleaseBoxMapper.selectCntPleaseBoxList(boxQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntPleaseBoxes,cntPleaseBoxes);
    }

    /**
     * 新增邀请好友送盲盒规则
     *
     * @param cntPleaseBox 邀请好友送盲盒规则
     * @return 结果
     */
    @Override
    public int insertCntPleaseBox(CntPleaseBox cntPleaseBox)
    {
        cntPleaseBox.setId(IdUtils.getSnowflakeNextIdStr());
        cntPleaseBox.setCreatedBy(SecurityUtils.getUsername());
        cntPleaseBox.setCreatedTime(DateUtils.getNowDate());
        return save(cntPleaseBox)==true?1:0;
    }

    /**
     * 修改邀请好友送盲盒规则
     *
     * @param cntPleaseBox 邀请好友送盲盒规则
     * @return 结果
     */
    @Override
    public int updateCntPleaseBox(CntPleaseBox cntPleaseBox)
    {
        cntPleaseBox.setUpdatedBy(SecurityUtils.getUsername());
        cntPleaseBox.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntPleaseBox)==true?1:0;
    }

    /**
     * 批量删除邀请好友送盲盒规则
     *
     * @param ids 需要删除的邀请好友送盲盒规则主键
     * @return 结果
     */
    @Override
    public int deleteCntPleaseBoxByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除邀请好友送盲盒规则信息
     *
     * @param id 邀请好友送盲盒规则主键
     * @return 结果
     */
    @Override
    public int deleteCntPleaseBoxById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
