package com.manyun.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.entity.CntPostExcel;

/**
 * <p>
 * 提前购表格 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
public interface ICntPostExcelService extends IService<CntPostExcel> {

    Boolean isExcelPostCustomer(String userId, String buiId);
}
