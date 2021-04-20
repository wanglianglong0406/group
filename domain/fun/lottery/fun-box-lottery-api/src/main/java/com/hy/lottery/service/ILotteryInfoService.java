package com.hy.lottery.service;

import com.hy.lottery.model.LotteryInfo;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * (LotteryInfo)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-23 15:31:47
 */
@Api(value = "期货彩票相关API", tags = "期货彩票相关API")
@RequestMapping("lottery-api/v1")
@FeignClient("fun-box-lottery-service")
public interface ILotteryInfoService {
    //获取彩票列表信息，目前没有用到，后台websocket 推送
    @ApiOperation(value = "获取彩票信息列表API", notes = "获取彩票信息列表API", httpMethod = "GET")
    @GetMapping("lotterys")
    public ResponseJsonResult queryLotteryInfoList(@ApiParam(value = "期货类型（PARITY   SAPRE  BCONE  EMEND）", name = "type", type = "String", example = "SAPRE", required = true)
                                                   @RequestParam("type") String type);

    //websocket 实时推送
    @ApiOperation(value = "获取期货彩票开奖记录SPI", notes = "获取期货彩票开奖记录SPI", httpMethod = "GET", hidden = true)
    @GetMapping("lotteryInfos")
    public ResponseJsonResult queryLotteryInfos();

    //生产彩票信息
    @ApiOperation(value = "生产彩票信息", notes = "生产彩票信息", httpMethod = "GET", hidden = true)
    @GetMapping("createLottery")
    public void createLottery();

    //根据当前期数筛选彩票信息列表
    @ApiOperation(value = "根据期数查询彩票信息SPI", notes = "根据期数查询彩票信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("lotterInfoByPeriod")
    public List<LotteryInfo> queryLotterInfoByPeriod(@RequestParam("period") String period);

    //同步数据，修改彩票信息对应的相关结果数据
    @ApiOperation(value = "同步修改开奖信息SPI", notes = "同步修改开奖信息SPI", httpMethod = "GET", hidden = true)
    @PutMapping("updateLotteryInfo")
    public int updateLotteryInfo(@RequestParam("period") String period,
                                 @RequestParam("type") String type,
                                 @RequestParam("price") Long price,
                                 @RequestParam("result") String result,
                                 @RequestParam("number") Integer number,
                                 @RequestParam("openStatus") Integer openStatus);

    //同步彩票信息的结果
    @ApiOperation(value = "同步彩票信息的结果SPI", notes = "同步彩票信息的结果SPI", httpMethod = "GET", hidden = true)
    @PutMapping("lotteryResults")
    public boolean operationLotteryResults(@RequestParam("period") String period,
                                           @RequestParam("type") String type,
                                           @RequestParam("price") Long price,
                                           @RequestParam("number") Integer number);


    //获取彩票历史记录信息 已开奖
    @ApiOperation(value = "彩票历史记录(带分页) API", notes = "彩票历史记录(带分页) API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（PARITY   SAPRE  BCONE  EMEND）", dataType = "String", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("lotteryHistoryList")
    public PagedGridResult queryLotterInfoHistoryList(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate);
}