package com.manyun.business.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.manyun.business.domain.entity.Box;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.CntTar;
import com.manyun.business.domain.entity.CntUserTar;
import com.manyun.business.mapper.CntTarMapper;
import com.manyun.business.service.IBoxService;
import com.manyun.business.service.ICntTarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.business.service.ICntUserTarService;
import com.manyun.business.service.ICollectionService;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.comm.api.RemoteSmsService;
import com.manyun.comm.api.domain.dto.BatchSmsCommDto;
import com.manyun.comm.api.domain.dto.CntUserDto;
import com.manyun.comm.api.domain.dto.SmsCommDto;
import com.manyun.comm.api.domain.dto.UserDto;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.core.utils.jg.JpushUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.SmsTemplateNumber.*;
import static com.manyun.common.core.enums.TarResultFlag.FLAG_PROCESS;
import static com.manyun.common.core.enums.TarResultFlag.FLAG_STOP;
import static com.manyun.common.core.enums.TarStatus.*;
import static com.manyun.common.core.enums.TarType.BOX_TAR;
import static com.manyun.common.core.enums.TarType.COLLECTION_TAR;
import static com.manyun.common.core.enums.UserRealStatus.OK_REAL;
import static com.manyun.common.core.enums.UserTaSell.NO_SELL;
import static com.manyun.common.core.enums.UserTaSell.SELL_OK;

/**
 * <p>
 * ????????????(??????,??????) ???????????????
 * </p>
 *
 * @author yanwei
 * @since 2022-06-27
 */
@Service
public class CntTarServiceImpl extends ServiceImpl<CntTarMapper, CntTar> implements ICntTarService {

    @Autowired
    private ICntUserTarService userTarService;

    @Autowired
    private JpushUtil jpushUtil;

    @Resource
    private RemoteBuiUserService remoteBuiUserService;

    @Resource
    private RemoteSmsService remoteSmsService;



    /**
     * ???????????????  buiId ????????????
     * ?????????????????? ??????????????? ?????????  ???????????????
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public Integer tarStatus(String userId, String buiId) {
        CntUserTar userTar = userTarService.getOne(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getUserId, userId).eq(CntUserTar::getBuiId, buiId));
        // ?????????null ????????????????????????
        if (Objects.isNull(userTar))
            return CEN_NOT_TAR.getCode();
        return userTar.getIsFull();
    }


    /**
     * ???????????????????
     * @param buiId
     * @param userId
     * @return
     */
     @Override
    public boolean isSell(String userId, String buiId){
        if (CEN_NOT_TAR.getCode().equals(tarStatus(userId, buiId)))return Boolean.FALSE;
        // ??????????????????????????????
          CntUserTar userTar = userTarService.getOne(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getUserId, userId).eq(CntUserTar::getBuiId, buiId));
         return  SELL_OK.getCode().equals( userTar.getGoSell());


    }

    @Override
    public String tarCollection(CntCollection cntCollection, String userId) {
        return tarComm(cntCollection.getCollectionName(),cntCollection.getId(),cntCollection.getTarId(),userId);
    }

    @Override
    public String tarBox(Box box, String userId) {
        return tarComm(box.getBoxTitle(),box.getId(),box.getTarId(),userId);
    }

    @Override
    public void overSelf(String userId, String id) {
        CntUserTar userTar = userTarService.getOne(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getUserId, userId).eq(CntUserTar::getBuiId, id));
        userTar.setGoSell(SELL_OK.getCode());
        userTar.updateD(userId);
        userTarService.updateById(userTar);
    }

    private String tarComm(String buiName,String buiId,String tarId,String userId){
        Assert.isTrue(CEN_NOT_TAR.getCode().equals(tarStatus(userId,buiId)),"?????????????????????!");
        // ????????????
        CntUserDto userDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        Assert.isTrue(OK_REAL.getCode().equals(userDto.getIsReal()),"???????????????????????????!");
        // ????????????
        CntTar cntTar = getOne(Wrappers.<CntTar>lambdaQuery().eq(CntTar::getId,tarId));
        Assert.isTrue(FLAG_PROCESS.getCode().equals(cntTar.getEndFlag()),"??????????????????,????????????!");
        // ???????????? ??????????????????
        //1=?????????,2=?????????
        //Integer isFull = nowTimeIndex(cntTar.getTarPro());
        CntUserTar cntUserTar = Builder.of(CntUserTar::new)
                .with(CntUserTar::setUserId,userId)
                .with(CntUserTar::setBuiId,buiId)
                .with(CntUserTar::setTarId,tarId)
                .with(CntUserTar::setGoSell, NO_SELL.getCode())
                .with(CntUserTar::setId, IdUtil.getSnowflakeNextIdStr())
                .with(CntUserTar::setIsFull,CEN_WAIT_TAR.getCode())
                .with(CntUserTar::createD,userId)
                .build();
        userTarService.save(cntUserTar);
        // ?????????
        String format = cntTar.getOpenTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        /*remoteSmsService.sendCommPhone(Builder.of(SmsCommDto::new)
                        .with(SmsCommDto::setPhoneNumber, cntUserDto.getPhone())
                        .with(SmsCommDto::setTemplateCode, TAR_MSG)
                        .with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().put("buiName", buiName).put("openTime", format).build())
                .build());*/
        String msg = StrUtil.format("??????????????????{}?????????,??????????????????{}??????!", buiName, format);
        if (StrUtil.isNotBlank(cntUserDto.getJgPush()))
        jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,msg, Arrays.asList(cntUserDto.getJgPush()));

        return msg;
    }

    // ????????????????????????
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void openTarResult(String tarId){
        CntTar cntTar = getById(tarId);
        // ????????????
        if (FLAG_STOP.getCode().equals( cntTar.getEndFlag()))return;
        cntTar.setEndFlag(FLAG_STOP.getCode());
        updateById(cntTar);
        execProcessBatchResult(tarId);



    }

    private void execProcessBatchResult(String tarId) {
        // ????????????????????????
        CntTar cntTar = getById(tarId);
        List<CntUserTar> cntUserTars = userTarService.list(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getTarId, tarId));
        // ????????????????????????????????????
        if (!cntUserTars.isEmpty()){
            // ??????????????????
            CntUserTar cntUserTar = cntUserTars.stream().findFirst().get();
            // ?????????????????????? ????????????????????????
            Integer count = null;
            if (BOX_TAR.getCode().equals(cntTar.getTarType())) {
                Box box = SpringUtil.getBean(IBoxService.class).getById(cntUserTar.getBuiId());
                count = box.getBalance();
            }
            if (COLLECTION_TAR.getCode().equals(cntTar.getTarType())){
                CntCollection cntCollection = SpringUtil.getBean(ICollectionService.class).getById(cntUserTar.getBuiId());
                count = cntCollection.getBalance();
            }
            Assert.isTrue(Objects.nonNull(count),"???????????????????????????!");
            // 1.1 ?????? ????????? ??????????????? >= ??????????????????
            if (count >= cntUserTars.size()) {
                //openSuccess(cntUserTars);
                //????????????
                List<CntUserTar> successUserTar = cntUserTars.parallelStream().filter(item -> CEN_YES_TAR.getCode().equals(nowTimeIndex(cntTar.getTarPro())) || CEN_YES_TAR.getCode().equals( item.getIsFull()) ).collect(Collectors.toList());
                if (!successUserTar.isEmpty())
                    openSuccess(successUserTar);

                Set<String> successIds = successUserTar.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
                List<CntUserTar> failUserTar = cntUserTars.parallelStream().filter(item -> !successIds.contains(item.getId())).collect(Collectors.toList());
                if (!failUserTar.isEmpty())
                    openFail(failUserTar);

                return;
            }
            // 1.2 ?????? ????????? ??????????????? <
            // 1.2.1 ???????????????????????? ??????????????? ????????????
            List<CntUserTar> successUserTarLists = cntUserTars.parallelStream().filter(item -> CEN_YES_TAR.getCode().equals(item.getIsFull())).collect(Collectors.toList());

            List<CntUserTar> whatUserTarLists = cntUserTars.parallelStream().filter(item -> CEN_WAIT_TAR.getCode().equals(item.getIsFull())).collect(Collectors.toList());

            //         ????????? ?????? -  ?????????  = ???????????????, ????????????????????????????????????.
             int tempPoint;
             int successBalance =  tempPoint  =  count - successUserTarLists.size();
             // ??????????????????????????????????????????
            // Assert.isTrue(successBalance <=0,"see by system error!");
             //successBalance ???????????????????????????
               Collections.shuffle(whatUserTarLists);
          for (int i = 0; i < successBalance; i++) {
                CntUserTar whatSuccessUserTar = whatUserTarLists.get(i);
                if (CEN_YES_TAR.getCode().equals(nowTimeIndex(cntTar.getTarPro())))
                successUserTarLists.add(whatSuccessUserTar);
            }
            // 1.3 ?????????????????? ???????????????????????????
/*            openSuccess(successUserTarLists);
            openFail(ListUtil.sub(whatUserTarLists, tempPoint, whatUserTarLists.size()));*/

//            List<CntUserTar> successUserTar = whatUserTarLists.parallelStream().filter(item -> CEN_YES_TAR.getCode().equals(nowTimeIndex(cntTar.getTarPro()))).collect(Collectors.toList());
//            successUserTarLists.addAll(successUserTar);
            if (!successUserTarLists.isEmpty())
                openSuccess(successUserTarLists);

              Set<String> successIds = successUserTarLists.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
            List<CntUserTar> failUserTar = whatUserTarLists.parallelStream().filter(item -> !successIds.contains(item.getId())).collect(Collectors.toList());
            if (!failUserTar.isEmpty())
                openFail(failUserTar);
        }
    }


    /**
     * ?????????
     * @param cntUserTars
     */
    private void openSuccess(List<CntUserTar> cntUserTars) {
        userTarService.update(Wrappers.<CntUserTar>lambdaUpdate()
                .in(CntUserTar::getId, cntUserTars.parallelStream().map(item -> item.getId()).collect(Collectors.toSet()))
                .set(CntUserTar::getIsFull, CEN_YES_TAR.getCode())
                .set(CntUserTar::getOpenTime, LocalDateTime.now())
        );
        ThreadUtil.execute(()->{
            R<List<CntUserDto>> userIdLists = remoteBuiUserService.findUserIdLists(Builder.of(UserDto::new).with(UserDto::setUserIds, cntUserTars.parallelStream().map(item -> item.getUserId()).collect(Collectors.toList())).build(), SecurityConstants.INNER);
            List<CntUserDto> cntUserDtos = userIdLists.getData();
            // ??????
            remoteSmsService.sendBatchCommPhone(Builder.of(BatchSmsCommDto::new).with(BatchSmsCommDto::setPhoneNumbers,cntUserDtos.parallelStream().map(item -> item.getPhone()).collect(Collectors.toSet())).with(BatchSmsCommDto::setTemplateCode,TAR_SUCCESS ).with(BatchSmsCommDto::setParamsMap,MapUtil.<String,String>builder().build()).build());
            // ??????
            List<String> jgPushIds = cntUserDtos.parallelStream().filter(item -> StrUtil.isNotBlank(item.getJgPush())).map(item -> item.getJgPush()).collect(Collectors.toList());
            // ?????????????????????????????????
            if (!jgPushIds.isEmpty()) jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,"???????????????????????????,?????????,???????????????!",jgPushIds);
        });
    }
    /**
     * ????????????
     * @param cntUserTars
     */
    private void openFail(List<CntUserTar> cntUserTars) {
        userTarService.update(Wrappers.<CntUserTar>lambdaUpdate()
                .in(CntUserTar::getId, cntUserTars.parallelStream().map(item -> item.getId()).collect(Collectors.toSet()))
                .set(CntUserTar::getIsFull, CEN_NO_TAR.getCode())
                .set(CntUserTar::getOpenTime, LocalDateTime.now())
        );
        ThreadUtil.execute(()->{
            R<List<CntUserDto>> userIdLists = remoteBuiUserService.findUserIdLists(Builder.of(UserDto::new).with(UserDto::setUserIds, cntUserTars.parallelStream().map(item -> item.getUserId()).collect(Collectors.toList())).build(), SecurityConstants.INNER);
            List<CntUserDto> cntUserDtos = userIdLists.getData();
            // ??????
          //  remoteSmsService.sendBatchCommPhone(Builder.of(BatchSmsCommDto::new).with(BatchSmsCommDto::setPhoneNumbers,cntUserDtos.parallelStream().map(item -> item.getPhone()).collect(Collectors.toSet())).with(BatchSmsCommDto::setTemplateCode,TAR_FAIL ).with(BatchSmsCommDto::setParamsMap,MapUtil.<String,String>builder().build()).build());
            // ??????
            List<String> jgPushIds =cntUserDtos.parallelStream().filter(item -> StrUtil.isNotBlank(item.getJgPush())).map(item -> item.getJgPush()).collect(Collectors.toList());
            // ?????????????????????????????????
            if (!jgPushIds.isEmpty()) jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,"???????????????????????????,?????????,???????????????!",jgPushIds);
        });

    }

    /**
     * 1=?????????,2=?????????
     * @param tarPro
     * @return
     */
    private Integer nowTimeIndex(BigDecimal tarPro) {
        int range = tarPro.intValue();
        HashMap<Integer, Integer> objectObjectHashMap = Maps.newHashMap();
        for (int i = 0; i < 100; i++) {
             if (range >=i){
                 objectObjectHashMap.put(i,Integer.valueOf(CEN_YES_TAR.getCode()));
                 continue;
             }
            objectObjectHashMap.put(i,Integer.valueOf(CEN_NO_TAR.getCode()));
        }
        return objectObjectHashMap.get( Long.valueOf(DateUtil.current() % 100).intValue());

    }
}
