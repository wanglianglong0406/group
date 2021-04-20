package com.hy.account.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.center.model.FinancialRecords;
import com.hy.center.service.ICenterStageService;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/31 16:29
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/31 16:29
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.account.task.FinancialSettlementJob",
        cron = "0 10 0 1/1 * ? ",
        description = "理财结算(每天零点10分执行一次)",
        overwrite = true
)
@Slf4j
@Component
public class FinancialSettlementJob implements SimpleJob {
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
            String userId = user.getUserId();
            List<FinancialRecords> financialRecordsList = iCenterStageService.getFinancialRecordsList(userId);
            financialRecordsList.forEach(financialRecords -> {
                FinancialRecords fr = iCenterStageService.calculateDailyInterest(userId, financialRecords.getProductId());
                boolean flag = iAccountService.financialSettlementOfTPlusOne(userId, fr.getDailyInterest());
                log.info("结果是："+flag);
            });

        });

    }
}
