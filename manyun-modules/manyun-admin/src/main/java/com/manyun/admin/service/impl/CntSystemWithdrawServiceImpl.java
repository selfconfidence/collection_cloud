package com.manyun.admin.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.admin.domain.CntMoney;
import com.manyun.admin.domain.CntUser;
import com.manyun.admin.domain.dto.UpdateWithDrawDto;
import com.manyun.admin.domain.query.SystemWithdrawQuery;
import com.manyun.admin.domain.vo.CntPostSellVo;
import com.manyun.admin.domain.vo.SystemWithdrawVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.admin.service.ICntMoneyService;
import com.manyun.admin.service.ICntUserService;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.utils.bean.BeanUtils;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import com.manyun.common.pays.abs.impl.AliComm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.manyun.admin.mapper.CntSystemWithdrawMapper;
import com.manyun.admin.domain.CntSystemWithdraw;
import com.manyun.admin.service.ICntSystemWithdrawService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 系统余额提现Service业务层处理
 *
 * @author yanwei
 * @date 2022-09-17
 */
@Service
public class CntSystemWithdrawServiceImpl extends ServiceImpl<CntSystemWithdrawMapper,CntSystemWithdraw> implements ICntSystemWithdrawService
{
    @Autowired
    private CntSystemWithdrawMapper cntSystemWithdrawMapper;

    @Autowired
    private AliComm aliComm;

    @Autowired
    private ICntMoneyService moneyService;

    @Autowired
    private ICntUserService userService;


    /**
     * 查询系统余额提现列表
     *
     * @param systemWithdrawQuery 系统余额提现
     * @return 系统余额提现
     */
    @Override
    public TableDataInfo<SystemWithdrawVo> selectCntSystemWithdrawList(SystemWithdrawQuery systemWithdrawQuery)
    {
        PageHelper.startPage(systemWithdrawQuery.getPageNum(),systemWithdrawQuery.getPageSize());
        List<CntSystemWithdraw> systemWithdraws = cntSystemWithdrawMapper.selectCntSystemWithdrawList(systemWithdrawQuery);
        return TableDataInfoUtil.pageTableDataInfo(systemWithdraws.parallelStream().map(m->{
            SystemWithdrawVo systemWithdrawVo = new SystemWithdrawVo();
            BeanUtils.copyProperties(m,systemWithdrawVo);
            return systemWithdrawVo;
        }).collect(Collectors.toList()), systemWithdraws);
    }

    /**
     * 修改打款状况
     * @param withDrawDto
     * @return
     */
    @Override
    @Transactional
    public void updateWithdrawStatus(UpdateWithDrawDto withDrawDto) {
        CntSystemWithdraw withdraw = getById(withDrawDto.getId());

        String orderNo = IdUtil.objectId();

        String orderInfo = "";
        withdraw.setOrderNo(orderNo);
        try {
            orderInfo = aliComm.aliTransfer(withdraw.getAliAccount(), withdraw.getRealWithdrawAmount(), withdraw.getUserName(), orderNo);
            if (StrUtil.isNotBlank(orderInfo)) {
                JSONObject jsonObject = JSON.parseObject(orderInfo).getJSONObject("alipay_fund_trans_uni_transfer_response");
                String code = jsonObject.getString("code");
                String msg = jsonObject.getString("msg");
                Assert.isTrue(JSON.parseObject(orderInfo).getJSONObject("alipay_fund_trans_uni_transfer_response").getString("code").equals("10000"),msg);
                if ("10000".equals(code)) {
                    withdraw.setWithdrawStatus(1);

                } else {
                    withdraw.setWithdrawStatus(0);
                }
                withdraw.setWithdrawMsg(msg);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        updateById(withdraw);


        //CntSystemWithdraw systemWithdraw = Builder.of(CntSystemWithdraw::new).with(CntSystemWithdraw::setWithdrawStatus, 1).build();
        //return update(systemWithdraw, Wrappers.<CntSystemWithdraw>lambdaUpdate().eq(CntSystemWithdraw::getId,withDrawDto.getId())) == true?1:0;
    }

    @Override
    @Transactional
    public void cancelWithDraw(UpdateWithDrawDto withDrawDto) {
        CntSystemWithdraw withdraw = getById(withDrawDto.getId());
        Assert.isTrue(Objects.nonNull(withdraw), "未找到该打款订单，请核实");
        Assert.isTrue(Integer.valueOf(0).equals(withdraw.getWithdrawStatus()), "打款状态有误，请核实");
        withdraw.setWithdrawStatus(2);
        CntMoney cntMoney = null;
        //兼容旧数据
        if (StrUtil.isNotBlank(withdraw.getUserId())) {
            cntMoney = moneyService.getOne(Wrappers.<CntMoney>lambdaQuery().eq(CntMoney::getUserId, withdraw.getUserId()));
        } else {
            CntUser cntUser = userService.getOne(Wrappers.<CntUser>lambdaQuery().eq(CntUser::getPhone, withdraw.getPhone()));
            cntMoney = moneyService.getOne(Wrappers.<CntMoney>lambdaQuery().eq(CntMoney::getUserId, cntUser.getId()));
        }
        cntMoney.setMoneyBalance(cntMoney.getMoneyBalance().add(withdraw.getWithdrawAmount()));
        moneyService.updateById(cntMoney);
        updateById(withdraw);

    }

    /**
     * 查询系统余额提现列表
     */
    @Override
    public List<SystemWithdrawVo> selectExportList() {
        List<CntSystemWithdraw> systemWithdraws = cntSystemWithdrawMapper.selectCntSystemWithdrawList(new SystemWithdrawQuery());
        return systemWithdraws.parallelStream().map(m->{
            SystemWithdrawVo systemWithdrawVo = new SystemWithdrawVo();
            BeanUtils.copyProperties(m,systemWithdrawVo);
            return systemWithdrawVo;
        }).collect(Collectors.toList());
    }

}
