package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Announcement;
import com.manyun.business.domain.vo.AnnouncementVo;
import com.manyun.business.mapper.AnnouncementMapper;
import com.manyun.business.service.IAnnouncementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.web.page.PageQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author qxh
 * @since 2022-06-23
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

    @Override
    public List<AnnouncementVo> list(PageQuery pageQuery) {
        List<Announcement> announcementList = list(Wrappers.<Announcement>lambdaQuery().orderByDesc(Announcement::getCreatedTime));
        return announcementList.parallelStream().map(this::providerAnnouncementVo).collect(Collectors.toList());
    }

    private AnnouncementVo providerAnnouncementVo(Announcement announcement) {
        AnnouncementVo announcementVo = Builder.of(AnnouncementVo::new).build();
        BeanUtil.copyProperties(announcement, announcementVo);
        return announcementVo;
    }
}
