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
 * (MyCollectionGame)表实体类
 *
 * @author 寒夜
 * @since 2020-11-22 16:13:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyCollectionGame extends Model<MyCollectionGame> {
    //ID（唯一主键） 唯一主键
    @TableId(type= IdType.ID_WORKER)
    private Long id;
    //用户Id 用户id
    private String userId;
    //游戏标识符 游戏标识符（游戏唯一主键ID）
    private String gameId;
    //游戏名称快照 游戏名称快照
    private String gameName;
    //游戏图片地址快照 游戏图片地址快照
    private String gameImgUrl;
    //游戏状态快照 游戏状态（1：上架 2：下架）
    private Integer gameStatus;
    //游戏类型 1 : HB Game 2: MG Game  3: UPG Game
    private Integer gameType;
    /** 游戏连接;游戏连接 */
    private String gameLinkAddress ;
    //游戏所得奖励 游戏所得奖励
    private Long reward;
    //创建时间 创建时间
    private Date createTime;
    //更新时间 更新时间
    private Date updatedTime;


}