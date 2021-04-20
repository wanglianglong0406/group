package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.VipinfoMapper;
import com.hy.search.mapper.ViprateMapper;
import com.hy.search.model.Vipinfo;
import com.hy.search.model.Viprate;
import com.hy.search.service.IVipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/13 18:31
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/13 18:31
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class VipServiceImpl implements IVipService {

    @Autowired
    private ViprateMapper viprateMapper;
    @Autowired
    private VipinfoMapper vipinfoMapper;

    //查询VIP 信息列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryVipInfoList() {
        return ResponseJsonResult.ok(vipinfoMapper.selectList(null));
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Vipinfo queryVipInfo(Integer vipLevel) {
        QueryWrapper<Vipinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", vipLevel);
        return vipinfoMapper.selectOne(queryWrapper);
    }

    //通过VIP等级查询VIP详细信息
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryVipInfoDetailByVipLevel(Integer vipLevel) {
        QueryWrapper<Vipinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", vipLevel);
        return ResponseJsonResult.ok(vipinfoMapper.selectOne(queryWrapper));
    }

    //查询VIP特权列表
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryVipRateInfoList() {
        QueryWrapper<Viprate> queryWrapper = new QueryWrapper<>();
        return ResponseJsonResult.ok(viprateMapper.selectList(queryWrapper));
    }
}
