package com.manyun.common.core.utils.jg;

import cn.hutool.core.collection.ListUtil;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

@Slf4j
public class JpushUtil {
    private JPushClient jpushClient;

    public JpushUtil(String masterSecret ,String appKey) {
        this.jpushClient = new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());;
    }

    public JPushClient getJpushClient() {
        return jpushClient;
    }

    public  void sendMsg(String title,String msgContent, List<String> alias) {
        // For push, all you need do is to build PushPayload object.
        List<PushPayload> payloadList = buildPushAlias(title,msgContent,alias);
        for (PushPayload pushPayload : payloadList) {
            try {
                PushResult result = jpushClient.sendPush(pushPayload);
                log.info("Got result - " + result);
            } catch (APIConnectionException e) {
                // Connection error, should retry later
                log.error("Connection error, should retry later", e);

            } catch (APIRequestException e) {
                // Should review the error, and fix the request
                log.error("Should review the error, and fix the request", e);
                log.error("HTTP Status: " + e.getStatus());
                log.error("Error Code: " + e.getErrorCode());
                log.error("Error Message: " + e.getErrorMessage());
            }
        }

    }

    // 全部 推送消息
    public  PushPayload buildPushMsg(String msgContent) {
        return PushPayload.messageAll(msgContent).alertAll(ALERT);
    }

    // 根据别名推送
    public  List<PushPayload> buildPushAlias(String title,String msgContent, List<String> alias){
        // 全平台
        List<PushPayload> pushPayloads = Lists.newArrayList();
        List<List<String>> aliasList = ListUtil.split(alias, 0B1111100111);
        for (List<String> alia : aliasList) {
            pushPayloads.add(PushPayload.newBuilder().setPlatform(Platform.all())
                    // 别名 一次推送最多 1000 个。
                    .setAudience(Audience.alias(alia))// 消息体
                    .setNotification(Notification.alert(ALERT))
                    .setMessage(Message.newBuilder().setTitle(title).setMsgContent(msgContent).build())
                    .build());
        }
       return pushPayloads;
    }


}
