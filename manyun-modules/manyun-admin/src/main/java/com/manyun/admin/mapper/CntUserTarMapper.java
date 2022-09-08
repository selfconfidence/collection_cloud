package com.manyun.admin.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntUserTar;
import com.manyun.admin.domain.query.UserTarQuery;
import com.manyun.admin.domain.vo.CntUserTarVo;

/**
 * 用户抽签购买藏品或盲盒中间Mapper接口
 *
 * @author yanwei
 * @date 2022-09-08
 */
public interface CntUserTarMapper extends BaseMapper<CntUserTar>
{
    /**
     * 查询用户抽签购买藏品或盲盒中间列表
     *
     * @param userTarQuery 用户抽签购买藏品或盲盒中间
     * @return 用户抽签购买藏品或盲盒中间集合
     */
    public List<CntUserTarVo> selectCntUserTarList(UserTarQuery userTarQuery);
}
