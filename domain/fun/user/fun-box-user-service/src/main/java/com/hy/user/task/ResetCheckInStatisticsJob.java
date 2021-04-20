package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.service.ISignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: $- 重置签到统计 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/20 13:39
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 13:39
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.user.task.ResetCheckInStatisticsJob",
        cron = "0 59 2 ? * MON",
        description = "重置签到统计",
        overwrite = true
)
@Slf4j
@Component
public class ResetCheckInStatisticsJob implements SimpleJob {

    @Autowired
    private ISignService iSignService;

    @Override
    public void execute(ShardingContext shardingContext) {
        iSignService.resetCheckInStatistics();
    }
}
