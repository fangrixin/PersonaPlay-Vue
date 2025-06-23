package com.personaplay.mbti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI测试版本对象 mbti_test_version
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiTestVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 版本ID */
    private Long versionId;

    /** 版本代码(short/standard/full) */
    @Excel(name = "版本代码(short/standard/full)")
    private String versionCode;

    /** 版本名称(简短版/标准版/完整版) */
    @Excel(name = "版本名称(简短版/标准版/完整版)")
    private String versionName;

    /** 题目数量 */
    @Excel(name = "题目数量")
    private Long questionCount;

    /** 时间限制(秒) */
    @Excel(name = "时间限制(秒)")
    private Long timeLimit;

    /** 版本描述 */
    @Excel(name = "版本描述")
    private String description;

    /** 状态(0启用 1禁用) */
    @Excel(name = "状态(0启用 1禁用)")
    private String status;

    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getVersionId()
    {
        return versionId;
    }
    public void setVersionCode(String versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getVersionCode()
    {
        return versionCode;
    }
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getVersionName()
    {
        return versionName;
    }
    public void setQuestionCount(Long questionCount)
    {
        this.questionCount = questionCount;
    }

    public Long getQuestionCount()
    {
        return questionCount;
    }
    public void setTimeLimit(Long timeLimit)
    {
        this.timeLimit = timeLimit;
    }

    public Long getTimeLimit()
    {
        return timeLimit;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("versionId", getVersionId())
            .append("versionCode", getVersionCode())
            .append("versionName", getVersionName())
            .append("questionCount", getQuestionCount())
            .append("timeLimit", getTimeLimit())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
