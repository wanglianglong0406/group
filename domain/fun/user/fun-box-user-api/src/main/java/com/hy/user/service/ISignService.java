package com.hy.user.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 19:25
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 19:25
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "签到相关接口", tags = "签到相关接口")
@RequestMapping("user-api/sign/v1")
@FeignClient("fun-box-user-service")
public interface ISignService {
    @ApiOperation(value = "用于签到", notes = "用于签到", httpMethod = "POST")
    @PostMapping("sign")
    public ResponseJsonResult signIn(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam("userId") String userId,
                                     @ApiParam(value = "weekDay", name = "weekDay", required = true, example = "1,2,3,4,5,6,7") @RequestParam("weekDay") String weekDay);

    //重置签到奖励
    @ApiOperation(value = "重置签到记录", notes = "重置签到记录", httpMethod = "PUT", hidden = true)
    @PutMapping("resetCheckInStatistics")
    public void resetCheckInStatistics();
}
