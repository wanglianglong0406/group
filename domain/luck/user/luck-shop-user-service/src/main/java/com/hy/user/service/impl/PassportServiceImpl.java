package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.account.service.IAccountService;
import com.hy.enums.Level;
import com.hy.enums.Sex;
import com.hy.enums.UserLevel;
import com.hy.enums.UserStatus;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.UserMapper;
import com.hy.user.model.User;
import com.hy.user.model.bo.UserBO;
import com.hy.user.model.vo.UserVO;
import com.hy.user.service.IPassportService;
import com.hy.user.service.IUserService;
import com.hy.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.UUID;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 13:29
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 13:29
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class PassportServiceImpl implements IPassportService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IAccountService iAccountService;

    //检查手机号
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean checkMobilePhone(String userMobilePhone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile_phone", userMobilePhone);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return false;
        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String checkRegistrationCode(String registrationCode) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("registration_code", registrationCode);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            registrationCode =RandomUtils.getCharAndNumr(10);
        }
        return registrationCode;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User createUser(UserBO userBO) {

        User user = null;
        try {
            String phone = userBO.getMobilePhone();
            String name = "User" + phone.substring(phone.length() - 4);
            user = User.builder()
                    .createTime(LocalDate.now())
                    .updateTime(LocalDate.now())
                    .membershipLevel(Level.VIP_ZERO.id)
                    .userStatus(UserStatus.NORMAL.id)
                    .userLevel(UserLevel.GENERAL_USER.id)
                    .source(userBO.getSource())
                    .registrationCode(checkRegistrationCode(RandomUtils.getCharAndNumr(10)))
                    .invitationCode(userBO.getInvitationCode())
                    .mobilePhone(userBO.getMobilePhone())
                    .password(MD5Utils.getMD5Str(userBO.getPassword()))
                    .birthday(DateUtil.stringToDate("1990-01-01"))
                    .sex(Sex.secret.type)
                    .userName(name)
                    .nicknames(name)
                    .build();
            userMapper.insert(user);
            iAccountService.createDefaultAccount(user.getUserId());
            return user;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;


    }




    @Override
    public ResponseJsonResult regist(UserBO userBO, HttpServletRequest request, HttpServletResponse response) {

        String password = userBO.getPassword().trim();
        String mobilePhone = userBO.getMobilePhone();


        if (password.length() < 4 || password.length() > 17) {
            return ResponseJsonResult.errorMsg("Password length must be 4 to 16 bit characters ！");
        }
        if (!MobileEmailUtils.validateMobilePhone(mobilePhone)) {
            return ResponseJsonResult.errorMsg("The length of mobile phone is incorrect !");
        }
        //验证手机号是否存在
        boolean flag = checkMobilePhone(userBO.getMobilePhone());
        if (!flag) {
            return ResponseJsonResult.errorMsg("The mobile number has been registered !");
        }

        User userResult = createUser(userBO);
        if(userResult ==null){
            return ResponseJsonResult.errorMsg("System Anomaly");
        }
        UserVO userVO = getUserVO(userResult);


        String invitationCode=userBO.getInvitationCode();
        if (StringUtils.isNotBlank(invitationCode)) {
            //更新用户团队规模
            iUserService.updateUserTeamSize(invitationCode);
        }

        return ResponseJsonResult.build(SUCCESS_CODE, "Registration successful", userVO);
    }

    @Override
    public ResponseJsonResult login(String mobilePhone, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //用户名和密码必须不为空
        if (StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(password)) {
            return ResponseJsonResult.errorMsg("User mobilePhone or password cannot be empty!");
        }
        //实现登录
        User userResult = queryUserForLogin(mobilePhone, MD5Utils.getMD5Str(password));
        if (userResult == null) {
            return ResponseJsonResult.errorMsg("Incorrect mobilePhone name or password!");
        }

        redisOperator.set(REDIS_USER_INFO + ":" + userResult.getUserId(), JsonUtils.objectToJson(userResult));


//        AuthResponse token = authService.tokenize(usersResult.getUserId());
//        if (!AuthCode.SUCCESS.getCode().equals(token.getCode())) {
//            log.error("Token error - uid={}", usersResult.getUserId());
//            return ResponseJsonResult.errorMsg("Token error！");
//        }
        // 将token添加到Header当中
        //addAuth2Header(response, token.getAccount());
        //将用户状态改为在线状态
        iUserService.updateUserStatus(userResult.getUserId(), UserStatus.ON_LINE.id);
        UserVO userVO = getUserVO(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(userVO), true);

        return ResponseJsonResult.build(SUCCESS_CODE, "Login successful", userVO);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public User queryUserForLogin(String mobilePhone, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile_phone", mobilePhone).eq("password", password);
        return userMapper.selectOne(queryWrapper);
    }


    public UserVO getUserVO(User userResult) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + userResult.getUserId(), uniqueToken, 60 * 60 * 2);
        return UserVO.builder().nickName(userResult.getNicknames()).userName(userResult.getUserName()).userId(userResult.getUserId())
                .membershipLevel(userResult.getMembershipLevel())
                .userStatus(userResult.getUserStatus())
                .sex(userResult.getSex()).userUniqueToken(uniqueToken).build();
    }

}
