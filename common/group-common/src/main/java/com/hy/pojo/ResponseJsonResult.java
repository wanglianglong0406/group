package com.hy.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @Title: ResponseJsonResult.java
 * @Package com.hy.utils
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 * 
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * 				556: 用户qq校验异常
 * 			    557: 校验用户是否在CAS 登录
 * @Copyright: Copyright (c) 2020
 * @Company:
 * @author 寒夜
 * @version V1.0
 */
public class ResponseJsonResult<T> {

    // 定义jackson对象
    //private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    //当前服务器时间
    private Long serverTime;
    
    @JsonIgnore
    private String ok;	// 不使用

    public static ResponseJsonResult build(Integer status, String msg, Object data) {
        return new ResponseJsonResult(status, msg, data);
    }

    public static ResponseJsonResult build(Integer status, String msg, Object data, String ok) {
        return new ResponseJsonResult(status, msg, data, ok);
    }
    
    public static ResponseJsonResult ok(Object data) {
        return new ResponseJsonResult(data);
    }

    public static ResponseJsonResult ok(Object data, Long serverTime) {
        return new ResponseJsonResult(data,serverTime);
    }

    public static ResponseJsonResult ok() {
        return new ResponseJsonResult(null);
    }
    
    public static ResponseJsonResult errorMsg(String msg) {
        return new ResponseJsonResult(500, msg, null);
    }

    public static ResponseJsonResult errorUserTicketMsg(String msg) {
        return new ResponseJsonResult(557, msg, null);
    }
    
    public static ResponseJsonResult errorMap(Object data) {
        return new ResponseJsonResult(501, "error", data);
    }
    
    public static ResponseJsonResult errorTokenMsg(String msg) {
        return new ResponseJsonResult(502, msg, null);
    }
    
    public static ResponseJsonResult errorException(String msg) {
        return new ResponseJsonResult(555, msg, null);
    }
    
    public static ResponseJsonResult errorUserQQ(String msg) {
        return new ResponseJsonResult(556, msg, null);
    }

    public ResponseJsonResult() {

    }

    public ResponseJsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public ResponseJsonResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public ResponseJsonResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public ResponseJsonResult(Object data, Long serverTime) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
        if(serverTime==null){
            this.serverTime=System.currentTimeMillis();
        }
        this.serverTime=serverTime;

    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }
}
