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
import static com.manyun.common.core.enums.UserTaSell.NO_SELL;
import static com.manyun.common.core.enums.UserTaSell.SELL_OK;

/**
 * <p>
 * 抽签规则(盲盒,藏品) 服务实现类
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
     * 判定对应的  buiId 抽签状态
     * 能进来的都是 必须抽签的 ！！！  需强制验证
     * @param userId
     * @param buiId
     * @return
     */
    @Override
    public Integer tarStatus(String userId, String buiId) {
        CntUserTar userTar = userTarService.getOne(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getUserId, userId).eq(CntUserTar::getBuiId, buiId));
        // 如果未null 代表从来没有抽过
        if (Objects.isNull(userTar))
            return CEN_NOT_TAR.getCode();
        return userTar.getIsFull();
    }


    /**
     * 买过了没有呢?
     * @param buiId
     * @param userId
     * @return
     */
     @Override
    public boolean isSell(String userId, String buiId){
        if (CEN_NOT_TAR.getCode().equals(tarStatus(userId, buiId)))return Boolean.FALSE;
        // 查询当前是否买过了？
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
        Assert.isTrue(CEN_NOT_TAR.getCode().equals(tarStatus(userId,buiId)),"您已经抽过签了!");
        // 开始抽签
        CntTar cntTar = getOne(Wrappers.<CntTar>lambdaQuery().eq(CntTar::getId,tarId));
        Assert.isTrue(FLAG_PROCESS.getCode().equals(cntTar.getEndFlag()),"抽签时间已过,敬请期待!");
        // 抽签算法 按照比例计算
        //1=已抽中,2=未抽中
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
        // 发短信
        String format = cntTar.getOpenTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        CntUserDto cntUserDto = remoteBuiUserService.commUni(userId, SecurityConstants.INNER).getData();
        /*remoteSmsService.sendCommPhone(Builder.of(SmsCommDto::new)
                        .with(SmsCommDto::setPhoneNumber, cntUserDto.getPhone())
                        .with(SmsCommDto::setTemplateCode, TAR_MSG)
                        .with(SmsCommDto::setParamsMap, MapUtil.<String,String>builder().put("buiName", buiName).put("openTime", format).build())
                .build());*/
        String msg = StrUtil.format("您已经参与对{}抽签了,抽签结果将在{}公布!", buiName, format);
        if (StrUtil.isNotBlank(cntUserDto.getJgPush()))
        jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,msg, Arrays.asList(cntUserDto.getJgPush()));

        return msg;
    }

    // 开始公布抽签结果
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void openTarResult(String tarId){
        CntTar cntTar = getById(tarId);
        // 幂等校验
        if (FLAG_STOP.getCode().equals( cntTar.getEndFlag()))return;
        cntTar.setEndFlag(FLAG_STOP.getCode());
        updateById(cntTar);
        execProcessBatchResult(tarId);



    }

    private void execProcessBatchResult(String tarId) {
        // 找到所有抽签记录
        CntTar cntTar = getById(tarId);
        List<CntUserTar> cntUserTars = userTarService.list(Wrappers.<CntUserTar>lambdaQuery().eq(CntUserTar::getTarId, tarId));
        // 看下当前库存量（中奖量）
        if (!cntUserTars.isEmpty()){
            // 得到资产主体
            CntUserTar cntUserTar = cntUserTars.stream().findFirst().get();
            // 是藏品还是盲盒? 得到对应库存即可
            Integer count = null;
            if (BOX_TAR.getCode().equals(cntTar.getTarType())) {
                Box box = SpringUtil.getBean(IBoxService.class).getById(cntUserTar.getBuiId());
                count = box.getBalance();
            }
            if (COLLECTION_TAR.getCode().equals(cntTar.getTarType())){
                CntCollection cntCollection = SpringUtil.getBean(ICollectionService.class).getById(cntUserTar.getBuiId());
                count = cntCollection.getBalance();
            }
            Assert.isTrue(Objects.nonNull(count),"此抽签规则类型有误!");
            // 1.1 如果 中奖量 比抽签记录 >= 全部中奖即可
            if (count >= cntUserTars.size()) {
                //openSuccess(cntUserTars);
                //是否中奖
                List<CntUserTar> successUserTar = cntUserTars.parallelStream().filter(item -> CEN_YES_TAR.getCode().equals(nowTimeIndex(cntTar.getTarPro())) || CEN_YES_TAR.getCode().equals( item.getIsFull()) ).collect(Collectors.toList());
                if (!successUserTar.isEmpty())
                    openSuccess(successUserTar);

                Set<String> successIds = successUserTar.parallelStream().map(item -> item.getId()).collect(Collectors.toSet());
                List<CntUserTar> failUserTar = cntUserTars.parallelStream().filter(item -> !successIds.contains(item.getId())).collect(Collectors.toList());
                if (!failUserTar.isEmpty())
                    openFail(failUserTar);

                return;
            }
            // 1.2 如果 中奖量 比抽签记录 <
            // 1.2.1 找到所有抽签记录 中奖的记录 统计起来
            List<CntUserTar> successUserTarLists = cntUserTars.parallelStream().filter(item -> CEN_YES_TAR.getCode().equals(item.getIsFull())).collect(Collectors.toList());

            List<CntUserTar> whatUserTarLists = cntUserTars.parallelStream().filter(item -> CEN_WAIT_TAR.getCode().equals(item.getIsFull())).collect(Collectors.toList());

            //         当一个 总数 -  中奖量  = 余下的名单, 开始派发其他未中奖的即可.
             int tempPoint;
             int successBalance =  tempPoint  =  count - successUserTarLists.size();
             // 预防特殊情况，后台管理没限制
            // Assert.isTrue(successBalance <=0,"see by system error!");
             //successBalance 还有剩余的中奖次数
               Collections.shuffle(whatUserTarLists);
          for (int i = 0; i < successBalance; i++) {
                CntUserTar whatSuccessUserTar = whatUserTarLists.get(i);
                if (CEN_YES_TAR.getCode().equals(nowTimeIndex(cntTar.getTarPro())))
                successUserTarLists.add(whatSuccessUserTar);
            }
            // 1.3 推送通知信息 未中奖的，中奖的人
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
     * 中奖的
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
            // 短信
            remoteSmsService.sendBatchCommPhone(Builder.of(BatchSmsCommDto::new).with(BatchSmsCommDto::setPhoneNumbers,cntUserDtos.parallelStream().map(item -> item.getPhone()).collect(Collectors.toSet())).with(BatchSmsCommDto::setTemplateCode,TAR_SUCCESS ).with(BatchSmsCommDto::setParamsMap,MapUtil.<String,String>builder().build()).build());
            // 推送
            List<String> jgPushIds = cntUserDtos.parallelStream().filter(item -> StrUtil.isNotBlank(item.getJgPush())).map(item -> item.getJgPush()).collect(Collectors.toList());
            // 推送用户了表明中奖状态
            if (!jgPushIds.isEmpty()) jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,"您抽签结果已经公布,已抽中,快去购买吧!",jgPushIds);
        });
    }
    /**
     * 未中奖的
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
            // 短信
          //  remoteSmsService.sendBatchCommPhone(Builder.of(BatchSmsCommDto::new).with(BatchSmsCommDto::setPhoneNumbers,cntUserDtos.parallelStream().map(item -> item.getPhone()).collect(Collectors.toSet())).with(BatchSmsCommDto::setTemplateCode,TAR_FAIL ).with(BatchSmsCommDto::setParamsMap,MapUtil.<String,String>builder().build()).build());
            // 推送
            List<String> jgPushIds =cntUserDtos.parallelStream().filter(item -> StrUtil.isNotBlank(item.getJgPush())).map(item -> item.getJgPush()).collect(Collectors.toList());
            // 推送用户了表明中奖状态
            if (!jgPushIds.isEmpty()) jpushUtil.sendMsg(BusinessConstants.JgPushConstant.PUSH_TITLE,"您抽签结果已经公布,已抽中,快去购买吧!",jgPushIds);
        });

    }

    /**
     * 1=已抽中,2=未抽中
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
