package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.domain.vo.BoxCollectionDictVo;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;

/**
 * 盲盒与藏品中间Service接口
 *
 * @author yanwei
 * @date 2022-07-15
 */
public interface ICntBoxCollectionService
{

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param cntBoxCollection 盲盒与藏品中间
     * @return 盲盒与藏品中间集合
     */
    public List<CntBoxCollectionVo> selectCntBoxCollectionList(CntBoxCollection cntBoxCollection);

    /**
     * 新增盲盒与藏品中间
     *
     * @param cntBoxCollectionList
     * @return 结果
     */
    public int insertCntBoxCollection(List<CntBoxCollection> cntBoxCollectionList);

    /**
     * 修改盲盒与藏品中间
     *
     * @param cntBoxCollectionList
     * @return 结果
     */
    public int updateCntBoxCollection(List<CntBoxCollection> cntBoxCollectionList);

    /**
     * 批量删除盲盒与藏品中间
     *
     * @param ids 需要删除的盲盒与藏品中间主键集合
     * @return 结果
     */
    public int deleteCntBoxCollectionByIds(String[] ids);

    /***
     * 查询藏品字典
     */
    List<BoxCollectionDictVo> boxCollectionDict();
}
