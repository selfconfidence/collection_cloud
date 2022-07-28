package com.manyun.admin.service;

import com.manyun.admin.domain.dto.MyChainxDto;
import com.manyun.admin.domain.vo.MyChainxVo;
import com.manyun.common.core.domain.R;

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
    public List<MyChainxVo> list();

    /**
     * 重新上链
     */
    public R update(MyChainxDto myChainxDto);
}
