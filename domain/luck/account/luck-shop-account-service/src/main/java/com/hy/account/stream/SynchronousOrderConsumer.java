package com.hy.account.stream;

import com.hy.account.service.IAccountService;
import com.hy.enums.OrderType;
import com.hy.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/3 1:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/3 1:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@EnableBinding(value = {
        SynchronousOrderTopic.class
})
public class SynchronousOrderConsumer {

    @Autowired
    private IOrderService iOrderService;
    @Autowired
    private IAccountService iAccountService;

    @StreamListener(SynchronousOrderTopic.INPUT)
    public void consumerluckDrawMessage(SynchronousOrder synchronousOrder) {
        iOrderService.updateOrderInfo(synchronousOrder.getOrderId(), 30, "success", OrderType.DAILY_INTEREST.name + " -> " + "Wallet");
        iAccountService.increaseRewards(synchronousOrder.getUserId(),synchronousOrder.getWalletAccount()+synchronousOrder.getDailyInterestAmount());
    }


}

