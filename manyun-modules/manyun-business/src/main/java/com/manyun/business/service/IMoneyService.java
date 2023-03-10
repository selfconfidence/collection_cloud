package com.manyun.business.service;

import com.manyun.business.domain.dto.UserMoneyDto;
import com.manyun.business.domain.entity.Money;
import com.baomidou.mybatisplus.extension.service.IService;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.form.SystemWithdrawForm;
import com.manyun.business.domain.query.CheckOrderPayQuery;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.CheckOrderVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.comm.api.domain.form.UserRealMoneyForm;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.page.TableDataInfo;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包表 服务类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
public interface IMoneyService extends IService<Money> {

    void orderBack(String userId, BigDecimal moneyBln, String formInfo);

    @Transactional(rollbackFor = Exception.class)
    void addMoney(String userId, BigDecimal money, String formInfo);

    BigDecimal ordePay(String outHost, String userId, BigDecimal realPayMoney, String formInfo);

    AccountInfoVo accountInfo(String userId);

    void updateAccountInfo(String userId, AccountInfoForm accountInfoForm);

    TableDataInfo<MoneyLogVo> pageMoneyLog(String userId, MoneyLogQuery moneyLogQuery);

    void initUserMoney(String userId);

    R updateUserMoney(UserRealMoneyForm userRealMoneyForm);

    R<String> checkIdentity(String identityNo);

    UserMoneyDto userMoneyInfo(String userId);

    CheckOrderVo checkOrderPayStatus(CheckOrderPayQuery checkOrderPayQuery);

    Boolean checkLlpayStatus(String userId);

    Boolean checkSandAccountStatus(String userId);

    R systemWithdraw(SystemWithdrawForm systemWithdrawForm, String userId);
}
