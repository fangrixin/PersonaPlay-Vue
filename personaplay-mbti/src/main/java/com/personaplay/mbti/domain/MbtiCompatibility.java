package com.personaplay.mbti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI类型兼容性对象 mbti_compatibility
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiCompatibility extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 兼容性ID */
    private Long compatibilityId;

    /** 第一个MBTI类型 */
    @Excel(name = "第一个MBTI类型")
    private String typeCode1;

    /** 第二个MBTI类型 */
    @Excel(name = "第二个MBTI类型")
    private String typeCode2;

    /** 兼容性评分(0-100) */
    @Excel(name = "兼容性评分(0-100)")
    private Long compatibilityScore;

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

    public void setCompatibilityId(Long compatibilityId)
    {
        this.compatibilityId = compatibilityId;
    }

    public Long getCompatibilityId()
    {
        return compatibilityId;
    }
    public void setTypeCode1(String typeCode1)
    {
        this.typeCode1 = typeCode1;
    }

    public String getTypeCode1()
    {
        return typeCode1;
    }
    public void setTypeCode2(String typeCode2)
    {
        this.typeCode2 = typeCode2;
    }

    public String getTypeCode2()
    {
        return typeCode2;
    }
    public void setCompatibilityScore(Long compatibilityScore)
    {
        this.compatibilityScore = compatibilityScore;
    }

    public Long getCompatibilityScore()
    {
        return compatibilityScore;
    }
    public void setRelationshipAnalysis(String relationshipAnalysis)
    {
        this.relationshipAnalysis = relationshipAnalysis;
    }

    public String getRelationshipAnalysis()
    {
        return relationshipAnalysis;
    }
    public void setAdvantages(String advantages)
    {
        this.advantages = advantages;
    }

    public String getAdvantages()
    {
        return advantages;
    }
    public void setChallenges(String challenges)
    {
        this.challenges = challenges;
    }

    public String getChallenges()
    {
        return challenges;
    }
    public void setRecommendations(String recommendations)
    {
        this.recommendations = recommendations;
    }

    public String getRecommendations()
    {
        return recommendations;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("compatibilityId", getCompatibilityId())
            .append("typeCode1", getTypeCode1())
            .append("typeCode2", getTypeCode2())
            .append("compatibilityScore", getCompatibilityScore())
            .append("relationshipAnalysis", getRelationshipAnalysis())
            .append("advantages", getAdvantages())
            .append("challenges", getChallenges())
            .append("recommendations", getRecommendations())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
