package com.hy.lottery.task;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/26 10:56
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/26 10:56
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
//@Component
@Slf4j
public class AsyncTask {

//    @Autowired
//    private ILotteryInfoService iLotteryInfoService;
//
//    //人工干预抽奖结果，异步回调
//    @Async
//    public Future<String> operationLotteryResults(String period, String type, Long price, Integer number) {
//        boolean result = iLotteryInfoService.operationLotteryResults(period, type, price, number);
//        if (!result) {
//            log.info("========== 人工干预抽奖数据异常，需要人工处理,当期彩票为：{} 传入的类型为 ：{} 价格为 ：{} 开奖号码为 {} ==========",period, type, price,number);
//            log.info("========== 人工干预抽奖处理失败！执行结果为 {} ==========", false);
//            return new AsyncResult<String>("fail");
//        }
//        return new AsyncResult<String>("success");
//    }

}
