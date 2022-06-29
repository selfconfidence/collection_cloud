package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import com.manyun.business.domain.form.TranAccForm;
import com.manyun.business.service.*;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 转赠相关实现
 */
@Service
public class ITranServiceImpl implements ITranService {

    @Autowired
    private IUserBoxService userBoxService;

    @Autowired
    private IUserCollectionService userCollectionService;


    @Resource
    private RemoteBuiUserService remoteBuiUserService;



    /**
     * 开始转赠
     * @param userId
     * @param tranAccForm
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tranTypeToPoint(String userId, TranAccForm tranAccForm) {
        //1.判定类型 决定是 藏品还是盲盒。 && 判定收入方用户是否有效。&& 查询当前用户是否正常拥有这个 藏品 和盲盒
        String toUserId = checkAll(userId,tranAccForm);
        //2.进行转赠,调用转赠链 增加流转记录, 部分日志新增
        Integer accFormType = tranAccForm.getType();
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(accFormType)){
            // 转让藏品 || 需上链
            userCollectionService.tranCollection(userId,toUserId,tranAccForm.getBuiId());
            return;
        }

        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(accFormType)){
            // 转让盲盒
            userBoxService.tranBox(userId,toUserId,tranAccForm.getBuiId());
            return;
        }
        throw new ServiceException("转让有误,请重试!");
    }

    private String checkAll(String userId, TranAccForm tranAccForm) {
        // 收方信息是否正确
        R<CntUserDto> cntUserDtoR = remoteBuiUserService.commUni(tranAccForm.getCommUni(), SecurityConstants.INNER);
        Assert.isTrue(cntUserDtoR.getCode() == R.SUCCESS && Objects.nonNull(cntUserDtoR.getData()),"转赠方信息有误,请核实!\n{}",cntUserDtoR.getMsg());
        // 转方信息是否准确  查询当前用户是否正常拥有这个 藏品 和盲盒
        Integer accFormType = tranAccForm.getType();


        // 藏品
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(accFormType))
            Assert.isTrue(userCollectionService.existUserCollection(userId,tranAccForm.getBuiId()),"选择的藏品有误,请核实藏品详细信息!");

        // 盲盒
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(accFormType))
            Assert.isTrue(userBoxService.existUserBox(userId,tranAccForm.getBuiId()),"选择的盲盒有误,请核实盲盒详细信息!");


        return cntUserDtoR.getData().getId();

    }
}
