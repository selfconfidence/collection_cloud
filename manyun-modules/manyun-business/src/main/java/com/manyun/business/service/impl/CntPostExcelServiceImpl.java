package com.manyun.business.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.CntPostExcel;
import com.manyun.business.mapper.CntPostExcelMapper;
import com.manyun.business.service.ICntPostExcelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提前购表格 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@Service
public class CntPostExcelServiceImpl extends ServiceImpl<CntPostExcelMapper, CntPostExcel> implements ICntPostExcelService {

    /**
     * 是否可以提前购
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public Boolean isExcelPostCustomer(String userId, String buiId) {
         // 可能有重复数据
        List<CntPostExcel> excelList = list(Wrappers.<CntPostExcel>lambdaQuery().eq(CntPostExcel::getBuiId, buiId).eq(CntPostExcel::getUserId, userId));
        return !excelList.isEmpty();
    }
}
