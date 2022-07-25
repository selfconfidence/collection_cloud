package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntActionRecord;

/**
 * 活动合成记录Mapper接口
 * 
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionRecordMapper 
{
    /**
     * 查询活动合成记录
     * 
     * @param id 活动合成记录主键
     * @return 活动合成记录
     */
    public CntActionRecord selectCntActionRecordById(String id);

    /**
     * 查询活动合成记录列表
     * 
     * @param cntActionRecord 活动合成记录
     * @return 活动合成记录集合
     */
    public List<CntActionRecord> selectCntActionRecordList(CntActionRecord cntActionRecord);

    /**
     * 新增活动合成记录
     * 
     * @param cntActionRecord 活动合成记录
     * @return 结果
     */
    public int insertCntActionRecord(CntActionRecord cntActionRecord);

    /**
     * 修改活动合成记录
     * 
     * @param cntActionRecord 活动合成记录
     * @return 结果
     */
    public int updateCntActionRecord(CntActionRecord cntActionRecord);

    /**
     * 删除活动合成记录
     * 
     * @param id 活动合成记录主键
     * @return 结果
     */
    public int deleteCntActionRecordById(String id);

    /**
     * 批量删除活动合成记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntActionRecordByIds(String[] ids);
}
