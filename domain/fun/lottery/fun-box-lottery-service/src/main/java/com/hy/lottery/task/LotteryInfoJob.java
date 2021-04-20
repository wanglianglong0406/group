package com.hy.lottery.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.lottery.model.LotteryInfo;
import com.hy.lottery.service.ILotteryInfoService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.utils.DateUtil;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.hy.constant.Constant.LOTTERY_ACTION_NO_REDIS_LOCK;
import static com.hy.constant.Constant.LOTTERY_INFO;


/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 13:55
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 13:55
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name= "com.hy.lottery.task.LotteryInfoJob",
        cron= "0 0/3 * * * ?",
        description = "生产彩票信息",
        overwrite = true
)
@Slf4j
@Component
public class LotteryInfoJob implements SimpleJob {

    @Autowired
    private ILotteryInfoService iLotteryInfoService;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations;//消息发送模板
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void execute(ShardingContext shardingContext) {

        queryLotteryInfo();

        log.info("**************************************************************");
        log.info("========== 开始执行定时任务，生产彩票信息 ==========");
        iLotteryInfoService.createLottery();
        log.info("========== 生产彩票信息完成，开始推送彩票信息到websocket ==========");
        //推送彩票列表
        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        List<LotteryInfo> lotteryInfos = iLotteryInfoService.queryLotterInfoByPeriod(period_redis_str);

        redisOperator.set(LOTTERY_INFO + period_redis_str, JsonUtils.objectToJson(lotteryInfos));
        //系统当前时间
        long serverTime=System.currentTimeMillis();

        String json_str = Objects.requireNonNull(JsonUtils.objectToJson(ResponseJsonResult.ok(lotteryInfos,serverTime)));
        log.info("========== 新一轮彩票信息推送列表如下：{} ==========", json_str);
        simpMessageSendingOperations.convertAndSend("/topic/queryLotterInfoByPeriod", json_str);
        log.info("========== 生产彩票信息定时任务执行结束，当前时间为：{} ==========", DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
        log.info("**************************************************************");
    }


    //推送开奖结果
    public void queryLotteryInfo() {
        log.info("**************************************************************");
        log.info("========== 开始执行定时任务，推送开奖信息到websocket客户端 ==========");
        ResponseJsonResult responseJsonResult = iLotteryInfoService.queryLotteryInfos();
        String json_str = Objects.requireNonNull(JsonUtils.objectToJson(responseJsonResult));
        log.info("========== 推送开奖信息到websocket客户端列表：{} ==========", json_str);
        simpMessageSendingOperations.convertAndSend("/topic/resultPush", json_str);
        log.info("========== 推送开奖信息到websocket客户端，定时任务执行结束，当前时间为：{} ==========", DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
        log.info("**************************************************************");
    }
}
