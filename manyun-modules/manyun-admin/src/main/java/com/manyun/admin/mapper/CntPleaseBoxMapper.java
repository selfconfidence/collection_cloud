package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntPleaseBox;
import com.manyun.admin.domain.query.PleaseBoxQuery;
import com.manyun.admin.domain.vo.CntPleaseBoxVo;

/**
 * 邀请好友送盲盒规则Mapper接口
 *
 * @author yanwei
 * @date 2022-08-06
 */
public interface CntPleaseBoxMapper extends BaseMapper<CntPleaseBox>
{
    /**
     * 查询邀请好友送盲盒规则列表
     *
     * @param boxQuery
     * @return 邀请好友送盲盒规则集合
     */
    public List<CntPleaseBoxVo> selectCntPleaseBoxList(PleaseBoxQuery boxQuery);
}
