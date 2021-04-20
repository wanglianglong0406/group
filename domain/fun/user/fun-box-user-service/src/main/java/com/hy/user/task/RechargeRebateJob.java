package com.hy.user.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hy.account.service.IAccountService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.service.INotificationService;
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
 * @CreateDate: 2020/12/15 19:08
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/15 19:08
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@ElasticJobConfig(
        name= "com.hy.user.task.RechargeRebateJob",
        cron= "0 0 0 1/1 * ? ",
        description = "代理用户首充返利",
        overwrite = true
)
@Slf4j
@Component
public class RechargeRebateJob implements SimpleJob {
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private INotificationService iNotificationService;
    //代理用户，下级用户首充返利给上级用户
    public void rechargeRebate() {
        List<User> userList = iUserService.querUserInfoList();
        ObjectMapper mapper = new ObjectMapper();
        for (User user : userList) {
            String userId = user.getUserId();
            ResponseJsonResult responseJsonResult = iUserService.findUserInfo(userId);
            if (responseJsonResult.getStatus() != 200) {
                throw new RuntimeException("用户系统异常!");
            }
            User u = mapper.convertValue(responseJsonResult.getData(), User.class);
            u = iUserService.queryUserInfoByUserInvitationCode(u.getUserInvitationCode());
            iAccountService.rechargeRebate(u.getUserId());
            //站内添加站内通知
            iNotificationService.addToNotificationInfoByType(u.getUserId(),"First charge rebate",3,"First charge rebate of agent user");
        }

    }

    @Override
    public void execute(ShardingContext shardingContext) {
        rechargeRebate();
    }



}
