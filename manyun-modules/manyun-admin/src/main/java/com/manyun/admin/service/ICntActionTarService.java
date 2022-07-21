package com.manyun.admin.service;

import java.util.List;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.domain.dto.SaveActionTarDto;
import com.manyun.admin.domain.vo.CntActionTarVo;

/**
 * 活动合成附属信息Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionTarService
{

    /**
     * 查询活动合成附属信息列表
     *
     * @param cntActionTar 活动合成附属信息
     * @return 活动合成附属信息集合
     */
    public List<CntActionTarVo> selectCntActionTarList(CntActionTar cntActionTar);

    /**
     * 新增活动合成附属信息
     *
     * @param saveActionTarDto
     * @return 结果
     */
    public int insertCntActionTar(SaveActionTarDto saveActionTarDto);
}
