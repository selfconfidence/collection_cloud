package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.query.AirdropRecordQuery;
import com.manyun.admin.domain.vo.CntAgreementTypeVo;
import com.manyun.admin.domain.vo.CntAirdropRecordVo;
import com.manyun.admin.service.ICntMediaService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntAirdropRecordMapper;
import com.manyun.admin.domain.CntAirdropRecord;
import com.manyun.admin.service.ICntAirdropRecordService;

/**
 * 空投记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-02
 */
@Service
public class CntAirdropRecordServiceImpl extends ServiceImpl<CntAirdropRecordMapper,CntAirdropRecord> implements ICntAirdropRecordService
{
    @Autowired
    private CntAirdropRecordMapper cntAirdropRecordMapper;

    @Autowired
    private ICntMediaService mediaService;

    /**
     * 查询空投记录列表
     *
     * @param airdropRecordQuery 空投记录
     * @return 空投记录
     */
    @Override
    public TableDataInfo<CntAirdropRecordVo> selectCntAirdropRecordList(AirdropRecordQuery airdropRecordQuery)
    {
        PageHelper.startPage(airdropRecordQuery.getPageNum(),airdropRecordQuery.getPageSize());
        List<CntAirdropRecord> cntAirdropRecords = cntAirdropRecordMapper.selectCntAirdropRecordList(airdropRecordQuery);
        return TableDataInfoUtil.pageTableDataInfo(cntAirdropRecords.parallelStream().map(m->{
            CntAirdropRecordVo cntAirdropRecordVo=new CntAirdropRecordVo();
            BeanUtils.copyProperties(m,cntAirdropRecordVo);
            if(StringUtils.isNotBlank(m.getGoodsName()) && m.getGoodsType()!=null){
                cntAirdropRecordVo.setMediaVos(mediaService.initMediaVos(m.getGoodsId(), m.getGoodsType()==0?BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE:BusinessConstants.ModelTypeConstant.BOX_MODEL_TYPE));
            }
            return cntAirdropRecordVo;
        }).collect(Collectors.toList()), cntAirdropRecords);
    }

}
