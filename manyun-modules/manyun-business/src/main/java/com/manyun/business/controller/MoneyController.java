package com.manyun.business.controller;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.manyun.business.domain.entity.Money;
import com.manyun.business.domain.form.AccountInfoForm;
import com.manyun.business.domain.query.MoneyLogQuery;
import com.manyun.business.domain.vo.AccountInfoVo;
import com.manyun.business.domain.vo.MoneyLogVo;
import com.manyun.business.service.IMoneyService;
import com.manyun.comm.api.model.LoginBusinessUser;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.web.controller.BaseController;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.security.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * <p>
 * 钱包表 前端控制器
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/money")
@Api(tags = "钱包相关Apis")
public class MoneyController extends BaseController {
    @Autowired
    private IMoneyService moneyService;


    @GetMapping("/thisMoney")
    @ApiOperation(value = "查看自己钱包余额",notes = "返回钱包余额")
    public R<BigDecimal>  thisMoney(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        Money moneyUser = moneyService.getOne(Wrappers.<Money>lambdaQuery().eq(Money::getUserId, notNullLoginBusinessUser.getUserId()));
        return R.ok(moneyUser.getMoneyBalance());
    }


    @PostMapping("/accountInfo")
    @ApiOperation("查询收款账户信息")
    public R<AccountInfoVo> accountInfo(){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(moneyService.accountInfo(notNullLoginBusinessUser.getUserId()));
    }

    @PostMapping("/updateAccountInfo")
    @ApiOperation("完善收款账户信息")
    public R updateAccountInfo(@RequestBody @Valid AccountInfoForm accountInfoForm){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        moneyService.updateAccountInfo(notNullLoginBusinessUser.getUserId(),accountInfoForm);
        return R.ok();
    }

    @PostMapping("/pageMoneyLog")
    @ApiOperation("分页查询钱包的收支记录")
    public R<TableDataInfo<MoneyLogVo>> pageMoneyLog(@RequestBody MoneyLogQuery moneyLogQuery){
        LoginBusinessUser notNullLoginBusinessUser = SecurityUtils.getNotNullLoginBusinessUser();
        return R.ok(moneyService.pageMoneyLog(notNullLoginBusinessUser.getUserId(),moneyLogQuery));
    }



    @GetMapping("/bindCard")
    @ApiOperation("调取银联 一键绑卡 -暂未实现 ")
    public R bindCard(){

        return R.ok();
    }






}

