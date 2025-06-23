package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI测试房间对象 mbti_room
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiRoom extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 房间ID */
    private Long roomId;

    /** 房间号码 */
    @Excel(name = "房间号码")
    private String roomCode;

    /** 创建者用户ID */
    @Excel(name = "创建者用户ID")
    private Long creatorId;

    /** 测试版本ID */
    @Excel(name = "测试版本ID")
    private Long versionId;

    /** 房间状态(0等待中 1测试中 2已完成 3已关闭) */
    @Excel(name = "房间状态(0等待中 1测试中 2已完成 3已关闭)")
    private String status;

    /** 成员上限 */
    @Excel(name = "成员上限")
    private Long memberLimit;

    /** 当前成员数 */
    @Excel(name = "当前成员数")
    private Long currentMembers;

    /** 二维码URL */
    @Excel(name = "二维码URL")
    private String qrCodeUrl;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Long getRoomId()
    {
        return roomId;
    }
    public void setRoomCode(String roomCode)
    {
        this.roomCode = roomCode;
    }

    public String getRoomCode()
    {
        return roomCode;
    }
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }
    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getVersionId()
    {
        return versionId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setMemberLimit(Long memberLimit)
    {
        this.memberLimit = memberLimit;
    }

    public Long getMemberLimit()
    {
        return memberLimit;
    }
    public void setCurrentMembers(Long currentMembers)
    {
        this.currentMembers = currentMembers;
    }

    public Long getCurrentMembers()
    {
        return currentMembers;
    }
    public void setQrCodeUrl(String qrCodeUrl)
    {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getQrCodeUrl()
    {
        return qrCodeUrl;
    }
    public void setExpireTime(Date expireTime)
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime()
    {
        return expireTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roomId", getRoomId())
            .append("roomCode", getRoomCode())
            .append("creatorId", getCreatorId())
            .append("versionId", getVersionId())
            .append("status", getStatus())
            .append("memberLimit", getMemberLimit())
            .append("currentMembers", getCurrentMembers())
            .append("qrCodeUrl", getQrCodeUrl())
            .append("expireTime", getExpireTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
