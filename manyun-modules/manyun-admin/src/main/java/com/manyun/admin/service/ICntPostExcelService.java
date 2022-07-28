package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostExcel;
import com.manyun.admin.domain.excel.PostExcel;
import com.manyun.admin.domain.vo.CntPostExcelVo;
import com.manyun.common.core.domain.R;

/**
 * 提前购格Service接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface ICntPostExcelService extends IService<CntPostExcel>
{

    /**
     * 查询提前购格列表
     *
     * @param cntPostExcel 提前购格
     * @return 提前购格集合
     */
    public List<CntPostExcelVo> selectCntPostExcelList(CntPostExcel cntPostExcel);

    /**
     * 批量删除提前购格
     *
     * @param ids 需要删除的提前购格主键集合
     * @return 结果
     */
    public int deleteCntPostExcelByIds(String[] ids);

    /***
     * 获取导入的数据,并处理
     *
     * @return
     */
    public R importPostExcel(List<PostExcel> postExcelList);
}
