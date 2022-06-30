package com.manyun.business.service;

import com.manyun.business.domain.entity.Action;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.SynthesisVo;
import com.manyun.business.domain.vo.SyntheticRecordVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 活动表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IActionService extends IService<Action> {

    TableDataInfo<SynthesisVo> synthesisList();

    TableDataInfo<SyntheticRecordVo> syntheticRecordList();

}
