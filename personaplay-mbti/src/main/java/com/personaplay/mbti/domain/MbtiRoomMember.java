package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * 房间成员对象 mbti_room_member
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiRoomMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成员ID */
    private Long memberId;

    /** 房间ID */
    @Excel(name = "房间ID")
    private Long roomId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 是否准备好(0未准备 1已准备) */
    @Excel(name = "是否准备好(0未准备 1已准备)")
    private Integer isReady;

    /** 是否是创建者(0否 1是) */
    @Excel(name = "是否是创建者(0否 1是)")
    private Integer isCreator;

    /** 加入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinTime;

    /** 准备时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "准备时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date readyTime;

    /** 对应的测试记录ID */
    @Excel(name = "对应的测试记录ID")
    private Long testRecordId;

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Long getMemberId()
    {
        return memberId;
    }
    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Long getRoomId()
    {
        return roomId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setIsReady(Integer isReady)
    {
        this.isReady = isReady;
    }

    public Integer getIsReady()
    {
        return isReady;
    }
    public void setIsCreator(Integer isCreator)
    {
        this.isCreator = isCreator;
    }

    public Integer getIsCreator()
    {
        return isCreator;
    }
    public void setJoinTime(Date joinTime)
    {
        this.joinTime = joinTime;
    }

    public Date getJoinTime()
    {
        return joinTime;
    }
    public void setReadyTime(Date readyTime)
    {
        this.readyTime = readyTime;
    }

    public Date getReadyTime()
    {
        return readyTime;
    }
    public void setTestRecordId(Long testRecordId)
    {
        this.testRecordId = testRecordId;
    }

    public Long getTestRecordId()
    {
        return testRecordId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("roomId", getRoomId())
            .append("userId", getUserId())
            .append("isReady", getIsReady())
            .append("isCreator", getIsCreator())
            .append("joinTime", getJoinTime())
            .append("readyTime", getReadyTime())
            .append("testRecordId", getTestRecordId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
