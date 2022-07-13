package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.dto.LogInfoDto;
import com.manyun.business.domain.entity.Logs;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.business.mapper.MoneyMapper;
import com.manyun.business.service.ILogsService;
import com.manyun.business.service.IMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.DateUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.MONEY_TYPE;

/**
 * <p>
 * 钱包表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class MoneyServiceImpl extends ServiceImpl<MoneyMapper, Money> implements IMoneyService {

    @Autowired
    private ILogsService logsService;





    /**
     * 根据进行进行扣除余额操作 —— 组合支付使用
     * @param userId
     * @param realPayMoney
     * @param formInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal ordePay(String outHost,String userId, BigDecimal realPayMoney, String formInfo) {
        // 剩余金额
        BigDecimal moneyPayMoney = NumberUtil.add(0D);
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        //1. 当前钱包金额是否满足当前扣得金额?
        // 够 - 直接扣除,记录日志
        // 不够，钱包余额扣完，剩余得返回 交给 第三方支付
        //Assert.isTrue(money.getMoneyBalance().compareTo(realPayMoney)>=0,"余额不足,请核实!");
        if (money.getMoneyBalance().compareTo(realPayMoney)>=0){
            // 够扣
            money.setMoneyBalance(NumberUtil.sub(money.getMoneyBalance(),realPayMoney));
            logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo(realPayMoney.toString()).isType(POLL_SOURCE).modelType(MONEY_TYPE).build());
        } else  {
            // 不够扣
            moneyPayMoney = NumberUtil.sub(realPayMoney, money.getMoneyBalance());
            logsService.saveLogs(LogInfoDto.builder().buiId(userId).jsonTxt(formInfo).formInfo( money.getMoneyBalance().toString()).isType(POLL_SOURCE).modelType(MONEY_TYPE).build());
            money.setMoneyBalance(NumberUtil.add(0D));

        }
        money.updateD(userId);
        updateById(money);
         return moneyPayMoney;
    }

    @Override
    public AccountInfoVo accountInfo(String userId) {
        Money moneyUser = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        AccountInfoVo accountInfo = Builder.of(AccountInfoVo::new).build();
        BeanUtil.copyProperties(moneyUser,accountInfo);
        // 是否绑定银行卡了
        return accountInfo;
    }

    @Override
    public void updateAccountInfo(String userId, AccountInfoForm accountInfoForm) {
        Money money = getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        BeanUtil.copyProperties(accountInfoForm,money);
        money.updateD(userId);
        updateById(money);
    }

    /**
     * 分页查询收支明细列表
     * @param userId
     * @param moneyLogQuery
     * @return
     */
    @Override
    public TableDataInfo<MoneyLogVo> pageMoneyLog(String userId, MoneyLogQuery moneyLogQuery) {
        PageHelper.startPage(moneyLogQuery.getPageNum(),moneyLogQuery.getPageSize());
        List<Logs> logsList = logsService.list(Wrappers.<Logs>lambdaQuery().eq(Logs::getBuiId, userId).eq(Logs::getModelType, MONEY_TYPE).eq(Objects.nonNull(moneyLogQuery.getIsType()), Logs::getIsType, moneyLogQuery.getIsType()).last(Objects.nonNull(moneyLogQuery.getCreatedTime()), " and DATE_FORMAT(created_time,'%Y-%m-%d') = " + DateUtils.getDate() + " ").orderByDesc(Logs::getCreatedTime));
        List<MoneyLogVo> moneyLogVos = logsList.parallelStream().map(this::initMoneyLogVo).collect(Collectors.toList());
        return TableDataInfoUtil.pageTableDataInfo(moneyLogVos,logsList);
    }

    @Override
    public void initUserMoney(String userId) {
        // 乐观执行
        long isExist = count(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, userId));
        if (isExist >=1)
            return;
        Money money = Builder.of(Money::new).build();
        money.setUserId(userId);
        money.setId(IdUtil.getSnowflakeNextIdStr());
        money.setMoneyBalance(NumberUtil.add(0D));
        money.createD(userId);
        saveOrUpdate(money);
    }

    private MoneyLogVo initMoneyLogVo(Logs logs) {
        MoneyLogVo moneyLogVo = Builder.of(MoneyLogVo::new).build();
        BeanUtil.copyProperties(logs,moneyLogVo);
        return moneyLogVo;
    }



}
