package com.hy.account.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: $- 银行卡信息表 银行卡信息表(BankcardInfo)表实体类 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/19 15:50
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/19 15:50
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankcardInfo extends Model<BankcardInfo> {
    private static final long serialVersionUID = -2806114625574273634L;
    //银行卡ID 银行卡ID唯一主键
    @TableId(type= IdType.ID_WORKER)
    private Long bankId;
    //用户ID 用户ID
    private String userId;
    //账户ID 账户ID
    private Long accountId;
    //银行卡号 银行卡号
    private Long bankNo;
    //持卡人姓名 持卡人姓名
    private String cardholderName;
    //银行名称 银行名称
    private String bankName;
    /** IFSC代码;IFSC代码 */
    private String ifsc ;
    //银行类型 银行类型
    private String bankType;
    //是否默认银行卡 是否默认银行卡(0 ：默认： 1：其他)
    private String bankIsDefault;
    //创建时间 创建时间
    private Date createTime;
    //更新时间 更新时间
    private Date updateTime;


    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBankNo() {
        return bankNo;
    }

    public void setBankNo(Long bankNo) {
        this.bankNo = bankNo;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankIsDefault() {
        return bankIsDefault;
    }

    public void setBankIsDefault(String bankIsDefault) {
        this.bankIsDefault = bankIsDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.bankId;
    }
}