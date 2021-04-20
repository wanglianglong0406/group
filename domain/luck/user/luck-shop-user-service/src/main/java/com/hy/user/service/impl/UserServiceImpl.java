package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hy.constant.Constant;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.AgencyLevelMapper;
import com.hy.user.mapper.UserAgentDistributionInfoMapper;
import com.hy.user.mapper.UserMapper;
import com.hy.user.model.AgencyLevel;
import com.hy.user.model.User;
import com.hy.user.model.UserAgentDistributionInfo;
import com.hy.user.service.IUserService;
import com.hy.utils.MobileEmailUtils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.hy.constant.Constant.PAGE_NO;
import static com.hy.constant.Constant.PAGE_SIZE;


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
    //    @Autowired
//    private AuthService authService;
//    @Autowired
//    private IAccountService iAccountService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AgencyLevelMapper agencyLevelMapper;
    @Autowired
    private UserAgentDistributionInfoMapper userAgentDistributionInfoMapper;


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


    //检查验证码
    @Override
    public ResponseJsonResult checkVerifcationCode(String mobilePhone, String verificationCode) {
        String verificationCodeJsonRedis = redisOperator.get(Constant.VERIFICATION_CODE + ":" + mobilePhone);
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




    //更新用户状态
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int updateUserStatus(String userId, Integer userStatus) {
        User user = new User();
        user.setUserId(userId);
        user.setUpdateTime(LocalDate.now());
        user.setUserStatus(userStatus);
        return userMapper.updateById(user);
    }

    //更新用户等级
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int memberUpgrade(String userId) {
        User user = queryUserInfoByUserId(userId);
        if (user.getMembershipLevel() == 3) {
            return 1;
        }
        user.setUserId(userId);
        user.setUpdateTime(LocalDate.now());
        user.setUserLevel(user.getMembershipLevel() + 1);
        return userMapper.updateById(user);
    }

    @Override
    public int memberDemotion(String userId) {
        User user = queryUserInfoByUserId(userId);
        if (user.getMembershipLevel() == 0) {
            return 1;
        }
        user.setUserId(userId);
        user.setUpdateTime(LocalDate.now());
        user.setUserLevel(user.getMembershipLevel() - 1);
        return userMapper.updateById(user);
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
        users.setPassword(null);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> querUserInfoList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_status", Arrays.asList(0, 2, 3));
        return userMapper.selectList(queryWrapper);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> getUserAgentInfoList() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_status", Arrays.asList(0, 2, 3)).ge("team_size",1);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public ResponseJsonResult querySubUserInfo(String registrationCode) {
        return ResponseJsonResult.ok(queryUserInfoByRegistrationCode(registrationCode));
    }


    //通过注册码查询下级用户
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> queryUserInfoByRegistrationCode(String registrationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invitation_code", registrationCode);
        return userMapper.selectList(queryWrapper);
    }

    //通过邀请码，查询上级用户信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public User queryUserInfoByInvitationCode(String invitationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("registration_code", invitationCode);
        return userMapper.selectOne(queryWrapper);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int updateUserTeamSize(String invitationCode) {

        User superiorUser = queryUserInfoByInvitationCode(invitationCode);
        User user = new User();
        user.setUserId(superiorUser.getUserId());
        user.setUpdateTime(LocalDate.now());
        user.setTeamSize(superiorUser.getTeamSize() + 1);
        return userMapper.updateById(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public int updateUserAgencyLevel(String userId, Integer agencyLevel) {
        User user = new User();
        user.setUserId(userId);
        user.setUpdateTime(LocalDate.now());
        user.setAgencyLevel(agencyLevel);
        return userMapper.updateById(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<AgencyLevel> getAgencyLevelInfoList() {
        QueryWrapper<AgencyLevel> queryWrapper = new QueryWrapper<>();
        return agencyLevelMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAgentDistributionInfo> getUserAgentDistributionInfoList() {
        QueryWrapper<UserAgentDistributionInfo> queryWrapper = new QueryWrapper<>();
        return userAgentDistributionInfoMapper.selectList(queryWrapper);
    }

    @Override
    public UserAgentDistributionInfo getUserAgentDistributionInfoByVipLevelAndAgencyLevel(Integer vipLevel, Integer agencyLevel) {
        QueryWrapper<UserAgentDistributionInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vip_level",vipLevel).eq("agency_level",agencyLevel);
        return userAgentDistributionInfoMapper.selectOne(queryWrapper);
    }


    //查询用户列表
    @Override
    public ResponseJsonResult getUserInfoPagedGridResult(Integer pageNo, Integer pageSize, String startDate, String endDate) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
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

        Page<User> page = new Page<>(pageNo, pageSize, true);
        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
        List<User> orderList = iPage.getRecords();
        return ResponseJsonResult.ok(PagedGridResult.builder().pageNo(pageNo).rows(orderList).pages(iPage.getPages()).total(iPage.getTotal()).build());
    }
}