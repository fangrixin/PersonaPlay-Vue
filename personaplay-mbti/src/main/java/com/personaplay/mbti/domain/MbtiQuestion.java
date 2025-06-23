package com.personaplay.mbti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI测试问题对象 mbti_question
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiQuestion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 问题ID */
    private Long questionId;

    /** 问题内容 */
    @Excel(name = "问题内容")
    private String questionText;

    /** 维度(E/I, S/N, T/F, J/P) */
    @Excel(name = "维度(E/I, S/N, T/F, J/P)")
    private String dimension;

    /** 选项A */
    @Excel(name = "选项A")
    private String optionA;

    /** 选项B */
    @Excel(name = "选项B")
    private String optionB;

    /** 选项A对应的维度值(如E、S、T、J) */
    @Excel(name = "选项A对应的维度值(如E、S、T、J)")
    private String optionAValue;

    /** 选项B对应的维度值(如I、N、F、P) */
    @Excel(name = "选项B对应的维度值(如I、N、F、P)")
    private String optionBValue;

    /** 问题权重 */
    @Excel(name = "问题权重")
    private Long weight;

    /** 适用的测试版本代码(多个用逗号分隔) */
    @Excel(name = "适用的测试版本代码(多个用逗号分隔)")
    private String versionCodes;

    /** 题目顺序 */
    @Excel(name = "题目顺序")
    private Long sequence;

    /** 状态(0启用 1禁用) */
    @Excel(name = "状态(0启用 1禁用)")
    private String status;

    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public Long getQuestionId()
    {
        return questionId;
    }
    public void setQuestionText(String questionText)
    {
        this.questionText = questionText;
    }

    public String getQuestionText()
    {
        return questionText;
    }
    public void setDimension(String dimension)
    {
        this.dimension = dimension;
    }

    public String getDimension()
    {
        return dimension;
    }
    public void setOptionA(String optionA)
    {
        this.optionA = optionA;
    }

    public String getOptionA()
    {
        return optionA;
    }
    public void setOptionB(String optionB)
    {
        this.optionB = optionB;
    }

    public String getOptionB()
    {
        return optionB;
    }
    public void setOptionAValue(String optionAValue)
    {
        this.optionAValue = optionAValue;
    }

    public String getOptionAValue()
    {
        return optionAValue;
    }
    public void setOptionBValue(String optionBValue)
    {
        this.optionBValue = optionBValue;
    }

    public String getOptionBValue()
    {
        return optionBValue;
    }
    public void setWeight(Long weight)
    {
        this.weight = weight;
    }

    public Long getWeight()
    {
        return weight;
    }
    public void setVersionCodes(String versionCodes)
    {
        this.versionCodes = versionCodes;
    }

    public String getVersionCodes()
    {
        return versionCodes;
    }
    public void setSequence(Long sequence)
    {
        this.sequence = sequence;
    }

    public Long getSequence()
    {
        return sequence;
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
            .append("questionId", getQuestionId())
            .append("questionText", getQuestionText())
            .append("dimension", getDimension())
            .append("optionA", getOptionA())
            .append("optionB", getOptionB())
            .append("optionAValue", getOptionAValue())
            .append("optionBValue", getOptionBValue())
            .append("weight", getWeight())
            .append("versionCodes", getVersionCodes())
            .append("sequence", getSequence())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
