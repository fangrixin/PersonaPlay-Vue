package com.personaplay.mbti.domain.vo;

import java.util.List;
import java.util.Map;

/**
 * AI模型请求对象
 *
 * @author ruoyi
 */
public class AIModelRequest {
    /** 用户输入的问题/提示 */
    private String prompt;

    /** 会话ID，用于保持上下文连续性 */
    private String sessionId;

    /** 模型名称，如果为空则使用配置的默认模型 */
    private String model;

    /** 温度参数，控制生成文本的随机性，0-2之间，值越大越随机 */
    private Float temperature;

    /** 最大生成token数量 */
    private Integer maxTokens;

    /** 历史消息，用于保持会话上下文 */
    private List<Map<String, String>> history;

    /** 系统提示，用于设置AI的行为和角色 */
    private String systemPrompt;

    /** 其他参数，供不同模型使用 */
    private Map<String, Object> extraParams;

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }

    public List<Map<String, String>> getHistory() {
        return history;
    }

    public void setHistory(List<Map<String, String>> history) {
        this.history = history;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public Map<String, Object> getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(Map<String, Object> extraParams) {
        this.extraParams = extraParams;
    }
}
