package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostSell;
import com.manyun.admin.domain.dto.SavePostSellDto;
import com.manyun.admin.domain.query.PostSellQuery;
import com.manyun.admin.domain.vo.CntPostSellVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置可以购买Service接口
 *
 * @author yanwei
 * @date 2022-08-19
 */
public interface ICntPostSellService extends IService<CntPostSell>
{

    /**
     * 查询提前购配置可以购买列表
     *
     * @param postSellQuery
     * @return 提前购配置可以购买集合
     */
    public TableDataInfo<CntPostSellVo> selectCntPostSellList(PostSellQuery postSellQuery);

    /**
     * 新增提前购配置可以购买
     *
     * @param savePostSellDto 提前购配置可以购买
     * @return 结果
     */
    public int insertCntPostSell(SavePostSellDto savePostSellDto);

}
