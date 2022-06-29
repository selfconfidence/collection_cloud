package com.manyun.business.service;

import com.manyun.business.domain.form.TranAccForm;

/**
 * 转赠相关 方法
 */
public interface ITranService {
    void tranTypeToPoint(String userId, TranAccForm tranAccForm);
}
