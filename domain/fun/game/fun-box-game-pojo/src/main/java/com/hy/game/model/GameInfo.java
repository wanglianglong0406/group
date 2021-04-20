package com.hy.game.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (GameInfo)表实体类
 *
 * @author makejava
 * @since 2020-11-22 16:12:25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameInfo extends Model<GameInfo> {
    //ID（唯一主键） 唯一主键
    @TableId(type= IdType.UUID)
    private String id;
    //游戏名称 游戏名称
    private String gameName;
    //游戏主图地址 游戏主图地址
    private String gameImgUrl;
    //游戏描述 游戏描述
    private String gameDescribe;
    //游戏状态 游戏状态（1：上架 2：下架）
    private Integer gameStatus;
    //游戏类型 1 : HB Game 2: MG Game  3: UPG Game
    private Integer gameType;
    //收藏总计 收藏总计，用于统排序，统计热门游戏
    private Integer collectionTotal;
    /** 游戏连接;游戏连接 */
    private String gameLinkAddress ;
    //创建时间 创建时间
    private Date createTime;
    //更新时间 更新时间
    private Date updatedTime;

}