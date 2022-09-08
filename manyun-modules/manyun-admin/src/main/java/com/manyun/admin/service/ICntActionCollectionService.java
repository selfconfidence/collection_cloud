package com.manyun.admin.service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntActionCollection;
import com.manyun.admin.domain.dto.SaveActionCollectionDto;
import com.manyun.admin.domain.query.ActionCollectionQuery;
import com.manyun.admin.domain.vo.ActionCollectionVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成目标藏品Service接口
 *
 * @author yanwei
 * @date 2022-09-06
 */
public interface ICntActionCollectionService extends IService<CntActionCollection>
{

    /**
     * 查询活动合成目标藏品列表
     *
     * @param actionCollectionQuery
     * @return 活动合成目标藏品集合
     */
    public TableDataInfo<ActionCollectionVo> selectCntActionCollectionList(ActionCollectionQuery actionCollectionQuery);

    /**
     * 新增活动合成目标藏品
     *
     * @param saveActionCollectionDto 活动合成目标藏品
     * @return 结果
     */
    public int insertCntActionCollection(SaveActionCollectionDto saveActionCollectionDto);

}
