package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntCollection;
import com.manyun.admin.domain.vo.CntCollectionDetailsVo;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品Mapper接口
 *
 * @author yanwei
 * @date 2022-07-14
 */
public interface CntCollectionMapper
{
    /**
     * 查询藏品
     *
     * @param id 藏品主键
     * @return 藏品
     */
    public CntCollection selectCntCollectionById(String id);

    /**
     * 查询藏品列表
     *
     * @param cntCollection 藏品
     * @return 藏品集合
     */
    public List<CntCollection> selectCntCollectionList(CntCollection cntCollection);

    /**
     * 新增藏品
     *
     * @param cntCollection 藏品
     * @return 结果
     */
    public int insertCntCollection(CntCollection cntCollection);

    /**
     * 修改藏品
     *
     * @param cntCollection 藏品
     * @return 结果
     */
    public int updateCntCollection(CntCollection cntCollection);

    /**
     * 删除藏品
     *
     * @param id 藏品主键
     * @return 结果
     */
    public int deleteCntCollectionById(String id);

    /**
     * 批量删除藏品
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntCollectionByIds(String[] ids);

    /**
     * 查询藏品列表
     *
     * @param ids 藏品系列id集合
     * @return 藏品集合
     */
    List<CntCollection> selectCntCollectionByCateIds(String[] ids);

    /**
     * 查询藏品详情
     *
     * @param id 藏品主键
     * @return 藏品
     */
    CntCollectionDetailsVo selectCntCollectionDetailsById(String id);

    /**
     * 查询藏品列表
     *
     * @param collectionIds 藏品主键集合
     * @return 藏品集合
     */
    List<CntCollection> selectCntCollectionByIds(@Param("collectionIds") List<String> collectionIds);


}
