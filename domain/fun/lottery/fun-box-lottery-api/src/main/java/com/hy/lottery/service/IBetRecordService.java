package com.hy.lottery.service;

import com.hy.lottery.model.BetRecord;
import com.hy.lottery.model.bo.BetRecordBO;
import com.hy.pojo.PagedGridResult;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (ParityRecord)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-23 15:35:57
 */
@Api(value = "投注记录相关API", tags = "投注记录相关API")
@RequestMapping("lottery-api/bet-record/v1")
@FeignClient("fun-box-lottery-service")
public interface IBetRecordService {

    //通过组合条件筛选投注记录的单笔流水
    @ApiOperation(value = "通过组合条件筛选投注记录的单笔流水信息API", notes = "通过组合条件筛选投注记录的单笔流水信息API", httpMethod = "GET")
    @GetMapping("betRecordDetail")
    public ResponseJsonResult queryBetRecordDetail(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId,
                                                   @RequestBody BetRecordBO betRecordBO);

    //获取订单token
    @ApiOperation(value = "获取订单token API", notes = "获取订单token API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/betRecordOrderToken")
    public ResponseJsonResult getBetRecordOrderToken(@RequestParam(value = "userId") String userId);

    //投注下单API
    @ApiOperation(value = "投注下单API", notes = "投注下单API", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "orderToken", value = "订单token", dataType = "String", required = true)
    })
    @PostMapping("betRecordOrder")
    public ResponseJsonResult placeBetRecordOrder(@RequestParam(value = "userId") String userId, @RequestParam(value = "orderToken") String orderToken,
                                                  @RequestBody List<BetRecordBO> betRecordBOList);

    //创建投注记录
    @ApiOperation(value = "创建投注记录SPI", notes = "创建投注记录SPI", httpMethod = "POST", hidden = true)
    @PostMapping("createRecord")
    public int createRecord(@ApiParam(value = "用户id", name = "userId", required = true) @RequestParam(value = "userId") String userId,
                            @ApiParam(value = "系统订单号", name = "orderId", required = true) @RequestParam(value = "orderId") Long orderId,
                            @ApiParam(value = "价格", name = "price", required = true) @RequestParam(value = "price") Long price,
                            @RequestBody BetRecordBO betRecordBO);

    //查询投注记录列表SPI
    @ApiOperation(value = "查询投注记录列表SPI", notes = "查询投注记录列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("betRecordInfos")
    public List<BetRecord> queryBetRecordInfos(@RequestParam(value = "period") String period, @RequestParam(value = "type") String type, @RequestParam("payStatus") Integer payStatus);

    //查询单条投注记录信息
    @ApiOperation(value = "查询投注记录", notes = "查询投注记录", httpMethod = "GET", hidden = true)
    @GetMapping("queryBetRecordInfoOne")
    public BetRecord queryBetRecordInfoOne(@RequestParam(value = "period") String period, @RequestParam(value = "type") String type,
                                           @RequestParam(value = "userId") String userId);

    //开奖结束同步更新BetRcordInfo SPI
    @ApiOperation(value = "开奖结束同步更新BetRcordInfo SPI", notes = "开奖结束同步更新BetRcordInfo SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "系统订单号", dataType = "Long", required = true),
            @ApiImplicitParam(name = "period", value = "期数", dataType = "String", required = true),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "isluckFlag", value = "是否幸运中奖 1 是  2 不是", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "result", value = "结果", dataType = "String"),
            @ApiImplicitParam(name = "odds", value = "赔率", dataType = "Double"),
            @ApiImplicitParam(name = "winOrLose", value = "输赢", dataType = "Double"),
            @ApiImplicitParam(name = "price", value = "价格", dataType = "Long")
    })
    @PutMapping("updateBetRecordInfo")
    public boolean updateBetRecordInfo(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "period") String period, @RequestParam(value = "type") String type,
                                       @RequestParam(value = "userId") String userId, @RequestParam(value = "isluckFlag") Integer isluckFlag,
                                       @RequestParam(value = "result") String result,
                                       @RequestParam(value = "odds") Double odds, @RequestParam(value = "winOrLose") Double winOrLose,
                                       @RequestParam(value = "price") Long price);


    //获取投注记录列表(带分页)
    @ApiOperation(value = "获取投注记录列表(带分页)API", notes = "获取投注记录列表(带分页)API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（PARITY   SAPRE  BCONE  EMEND）", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("betRecordInfoList")
    public PagedGridResult queryBetRecordInfoList(@RequestParam(value = "type") String type, @RequestParam(value = "userId") String userId, @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                                  @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestParam(value = "startDate", required = false) String startDate,
                                                  @RequestParam(value = "endDate", required = false) String endDate);

    //同步投注记录支付状态
    @ApiOperation(value = "开奖结束同步更新BetRcordInfo SPI", notes = "开奖结束同步更新BetRcordInfo SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderIds", value = "系统订单号，多个订单号，用逗号隔开", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @PutMapping("betRecordPayStatus")
    public ResponseJsonResult updateBetRecordPayStatus(@RequestParam(value = "orderId") Long orderId, @RequestParam(value = "userId") String userId);


    //统计投注记录总金额
    @ApiOperation(value = "投注总金额API", notes = "失去总金额API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（PARITY   SAPRE  BCONE  EMEND）", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("totalAmountOfBetting")
    public ResponseJsonResult totalAmountOfBetting(@RequestParam(value = "type") String type, @RequestParam(value = "userId") String userId, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);

    //统计投注记录失去金额
    @ApiOperation(value = "失去总金额API", notes = "失去总金额API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（PARITY   SAPRE  BCONE  EMEND）", dataType = "String", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("statisticsOfBetLoss")
    public ResponseJsonResult statisticsOfBetLoss(@RequestParam(value = "type") String type, @RequestParam(value = "userId") String userId, @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


}