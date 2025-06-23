package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiQuestion;

/**
 * MBTI测试问题Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiQuestionService
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
     * 批量删除MBTI测试问题
     *
     * @param questionIds 需要删除的MBTI测试问题主键集合
     * @return 结果
     */
    public int deleteMbtiQuestionByQuestionIds(Long[] questionIds);

    /**
     * 删除MBTI测试问题信息
     *
     * @param questionId MBTI测试问题主键
     * @return 结果
     */
    public int deleteMbtiQuestionByQuestionId(Long questionId);

    public List<MbtiQuestion> selectSingleMbtiQuestionList(String versionCode);
}
