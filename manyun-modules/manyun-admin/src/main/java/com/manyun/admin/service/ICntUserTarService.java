package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntUserTar;
import com.manyun.admin.domain.excel.UserTarExcel;
import com.manyun.admin.domain.query.UserTarQuery;
import com.manyun.admin.domain.vo.CntUserTarVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * 用户抽签购买藏品或盲盒中间Service接口
 *
 * @author yanwei
 * @date 2022-09-08
 */
public interface ICntUserTarService extends IService<CntUserTar>
{

    /**
     * 查询用户抽签购买藏品或盲盒中间列表
     *
     * @param userTarQuery 用户抽签购买藏品或盲盒中间
     * @return 用户抽签购买藏品或盲盒中间集合
     */
    public TableDataInfo<CntUserTarVo> selectCntUserTarList(UserTarQuery userTarQuery);

    /**
     * 批量删除用户抽签购买藏品或盲盒中间
     *
     * @param ids 需要删除的用户抽签购买藏品或盲盒中间主键集合
     * @return 结果
     */
    public int deleteCntUserTarByIds(String[] ids);

    /**
     * 导入抽签记录
     * @param userTarExcelList
     * @return
     */
    R importPostExcel(List<UserTarExcel> userTarExcelList);

}
