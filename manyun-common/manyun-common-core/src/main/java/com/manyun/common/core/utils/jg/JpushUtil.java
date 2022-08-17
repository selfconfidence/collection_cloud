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

    public  void sendMsg(String title,String msgContent, List<String> registrationIdIds) {
        // For push, all you need do is to build PushPayload object.
        List<PushPayload> payloadList = buildPushRegistrationIdIds(title,msgContent,registrationIdIds);
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
    private   PushPayload buildPushMsg(String msgContent) {
        return PushPayload.messageAll(msgContent).alertAll(ALERT);
    }

    // 根据别名推送
    private  List<PushPayload> buildPushRegistrationIdIds(String title,String msgContent, List<String> registrationIdIds){
        // 全平台
        List<PushPayload> pushPayloads = Lists.newArrayList();
        List<List<String>> registrationIdIdsList = ListUtil.split(registrationIdIds, 0B1111100111);
        for (List<String> regId : registrationIdIdsList) {
            pushPayloads.add(PushPayload.newBuilder().setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(regId))// 消息体
                    .setNotification(Notification.alert(msgContent))
                    .setMessage(Message.newBuilder().setTitle(title).setMsgContent(msgContent).build())
                    .build());
        }
       return pushPayloads;
    }

    // 根据uuid推送
    private  List<PushPayload> buildPushUuid(String title,String msgContent, List<String> uuIds){
        // 全平台
        List<PushPayload> pushPayloads = Lists.newArrayList();
        List<List<String>> aliasList = ListUtil.split(uuIds, 0B1111100111);
        for (List<String> alia : aliasList) {
            pushPayloads.add(PushPayload.newBuilder().setPlatform(Platform.all())
                    // 别名 一次推送最多 1000 个。
                    .setAudience(Audience.registrationId(alia))// 消息体
                    .setNotification(Notification.alert(ALERT))
                    .setMessage(Message.newBuilder().setTitle(title).setMsgContent(msgContent).build())
                    .build());
        }
        return pushPayloads;
    }


}
