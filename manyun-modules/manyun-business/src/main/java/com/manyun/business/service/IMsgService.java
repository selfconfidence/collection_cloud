package com.manyun.business.service;

import com.manyun.business.domain.dto.MsgCommDto;
import com.manyun.business.domain.dto.MsgThisDto;
import com.manyun.business.domain.entity.Msg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.MsgVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 用户消息 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IMsgService extends IService<Msg> {

    void saveMsgThis(MsgThisDto msgThisDto);

    void saveCommMsg(MsgCommDto msgCommDto);

    TableDataInfo<MsgVo> pageMsgList(PageQuery pageQuery);

    TableDataInfo<MsgVo> pageMsgThisList(String userId, PageQuery pageQuery);
}
