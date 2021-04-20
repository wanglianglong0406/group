package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 轮播图(Carousel)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-18 13:37:23
 */
@Api(value = "首页轮播图相关接口", tags = "首页轮播图相关接口")
@RequestMapping("search-api/carousel/v1")
@FeignClient("fun-box-search-service")
public interface ICarouselService {

    /**
     * 查询轮播图列表
     * @return
     */
    @ApiOperation(value = "首页轮播图列表", notes = "首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ResponseJsonResult carousel();

}