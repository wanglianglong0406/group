package com.hy.user.service;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/18 21:35
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/18 21:35
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */

@Api(value = "用户反馈相关接口", tags = "用户反馈相关接口")
@RequestMapping("user-api/feedback/v1")
@FeignClient("fun-box-user-service")
public interface IFeedbackService {
    //查询反馈类型信息列表
    @ApiOperation(value = "查询反馈类型信息列表API", notes = "查询反馈类型信息列表API", httpMethod = "GET")
    @GetMapping("/feedbackTypeInfo")
    public ResponseJsonResult queryFeedbackTypeInfo();


    //查询我的反馈信息列表
    @ApiOperation(value = "查询我的反馈信息列表API", notes = "查询我的反馈信息列表API", httpMethod = "GET")
    @GetMapping("/myFeedbackInfo")
    public ResponseJsonResult queryMyFeedbackInfo(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId);


    //查询反馈互动信息
    @ApiOperation(value = "查询反馈互动信息API", notes = "查询反馈互动信息API", httpMethod = "GET")
    @GetMapping("/queryFeedbackInteractive")
    public ResponseJsonResult queryFeedbackInteractive(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId,
                                                       @ApiParam(value = "反馈消息ID", name = "feedbackId", required = true) @RequestParam(value = "feedbackId") Long feedbackId);


    //提交反馈信息
    @ApiOperation(value = "反馈信息提交API", notes = "反馈信息提交API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "typeId", value = "typeId", dataType = "Long", required = true),
            @ApiImplicitParam(name = "des", value = "反馈消息描述", dataType = "String"),
            @ApiImplicitParam(name = "images", value = "上传图片", dataType = "list")
    })
    @PostMapping("/submitFeedbackInfo")
    public ResponseJsonResult submitFeedbackInfo(@RequestParam(value = "userId") String userId, @RequestParam(value = "des") String des,
                                                 @RequestParam(value = "typeId") Integer typeId, MultipartFile[] images);


    //反馈留言互动API
    @ApiOperation(value = "反馈留言互动API", notes = "反馈留言互动API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "feedbackId", value = "反馈消息ID", dataType = "Long", required = true),
            @ApiImplicitParam(name = "content", value = "内容", dataType = "String")
    })
    @PostMapping("/leavingMessageFeedback")
    public ResponseJsonResult leavingMessageFeedback(@RequestParam(value = "userId") String userId, @RequestParam(value = "feedbackId") Long feedbackId, @RequestParam(value = "content") String content);


    //查询未处理的留言
    @ApiOperation(value = "[管理系统]-查询未处理的反馈留言信息API", notes = "[管理系统]-查询未处理的反馈留言信息API", httpMethod = "GET")
    @GetMapping("/queryOutstandingMessages")
    public ResponseJsonResult queryOutstandingMessages(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId);


}