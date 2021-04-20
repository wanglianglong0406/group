package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.order.service.IOrderService;
import com.hy.search.service.INotificationService;
import com.hy.search.service.IVipService;
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
 * @CreateDate: 2020/12/20 13:31
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 13:31
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@ElasticJobConfig(
        name = "com.hy.user.task.MemberDemotionJob",
        cron = "0 0 0 1/7 * ? ",
        description = "会员降级",
        overwrite = true
)
@Slf4j
@Component
public class MemberDemotionJob implements SimpleJob {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IVipService iVipService;
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private INotificationService iNotificationService;

    @Override
    public void execute(ShardingContext shardingContext) {
        memberDemotion();
    }

    //会员降级，隔七天零点跑一次。该用户90天内没有任何交易流水时，自动降1级
    public void memberDemotion() {
        List<User> userList = iUserService.querUserInfoList();
        for (User user : userList) {
            String userId = user.getUserId();
            String startDate = LocalDate.now().plusDays(-90).toString();
            String endDate=LocalDate.now().toString();
            Double totalCashFlow = iOrderService.totalAdateFlow(userId,startDate,endDate);
            if(totalCashFlow ==0.00){
                iUserService.memberDemotion(userId);
                //站内添加站内通知
                iNotificationService.addToNotificationInfoByType(userId, "Member demotion", 4, "You do not have any transaction flow within 90 days, and your membership level has been automatically downgraded");

            }
        }
    }
}
