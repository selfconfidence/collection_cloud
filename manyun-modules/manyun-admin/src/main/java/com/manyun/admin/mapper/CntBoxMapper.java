package com.manyun.admin.mapper;

import java.util.List;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.vo.CntBoxOrderVo;

/**
 * 盲盒;盲盒主体Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntBoxMapper
{
    /**
     * 查询盲盒;盲盒主体
     *
     * @param id 盲盒;盲盒主体主键
     * @return 盲盒;盲盒主体
     */
    public CntBox selectCntBoxById(String id);

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param cntBox 盲盒;盲盒主体
     * @return 盲盒;盲盒主体集合
     */
    public List<CntBox> selectCntBoxList(CntBox cntBox);

    /**
     * 新增盲盒;盲盒主体
     *
     * @param cntBox 盲盒;盲盒主体
     * @return 结果
     */
    public int insertCntBox(CntBox cntBox);

    /**
     * 修改盲盒;盲盒主体
     *
     * @param cntBox 盲盒;盲盒主体
     * @return 结果
     */
    public int updateCntBox(CntBox cntBox);

    /**
     * 删除盲盒;盲盒主体
     *
     * @param id 盲盒;盲盒主体主键
     * @return 结果
     */
    public int deleteCntBoxById(String id);

    /**
     * 批量删除盲盒;盲盒主体
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCntBoxByIds(String[] ids);

    /**
     * 查询盲盒订单列表
     */
    List<CntBoxOrderVo> boxOrderList();
}
