package com.personaplay.mbti.domain.vo;

import java.util.Map;

/**
 * AI模型响应对象
 *
 * @author ruoyi
 */
public class AIModelResponse {
    /** 响应状态，true表示成功，false表示失败 */
    private boolean success;

    /** 生成的文本内容 */
    private String content;

    /** 会话ID */
    private String sessionId;

    /** 消息ID */
    private String messageId;

    /** 错误信息，如果有的话 */
    private String error;

    /** 错误代码，如果有的话 */
    private String errorCode;

    /** 使用的token数量 */
    private Integer tokenUsage;

    /** 模型名称 */
    private String model;

    /** 其他元数据信息 */
    private Map<String, Object> metadata;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getTokenUsage() {
        return tokenUsage;
    }

    public void setTokenUsage(Integer tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
