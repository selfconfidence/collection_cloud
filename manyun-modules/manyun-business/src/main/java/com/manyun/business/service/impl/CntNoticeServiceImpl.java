package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.entity.CntNotice;
import com.manyun.business.domain.query.NoticeQuery;
import com.manyun.business.domain.vo.CntNoticeVo;
import com.manyun.business.mapper.CntNoticeMapper;
import com.manyun.business.service.ICntNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.NoticeStatus.NOTICE_STATUS_OK;

/**
 * <p>
 * 通知公告表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-07-08
 */
@Service
public class CntNoticeServiceImpl extends ServiceImpl<CntNoticeMapper, CntNotice> implements ICntNoticeService {

    /**
     * 分页查询对应的公告
     * @param noticeQuery
     * @return
     */
    @Override
    public TableDataInfo<CntNoticeVo> pageNoticeList(NoticeQuery noticeQuery) {
        PageHelper.startPage(noticeQuery.getPageNum(),noticeQuery.getPageSize());
        List<CntNotice> cntNotices = list(Wrappers.<CntNotice>lambdaQuery().eq(Objects.nonNull(noticeQuery.getNoticeType()), CntNotice::getNoticeType, noticeQuery.getNoticeType()).eq(CntNotice::getStatus, NOTICE_STATUS_OK.getCode()).orderByDesc(CntNotice::getCreateTime));
        return TableDataInfoUtil.pageTableDataInfo(cntNotices.parallelStream().map(this::initCntNoticeVo).collect(Collectors.toList()), cntNotices);
    }

    private CntNoticeVo initCntNoticeVo(CntNotice cntNotice) {
        CntNoticeVo cntNoticeVo = Builder.of(CntNoticeVo::new).build();
        BeanUtil.copyProperties(cntNotice, cntNoticeVo);
        return cntNoticeVo;
    }
}
