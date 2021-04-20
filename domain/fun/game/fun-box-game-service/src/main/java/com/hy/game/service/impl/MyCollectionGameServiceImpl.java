package com.hy.game.service.impl;

import com.hy.game.mapper.MyCollectionGameMapper;
import com.hy.game.model.GameInfo;
import com.hy.game.model.MyCollectionGame;
import com.hy.game.service.GameInfoService;
import com.hy.game.service.MyCollectionGameService;
import com.hy.pojo.ResponseJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * (MyCollectionGame)表服务实现类
 *
 * @author makejava
 * @since 2020-11-22 16:13:19
 */
@RestController
public class MyCollectionGameServiceImpl implements MyCollectionGameService {

    @Autowired
    private MyCollectionGameMapper myCollectionGameMapper;
    @Autowired
    private GameInfoService gameInfoService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ResponseJsonResult addToMyCollectionlist(String userId, String gameId,Integer gameType) {
        //先获取到游戏信息
        GameInfo gameInfo = gameInfoService.queryGameInfo(gameId,gameType);
        //将游戏快照保存在我的游戏列表
        myCollectionGameMapper.insert( MyCollectionGame.builder().gameId(gameId).userId(userId).gameType(gameType).gameLinkAddress(gameInfo.getGameLinkAddress())
                .gameImgUrl(gameInfo.getGameImgUrl()).gameName(gameInfo.getGameName()).gameStatus(gameInfo.getGameStatus())
                .reward(0L).createTime(new Date()).updatedTime(new Date()).build());
        return ResponseJsonResult.ok("Collection successful");
    }
}