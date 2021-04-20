package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hy.account.model.Account;
import com.hy.account.service.IAccountService;
import com.hy.constant.Constant;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.UserMapper;
import com.hy.user.model.User;
import com.hy.user.model.bo.UserBO;
import com.hy.user.model.vo.InvitationRecordVO;
import com.hy.user.service.IUserService;
import com.hy.utils.MobileEmailUtils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.hy.constant.Constant.*;

/**
 * 用户信息表 用户信息表(User)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-17 13:55:42
 */
@RestController
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private UserMapper userMapper;


    //手机短信获取验证码
    public String verificationCode(String userMobilePhone) {
        //TODO 调用验证码接口
        //得到验证码，存入redis中
        String verificationCode = "111111";
        redisOperator.set(Constant.VERIFICATION_CODE + ":" + userMobilePhone, verificationCode, 120);
        return verificationCode;

    }


    @Override
    public ResponseJsonResult sendVerificationCode(String userMobilePhone) {
        if (!MobileEmailUtils.validateMobilePhone(userMobilePhone)) {
            return ResponseJsonResult.errorMsg("he length of mobile phone is incorrect !");
        }
        return ResponseJsonResult.ok(verificationCode(userMobilePhone));
    }


    //通过邀请码，查询上级用户信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfoByUserInvitationCode(String invitationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_registration_code", invitationCode);
        return userMapper.selectOne(queryWrapper);
    }





    //更新用户状态
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public int updateUserStatus(String userId, Integer userStatus) {
        User user = new User();
        user.setUserId(userId);
        user.setUpdateTime(new Date());
        user.setUserStatus(userStatus);
        int resutl = userMapper.updateById(user);
        return resutl;
    }

    //更新用户等级
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public int memberUpgrade(String userId) {
        User user = queryUserInfoByUserId(userId);
        if (user.getUserMembershipLevel() == 10) {
            return 1;
        }
        user.setUserId(userId);
        user.setUpdateTime(new Date());
        user.setUserLevel(user.getUserMembershipLevel() + 1);
        int resutl = userMapper.updateById(user);
        return resutl;
    }

    @Override
    public int memberDemotion(String userId) {
        User user = queryUserInfoByUserId(userId);
        if (user.getUserMembershipLevel() == 0) {
            return 1;
        }
        user.setUserId(userId);
        user.setUpdateTime(new Date());
        user.setUserLevel(user.getUserMembershipLevel() - 1);
        int resutl = userMapper.updateById(user);
        return resutl;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserForLogin(String userName, String userLoginPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName).and(p -> p.eq("user_login_password", userLoginPassword));
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }


    @Override
    public ResponseJsonResult findUserInfo(String userId) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("用户ID 不能为空！");
        }
        return ResponseJsonResult.ok(queryUserInfoByUserId(userId));

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfoByUserId(String userId) {
        User users = userMapper.selectById(userId);
        users.setUserLoginPassword(null);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> querUserInfoList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_status", Arrays.asList(0,2,3));
        return userMapper.selectList(queryWrapper);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User modifyUserInfo(String userId, UserBO UserBO) {
        User editUsers = new User();
        BeanUtils.copyProperties(UserBO, editUsers);
        editUsers.setUserId(userId);
        editUsers.setUpdateTime(new Date());
        userMapper.updateById(editUsers);
        return queryUserInfoByUserId(userId);
    }


    //检查验证码
    public ResponseJsonResult checkVerifcationCode(String userMobilePhone, String verificationCode) {
        return getResponseJsonResult(userMobilePhone, verificationCode, redisOperator);

    }

    static ResponseJsonResult getResponseJsonResult(String userMobilePhone, String verificationCode, RedisOperator redisOperator) {
        String verificationCodeJsonRedis = redisOperator.get(Constant.VERIFICATION_CODE + ":" + userMobilePhone);
        if (verificationCode == null) {
            return ResponseJsonResult.errorMsg("Receiving verification code cannot be empty !");
        }
        if (StringUtils.isBlank(verificationCodeJsonRedis)) {
            return ResponseJsonResult.errorMsg("Verification code has expired, please re-enter !");
        }
        if (!verificationCodeJsonRedis.equals(verificationCode)) {
            return ResponseJsonResult.errorMsg("The verification code is incorrect, please re-enter!");
        }
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult modifyMobilePhone(String userId, String userMobilePhone, String verificationCode) {
        ResponseJsonResult x = checkVerifcationCode(userMobilePhone, verificationCode);
        if (x.getStatus() != 200) return x;
        modifyMobilePhone(userId, userMobilePhone);
        return ResponseJsonResult.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyMobilePhone(String userId, String userMobilePhone) {
        User user = User.builder().userId(userId).userMobilePhone(userMobilePhone).updateTime(new Date()).build();
        userMapper.updateById(user);
    }


    @Override
    public ResponseJsonResult modifyLoginPassword(String userId, String userLoginPassword, String userMobilePhone, String verificationCode) {

        ResponseJsonResult x = checkVerifcationCode(userMobilePhone, verificationCode);
        if (x.getStatus() != 200) return x;
        modifyLoginPassword(userId, userLoginPassword);
        return ResponseJsonResult.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyLoginPassword(String userId, String userLoginPassword) {
        User user = User.builder().userId(userId).userLoginPassword(userLoginPassword).updateTime(new Date()).build();
        userMapper.updateById(user);
    }


    @Override
    public void modifyUserAddress(String userId) {
        //TODO  修改用户地址
    }


    //通过注册码查询下级用户
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> queryUserInfoByUserRegistrationCode(String userRegistrationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_invitation_code", userRegistrationCode);
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryInvitationRecordInfoList(String userId, Integer pageNo, Integer pageSize, String startDate, String endDate) {

        if (StringUtil.isBlank(userId)) {
            return PagedGridResult.builder().code(500).msg("This userId cannot be empty !").build();
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return PagedGridResult.builder().code(LOGIN_EXPIRED_CODE).msg("Login expired, please login again !").build();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("user_id", userId);

        queryWrapper.orderByDesc("create_time");
        if (StringUtil.isNotBlank(startDate)) {
            queryWrapper.ge("date_format(create_time,'%Y-%m-%d')", startDate);
        }
        if (StringUtil.isNotBlank(endDate)) {
            queryWrapper.le("date_format(create_time,'%Y-%m-%d')", endDate);
        }
        if (pageNo == null) {
            pageNo = PAGE_NO;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        Page<User> page = new Page<User>(pageNo, pageSize, true);
        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
        List<User> userList = iPage.getRecords();

        List<InvitationRecordVO> invitationRecordVOList = new ArrayList<>();
        userList.forEach(user1 -> {
            List<User> users = queryUserInfoByUserRegistrationCode(user1.getUserRegistrationCode());
            users.forEach(user -> {

                Account account = iAccountService.getAccountInfo(user.getUserId());
                InvitationRecordVO invitationRecordVO = InvitationRecordVO.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userStatus(user.getUserStatus())
                        .rewardAmount(account.getCommissionBlance())
                        .rewardCategory("Invitation Reward")
                        .createTime(user.getCreateTime())
                        .build();
                invitationRecordVOList.add(invitationRecordVO);
            });


        });
        return PagedGridResult.builder().pageNo(pageNo).rows(invitationRecordVOList).pages(iPage.getPages()).total(iPage.getTotal()).build();
    }

}