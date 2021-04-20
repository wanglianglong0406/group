package com.hy.lottery.stream;

import com.hy.account.service.IAccountService;
import com.hy.enums.Colour;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.lottery.model.BetRecord;
import com.hy.lottery.model.GoldInfo;
import com.hy.lottery.model.LotteryInfo;
import com.hy.lottery.service.IBetRecordService;
import com.hy.lottery.service.IGoldInfoService;
import com.hy.lottery.service.ILotteryInfoService;
import com.hy.order.service.IOrderService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.JsonUtils;
import com.hy.utils.RandomUtils;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Description: $- LotteryMessageConsumer -$ #--> 开奖业务处理消费者
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 14:18
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 14:18
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@EnableBinding(value = {
        LotteryTopic.class
})
public class LotteryMessageConsumer {
    @Autowired
    private ILotteryInfoService iLotteryInfoService;
    @Autowired
    private IGoldInfoService iGoldInfoService;
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private IBetRecordService iBetRecordService;
    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private SimpMessagingTemplate simpMessageSendingOperations;//消息发送模板

    @Autowired
    private WebSocketTopic webSocketProducer;

    @StreamListener(LotteryTopic.INPUT)
    public void consumerluckDrawMessage(List<LotteryInfo> lotteryInfos) {
        log.info("**************************************************************");
        log.info("========== 收到抽奖消息列表，开始处理抽奖业务 {} ==========", lotteryInfos.toString());
        String period;
        String type;
        Long endTime = null;
        //循环处理进行开奖
        for (LotteryInfo lotteryInfo : lotteryInfos) {
            period = lotteryInfo.getPeriod();
            type = lotteryInfo.getType();
            endTime = lotteryInfo.getEndTime();
            GoldInfo goldInfo = iGoldInfoService.queryGoldInfoByType(type);
            //查询投注记录列表，只有已支付的订单才进行开奖
            List<BetRecord> betRecords = iBetRecordService.queryBetRecordInfos(period, type, 2);

            int randomNo = RandomUtils.createRandomNoOfOneToNine();
            //将此随机数维护进彩票信息中
            String price = String.valueOf(lotteryInfo.getPrice());
            //将彩票信息中的价格信息的末尾数字替换位随机数，并且维护进去
            price = price.substring(0, price.length() - 1);
            String b = price.substring(price.length() - 1);
            String c = b.replace(b, String.valueOf(randomNo));

            price = price + c;

            String result = null;

            if (randomNo % 2 == 1 && randomNo != 5) {
                result = Colour.GREEN.value;
            }
            if (randomNo % 2 == 0 && randomNo != 0) {
                result = Colour.RED.value;
            }
            if (randomNo == 0) {
                result = "RED,VIOLET";
            }
            if (randomNo == 5) {
                result = "GREEN,VIOLET";
            }

            if (null == betRecords || betRecords.size() == 0) {
                //如果 没有订购记录，直接更新彩票信息为已开奖
                iLotteryInfoService.updateLotteryInfo(period, type, Long.valueOf(price), result, randomNo, 2);
            } else {

                if (lotteryInfo.getIsFlag().trim().equals("1")) {
                    //系统开奖
                    systemOpenDrawAprize(randomNo, lotteryInfo, period, type, betRecords, Long.valueOf(price));
                }
                if (lotteryInfo.getIsFlag().trim().equals("2")) {
                    // 人工干预开奖结果
                    manualInterventionOpenDrawAprize(lotteryInfo, period, type, goldInfo, betRecords);
                }
            }

        }

//        Date nowTime = new Date();
//        try {
//            Thread.sleep(Objects.requireNonNull(endTime) - nowTime.getTime() - 10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        queryLotteryInfo();
        log.info("========== 抽奖业务处理结束 ==========");
        log.info("**************************************************************");
    }

    //系统开奖处理流程
    private void systemOpenDrawAprize(int randomNo, LotteryInfo lotteryInfo, String period, String type, List<BetRecord> betRecords, Long price) {
        iLotteryInfoService.updateLotteryInfo(period, type, price, "", randomNo, 1);
        //查询投注记录
        for (BetRecord betRecord : betRecords) {
            String userId = betRecord.getUserId();
            String number = betRecord.getNumber();
            Long orderId = betRecord.getOrderId();
            log.info("========== 系统开奖流程--->开始校验用户幸运号码，校验结果： {} ，当期用户id为：{} ，投注流水为：{} ，选择球的颜色为：{} ==========", number, userId, betRecord.getId(), betRecord.getSelectColour());
            if (StringUtil.isNotBlank(number) && StringUtil.isBlank(betRecord.getSelectColour())) {
                double odds = 9.00;
                selectNumResult(randomNo, lotteryInfo, period, type, price, betRecord, userId, number, orderId, odds);
            }

            if (StringUtil.isNotBlank(betRecord.getSelectColour()) && StringUtil.isBlank(number)) {
                selectColourResult(randomNo, lotteryInfo, period, type, price, betRecord, userId, orderId);
            }
        }

    }

    //人工干预开奖处理流程
    private void manualInterventionOpenDrawAprize(LotteryInfo lotteryInfo, String period, String type, GoldInfo goldInfo, List<BetRecord> betRecords) {
        for (BetRecord betRecord : betRecords) {
            String userId = betRecord.getUserId();
            String number = betRecord.getNumber();
            int selectNum = lotteryInfo.getNumber();
            Long orderId = betRecord.getOrderId();
            log.info("========== 人工开奖流程--->开始校验用户幸运号码 ，当期用户id为：{} ，投注流水为：{} ，选择球的颜色为：{} ==========", userId, betRecord.getId(), betRecord.getSelectColour());
            if (StringUtil.isNotBlank(number) && StringUtil.isBlank(betRecord.getSelectColour())) {
                double odds = 9.00;
                selectNumResult(selectNum, lotteryInfo, period, type, goldInfo.getPrice(), betRecord, userId, number, orderId, odds);
            }

            if (StringUtil.isNotBlank(betRecord.getSelectColour()) && StringUtil.isBlank(number)) {
                String colour = betRecord.getSelectColour();
                selectColourResult(selectNum, lotteryInfo, period, type, goldInfo.getPrice(), betRecord, userId, orderId);
            }
        }
    }

    private void selectNumResult(int num, LotteryInfo lotteryInfo, String period, String type, Long price, BetRecord betRecord, String userId, String number, Long orderId, double odds) {
        double amount;
        if (number.equals(String.valueOf(num))) {
            amount = betRecord.getBetAmount() * odds;
            if (num == 0) {
                luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, "RED,VIOLET", odds, amount, price);
            } else {
                if (num % 2 == 0) {
                    luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, Colour.RED.value, odds, amount, price);
                }
            }

            if (num == 5) {
                luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, "GREEN,VIOLET", odds, amount, price);
            } else {
                if (num % 2 == 1) {
                    luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, Colour.GREEN.value, odds, amount, price);
                }
            }

        } else {
            updateNoLuckResultInfo(num, period, type, price, betRecord, userId, orderId, odds);
        }
    }

    private void selectColourResult(int num, LotteryInfo lotteryInfo, String period, String type, Long price, BetRecord betRecord, String userId, Long orderId) {
        double amount;
        String colour = betRecord.getSelectColour();
        //1. 加入绿色：
        //如果结果显示 1，3，7，9，你会得到 （100*2） 200，如果结果显示 5，你会得到 （100*1.5） 150
        if (colour.equals(Colour.GREEN.value)) {

            if (num == 5) {
                amount = betRecord.getBetAmount() * 1.50;
                luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, colour, 1.50, amount, price);
            } else {
                if (num % 2 == 1) {
                    //获得的金额
                    amount = betRecord.getBetAmount() * 2;
                    luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, colour, 2.00, amount, price);
                } else {
                    double odds = 2.00;
                    updateNoLuckResultInfo(num, period, type, price, betRecord, userId, orderId, odds);
                }
            }
        }

        //2. 加入红色：
        //如果结果显示 2，4，6，8，你会得到 （100*2） 200;如果结果显示 0，您将得到 （100*1.5） 150
        if (colour.equals(Colour.RED.value)) {

            if (num == 0) {
                amount = betRecord.getBetAmount() * 1.50;
                luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, colour, 1.50, amount, price);
            } else {
                double odds = 2.00;
                if (num % 2 == 0) {
                    //获得的金额
                    amount = betRecord.getBetAmount() * 2;
                    luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, colour, odds, amount, price);
                } else {
                    updateNoLuckResultInfo(num, period, type, price, betRecord, userId, orderId, odds);
                }
            }

        }

        //3. 加入紫罗兰：
        //如果结果显示 0 或 5，您将得到 （100*4.5） 450
        if (colour.equals(Colour.VIOLET.value)) {
            double odds = 4.50;
            if (num == 0 || num == 5) {
                //获得的金额
                amount = betRecord.getBetAmount() * 4.50;
                luckSysnData(orderId, num, lotteryInfo, period, type, betRecord, userId, colour, odds, amount, price);
            } else {
                updateNoLuckResultInfo(num, period, type, price, betRecord, userId, orderId, odds);
            }

        }
    }


    private void updateNoLuckResultInfo(int num, String period, String type, Long price, BetRecord betRecord, String userId, Long orderId, double odds) {
        if (num == 0) {
            noLuckSysnData(orderId, num, period, type, betRecord, userId, price, "RED,VIOLET", odds, betRecord.getBetAmount());
        } else {
            if (num % 2 == 0) {
                noLuckSysnData(orderId, num, period, type, betRecord, userId, price, Colour.RED.value, odds, betRecord.getBetAmount());
            }
        }
        if (num == 5) {
            noLuckSysnData(orderId, num, period, type, betRecord, userId, price, "GREEN,VIOLET", odds, betRecord.getBetAmount());
        } else {
            if (num % 2 == 1) {
                noLuckSysnData(orderId, num, period, type, betRecord, userId, price, Colour.GREEN.value, odds, betRecord.getBetAmount());
            }
        }
    }


    //将用户所选的数字按照逗号切割，并且将其放入数组之中，然后添加到list集合之中
    private List<String> splitNumber(BetRecord betRecord) {
        String number = betRecord.getNumber();
        String[] nums = number.split(",");
        List<String> numberList = new ArrayList<>();
        Collections.addAll(numberList, nums);
        return numberList;
    }

    private void noLuckSysnData(Long orderId, int number, String period, String type, BetRecord betRecord, String userId, Long price, String result, Double odds, Double winOrLose) {
        //没有中奖
        log.info("**************************************************************");
        log.info("*****************************同步数据，处理未中奖流程开始*********************************");
        log.info("========== 很遗憾，没有中奖 ，当期用户id为：{} ，投注流水为：{} ，选择球的颜色为：{} ,选择的数字为：{} ==========", userId, betRecord.getId(), betRecord.getSelectColour(), number);
        iLotteryInfoService.updateLotteryInfo(period, type, price, result, number, 2);
        iBetRecordService.updateBetRecordInfo(orderId, period, type, userId, 1, result, odds, winOrLose, price);
        iOrderService.updateBetOrderInfoWinOrLosses(orderId,1,winOrLose);
        log.info("*****************************同步数据，处理未中奖流程结束*********************************");
        log.info("**************************************************************");
    }

    //同步数据，更新中奖结果
    private void luckSysnData(Long orderId, int randomNo, LotteryInfo lotteryInfo, String period, String type, BetRecord betRecord, String userId, String colour, Double odds, Double winOrLose, Long price) {
        log.info("**************************************************************");
        log.info("*****************************同步数据，处理已中奖流程开始*********************************");
        iLotteryInfoService.updateLotteryInfo(period, type, price, colour, randomNo, 2);
        iBetRecordService.updateBetRecordInfo(orderId, period, type, userId, 2, colour, odds, winOrLose, price);
        //计算用户幸运所得额，增加到用户的账户余额之中
        //调用账户中心，增加余额的接口，或者利用消息队列进行应用解耦。
        //这里使用直接调用账户中心的接口
        log.info("========== 开奖结束，调用账户中心，增加余额的接口,将用户所得幸运奖金增加至用户的主账户余额，幸运奖励金额为： {} ==========", winOrLose);
        Double blance = iAccountService.toRecharge(userId, winOrLose, PayMethod.ACCOUNT_PAY.id, OrderType.BETS.id);
        iOrderService.updateBetOrderInfoWinOrLosses(orderId,2,winOrLose);
        log.info("========== 开奖结束，账户余额为: {} ==========", blance);
        log.info("*****************************同步数据，处理已中奖流程结束*********************************");
        log.info("**************************************************************");

    }


    public void queryLotteryInfo() {
        log.info("**************************************************************");
        log.info("*****************************开始推送开奖结果到websocket*********************************");
        ResponseJsonResult responseJsonResult = iLotteryInfoService.queryLotteryInfos();
        String json_str = Objects.requireNonNull(JsonUtils.objectToJson(responseJsonResult));
        log.info("========== 推送开奖结果信息是：{} ==========", json_str);
        simpMessageSendingOperations.convertAndSend("/topic/resultPush", json_str);
        log.info("*****************************开奖结果推送完成*********************************");
        log.info("**************************************************************");
    }


}
