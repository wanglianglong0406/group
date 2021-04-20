package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.AmountInfoMapper;
import com.hy.search.model.AmountInfo;
import com.hy.search.service.IAmountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/17 15:34
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/17 15:34
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class AmountInfoServiceImpl implements IAmountInfoService {
    @Autowired
    private AmountInfoMapper amountInfoMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryPriceInfoList() {
        QueryWrapper<AmountInfo> queryWrapper = new QueryWrapper<>();
        return ResponseJsonResult.ok(amountInfoMapper.selectList(queryWrapper));
    }
}
