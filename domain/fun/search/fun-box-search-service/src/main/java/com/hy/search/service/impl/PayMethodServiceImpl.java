package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.search.mapper.PayMethodMapper;
import com.hy.search.model.PayMethod;
import com.hy.search.service.IPayMethodService;
import com.hy.utils.JsonUtils;
import com.hy.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hy.constant.Constant.PAY_METHOD;


/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/24 13:09
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/24 13:09
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class PayMethodServiceImpl implements IPayMethodService {
    @Autowired
    private PayMethodMapper payMethodMapper;
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public List<PayMethod> queryPayMethodInfoList() {
        QueryWrapper<PayMethod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable_is_flag",1);

        List<PayMethod> payMethodList;
        String payMethodList_redis = redisOperator.get(PAY_METHOD);
        if (StringUtils.isBlank(payMethodList_redis)) {
            payMethodList = payMethodMapper.selectList(queryWrapper);
            redisOperator.set(PAY_METHOD, JsonUtils.objectToJson(payMethodList));
        } else {
            payMethodList = JsonUtils.jsonToList(payMethodList_redis, PayMethod.class);
        }
        return payMethodList;
    }
}
