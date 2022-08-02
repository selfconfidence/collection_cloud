package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntActionTar;
import com.manyun.admin.domain.dto.SaveActionTarDto;
import com.manyun.admin.domain.query.ActionTarQuery;
import com.manyun.admin.domain.vo.CntActionTarVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 活动合成附属信息Service接口
 *
 * @author yanwei
 * @date 2022-07-21
 */
public interface ICntActionTarService extends IService<CntActionTar>
{

    /**
     * 查询活动合成附属信息列表
     *
     * @param actionTarQuery 活动合成附属信息
     * @return 活动合成附属信息集合
     */
    public TableDataInfo<CntActionTarVo> selectCntActionTarList(ActionTarQuery actionTarQuery);

    /**
     * 新增活动合成附属信息
     *
     * @param saveActionTarDto
     * @return 结果
     */
    public int insertCntActionTar(SaveActionTarDto saveActionTarDto);
}
