package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntActionTar;
import org.apache.ibatis.annotations.Param;

/**
 * 活动合成附属信息Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionTarMapper
{
    /**
     * 查询活动合成附属信息
     *
     * @param id 活动合成附属信息主键
     * @return 活动合成附属信息
     */
    public CntActionTar selectCntActionTarById(String id);

    /**
     * 查询活动合成附属信息列表
     *
     * @param cntActionTar 活动合成附属信息
     * @return 活动合成附属信息集合
     */
    public List<CntActionTar> selectCntActionTarList(CntActionTar cntActionTar);

    /**
     * 新增活动合成附属信息
     *
     * @param cntActionTar 活动合成附属信息
     * @return 结果
     */
    public int insertCntActionTar(CntActionTar cntActionTar);

    /**
     * 批量新增活动合成附属信息
     *
     * @param list 活动合成附属信息集合
     * @return 结果
     */
    public int insertCntActionTars(@Param("list") List<CntActionTar> list);

    /**
     * 修改活动合成附属信息
     *
     * @param cntActionTar 活动合成附属信息
     * @return 结果
     */
    public int updateCntActionTar(CntActionTar cntActionTar);

    /**
     * 删除活动合成附属信息
     *
     * @param id 活动合成附属信息主键
     * @return 结果
     */
    public int deleteCntActionTarById(@Param("id") String id,@Param("actionId") String actionId);

    /**
     * 批量删除活动合成附属信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntActionTarByIds(String[] ids);

}
