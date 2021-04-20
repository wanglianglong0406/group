package com.hy.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.account.mapper.BankcardInfoMapper;
import com.hy.account.model.Account;
import com.hy.account.model.BankcardInfo;
import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.account.service.IAccountService;
import com.hy.account.service.IBankcardInfoService;
import com.hy.constant.Constant;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 14:00
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 14:00
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class BankcardInfoServiceImpl implements IBankcardInfoService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private BankcardInfoMapper bankcardInfoMapper;
    @Autowired
    private IAccountService iAccountService;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult bindingBankCardInfo(BankCardInfoBO bankCardInfoBO) {
        String userId = bankCardInfoBO.getUserId();
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }

        Account account = iAccountService.getAccountInfo(userId);
        Long accountId = account.getAccountId();
        //将账户和银行卡进行绑定
        bankcardInfoMapper.insert(BankcardInfo.builder()
                .userId(userId)
                .accountId(accountId)
                .ifsc(bankCardInfoBO.getIfsc())
                .bankIsDefault(bankCardInfoBO.getBankIsDefault())
                .bankName(bankCardInfoBO.getBankName())
                .bankType(bankCardInfoBO.getBankType())
                .bankNo(bankCardInfoBO.getBankNo())
                .cardholderName(bankCardInfoBO.getCardholderName()).build());
        //返回银行卡信息
        return ResponseJsonResult.ok(getBankcardInfo(userId,accountId));
    }

    //获取银行卡信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public BankcardInfo getBankcardInfo(String userId,Long accountId){
        QueryWrapper<BankcardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_id",accountId);
        return bankcardInfoMapper.selectOne(queryWrapper);
    }
    //获取银行卡列表信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult getBankcardInfos(String userId,Long accountId){
        QueryWrapper<BankcardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("account_id",accountId);
        return ResponseJsonResult.ok(bankcardInfoMapper.selectList(queryWrapper));
    }

}

