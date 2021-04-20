package com.hy.lottery.websocket;

import com.hy.lottery.stream.WebSocketTopic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/26 22:30
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/26 22:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Slf4j
@EnableBinding(value = {
        WebSocketTopic.class
})
public class WebSocketMessageConsumer {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @StreamListener(WebSocketTopic.INPUT)
    public void queryLotteryInfo(String payload) {
//        log.info("========== 开奖结果查询收到消息，消息总条数 {} ==========", payload);
//
//        simpMessagingTemplate.convertAndSend("/topic/queryLotteryInfo", payload);
//        //simpMessagingTemplate.convertAndSend(webSocketMessage.getPayload());
//        log.info("========== 开奖结果查询,收到消息成功,websocket 推送成功 {} ==========", payload);

    }


}
