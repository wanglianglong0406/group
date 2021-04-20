package com.hy.lottery.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.enums.Colour;
import com.hy.lottery.mapper.LotteryInfoMapper;
import com.hy.lottery.model.GoldInfo;
import com.hy.lottery.model.LotteryInfo;
import com.hy.lottery.service.IGoldInfoService;
import com.hy.lottery.service.ILotteryInfoService;
import com.hy.lottery.stream.LotteryTopic;
import com.hy.lottery.stream.WebSocketTopic;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.DateUtil;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.hy.constant.Constant.*;

/**
 * (LotteryInfo)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-23 15:31:49
 */
@RestController
@Slf4j
public class LotteryInfoServiceImpl implements ILotteryInfoService {

    @Autowired
    private LotteryInfoMapper lotteryInfoMapper;
    @Autowired
    private LotteryTopic lotteryTopicProducer;
    @Autowired
    private IGoldInfoService iGoldInfoService;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private WebSocketTopic webSocketProducer;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryLotteryInfoList(String type) {
        System.out.println(type);
        if (StringUtil.isBlank(type)) {
            return ResponseJsonResult.errorMsg("Futures type cannot be empty");
        }
        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        QueryWrapper<LotteryInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("period", period_redis_str);
        //查询当前期数列表（还未开奖的，已开将的不不进行查询）
        queryWrapper.eq("lottery_status", 1);
        LotteryInfo lotteryInfo = lotteryInfoMapper.selectOne(queryWrapper);
        return ResponseJsonResult.ok(lotteryInfo);
    }



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryLotteryInfos() {
        QueryWrapper<LotteryInfo> queryWrapper = new QueryWrapper<>();
        List<GoldInfo> goldInfos = iGoldInfoService.queryGoldInfoList();
        ArrayList<String> list = new ArrayList<>();
        goldInfos.forEach(goldInfo -> list.add(goldInfo.getType()));
        queryWrapper.eq("lottery_status", 2).and(t-> t.in("type", list))
                .orderByDesc("start_time")
                .last("limit 4");
        List<LotteryInfo> lotteryInfos = lotteryInfoMapper.selectList(queryWrapper);
        return ResponseJsonResult.ok(lotteryInfos);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<LotteryInfo> queryLotterInfoByPeriod(String period) {
        LambdaQueryWrapper<LotteryInfo> lambdaQuery = Wrappers.<LotteryInfo>lambdaQuery();
        lambdaQuery.eq(LotteryInfo::getPeriod, period).eq(LotteryInfo::getLotteryStatus, "1");
        return lotteryInfoMapper.selectList(lambdaQuery);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int updateLotteryInfo(String period, String type, Long price, String result, Integer number,Integer openStatus) {
        LambdaUpdateWrapper<LotteryInfo> lambdaUpdateWrapper = Wrappers.<LotteryInfo>lambdaUpdate();
        lambdaUpdateWrapper.eq(LotteryInfo::getPeriod, period)
                .eq(LotteryInfo::getType, type)
                .eq(LotteryInfo::getLotteryStatus, 1)
                .set(LotteryInfo::getLotteryStatus, openStatus)
                .set(LotteryInfo::getPrice, price)
                .set(LotteryInfo::getResult, result)
                .set(LotteryInfo::getNumber, number)
                //.set(LotteryInfo::getEndTime, new Date())
                .set(LotteryInfo::getUpdatedTime, new Date())
                .set(LotteryInfo::getOpenTime, new Date());
        return lotteryInfoMapper.update(null, lambdaUpdateWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean operationLotteryResults(String period, String type, Long price, Integer number) {
        boolean update = new LambdaUpdateChainWrapper<LotteryInfo>(lotteryInfoMapper)
                .eq(LotteryInfo::getPeriod, period)
                .eq(LotteryInfo::getType, type)
                .eq(LotteryInfo::getLotteryStatus, 1)
                .set(LotteryInfo::getPrice, price)
                .set(LotteryInfo::getIsFlag, 2) //标识符 1 : 系统干预  2： 人工干预  3：其他
                .set(LotteryInfo::getNumber, number)
                .set(LotteryInfo::getEndTime, new Date())
                .set(LotteryInfo::getUpdatedTime, new Date()).update();
        return update;
    }

    @Override
    public PagedGridResult queryLotterInfoHistoryList(String type, Integer pageNo, Integer pageSize, String startDate, String endDate) {
        if (StringUtil.isBlank(type)) {
            return PagedGridResult.builder().pageNo(0).rows(null).pages(0).total(0).build();
        }

        if (StringUtil.isBlank(type)) {
            return PagedGridResult.builder().code(500).msg("This type cannot be empty !").build();
        }

        QueryWrapper<LotteryInfo> queryWrapper = new QueryWrapper<LotteryInfo>();
        queryWrapper.eq("type", type.trim());
        queryWrapper.eq("lottery_status",2);
        queryWrapper.orderByDesc("start_time");
        if(StringUtil.isNotBlank(startDate)){
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')",startDate.trim());
        }
        if(StringUtil.isNotBlank(endDate)){
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')",endDate.trim());
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        Page<LotteryInfo> page = new Page<LotteryInfo>(pageNo, pageSize, true);
        IPage<Map<String, Object>> iPage = lotteryInfoMapper.selectMapsPage(page, queryWrapper);
        List<Map<String, Object>> betRecordList = iPage.getRecords();
        return PagedGridResult.builder().pageNo(pageNo).rows(betRecordList).pages(iPage.getPages()).total(iPage.getTotal()).build();
    }


    @Override
    public void createLottery() {
        //当前时间
        Date date = new Date();
        //下单截止时间
        Date lastCreateOrderTime = DateUtil.addDateMinut(date, 150000L);
        //开奖结束时间
        Date endTime = DateUtil.addDateMinut(date, 180000L);

        String period;

        String period_redis_str = redisOperator.get(LOTTERY_ACTION_NO_REDIS_LOCK);
        if (StringUtils.isNotEmpty(period_redis_str)) {
            Long a = Long.valueOf(period_redis_str);
            a = ++a;
            period = String.valueOf(a);
            //截取当前字符串后三位数字
            String c = period.substring(period.length() - 3);
            //如果末尾三位的值等于480 period 需要从零开始计算，然后再加入缓存之中
            if (c.equals("480") || !period.substring(0, 8).equals(DateUtil.dateToStringyyyyMMdd(date))) {
                period = DateUtil.createActionNo(date);
                redisOperator.set(LOTTERY_ACTION_NO_REDIS_LOCK, period);
            }

        } else {
            period = DateUtil.createActionNo(date);
            redisOperator.set(LOTTERY_ACTION_NO_REDIS_LOCK, period);
        }
        //生成期数并且进行保存
        saveLotteryInfo(date, lastCreateOrderTime, endTime, period);

        redisOperator.set(LOTTERY_ACTION_NO_REDIS_LOCK, period);
        //发送一条消息到rabbitmq  利用rabbitmq的延时消息  2分30秒 后进行抽奖
        List<LotteryInfo> lotteryInfos = queryLotterInfoByPeriod(period);
        log.info("========== 开始发送消息到 rabbitmq ,发送总条数 {} ==========", lotteryInfos.size());
        lotteryTopicProducer.output().send(MessageBuilder.withPayload(lotteryInfos).setHeader(X_DELAY, THREE_MINUTES).build());
        log.info("========== 消息发送成功等待开奖客户端消费 {} ==========", lotteryInfos.toString());
        //TODO 落库保存消息状态


    }

    /**
     * 生产彩票信息
     *
     * @param date
     * @param lastCreateOrderTime
     * @param endTime
     * @param period
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveLotteryInfo(Date date, Date lastCreateOrderTime, Date endTime, String period) {
        log.info("========== 开始生成彩票信息 当期彩票代码为 {} ==========", period);

        List<GoldInfo> goldInfos = iGoldInfoService.queryGoldInfoList();

        if((Objects.requireNonNull(goldInfos).size() != 0)){

            goldInfos.forEach(goldInfo -> {
                String result = setResult(goldInfo);
                lotteryInfoMapper.insert(LotteryInfo.builder()
                        .period(period)
                        .isFlag("1")
                        .price(goldInfo.getPrice())
                        .number(goldInfo.getNumber())
                        .type(goldInfo.getType())
                        .lotteryStatus(1)
                        .createTime(date)
                        .updatedTime(date)
                        .startTime(date.getTime())
                        .lastCreateOrderTime(lastCreateOrderTime.getTime())
                        .endTime(endTime.getTime())
                        .result(result)
                        .build());
            });
        }else {
           throw new RuntimeException("========== 商品信息不存在=============");
        }
    }

    //设置结果
    private String setResult(GoldInfo goldInfo) {
        int number;
        String result = null;
        number=goldInfo.getNumber();
        if (number % 2 == 1 && number != 5) {
            result= Colour.GREEN.value;
        }
        if (number % 2 == 0 && number != 0) {
            result=Colour.RED.value;
        }
        if (number == 0) {
            result="RED,VIOLET";
        }
        if (number == 5) {
            result="GREEN,VIOLET";
        }
        return result;
    }



}