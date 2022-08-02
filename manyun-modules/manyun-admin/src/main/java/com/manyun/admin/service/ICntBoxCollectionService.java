package com.manyun.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.admin.domain.CntBoxCollection;
import com.manyun.admin.domain.dto.SaveBoxCollectionDto;
import com.manyun.admin.domain.query.BoxCollectionQuery;
import com.manyun.admin.domain.vo.CntBoxCollectionVo;
import com.manyun.common.core.web.page.TableDataInfo;

/**
 * 盲盒与藏品中间Service接口
 *
 * @author yanwei
 * @date 2022-07-15
 */
public interface ICntBoxCollectionService extends IService<CntBoxCollection>
{

    /**
     * 查询盲盒与藏品中间列表
     *
     * @param boxCollectionQuery 盲盒与藏品中间
     * @return 盲盒与藏品中间集合
     */
    public TableDataInfo<CntBoxCollectionVo> selectCntBoxCollectionList(BoxCollectionQuery boxCollectionQuery);

    /**
     * 新增盲盒与藏品中间
     *
     * @param boxCollectionDto
     * @return 结果
     */
    public int insertCntBoxCollection(SaveBoxCollectionDto boxCollectionDto);

}
