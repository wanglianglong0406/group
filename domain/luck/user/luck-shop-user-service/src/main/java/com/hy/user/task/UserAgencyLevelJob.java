package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.model.AgencyLevel;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/3 16:12
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/3 16:12
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.user.task.UserAgencyLevelJob",
        cron = "0 0 0/1 * * ? ",
        description = "更新用户代理等级",
        overwrite = true
)
@Slf4j
@Component
public class UserAgencyLevelJob implements SimpleJob {
    @Autowired
    private IUserService iUserService;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.querUserInfoList();
        List<AgencyLevel> agencyLevelList = iUserService.getAgencyLevelInfoList();
        userList.forEach(user -> agencyLevelList.forEach(agencyLevel -> {
            if (user.getTeamSize() >= agencyLevel.getMinSize() && user.getTeamSize() <= agencyLevel.getMaxSize()) {
                iUserService.updateUserAgencyLevel(user.getUserId(), agencyLevel.getAgencyLevel());
            }
        }));
    }
}
