package com.manyun.admin.service;

import com.manyun.admin.domain.vo.FrontPageVo;
import com.manyun.common.core.domain.R;


public interface IFrontPageService
{

    /**
     * 首页
     * @return
     */
    R<FrontPageVo> list();

}
