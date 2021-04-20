package com.hy.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hy.pojo.ResponseJsonResult;
import com.hy.search.mapper.AnnouncementMapper;
import com.hy.search.model.Announcement;
import com.hy.search.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告信息表(Announcement)表服务实现类
 *
 * @author 寒夜
 * @since 2020-11-18 17:48:37
 */
@RestController
public class AnnouncementServiceImpl implements IAnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponseJsonResult queryAnnouncements() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return ResponseJsonResult.ok(announcementMapper.selectList(queryWrapper));
    }
}