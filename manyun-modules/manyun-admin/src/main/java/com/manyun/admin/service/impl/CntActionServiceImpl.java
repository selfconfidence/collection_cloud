package com.manyun.admin.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.domain.query.ActionQuery;
import com.manyun.admin.domain.vo.CntActionVo;
import com.manyun.admin.mapper.CntActionTarMapper;
import com.manyun.admin.service.ICntActionTarService;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntActionMapper;
import com.manyun.admin.domain.CntAction;
import com.manyun.admin.service.ICntActionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-21
 */
@Service
public class CntActionServiceImpl extends ServiceImpl<CntActionMapper,CntAction> implements ICntActionService
{
    @Autowired
    private CntActionMapper cntActionMapper;

    @Autowired
    private ICntActionTarService actionTarService;

    /**
     * 查询活动
     *
     * @param id 活动主键
     * @return 活动
     */
    @Override
    public CntAction selectCntActionById(String id)
    {
        return getById(id);
    }

    /**
     * 查询活动列表
     *
     * @param actionQuery 活动
     * @return 活动
     */
    @Override
    public TableDataInfo<CntActionVo> selectCntActionList(ActionQuery actionQuery)
    {
        PageHelper.startPage(actionQuery.getPageNum(),actionQuery.getPageSize());
        List<CntAction> cntActions = cntActionMapper.selectSearchActionList(actionQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntActions.parallelStream().map(m ->{
            CntActionVo cntActionVo=new CntActionVo();
            BeanUtil.copyProperties(m,cntActionVo);
            return cntActionVo;
        }).collect(Collectors.toList()),cntActions);
    }

    /**
     * 新增活动
     *
     * @param cntAction 活动
     * @return 结果
     */
    @Override
    public int insertCntAction(CntAction cntAction)
    {
        cntAction.setId(IdUtils.getSnowflakeNextIdStr());
        cntAction.setCreatedBy(SecurityUtils.getUsername());
        cntAction.setCreatedTime(DateUtils.getNowDate());
        return save(cntAction)==true?1:0;
    }

    /**
     * 修改活动
     *
     * @param cntAction 活动
     * @return 结果
     */
    @Override
    public int updateCntAction(CntAction cntAction)
    {
        cntAction.setUpdatedBy(SecurityUtils.getUsername());
        cntAction.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntAction)==true?1:0;
    }

    /**
     * 批量删除活动
     *
     * @param ids 需要删除的活动主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCntActionByIds(String[] ids)
    {
        removeByIds(Arrays.asList(ids));
        actionTarService.remove(Wrappers.<CntActionTar>lambdaQuery().in(CntActionTar::getActionId,ids));
        return 1;
    }
}
