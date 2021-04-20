package com.hy.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: $- 预下单参数 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/15 17:49
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/15 17:49
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreOrder implements Serializable {
    private static final long serialVersionUID = -6842802338102146184L;
    //商户号
    private String pay_memberid;
    // 订单号
    private String pay_orderid;
    //提交时间
    private String pay_applydate;
    //银行编码
    private String pay_bankcode;
    //服务端通知
    private String pay_notifyurl;
    //页面跳转通知
    private String pay_callbackurl;
    //订单金额
    private String pay_amount;
    // MD5签名
    private String pay_md5sign;
    //商品名称
    private String pay_productname;
    //商户品数量
//    private String pay_productnum;
//    //商品描述
//    private String pay_productdesc;
//    //商户链接地址
//    private String pay_producturl;
//    //附加字段
//    private String pay_attach;

}
