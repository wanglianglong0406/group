package com.hy.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.account.mapper.BankcardInfoMapper;
import com.hy.account.model.Account;
import com.hy.account.model.BankcardInfo;
import com.hy.account.model.bo.BankCardInfoBO;
import com.hy.account.service.IAccountService;
import com.hy.account.service.IBankcardInfoService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;
import static com.hy.constant.Constant.REDIS_USER_TOKEN;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 14:31
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 14:31
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
        String bankNo = bankCardInfoBO.getBankNo();
        String cardholderName = bankCardInfoBO.getCardholderName();
        String bankName = bankCardInfoBO.getBankName();
        String ifsc = bankCardInfoBO.getIfsc();
        String address = bankCardInfoBO.getAddress();
        String phone = bankCardInfoBO.getPhone();
        String bankBranch = bankCardInfoBO.getBankBranch();

        ResponseJsonResult x = checkParams(userId, bankNo, cardholderName, bankName, bankBranch, ifsc, address, phone);
        if (x != null) return x;

        Account account = iAccountService.getAccountInfo(userId);

        Long accountId = account.getAccountId();
        //将账户和银行卡进行绑定
        bankcardInfoMapper.insert(BankcardInfo.builder()
                .userId(userId)
                .accountId(accountId)
                .cardholderName(cardholderName)
                .phone(phone)
                .ifsc(ifsc)
                .bankName(bankName)
                .bankBranch(bankBranch)
                .bankNo(bankNo)
                .address(address)
                .build());
        //返回银行卡信息
        return ResponseJsonResult.ok(getBankcardInfo(userId));
    }


    //获取银行卡信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public BankcardInfo getBankcardInfo(String userId) {
        QueryWrapper<BankcardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return bankcardInfoMapper.selectOne(queryWrapper);
    }

    //获取银行卡列表信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult myBankcardInfos(String userId) {
        ResponseJsonResult x = checkParams(userId, "1", "1", "1", "1", "1", "1", "1");
        if (x != null) return x;
        QueryWrapper<BankcardInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return ResponseJsonResult.ok(bankcardInfoMapper.selectList(queryWrapper));
    }

    private ResponseJsonResult checkParams(String userId, String bankNo, String cardholderName, String bankName, String bankBranch, String ifsc, String address, String phone) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("The userId can not be empty");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }
        if (StringUtil.isBlank(bankNo)) {
            return ResponseJsonResult.errorMsg("The bank card number can not be empty");
        }
        if (StringUtil.isBlank(cardholderName)) {
            return ResponseJsonResult.errorMsg("The cardholderName can not be empty");
        }
        if (StringUtil.isBlank(bankName)) {
            return ResponseJsonResult.errorMsg("The bankName can not be empty");
        }
        if (StringUtil.isBlank(bankBranch)) {
            return ResponseJsonResult.errorMsg("The bankBranch can not be empty");
        }
        if (StringUtil.isBlank(ifsc)) {
            return ResponseJsonResult.errorMsg("The ifsc can not be empty");
        }
        if (StringUtil.isBlank(phone)) {
            return ResponseJsonResult.errorMsg("The phone can not be empty");
        }
        if (StringUtil.isBlank(address)) {
            return ResponseJsonResult.errorMsg("The address can not be empty");
        }
        return null;
    }
}
