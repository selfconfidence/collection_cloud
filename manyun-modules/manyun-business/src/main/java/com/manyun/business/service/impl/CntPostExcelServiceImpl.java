package com.manyun.business.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.CntPostExcel;
import com.manyun.business.domain.entity.CntPostExcelLog;
import com.manyun.business.mapper.CntPostExcelMapper;
import com.manyun.business.service.ICntPostConfigService;
import com.manyun.business.service.ICntPostExcelLogService;
import com.manyun.business.service.ICntPostExcelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 提前购表格 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@Service
@Slf4j
public class CntPostExcelServiceImpl extends ServiceImpl<CntPostExcelMapper, CntPostExcel> implements ICntPostExcelService {

    @Autowired
    private ICntPostExcelLogService iCntPostExcelLogService;

    @Autowired
    private ICntPostConfigService cntPostConfigService;


    /**
     * 是否可以提前购
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public Boolean isExcelPostCustomer(String userId, String buiId) {
         // 可能有重复数据
        CntPostExcel excel = getCntPostExcel(userId, buiId);
        if (Objects.isNull(excel))return Boolean.FALSE;
        // 是否购买到达这个次数了?
        CntPostExcelLog postExcelLog = getPostExcelLog(userId, excel);
            // 数据不完整性，必须限制此操作，否则 null
            if (Objects.isNull(postExcelLog))return Boolean.TRUE;
            return postExcelLog.getBuyFrequency().compareTo(excel.getBuyFrequency()) <0;

    }


    /**
     * 硬执行，并不会抛异常信息！
     * 顺序执行 _ 如果不是表格 就执行配置提前购次数
     * @param userId
     * @param buiId
     */
    @Override
    public void orderExec(String userId, String buiId) {
        try {
            // 1. 首先查找当前 excel的逻辑体 -找到
            // 1.1 查询这个用户对当前记录是否有存根，如果有的话，购买次数 +1 负责新增默认为1 即可，完事退出本次操作！
            CntPostExcel cntPostExcel = getCntPostExcel(userId, buiId);
            if (Objects.nonNull(cntPostExcel)){
                CntPostExcelLog postExcelLog = getPostExcelLog(userId, cntPostExcel);
                if (Objects.nonNull(postExcelLog) && postExcelLog.getBuyFrequency() < cntPostExcel.getBuyFrequency()){
                    postExcelLog.setBuyFrequency(postExcelLog.getBuyFrequency() +1);
                    iCntPostExcelLogService.updateById(postExcelLog);
                    return;
                }
                postExcelLog = Builder.of(CntPostExcelLog::new)
                        .with(CntPostExcelLog::setId, IdUtil.getSnowflake().nextIdStr())
                        .with(CntPostExcelLog::setExcelId, cntPostExcel.getId())
                        .with(CntPostExcelLog::setBuyFrequency, Integer.valueOf(1))
                        .with(CntPostExcelLog::setUserId, userId)
                        .build();
                iCntPostExcelLogService.save(postExcelLog);
                return;
            }
            //2. 未找到就走 配置表的逻辑体
            cntPostConfigService.orderExec(userId,buiId);


        }catch (Exception e){
            log.error("提前购保存购买次数出错了！！！", e);
        }
    }

    private CntPostExcel getCntPostExcel(String userId, String buiId) {
        CntPostExcel excel = getOne(Wrappers.<CntPostExcel>lambdaQuery().eq(CntPostExcel::getBuiId, buiId).eq(CntPostExcel::getUserId, userId));
        return excel;
    }

    private CntPostExcelLog getPostExcelLog(String userId, CntPostExcel excel) {
        CntPostExcelLog cntPostExcelLog = iCntPostExcelLogService.getOne(Wrappers.<CntPostExcelLog>lambdaQuery().eq(CntPostExcelLog::getUserId, userId).eq(CntPostExcelLog::getExcelId, excel.getId()));
         return cntPostExcelLog;
    }


    /**
     * 获取excel 中，用户实际购买后剩余的次数
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public int getSellNum(String userId, String buiId){
        CntPostExcel cntPostExcel = getCntPostExcel(userId, buiId);
        if (Objects.isNull(cntPostExcel)) return 0;
        CntPostExcelLog postExcelLog = getPostExcelLog(userId, cntPostExcel);
        if (Objects.isNull(postExcelLog)) return 0;

        return  postExcelLog.getBuyFrequency();
    }
}
