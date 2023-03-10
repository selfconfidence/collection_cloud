package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBox;
import com.manyun.admin.domain.dto.AirdropBalanceDto;
import com.manyun.admin.domain.dto.BoxAirdropDto;
import com.manyun.admin.domain.dto.BoxStateDto;
import com.manyun.admin.domain.dto.CntBoxAlterCombineDto;
import com.manyun.admin.domain.excel.BoxBachAirdopExcel;
import com.manyun.admin.domain.query.BoxQuery;
import com.manyun.admin.domain.query.OrderQuery;
import com.manyun.admin.domain.vo.CntBoxDetailsVo;
import com.manyun.admin.domain.vo.CntBoxOrderVo;
import com.manyun.admin.domain.vo.CntBoxVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     * 修改状态
     * @param boxStateDto
     * @return
     */
    int updateState(BoxStateDto boxStateDto);

    /**
     * 查询盲盒订单列表
     */
    TableDataInfo<CntBoxOrderVo> boxOrderList(OrderQuery orderQuery);

    @Transactional(rollbackFor = Exception.class)
    void taskCheckStatus();

    /***
     * 空投库存
     * @param airdropBalanceDto 空投库存请求参数
     * @return
     */
    R airdropBalance(AirdropBalanceDto airdropBalanceDto);

    /***
     * 空投
     * @param boxAirdropDto 空投请求参数
     * @return
     */
    R airdrop(BoxAirdropDto boxAirdropDto);

    /***
     * 批量空投
     * @param boxBachAirdopExcels 批量空投请求参数
     * @return
     */
    R postExcelList(List<BoxBachAirdopExcel> boxBachAirdopExcels);
}
