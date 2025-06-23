package com.personaplay.mbti.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * 测试答题记录对象 mbti_test_answer
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiTestAnswer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 答题ID */
    private Long answerId;

    /** 测试记录ID */
    @Excel(name = "测试记录ID")
    private Long recordId;

    /** 问题ID */
    @Excel(name = "问题ID")
    private Long questionId;

    /** 用户答案(A或B) */
    @Excel(name = "用户答案(A或B)")
    private String userAnswer;

    /** 对应的维度值(如E、I、S、N等) */
    @Excel(name = "对应的维度值(如E、I、S、N等)")
    private String dimensionValue;

    /** 答题时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "答题时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date answerTime;

    /** 答题用时(秒) */
    @Excel(name = "答题用时(秒)")
    private Long timeSpent;

    /** 题目序号 */
    @Excel(name = "题目序号")
    private Long sequence;

    public void setAnswerId(Long answerId)
    {
        this.answerId = answerId;
    }

    public Long getAnswerId()
    {
        return answerId;
    }
    public void setRecordId(Long recordId)
    {
        this.recordId = recordId;
    }

    public Long getRecordId()
    {
        return recordId;
    }
    public void setQuestionId(Long questionId)
    {
        this.questionId = questionId;
    }

    public Long getQuestionId()
    {
        return questionId;
    }
    public void setUserAnswer(String userAnswer)
    {
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer()
    {
        return userAnswer;
    }
    public void setDimensionValue(String dimensionValue)
    {
        this.dimensionValue = dimensionValue;
    }

    public String getDimensionValue()
    {
        return dimensionValue;
    }
    public void setAnswerTime(Date answerTime)
    {
        this.answerTime = answerTime;
    }

    public Date getAnswerTime()
    {
        return answerTime;
    }
    public void setTimeSpent(Long timeSpent)
    {
        this.timeSpent = timeSpent;
    }

    public Long getTimeSpent()
    {
        return timeSpent;
    }
    public void setSequence(Long sequence)
    {
        this.sequence = sequence;
    }

    public Long getSequence()
    {
        return sequence;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("answerId", getAnswerId())
            .append("recordId", getRecordId())
            .append("questionId", getQuestionId())
            .append("userAnswer", getUserAnswer())
            .append("dimensionValue", getDimensionValue())
            .append("answerTime", getAnswerTime())
            .append("timeSpent", getTimeSpent())
            .append("sequence", getSequence())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
