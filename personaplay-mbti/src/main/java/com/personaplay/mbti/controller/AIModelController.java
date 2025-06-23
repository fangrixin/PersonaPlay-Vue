package com.personaplay.mbti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.personaplay.common.annotation.Anonymous;
import com.personaplay.common.annotation.Log;
import com.personaplay.common.core.controller.BaseController;
import com.personaplay.common.core.domain.AjaxResult;
import com.personaplay.common.enums.BusinessType;
import com.personaplay.mbti.domain.vo.AIModelRequest;
import com.personaplay.mbti.domain.vo.AIModelResponse;
import com.personaplay.mbti.service.IAIModelService;

/**
 * AI模型控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/api/mbti/ai")
public class AIModelController extends BaseController {
    @Autowired
    private IAIModelService aiModelService;

    /**
     * 创建新会话
     */
    @GetMapping("/session")
    public AjaxResult createSession() {
        String sessionId = aiModelService.createSession();
        return AjaxResult.success("创建会话成功", sessionId);
    }

    /**
     * 生成文本
     */
    @PostMapping("/generate")
    @PreAuthorize("@ss.hasPermi('mbti:ai:generate')")
    @Log(title = "AI生成", businessType = BusinessType.OTHER)
    public AjaxResult generateText(@RequestBody AIModelRequest request) {
        AIModelResponse response = aiModelService.generateText(request);
        if (response.isSuccess()) {
            return AjaxResult.success("生成成功", response);
        } else {
            return AjaxResult.error(response.getError());
        }
    }

    /**
     * 生成性格分析
     */
    @GetMapping("/analyze/{mbtiType}")
    public AjaxResult generatePersonalityAnalysis(@PathVariable("mbtiType") String mbtiType) {
        AIModelResponse response = aiModelService.generatePersonalityAnalysis(mbtiType);
        if (response.isSuccess()) {
            return AjaxResult.success("生成成功", response);
        } else {
            return AjaxResult.error(response.getError());
        }
    }

    /**
     * 生成兼容性分析
     */
    @GetMapping("/compatibility/{type1}/{type2}")
    public AjaxResult generateCompatibilityAnalysis(
            @PathVariable("type1") String type1,
            @PathVariable("type2") String type2) {
        AIModelResponse response = aiModelService.generateCompatibilityAnalysis(type1, type2);
        if (response.isSuccess()) {
            return AjaxResult.success("生成成功", response);
        } else {
            return AjaxResult.error(response.getError());
        }
    }

    /**
     * 生成MBTI测试问题
     */
    @GetMapping("/questions/{dimension}/{count}")
    @PreAuthorize("@ss.hasPermi('mbti:ai:questions')")
    @Log(title = "生成MBTI问题", businessType = BusinessType.OTHER)
    public AjaxResult generateMbtiQuestions(
            @PathVariable("dimension") String dimension,
            @PathVariable("count") int count) {
        // 验证维度是否有效
        if (!isValidDimension(dimension)) {
            return AjaxResult.error("无效的MBTI维度，有效值：EI, SN, TF, JP");
        }

        // 验证数量是否在合理范围
        if (count < 1 || count > 50) {
            return AjaxResult.error("问题数量应在1-50之间");
        }

        AIModelResponse response = aiModelService.generateMbtiQuestions(dimension, count);
        if (response.isSuccess()) {
            return AjaxResult.success("生成成功", response);
        } else {
            return AjaxResult.error(response.getError());
        }
    }

    /**
     * 验证MBTI维度是否有效
     */
    private boolean isValidDimension(String dimension) {
        if (dimension == null || dimension.length() != 2) {
            return false;
        }

        return dimension.equalsIgnoreCase("EI") ||
               dimension.equalsIgnoreCase("SN") ||
               dimension.equalsIgnoreCase("TF") ||
               dimension.equalsIgnoreCase("JP");
    }

    /**
     * 调用AI模型生成文本（流式响应）
     */
    @Anonymous
    @PostMapping("/stream/generate")
    public SseEmitter generateTextStream(@RequestBody AIModelRequest request) {
        // 创建SseEmitter实例，设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        // 异步处理流式响应
        aiModelService.generateTextStream(request, emitter);

        return emitter;
    }

    /**
     * 根据MBTI类型生成性格分析（流式响应）
     */
    @Anonymous
    @GetMapping("/stream/personality/{mbtiType}")
    public SseEmitter generatePersonalityAnalysisStream(@PathVariable("mbtiType") String mbtiType) {
        // 创建SseEmitter实例，设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        // 异步处理流式响应
        aiModelService.generatePersonalityAnalysisStream(mbtiType, emitter);

        return emitter;
    }

    /**
     * 根据两个MBTI类型生成兼容性分析（流式响应）
     */
    @Anonymous
    @GetMapping("/stream/compatibility/{mbtiType1}/{mbtiType2}")
    public SseEmitter generateCompatibilityAnalysisStream(@PathVariable("mbtiType1") String mbtiType1,
                                                         @PathVariable("mbtiType2") String mbtiType2) {
        // 创建SseEmitter实例，设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        // 异步处理流式响应
        aiModelService.generateCompatibilityAnalysisStream(mbtiType1, mbtiType2, emitter);

        return emitter;
    }

    /**
     * 生成MBTI测试问题（流式响应）
     */
    @Anonymous
    @GetMapping("/stream/questions/{dimension}/{count}")
    public SseEmitter generateMbtiQuestionsStream(@PathVariable("dimension") String dimension,
                                                 @PathVariable("count") int count) {
        // 创建SseEmitter实例，设置超时时间为5分钟
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);

        // 异步处理流式响应
        aiModelService.generateMbtiQuestionsStream(dimension, count, emitter);

        return emitter;
    }
}
