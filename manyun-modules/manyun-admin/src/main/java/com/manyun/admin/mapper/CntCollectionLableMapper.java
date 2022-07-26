package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntCollectionLable;
import org.apache.ibatis.annotations.Param;

/**
 * 藏品和标签中间（必须选定个标签,最多为三个）Mapper接口
 *
 * @author yanwei
 * @date 2022-07-19
 */
public interface CntCollectionLableMapper extends BaseMapper<CntCollectionLable>
{

}
