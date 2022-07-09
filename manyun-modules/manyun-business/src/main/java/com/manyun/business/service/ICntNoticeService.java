package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntNotice;
import com.manyun.business.domain.query.NoticeQuery;
import com.manyun.business.domain.vo.CntNoticeVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * <p>
 * 通知公告表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-07-08
 */
public interface ICntNoticeService extends IService<CntNotice> {

    TableDataInfo<CntNoticeVo> pageNoticeList(NoticeQuery noticeQuery);
}
