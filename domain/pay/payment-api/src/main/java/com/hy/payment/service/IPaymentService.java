package com.hy.payment.service;

import com.hy.payment.model.req.WithdrawalReq;
import com.hy.payment.model.res.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/17 15:11
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/17 15:11
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Api(value = "支付中心相关接口", tags = "支付中心相关接口")
@RequestMapping("payment-api/v1")
@FeignClient("payment-service")
public interface IPaymentService {

    //支付预计单SPI
    @ApiOperation(value = "支付预计单SPI", notes = "支付预计单SPI", httpMethod = "POST", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paymentName", value = "支付名称", dataType = "String", required = true),
            @ApiImplicitParam(name = "outTradeNo", value = "商户订单号", dataType = "String", required = true),
            @ApiImplicitParam(name = "totalFee", value = "交易金额", dataType = "String", required = true)
    })
    @PostMapping("/placeOrder")
    public PayInfoResponse placeOrder(@RequestParam("paymentName") String paymentName, @RequestParam("outTradeNo") String outTradeNo, @RequestParam("totalFee") String totalFee);

    //服务器通知
    @ApiOperation(value = "支付服务器通知SPI", notes = "支付服务器通知SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/serverNotify")
    public NoticeOfPaymentResultResponse serverNoticeOfPaymentResult(HttpServletRequest request, HttpServletRequest response);

    //页面通知
    @ApiOperation(value = "支付页面通知SPI", notes = "支付页面通知SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/pageCallback")
    public NoticeOfPaymentResultResponse pageCallBackOfPaymentResult(HttpServletRequest request, HttpServletRequest response);

    //查询交易信息
    @ApiOperation(value = "支付预计单SPI", notes = "支付预计单SPI", httpMethod = "POST", hidden = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paymentName", value = "支付名称", dataType = "String", required = true),
            @ApiImplicitParam(name = "outTradeNo", value = "商户订单号", dataType = "String", required = true),
            @ApiImplicitParam(name = "totalFee", value = "交易金额", dataType = "String", required = true)
    })
    @GetMapping("/queryTransactionInformation")
    public TradeInfoResponse queryTransactionInformation(String outTradeNo);

    //代付（提现）
    @ApiOperation(value = "代付（提现）SPI", notes = "代付（提现）SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/withdrawal")
    public WithdrawalResponse withdrawal(@RequestBody WithdrawalReq withdrawalReq);

    //代付（回调）
    @ApiOperation(value = "代付（回调）SPI", notes = "代付（回调）SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/callBackOfWithdrawalResult")
    public CallBackOfWithdrawalResponse callBackOfWithdrawalResult(HttpServletRequest request, HttpServletRequest response);

    //代付（查询）
    @ApiOperation(value = "代付（查询）SPI", notes = "代付（查询）SPI", httpMethod = "POST", hidden = true)
    @PostMapping("/queryWithdrawalTransactionInformation")
    public WithdrawalTradeInfoResponse queryWithdrawalTransactionInformation(String outTradeNo);


    //银行编码查询
    @ApiOperation(value = "银行编码查询 SPI", notes = "银行编码查询 SPI", httpMethod = "GET", hidden = true)
    @GetMapping("/queryBankCodeList")
    public BankCodeResponse queryBankCodeList(String currencyCode);
}
