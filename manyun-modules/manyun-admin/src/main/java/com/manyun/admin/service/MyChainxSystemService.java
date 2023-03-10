package com.manyun.admin.service;

import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;

import java.util.List;

/**
 * 重试上链Service接口
 *
 * @author yanwei
 * @date 2022-07-25
 */
public interface MyChainxSystemService {

    /**
     * 查询重试上链藏品列表
     */
    public TableDataInfo<MyChainxVo> list(PageQuery pageQuery);

    /**
     * 重新上链
     */
    public R update(MyChainxDto myChainxDto);
}
