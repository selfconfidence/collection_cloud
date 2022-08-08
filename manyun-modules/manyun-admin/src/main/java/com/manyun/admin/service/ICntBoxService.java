package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.dto.CntBoxAlterCombineDto;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.CntBoxDetailsVo;
import com.manyun.admin.domain.vo.CntBoxOrderVo;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 盲盒;盲盒主体Service接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface ICntBoxService extends IService<CntBox>
{
    /**
     * 查询盲盒;盲盒主体详情
     *
     * @param id 盲盒;盲盒主体主键
     * @return 盲盒;盲盒主体
     */
    public CntBoxDetailsVo selectCntBoxById(String id);

    /**
     * 查询盲盒;盲盒主体列表
     *
     * @param boxQuery 盲盒;盲盒主体
     * @return 盲盒;盲盒主体集合
     */
    public TableDataInfo<CntBoxVo> selectCntBoxList(BoxQuery boxQuery);

    /**
     * 新增盲盒;盲盒主体
     *
     * @param boxAlterCombineDto
     * @return 结果
     */
    public R insertCntBox(CntBoxAlterCombineDto boxAlterCombineDto);

    /**
     * 修改盲盒;盲盒主体
     *
     * @param boxAlterCombineDto
     * @return 结果
     */
    public R updateCntBox(CntBoxAlterCombineDto boxAlterCombineDto);

    /**
     * 批量删除盲盒;盲盒主体
     *
     * @param ids 需要删除的盲盒;盲盒主体主键集合
     * @return 结果
     */
    public int deleteCntBoxByIds(String[] ids);

    /**
     * 查询盲盒订单列表
     */
    TableDataInfo<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery);

}
