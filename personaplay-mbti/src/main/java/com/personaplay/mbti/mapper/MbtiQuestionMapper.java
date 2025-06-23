package com.personaplay.mbti.mapper;

import java.util.List;
import com.personaplay.mbti.domain.MbtiQuestion;
import org.apache.ibatis.annotations.Param;

/**
 * MBTI测试问题Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiQuestionMapper
{
    /**
     * 查询MBTI测试问题
     *
     * @param questionId MBTI测试问题主键
     * @return MBTI测试问题
     */
    public MbtiQuestion selectMbtiQuestionByQuestionId(Long questionId);

    /**
     * 查询MBTI测试问题列表
     *
     * @param mbtiQuestion MBTI测试问题
     * @return MBTI测试问题集合
     */
    public List<MbtiQuestion> selectMbtiQuestionList(MbtiQuestion mbtiQuestion);

    /**
     * 新增MBTI测试问题
     *
     * @param mbtiQuestion MBTI测试问题
     * @return 结果
     */
    public int insertMbtiQuestion(MbtiQuestion mbtiQuestion);

    /**
     * 修改MBTI测试问题
     *
     * @param mbtiQuestion MBTI测试问题
     * @return 结果
     */
    public int updateMbtiQuestion(MbtiQuestion mbtiQuestion);

    /**
     * 删除MBTI测试问题
     *
     * @param questionId MBTI测试问题主键
     * @return 结果
     */
    public int deleteMbtiQuestionByQuestionId(Long questionId);

    /**
     * 批量删除MBTI测试问题
     *
     * @param questionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiQuestionByQuestionIds(Long[] questionIds);

    List<MbtiQuestion> selectRandomQuestionsByDimension(@Param("dimension") String dimension, @Param("limit") int limit);

}
