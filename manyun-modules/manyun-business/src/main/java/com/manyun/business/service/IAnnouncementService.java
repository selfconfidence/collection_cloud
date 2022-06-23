package com.manyun.business.service;

import com.manyun.business.domain.entity.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.vo.AnnouncementVo;
import com.manyun.common.core.web.page.PageQuery;

import java.util.List;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author qxh
 * @since 2022-06-23
 */
public interface IAnnouncementService extends IService<Announcement> {

    List<AnnouncementVo> list(PageQuery pageQuery);

}
