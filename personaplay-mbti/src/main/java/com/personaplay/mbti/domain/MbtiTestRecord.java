package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI测试记录对象 mbti_test_record
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiTestRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 测试类型(1单人 2双人) */
    @Excel(name = "测试类型(1单人 2双人)")
    private String testType;

    /** 测试版本ID */
    @Excel(name = "测试版本ID")
    private Long versionId;

    /** 房间ID(双人测试) */
    @Excel(name = "房间ID(双人测试)")
    private Long roomId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 测试时长(秒) */
    @Excel(name = "测试时长(秒)")
    private Long duration;

    /** MBTI结果 */
    @Excel(name = "MBTI结果")
    private String mbtiResult;

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

    /** 匹配度得分(双人测试) */
    @Excel(name = "匹配度得分(双人测试)")
    private Long compatibilityScore;

    /** 默契度得分(双人测试) */
    @Excel(name = "默契度得分(双人测试)")
    private Long chemistryScore;

    /** 相同答案数(双人测试) */
    @Excel(name = "相同答案数(双人测试)")
    private Long identicalAnswers;

    /** 总题目数 */
    @Excel(name = "总题目数")
    private Long totalQuestions;

    /** 状态(0进行中 1已完成 2中断) */
    @Excel(name = "状态(0进行中 1已完成 2中断)")
    private String status;


    // 伙伴id
    private String partnerId;


    // 伙伴名称
    private String partnerName;


    // 伙伴测试结果
    private String partnerMbtiResult;

    /** 关系分析 */
    @Excel(name = "关系分析")
    private String relationshipAnalysis;

    /** 优势互补 */
    @Excel(name = "优势互补")
    private String advantages;

    /** 潜在挑战 */
    @Excel(name = "潜在挑战")
    private String challenges;

    /** 相处建议 */
    @Excel(name = "相处建议")
    private String recommendations;


    public String getRelationshipAnalysis() {
        return relationshipAnalysis;
    }

    public void setRelationshipAnalysis(String relationshipAnalysis) {
        this.relationshipAnalysis = relationshipAnalysis;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getChallenges() {
        return challenges;
    }

    public void setChallenges(String challenges) {
        this.challenges = challenges;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }



    public String getPartnerMbtiResult() {
        return partnerMbtiResult;
    }

    public void setPartnerMbtiResult(String partnerMbtiResult) {
        this.partnerMbtiResult = partnerMbtiResult;
    }
    public String getPartnerName() {
        return partnerName;
    }


    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }


    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTestType(String testType)
    {
        this.testType = testType;
    }

    public String getTestType()
    {
        return testType;
    }
    public void setVersionId(Long versionId)
    {
        this.versionId = versionId;
    }

    public Long getVersionId()
    {
        return versionId;
    }
    public void setRoomId(Long roomId)
    {
        this.roomId = roomId;
    }

    public Long getRoomId()
    {
        return roomId;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setDuration(Long duration)
    {
        this.duration = duration;
    }

    public Long getDuration()
    {
        return duration;
    }
    public void setMbtiResult(String mbtiResult)
    {
        this.mbtiResult = mbtiResult;
    }

    public String getMbtiResult()
    {
        return mbtiResult;
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
    public void setIdenticalAnswers(Long identicalAnswers)
    {
        this.identicalAnswers = identicalAnswers;
    }

    public Long getIdenticalAnswers()
    {
        return identicalAnswers;
    }
    public void setTotalQuestions(Long totalQuestions)
    {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalQuestions()
    {
        return totalQuestions;
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
            .append("recordId", getRecordId())
            .append("userId", getUserId())
            .append("testType", getTestType())
            .append("versionId", getVersionId())
            .append("roomId", getRoomId())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("duration", getDuration())
            .append("mbtiResult", getMbtiResult())
            .append("eIScore", geteIScore())
            .append("sNScore", getsNScore())
            .append("tFScore", gettFScore())
            .append("jPScore", getjPScore())
            .append("compatibilityScore", getCompatibilityScore())
            .append("chemistryScore", getChemistryScore())
            .append("identicalAnswers", getIdenticalAnswers())
            .append("totalQuestions", getTotalQuestions())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
