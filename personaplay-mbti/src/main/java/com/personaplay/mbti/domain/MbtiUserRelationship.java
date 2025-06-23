package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * 用户关系对象 mbti_user_relationship
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiUserRelationship extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关系ID */
    private Long relationshipId;

    /** 用户A的ID */
    @Excel(name = "用户A的ID")
    private Long userIdA;

    /** 用户B的ID */
    @Excel(name = "用户B的ID")
    private Long userIdB;

    /** 最近一次测试记录ID */
    @Excel(name = "最近一次测试记录ID")
    private Long latestTestRecordId;

    /** 共同测试次数 */
    @Excel(name = "共同测试次数")
    private Long testCount;

    /** 最新匹配度得分 */
    @Excel(name = "最新匹配度得分")
    private Long compatibilityScore;

    /** 最新默契度得分 */
    @Excel(name = "最新默契度得分")
    private Long chemistryScore;

    /** 最近测试时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近测试时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastTestTime;

    public void setRelationshipId(Long relationshipId)
    {
        this.relationshipId = relationshipId;
    }

    public Long getRelationshipId()
    {
        return relationshipId;
    }
    public void setUserIdA(Long userIdA)
    {
        this.userIdA = userIdA;
    }

    public Long getUserIdA()
    {
        return userIdA;
    }
    public void setUserIdB(Long userIdB)
    {
        this.userIdB = userIdB;
    }

    public Long getUserIdB()
    {
        return userIdB;
    }
    public void setLatestTestRecordId(Long latestTestRecordId)
    {
        this.latestTestRecordId = latestTestRecordId;
    }

    public Long getLatestTestRecordId()
    {
        return latestTestRecordId;
    }
    public void setTestCount(Long testCount)
    {
        this.testCount = testCount;
    }

    public Long getTestCount()
    {
        return testCount;
    }
    public void setCompatibilityScore(Long compatibilityScore)
    {
        this.compatibilityScore = compatibilityScore;
    }

    public Long getCompatibilityScore()
    {
        return compatibilityScore;
    }
    public void setChemistryScore(Long chemistryScore)
    {
        this.chemistryScore = chemistryScore;
    }

    public Long getChemistryScore()
    {
        return chemistryScore;
    }
    public void setLastTestTime(Date lastTestTime)
    {
        this.lastTestTime = lastTestTime;
    }

    public Date getLastTestTime()
    {
        return lastTestTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("relationshipId", getRelationshipId())
            .append("userIdA", getUserIdA())
            .append("userIdB", getUserIdB())
            .append("latestTestRecordId", getLatestTestRecordId())
            .append("testCount", getTestCount())
            .append("compatibilityScore", getCompatibilityScore())
            .append("chemistryScore", getChemistryScore())
            .append("lastTestTime", getLastTestTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
