package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hy.account.model.Account;
import com.hy.account.service.IAccountService;
import com.hy.constant.Constant;
import com.hy.enums.AccountStatus;
import com.hy.enums.OrderType;
import com.hy.enums.PayMethod;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.SignHistoryMapper;
import com.hy.user.mapper.SignInfoMapper;
import com.hy.user.model.SignInfo;
import com.hy.user.model.SignInfoHistroy;
import com.hy.user.service.ISignService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 19:26
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 19:26
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class SignServiceImpl implements ISignService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private SignInfoMapper signInfoMapper;
    @Autowired
    private SignHistoryMapper signHistoryMapper;
    @Autowired
    private IAccountService iAccountService;
    Gson gson = new Gson();

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult signIn(String userId, String weekDay) {
        if (StringUtils.isEmpty(weekDay) || StringUtils.isEmpty(userId)) {
            return ResponseJsonResult.errorMsg("Param cannot be empty !");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }

        Map<String, String> map = new HashMap<>();
        map.put("week_" + weekDay, weekDay);

        //查询是否存在签到记录
        SignInfo signInfo = getSignInfo(userId);
        if (signInfo == null) {
            signInfoMapper.insert(SignInfo.builder().userId(userId).weekDay(JsonUtils.objectToJson(map))
                    .isFlag(1)
                    .countTotal(1)
                    .continuousSignInCount(1)
                    .createTime(new Date())
                    .updateTime(new Date())
                    .lastSignTime(new Date())
                    .build());
        } else {
            String str_json = signInfo.getWeekDay().trim();
            Map<String, String> weekMap = gson.fromJson(str_json, Map.class);
            weekMap.put(weekDay, weekDay);
            String mapJson = gson.toJson(weekMap);
            signInfoMapper.updateById(SignInfo.builder().id(signInfo.getId())
                    .weekDay(mapJson)
                    .countTotal(signInfo.getCountTotal() + 1)
                    .lastSignTime(new Date())
                    .updateTime(new Date()).build());
        }
        signInfo = getSignInfo(userId);

        SignInfoHistroy signInfoHistroy = SignInfoHistroy.builder().signId(signInfo.getId()).weekDay(signInfo.getWeekDay()).isResetFlag(2).signStatus(1).countTotal(signInfo.getCountTotal() + 1)
                .createTime(new Date()).updateTime(new Date()).lastSignTime(new Date())
                .build();
        signHistoryMapper.insert(signInfoHistroy);

        //判断账户是否正常，如果账户状态处于正在开户中，签到奖励不纳入计算之列，直接返回签到成功
        ResponseJsonResult responseJsonResult = iAccountService.queryAccountInfo(userId);
        if (responseJsonResult.getStatus() != 200) {
            return ResponseJsonResult.errorMsg("The request data is loading...");
        }

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.convertValue(responseJsonResult.getData(), Account.class);

        int accountStatus = account.getAccountStatus();
        if (accountStatus != AccountStatus.NORMAL.type) {
            return ResponseJsonResult.build(200,"Sign in successfully","");
        }

        return changeBlance(userId, weekDay,account.getRewardAmount());

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public SignInfo getSignInfo(String userId) {
        QueryWrapper<SignInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return signInfoMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseJsonResult changeBlance(String userId, String weekDay,Double rewardAmount) {
        System.out.println(iAccountService.queryAccountBlance(userId));
        ResponseJsonResult responseJsonResult = iAccountService.queryAccountInfo(userId);
        if (responseJsonResult.getStatus() != 200) {
            return ResponseJsonResult.errorMsg("Sign in failed,Please try again later...");
        }

        Double reward_amount = checkKey(rewardAmount, weekDay);

        Boolean result = iAccountService.signInReward(userId, reward_amount, PayMethod.SYSTEM_PAY.id, OrderType.SIGN_IN.id);

        return ResponseJsonResult.build(200,"Sign in successfully","");

    }
    //校验是否每日签到，如果是，则进行签到奖励 1块钱  单位/分
    private Double checkKey(Double rewardAmount, String weekDay) {
        double reward_amount = 0.00;
        //构建一个星期的Map
        Map<String, String> week_map = new HashMap<>();
        for (int i = 1; i < 8; i++) {
            week_map.put("week_" + i, String.valueOf(i));
        }
        for (String key : week_map.keySet()) {
            String value = week_map.get(key);
            if (key.equals("week_" + weekDay) || value.equals(weekDay)) {
                reward_amount = rewardAmount + 1.00;
                break;
            }
        }

        return reward_amount;
    }


    //   0 59 1 ?* MON   每周一凌晨一点执行
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void resetCheckInStatistics() {

        LambdaUpdateWrapper<SignInfo> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(SignInfo::getIsFlag, 1)
                .set(SignInfo::getIsFlag, 0)
                .set(SignInfo::getCountTotal, 0)
                .set(SignInfo::getWeekDay, null)
                .set(SignInfo::getUpdateTime, new Date());
        signInfoMapper.update(null, lambdaUpdateWrapper);


        UpdateWrapper<SignInfoHistroy> SignHistoryUpdateWrapper = new UpdateWrapper<>();
        SignHistoryUpdateWrapper.eq("is_reset_flag", 2);
        SignInfoHistroy signInfoHistroy = SignInfoHistroy.builder()
                .isResetFlag(1).signStatus(1).countTotal(0)
                .updateTime(new Date()).build();
        signHistoryMapper.update(signInfoHistroy, SignHistoryUpdateWrapper);

    }


}
