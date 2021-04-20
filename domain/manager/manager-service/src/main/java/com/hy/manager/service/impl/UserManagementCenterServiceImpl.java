package com.hy.manager.service.impl;

import com.hy.manager.service.IUserManagementCenterService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.service.IUserService;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;
import static com.hy.constant.Constant.REDIS_USER_TOKEN;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 15:52
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 15:52
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class UserManagementCenterServiceImpl implements IUserManagementCenterService {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private IUserService iUserService;

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

    //查询用户列表（幸运商店）
    @Override
    public ResponseJsonResult queryUserInfoList(String userId, Integer pageNo, Integer pageSize, String startDate, String endDate) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return iUserService.getUserInfoPagedGridResult(pageNo, pageSize, startDate, endDate);
    }

}
