package com.personaplay.mbti.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.personaplay.common.utils.DateUtils;
import com.personaplay.mbti.domain.MbtiTestVersion;
import com.personaplay.mbti.mapper.MbtiTestVersionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiQuestionMapper;
import com.personaplay.mbti.domain.MbtiQuestion;
import com.personaplay.mbti.service.IMbtiQuestionService;

/**
 * MBTI测试问题Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiQuestionServiceImpl implements IMbtiQuestionService
{
    @Autowired
    private MbtiQuestionMapper mbtiQuestionMapper;


    @Autowired
    private MbtiTestVersionMapper mbtiTestVersionMapper;

    private static final Logger log = LoggerFactory.getLogger(MbtiQuestionServiceImpl.class);
    /**
     * 查询MBTI测试问题
     *
     * @param questionId MBTI测试问题主键
     * @return MBTI测试问题
     */
    @Override
    public MbtiQuestion selectMbtiQuestionByQuestionId(Long questionId)
    {
        return mbtiQuestionMapper.selectMbtiQuestionByQuestionId(questionId);
    }

    /**
     * 查询MBTI测试问题列表
     *
     * @param mbtiQuestion MBTI测试问题
     * @return MBTI测试问题
     */
    @Override
    public List<MbtiQuestion> selectMbtiQuestionList(MbtiQuestion mbtiQuestion)
    {
        return mbtiQuestionMapper.selectMbtiQuestionList(mbtiQuestion);
    }

    /**
     * 新增MBTI测试问题
     *
     * @param mbtiQuestion MBTI测试问题
     * @return 结果
     */
    @Override
    public int insertMbtiQuestion(MbtiQuestion mbtiQuestion)
    {
        mbtiQuestion.setCreateTime(DateUtils.getNowDate());
        return mbtiQuestionMapper.insertMbtiQuestion(mbtiQuestion);
    }

    /**
     * 修改MBTI测试问题
     *
     * @param mbtiQuestion MBTI测试问题
     * @return 结果
     */
    @Override
    public int updateMbtiQuestion(MbtiQuestion mbtiQuestion)
    {
        mbtiQuestion.setUpdateTime(DateUtils.getNowDate());
        return mbtiQuestionMapper.updateMbtiQuestion(mbtiQuestion);
    }

    /**
     * 批量删除MBTI测试问题
     *
     * @param questionIds 需要删除的MBTI测试问题主键
     * @return 结果
     */
    @Override
    public int deleteMbtiQuestionByQuestionIds(Long[] questionIds)
    {
        return mbtiQuestionMapper.deleteMbtiQuestionByQuestionIds(questionIds);
    }

    /**
     * 删除MBTI测试问题信息
     *
     * @param questionId MBTI测试问题主键
     * @return 结果
     */
    @Override
    public int deleteMbtiQuestionByQuestionId(Long questionId)
    {
        return mbtiQuestionMapper.deleteMbtiQuestionByQuestionId(questionId);
    }

    @Override
    public List<MbtiQuestion> selectSingleMbtiQuestionList(String versionCode) {
        MbtiTestVersion mbtiTestVersionParam = new MbtiTestVersion();
        mbtiTestVersionParam.setVersionCode(versionCode);
        List<MbtiTestVersion> mbtiTestVersions = mbtiTestVersionMapper.selectMbtiTestVersionList(mbtiTestVersionParam);
        MbtiTestVersion mbtiTestVersion = new MbtiTestVersion();
        if(mbtiTestVersions != null && mbtiTestVersions.size() > 0){
            mbtiTestVersion = mbtiTestVersions.get(0);
        }
        String questions = null;
        long totalQuestionNum = 0;
        if(mbtiTestVersion != null){
            // 获取总题目数
            totalQuestionNum = mbtiTestVersion.getQuestionCount();
            // 每个维度的题目数
            int questionsPerDimension = (int)(totalQuestionNum / 4L);
            // 获取每个维度的题目并随机抽取
            List<MbtiQuestion> eiQuestions = mbtiQuestionMapper.selectRandomQuestionsByDimension("EI", questionsPerDimension);
            List<MbtiQuestion> snQuestions = mbtiQuestionMapper.selectRandomQuestionsByDimension("SN", questionsPerDimension);
            List<MbtiQuestion> tfQuestions = mbtiQuestionMapper.selectRandomQuestionsByDimension("TF", questionsPerDimension);
            List<MbtiQuestion> jpQuestions = mbtiQuestionMapper.selectRandomQuestionsByDimension("JP", questionsPerDimension);
            // 合并所有题目并打乱顺序
            List<MbtiQuestion> mbtiQuestionList = new ArrayList<>();
            mbtiQuestionList.addAll(eiQuestions);
            mbtiQuestionList.addAll(snQuestions);
            mbtiQuestionList.addAll(tfQuestions);
            mbtiQuestionList.addAll(jpQuestions);
            Collections.shuffle(mbtiQuestionList);
            // 转换为JSON字符串
//            questions = JSON.toJSONString(mbtiQuestionList);
            // 打印日志
            log.info("NewQuestions: {}", questions);
            return mbtiQuestionList;
        }
        return null;
    }
}
