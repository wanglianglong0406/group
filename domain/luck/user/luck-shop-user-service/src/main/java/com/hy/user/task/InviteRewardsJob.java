package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.model.User;
import com.hy.user.model.UserAgentDistributionInfo;
import com.hy.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/4 17:49
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/4 17:49
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.user.task.InviteRewardsJob",
        cron = "0 0 0 L * ? ",
        description = "会员邀请到达一定数量进行额外奖励（本月最后一天执行）",
        overwrite = true
)
@Slf4j
@Component
public class InviteRewardsJob implements SimpleJob {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.querUserInfoList();
        List<UserAgentDistributionInfo> userAgentDistributionInfoList = iUserService.getUserAgentDistributionInfoList();
        userList.forEach(user -> userAgentDistributionInfoList.forEach(userAgentDistributionInfo -> {
            if (user.getTeamSize().equals(userAgentDistributionInfo.getSize())) {
                if(user.getMembershipLevel().equals(userAgentDistributionInfo.getVipLevel())
                        && user.getAgencyLevel().equals(userAgentDistributionInfo.getAgencyLevel()))
                iAccountService.increaseRewards(user.getUserId(),userAgentDistributionInfo.getInviteRewards());
            }
        }));
    }
}
