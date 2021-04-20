package com.hy.manager.service.impl;

import com.hy.center.model.Announcement;
import com.hy.center.service.ICenterStageService;
import com.hy.manager.mapper.ManagerUserMapper;
import com.hy.manager.model.ManagerUser;
import com.hy.manager.model.bo.AnnouncementBO;
import com.hy.manager.model.bo.VipInfoBO;
import com.hy.manager.service.ISystemManagementService;
import com.hy.pojo.ResponseJsonResult;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/2/4 11:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/2/4 11:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@RestController
public class SystemManagementServiceImpl implements ISystemManagementService {
    @Autowired
    private ManagerUserMapper managerUserMapper;
    @Autowired
    private ICenterStageService iCenterStageService;
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public ResponseJsonResult setPassword(String userId, String password, String confirmPassword) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;

        if (StringUtil.isBlank(password)) {
            return ResponseJsonResult.errorMsg("The password can not be empty");
        }

        if (StringUtil.isBlank(confirmPassword)) {
            return ResponseJsonResult.errorMsg("The confirmPassword can not be empty");
        }

        if (!password.equals(confirmPassword)) {
            return ResponseJsonResult.errorMsg("The passwords you entered are inconsistent");
        }
        ManagerUser managerUser = new ManagerUser();
        managerUser.setUserId(userId);
        managerUser.setPassword(password);
        managerUser.setUpdateTime(new Date());
        managerUserMapper.updateById(managerUser);
        return ResponseJsonResult.build(SUCCESS_CODE, "The login password was set successfully", "");
    }

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryVipInfoList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(iCenterStageService.getVipInfos(userId));
    }

    @Override
    public ResponseJsonResult saveVipInfo(String userId, VipInfoBO vipInfoBO) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        com.hy.center.model.bo.VipInfoBO vb = com.hy.center.model.bo.VipInfoBO.builder()
                .assets(vipInfoBO.getAssets())
                .commissionRate(vipInfoBO.getCommissionRate())
                .monthlyWithdrawalLimit(vipInfoBO.getMonthlyWithdrawalLimit())
                .name(vipInfoBO.getName())
                .numberOfDailyWithdrawals(vipInfoBO.getNumberOfDailyWithdrawals())
                .numberOfTasks(vipInfoBO.getNumberOfTasks())
                .vipLevel(vipInfoBO.getVipLevel())
                .build();
        iCenterStageService.saveVipInfo(vb);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult editVipInfo(String userId, VipInfoBO vipInfoBO) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        if (vipInfoBO.getVipLevel() == null) {
            return ResponseJsonResult.errorMsg("The VipLevel can not be empty");
        }

        com.hy.center.model.bo.VipInfoBO vb = com.hy.center.model.bo.VipInfoBO.builder()
                .assets(vipInfoBO.getAssets())
                .commissionRate(vipInfoBO.getCommissionRate())
                .monthlyWithdrawalLimit(vipInfoBO.getMonthlyWithdrawalLimit())
                .name(vipInfoBO.getName())
                .numberOfDailyWithdrawals(vipInfoBO.getNumberOfDailyWithdrawals())
                .numberOfTasks(vipInfoBO.getNumberOfTasks())
                .vipLevel(vipInfoBO.getVipLevel())
                .build();
        iCenterStageService.editVipInfo(vb);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult queryPriceInfoList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(iCenterStageService.getPriceInfoList());
    }


    @Override
    public ResponseJsonResult addPriceInfo(String userId, Double price) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        iCenterStageService.savePriceInfo(price);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult editPriceInfo(String userId, Integer id, Double price) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        if (id == null) {
            return ResponseJsonResult.errorMsg("The id can not be empty");
        }

        iCenterStageService.editPriceInfo(id, price);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult queryFinancialProductsList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(iCenterStageService.getFinancialProductsList());
    }

    @Override
    public ResponseJsonResult saveFinancialProductsInfo(String userId, Integer cycle, Double interest, Double totalRevenue) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        iCenterStageService.saveFinancialProductsInfo(cycle, interest, totalRevenue);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult editFinancialProductsInfo(String userId, Integer id, Integer cycle, Double interest, Double totalRevenue) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        if (id == null) {
            return ResponseJsonResult.errorMsg("The id can not be empty");
        }
        iCenterStageService.editFinancialProductsInfo(id, cycle, interest, totalRevenue);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult queryAnnouncementInfoList(String userId) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        return ResponseJsonResult.ok(iCenterStageService.getAnnouncementInfoList());
    }

    @Override
    public ResponseJsonResult addAnnouncementInfo(String userId, AnnouncementBO announcementBO) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        com.hy.center.model.bo.AnnouncementBO ab = com.hy.center.model.bo.AnnouncementBO.builder()
                .content(announcementBO.getContent())
                .popUp(announcementBO.getPopUp())
                .status(announcementBO.getStatus())
                .title(announcementBO.getTitle())
                .type(announcementBO.getType())
                .build();
        iCenterStageService.addAnnouncementInfo(ab);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult editAnnouncementInfo(String userId, AnnouncementBO announcementBO) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        String id = announcementBO.getId();
        if (StringUtil.isBlank(id)) {
            return ResponseJsonResult.errorMsg("Announcement ID cannot be empty!");
        }
        Announcement announcement = iCenterStageService.getAnnouncementInfoById(id);
        if (announcement == null) {
            return ResponseJsonResult.errorMsg("Announcement ID does not exist!");
        }
        com.hy.center.model.bo.AnnouncementBO ab = com.hy.center.model.bo.AnnouncementBO.builder()
                .id(id)
                .content(announcementBO.getContent() != null ? announcementBO.getContent() : announcement.getContent())
                .popUp(announcementBO.getPopUp() != null ? announcementBO.getPopUp() : announcement.getPopUp())
                .status(announcementBO.getStatus() != null ? announcementBO.getStatus() : announcement.getStatus())
                .title(announcementBO.getTitle() != null ? announcementBO.getTitle() : announcement.getTitle())
                .type(announcementBO.getType() != null ? announcementBO.getType() : announcement.getType())
                .build();
        iCenterStageService.editAnnouncementInfo(ab);
        return ResponseJsonResult.ok();
    }

    @Override
    public ResponseJsonResult deleteAnnouncementInfo(String userId, String id) {
        ResponseJsonResult x = checkParams(userId);
        if (x != null) return x;
        if (StringUtil.isBlank(id)) {
            return ResponseJsonResult.errorMsg("Announcement ID cannot be empty");
        }
        iCenterStageService.deleteAnnouncementInfo(id);
        return ResponseJsonResult.ok();
    }
}
