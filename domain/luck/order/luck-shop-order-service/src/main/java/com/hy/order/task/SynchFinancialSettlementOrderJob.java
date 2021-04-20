package com.hy.order.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hy.enums.OrderType;
import com.hy.order.model.Orders;
import com.hy.order.service.IOrderService;
import com.hy.task.annotation.ElasticJobConfig;
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
        name = "com.hy.order.task.SynchFinancialSettlementOrderJob",
        cron = "0 0,15 0 1/1 * ? ",
        description = "理财结算，订单同步状态延时同步，每天零点15分执行一次",
        overwrite = true
)
@Slf4j
@Component
public class SynchFinancialSettlementOrderJob implements SimpleJob {
    @Autowired
    private IOrderService iOrderService;

    @Override
    public void execute(ShardingContext shardingContext) {
       List<Orders> orders =iOrderService.getFinancialOrderInfoList();
       orders.forEach(od->{
           iOrderService.updateOrderInfo(od.getOrderId(), 30, "success", OrderType.DAILY_INTEREST.name + " -> " + "Wallet");
       });

    }
}
