package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPleaseBox;
import com.manyun.admin.domain.query.PleaseBoxQuery;
import com.manyun.admin.domain.vo.CntFeedbackVo;
import com.manyun.admin.domain.vo.CntPleaseBoxVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 邀请好友送盲盒规则Service接口
 *
 * @author yanwei
 * @date 2022-08-06
 */
public interface ICntPleaseBoxService extends IService<CntPleaseBox>
{
    /**
     * 查询邀请好友送盲盒规则详情
     *
     * @param id 邀请好友送盲盒规则主键
     * @return 邀请好友送盲盒规则
     */
    public CntPleaseBox selectCntPleaseBoxById(String id);

    /**
     * 查询邀请好友送盲盒规则列表
     *
     * @param boxQuery
     * @return 邀请好友送盲盒规则集合
     */
    public TableDataInfo<CntPleaseBoxVo> selectCntPleaseBoxList(PleaseBoxQuery boxQuery);

    /**
     * 新增邀请好友送盲盒规则
     *
     * @param cntPleaseBox 邀请好友送盲盒规则
     * @return 结果
     */
    public int insertCntPleaseBox(CntPleaseBox cntPleaseBox);

    /**
     * 修改邀请好友送盲盒规则
     *
     * @param cntPleaseBox 邀请好友送盲盒规则
     * @return 结果
     */
    public int updateCntPleaseBox(CntPleaseBox cntPleaseBox);

    /**
     * 批量删除邀请好友送盲盒规则
     *
     * @param ids 需要删除的邀请好友送盲盒规则主键集合
     * @return 结果
     */
    public int deleteCntPleaseBoxByIds(String[] ids);

    /**
     * 删除邀请好友送盲盒规则信息
     *
     * @param id 邀请好友送盲盒规则主键
     * @return 结果
     */
    public int deleteCntPleaseBoxById(String id);
}
