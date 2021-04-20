package com.hy.payment.service.impl;

import com.hy.payment.model.req.ExtendData;
import com.hy.payment.model.req.WithdrawalReq;
import com.hy.payment.model.res.*;
import com.hy.payment.resource.PayResource;
import com.hy.payment.service.IPaymentService;
import com.hy.utils.DateUtil;
import com.hy.utils.JsonUtils;
import com.hy.utils.pay.HttpUtil;
import com.hy.utils.pay.Sign;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.hy.constant.Constant.*;

/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/16 10:40
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/16 10:40
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class PaymentService implements IPaymentService {

    @Resource
    private PayResource payResource;

    //支付预下单
    @Override
    public PayInfoResponse placeOrder(String paymentName, String outTradeNo, String totalFee) {

        //log.info("=================进入支付中心，开始调用预下单接口================请求参数: 支付名称 ： {}，商户订单号： {}，交易金额： {}", paymentName, outTradeNo, totalFee);
        String pay_applydate = DateUtil.dateToStringWithTime(new Date());

        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("pay_memberid", payResource.getPayMemberid());
        p.put("pay_orderid", outTradeNo);
        p.put("pay_applydate", pay_applydate);
        p.put("pay_bankcode", payResource.getPayBankCode());
        p.put("pay_notifyurl", payResource.getPayNotifyUrl());
        p.put("pay_callbackurl", payResource.getPayCallbackUrl());
        p.put("pay_amount", totalFee);
        // 获得签名
        String sign = Sign.createSign("utf-8", p, payResource.getKey()).toUpperCase();
        //支付接口请求参数组装
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pay_memberid", payResource.getPayMemberid());
        map.add("pay_orderid", outTradeNo);
        map.add("pay_applydate", pay_applydate);
        map.add("pay_bankcode", payResource.getPayBankCode());
        map.add("pay_notifyurl", payResource.getPayNotifyUrl());
        map.add("pay_callbackurl", payResource.getPayCallbackUrl());
        map.add("pay_amount", totalFee);
        map.add("pay_md5sign", sign);
        map.add("pay_productname", paymentName);
        //发起支付请求
        String rechargeResultJsonStr = HttpUtil.sendPost(payResource.getPaymentUrl(), map);

        //处理支付返回信息
        RechargeResponse rechargeResponse = JsonUtils.jsonToPojo(rechargeResultJsonStr, RechargeResponse.class);
        assert rechargeResponse != null;
        if (rechargeResponse.getStatus().equals(RES_SUCCESS_CODE)) {
            return PayInfoResponse.builder()
                    .resCode(RES_SUCCESS_CODE)
                    .msg(rechargeResponse.getMsg())
                    .payUrl(rechargeResponse.getData().getPay_url())
                    .build();
        } else {
            return PayInfoResponse.builder()
                    .resCode(RES_FAIL_CODE)
                    .msg(rechargeResponse.getMsg())
                    .payUrl(null)
                    .build();
        }


    }

    //服务器通知 （交易成功）
    @Override
    public NoticeOfPaymentResultResponse serverNoticeOfPaymentResult(HttpServletRequest request, HttpServletRequest response) {

        String memberid = request.getParameter("memberid");
        String orderid = request.getParameter("orderid");
        String amount = request.getParameter("amount");
        String datetime = request.getParameter("datetime");
        String attach = request.getParameter("attach");
        String transaction_id = request.getParameter("transaction_id");
        String return_code = request.getParameter("returncode");
        String sign = request.getParameter("sign");

        return NoticeOfPaymentResultResponse.builder()
                .memberId(memberid)
                .orderId(orderid)
                .amount(amount)
                .datetime(datetime)
                .attach(attach)
                .returnCode(return_code)
                .transactionId(transaction_id)
                .sign(sign)
                .build();
    }

    //页面通知 (支付成功)
    public NoticeOfPaymentResultResponse pageCallBackOfPaymentResult(HttpServletRequest request, HttpServletRequest response) {

        String memberid = request.getParameter("memberid");
        String orderid = request.getParameter("orderid");
        String amount = request.getParameter("amount");
        String datetime = request.getParameter("datetime");
        String attach = request.getParameter("attach");
        String transaction_id = request.getParameter("transaction_id");
        String return_code = request.getParameter("returncode");
        String sign = request.getParameter("sign");

        return NoticeOfPaymentResultResponse.builder()
                .memberId(memberid)
                .orderId(orderid)
                .amount(amount)
                .datetime(datetime)
                .attach(attach)
                .returnCode(return_code)
                .transactionId(transaction_id)
                .sign(sign)
                .build();
    }

    //查询交易信息
    @Override
    public TradeInfoResponse queryTransactionInformation(String outTradeNo) {


        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("pay_memberid", payResource.getPayMemberid());
        p.put("pay_orderid", outTradeNo);
        // 获得签名
        String sign = Sign.createSign("utf-8", p, payResource.getKey()).toUpperCase();
        //支付接口请求参数组装
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("pay_memberid", payResource.getPayMemberid());
        map.add("pay_orderid", outTradeNo);
        map.add("pay_md5sign", sign);
        //发起支付请求
        String tradeResultJsonStr = HttpUtil.sendPost(payResource.getQueryTradeUrl(), map);
        System.err.println("订单信息：" + tradeResultJsonStr);

        TradeInfoResponse tradeInfoResponse = JsonUtils.jsonToPojo(tradeResultJsonStr, TradeInfoResponse.class);
        assert tradeInfoResponse != null;
        return tradeInfoResponse;

    }


    public WithdrawalResponse withdrawal(WithdrawalReq withdrawalReq) {

        String outTradeNo = withdrawalReq.getOutTradeNo();
        String money = withdrawalReq.getMoney();
        String bankname = withdrawalReq.getBankname();
        String subbranch = withdrawalReq.getSubbranch();
        String accountname = withdrawalReq.getAccountname();
        String cardnumber = withdrawalReq.getCardnumber();
        String province = withdrawalReq.getProvince();
        String city = withdrawalReq.getCity();
        String ifsc = withdrawalReq.getIfsc();

        ExtendData extendData = ExtendData.builder().ifscCode(ifsc).build();
        String extendJsonStr = JsonUtils.objectToJson(extendData);
        assert extendJsonStr != null;
        String extend = new String(Base64.getEncoder().encode(extendJsonStr.getBytes()));

        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("mchid", payResource.getPayMemberid());
        p.put("out_trade_no", outTradeNo);
        p.put("money", money);
        p.put("bankname", bankname);
        p.put("subbranch", subbranch);
        p.put("accountname", accountname);
        p.put("cardnumber", cardnumber);
        p.put("province", province);
        p.put("city", city);
        p.put("extends", extend);
        p.put("currency", payResource.getCurrency());
        p.put("bank_code", payResource.getPayBankCode());
        p.put("callback_url", payResource.getWithdrawalCallbackUrl());
        p.put("pay_callbackurl", payResource.getPayCallbackUrl());

        // 获得签名
        String sign = Sign.createSign("utf-8", p, payResource.getKey()).toUpperCase();
        //支付接口请求参数组装
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("mchid", payResource.getPayMemberid());
        map.add("out_trade_no", outTradeNo);
        map.add("money", money);
        map.add("bankname", bankname);
        map.add("subbranch", subbranch);
        map.add("accountname", accountname);
        map.add("cardnumber", cardnumber);
        map.add("province", province);
        map.add("city", city);
        map.add("extends", extend);
        map.add("currency", payResource.getCurrency());
        map.add("bank_code", payResource.getPayBankCode());
        map.add("callback_url", payResource.getWithdrawalCallbackUrl());
        map.add("pay_md5sign", sign);
        //发起支付请求
        String withdrawalResultJsonStr = HttpUtil.sendPost(payResource.getWithdrawalUrl(), map);
        WithdrawalResponse withdrawalResponse = JsonUtils.jsonToPojo(withdrawalResultJsonStr, WithdrawalResponse.class);
        assert withdrawalResponse != null;

        return withdrawalResponse;
    }


    //代付 回调
    public CallBackOfWithdrawalResponse callBackOfWithdrawalResult(HttpServletRequest request, HttpServletRequest response) {

        String status = request.getParameter("status");
        String out_trade_no = request.getParameter("out_trade_no");
        String amount = request.getParameter("amount");
        String message = request.getParameter("message");
        String sign = request.getParameter("pay_md5sign");

        return CallBackOfWithdrawalResponse.builder()
                .amount(amount)
                .message(message)
                .outTradeNo(out_trade_no)
                .sign(sign)
                .status(status)
                .build();
    }


    @Override
    public WithdrawalTradeInfoResponse queryWithdrawalTransactionInformation(String outTradeNo) {

        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("mchid", payResource.getPayMemberid());
        p.put("out_trade_no", outTradeNo);
        // 获得签名
        String sign = Sign.createSign("utf-8", p, payResource.getKey()).toUpperCase();
        //支付接口请求参数组装
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        p.put("mchid", payResource.getPayMemberid());
        p.put("out_trade_no", outTradeNo);
        map.add("pay_md5sign", sign);
        //发起支付请求
        String withdrawaltradeResultJsonStr = HttpUtil.sendPost(payResource.getQueryTradeWithdrawalUrl(), map);
        System.err.println("订单信息：" + withdrawaltradeResultJsonStr);

        WithdrawalTradeInfoResponse withdrawalTradeInfoResponse = JsonUtils.jsonToPojo(withdrawaltradeResultJsonStr, WithdrawalTradeInfoResponse.class);
        assert withdrawalTradeInfoResponse != null;
        return withdrawalTradeInfoResponse;

    }

    //银行编码查询
    public BankCodeResponse queryBankCodeList(String currencyCode) {
        //发起支付请求
        String bankCodeListResultJsonStr = HttpUtil.sendGet(payResource.getQueryBankCodeUrl() + "" + currencyCode);
        System.err.println("订单信息：" + bankCodeListResultJsonStr);

        BankCodeResponse bankCodeResponse = JsonUtils.jsonToPojo(bankCodeListResultJsonStr, BankCodeResponse.class);
        assert bankCodeResponse != null;
        if (bankCodeResponse.getStatus().equals("ok")) {
            Object data = bankCodeResponse.getData();

        }

        return bankCodeResponse;

    }


    public static void main(String[] args) {

        String orderId = "upi00000" + DateUtil.dateToStringyyyyMMddhhmm(new Date());
        PaymentService paymentService = new PaymentService();
        paymentService.placeOrder("UPI payment", orderId, "10");


        paymentService.queryTransactionInformation(orderId);
    }
}
