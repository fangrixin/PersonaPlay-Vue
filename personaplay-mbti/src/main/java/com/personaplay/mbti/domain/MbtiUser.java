package com.personaplay.mbti.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * MBTI用户信息对象 mbti_user
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String username;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickname;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 头像URL */
    @Excel(name = "头像URL")
    private String avatar;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 性别（0未知 1男 2女） */
    @Excel(name = "性别", readConverterExp = "0=未知,1=男,2=女")
    private String sex;

    /** 微信openid */
    @Excel(name = "微信openid")
    private String openId;

    /** 微信unionid */
    @Excel(name = "微信unionid")
    private String unionId;

    /** 账号状态（0正常 1停用） */
    @Excel(name = "账号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 会员开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipStart;

    /** 会员过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipExpire;


    /** 会员等级 */
    private String vipLevel;

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getNickname()
    {
        return nickname;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getSex()
    {
        return sex;
    }
    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getOpenId()
    {
        return openId;
    }
    public void setUnionId(String unionId)
    {
        this.unionId = unionId;
    }

    public String getUnionId()
    {
        return unionId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }


    public Date getVipStart() {
        return vipStart;
    }

    public void setVipStart(Date vipStart) {
        this.vipStart = vipStart;
    }

    public Date getVipExpire() {
        return vipExpire;
    }

    public void setVipExpire(Date vipExpire) {
        this.vipExpire = vipExpire;
    }


    public String getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(String vipLevel) {
        this.vipLevel = vipLevel;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("username", getUsername())
            .append("nickname", getNickname())
            .append("password", getPassword())
            .append("avatar", getAvatar())
            .append("email", getEmail())
            .append("phone", getPhone())
            .append("sex", getSex())
            .append("openId", getOpenId())
            .append("unionId", getUnionId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
