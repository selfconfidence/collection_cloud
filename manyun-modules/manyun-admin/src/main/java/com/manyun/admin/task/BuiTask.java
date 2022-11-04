package com.manyun.admin.task;

import com.manyun.admin.service.ICntActionService;
import com.manyun.admin.service.ICntBoxService;
import com.manyun.admin.service.ICntCollectionService;
import com.manyun.comm.api.RemoteAuctionService;
import com.manyun.comm.api.RemoteConsignmentService;
import com.manyun.comm.api.RemoteOrderService;
import com.manyun.comm.api.RemoteTarService;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.utils.StringUtils;
import com.manyun.common.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.manyun.common.core.constant.BusinessConstants.RedisDict.USER_ACTIVE_NUMBERS;

/**
 * 业务相关调度
 *
 * @author ruoyi
 */
@Component("buiTask")
@Slf4j
public class BuiTask
{

    @Resource
    private RemoteOrderService remoteOrderService;

    @Resource
    private RemoteAuctionService remoteAuctionService;

    @Resource
    private RemoteConsignmentService remoteConsignmentService;

    @Resource
    private RemoteTarService remoteTarService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ICntBoxService boxService;

    @Autowired
    private ICntCollectionService cntCollectionService;

    @Autowired
    private ICntActionService cntActionService;

    /**
     * 藏品盲盒,提前一个小时发售全局进行推送用户.
     * 等待后台管理生成代码
     */
    public void pushPublishTimeTarIds(){
     log.info("推送了！！！");

 }

    /**
     * 定时调度取消未支付的订单
     */
    public void timeCancel(){
        remoteOrderService.timeCancel(SecurityConstants.INNER);
    }

    /**
     * 定时调度取消未支付的拍卖订单
     *
     */
    public void timeCancelAuction() {
        remoteAuctionService.timeCancelAuction(SecurityConstants.INNER);
    }

    /**
     * 定时调度检查是否开始拍卖
     */
    public void checkAuctionEnd() {
        remoteAuctionService.checkAuctionEnd(SecurityConstants.INNER);
    }

    /**
     * 定时调度检查拍卖是否开始
     */
    public void timeStartAuction() {
        remoteAuctionService.timeStartAuction(SecurityConstants.INNER);
    }

    /**
     * 定时调度检查正常拍卖流程
     */
    public void checkWinner() {
        remoteAuctionService.checkWinner(SecurityConstants.INNER);
    }

    /**
     * 定时调度检查延迟拍卖流程
     */
    public void checkDelayWinner() {
        remoteAuctionService.checkDelayWinner(SecurityConstants.INNER);
    }

    /**
     * 定时调度退回保证金余额支付部分
     */
    public void checkPayMarginFail() {
        remoteAuctionService.checkPayMarginFail(SecurityConstants.INNER);
    }

    /**
     * 定时调度寄售时限周期退回源
     */
    public void backConsignmentByTime(){
        remoteConsignmentService.cancelSchedulingConsignment(SecurityConstants.INNER);
    }

    /**
     * 定时清空日活
     */
    public void clearActives(){
        redisService.deleteObject(USER_ACTIVE_NUMBERS);
    }

    /**
     * 定时刷新 盲盒,藏品的状态库存信息及活动状态
     */
    public void doTaskExe(){
        //boxService.taskCheckStatus();
        //cntCollectionService.taskCheckStatus();
        cntActionService.taskCheckStatus();
    }

    /**
     * 定时公布抽签开奖结果
     */
    public void doPublishTarOpen(){
        remoteTarService.taskEndFlag(SecurityConstants.INNER);
    }

}
