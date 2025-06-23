package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiTestAnswerMapper;
import com.personaplay.mbti.domain.MbtiTestAnswer;
import com.personaplay.mbti.service.IMbtiTestAnswerService;

/**
 * 测试答题记录Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiTestAnswerServiceImpl implements IMbtiTestAnswerService
{
    @Autowired
    private MbtiTestAnswerMapper mbtiTestAnswerMapper;

    /**
     * 查询测试答题记录
     *
     * @param answerId 测试答题记录主键
     * @return 测试答题记录
     */
    @Override
    public MbtiTestAnswer selectMbtiTestAnswerByAnswerId(Long answerId)
    {
        return mbtiTestAnswerMapper.selectMbtiTestAnswerByAnswerId(answerId);
    }

    /**
     * 查询测试答题记录列表
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 测试答题记录
     */
    @Override
    public List<MbtiTestAnswer> selectMbtiTestAnswerList(MbtiTestAnswer mbtiTestAnswer)
    {
        return mbtiTestAnswerMapper.selectMbtiTestAnswerList(mbtiTestAnswer);
    }

    /**
     * 新增测试答题记录
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 结果
     */
    @Override
    public int insertMbtiTestAnswer(MbtiTestAnswer mbtiTestAnswer)
    {
        mbtiTestAnswer.setCreateTime(DateUtils.getNowDate());
        return mbtiTestAnswerMapper.insertMbtiTestAnswer(mbtiTestAnswer);
    }

    /**
     * 修改测试答题记录
     *
     * @param mbtiTestAnswer 测试答题记录
     * @return 结果
     */
    @Override
    public int updateMbtiTestAnswer(MbtiTestAnswer mbtiTestAnswer)
    {
        mbtiTestAnswer.setUpdateTime(DateUtils.getNowDate());
        return mbtiTestAnswerMapper.updateMbtiTestAnswer(mbtiTestAnswer);
    }

    /**
     * 批量删除测试答题记录
     *
     * @param answerIds 需要删除的测试答题记录主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestAnswerByAnswerIds(Long[] answerIds)
    {
        return mbtiTestAnswerMapper.deleteMbtiTestAnswerByAnswerIds(answerIds);
    }

    /**
     * 删除测试答题记录信息
     *
     * @param answerId 测试答题记录主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestAnswerByAnswerId(Long answerId)
    {
        return mbtiTestAnswerMapper.deleteMbtiTestAnswerByAnswerId(answerId);
    }
}
