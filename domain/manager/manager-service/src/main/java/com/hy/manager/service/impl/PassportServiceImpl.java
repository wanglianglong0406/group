package com.hy.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.enums.UserLevel;
import com.hy.enums.UserStatus;
import com.hy.manager.mapper.ManagerUserMapper;
import com.hy.manager.model.ManagerUser;
import com.hy.manager.model.bo.SystemManagerUserBO;
import com.hy.manager.service.IPassportService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.MD5Utils;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:16
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:16
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class PassportServiceImpl implements IPassportService {
    @Autowired
    private ManagerUserMapper managerUserMapper;
    @Autowired
    private RedisOperator redisOperator;


    //检查用户名是否存在
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean checkManagerUserNameIsExists(String userName, Integer platformChannel) {
        QueryWrapper<ManagerUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        queryWrapper.eq("platform_channel", platformChannel);
        ManagerUser managerUser = managerUserMapper.selectOne(queryWrapper);
        if (managerUser == null) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public ResponseJsonResult registSystemManagerUser(SystemManagerUserBO systemManagerUserBO) {

        String password = systemManagerUserBO.getPassword().trim();


        if (password.length() < 4 || password.length() > 17) {
            return ResponseJsonResult.errorMsg("Password length must be 4 to 16 bit characters ！");
        }
        if (systemManagerUserBO.getPlatformChannel() == null) {
            return ResponseJsonResult.errorMsg("Platform channel cannot be empty ！");

        }
//        if (systemManagerUserBO.getPlatformChannel() != 1 || systemManagerUserBO.getPlatformChannel() != 2) {
//            return ResponseJsonResult.errorMsg("Incorrect platform channel number ！");
//        }
        if (systemManagerUserBO.getUserName() == null) {
            return ResponseJsonResult.errorMsg("UserName cannot be empty ！");
        }


        //验证用户名是否存在
        boolean flag = checkManagerUserNameIsExists(systemManagerUserBO.getUserName(), systemManagerUserBO.getPlatformChannel());
        if (!flag) {
            return ResponseJsonResult.errorMsg("The UserName has been registered !");
        }

        ManagerUser managerUserResult = createSystemManagerUser(systemManagerUserBO);
        if (managerUserResult == null) {
            return ResponseJsonResult.errorMsg("System Anomaly");
        }
        ManagerUser showManagerUserInfo = showUserInfoAndGetUserToken(managerUserResult);

        return ResponseJsonResult.build(SUCCESS_CODE, "Registration successful", showManagerUserInfo);
    }


    //创建系统用户
    @Transactional(propagation = Propagation.REQUIRED)
    public ManagerUser createSystemManagerUser(SystemManagerUserBO systemManagerUserBO) {

        ManagerUser managerUser = null;
        try {
            managerUser = ManagerUser.builder()
                    .createTime(new Date())
                    .updateTime(new Date())
                    .userStatus(UserStatus.NORMAL.id)
                    .userLevel(UserLevel.SYSTEM_USER.id)
                    .password(MD5Utils.getMD5Str(systemManagerUserBO.getPassword()))
                    .userName(systemManagerUserBO.getUserName())
                    .platformChannel(systemManagerUserBO.getPlatformChannel())
                    .build();
            managerUserMapper.insert(managerUser);
            return managerUser;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return managerUser;


    }

    @Override
    public ResponseJsonResult loginSystemManagerUser(String userName, String password, Integer platformChannel) throws Exception {
        //用户名和密码必须不为空
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return ResponseJsonResult.errorMsg("userName or password cannot be empty!");
        }
        if (platformChannel == null) {
            return ResponseJsonResult.errorMsg("Platform channel cannot be empty ！");
        }

//        if (platformChannel != 1 || platformChannel != 2) {
//            return ResponseJsonResult.errorMsg("Incorrect platform channel number ！");
//        }
        //实现登录
        ManagerUser managerUserResult = queryUserForLogin(userName, MD5Utils.getMD5Str(password), platformChannel);
        if (managerUserResult == null) {
            return ResponseJsonResult.errorMsg("Incorrect mobilePhone name or password!");
        }
        updateManagerUserStatus(managerUserResult.getUserId(), UserStatus.ON_LINE.id);

        ManagerUser showManagerUserInfo = showUserInfoAndGetUserToken(managerUserResult);

        return ResponseJsonResult.build(SUCCESS_CODE, "Login successful", showManagerUserInfo);
    }

    //注册登录后，展示用户信息并且获取用户token,将token加入redis 用于校验用户登录状况
    private ManagerUser showUserInfoAndGetUserToken(ManagerUser managerUserResult) {
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + managerUserResult.getUserId(), uniqueToken, 60 * 60 * 2);
        return managerUserResult;
    }

    //更新用户在状态
    @Transactional(propagation = Propagation.REQUIRED)
    public int updateManagerUserStatus(String userId, Integer userStatus) {
        ManagerUser managerUser = new ManagerUser();
        managerUser.setUserId(userId);
        managerUser.setUpdateTime(new Date());
        managerUser.setUserStatus(userStatus);
        return managerUserMapper.updateById(managerUser);
    }

    //登录
    @Transactional(propagation = Propagation.SUPPORTS)
    public ManagerUser queryUserForLogin(String userName, String password, Integer platformChannel) {
        QueryWrapper<ManagerUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName).eq("password", password).eq("platform_channel", platformChannel);
        return managerUserMapper.selectOne(queryWrapper);
    }

    //退出登录
    @Override
    public ResponseJsonResult signOut(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;

        //分布式会话中需要清除redis 中用户数据
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        //退出时，将用户状态改为离线状态
        updateManagerUserStatus(userId, UserStatus.OFF_LINE.id);
        return ResponseJsonResult.build(SUCCESS_CODE, "Logout successful", "");
    }

    //检查参数
    private ResponseJsonResult checkParams(String userId) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("The userId can not be empty");
        }
        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }
        return null;
    }

}
