package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.domain.query.ActionTarQuery;
import org.apache.ibatis.annotations.Param;

/**
 * 活动合成附属信息Mapper接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface CntActionTarMapper extends BaseMapper<CntActionTar>
{

    /**
     * 根据条件查询活动合成附属信息列表
     *
     * @param actionTarQuery 活动合成附属信息
     * @return 活动合成附属信息集合
     */
    public List<CntActionTar> selectSearchActionTarList(ActionTarQuery actionTarQuery);

}
