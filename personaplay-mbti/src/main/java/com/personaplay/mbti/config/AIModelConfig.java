package com.personaplay.mbti.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI大模型配置类
 *
 * @author ruoyi
 */
@Configuration
@ConfigurationProperties(prefix = "ai.model")
public class AIModelConfig {
    /** API密钥 */
    private String apiKey;

    /** API接口地址 */
    private String apiUrl;

    /** 模型名称 */
    private String modelName;

    /** 超时时间（毫秒） */
    private int timeout = 30000;

    /** 是否启用代理 */
    private boolean proxyEnabled = false;

    /** 代理主机 */
    private String proxyHost;

    /** 代理端口 */
    private int proxyPort;

    /** 流式响应输出块大小(字符)，0表示不限制 */
    private int chunkSize = 20;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
