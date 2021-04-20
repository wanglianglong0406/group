package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:23
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Api(value = "通知信息相关接口", tags = "通知信息相关接口")
@RequestMapping("search-api/notification/v1")
@FeignClient("fun-box-search-service")
public interface INotificationService {


    //通知列表
    @ApiOperation(value = "通知信息列表API", notes = "通知信息列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1 匹配  2 活动 3 系统通知 4 我的通知）", dataType = "Integer")
    })
    @GetMapping("/notificationInfoList")
    public ResponseJsonResult queryNotificationInfoList(@RequestParam(value = "type") Integer type, @RequestParam(value = "userId") String userId);

    //添加通知
    @ApiOperation(value = "添加通知信息SPI", notes = "添加通知信息SPI", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型（1 匹配  2 活动 3 系统通知 4 我的通知）", dataType = "Integer"),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "String")
    })
    @PostMapping("/addToNotificationInfoByType")
    public Integer addToNotificationInfoByType(@RequestParam(value = "userId") String userId, @RequestParam(value = "title") String title,
                                               @RequestParam(value = "type") Integer type, @RequestParam(value = "content") String content);

    //读取通知
    @ApiOperation(value = "读取通知API", notes = "读取通知API", httpMethod = "POST")
    @PostMapping("/readNotification")
    public ResponseJsonResult toReadNotification(@ApiParam(value = "用户ID", type = "String", name = "userId", required = true) @RequestParam(value = "userId") String userId,
                                                 @ApiParam(value = "主键ID，通知消息ID", type = "Long", name = "id", required = true) @RequestParam(value = "id") Long id);

    //清空通知信息列表
    @ApiOperation(value = "清空通知信息列表API", notes = "清空通知信息列表API", httpMethod = "POST")
    @PostMapping("/clearNotification")
    public ResponseJsonResult toClearNotification(@ApiParam(value = "用户ID", type = "String", name = "userId", required = true) @RequestParam(value = "userId") String userId);


    @ApiOperation(value = "删除一条通知信息API", notes = "删除一条通知信息API", httpMethod = "POST")
    @PostMapping("/deleteNotification")
    public ResponseJsonResult toDeleteNotification(
            @ApiParam(value = "用户ID", type = "String", name = "userId", required = true) @RequestParam(value = "userId") String userId,
            @ApiParam(value = "主键ID，通知消息ID", type = "Long", name = "id", required = true) @RequestParam(value = "id") Long id);

}
