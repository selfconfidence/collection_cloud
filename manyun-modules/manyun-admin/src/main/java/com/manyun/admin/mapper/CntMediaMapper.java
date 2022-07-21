package com.manyun.admin.mapper;

import java.util.List;

import com.manyun.admin.domain.CntMedia;
import com.manyun.admin.domain.vo.MediaVo;
import org.apache.ibatis.annotations.Param;

/**
 * 媒体存储器Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntMediaMapper
{
    List<MediaVo> initMediaVos(@Param("buiId") String buiId,@Param("modelType") String modelType);

    /**
     * 查询媒体存储器
     *
     * @param id 媒体存储器主键
     * @return 媒体存储器
     */
    public CntMedia selectCntMediaById(String id);

    /**
     * 查询媒体存储器列表
     *
     * @param cntMedia 媒体存储器
     * @return 媒体存储器集合
     */
    public List<CntMedia> selectCntMediaList(CntMedia cntMedia);

    /**
     * 新增媒体存储器
     *
     * @param cntMedia 媒体存储器
     * @return 结果
     */
    public int insertCntMedia(CntMedia cntMedia);

    /**
     * 修改媒体存储器
     *
     * @param cntMedia 媒体存储器
     * @return 结果
     */
    public int updateCntMedia(CntMedia cntMedia);

    /**
     * 删除媒体存储器
     *
     * @param id 媒体存储器主键
     * @return 结果
     */
    public int deleteCntMediaById(String id);

    /**
     * 批量删除媒体存储器
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntMediaByIds(String[] ids);

    /**
     * 批量删除媒体存储器
     *
     * @param ids 需要删除的数据藏品id集合
     * @return 结果
     */
    public int deleteCntMediaByCollectionIds(String[] ids);

    /**
     * 查询媒体存储器列表
     *
     * @param collectionIds 藏品ids
     * @return 媒体存储器集合
     */
    List<MediaVo> selectCntMediaByCollectionIds(@Param("collectionIds") List<String> collectionIds);
}
