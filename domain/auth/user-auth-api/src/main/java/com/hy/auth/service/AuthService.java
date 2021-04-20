package com.hy.auth.service;

import com.hy.auth.service.pojo.Account;
import com.hy.auth.service.pojo.AuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: $- AuthService -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 8:13
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 8:13
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@FeignClient("fun-box-auth-service")
@RequestMapping("auth-service")
public interface AuthService {

    @PostMapping("token")
    public AuthResponse tokenize(@RequestParam("userId") String userId);

    @PostMapping("verify")
    public AuthResponse verify(@RequestBody Account account);

    @PostMapping("refresh")
    public AuthResponse refresh(@RequestParam("refresh") String refresh);

    @DeleteMapping("delete")
    public AuthResponse delete(@RequestBody Account account);

}
