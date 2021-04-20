package com.hy.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.center.model.FinancialRecords;
import com.hy.center.service.ICenterStageService;
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
 * @CreateDate: 2021/1/6 18:34
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/6 18:34
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.account.task.FinancialDailyInterestCommissionJob",
        cron = "0 0 16 1/1 * ? ",
        description = "下级用户理财产品日息回扣(每天下午4点执行一次)",
        overwrite = true
)
@Slf4j
@Component
public class FinancialDailyInterestCommissionJob implements SimpleJob {

    @Autowired
    private ICenterStageService iCenterStageService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;


    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.querUserInfoList();

        userList.forEach(user -> {
            List<User> subordinateUserList = iUserService.queryUserInfoByRegistrationCode(user.getRegistrationCode());
            subordinateUserList.forEach(subordinateUser -> {
                String userId = subordinateUser.getUserId();
                Integer vipLevel = subordinateUser.getMembershipLevel();
                Integer agencyLevel = subordinateUser.getAgencyLevel();
                UserAgentDistributionInfo userAgentDistributionInfo = iUserService.getUserAgentDistributionInfoByVipLevelAndAgencyLevel(vipLevel, agencyLevel);
                Double dailyInterestCommissionRate = userAgentDistributionInfo.getDailyInterestCommissionRate();
                List<FinancialRecords> financialRecordsList = iCenterStageService.getFinancialRecordsList(userId);
                financialRecordsList.forEach(financialRecords -> {
                    Double dailyInterestCommission = financialRecords.getDailyInterest();
                    Double dailyInterestReward = dailyInterestCommission * dailyInterestCommissionRate / 100;
                    iAccountService.increaseRewards(user.getUserId(), dailyInterestReward);
                });
            });
        });
    }
}
