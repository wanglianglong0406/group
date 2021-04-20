package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.order.model.Orders;
import com.hy.order.service.IOrderService;
import com.hy.search.model.Vipinfo;
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
 * @CreateDate: 2020/12/20 13:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 13:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.user.task.MonthlyTicketJob",
        cron = "0 10 0 1 * ?",
        description = "月票红包奖励",
        overwrite = true
)
@Slf4j
@Component
public class MonthlyTicketJob implements SimpleJob {

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
        monthlyTicket();
    }

    //月票红包，每月1号凌晨发一次
    public void monthlyTicket() {
        List<User> userList = iUserService.querUserInfoList();
        for (User user : userList) {
            String userId = user.getUserId();
            String startDate = LocalDate.now().plusDays(-90).toString();
            String endDate=LocalDate.now().toString();
            Integer vipLevel = user.getUserMembershipLevel();
            Vipinfo vipinfo = iVipService.queryVipInfo(vipLevel);
            List<Orders> orders = iOrderService.totalThisMonthRechargeFlow(userId,startDate);
            if(null != orders || orders.size()>1){
                //发放月票红包
                iAccountService.monthlyTicketBlance(userId,vipinfo.getMonthlyTicket(), PayMethod.SYSTEM_PAY.id, OrderType.MONTHLY_RED_ENVELOPE.id);

                //站内添加站内通知
                iNotificationService.addToNotificationInfoByType(userId, "Monthly ticket red envelope", 4, "Monthly ticket red envelope received successfully");

            }

        }
    }
}
