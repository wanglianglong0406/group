package com.hy.lottery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.lottery.mapper.GoldInfoMapper;
import com.hy.lottery.model.GoldInfo;
import com.hy.lottery.model.LotteryInfo;
import com.hy.lottery.model.bo.GoldInfoBO;
import com.hy.lottery.service.IGoldInfoService;
import com.hy.lottery.service.ILotteryInfoService;
import com.hy.pojo.ResponseJsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:30
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:30
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@Slf4j
public class GoldInfoServiceImpl implements IGoldInfoService {
    @Autowired
    private GoldInfoMapper goldInfoMapper;
    @Autowired
    private ILotteryInfoService iLotteryInfoService;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<GoldInfo> queryGoldInfoList() {
        QueryWrapper<GoldInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gold_status", 1);
        return goldInfoMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public GoldInfo queryGoldInfoByType(String type) {
        QueryWrapper<GoldInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return goldInfoMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult updateGoldInfoByType(String type, Long price) {
        log.info("========== 开始更新黄金信息表,传入参数 类型为 ：{} 价格为 ：{}  ==========", type, price);

        //判断当前时间是否 小于 订单截至时间，如果小于订单截至时间，则可以进行人工干预，否正，不可以进行人工干预
        ResponseJsonResult responseJsonResult = iLotteryInfoService.queryLotteryInfoList(type);

        log.info("========== 人工干预的彩票信息为 {} ==========", responseJsonResult.getData());
        if (responseJsonResult.getStatus() != 200) {
            return ResponseJsonResult.errorMsg("System abnormality !");
        }
        LotteryInfo lotteryInfo = (LotteryInfo) responseJsonResult.getData();

        if (lotteryInfo != null) {
            log.info("========== 人工干预的彩票信息为 {} ==========", lotteryInfo.toString());

            Date nowTime = new Date();
            long lastCreateOrderTime = lotteryInfo.getLastCreateOrderTime();

            if (nowTime.getTime()>lastCreateOrderTime) {
                return ResponseJsonResult.errorMsg("The current time is greater than the opening time, and the manual intervention fails !");
            }
            //截取价格末尾数字，更新到 number
            String number = String.valueOf(price);
            Integer num = Integer.valueOf(number.substring(number.length() - 1));
            //修改当期彩票状态为人工干预状态  2
            //同步处理
            //也可以用MQ异步处理
            boolean result = iLotteryInfoService.operationLotteryResults(lotteryInfo.getPeriod(), type, price, num);
            if (!result) {
                log.info("========== 人工干预抽奖数据异常，需要人工处理,当期彩票为：{} 传入的类型为 ：{} 价格为 ：{} 开奖号码为 {} ==========", lotteryInfo.getPeriod(), type, price, number);
                log.info("========== 人工干预抽奖处理失败！执行结果为 {} ==========", false);
                return ResponseJsonResult.errorMsg("System abnormality !");
            }
            boolean update = new LambdaUpdateChainWrapper<GoldInfo>(goldInfoMapper)
                    .eq(GoldInfo::getType, type)
                    .eq(GoldInfo::getGoldStatus, 1)
                    .set(GoldInfo::getPrice, price)
                    .set(GoldInfo::getNumber, num)
                    .set(GoldInfo::getUpdatedTime, new Date())
                    .update();
            if (!update) {
                log.info("========== 更新黄金信息表失败！数据异常，需要人工处理,传入的类型为 ：{} 价格为 ：{}==========", type, price);
                log.info("========== 更新黄金信息表失败！执行结果为 {} ==========", false);
                return ResponseJsonResult.errorMsg("System abnormality !");
            }
            return ResponseJsonResult.ok("Modification succeeded!");
        } else {
            return ResponseJsonResult.errorMsg("Abnormal data !");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult saveGoldInfo(GoldInfoBO goldInfoBO) {
        goldInfoMapper.insert(GoldInfo.builder().createTime(new Date())
                .goldStatus(goldInfoBO.getGoldStatus()).number(goldInfoBO.getNumber()).price(goldInfoBO.getPrice())
                .type(goldInfoBO.getType())
                .createTime(new Date())
                .updatedTime(new Date())
                .build());
        return ResponseJsonResult.ok();

    }


}
