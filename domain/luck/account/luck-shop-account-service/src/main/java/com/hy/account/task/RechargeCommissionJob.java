package com.hy.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.center.service.ICenterStageService;
import com.hy.order.model.Orders;
import com.hy.order.service.IOrderService;
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
 * @CreateDate: 2021/1/6 19:15
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/6 19:15
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.account.task.RechargeCommissionJob",
        cron = "0 0 0 1/7 * ? ",
        description = "下级用户充值回扣奖励(每隔七天执行一次)",
        overwrite = true
)
@Slf4j
@Component
public class RechargeCommissionJob implements SimpleJob {

    @Autowired
    private ICenterStageService iCenterStageService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;

    private IOrderService iOrderService;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.getUserAgentInfoList();

        userList.forEach(user -> {
            List<User> subordinateUserList = iUserService.queryUserInfoByRegistrationCode(user.getRegistrationCode());
            subordinateUserList.forEach(subordinateUser ->{
                String userId = subordinateUser.getUserId();
                Integer vipLevel = subordinateUser.getMembershipLevel();
                Integer agencyLevel = subordinateUser.getAgencyLevel();
                UserAgentDistributionInfo userAgentDistributionInfo = iUserService.getUserAgentDistributionInfoByVipLevelAndAgencyLevel(vipLevel, agencyLevel);
                Double rechargeRebateRate = userAgentDistributionInfo.getRechargeRebateRate();
                Orders order = iOrderService.getUserFirstRechargeInfo(userId);
                Double rechargeRebateRateReward = order.getAmount() * rechargeRebateRate;
                iAccountService.increaseRewards(user.getUserId(), rechargeRebateRateReward);
            });

        });
    }
}
