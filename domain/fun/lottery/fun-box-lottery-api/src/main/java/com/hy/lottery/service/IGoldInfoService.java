package com.hy.lottery.service;

import com.hy.lottery.model.GoldInfo;
import com.hy.lottery.model.bo.GoldInfoBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/24 17:31
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/24 17:31
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "黄金信息相关API", tags = "黄金信息相关API")
@RequestMapping("lottery-api/gold-info/v1")
@FeignClient("fun-box-lottery-service")
public interface IGoldInfoService {

    @ApiOperation(value = "获取黄金价格信息列表SPI", notes = "获取黄金价格信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("goldInfoList")
    public List<GoldInfo> queryGoldInfoList();


    @ApiOperation(value = "根据类型获取黄金价格信息列表SPI", notes = "根据类型获取黄金价格信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("queryGoldInfoByType")
    public GoldInfo queryGoldInfoByType(@ApiParam(value = "类型（PARITY   SAPRE  BCONE  EMEND）", name = "type", required = true)
                                        @RequestParam("type") String type);

    @ApiOperation(value = "修改黄金信息表相关信息（人工干预抽奖结果）API", notes = "修改黄金信息表相关信息（人工干预抽奖结果接口）API", httpMethod = "PUT")
    @PutMapping("updateGoldInfoByType")
    public ResponseJsonResult updateGoldInfoByType(@ApiParam(value = "类型（PARITY   SAPRE  BCONE  EMEND）", name = "type", required = true)
                                                   @RequestParam("type") String type,
                                                   @ApiParam(value = "价格", name = "price", required = true)
                                                   @RequestParam("price") Long price);

    @ApiOperation(value = "新增一个产品API", notes = "新增一个产品API", httpMethod = "POST")
    @PostMapping("saveGoldInfo")
    public ResponseJsonResult saveGoldInfo(@RequestBody GoldInfoBO goldInfoBO);


}
