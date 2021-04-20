package com.hy.center.service;

import com.hy.center.model.*;
import com.hy.center.model.bo.AnnouncementBO;
import com.hy.center.model.bo.FinancialRecordsBO;
import com.hy.center.model.bo.ItemsBiz;
import com.hy.center.model.bo.VipInfoBO;
import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/30 15:11
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/30 15:11
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "通用接口", tags = "通用接口")
@RequestMapping("luckshop/center-stage-api/v1")
@FeignClient("luck-shop-center-stage-service")
public interface ICenterStageService {

    //添加商品信息
    @ApiOperation(value = "添加商品信息SPI", notes = "添加商品信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/addItemsInformation")
    public void addItemsInformation(@RequestBody ItemsBiz itemsBiz);

    //获取商品信息列表
    @ApiOperation(value = "获取商品信息列表SPI", notes = "获取商品信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getItemsInfoList")
    public List<ItemsBiz> getItemsInfoList();

    //查询用户列表
    @ApiOperation(value = "获取商品信息列表(分页)SPI", notes = "获取商品信息列表（分页）SPI", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "当前页数", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页的数量", dataType = "Integer"),
            @ApiImplicitParam(name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(name = "endDate", value = "结束日期", dataType = "String")
    })
    @GetMapping("/getItemsInfoPagedGridResult")
    public ResponseJsonResult getItemsInfoPagedGridResult(@RequestParam(value = "pageNo", required = false) Integer pageNo, @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                          @RequestParam(value = "startDate", required = false) String startDate, @RequestParam(value = "endDate", required = false) String endDate);


    //编辑商品信息
    @ApiOperation(value = "编辑商品信息SPI", notes = "编辑商品信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/editItemsInformationByItemsId")
    public void editItemsInformationByItemsId(@RequestBody ItemsBiz itemsBiz);

    //通过商品ID查询商品信息SPI
    @ApiOperation(value = "通过商品ID查询商品信息SPI", notes = "通过商品ID查询商品信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getItemsInfoByItemsId")
    public Items getItemsInfoByItemsId(@RequestParam("itemsId") Long itemsId);

    //获取每日任务商品列表SPI
    @ApiOperation(value = "获取每日商品列表SPI", notes = "获取每日商品列表SPI", httpMethod = "GET", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "numberOfTasks", value = "任务数量", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/getItemsList")
    public List<ItemsBiz> getItemsList(@RequestParam("userId") String userId, @RequestParam("numberOfTasks") Integer numberOfTasks);


    //查询会员信息列表
    @ApiOperation(value = "查询会员信息列表API", notes = "查询会员信息列表API", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/queryVipInfoList")
    public ResponseJsonResult queryVipInfoList(@RequestParam("userId") String userId);

    //查询会员信息列表
    @ApiOperation(value = "查询会员信息列表SPI", notes = "查询会员信息列表SPI", httpMethod = "GET", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true)
    })
    @GetMapping("/getVipInfos")
    public List<VipInfo> getVipInfos(@RequestParam("userId") String userId);

    //保存VIP信息SPI
    @ApiOperation(value = "保存VIP信息SPI", notes = "保存VIP信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/saveVipInfo")
    public void saveVipInfo(@RequestBody VipInfoBO vipInfoBO);

    //修改VIP信息SPI
    @ApiOperation(value = "修改VIP信息SPI", notes = "修改VIP信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/editVipInfo")
    public void editVipInfo(@RequestBody VipInfoBO vipInfoBO);

    //通过VIP等级查询vip信息SPI
    @ApiOperation(value = "通过VIP等级查询vip信息SPI", notes = "通过VIP等级查询vip信息SPI", httpMethod = "GET", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "vipLevel", value = "vip等级", dataType = "Integer", required = true)
    })
    @GetMapping("/getVipInfosByVipLevel")
    public VipInfo getVipInfosByVipLevel(@RequestParam("vipLevel") Integer vipLevel);

    //查询价格信息列表
    @ApiOperation(value = "查询价格信息列表API", notes = "查询价格信息列表API", httpMethod = "GET")
    @GetMapping("/priceInfoList")
    public ResponseJsonResult queryPriceInfoList(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //获取价格信息列表
    @ApiOperation(value = "获取价格信息列表SPI", notes = "获取价格信息列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getPriceInfoList")
    public List<PriceInfo> getPriceInfoList();

    //新增价格信息
    @ApiOperation(value = "新增价格信息SPI", notes = "新增价格信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/savePriceInfo")
    public void savePriceInfo(@RequestParam("price") Double price);

    //修改价格信息
    @ApiOperation(value = "修改价格信息SPI", notes = "修改价格信息SPI", httpMethod = "POST", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "价格信息ID", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "price", value = "价格", dataType = "Double", required = true)
    })
    @PostMapping("/editPriceInfo")
    public void editPriceInfo(@RequestParam("id") Integer id, @RequestParam("price") Double price);

    //查询金融产品信息列表
    @ApiOperation(value = "查询金融产品信息列表API", notes = "查询金融产品信息列表API", httpMethod = "GET")
    @GetMapping("/financialProductsList")
    public ResponseJsonResult queryFinancialProductsList(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);


    //查询金融产品信息列表
    @ApiOperation(value = "查询金融产品信息列表API", notes = "查询金融产品信息列表API", httpMethod = "GET")
    @GetMapping("/getFinancialProductsList")
    public List<FinancialProducts> getFinancialProductsList();

    //新增理财产品信息SPI
    @ApiOperation(value = "新增理财产品信息SPI", notes = "新增理财产品信息SPI", httpMethod = "POST", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cycle", value = "周期", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "interest", value = "利息", dataType = "Double", required = true),
            @ApiImplicitParam(name = "totalRevenue", value = "总收入", dataType = "Double", required = true)
    })
    @PostMapping("/saveFinancialProductsInfo")
    public void saveFinancialProductsInfo(@RequestParam("cycle") Integer cycle,
                                          @RequestParam("interest") Double interest,
                                          @RequestParam("totalRevenue") Double totalRevenue);


    //修改理财产品信息SPI
    @ApiOperation(value = "修改理财产品信息SPI", notes = "修改理财产品信息SPI", httpMethod = "POST", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "cycle", value = "周期", dataType = "Integer", required = true),
            @ApiImplicitParam(name = "interest", value = "利息", dataType = "Double", required = true),
            @ApiImplicitParam(name = "totalRevenue", value = "总收入", dataType = "Double", required = true)
    })
    @PostMapping("/editFinancialProductsInfo")
    public void editFinancialProductsInfo(@RequestParam("id") Integer id,
                                          @RequestParam("cycle") Integer cycle,
                                          @RequestParam("interest") Double interest,
                                          @RequestParam("totalRevenue") Double totalRevenue);


    //查询用户购买的理财产品列表
    @ApiOperation(value = "查询用户购买的理财产品列表SIP", notes = "查询用户购买的理财产品列表SIP", httpMethod = "GET", hidden = true)
    @GetMapping("/getFinancialRecordsList")
    public List<FinancialRecords> getFinancialRecordsList(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //我的理财记录列表
    @ApiOperation(value = "我的理财记录列表API", notes = "我的理财记录列表API", httpMethod = "GET")
    @GetMapping("/myFinancialRecordsList")
    public ResponseJsonResult myFinancialRecordsList(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);

    //通过产品ID查询用户购买的产品信息
    @ApiOperation(value = "通过产品ID查询用户购买的产品信息SPI", notes = "通过产品ID查询用户购买的产品信息SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getFinancialRecords")
    public FinancialRecords getFinancialRecords(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId,
                                                @RequestParam("productId") @ApiParam(name = "productId", value = "金融产品id", required = true) Integer productId);

    //创建购买金融产品记录
    @ApiOperation(value = "创建购买金融产品记录SPI", notes = "创建购买金融产品记录SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/createFinancialRecordsInfo")
    public FinancialRecords createFinancialRecordsInfo(@RequestBody FinancialRecordsBO financialRecordsBO);


    //同步更新理财记录信息
    @ApiOperation(value = "同步更新理财记录信息SPI", notes = "同步更新理财记录信息SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "Integer"),
            @ApiImplicitParam(name = "amount", value = "金额", dataType = "Double")
    })
    @PutMapping("updateFinancialRecordsInfo")
    public ResponseJsonResult updateFinancialRecordsInfo(@RequestParam("userId") String userId, @RequestParam("productId") Integer productId, @RequestParam("amount") Double amount);

    //计算日息
    @ApiOperation(value = "计算日息SPI", notes = "计算日息SPI", httpMethod = "PUT", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "productId", value = "产品ID", dataType = "Integer")
    })
    @PutMapping("calculateDailyInterest")
    public FinancialRecords calculateDailyInterest(@RequestParam("userId") String userId, @RequestParam("productId") Integer productId);


    //统计用户理财收益信息
    @ApiOperation(value = "统计用户理财收益信息API", notes = "统计用户理财收益信息API", httpMethod = "GET")
    @GetMapping("/totalFinancialEarningsInfo")
    public ResponseJsonResult totalFinancialEarningsInfo(@RequestParam("userId") @ApiParam(name = "userId", value = "用户id", required = true) String userId);


    //获取公告列表
    @ApiOperation(value = "获取公告列表SPI", notes = "获取公告列表SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/getAnnouncementInfoList")
    public List<Announcement> getAnnouncementInfoList();

    //通过公告id获取公告信息
    @ApiOperation(value = "通过公告id获取公告信息", notes = "通过公告id获取公告信息", httpMethod = "GET", hidden = true)
    @GetMapping("/getAnnouncementInfoById")
    public Announcement getAnnouncementInfoById(@RequestParam("id") String id);

    //获取公告列表
    @ApiOperation(value = "获取公告列表API", notes = "获取公告列表API", httpMethod = "GET")
    @GetMapping("/queryAnnouncementInfoList")
    public ResponseJsonResult queryAnnouncementInfoList();

    @ApiOperation(value = "添加公告信息SPI", notes = "添加公告信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/addAnnouncementInfo")
    public void addAnnouncementInfo(@RequestBody AnnouncementBO announcementBO);

    @ApiOperation(value = "编辑公告信息SPI", notes = "编辑公告信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/editAnnouncementInfo")
    public void editAnnouncementInfo(@RequestBody AnnouncementBO announcementBO);

    @ApiOperation(value = "删除公告信息SPI", notes = "删除公告信息SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/deleteAnnouncementInfo")
    public void deleteAnnouncementInfo(@ApiParam(name = "id", value = "公告ID", required = true) @RequestParam("id") String id);

}
