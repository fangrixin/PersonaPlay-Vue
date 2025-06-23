package com.personaplay.mbti.service;

import com.personaplay.mbti.domain.vo.AIModelRequest;
import com.personaplay.mbti.domain.vo.AIModelResponse;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * AI模型服务接口
 *
 * @author ruoyi
 */
public interface IAIModelService {
    /**
     * 使用AI模型生成文本
     *
     * @param request AI模型请求
     * @return AI模型响应
     */
    AIModelResponse generateText(AIModelRequest request);

    /**
     * 使用AI模型生成文本（流式响应）
     *
     * @param request AI模型请求
     * @param emitter 服务器发送事件的发射器
     */
    void generateTextStream(AIModelRequest request, SseEmitter emitter);

    /**
     * 创建新会话
     *
     * @return 会话ID
     */
    String createSession();

    /**
     * 根据MBTI类型生成性格分析
     *
     * @param mbtiType MBTI类型，例如"INTJ"
     * @return 性格分析结果
     */
    AIModelResponse generatePersonalityAnalysis(String mbtiType);

    /**
     * 根据MBTI类型生成性格分析（流式响应）
     *
     * @param mbtiType MBTI类型，例如"INTJ"
     * @param emitter 服务器发送事件的发射器
     */
    void generatePersonalityAnalysisStream(String mbtiType, SseEmitter emitter);

    /**
     * 根据两个MBTI类型生成兼容性分析
     *
     * @param mbtiType1 第一个MBTI类型
     * @param mbtiType2 第二个MBTI类型
     * @return 兼容性分析结果
     */
    AIModelResponse generateCompatibilityAnalysis(String mbtiType1, String mbtiType2);

    /**
     * 根据两个MBTI类型生成兼容性分析（流式响应）
     *
     * @param mbtiType1 第一个MBTI类型
     * @param mbtiType2 第二个MBTI类型
     * @param emitter 服务器发送事件的发射器
     */
    void generateCompatibilityAnalysisStream(String mbtiType1, String mbtiType2, SseEmitter emitter);

    /**
     * 使用AI模型生成MBTI测试问题
     *
     * @param dimension MBTI维度(EI, SN, TF, JP)
     * @param count 需要生成的问题数量
     * @return 生成的测试问题
     */
    AIModelResponse generateMbtiQuestions(String dimension, int count);

    /**
     * 使用AI模型生成MBTI测试问题（流式响应）
     *
     * @param dimension MBTI维度(EI, SN, TF, JP)
     * @param count 需要生成的问题数量
     * @param emitter 服务器发送事件的发射器
     */
    void generateMbtiQuestionsStream(String dimension, int count, SseEmitter emitter);
}
