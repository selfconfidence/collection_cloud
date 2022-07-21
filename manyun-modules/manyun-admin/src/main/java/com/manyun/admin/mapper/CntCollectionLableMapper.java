package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntCollectionLable;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品和标签中间（必须选定个标签,最多为三个）Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntCollectionLableMapper
{
    /**
     * 查询藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param id 藏品和标签中间（必须选定个标签,最多为三个）主键
     * @return 藏品和标签中间（必须选定个标签,最多为三个）
     */
    public CntCollectionLable selectCntCollectionLableById(String id);

    /**
     * 查询藏品和标签中间（必须选定个标签,最多为三个）列表
     *
     * @param cntCollectionLable 藏品和标签中间（必须选定个标签,最多为三个）
     * @return 藏品和标签中间（必须选定个标签,最多为三个）集合
     */
    public List<CntCollectionLable> selectCntCollectionLableList(CntCollectionLable cntCollectionLable);

    /**
     * 新增藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param cntCollectionLable 藏品和标签中间（必须选定个标签,最多为三个）
     * @return 结果
     */
    public int insertCntCollectionLable(CntCollectionLable cntCollectionLable);

    /**
     * 新增藏品和标签中间集（必须选定个标签,最多为三个）
     *
     * @param cntCollectionLables 藏品和标签中间集
     * @return 结果
     */
    public int insertCntCollectionLables(@Param("cntCollectionLables") List<CntCollectionLable> cntCollectionLables);

    /**
     * 修改藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param cntCollectionLable 藏品和标签中间（必须选定个标签,最多为三个）
     * @return 结果
     */
    public int updateCntCollectionLable(CntCollectionLable cntCollectionLable);

    /**
     * 删除藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param id,collectionId 通过主键或者藏品id
     * @return 结果
     */
    public int deleteCntCollectionLableById(@Param("id") String id,@Param("collectionId") String collectionId);

    /**
     * 批量删除藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntCollectionLableByIds(String[] ids);


    /**
     * 批量删除藏品和标签中间（必须选定个标签,最多为三个）
     *
     * @param ids 需要删除的数据藏品id集合
     * @return 结果
     */
    public int deleteCntCollectionLableByCollectionIds(String[] ids);
}
