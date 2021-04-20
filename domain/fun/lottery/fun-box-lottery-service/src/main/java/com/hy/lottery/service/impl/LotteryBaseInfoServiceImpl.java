package com.hy.lottery.service.impl;

import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.hy.lottery.mapper.LotteryBaseInfoMapper;
import com.hy.lottery.model.LotteryBaseInfo;
import com.hy.lottery.service.ILotteryBaseInfoService;
import com.hy.pojo.ResponseJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:11
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:11
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class LotteryBaseInfoServiceImpl implements ILotteryBaseInfoService {
    @Autowired
    private LotteryBaseInfoMapper lotteryBaseInfoMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryLotteryBasicInfo() {
        List<LotteryBaseInfo> lotteryBaseInfos = new LambdaQueryChainWrapper<LotteryBaseInfo>(lotteryBaseInfoMapper).list();
        return ResponseJsonResult.ok(lotteryBaseInfos);
    }




}
