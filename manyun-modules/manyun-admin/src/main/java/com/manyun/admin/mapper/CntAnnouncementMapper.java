package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntAnnouncement;

/**
 * 公告Mapper接口
 *
 * @author yanwei
 * @date 2022-07-25
 */
public interface CntAnnouncementMapper extends BaseMapper<CntAnnouncement>
{
    /**
     * 查询公告列表
     *
     * @param cntAnnouncement 公告
     * @return 公告集合
     */
    public List<CntAnnouncement> selectCntAnnouncementList(CntAnnouncement cntAnnouncement);
}
