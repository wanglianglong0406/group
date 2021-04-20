package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.account.service.IAccountService;
import com.hy.constant.Constant;
import com.hy.enums.*;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.UserMapper;
import com.hy.user.model.User;
import com.hy.user.model.bo.UserBO;
import com.hy.user.model.vo.UserVO;
import com.hy.user.service.IPassportService;
import com.hy.user.service.IUserService;
import com.hy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/26 13:38
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/26 13:38
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
@Slf4j
public class PassportServiceImpl implements IPassportService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService iUserService;

    //查询用户名是否存在
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUserNameIsExist(String userName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        User user = userMapper.selectOne(queryWrapper);
        return user == null ? false : true;
    }


    //检查手机号
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean checkUserMobilePhone(String userMobilePhone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_mobile_phone", userMobilePhone);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return false;
        }
        return true;
    }

    //查询邀请码是否存在
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryUserInvitationCodeIsExist(String invitationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_invitation_code", invitationCode);
        List<User> users = userMapper.selectList(queryWrapper);

        if (users == null || users.size() <= 0) {
            return null;
        }
        return users.get(0).getUserRegistrationCode();
    }


    @Override
    public ResponseJsonResult usernameIsExit(String userName) {
        log.info("校验用户是否存在");
        //判断用户名不能为空
        if (StringUtils.isBlank(userName)) {
            return ResponseJsonResult.errorMsg("用户名不能为空！！！");
        }
        //查找用户名是否存在
        boolean isExist = queryUserNameIsExist(userName);
        if (isExist) {
            return ResponseJsonResult.errorMsg("用户名已经存在！！！");
        }

        return ResponseJsonResult.ok();

    }

    @Override
    public ResponseJsonResult regist(UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String userName = userBO.getUserName().trim();
        String password = userBO.getUserLoginPassword().trim();
        String confirmPassword = userBO.getConfirmPassword().trim();
        String userMobilePhone = userBO.getUserMobilePhone().trim();
        String verificationCode = userBO.getVerificationCode().trim();
        String invitationCode = userBO.getUserInvitationCode().trim();
        String source = userBO.getSource().trim();
        //用户名和密码必须不为空
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return ResponseJsonResult.errorMsg("The user name or password cannot be empty !");
        }
        if (StringUtils.isBlank(source)) {
            return ResponseJsonResult.errorMsg("Registration source cannot be empty !");
        }
        //查找用户名是否存在
        boolean isExist = queryUserNameIsExist(userName);
        if (isExist) {
            return ResponseJsonResult.errorMsg("The user name already exists !");
        }


        if (password.length() < 4 || password.length() > 17) {
            return ResponseJsonResult.errorMsg("Password length must be 4 to 16 bit characters ！");
        }
        if (!MobileEmailUtils.validateMobilePhone(userMobilePhone)) {
            return ResponseJsonResult.errorMsg("The length of mobile phone is incorrect !");
        }


        //验证手机号是否存在
        if (!checkUserMobilePhone(userMobilePhone)) {
            return ResponseJsonResult.errorMsg("The mobile number has been registered !");
        }


        ResponseJsonResult x = iUserService.checkVerifcationCode(userMobilePhone, verificationCode);
        if (x.getStatus() != 200) return x;

        //验证邀请码是否存在
        String result = queryUserInvitationCodeIsExist(invitationCode);
        if (result != null) {//实现注册
            userBO.setUserInvitationCode(result);
            User usersResult = createUser(userBO);
            //通过邀请码验证邀请者用户，给予邀请者和被邀请者相应的奖励 这里奖励 8.00
            User user = queryUserInfoByUserRegistrationCode(invitationCode);
            //调用账户中心，增加奖励
            //邀请者，增加奖励  代理佣金奖励
            iAccountService.commissionBlance(user.getUserId(), 8.00, PayMethod.SYSTEM_PAY.id, OrderType.AGENCY_COMMISSION.id);
            //受邀者，增加奖励  邀请奖励
            iAccountService.invitationRewardBlance(usersResult.getUserId(), 8.00, PayMethod.SYSTEM_PAY.id, OrderType.INVITATION_REWARD.id);


            //实现用户的redis会话
            UserVO userVO = getUserVO(usersResult);
            //CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);
            return ResponseJsonResult.ok(userVO);
        } else {
            return ResponseJsonResult.errorMsg("Incorrect invitation code !");
        }

    }

    //通过邀请码，查询上级用户信息
    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserInfoByUserRegistrationCode(String invitationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_registration_code", invitationCode);
        return userMapper.selectOne(queryWrapper);
    }

    //创建用户信息
    @Transactional(propagation = Propagation.REQUIRED)
    public User createUser(UserBO userBO) {

        User user = new User();
        try {
            user = User.builder()
                    .createTime(new Date())
                    .updateTime(new Date())
                    .userMembershipLevel(Level.NOT_VIP.id)
                    .userStatus(UserStatus.NORMAL.id)
                    .userLevel(UserLevel.GENERAL_USER.id)
                    .source(userBO.getSource())
                    .userRegistrationCode(RandomUtils.getCharAndNumr(8))
                    .userInvitationCode(userBO.getUserInvitationCode())
                    .userMobilePhone(userBO.getUserMobilePhone())
                    .userLoginPassword(MD5Utils.getMD5Str(userBO.getUserLoginPassword()))
                    .userFace(Constant.USER_FAACE_IMGE)
                    .userBirthday(DateUtil.stringToDate("1990-01-01"))
                    .userSex(Sex.secret.type)
                    .userName(userBO.getUserName())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("--------> {} ", e.getMessage());
        }

        userMapper.insert(user);
        //需要对用户进行创建默认账户
        iAccountService.createDefaultAccount(user.getUserId());
        return user;
    }


    @Override
    public ResponseJsonResult login(String userName, String userLoginPassword, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //用户名和密码必须不为空
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userLoginPassword)) {
            return ResponseJsonResult.errorMsg("User name or password cannot be empty!");
        }
        //实现登录
        User usersResult = queryUserForLogin(userName, MD5Utils.getMD5Str(userLoginPassword));
        if (usersResult == null) {
            return ResponseJsonResult.errorMsg("Incorrect user name or password!");
        }

//        AuthResponse token = authService.tokenize(usersResult.getUserId());
//        if (!AuthCode.SUCCESS.getCode().equals(token.getCode())) {
//            log.error("Token error - uid={}", usersResult.getUserId());
//            return ResponseJsonResult.errorMsg("Token error！");
//        }
//        // 将token添加到Header当中
//        addAuth2Header(response, token.getAccount());
        UserVO userVO = getUserVO(usersResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);
        //将用户状态改为在线状态
        iUserService.updateUserStatus(usersResult.getUserId(), UserStatus.ON_LINE.id);
        return ResponseJsonResult.ok(userVO);

    }



    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserForLogin(String userName, String userLoginPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName).and(p -> p.eq("user_login_password", userLoginPassword));
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }


    @Override
    public ResponseJsonResult logout(HttpServletRequest request, HttpServletResponse response, String userId) {

//        Account account = Account.builder()
//                .token(request.getHeader(Constant.AUTH_HEADER))
//                .refreshToken(request.getHeader(Constant.REFRESH_TOKEN_HEADER))
//                .userId(userId)
//                .build();
//        AuthResponse auth = authService.delete(account);
//
//        if (!AuthCode.SUCCESS.getCode().equals(auth.getCode())) {
//            log.error("Token error - uid={}", userId);
//            return ResponseJsonResult.errorMsg("Token error");
//        }
        //清除用户相关的cookie
        CookieUtils.deleteCookie(request, response, "user");
        //分布式会话中需要清除redis 中用户数据
        redisOperator.del(Constant.REDIS_USER_TOKEN + ":" + userId);
        //退出时，将用户状态改为离线状态
        iUserService.updateUserStatus(userId, UserStatus.OFF_LINE.id);
        return ResponseJsonResult.ok();
    }




    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserInfo(String userId) {
        User users = userMapper.selectById(userId);
        users.setUserLoginPassword(null);
        return users;
    }



    @Override
    public ResponseJsonResult update(@RequestParam("userId") String userId,
                                     @RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
//        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
//        if (result.hasErrors()) {
//            Map<String, String> errorMap = getErrors(result);
//            return ResponseJsonResult.errorMap(errorMap);
//        }
        User userResult = modifyUserInfo(userId, userBO);
        //增加令牌token，整合进redis，分布式会话
        UserVO userVO = getUserVO(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

        return ResponseJsonResult.ok();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public User modifyUserInfo(String userId, UserBO UserBO) {
        User editUsers = new User();
        BeanUtils.copyProperties(UserBO, editUsers);
        editUsers.setUserId(userId);
        editUsers.setUpdateTime(new Date());
        userMapper.updateById(editUsers);
        return queryUserInfo(userId);
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyMobilePhone(String userId, String userMobilePhone) {
        User user = User.builder().userId(userId).userMobilePhone(userMobilePhone).updateTime(new Date()).build();
        userMapper.updateById(user);
    }




    @Transactional(propagation = Propagation.REQUIRED)
    public void modifyLoginPassword(String userId, String userLoginPassword) {
        User user = User.builder().userId(userId).userLoginPassword(userLoginPassword).updateTime(new Date()).build();
        userMapper.updateById(user);
    }


    // 在前端页面里拿到Authorization, refresh-token和 user-id。
    // 前端每次请求服务，都把这几个参数带上
//    private void addAuth2Header(HttpServletResponse response, Account token) {
//        response.setHeader(Constant.AUTH_HEADER, token.getToken());
//        response.setHeader(Constant.REFRESH_TOKEN_HEADER, token.getRefreshToken());
//        response.setHeader(Constant.UID_HEADER, token.getUserId());
//
//        // 让前端感知到，过期时间一天，这样可以在临近过期的时候refresh token
//        Calendar expTime = Calendar.getInstance();
//        expTime.add(Calendar.DAY_OF_MONTH, 1);
//        response.setHeader("token-exp-time", expTime.getTimeInMillis() + "");
//    }


    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError error : fieldErrors) {
            String errorFieId = error.getField();//发生验证错误所对应的属性
            String errorMsg = error.getDefaultMessage();
            map.put(errorFieId, errorMsg);
        }
        return map;
    }

    public UserVO getUserVO(User usersResult) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(Constant.REDIS_USER_TOKEN + ":" + usersResult.getUserId(), uniqueToken, 60 * 60 * 2);
        return UserVO.builder().userFace(usersResult.getUserFace()).userName(usersResult.getUserName()).id(usersResult.getUserId())
                .userMembershipLevel(usersResult.getUserMembershipLevel())
                .userStatus(usersResult.getUserStatus())
                .userSex(usersResult.getUserSex()).userUniqueToken(uniqueToken).build();
    }


}
