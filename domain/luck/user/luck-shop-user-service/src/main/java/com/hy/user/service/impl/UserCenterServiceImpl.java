package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.constant.Constant;
import com.hy.enums.UserStatus;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.mapper.UserMapper;
import com.hy.user.mapper.center.AddressMapper;
import com.hy.user.model.User;
import com.hy.user.model.bo.UserBO;
import com.hy.user.model.bo.center.AddressBO;
import com.hy.user.model.center.Address;
import com.hy.user.service.IUserCenterService;
import com.hy.user.service.IUserService;
import com.hy.utils.CookieUtils;
import com.hy.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/29 14:28
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/29 14:28
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class UserCenterServiceImpl implements IUserCenterService {

    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public ResponseJsonResult setUserPassword(String userId, UserBO userBO) {
        String mobilePhone = userBO.getMobilePhone();
        String verificationCode = userBO.getVerificationCode();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        ResponseJsonResult x = checkParams("1", "1", "1", "1", "1", "1", "1", "1", mobilePhone, verificationCode,password,confirmPassword);

        if (x != null) return x;

        if(!password.equals(confirmPassword)){
            return ResponseJsonResult.errorMsg("The passwords you entered are inconsistent");
        }

        ResponseJsonResult responseJsonResult = iUserService.checkVerifcationCode(mobilePhone, verificationCode);
        if (!responseJsonResult.getStatus().equals(SUCCESS_CODE)) {
            return responseJsonResult;
        }

        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setUpdateTime(LocalDate.now());
        userMapper.updateById(user);
        return ResponseJsonResult.build(SUCCESS_CODE, "The login password was set successfully", "");
    }

    @Override
    public ResponseJsonResult setUserNicknames(String userId, String nicknames) {

        ResponseJsonResult x = checkParams(userId, nicknames, "1", "1", "1", "1", "1", "1", "1", "1","1","1");
        if (x != null) return x;

        User user = new User();
        user.setUserId(userId);
        user.setNicknames(nicknames);
        user.setUserName(nicknames);
        user.setUpdateTime(LocalDate.now());
        userMapper.updateById(user);
        return ResponseJsonResult.build(SUCCESS_CODE, "The user nickname was set successfully", "");
    }

    @Override
    public ResponseJsonResult signOut(String userId, HttpServletRequest request, HttpServletResponse response) {

        ResponseJsonResult x = checkParams(userId, "1", "1", "1", "1", "1", "1", "1", "1", "1","1","1");
        if (x != null) return x;

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
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        redisOperator.del(REDIS_USER_INFO + ":" + userId);
        //退出时，将用户状态改为离线状态
        iUserService.updateUserStatus(userId, UserStatus.OFF_LINE.id);
        return ResponseJsonResult.build(SUCCESS_CODE, "Logout successful", "");
    }

    @Override
    public ResponseJsonResult addUserAddressInfo(String userId, AddressBO addressBO) {

        String province = addressBO.getProvince();

        String city = addressBO.getCity();

        String district = addressBO.getDistrict();

        String streets = addressBO.getStreets();

        String detailedAddress = addressBO.getDetailedAddress();

        String realName = addressBO.getRealName();

        String mobilePhone = addressBO.getMobilePhone();

        String state = addressBO.getState();

        ResponseJsonResult x = checkParams(userId, "1", province, city, district, streets, detailedAddress, realName, mobilePhone, "1", "1", "1");
        if (x != null) return x;


        Address address = Address.builder()
                .province(province)
                .city(city)
                .district(district)
                .state(state)
                .streets(streets)
                .detailedAddress(detailedAddress)
                .realName(realName)
                .mobilePhone(mobilePhone)
                .userId(userId)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        addressMapper.insert(address);
        return ResponseJsonResult.build(SUCCESS_CODE, "Address added successfully", "");
    }


    @Override
    public ResponseJsonResult queryMyAddressList(String userId, HttpServletRequest request, HttpServletResponse response) {

        ResponseJsonResult x = checkParams(userId, "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1");
        if (x != null) return x;
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Address> addressList = addressMapper.selectList(queryWrapper);
        return ResponseJsonResult.ok(addressList);
    }

    private ResponseJsonResult checkParams(String userId, String nicknames, String province, String city, String district, String streets, String detailedAddress, String realName,
                                           String mobilePhone, String verificationCode, String password, String confirmPassword) {

        if (StringUtils.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("User userId cannot be empty!");
        }
        if (StringUtils.isBlank(nicknames)) {
            return ResponseJsonResult.errorMsg("User nicknames cannot be empty!");
        }

        //校验用户登录状态，查询Redis缓存
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE, "Login expired, please login again !", "");
        }
        if (StringUtils.isBlank(province)) {
            return ResponseJsonResult.errorMsg("province cannot be empty!");
        }
        if (StringUtils.isBlank(city)) {
            return ResponseJsonResult.errorMsg("city cannot be empty!");
        }
        if (StringUtils.isBlank(district)) {
            return ResponseJsonResult.errorMsg("district cannot be empty!");
        }
        if (StringUtils.isBlank(streets)) {
            return ResponseJsonResult.errorMsg("streets cannot be empty!");
        }
        if (StringUtils.isBlank(detailedAddress)) {
            return ResponseJsonResult.errorMsg("detailedAddress cannot be empty!");
        }
        if (StringUtils.isBlank(realName)) {
            return ResponseJsonResult.errorMsg("User userId cannot be empty!");
        }
        if (StringUtils.isBlank(mobilePhone)) {
            return ResponseJsonResult.errorMsg("mobilePhone cannot be empty!");
        }
        if (StringUtils.isBlank(mobilePhone)) {
            return ResponseJsonResult.errorMsg("mobilePhone cannot be empty!");
        }
        if (StringUtils.isBlank(verificationCode)) {
            return ResponseJsonResult.errorMsg("verificationCode cannot be empty!");
        }
        if (StringUtils.isBlank(password)) {
            return ResponseJsonResult.errorMsg("password cannot be empty!");
        }
        if (StringUtils.isBlank(confirmPassword)) {
            return ResponseJsonResult.errorMsg("confirmPassword cannot be empty!");
        }
        return null;
    }
}
