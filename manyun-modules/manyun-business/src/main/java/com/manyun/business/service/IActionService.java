package com.manyun.business.service;

import com.manyun.business.domain.entity.Action;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.SynthesisVo;
import com.manyun.business.domain.vo.SynthesizeNowVo;
import com.manyun.business.domain.vo.SyntheticActivityVo;
import com.manyun.business.domain.vo.SyntheticRecordVo;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
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

    TableDataInfo<SyntheticActivityVo> syntheticActivityList();

    TableDataInfo<SyntheticRecordVo> syntheticRecordList(String userId);

    R<SynthesisVo> synthesisInfo(String userId, String id);

    R<SynthesizeNowVo> synthesizeNow(String userId, String userName, String id);

}
