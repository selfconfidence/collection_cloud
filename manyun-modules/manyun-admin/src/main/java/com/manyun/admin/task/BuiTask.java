package com.manyun.admin.task;

import com.manyun.comm.api.RemoteAuctionService;
import com.manyun.comm.api.RemoteConsignmentService;
import com.manyun.comm.api.RemoteOrderService;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
     * 定时调度寄售时限周期退回源
     */
    public void backConsignmentByTime(){
        remoteConsignmentService.cancelSchedulingConsignment(SecurityConstants.INNER);
    }

}
