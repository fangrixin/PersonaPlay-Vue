package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiCompatibility;

/**
 * MBTI类型兼容性Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiCompatibilityService
{
    /**
     * 查询MBTI类型兼容性
     *
     * @param compatibilityId MBTI类型兼容性主键
     * @return MBTI类型兼容性
     */
    public MbtiCompatibility selectMbtiCompatibilityByCompatibilityId(Long compatibilityId);

    /**
     * 查询MBTI类型兼容性列表
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return MBTI类型兼容性集合
     */
    public List<MbtiCompatibility> selectMbtiCompatibilityList(MbtiCompatibility mbtiCompatibility);

    /**
     * 新增MBTI类型兼容性
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return 结果
     */
    public int insertMbtiCompatibility(MbtiCompatibility mbtiCompatibility);

    /**
     * 修改MBTI类型兼容性
     *
     * @param mbtiCompatibility MBTI类型兼容性
     * @return 结果
     */
    public int updateMbtiCompatibility(MbtiCompatibility mbtiCompatibility);

    /**
     * 批量删除MBTI类型兼容性
     *
     * @param compatibilityIds 需要删除的MBTI类型兼容性主键集合
     * @return 结果
     */
    public int deleteMbtiCompatibilityByCompatibilityIds(Long[] compatibilityIds);

    /**
     * 删除MBTI类型兼容性信息
     *
     * @param compatibilityId MBTI类型兼容性主键
     * @return 结果
     */
    public int deleteMbtiCompatibilityByCompatibilityId(Long compatibilityId);
}
