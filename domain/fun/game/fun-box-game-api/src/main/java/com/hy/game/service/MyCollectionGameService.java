package com.hy.game.service;

import com.hy.pojo.ResponseJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (MyCollectionGame)表服务接口
 *
 * @author makejava
 * @since 2020-11-22 16:13:19
 */
@Api(value = "游戏中心我的游戏相关API", tags = "游戏中心我的游戏相关API")
@RequestMapping("game-api/colletion/v1")
@FeignClient("fun-box-game-service")
public interface MyCollectionGameService {
    /**
     * 游戏收藏
     * @param userId
     * @param gameId
     * @return
     */
    @ApiOperation(value = "收藏游戏", notes = "收藏游戏", httpMethod = "POST")
    @PostMapping("collection")
    public ResponseJsonResult addToMyCollectionlist(
            @ApiParam(value = "用户id", name = "userId", type = "String",required = true)
            @RequestParam("userId") String userId,
            @ApiParam(value = "游戏ID", name = "gameId", type = "String",required = true, example = "")
            @RequestParam("gameId") String gameId,
            @ApiParam(value = "游戏类型（1 : HB Game  2 : MG Game    3 : UPG Game）", name = "gameType", type = "Integer", required = true, example = "1")
            @RequestParam("gameType") Integer gameType);

}