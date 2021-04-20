package com.hy.constant;

/**
 * @Description: $- Constant -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 18:32
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 18:32
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Constant {

    public static final String VERIFICATION_CODE = "verification-code";
    public static final String USER_FAACE_IMGE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    public final static String REDIS_USER_TOKEN = "redis_user_token";
    public final static String REDIS_USER_INFO = "redis_user_info";
    public final static String AUTH_HEADER = "Authorization";
    public final static String REFRESH_TOKEN_HEADER = "refresh-token";
    public final static String UID_HEADER = "user-id";

    public final static String DEFAULT_PASSWORD = "1111";
    public final static Double  DEFAULT_BLANCE = 0.00;
    public final static Double  VIRTUAL_EXPERIENCE_GOLD = 200.00;
    public static final String LOTTERY_ACTION_NO_REDIS_LOCK = "lottery:actionNo:";
    public static final String REDIS_LOCK_ORDER_BET_RECORD = "lottery:order:bet_record:redis_lock:";
    public static final String REDIS_ORDER_LOTTERY_TOKEN = "lottery:order:redis_order_token:";
    public final static String CREATE_LOTTERY_REDIS_LOCK = "lottery:create_lottery:task:redis_lock";
    public final static String REDIS_LOCK_ORDER_TASK_RECORD = "order:task:redis_lock:";
    public final static String REDIS_LOCK_ORDER_FINANCIAL = "order:financial:redis_lock:";
    public final static String REDIS_ORDER_TOKEN = "order:task:order:redis_order_token:";
    public final static String LOTTERY_SHOP_CART = "lottery:shopcart:";
    public final static String LOTTERY_INFO = "lottery:info:";

    public final static String PLATFROM_INFO = "platfrom:info:list";
    public final static String PAY_METHOD = "paymethod:info:list";
    public final static String ITEMS_LIST = "items:info:list";


    public final static String X_DELAY = "x-delay";
    public final static int THREE_MINUTES = 150*1000;


    public final static Integer PAGE_NO=1;
    public final static Integer PAGE_SIZE=20;


    //登录过期
    public final static Integer LOGIN_EXPIRED_CODE=1001;
    public final static Integer SUCCESS_CODE=200;

    //支付相关代码
    public final static String RES_SUCCESS_CODE="success";
    public final static String RES_SUCCESS_MSG="success";

    public final static String RES_FAIL_CODE="fail";
    public final static String RES_FAIL_MSG="fail";
    public final static String RETURN_SUCCESS_CODE="00";




}

