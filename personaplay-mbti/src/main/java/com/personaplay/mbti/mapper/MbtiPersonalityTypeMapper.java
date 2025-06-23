package com.personaplay.mbti.mapper;

import java.util.List;
import com.personaplay.mbti.domain.MbtiPersonalityType;

/**
 * MBTI人格类型Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiPersonalityTypeMapper
{
    /**
     * 查询MBTI人格类型
     *
     * @param typeId MBTI人格类型主键
     * @return MBTI人格类型
     */
    public MbtiPersonalityType selectMbtiPersonalityTypeByTypeId(Long typeId);

    /**
     * 查询MBTI人格类型列表
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return MBTI人格类型集合
     */
    public List<MbtiPersonalityType> selectMbtiPersonalityTypeList(MbtiPersonalityType mbtiPersonalityType);

    /**
     * 新增MBTI人格类型
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return 结果
     */
    public int insertMbtiPersonalityType(MbtiPersonalityType mbtiPersonalityType);

    /**
     * 修改MBTI人格类型
     *
     * @param mbtiPersonalityType MBTI人格类型
     * @return 结果
     */
    public int updateMbtiPersonalityType(MbtiPersonalityType mbtiPersonalityType);

    /**
     * 删除MBTI人格类型
     *
     * @param typeId MBTI人格类型主键
     * @return 结果
     */
    public int deleteMbtiPersonalityTypeByTypeId(Long typeId);

    /**
     * 批量删除MBTI人格类型
     *
     * @param typeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiPersonalityTypeByTypeIds(Long[] typeIds);
}
