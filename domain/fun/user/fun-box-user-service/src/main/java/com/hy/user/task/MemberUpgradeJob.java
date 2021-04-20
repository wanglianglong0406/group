package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.account.service.IAccountService;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
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

import java.util.List;

/**
 * @Description: $- 会员升级 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/20 13:25
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/20 13:25
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ElasticJobConfig(
        name = "com.hy.user.task.MemberUpgradeJob",
        cron = "0 0 0 1/1 * ? ",
        description = "会员升级",
        overwrite = true
)
@Slf4j
@Component
public class MemberUpgradeJob implements SimpleJob {

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
        memberUpgrade();
    }

    //会员升级，每天零点跑一次
    public void memberUpgrade() {
        List<User> userList = iUserService.querUserInfoList();
        for (User user : userList) {
            String userId = user.getUserId();
            Double totalCashFlow = iOrderService.totalCashFlow(userId);
            Double rechargeFlow = iOrderService.totalRechargeFlow(userId);
            Integer vipLevel = user.getUserMembershipLevel();
            Vipinfo vipinfo = iVipService.queryVipInfo(vipLevel);
            //会员升级必须满足两个条件 其一、消费总流水达到相应的条件；其二、充值总流水达到相应的条件。
            if (totalCashFlow > vipinfo.getCashFlow() && rechargeFlow > vipinfo.getRetainedCashFlow()) {
                iUserService.memberUpgrade(userId);

                iAccountService.upgradeRewardBlance(userId, vipinfo.getMonthlyTicket(), PayMethod.SYSTEM_PAY.id, OrderType.UPGRADE_REWARD.id);

                //站内添加站内通知
                iNotificationService.addToNotificationInfoByType(userId, "Member upgrade", 4, "Member upgrade successful");

            }
        }

    }
}
