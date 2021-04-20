package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.constant.Constant;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.NotificationMapper;
import com.hy.search.model.Notification;
import com.hy.search.service.INotificationService;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;


/**
 * @Description: $- 通知相关业务处理 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:32
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:32
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class NotificationServiceImpl implements INotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private RedisOperator redisOperator;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryNotificationInfoList(Integer type, String userId) {
        ResponseJsonResult x = checkParams(userId, null, type);
        if (x != null) return x;
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type).eq("is_delete", 1).eq("user_id", userId);
        return ResponseJsonResult.ok(notificationMapper.selectList(queryWrapper));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer addToNotificationInfoByType(String userId, String title, Integer type, String content) {
        Notification notification = Notification.builder()
                .userId(userId)
                .content(content)
                .title(title)
                .isRead(1)
                .isDelete(1)
                .type(type)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        return notificationMapper.insert(notification);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult toReadNotification(String userId, Long id) {

        ResponseJsonResult x = checkParams(userId, id, null);
        if (x != null) return x;

        Notification notification = Notification.builder()
                .id(id)
                .userId(userId)
                .isRead(2)
                .updateTime(new Date())
                .build();
        notificationMapper.updateById(notification);
        return ResponseJsonResult.ok();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult toClearNotification(String userId) {
        ResponseJsonResult x = checkParams(userId, null, null);
        if (x != null) return x;
        boolean result = new LambdaUpdateChainWrapper<Notification>(notificationMapper)
                .eq(Notification::getUserId, userId)
                .set(Notification::getIsDelete, 2)
                .set(Notification::getUpdateTime, new Date())
                .update();
        return ResponseJsonResult.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult toDeleteNotification(String userId, Long id) {
        ResponseJsonResult x = checkParams(userId, id, null);
        if (x != null) return x;
        Notification notification = Notification.builder()
                .id(id)
                .userId(userId)
                .isDelete(2)
                .updateTime(new Date())
                .build();
        notificationMapper.updateById(notification);
        return ResponseJsonResult.ok();
    }


    private ResponseJsonResult checkParams(String userId, Long id, Integer type) {
        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty!");
        }
        if (StringUtil.isBlank(String.valueOf(id))) {
            return ResponseJsonResult.errorMsg("This id cannot be empty!");
        }
        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        if (StringUtil.isBlank(String.valueOf(type))) {
            return ResponseJsonResult.errorMsg("This type cannot be empty!");
        }
        return null;
    }
}
