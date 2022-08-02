package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntAnnouncement;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 公告Service接口
 *
 * @author yanwei
 * @date 2022-07-25
 */
public interface ICntAnnouncementService extends IService<CntAnnouncement>
{
    /**
     * 查询公告详情
     *
     * @param id 公告主键
     * @return 公告
     */
    public CntAnnouncement selectCntAnnouncementById(String id);

    /**
     * 查询公告列表
     *
     * @param pageQuery
     * @return 公告集合
     */
    public TableDataInfo<CntAnnouncement> selectCntAnnouncementList(PageQuery pageQuery);

    /**
     * 新增公告
     *
     * @param cntAnnouncement 公告
     * @return 结果
     */
    public int insertCntAnnouncement(CntAnnouncement cntAnnouncement);

    /**
     * 修改公告
     *
     * @param cntAnnouncement 公告
     * @return 结果
     */
    public int updateCntAnnouncement(CntAnnouncement cntAnnouncement);

    /**
     * 批量删除公告
     *
     * @param ids 需要删除的公告主键集合
     * @return 结果
     */
    public int deleteCntAnnouncementByIds(String[] ids);

    /**
     * 删除公告信息
     *
     * @param id 公告主键
     * @return 结果
     */
    public int deleteCntAnnouncementById(String id);
}
