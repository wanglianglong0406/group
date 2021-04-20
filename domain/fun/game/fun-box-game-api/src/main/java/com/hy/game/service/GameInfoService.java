package com.hy.game.service;

import com.hy.game.model.GameInfo;
import com.hy.pojo.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * (GameInfo)表服务接口
 *
 * @author 寒夜
 * @since 2020-11-22 16:12:26
 */
@Api(value = "游戏中心游戏信息相关API", tags = "游戏中心信息相关API")
@RequestMapping("game-api/v1")
@FeignClient("fun-box-game-service")
public interface GameInfoService {

    @ApiOperation(value = "获取游戏列表", notes = "获取游戏列表", httpMethod = "GET")
    @GetMapping("games")
    public PagedGridResult queryGameInfoList(
            @ApiParam(value = "游戏类型（1 : HB Game  2 : MG Game    3 : UPG Game）", name = "gameType", type = "Integer", required = true)
            @RequestParam("gameType") Integer gameType,
            @ApiParam(value = "当前页数", name = "pageNo", type = "Integer", required = true)
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @ApiParam(value = "每页的数量", name = "pageSize", type = "Integer", required = true)
            @RequestParam(value = "pageSize", required = false) Integer pageSize);


    @ApiOperation(value = "根据游戏名称搜索游戏", notes = "根据游戏名称搜索游戏", httpMethod = "GET")
    @GetMapping("gameInfoByName")
    public PagedGridResult queryGameInfoByGameName(
            @ApiParam(value = "游戏类型（1 : HB Game  2 : MG Game    3 : UPG Game）", name = "gameType", type = "Integer", required = true)
            @RequestParam("gameType") Integer gameType,
            @ApiParam(value = "游戏名称", name = "gameName", type = "String", required = true)
            @RequestParam(value = "gameName", required = false) String gameName,
            @ApiParam(value = "当前页数", name = "pageNo", type = "Integer", required = true)
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @ApiParam(value = "每页的数量", name = "pageSize", type = "Integer", required = true)
            @RequestParam(value = "pageSize", required = false) Integer pageSize);

    @ApiOperation(value = "根据游戏名称搜索游戏", notes = "根据游戏名称搜索游戏", httpMethod = "GET",hidden = true)
    @GetMapping("gameInfo")
    public GameInfo queryGameInfo(@ApiParam(value = "游戏ID", name = "gameId", type = "String", required = true)
                                  @RequestParam("gameId") String gameId,
                                  @ApiParam(value = "游戏类型（1 : HB Game  2 : MG Game    3 : UPG Game）", name = "gameType", type = "Integer", required = true)
                                  @RequestParam("gameType") Integer gameType);
}