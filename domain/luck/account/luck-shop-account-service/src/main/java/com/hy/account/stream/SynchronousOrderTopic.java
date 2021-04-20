package com.hy.account.stream;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 14:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 14:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public interface SynchronousOrderTopic {



    String INPUT = "synchronous-order-consumer";

    String OUTPUT = "synchronous-order-producer";

    @Input(INPUT)
    SubscribableChannel input();

    @Output(OUTPUT)
    MessageChannel output();
}
