package com.personaplay.mbti.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.personaplay.mbti.config.AIModelConfig;
import com.personaplay.mbti.domain.vo.AIModelRequest;
import com.personaplay.mbti.domain.vo.AIModelResponse;
import com.personaplay.mbti.service.IAIModelService;

/**
 * AI模型服务实现类
 *
 * @author ruoyi
 */
@Service
public class AIModelServiceImpl implements IAIModelService {
    private static final Logger log = LoggerFactory.getLogger(AIModelServiceImpl.class);

    @Autowired
    public AIModelConfig config;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 使用AI模型生成文本
     *
     * @param request AI模型请求
     * @return AI模型响应
     */
    @Override
    public AIModelResponse generateText(AIModelRequest request) {
        AIModelResponse response = new AIModelResponse();

        try {
            // 创建HttpClient和配置
            CloseableHttpClient httpClient = createHttpClient();
            HttpPost httpPost = createHttpPost();

            // 构建请求体
            Map<String, Object> requestBody = buildRequestBody(request);
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // 日志记录请求内容（可在生产环境中移除或屏蔽敏感信息）
            log.info("发送API请求体: {}", jsonBody);

            // 设置请求体
            StringEntity entity = new StringEntity(jsonBody, "UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            // 执行请求
            log.info("开始执行API请求");
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

            try {
                // 获取响应状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                log.info("API响应状态码: {}", statusCode);

                // 处理响应
                HttpEntity responseEntity = httpResponse.getEntity();
                String jsonResponse = EntityUtils.toString(responseEntity);

                // 日志记录响应内容（可屏蔽敏感信息）
                log.info("收到API响应: {}", jsonResponse);

                // 非成功状态码处理
                if (statusCode < 200 || statusCode >= 300) {
                    log.error("API调用失败，状态码: {}，响应: {}", statusCode, jsonResponse);
                    response.setSuccess(false);
                    response.setError("API调用失败，状态码: " + statusCode);
                    response.setErrorCode("HTTP_ERROR_" + statusCode);
                    return response;
                }

                // 解析响应
                parseResponse(jsonResponse, response);

                // 记录token使用情况
                log.info("AI模型调用成功，使用token数量：{}", response.getTokenUsage());

            } finally {
                httpResponse.close();
                httpClient.close();
            }
        } catch (Exception e) {
            log.error("调用AI模型出错：", e);
            response.setSuccess(false);
            response.setError(e.getMessage());
            response.setErrorCode("API_ERROR");
        }

        return response;
    }

    /**
     * 创建HttpClient
     */
    private CloseableHttpClient createHttpClient() {
        // 如果启用了代理，则配置代理
        if (config.isProxyEnabled() && config.getProxyHost() != null && !config.getProxyHost().isEmpty()) {
            HttpHost proxy = new HttpHost(config.getProxyHost(), config.getProxyPort());
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getTimeout())
                    .setSocketTimeout(config.getTimeout())
                    .setProxy(proxy)
                    .build();

            return HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        } else {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(config.getTimeout())
                    .setSocketTimeout(config.getTimeout())
                    .build();

            return HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        }
    }

    /**
     * 创建HttpPost
     */
    private HttpPost createHttpPost() {
        HttpPost httpPost = new HttpPost(config.getApiUrl());
        httpPost.setHeader("Authorization", "Bearer " + config.getApiKey());
        httpPost.setHeader("Content-Type", "application/json");
        // 添加Accept头，明确接受JSON响应
        httpPost.setHeader("Accept", "application/json");
        log.info("API请求URL: {}", config.getApiUrl());
        return httpPost;
    }

    /**
     * 构建请求体
     */
    private Map<String, Object> buildRequestBody(AIModelRequest request) {
        Map<String, Object> requestBody = new HashMap<>();

        // 设置模型名称
        requestBody.put("model", request.getModel() != null ? request.getModel() : config.getModelName());

        // 构建消息数组
        List<Map<String, String>> messages = new ArrayList<>();

        // 添加系统提示
        if (request.getSystemPrompt() != null && !request.getSystemPrompt().isEmpty()) {
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", request.getSystemPrompt());
            messages.add(systemMessage);
        }

        // 添加历史消息
        if (request.getHistory() != null && !request.getHistory().isEmpty()) {
            messages.addAll(request.getHistory());
        }

        // 添加当前用户消息
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", request.getPrompt());
        messages.add(userMessage);

        requestBody.put("messages", messages);

        // 设置其他参数
        if (request.getTemperature() != null) {
            requestBody.put("temperature", request.getTemperature());
        }

        if (request.getMaxTokens() != null) {
            requestBody.put("max_tokens", request.getMaxTokens());
        }

        // 添加额外参数
        if (request.getExtraParams() != null) {
            requestBody.putAll(request.getExtraParams());
        }

        return requestBody;
    }

    /**
     * 解析API响应
     */
    private void parseResponse(String jsonResponse, AIModelResponse response) throws IOException {
        Map<String, Object> jsonMap = objectMapper.readValue(jsonResponse, Map.class);

        // 设置响应字段
        response.setSuccess(true);

        // 提取内容，不同API可能有不同的响应结构，这里以OpenAI为例
        if (jsonMap.containsKey("choices") && ((List)jsonMap.get("choices")).size() > 0) {
            Map<String, Object> choice = (Map<String, Object>) ((List)jsonMap.get("choices")).get(0);

            if (choice.containsKey("message")) {
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                response.setContent((String) message.get("content"));
            } else {
                response.setContent((String) choice.get("text"));
            }
        }

        // 提取元数据
        if (jsonMap.containsKey("model")) {
            response.setModel((String) jsonMap.get("model"));
        }

        if (jsonMap.containsKey("id")) {
            response.setMessageId((String) jsonMap.get("id"));
        }

        // 提取token使用情况
        if (jsonMap.containsKey("usage")) {
            Map<String, Object> usage = (Map<String, Object>) jsonMap.get("usage");

            if (usage.containsKey("total_tokens")) {
                response.setTokenUsage(((Number) usage.get("total_tokens")).intValue());
            }
        }

        // 设置其他元数据
        Map<String, Object> metadata = new HashMap<>();

        if (jsonMap.containsKey("created")) {
            metadata.put("created", jsonMap.get("created"));
        }

        if (jsonMap.containsKey("system_fingerprint")) {
            metadata.put("system_fingerprint", jsonMap.get("system_fingerprint"));
        }

        response.setMetadata(metadata);
    }

    /**
     * 创建新会话
     *
     * @return 会话ID
     */
    @Override
    public String createSession() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据MBTI类型生成性格分析
     *
     * @param mbtiType MBTI类型，例如"INTJ"
     * @return 性格分析结果
     */
    @Override
    public AIModelResponse generatePersonalityAnalysis(String mbtiType) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        request.setSystemPrompt("你是一个MBTI性格分析专家，请根据用户提供的MBTI类型提供详细的性格分析，包括优势、劣势、职业建议、人际关系建议等方面。请以客观、专业的语气回答，不要过于笼统。");

        // 设置用户提示
        request.setPrompt("请详细分析" + mbtiType + "性格类型的特点，包括：\n1. 核心特质和行为模式\n2. 优势和潜力\n3. 可能的盲点和成长方向\n4. 适合的职业领域\n5. 人际关系和沟通模式\n请以通俗易懂的语言进行分析，避免过于专业的术语。");

        // 设置温度
        request.setTemperature(0.7f);

        // 调用API
        return generateText(request);
    }

    /**
     * 根据两个MBTI类型生成兼容性分析
     *
     * @param mbtiType1 第一个MBTI类型
     * @param mbtiType2 第二个MBTI类型
     * @return 兼容性分析结果
     */
    @Override
    public AIModelResponse generateCompatibilityAnalysis(String mbtiType1, String mbtiType2) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        request.setSystemPrompt("你是一个MBTI关系兼容性分析专家，请根据用户提供的两种MBTI类型进行详细的兼容性分析，包括共同点、差异点、潜在冲突、沟通建议等方面。分析应当全面且具体。");

        // 设置用户提示
        request.setPrompt("请详细分析" + mbtiType1 + "和" + mbtiType2 + "两种性格类型的兼容性，包括：\n1. 互补与相似之处\n2. 可能产生的冲突点\n3. 沟通与理解的建议\n4. 作为朋友/恋人的相处模式\n5. 双方需要注意的关系盲点\n请从心理学角度进行分析，提供实用的建议。");

        // 设置温度
        request.setTemperature(0.7f);

        // 调用API
        return generateText(request);
    }

    /**
     * 使用AI模型生成MBTI测试问题
     *
     * @param dimension MBTI维度(EI, SN, TF, JP)
     * @param count 需要生成的问题数量
     * @return 生成的测试问题
     */
    @Override
    public AIModelResponse generateMbtiQuestions(String dimension, int count) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        String dimensionFullName = getDimensionFullName(dimension);
        request.setSystemPrompt("你是一个MBTI测试题设计专家，擅长设计能够区分不同性格维度的测试题目。请确保问题具有区分度，并且贴近日常生活场景。");

        // 设置用户提示
        request.setPrompt("请生成" + count + "道用于测试" + dimensionFullName + "(" + dimension + ")的MBTI题目。每个题目应当包含:\n1. 题干(真实生活中的情景)\n2. A选项(对应" + dimension.charAt(0) + "类型的选择)\n3. B选项(对应" + dimension.charAt(1) + "类型的选择)\n\n要求：\n1. 题目应该有区分度，能够明确区分两种倾向\n2. 题目应贴近生活，容易理解\n3. 避免使用专业术语\n4. 避免过于抽象的表述\n\n请以JSON格式返回题目，格式如下:\n```json\n[\n  {\n    \"question\": \"题干内容\",\n    \"optionA\": \"A选项内容\",\n    \"optionB\": \"B选项内容\",\n    \"dimensionA\": \"" + dimension.charAt(0) + "\",\n    \"dimensionB\": \"" + dimension.charAt(1) + "\"\n  }\n]\n```");

        // 设置温度
        request.setTemperature(0.8f);

        // 调用API
        return generateText(request);
    }

    /**
     * 获取维度的全名
     */
    private String getDimensionFullName(String dimension) {
        switch (dimension.toUpperCase()) {
            case "EI":
                return "外向与内向";
            case "SN":
                return "感觉与直觉";
            case "TF":
                return "思考与情感";
            case "JP":
                return "判断与感知";
            default:
                return "未知维度";
        }
    }

    /**
     * 使用AI模型生成文本（流式响应）
     *
     * @param request AI模型请求
     * @param emitter 服务器发送事件的发射器
     */
    @Override
    public void generateTextStream(AIModelRequest request, SseEmitter emitter) {
        // 使用线程池执行异步请求
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                // 创建HttpClient和配置
                CloseableHttpClient httpClient = createHttpClient();
                HttpPost httpPost = createHttpPost();

                // 构建请求体并添加流式处理参数
                Map<String, Object> requestBody = buildRequestBody(request);
                requestBody.put("stream", true);  // 启用流式响应

                String jsonBody = objectMapper.writeValueAsString(requestBody);

                // 设置请求体
                StringEntity entity = new StringEntity(jsonBody, "UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);

                // 执行请求
                CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

                try {
                    // 处理流式响应
                    HttpEntity responseEntity = httpResponse.getEntity();

                    // OpenAI流式响应是以"data: "开头的多行文本，每行是一个JSON对象
                    StringBuilder buffer = new StringBuilder();
                    StringBuilder contentBuffer = new StringBuilder();
                    int tokenCount = 0;

                    // 读取响应内容
                    byte[] data = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = responseEntity.getContent().read(data)) != -1) {
                        String chunk = new String(data, 0, bytesRead, "UTF-8");
                        buffer.append(chunk);

                        // 处理接收到的数据
                        tokenCount = processStreamChunks(buffer, contentBuffer, emitter, tokenCount);
                    }

                    // 发送完成信号 - 只发送一种完成信号，不包含内容，避免重复
                    Map<String, Object> completeData = new HashMap<>();
                    completeData.put("type", "AI_COMPLETE");
                    completeData.put("tokenUsage", tokenCount);
                    completeData.put("message", "分析已完成");

                    log.info("发送完成事件: 最终token数={}", tokenCount);

                    // 发送完成标记
                    emitter.send("COMPLETE:" + tokenCount);

                    // 完成
                    emitter.complete();

                    log.info("AI模型流式调用完成，使用token数量：{}", tokenCount);

                } catch (Exception e) {
                    log.error("处理流式响应出错：", e);
                    sendErrorEvent(emitter, e.getMessage());
                } finally {
                    httpResponse.close();
                    httpClient.close();
                }

            } catch (Exception e) {
                log.error("调用AI模型流式接口出错：", e);
                sendErrorEvent(emitter, e.getMessage());
            } finally {
                executor.shutdown();
            }
        });
    }

    /**
     * 处理流式响应的数据块
     */
    private int processStreamChunks(StringBuilder buffer, StringBuilder contentBuffer,
                                    SseEmitter emitter, int tokenCount) throws IOException {
        // 查找完整的行
        int pos;
        while ((pos = buffer.indexOf("\n")) != -1) {
            String line = buffer.substring(0, pos);
            buffer.delete(0, pos + 1);

            // 忽略空行
            if (line.trim().isEmpty()) {
                continue;
            }

            // 处理数据行
            if (line.startsWith("data: ")) {
                String jsonData = line.substring(6);

                // 处理结束标记
                if ("[DONE]".equals(jsonData)) {
                    return tokenCount;
                }

                try {
                    // 解析JSON响应
                    Map<String, Object> chunk = objectMapper.readValue(jsonData, Map.class);

                    // 提取内容
                    if (chunk.containsKey("choices") && ((List)chunk.get("choices")).size() > 0) {
                        Map<String, Object> choice = (Map<String, Object>) ((List)chunk.get("choices")).get(0);

                        if (choice.containsKey("delta")) {
                            Map<String, Object> delta = (Map<String, Object>) choice.get("delta");

                            if (delta.containsKey("content")) {
                                String content = (String) delta.get("content");

                                // 如果配置了块大小，则进行内容分块
                                int chunkSize = config.getChunkSize();
                                if (chunkSize > 0 && content.length() > chunkSize) {
                                    // 将内容分块发送，确保前端能够增量显示
                                    for (int i = 0; i < content.length(); i += chunkSize) {
                                        int endIndex = Math.min(i + chunkSize, content.length());
                                        String subContent = content.substring(i, endIndex);

                                        emitter.send(subContent);
                                        contentBuffer.append(subContent);

                                        log.debug("发送分块内容 [{}]: {}", i/chunkSize + 1, subContent);

                                        // 添加短暂延迟，使前端有时间处理
                                        try {
                                            Thread.sleep(30);
                                        } catch (InterruptedException ie) {
                                            Thread.currentThread().interrupt();
                                        }
                                    }
                                } else {
                                    // 直接发送较小的内容块
                                    contentBuffer.append(content);

                                    // 检查emitter是否已经完成
                                    try {
                                        emitter.send(content);
//                                        log.debug("发送流式内容片段 [长度:{}]", content.length());
                                    } catch (IllegalStateException e) {
                                        // 如果emitter已经完成，则不再发送
                                        log.warn("SseEmitter已经完成，跳过发送: {}", e.getMessage());
                                    }
                                }
                            }
                        }
                    }

                    // 更新token计数
                    if (chunk.containsKey("usage")) {
                        Map<String, Object> usage = (Map<String, Object>) chunk.get("usage");
                        if (usage.containsKey("total_tokens")) {
                            tokenCount = ((Number) usage.get("total_tokens")).intValue();
                        }
                    }

                } catch (Exception e) {
                    log.error("解析流式响应数据出错: {}", e.getMessage());
                }
            }
        }
        return tokenCount;
    }

    /**
     * 发送错误事件
     */
    private void sendErrorEvent(SseEmitter emitter, String errorMessage) {
        try {
            // 方式1: 使用Map发送结构化错误
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("type", "ERROR");
            errorData.put("message", errorMessage);
            errorData.put("timestamp", System.currentTimeMillis());
            emitter.send(errorData);

            // 方式2: 发送简单的错误消息
            emitter.send("ERROR: " + errorMessage);

            // 方式3: 发送本地化错误消息
            emitter.send("错误信息: " + errorMessage);

            // 记录错误
            log.error("AI分析错误: {}", errorMessage);

            // 完成发射器
            emitter.complete();
        } catch (Exception e) {
            log.error("发送错误事件失败: ", e);
        }
    }

    /**
     * 根据MBTI类型生成性格分析（流式响应）
     *
     * @param mbtiType MBTI类型，例如"INTJ"
     * @param emitter 服务器发送事件的发射器
     */
    @Override
    public void generatePersonalityAnalysisStream(String mbtiType, SseEmitter emitter) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        request.setSystemPrompt("你是一个MBTI性格分析专家，请根据用户提供的MBTI类型提供详细的性格分析，包括优势、劣势、职业建议、人际关系建议等方面。请以客观、专业的语气回答，不要过于笼统。");

        // 设置用户提示
        request.setPrompt("请详细分析" + mbtiType + "性格类型的特点，包括：\n1. 核心特质和行为模式\n2. 优势和潜力\n3. 可能的盲点和成长方向\n4. 适合的职业领域\n5. 人际关系和沟通模式\n请以通俗易懂的语言进行分析，避免过于专业的术语。");

        // 设置温度
        request.setTemperature(0.7f);

        // 调用流式API
        generateTextStream(request, emitter);
    }

    /**
     * 根据两个MBTI类型生成兼容性分析（流式响应）
     *
     * @param mbtiType1 第一个MBTI类型
     * @param mbtiType2 第二个MBTI类型
     * @param emitter 服务器发送事件的发射器
     */
    @Override
    public void generateCompatibilityAnalysisStream(String mbtiType1, String mbtiType2, SseEmitter emitter) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        request.setSystemPrompt("你是一个MBTI关系兼容性分析专家，请根据用户提供的两种MBTI类型进行详细的兼容性分析，包括共同点、差异点、潜在冲突、沟通建议等方面。分析应当全面且具体,其中文字在100字，内容不要体现字数。");

        // 设置用户提示
        request.setPrompt("请用详细分析" + mbtiType1 + "和" + mbtiType2 + "两种性格类型的兼容性，包括：\n1. 互补与相似之处\n2. 可能产生的冲突点\n3. 沟通与理解的建议\n4. 作为朋友/恋人的相处模式\n5. 双方需要注意的关系盲点\n请从心理学角度进行分析，提供实用的建议，要通俗易理解。");

        // 设置温度
        request.setTemperature(0.7f);

        // 调用流式API
        generateTextStream(request, emitter);
    }

    /**
     * 使用AI模型生成MBTI测试问题（流式响应）
     *
     * @param dimension MBTI维度(EI, SN, TF, JP)
     * @param count 需要生成的问题数量
     * @param emitter 服务器发送事件的发射器
     */
    @Override
    public void generateMbtiQuestionsStream(String dimension, int count, SseEmitter emitter) {
        AIModelRequest request = new AIModelRequest();

        // 设置系统提示
        String dimensionFullName = getDimensionFullName(dimension);
        request.setSystemPrompt("你是一个MBTI测试题设计专家，擅长设计能够区分不同性格维度的测试题目。请确保问题具有区分度，并且贴近日常生活场景。");

        // 设置用户提示
        request.setPrompt("请生成" + count + "道用于测试" + dimensionFullName + "(" + dimension + ")的MBTI题目。每个题目应当包含:\n1. 题干(真实生活中的情景)\n2. A选项(对应" + dimension.charAt(0) + "类型的选择)\n3. B选项(对应" + dimension.charAt(1) + "类型的选择)\n\n要求：\n1. 题目应该有区分度，能够明确区分两种倾向\n2. 题目应贴近生活，容易理解\n3. 避免使用专业术语\n4. 避免过于抽象的表述\n\n请以JSON格式返回题目，格式如下:\n```json\n[\n  {\n    \"question\": \"题干内容\",\n    \"optionA\": \"A选项内容\",\n    \"optionB\": \"B选项内容\",\n    \"dimensionA\": \"" + dimension.charAt(0) + "\",\n    \"dimensionB\": \"" + dimension.charAt(1) + "\"\n  }\n]\n```");

        // 设置温度
        request.setTemperature(0.8f);

        // 调用流式API
        generateTextStream(request, emitter);
    }
}
