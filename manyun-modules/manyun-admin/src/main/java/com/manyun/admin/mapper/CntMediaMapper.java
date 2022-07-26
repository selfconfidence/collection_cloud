package com.manyun.admin.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manyun.admin.domain.CntMedia;
import com.manyun.admin.domain.vo.MediaVo;
import org.apache.ibatis.annotations.Param;

/**
 * 媒体存储器Mapper接口
 *
 * @author yanwei
 * @date 2022-07-13
 */
public interface CntMediaMapper extends BaseMapper<CntMedia>
{
    List<MediaVo> initMediaVos(@Param("buiId") String buiId,@Param("modelType") String modelType);
}
