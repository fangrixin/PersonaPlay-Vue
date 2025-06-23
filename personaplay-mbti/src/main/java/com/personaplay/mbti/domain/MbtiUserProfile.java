package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * 用户MBTI档案对象 mbti_user_profile
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiUserProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 档案ID */
    private Long profileId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** MBTI类型 */
    @Excel(name = "MBTI类型")
    private String mbtiType;

    /** 最近测试时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最近测试时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date latestTestTime;

    /** E/I维度得分 */
    @Excel(name = "E/I维度得分")
    private Long eIScore;

    /** S/N维度得分 */
    @Excel(name = "S/N维度得分")
    private Long sNScore;

    /** T/F维度得分 */
    @Excel(name = "T/F维度得分")
    private Long tFScore;

    /** J/P维度得分 */
    @Excel(name = "J/P维度得分")
    private Long jPScore;

    public void setProfileId(Long profileId)
    {
        this.profileId = profileId;
    }

    public Long getProfileId()
    {
        return profileId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setMbtiType(String mbtiType)
    {
        this.mbtiType = mbtiType;
    }

    public String getMbtiType()
    {
        return mbtiType;
    }
    public void setLatestTestTime(Date latestTestTime)
    {
        this.latestTestTime = latestTestTime;
    }

    public Date getLatestTestTime()
    {
        return latestTestTime;
    }
    public void seteIScore(Long eIScore)
    {
        this.eIScore = eIScore;
    }

    public Long geteIScore()
    {
        return eIScore;
    }
    public void setsNScore(Long sNScore)
    {
        this.sNScore = sNScore;
    }

    public Long getsNScore()
    {
        return sNScore;
    }
    public void settFScore(Long tFScore)
    {
        this.tFScore = tFScore;
    }

    public Long gettFScore()
    {
        return tFScore;
    }
    public void setjPScore(Long jPScore)
    {
        this.jPScore = jPScore;
    }

    public Long getjPScore()
    {
        return jPScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("profileId", getProfileId())
            .append("userId", getUserId())
            .append("mbtiType", getMbtiType())
            .append("latestTestTime", getLatestTestTime())
            .append("eIScore", geteIScore())
            .append("sNScore", getsNScore())
            .append("tFScore", gettFScore())
            .append("jPScore", getjPScore())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
