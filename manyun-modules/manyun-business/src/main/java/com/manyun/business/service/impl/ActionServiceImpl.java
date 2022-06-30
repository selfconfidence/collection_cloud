package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Action;
import com.manyun.business.domain.entity.ActionRecord;
import com.manyun.business.domain.vo.SynthesisVo;
import com.manyun.business.domain.vo.SyntheticRecordVo;
import com.manyun.business.mapper.CntActionMapper;
import com.manyun.business.service.IActionRecordService;
import com.manyun.business.service.IActionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 活动表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class ActionServiceImpl extends ServiceImpl<CntActionMapper, Action> implements IActionService {

    @Resource
    private CntActionMapper actionMapper;

    @Autowired
    private IActionRecordService actionRecordService;

    /**
     * 查询合成列表
     * @return
     */
    @Override
    public TableDataInfo<SynthesisVo> synthesisList() {
        List<Action> actionList = list(Wrappers.<Action>lambdaQuery().orderByAsc(Action::getActionStatus));
        return TableDataInfoUtil.pageTableDataInfo(actionList.parallelStream()
                .map(this::SynthesisVo).collect(Collectors.toList()), actionList);
    }

    /**
     * 查询合成记录列表
     * @return
     */
    @Override
    public TableDataInfo<SyntheticRecordVo> syntheticRecordList() {
        List<ActionRecord> actionRecordList = actionRecordService.list(Wrappers.<ActionRecord>lambdaQuery().orderByDesc(ActionRecord::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(actionRecordList.parallelStream()
                .map(this::SyntheticRecordVo).collect(Collectors.toList()), actionRecordList);
    }

    private SyntheticRecordVo SyntheticRecordVo(ActionRecord actionRecord) {
        SyntheticRecordVo syntheticRecordVo = Builder.of(SyntheticRecordVo::new).build();
        BeanUtil.copyProperties(actionRecord, syntheticRecordVo);
        return syntheticRecordVo;
    }

    private SynthesisVo SynthesisVo(Action action) {
        SynthesisVo synthesisVo = Builder.of(SynthesisVo::new).build();
        BeanUtil.copyProperties(action, synthesisVo);
        return synthesisVo;
    }

}
