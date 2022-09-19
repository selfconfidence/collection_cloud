package com.manyun.admin.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntMoney;
import com.manyun.admin.domain.dto.UpdateWithDrawDto;
import com.manyun.admin.domain.query.SystemWithdrawQuery;
import com.manyun.admin.domain.vo.CntPostSellVo;
import com.manyun.admin.domain.vo.SystemWithdrawVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.bean.BeanUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntSystemWithdrawMapper;
import com.manyun.admin.domain.CntSystemWithdraw;
import com.manyun.admin.service.ICntSystemWithdrawService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统余额提现Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-17
 */
@Service
public class CntSystemWithdrawServiceImpl extends ServiceImpl<CntSystemWithdrawMapper,CntSystemWithdraw> implements ICntSystemWithdrawService
{
    @Autowired
    private CntSystemWithdrawMapper cntSystemWithdrawMapper;


    /**
     * 查询系统余额提现列表
     *
     * @param systemWithdrawQuery 系统余额提现
     * @return 系统余额提现
     */
    @Override
    public TableDataInfo<SystemWithdrawVo> selectCntSystemWithdrawList(SystemWithdrawQuery systemWithdrawQuery)
    {
        PageHelper.startPage(systemWithdrawQuery.getPageNum(),systemWithdrawQuery.getPageSize());
        List<CntSystemWithdraw> systemWithdraws = cntSystemWithdrawMapper.selectCntSystemWithdrawList(systemWithdrawQuery);
        return TableDataInfoUtil.pageTableDataInfo(systemWithdraws.parallelStream().map(m->{
            SystemWithdrawVo systemWithdrawVo = new SystemWithdrawVo();
            BeanUtils.copyProperties(m,systemWithdrawVo);
            return systemWithdrawVo;
        }).collect(Collectors.toList()), systemWithdraws);
    }

    /**
     * 修改打款状况
     * @param withDrawDto
     * @return
     */
    @Override
    public int updateWithdrawStatus(UpdateWithDrawDto withDrawDto) {
        CntSystemWithdraw systemWithdraw = Builder.of(CntSystemWithdraw::new).with(CntSystemWithdraw::setWithdrawStatus, 1).build();
        return update(systemWithdraw, Wrappers.<CntSystemWithdraw>lambdaUpdate().eq(CntSystemWithdraw::getId,withDrawDto.getId())) == true?1:0;
    }

    /**
     * 查询系统余额提现列表
     */
    @Override
    public List<SystemWithdrawVo> selectExportList() {
        List<CntSystemWithdraw> systemWithdraws = cntSystemWithdrawMapper.selectCntSystemWithdrawList(new SystemWithdrawQuery());
        return systemWithdraws.parallelStream().map(m->{
            SystemWithdrawVo systemWithdrawVo = new SystemWithdrawVo();
            BeanUtils.copyProperties(m,systemWithdrawVo);
            return systemWithdrawVo;
        }).collect(Collectors.toList());
    }

}
