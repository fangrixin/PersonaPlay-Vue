package com.personaplay.mbti.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.personaplay.common.annotation.Excel;
import com.personaplay.common.core.domain.BaseEntity;

/**
 * MBTI人格类型对象 mbti_personality_type
 *
 * @author fangrx
 * @date 2025-03-24
 */
public class MbtiPersonalityType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 类型ID */
    private Long typeId;

    /** MBTI类型代码(如ENFP) */
    @Excel(name = "MBTI类型代码(如ENFP)")
    private String typeCode;

    /** 类型名称(如活力型人格) */
    @Excel(name = "类型名称(如活力型人格)")
    private String typeName;

    /** 类型详细描述 */
    @Excel(name = "类型详细描述")
    private String description;

    /** 性格特点 */
    @Excel(name = "性格特点")
    private String characteristics;

    /** 优势 */
    @Excel(name = "优势")
    private String strengths;

    /** 弱点 */
    @Excel(name = "弱点")
    private String weaknesses;

    /** 职业建议 */
    @Excel(name = "职业建议")
    private String careerSuggestions;

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Long getTypeId()
    {
        return typeId;
    }
    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    public String getTypeCode()
    {
        return typeCode;
    }
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    public void setCharacteristics(String characteristics)
    {
        this.characteristics = characteristics;
    }

    public String getCharacteristics()
    {
        return characteristics;
    }
    public void setStrengths(String strengths)
    {
        this.strengths = strengths;
    }

    public String getStrengths()
    {
        return strengths;
    }
    public void setWeaknesses(String weaknesses)
    {
        this.weaknesses = weaknesses;
    }

    public String getWeaknesses()
    {
        return weaknesses;
    }
    public void setCareerSuggestions(String careerSuggestions)
    {
        this.careerSuggestions = careerSuggestions;
    }

    public String getCareerSuggestions()
    {
        return careerSuggestions;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("typeCode", getTypeCode())
            .append("typeName", getTypeName())
            .append("description", getDescription())
            .append("characteristics", getCharacteristics())
            .append("strengths", getStrengths())
            .append("weaknesses", getWeaknesses())
            .append("careerSuggestions", getCareerSuggestions())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
