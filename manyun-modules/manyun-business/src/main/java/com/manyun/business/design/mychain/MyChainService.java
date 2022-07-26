package com.manyun.business.design.mychain;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import com.manyun.business.domain.dto.CallCommitDto;
import com.manyun.business.domain.dto.CallTranDto;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.service.IUserCollectionService;
import com.manyun.common.core.exception.LinkTranException;
import com.manyun.common.core.exception.LinkUpException;
import com.manyun.common.core.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;

/*
 * 蚂蚁链服务
 *
 * @author yanwei
 * @create 2022-07-26
 */
@Service
@Slf4j
public class MyChainService {
    // 公钥
    private RSA pubRsa;

    @Value("${mychain.linkHttp}")
    private String linkHttp;

    @Value("${mychain.projectName}")
    private String projectName;

    private String upBase = "/callMychain/call";

    private String megerBase = "/callMychain/tranForm";

    @Autowired
    private ObjectFactory<IUserCollectionService> userCollectionServiceObjectFactory;

 public MyChainService(@Value("${mychain.publickey:error}") String publicKey){
     Assert.isFalse("error".equals(publicKey),"MyChainService => mychain.publickey is not null!");
     this.pubRsa = new RSA(null, publicKey);
 }



    /**
     * 上链
     *
     * 3次重试，  5秒重试一次， 每次按 2倍时间递增
     *                        5s   10s  20s
     */
    @Retryable(maxAttempts = 3,value = LinkUpException.class,backoff = @Backoff(multiplier = 2,value = 5000L))
    public void accountCollectionUp(CallCommitDto callCommitDto, Consumer<String> consumer){
        try {
            log.info("开始上链:{}",callCommitDto.toString());
            String hash = httpAccountCollectionUp(callCommitDto);
            consumer.accept(hash);
            log.info("上链成功:对应得 hash 为:{}",hash);
        }catch (Exception e){
            throw new LinkUpException(callCommitDto.getUserCollectionId(), e.getMessage());
        }

    }

    /**
     * 上链 失败兜底方法
     */
    /**
     * 兜底方法，必须在一个 类中  才会默认找到
     * @param
     */
    @Recover
    public void accountCollectionFailUp(LinkUpException linkUpException){
      log.error("上链失败:上链得用户藏品编号为:{},错误原因为:{}.",linkUpException.getUserCollectionId(),linkUpException.getErrorMsg());
        IUserCollectionService userCollectionService = userCollectionServiceObjectFactory.getObject();
        errorInvoke(linkUpException.getUserCollectionId(),linkUpException.getErrorMsg(), userCollectionService);


    }


    /**
     * 转换
     *
     * 3次重试，  5秒重试一次， 每次按 2倍时间递增
     *        5s   10s  20s
     */
    @Retryable(maxAttempts = 3,value = LinkTranException.class,backoff = @Backoff(multiplier = 2,value = 5000L))
    public void tranForm(CallTranDto callTranDto, Consumer<String> consumer){
        log.info("开始转赠链:{}",callTranDto.toString());
        try {
            String hash = httpAccountCollectionTran(callTranDto);
            consumer.accept(hash);
            log.info("转赠链成功:对应得 hash 为:{}",hash);
        }catch (Exception e){
            throw new LinkTranException(callTranDto.getUserCollectionId(),e.getMessage());
        }

    }



    /**
     * 转赠链 失败兜底方法
     */
    /**
     * 兜底方法，必须在一个 类中  才会默认找到
     * @param
     */
    @Recover
    public void accountCollectionFailTran(LinkTranException linkTranException){
        log.error("转赠失败:上链得 用户藏品编号为:{},错误原因为:{}.",linkTranException.getUserCollectionId(),linkTranException.getErrorMsg());
        // 失败了，错误原因记录
        IUserCollectionService userCollectionService = userCollectionServiceObjectFactory.getObject();
        errorInvoke(linkTranException.getUserCollectionId(),linkTranException.getErrorMsg(), userCollectionService);

    }

    private void errorInvoke(String userCollectionId,String errorMsg, IUserCollectionService userCollectionService) {
        UserCollection userCollection = userCollectionService.getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId, userCollectionId).eq(UserCollection::getIsLink,NOT_LINK.getCode()));
        if (Objects.nonNull(userCollection)){
            userCollection.setCollectionHash(errorMsg);
            userCollection.updateD(userCollection.getUserId());
            userCollectionService.updateById(userCollection);
        }
    }


    /**
     * http 调用上链
     * @return
     */
    private String httpAccountCollectionUp(CallCommitDto callCommitDto){
        return httpCommLink(JSON.toJSONString(callCommitDto),upBase);
    }

    /**
     * http 转赠链
     */
    private String httpAccountCollectionTran(CallTranDto callTranDto){
        return httpCommLink(JSON.toJSONString(callTranDto),megerBase);
    }

    private String  httpCommLink(String bodyJson,String baseUrl){
        HashMap<String, Object> bodyMap = Maps.newHashMap();
        bodyMap.put("projectName", projectName);
        bodyMap.put("time", DateUtil.current());
        bodyMap.put("data", pubRsa.encryptBase64(bodyJson, KeyType.PublicKey));
        String body = HttpUtil.post(linkHttp.concat(baseUrl), JSON.toJSONString(bodyMap));
        JSONObject bodyJsonObj = JSON.parseObject(body);
        Assert.isTrue(bodyJsonObj.getInteger("code").equals(200),bodyJsonObj.getString("msg"));
        return bodyJsonObj.getString("data");

    }
}
