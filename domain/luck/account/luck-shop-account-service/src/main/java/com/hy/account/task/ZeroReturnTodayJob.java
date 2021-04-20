package com.hy.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/5 5:30
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/5 5:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.account.task.ZeroReturnTodayJob",
        cron = "0 0 0 1/1 * ? *",
        description = "今日收益清零",
        overwrite = true
)
@Slf4j
@Component
public class ZeroReturnTodayJob implements SimpleJob {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;
    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.querUserInfoList();
        userList.forEach(user -> {
            LocalDate registDate= user.getCreateTime();
            LocalDate now = LocalDate.now();
            if (registDate.plusDays(7).isEqual(now)){
                iAccountService.zeroReturnToday(user.getUserId());
            }
        });
    }
}
