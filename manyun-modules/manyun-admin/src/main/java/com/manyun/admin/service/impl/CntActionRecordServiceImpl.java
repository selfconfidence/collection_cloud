package com.manyun.admin.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import com.manyun.admin.domain.vo.CntActionRecordVo;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.utils.uuid.IdUtils;
import com.manyun.common.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntActionRecordMapper;
import com.manyun.admin.domain.CntActionRecord;
import com.manyun.admin.service.ICntActionRecordService;

/**
 * 活动合成记录Service业务层处理
 *
 * @author yanwei
 * @date 2022-07-21
 */
@Service
public class CntActionRecordServiceImpl implements ICntActionRecordService
{
    @Autowired
    private CntActionRecordMapper cntActionRecordMapper;

    /**
     * 查询活动合成记录
     *
     * @param id 活动合成记录主键
     * @return 活动合成记录
     */
    @Override
    public CntActionRecord selectCntActionRecordById(String id)
    {
        return cntActionRecordMapper.selectCntActionRecordById(id);
    }

    /**
     * 查询活动合成记录列表
     *
     * @param cntActionRecord 活动合成记录
     * @return 活动合成记录
     */
    @Override
    public List<CntActionRecordVo> selectCntActionRecordList(CntActionRecord cntActionRecord)
    {
        return cntActionRecordMapper.selectCntActionRecordList(cntActionRecord).stream().map(m ->{
            CntActionRecordVo cntActionRecordVo=new CntActionRecordVo();
            BeanUtil.copyProperties(m,cntActionRecordVo);
            return cntActionRecordVo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增活动合成记录
     *
     * @param cntActionRecord 活动合成记录
     * @return 结果
     */
    @Override
    public int insertCntActionRecord(CntActionRecord cntActionRecord)
    {
        cntActionRecord.setId(IdUtils.getSnowflakeNextIdStr());
        cntActionRecord.setCreatedBy(SecurityUtils.getUsername());
        cntActionRecord.setCreatedTime(DateUtils.getNowDate());
        return cntActionRecordMapper.insertCntActionRecord(cntActionRecord);
    }

    /**
     * 修改活动合成记录
     *
     * @param cntActionRecord 活动合成记录
     * @return 结果
     */
    @Override
    public int updateCntActionRecord(CntActionRecord cntActionRecord)
    {
        cntActionRecord.setUpdatedBy(SecurityUtils.getUsername());
        cntActionRecord.setUpdatedTime(DateUtils.getNowDate());
        return cntActionRecordMapper.updateCntActionRecord(cntActionRecord);
    }

    /**
     * 批量删除活动合成记录
     *
     * @param ids 需要删除的活动合成记录主键
     * @return 结果
     */
    @Override
    public int deleteCntActionRecordByIds(String[] ids)
    {
        return cntActionRecordMapper.deleteCntActionRecordByIds(ids);
    }

    /**
     * 删除活动合成记录信息
     *
     * @param id 活动合成记录主键
     * @return 结果
     */
    @Override
    public int deleteCntActionRecordById(String id)
    {
        return cntActionRecordMapper.deleteCntActionRecordById(id);
    }
}
