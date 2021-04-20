package com.hy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.LambdaUpdateChainWrapper;
import com.hy.constant.Constant;
import com.hy.pojo.ResponseJsonResult;
import com.hy.user.config.FileUpload;
import com.hy.user.mapper.FeedbackInfoMapper;
import com.hy.user.mapper.FeedbackInteractiveMapper;
import com.hy.user.mapper.FeedbackTypeMapper;
import com.hy.user.model.FeedbackInfo;
import com.hy.user.model.FeedbackInteractive;
import com.hy.user.model.FeedbackType;
import com.hy.user.model.User;
import com.hy.user.service.IFeedbackService;
import com.hy.user.service.IUserService;
import com.hy.utils.DateUtil;
import com.hy.utils.RedisOperator;
import jodd.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static com.hy.constant.Constant.LOGIN_EXPIRED_CODE;


/**
 * @Description: $- 反馈相关业务处理 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/18 21:36
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/18 21:36
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class FeedbackServiceImpl implements IFeedbackService {
    @Autowired
    private FeedbackTypeMapper feedbackTypeMapper;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private FeedbackInteractiveMapper feedbackInteractiveMapper;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private FileUpload fileUpload;
    @Autowired
    private RedisOperator redisOperator;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryFeedbackTypeInfo() {
        QueryWrapper<FeedbackType> queryWrapper = new QueryWrapper<>();
        return ResponseJsonResult.ok(feedbackTypeMapper.selectList(queryWrapper));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryMyFeedbackInfo(String userId) {
        ResponseJsonResult x = checkParams(userId, null, null, null, null);
        if (x != null) return x;
        QueryWrapper<FeedbackInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return ResponseJsonResult.ok(feedbackInfoMapper.selectList(queryWrapper));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryFeedbackInteractive(String userId, Long feedbackId) {
        ResponseJsonResult x = checkParams(userId, feedbackId, null, null, null);
        if (x != null) return x;
        QueryWrapper<FeedbackInteractive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("feedback_id", feedbackId);
        return ResponseJsonResult.ok(feedbackInteractiveMapper.selectList(queryWrapper));
    }

    @Override
    public ResponseJsonResult submitFeedbackInfo(String userId, String des, Integer typeId, MultipartFile[] images) {

        ResponseJsonResult x = checkParams(userId, null, null, des, typeId);
        if (x != null) return x;

        ResponseJsonResult responseJsonResult = iUserService.findUserInfo(userId);
        User user = (User) responseJsonResult.getData();
        String user_name = user.getUserName();

        FeedbackType feedbackType = feedbackTypeMapper.selectById(typeId);
        String name = feedbackType.getName();
        FeedbackInfo feedbackInfo = createFeedBackInfo(userId, des, typeId, name);
        Long feedbackId = feedbackInfo.getId();
        createFeedbackInteractive(userId, feedbackId, des, feedbackInfo, user_name, "2");
        //上传图片，并且保存相关数据
        uploadImage(userId, feedbackId, images);
        return ResponseJsonResult.ok();

    }


    //创建反馈信息
    @Transactional(propagation = Propagation.REQUIRED)
    FeedbackInfo createFeedBackInfo(String userId, String des, Integer typeId, String name) {
        FeedbackInfo feedbackInfo = FeedbackInfo.builder()
                .userId(userId)
                .des(des)
                .type(typeId)
                .name(name)
                .createTime(new Date())
                .updateTime(new Date())
                .isFlag(1)
                .build();
        feedbackInfoMapper.insert(feedbackInfo);
        return feedbackInfo;
    }


    //图片上传，并且保存图片路径地址
    private ResponseJsonResult uploadImage(String userId, Long feedbackId, MultipartFile[] images) {
        StringBuilder urls = new StringBuilder();
        // 开始文件上传
        if (images != null) {
            if (images.length > 3) {
                return ResponseJsonResult.errorMsg("The number of pictures cannot be greater than three");
            }
            FileOutputStream fileOutputStream = null;
            try {
                for (int i = 0; i < images.length; i++) {

                    String fileSpace = fileUpload.getImageFeedbackLocation();
                    // 在路径上为每一个用户增加一个userId，用于区分不同用户上传
                    String uploadPathPrefix = File.separator + userId + DateUtil.dateToStringyyyyMMdd(new Date());
                    // 获得文件上传的文件名称
                    String fileName = images[i].getOriginalFilename();

                    if (StringUtils.isNotBlank(fileName)) {

                        // 文件重命名  hy-face.png -> ["hy-face", "png"]
                        String[] fileNameArr = fileName.split("\\.");

                        // 获取文件的后缀名
                        String suffix = fileNameArr[fileNameArr.length - 1];

                        if (!suffix.equalsIgnoreCase("png") &&
                                !suffix.equalsIgnoreCase("jpg") &&
                                !suffix.equalsIgnoreCase("jpeg")) {
                            return ResponseJsonResult.errorMsg("The picture format is not correct");
                        }

                        // face-{userid}.png
                        // 文件名称重组 覆盖式上传，增量式：额外拼接当前时间
                        String newFileName = "feedback-img-" + userId + "." + suffix;

                        // 上传的图片最终保存的位置
                        String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                        // 用于提供给web服务访问的地址
                        uploadPathPrefix += ("\\" + newFileName);

                        File outFile = new File(finalFacePath);
                        if (outFile.getParentFile() != null) {
                            // 创建文件夹
                            outFile.getParentFile().mkdirs();
                        }

                        // 文件输出保存到目录
                        fileOutputStream = new FileOutputStream(outFile);
                        InputStream inputStream = images[i].getInputStream();
                        IOUtils.copy(inputStream, fileOutputStream);

                        // 获取图片服务地址
                        String imageServerUrl = fileUpload.getImageServerUrl();

                        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
                        String finalFeedbackUrl = imageServerUrl + uploadPathPrefix
                                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

                        urls.append(finalFeedbackUrl);
                        urls.append("#");//多个用|分割
                        urls.substring(0, urls.length() - 1);
                        //更新图片地址到数据库
                        boolean result = updateFeedbackInfo(userId, feedbackId, urls);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return ResponseJsonResult.ok();
    }

    //更新反馈信息
    @Transactional(propagation = Propagation.REQUIRED)
    boolean updateFeedbackInfo(String userId, Long feedbackId, StringBuilder urls) {
        boolean result = new LambdaUpdateChainWrapper<FeedbackInfo>(feedbackInfoMapper)
                .eq(FeedbackInfo::getUserId, userId)
                .eq(FeedbackInfo::getType, urls.toString())
                .eq(FeedbackInfo::getId, feedbackId)
                .set(FeedbackInfo::getImageUrl, urls.toString())
                .set(FeedbackInfo::getUpdateTime, new Date())
                .update();
        return result;
    }


    @Override
    public ResponseJsonResult leavingMessageFeedback(String userId, Long feedbackId, String content) {

        ResponseJsonResult x = checkParams(userId, feedbackId, content, null, null);
        if (x != null) return x;

        FeedbackInfo feedbackInfo = feedbackInfoMapper.selectById(feedbackId);
        ResponseJsonResult responseJsonResult = iUserService.findUserInfo(userId);
        User user = (User) responseJsonResult.getData();
        String user_name = user.getUserName();
        String handler = "";
        if (user.getUserLevel() == 1) {  //系统用户
            feedbackInfo.setUpdateTime(new Date());
            feedbackInfo.setIsFlag(2);
            feedbackInfo.setId(feedbackId);
            feedbackInfoMapper.updateById(feedbackInfo);
            handler = "1";

        }
        if (user.getUserLevel() == 2) {
            handler = "2";
            QueryWrapper<FeedbackInteractive> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId).eq("feedback_id", feedbackId);
            FeedbackInteractive feedbackInteractive = feedbackInteractiveMapper.selectOne(queryWrapper);
            if (null != feedbackInteractive) {
                if (!feedbackInteractive.getHandler().equals("1")) {
                    return ResponseJsonResult.errorMsg("You have submitted your message, please wait patiently");
                }
            }
        }

        createFeedbackInteractive(userId, feedbackId, content, feedbackInfo, user_name, handler);

        return ResponseJsonResult.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void createFeedbackInteractive(String userId, Long feedbackId, String content, FeedbackInfo feedbackInfo, String user_name, String handler) {
        FeedbackInteractive feedbackInteractive = FeedbackInteractive.builder()
                .userId(userId)
                .content(content)
                .feedbackId(feedbackId)
                .name(feedbackInfo.getName())
                .userName(user_name)
                .handler(handler)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        feedbackInteractiveMapper.insert(feedbackInteractive);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryOutstandingMessages(String userId) {

        ResponseJsonResult x = checkParams(userId, null, null, null, null);
        if (x != null) return x;

        ResponseJsonResult responseJsonResult = iUserService.findUserInfo(userId);
        User user = (User) responseJsonResult.getData();

        if (user.getUserLevel() == 1) {  //系统用户
            QueryWrapper<FeedbackInteractive> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("handler", 2);
            return ResponseJsonResult.ok(feedbackInteractiveMapper.selectOne(queryWrapper));
        }
        return ResponseJsonResult.ok();
    }


    private ResponseJsonResult checkParams(String userId, Long feedbackId, String content, String des, Integer typeId) {
        if (StringUtil.isBlank(String.valueOf(typeId))) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty!");
        }
        if (StringUtil.isBlank(content)) {
            return ResponseJsonResult.errorMsg("This content cannot be empty!");
        }

        if (StringUtil.isBlank(des)) {
            return ResponseJsonResult.errorMsg("This des cannot be empty!");
        }

        if (StringUtil.isBlank(String.valueOf(feedbackId))) {
            return ResponseJsonResult.errorMsg("This feedbackId cannot be empty!");
        }

        if (StringUtil.isBlank(userId)) {
            return ResponseJsonResult.errorMsg("This userId cannot be empty!");
        }

        String str = redisOperator.get(Constant.REDIS_USER_TOKEN + ":" + userId);
        if (str == null) {
            return ResponseJsonResult.build(LOGIN_EXPIRED_CODE,"Login expired, please login again !","");
        }
        return null;
    }
}
