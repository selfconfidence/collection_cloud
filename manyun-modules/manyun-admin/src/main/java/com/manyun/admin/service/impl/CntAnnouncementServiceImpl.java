package com.manyun.admin.service.impl;

import java.util.List;
import java.util.Arrays;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.admin.mapper.CntAnnouncementMapper;
import com.manyun.admin.domain.CntAnnouncement;
import com.manyun.admin.service.ICntAnnouncementService;

/**
 * 公告Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-25
 */
@Service
public class CntAnnouncementServiceImpl extends ServiceImpl<CntAnnouncementMapper,CntAnnouncement> implements ICntAnnouncementService
{
    @Autowired
    private CntAnnouncementMapper cntAnnouncementMapper;

    /**
     * 查询公告详情
     *
     * @param id 公告主键
     * @return 公告
     */
    @Override
    public CntAnnouncement selectCntAnnouncementById(String id)
    {
        return getById(id);
    }

    /**
     * 查询公告列表
     *
     * @param cntAnnouncement 公告
     * @return 公告
     */
    @Override
    public List<CntAnnouncement> selectCntAnnouncementList(CntAnnouncement cntAnnouncement)
    {
        return cntAnnouncementMapper.selectCntAnnouncementList(cntAnnouncement);
    }

    /**
     * 新增公告
     *
     * @param cntAnnouncement 公告
     * @return 结果
     */
    @Override
    public int insertCntAnnouncement(CntAnnouncement cntAnnouncement)
    {
        cntAnnouncement.setId(IdUtils.getSnowflakeNextIdStr());
        cntAnnouncement.setCreatedBy(SecurityUtils.getUsername());
        cntAnnouncement.setCreatedTime(DateUtils.getNowDate());
        return save(cntAnnouncement)==true?1:0;
    }

    /**
     * 修改公告
     *
     * @param cntAnnouncement 公告
     * @return 结果
     */
    @Override
    public int updateCntAnnouncement(CntAnnouncement cntAnnouncement)
    {
        cntAnnouncement.setUpdatedBy(SecurityUtils.getUsername());
        cntAnnouncement.setUpdatedTime(DateUtils.getNowDate());
        return updateById(cntAnnouncement)==true?1:0;
    }

    /**
     * 批量删除公告
     *
     * @param ids 需要删除的公告主键
     * @return 结果
     */
    @Override
    public int deleteCntAnnouncementByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids))==true?1:0;
    }

    /**
     * 删除公告信息
     *
     * @param id 公告主键
     * @return 结果
     */
    @Override
    public int deleteCntAnnouncementById(String id)
    {
        return removeById(id)==true?1:0;
    }
}
