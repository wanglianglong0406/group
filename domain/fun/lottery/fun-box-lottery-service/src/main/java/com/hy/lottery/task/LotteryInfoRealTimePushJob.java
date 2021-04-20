package com.hy.lottery.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.lottery.model.LotteryInfo;
import com.hy.pojo.ResponseJsonResult;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.hy.constant.Constant.LOTTERY_ACTION_NO_REDIS_LOCK;
import static com.hy.constant.Constant.LOTTERY_INFO;


/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 13:59
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 13:59
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name= "com.hy.lottery.task.LotteryInfoRealTimePushJob",
        cron= "0/15 * * * * ?",
        description = "彩票信息实时推送",
        overwrite = true
)
@Slf4j
@Component
public class LotteryInfoRealTimePushJob implements SimpleJob {

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations;//消息发送模板

    @Override
    public void execute(ShardingContext shardingContext) {
        queryLotterInfoByPeriod();
    }

    public void queryLotterInfoByPeriod(){
        log.info("========== 定时任务，实时同步彩票信息========== ");
        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);

        String lotteryInfos_json=redisOperator.get(LOTTERY_INFO+period_redis_str);
        List<LotteryInfo> lotteryInfos = JsonUtils.jsonToList(lotteryInfos_json, LotteryInfo.class);
        long serverTime=System.currentTimeMillis();
        String json_str = Objects.requireNonNull(JsonUtils.objectToJson(ResponseJsonResult.ok(lotteryInfos,serverTime)));
        simpMessageSendingOperations.convertAndSend("/topic/queryLotterInfoByPeriod", json_str);
    }
}
