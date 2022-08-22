package com.manyun.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntPostExist;
import com.manyun.admin.domain.dto.SavePostExistDto;
import com.manyun.admin.domain.query.PostExistQuery;
import com.manyun.admin.domain.vo.CntPostExistVo;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 提前购配置已经拥有Service接口
 *
 * @author yanwei
 * @date 2022-07-27
 */
public interface ICntPostExistService extends IService<CntPostExist>
{

    /**
     * 查询提前购配置已经拥有列表
     *
     * @param postExistQuery
     * @return 提前购配置已经拥有集合
     */
    public TableDataInfo<CntPostExistVo> selectCntPostExistList(PostExistQuery postExistQuery);

    /**
     * 新增提前购配置已经拥有
     *
     * @param savePostExistDto 提前购配置已经拥有
     * @return 结果
     */
    public int insertCntPostExist(SavePostExistDto savePostExistDto);

}
