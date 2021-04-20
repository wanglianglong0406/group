package com.hy.search.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页分类(Classification)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-18 13:37:51
 */
@Api(value = "首页分类列表接口", tags = "首页分类列表接口")
@RequestMapping("search-api/classification/v1")
@FeignClient("fun-box-search-service")
public interface IClassificationService {

    /**
     * 查询所有一级分类
     * @return
     */
    @ApiOperation(value = "获首页分类(一级分类)API", notes = "用于获取商品分类(一级分类)API", httpMethod = "GET")
    @GetMapping("/root")
    public ResponseJsonResult root();

    /**
     * 根据一级分类id查询分类信息
     * @param rootId
     * @return
     */
    @ApiOperation(value = "获取首页子分类API", notes = "获取首页子分类API", httpMethod = "GET")
    @GetMapping("/sub/{rootId}")
    public ResponseJsonResult sub(@ApiParam(name = "rootId", value = "一级分类id", required = true)
            @PathVariable Integer rootId);



}