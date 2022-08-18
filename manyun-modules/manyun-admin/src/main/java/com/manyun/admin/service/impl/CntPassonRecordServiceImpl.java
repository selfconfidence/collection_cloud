package com.manyun.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.PassonRecordQuery;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.admin.domain.vo.CntPassonRecordVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntPassonRecordMapper;
import com.manyun.admin.domain.CntPassonRecord;
import com.manyun.admin.service.ICntPassonRecordService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 转赠记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-08-18
 */
@Service
public class CntPassonRecordServiceImpl extends ServiceImpl<CntPassonRecordMapper,CntPassonRecord> implements ICntPassonRecordService
{
    @Autowired
    private CntPassonRecordMapper cntPassonRecordMapper;

    @Autowired
    private ICntMediaService mediaService;

    /**
     * 查询转赠记录列表
     *
     * @param passonRecordQuery 转赠记录
     * @return 转赠记录
     */
    @Override
    public TableDataInfo<CntPassonRecordVo> selectCntPassonRecordList(PassonRecordQuery passonRecordQuery)
    {
        PageHelper.startPage(passonRecordQuery.getPageNum(),passonRecordQuery.getPageSize());
        List<CntPassonRecord> cntPassonRecords = cntPassonRecordMapper.selectCntPassonRecordList(passonRecordQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntPassonRecords.parallelStream().map(item -> {
            CntPassonRecordVo passonRecordVo = new CntPassonRecordVo();
            BeanUtil.copyProperties(item, passonRecordVo);
            passonRecordVo.setMediaVos(mediaService.initMediaVos(item.getPictureId(),"0".equals(item.getPictureType())?BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));

            return passonRecordVo;
        }).collect(Collectors.toList()),cntPassonRecords);
    }

}
