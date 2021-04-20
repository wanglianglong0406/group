package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公告信息表(Announcement)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-18 17:48:01
 */
@Api(value = "动态公告相关接口", tags = "动态公告相关接口")
@RequestMapping("search-api/announcement/v1")
@FeignClient("fun-box-search-service")
public interface IAnnouncementService {

    /**
     * 查询生效中的公告列表
     * @return
     */
    @ApiOperation(value = "首页动态公告列表API", notes = "首页动态公告相关API", httpMethod = "GET")
    @GetMapping("/announcements")
    public ResponseJsonResult queryAnnouncements();

}