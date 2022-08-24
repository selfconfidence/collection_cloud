package com.manyun.business.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.CntPassonRecord;
import com.manyun.business.domain.form.TranAccForm;
import com.manyun.business.service.*;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Objects;

import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;

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

    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IBoxService boxService;

    @Autowired
    private ICntPassonRecordService passonRecordService;


    /**
     * 开始转赠
     * @param cntUserDto
     * @param tranAccForm
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void tranTypeToPoint(CntUserDto cntUserDto, TranAccForm tranAccForm) {
        //1.判定类型 决定是 藏品还是盲盒。 && 判定收入方用户是否有效。&& 查询当前用户是否正常拥有这个 藏品 和盲盒
        CntUserDto touserDto = checkAll(cntUserDto.getId(),tranAccForm);
        //2.进行转赠,调用转赠链 增加流转记录, 部分日志新增
        Integer accFormType = tranAccForm.getType();
        CntCollection collection = null;
        Box box = null;
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(accFormType)){
            // 转让藏品 || 需上链
            String collectionId = userCollectionService.tranCollection(cntUserDto.getId(),touserDto.getId(),tranAccForm.getBuiId());
            collection = collectionService.getById(collectionId);
        }else if(BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(accFormType)){
            // 转让盲盒
            String boxId = userBoxService.tranBox(cntUserDto.getId(),touserDto.getId(),tranAccForm.getBuiId());
            box = boxService.getById(boxId);
        }else {
            throw new ServiceException("转让有误,请重试!");
        }
        //转赠记录
        passonRecordService.save(
                Builder
                .of(CntPassonRecord::new)
                .with(CntPassonRecord::setId, IdUtil.getSnowflake().nextIdStr())
                .with(CntPassonRecord::setOldUserId, cntUserDto.getId())
                .with(CntPassonRecord::setOldNickName, cntUserDto.getNickName())
                .with(CntPassonRecord::setOldUserPhone, cntUserDto.getPhone())
                .with(CntPassonRecord::setNewUserId, touserDto.getId())
                .with(CntPassonRecord::setNewNickName, touserDto.getNickName())
                .with(CntPassonRecord::setNewUserPhone, touserDto.getPhone())
                .with(CntPassonRecord::setGoodsId, accFormType==0?collection.getId():box.getId())
                .with(CntPassonRecord::setGoodsName, accFormType==0?collection.getCollectionName():box.getBoxTitle())
                .with(CntPassonRecord::setPictureId, accFormType==0?collection.getId():box.getId())
                .with(CntPassonRecord::setPictureType, accFormType.toString())
                .with(CntPassonRecord::setPrice, accFormType==0?collection.getRealPrice():box.getRealPrice())
                .with(CntPassonRecord::createD, cntUserDto.getId())
                .build()
        );
    }

    private CntUserDto checkAll(String userId, TranAccForm tranAccForm) {
        // 收方信息是否正确
        R<CntUserDto> cntUserDtoR = remoteBuiUserService.commUni(tranAccForm.getCommUni(), SecurityConstants.INNER);
        Assert.isTrue(cntUserDtoR.getCode() == R.SUCCESS && Objects.nonNull(cntUserDtoR.getData()),"转赠方信息有误,请核实!\n{}",cntUserDtoR.getMsg());
        Assert.isTrue(OK_REAL.getCode().equals(cntUserDtoR.getData().getIsReal()),"受赠方暂未实名认证!");
        // 转方信息是否准确  查询当前用户是否正常拥有这个 藏品 和盲盒
        Integer accFormType = tranAccForm.getType();


        // 藏品
        if (BusinessConstants.ModelTypeConstant.COLLECTION_TAYPE.equals(accFormType))
            Assert.isTrue(userCollectionService.existUserCollection(userId,tranAccForm.getBuiId()),"选择的藏品有误,请核实藏品详细信息,确定藏品是否上链!");

        // 盲盒
        if (BusinessConstants.ModelTypeConstant.BOX_TAYPE.equals(accFormType))
            Assert.isTrue(userBoxService.existUserBox(userId,tranAccForm.getBuiId()),"选择的盲盒有误,请核实盲盒详细信息!");


        return cntUserDtoR.getData();
    }
}
