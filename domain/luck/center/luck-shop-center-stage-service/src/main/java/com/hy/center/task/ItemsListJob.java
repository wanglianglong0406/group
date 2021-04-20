package com.hy.center.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.task.annotation.ElasticJobConfig;
import com.hy.user.model.User;
import com.hy.user.service.IUserService;
import com.hy.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.hy.constant.Constant.ITEMS_LIST;

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
        name = "com.hy.center.task.ItemsListJob",
        cron = "0 0 0/1 * * ? ",
        description = "清空商品缓存(每天凌晨执行一次)",
        overwrite = true
)
@Slf4j
@Component
public class ItemsListJob implements SimpleJob {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public void execute(ShardingContext shardingContext) {
        List<User> userList = iUserService.querUserInfoList();
        userList.forEach(user -> {
            String itemsListRedisKey = ITEMS_LIST + ":" + user.getUserId();
            redisOperator.del(itemsListRedisKey);
        });
    }
}
