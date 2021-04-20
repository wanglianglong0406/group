package com.hy.manager.service;

import com.hy.manager.model.bo.ItemsBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/30 13:14
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/30 13:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "管理平台-->商品管理中心相关接口", tags = "管理平台-->商品管理中心相关接口")
@RequestMapping("manager/items-api/v1")
@FeignClient("manager-service")
public interface ICommodityManagementCenterService {

    //查询商品信息列表
    @ApiOperation(value = "<幸运商店>查询商品信息列表API", notes = "<幸运商店>查询商品信息列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/queryItemsInfoList")
    public ResponseJsonResult queryItemsInfoList(@RequestParam(value = "userId") String userId,
                                                @RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);



    //编辑商品信息
    @ApiOperation(value = "<幸运商店>编辑商品信息API", notes = "<幸运商店>编辑商品信息API", httpMethod = "POST")
    @PostMapping("/editItemsInformation")
    public ResponseJsonResult editItemsInformation(@RequestBody ItemsBO itemsBO, @RequestParam("userId") String userId);

    //导入商品信息
    @ApiOperation(value = "<幸运商店>导入商品信息API", notes = "<幸运商店>导入商品信息API", httpMethod = "POST")
    @PostMapping("/importItemsCSV")
    public ResponseJsonResult importItemsCSV(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam("userId") String userId, MultipartFile file) throws IOException;

    //导入商品信息模板下载
    @ApiOperation(value = "<幸运商店>导入商品信息模板下载API", notes = "<幸运商店>导入商品信息模板下载API", httpMethod = "GET")
    @RequestMapping("/downloadedItemsCSVTemplate")
    public ResponseJsonResult downloadedItemsCSVTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException;

}
