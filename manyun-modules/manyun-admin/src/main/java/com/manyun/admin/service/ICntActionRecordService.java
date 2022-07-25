package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntActionRecord;
import com.manyun.admin.domain.vo.CntActionRecordVo;

/**
 * 活动合成记录Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionRecordService
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
    public List<CntActionRecordVo> selectCntActionRecordList(CntActionRecord cntActionRecord);

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
     * 批量删除活动合成记录
     *
     * @param ids 需要删除的活动合成记录主键集合
     * @return 结果
     */
    public int deleteCntActionRecordByIds(String[] ids);

    /**
     * 删除活动合成记录信息
     *
     * @param id 活动合成记录主键
     * @return 结果
     */
    public int deleteCntActionRecordById(String id);
}
