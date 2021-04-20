package com.hy.utils.pay;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Sign {

    //支付签名算法
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters, String key) {
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        for (Object e : es) {
            Map.Entry entry = (Map.Entry) e;
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(key);
        return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
    }
}
