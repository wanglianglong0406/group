package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.PlatfromInfoMapper;
import com.hy.search.model.PlatfromInfo;
import com.hy.search.service.IPlatfromInfoService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hy.constant.Constant.PLATFROM_INFO;


/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/22 23:21
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/22 23:21
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class PlatformInfoServiceImpl implements IPlatfromInfoService {
    @Autowired
    private PlatfromInfoMapper platfromInfoMapper;
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public ResponseJsonResult queryPlatfromInfoList() {
        QueryWrapper<PlatfromInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_is_flag",1);
        List<PlatfromInfo> platfromInfoList;
        String platfromInfoList_redis = redisOperator.get(PLATFROM_INFO);
        if (StringUtils.isBlank(platfromInfoList_redis)) {
            platfromInfoList = platfromInfoMapper.selectList(queryWrapper);
            redisOperator.set(PLATFROM_INFO, JsonUtils.objectToJson(platfromInfoList));
        } else {
            platfromInfoList = JsonUtils.jsonToList(platfromInfoList_redis, PlatfromInfo.class);
        }
        return ResponseJsonResult.ok(platfromInfoList);
    }
}
