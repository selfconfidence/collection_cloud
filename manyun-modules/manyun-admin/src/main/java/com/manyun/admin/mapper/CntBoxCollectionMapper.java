package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntBoxCollection;
import org.apache.ibatis.annotations.Param;

/**
 * 盲盒与藏品中间Mapper接口
 *
 * @author yanwei
 * @date 2022-07-15
 */
public interface CntBoxCollectionMapper
{
    /**
     * 查询盲盒与藏品中间
     *
     * @param id 盲盒与藏品中间主键
     * @return 盲盒与藏品中间
     */
    public CntBoxCollection selectCntBoxCollectionById(String id);

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param cntBoxCollection 盲盒与藏品中间
     * @return 盲盒与藏品中间集合
     */
    public List<CntBoxCollection> selectCntBoxCollectionList(CntBoxCollection cntBoxCollection);

    /**
     * 新增盲盒与藏品中间
     *
     * @param cntBoxCollection 盲盒与藏品中间
     * @return 结果
     */
    public int insertCntBoxCollection(CntBoxCollection cntBoxCollection);

    /**
     * 批量新增
     *
     * @param cntBoxCollectionList
     * @return 结果
     */
    int insertCntBoxCollectionList(@Param("cntBoxCollectionList") List<CntBoxCollection> cntBoxCollectionList);


    /**
     * 修改盲盒与藏品中间
     *
     * @param cntBoxCollection 盲盒与藏品中间
     * @return 结果
     */
    public int updateCntBoxCollection(CntBoxCollection cntBoxCollection);


    /**
     * 删除盲盒与藏品中间
     *
     * @param id 盲盒与藏品中间主键
     * @return 结果
     */
    public int deleteCntBoxCollectionById(@Param("id") String id,@Param("boxId") String boxId);

    /**
     * 批量删除盲盒与藏品中间
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntBoxCollectionByIds(String[] ids);

}
