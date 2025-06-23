package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiTestAnswer;

/**
 * 测试答题记录Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiTestAnswerService
{
    /**
     * 查询测试答题记录
     *
     * @param answerId 测试答题记录主键
     * @return 测试答题记录
     */
    public MbtiTestAnswer selectMbtiTestAnswerByAnswerId(Long answerId);

    /**
     * 查询测试答题记录列表
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 测试答题记录集合
     */
    public List<MbtiTestAnswer> selectMbtiTestAnswerList(MbtiTestAnswer mbtiTestAnswer);

    /**
     * 新增测试答题记录
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 结果
     */
    public int insertMbtiTestAnswer(MbtiTestAnswer mbtiTestAnswer);

    /**
     * 修改测试答题记录
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 结果
     */
    public int updateMbtiTestAnswer(MbtiTestAnswer mbtiTestAnswer);

    /**
     * 批量删除测试答题记录
     *
     * @param answerIds 需要删除的测试答题记录主键集合
     * @return 结果
     */
    public int deleteMbtiTestAnswerByAnswerIds(Long[] answerIds);

    /**
     * 删除测试答题记录信息
     *
     * @param answerId 测试答题记录主键
     * @return 结果
     */
    public int deleteMbtiTestAnswerByAnswerId(Long answerId);
}
