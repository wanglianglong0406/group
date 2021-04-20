package com.hy.game.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hy.game.mapper.GameInfoMapper;
import com.hy.game.model.GameInfo;
import com.hy.game.service.GameInfoService;
import com.hy.pojo.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * (GameInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-11-22 16:12:26
 */
@RestController
public class GameInfoServiceImpl implements GameInfoService {
    @Autowired
    private GameInfoMapper gameInfoMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryGameInfoList(Integer gameType, Integer pageNo, Integer pageSize) {
        //  pageNo :第几页 pageSize : 每页显示的条数
        QueryWrapper<GameInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_type", gameType);
        queryWrapper.eq("game_status", 1);
        Page<GameInfo> page = new Page<>(pageNo, pageSize, true);
        IPage<GameInfo> iPage = gameInfoMapper.selectPage(page, queryWrapper);
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总记录数：" + iPage.getTotal());
        List<GameInfo> gameInfos = iPage.getRecords();
        gameInfos.forEach(System.out::println);
        return PagedGridResult.builder().pageNo(pageNo).rows(gameInfos).pages(iPage.getPages()).total(iPage.getTotal()).build();

    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryGameInfoByGameName(Integer gameType, String gameName, Integer pageNo, Integer pageSize) {


        QueryWrapper<GameInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_type", gameType);
        queryWrapper.eq("game_status", 1);
        queryWrapper.like("game_name", gameName);

        Page<GameInfo> page = new Page<>(pageNo, pageSize, true);
        IPage<GameInfo> iPage = gameInfoMapper.selectPage(page, queryWrapper);
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("总记录数：" + iPage.getTotal());
        List<GameInfo> gameInfos = iPage.getRecords();
        gameInfos.forEach(System.out::println);
        return PagedGridResult.builder().pageNo(pageNo).rows(gameInfos).pages(iPage.getPages()).total(iPage.getTotal()).build();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public GameInfo queryGameInfo(String gameId, Integer gameType) {
        QueryWrapper<GameInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_type", gameType);
        queryWrapper.eq("game_status", 1);
        queryWrapper.eq("game_type", gameType);
        return gameInfoMapper.selectOne(queryWrapper);
    }


}