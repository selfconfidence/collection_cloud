package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBoxScore;
import com.manyun.admin.domain.query.BoxScoreQuery;
import com.manyun.admin.domain.vo.CntBoxScoreVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 盲盒评分Service接口
 *
 * @author yanwei
 * @date 2022-08-19
 */
public interface ICntBoxScoreService extends IService<CntBoxScore>
{
    /**
     * 查询盲盒评分详情
     *
     * @param id 盲盒评分主键
     * @return 盲盒评分
     */
    public CntBoxScore selectCntBoxScoreById(String id);

    /**
     * 查询盲盒评分列表
     *
     * @param boxScoreQuery 盲盒评分
     * @return 盲盒评分集合
     */
    public TableDataInfo<CntBoxScoreVo> selectCntBoxScoreList(BoxScoreQuery boxScoreQuery);

    /**
     * 新增盲盒评分
     *
     * @param cntBoxScore 盲盒评分
     * @return 结果
     */
    public int insertCntBoxScore(CntBoxScore cntBoxScore);

    /**
     * 修改盲盒评分
     *
     * @param cntBoxScore 盲盒评分
     * @return 结果
     */
    public int updateCntBoxScore(CntBoxScore cntBoxScore);

    /**
     * 批量删除盲盒评分
     *
     * @param ids 需要删除的盲盒评分主键集合
     * @return 结果
     */
    public int deleteCntBoxScoreByIds(String[] ids);

    /**
     * 删除盲盒评分信息
     *
     * @param id 盲盒评分主键
     * @return 结果
     */
    public int deleteCntBoxScoreById(String id);
}
