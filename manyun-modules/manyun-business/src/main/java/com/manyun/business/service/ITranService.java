package com.manyun.business.service;

import com.manyun.business.domain.form.TranAccForm;
import com.manyun.comm.api.domain.dto.CntUserDto;

/**
 * 转赠相关 方法
 */
public interface ITranService {
    void tranTypeToPoint(CntUserDto cntUserDto, TranAccForm tranAccForm);
}
