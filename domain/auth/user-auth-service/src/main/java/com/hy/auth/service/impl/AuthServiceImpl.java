package com.hy.auth.service.impl;

import com.hy.auth.service.AuthService;
import com.hy.auth.service.pojo.Account;
import com.hy.auth.service.pojo.AuthCode;
import com.hy.auth.service.pojo.AuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Description: $- AuthServiceImpl -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 8:22
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 8:22
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@RestController
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String USER_TOKEN = "USER_TOKEN-";

    @Override
    public AuthResponse tokenize(String userId) {
        Account account = Account.builder()
                .userId(userId)
                .build();

        String token = jwtService.token(account);
        account.setToken(token);
        account.setRefreshToken(UUID.randomUUID().toString());

        redisTemplate.opsForValue().set(USER_TOKEN + userId, account);
        redisTemplate.opsForValue().set(account.getRefreshToken(), userId);

        return AuthResponse.builder()
                .account(account)
                .code(AuthCode.SUCCESS.getCode())
                .build();
    }

    @Override
    public AuthResponse verify(Account account) {
        boolean success = jwtService.verify(account.getToken(), account.getUserId());
        return AuthResponse.builder()
                // TODO 此处最好用invalid token之类的错误信息
                .code(success ? AuthCode.SUCCESS.getCode() : AuthCode.USER_NOT_FOUND.getCode())
                .build();
    }

    // 有很多种方法实现自动刷新，比如前端主动调用（可以在AuthResponse里将过期时间返回给前端口）
    @Override
    public AuthResponse refresh(String refreshToken) {
        String userId = (String) redisTemplate.opsForValue().get(refreshToken);
        if (StringUtils.isBlank(userId)) {
            return AuthResponse.builder()
                    .code(AuthCode.USER_NOT_FOUND.getCode())
                    .build();
        }

        redisTemplate.delete(refreshToken);
        return tokenize(userId);
    }

    @Override
    public AuthResponse delete(@RequestBody Account account) {
        AuthResponse token = verify(account);
        AuthResponse resp = new AuthResponse();
        if (AuthCode.SUCCESS.getCode().equals(token.getCode())) {
            redisTemplate.delete(USER_TOKEN + account.getUserId());
            redisTemplate.delete(account.getRefreshToken());
            resp.setCode(AuthCode.SUCCESS.getCode());
        } else {
            resp.setCode(AuthCode.USER_NOT_FOUND.getCode());
        }
        return resp;
    }
}
