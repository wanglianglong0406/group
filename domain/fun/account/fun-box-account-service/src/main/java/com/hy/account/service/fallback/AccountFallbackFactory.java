package com.hy.account.service.fallback;

import com.hy.account.model.Account;
import com.hy.account.model.AccountType;
import com.hy.pojo.ResponseJsonResult;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: $- IAccountService 服务降级处理流程-$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/20 15:17
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/20 15:17
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
@Component
public class AccountFallbackFactory implements FallbackFactory<AccountServiceFeignClient> {


    @Override
    public AccountServiceFeignClient create(Throwable throwable) {

        return new AccountServiceFeignClient() {

            @Override
            public ResponseJsonResult queryAccountTypeInfoList(String userId) {
                return null;
            }

            @Override
            public List<AccountType> getAccountTypeInfoList() {
                return null;
            }

            @Override
            public ResponseJsonResult transferIn(String userId, Integer accountType, Double amount) {
                return null;
            }

            @Override
            public ResponseJsonResult lockWallet(String userId) {
                return null;
            }

            @Override
            public ResponseJsonResult transferOut(String userId) {
                return null;
            }

            @Override
            public Account createDefaultAccount(String userId) {
                log.error("Fallback: Loading... {} ", userId);

                return Account.builder().userId(userId).build();
            }

            @Override
            public ResponseJsonResult accountRecharge(String userId, Double amount, Long accountId, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Double toRecharge(String userId, Double amount, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public void rechargeRebate(String userId) {

            }

            @Override
            public Boolean signInReward(String userId, Double reward_amount, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Boolean rechargeRebateBlance(String userId, Double rechargeRebate, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Boolean commissionBlance(String userId, Double commission, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Boolean invitationRewardBlance(String userId, Double invitationReward, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Boolean upgradeRewardBlance(String userId, Double upgradeReward, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public Boolean monthlyTicketBlance(String userId, Double monthlyTicket, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public ResponseJsonResult pay(String userId, Long orderId, Double amount, String accountPassword, Integer payMethod, Integer orderType) {
                return null;
            }

            @Override
            public ResponseJsonResult accountPayment(String userId, Double amount, Long orderId) {
                return null;
            }

            @Override
            public ResponseJsonResult checkUserAccountPassword(String userId, String accountPassword) {
                return null;
            }

            @Override
            public ResponseJsonResult checkUserAccountStatus(String userId) {
                return null;
            }


            @Override
            public ResponseJsonResult queryAccountBlance(String userId) {
                return null;
            }

            @Override
            public ResponseJsonResult queryAccountInfo(String userId) {
                return null;
            }

            @Override
            public Account getAccountInfo(String userId) {
                return null;
            }

            @Override
            public boolean openAccount(String userId, long accountId) {
                return false;
            }

            @Override
            public ResponseJsonResult querySecondaryAccountInfo(String userId) {
                return null;
            }

            @Override
            public ResponseJsonResult totalInvitationGiftAndRebateReward(String userId) {
                return null;
            }

            @Override
            public ResponseJsonResult totalInvitationRewardAndCommissionReward(String userId) {
                return null;
            }
        };
    }
}
