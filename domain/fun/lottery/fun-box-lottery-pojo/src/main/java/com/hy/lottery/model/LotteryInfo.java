package com.hy.lottery.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * (LotteryInfo)表实体类
 *
 * @author 寒夜
 * @since 2020-11-23 15:31:44
 */
@ApiModel(value = "彩票列表实体对象", description = "返回给客户端的数据封装再次实体对象中")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LotteryInfo extends Model<LotteryInfo> {
    //ID（唯一主键） 唯一主键
    @TableId(type= IdType.ID_WORKER)
    @JsonIgnore
    private Long id;
    //期数 期数
    @ApiModelProperty(value = "期数", name = "period", dataType = "String")
    private String period;
    //浮动数值 浮动数值
    @ApiModelProperty(value = "浮动数值", name = "number", dataType = "Integer")
    private Integer number;
    //价格 价格
    @ApiModelProperty(value = "价格", name = "price", dataType = "Long")
    private Long price;
    //结果 开奖结果： 1：红球 2 ：绿球 3 ：紫罗兰
    @ApiModelProperty(value = "开奖结果 RED GREEN VIOLET ", name = "result", dataType = "String")
    private String result;
    //类型 类型（PARITY   SAPRE  BCONE  EMEND）
    @ApiModelProperty(value = "类型（PARITY   SAPRE  BCONE  EMEND）", name = "type", dataType = "String")
    private String type;
    //开奖状态 开奖状态 1  ：未开 2 ：已开
    @ApiModelProperty(value = "开奖状态 1  ：未开 2 ：已开", name = "lotteryStatus", dataType = "Integer")
    private Integer lotteryStatus;
    //创建时间 创建时间
    @JsonIgnore
    private Date createTime;
    //更新时间 更新时间
    @JsonIgnore
    private Date updatedTime;
    //开始时间 开始时间
    @ApiModelProperty(value = "开始时间", name = "startTime", dataType = "Date")
    private Long startTime;
    //结束时间 结束时间
    @ApiModelProperty(value = "结束时间", name = "endTime", dataType = "Date")
    private Long endTime;
    //下单截至时间 下单截至时间（同一期 三分钟 ，2分30秒下单 时间，超过150秒 不允许下单，30秒显示开奖结果）
    @ApiModelProperty(value = "下单截至时间（同一期 三分钟 ，2分30秒下单 时间，超过150秒 不允许下单，30秒显示开奖结果）", name = "lastCreateOrderTime", dataType = "Date")
    private Long lastCreateOrderTime;
    //标识符 1 : 系统干预  2： 人工干预  3：其他
    private String isFlag;
    /** 开奖时间 */
    private Date openTime ;



}