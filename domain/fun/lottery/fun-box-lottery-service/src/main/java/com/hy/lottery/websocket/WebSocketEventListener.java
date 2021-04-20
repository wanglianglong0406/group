package com.hy.lottery.websocket;

import com.hy.lottery.model.LotteryInfo;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.Objects;

import static com.hy.constant.Constant.LOTTERY_ACTION_NO_REDIS_LOCK;
import static com.hy.constant.Constant.LOTTERY_INFO;


/**
 * @Description: $- websocket 监听客户端状态并处理初始信息 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/3 11:15
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/3 11:15
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Component
@Slf4j
public class WebSocketEventListener {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations;
    @EventListener
    public void connectEvent(SessionConnectEvent event) {
        log.info("========== websocket 正在建立连接==========");
    }

    //连接事件
    @EventListener
    public void connectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        log.info("========== websocket 连接建立完成 =========="+sessionConnectedEvent.getMessage());
    }

    //订阅事件
    @EventListener
    public void subscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {

        log.info("========== websocket 开始推送消息 {} ========== ", sessionSubscribeEvent.getMessage());
        log.info("========== 新一轮彩票信息推送列表SessionSubscribeEvent==========");
        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        String lotteryInfos_json=redisOperator.get(LOTTERY_INFO+period_redis_str);
        List<LotteryInfo> lotteryInfos =JsonUtils.jsonToList(lotteryInfos_json, LotteryInfo.class);
        long serverTime=System.currentTimeMillis();
        String json_str = Objects.requireNonNull(JsonUtils.objectToJson(ResponseJsonResult.ok(lotteryInfos,serverTime)));
        log.info("========== 新一轮彩票信息推送列表如下：{} ==========", json_str);
        simpMessageSendingOperations.convertAndSend("/topic/queryLotterInfoByPeriod", json_str);
    }
    //
//    //退订时间
//    @EventListener
//    public void unsubscribeEvent(SessionUnsubscribeEvent sessionsEndpoint) {
//        log.info("========== 新一轮彩票信息推送列表 SessionUnsubscribeEvent==========");

//    }
//
//
    //断开事件
    @EventListener
    public void disconnectEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        log.info("========== websocket 客户端已经下线=========="+sessionDisconnectEvent.getSessionId());
    }
}
