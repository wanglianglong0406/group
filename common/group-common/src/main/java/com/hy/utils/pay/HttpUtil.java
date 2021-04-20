package com.hy.utils.pay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: $-  -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2021/1/17 15:11
 * @UpdateUser: 寒夜
 * @UpdateDate: 2021/1/17 15:11
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Slf4j
public class HttpUtil {

    public static String sendPost(String url, MultiValueMap<String, String> map) {
        log.info("===============请求参数：url={},map={}", url, map);
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf("multipart/form-data"));
            httpHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            HttpEntity<MultiValueMap<String, String>> requestParams = new HttpEntity<>(map, httpHeaders);
            ResponseEntity<String> response = new RestTemplate().postForEntity(url, requestParams, String.class);
            String result = response.getBody();
            log.info("======================响应结果result={}", result);
            return result;
        } catch (Exception e) {
            log.error("接口异常", e);
        }
        return null;
    }

    public static String sendGet(String url) {
        log.info("===============请求参数：url={}", url);
        try {

            ResponseEntity<String> response = new RestTemplate().getForEntity(url,String.class);
            String result = response.getBody();
            log.info("======================响应结果result={}", result);
            return result;
        } catch (Exception e) {
            log.error("接口异常", e);
        }
        return null;
    }
}



