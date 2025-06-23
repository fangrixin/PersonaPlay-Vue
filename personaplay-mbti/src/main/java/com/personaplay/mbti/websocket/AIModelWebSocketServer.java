package com.personaplay.mbti.websocket;

import com.alibaba.fastjson2.JSON;
import com.personaplay.mbti.service.IAIModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * AI模型分析WebSocket服务器
 * 用于流式响应AI分析内容
 */
@ServerEndpoint("/websocket/mbti/ai-analysis/{roomCode}/{userId}/{id}")
@Component
public class AIModelWebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(AIModelWebSocketServer.class);

    // 使用静态变量存储所有连接会话
    private static final Map<String, Session> SESSION_MAPS = new ConcurrentHashMap<>();

    // AI模型服务
    private static IAIModelService aiModelService;

    // 存储每个会话的Future，用于取消任务
    private static final Map<String, Future<?>> FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 注入AIModelService
     */
    @Autowired
    public void setAIModelService(IAIModelService aiModelService) {
        AIModelWebSocketServer.aiModelService = aiModelService;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomCode") String roomCode, @PathParam("userId") String userId,@PathParam("id") String id) {
        log.info("AI分析WebSocket连接建立 - 会话ID: {}, SessionID: {}", id, session.getId());
        // 存储会话
        SESSION_MAPS.put(userId+id, session);

        // 发送连接成功消息
        sendMessage(session, createMessage("CONNECTED", Map.of(
                "message", "WebSocket连接成功",
                "id", id
        )));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("roomCode") String roomCode, @PathParam("userId") String userId,@PathParam("id") String id) {
        log.info("AI分析WebSocket连接关闭 - 会话ID: {}", id);

        SESSION_MAPS.remove(userId+id);

        // 取消相关任务
        Future<?> future = FUTURE_MAP.remove(userId+id);
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message,@PathParam("roomCode") String roomCode, @PathParam("userId") String userId, @PathParam("id") String id) {
        log.info("收到消息 - 会话ID: {}, 消息: {}", id, message);

        try {
            Map<String, Object> msgMap = JSON.parseObject(message, Map.class);
            String type = (String) msgMap.get("type");
            Map<String, Object> data = (Map<String, Object>) msgMap.get("data");

            if ("START_ANALYSIS".equals(type)) {
                startAnalysis(userId,id, data);
            } else {
                log.warn("未知消息类型: {}", type);
                sendError(userId,id, "未知消息类型: " + type);
            }
        } catch (Exception e) {
            log.error("处理消息异常", e);
            sendError(userId,id, "处理请求失败: " + e.getMessage());
        }
    }

    /**
     * 开始分析
     */
    private void startAnalysis(String userId,String id, Map<String, Object> data) {
        log.info("开始分析 - 会话ID: {}, 数据: {}", id, data);

        Session session = SESSION_MAPS.get(userId+id);
        if (session == null) {
            log.warn("会话不存在，无法开始分析");
            return;
        }

        // 创建执行线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            String analysisType = (String) data.get("analysisType");

            // 也可以从ID中提取分析类型
            if (analysisType == null || analysisType.isEmpty()) {
                if (id.startsWith("personality-")) {
                    analysisType = "personality";
                } else if (id.startsWith("compatibility-")) {
                    analysisType = "compatibility";
                }
            }

            if ("personality".equals(analysisType)) {
                // 个性分析
                String mbtiType = (String) data.get("mbtiType");

                // 尝试从ID中提取MBTI类型
                if ((mbtiType == null || mbtiType.isEmpty()) && id.startsWith("personality-")) {
                    mbtiType = id.substring("personality-".length());
                }

                if (mbtiType == null || mbtiType.isEmpty()) {
                    sendError(userId,id, "缺少MBTI类型参数");
                    return;
                }

                log.info("进行个性分析 - MBTI类型: {}", mbtiType);

                // 创建一个final变量来存储ID和MBTI类型
                final String sessionId = id;
                final String finalMbtiType = mbtiType;

                // 启动分析任务
                Future<?> future = executor.submit(() -> {
                    try {
                        // 创建自定义SSE发射器
                        CustomSseEmitter emitter = new CustomSseEmitter(
                                content -> sendAnalysisContent(userId,sessionId, content),
                                () -> sendAnalysisComplete(userId,sessionId),
                                error -> sendError(userId,sessionId, error)
                        );

                        // 调用AI模型服务
                        aiModelService.generatePersonalityAnalysisStream(finalMbtiType, emitter);
                    } catch (Exception e) {
                        log.error("个性分析执行异常", e);
                        sendError(userId,sessionId, "分析执行失败: " + e.getMessage());
                    }
                });

                FUTURE_MAP.put(userId+id, future);
            } else if ("compatibility".equals(analysisType)) {
                // 兼容性分析
                String myMbtiType = (String) data.get("myMbtiType");
                String partnerMbtiType = (String) data.get("partnerMbtiType");

                // 尝试从ID中提取MBTI类型
                if ((myMbtiType == null || myMbtiType.isEmpty() ||
                        partnerMbtiType == null || partnerMbtiType.isEmpty()) &&
                        id.startsWith("compatibility-")) {

                    String[] parts = id.substring("compatibility-".length()).split("-");
                    if (parts.length >= 2) {
                        myMbtiType = parts[0];
                        partnerMbtiType = parts[1];
                    }
                }

                if (myMbtiType == null || myMbtiType.isEmpty() ||
                        partnerMbtiType == null || partnerMbtiType.isEmpty()) {
                    sendError(userId,id, "缺少MBTI类型参数");
                    return;
                }

                log.info("进行兼容性分析 - MBTI类型: {} 和 {}", myMbtiType, partnerMbtiType);

                // 创建final变量来存储ID和MBTI类型
                final String sessionId = id;
                final String finalMyMbtiType = myMbtiType;
                final String finalPartnerMbtiType = partnerMbtiType;

                // 启动分析任务
                Future<?> future = executor.submit(() -> {
                    try {
                        // 创建自定义SSE发射器
                        CustomSseEmitter emitter = new CustomSseEmitter(
                                content -> sendAnalysisContent(userId,sessionId, content),
                                () -> sendAnalysisComplete(userId,sessionId),
                                error -> sendError(userId,sessionId, error)
                        );

                        // 调用AI模型服务
                        aiModelService.generateCompatibilityAnalysisStream(finalMyMbtiType, finalPartnerMbtiType, emitter);
                    } catch (Exception e) {
                        log.error("兼容性分析执行异常", e);
                        sendError(userId,sessionId, "分析执行失败: " + e.getMessage());
                    }
                });

                FUTURE_MAP.put(userId+id, future);
            } else {
                sendError(userId,id, "不支持的分析类型: " + analysisType);
            }
        } catch (Exception e) {
            log.error("启动分析异常", e);
            sendError(userId,id, "启动分析失败: " + e.getMessage());
            executor.shutdown();
        }
    }

    /**
     * 发送分析内容
     */
    private void sendAnalysisContent(String userId,String id, String content) {
        Session session = SESSION_MAPS.get(userId+id);
        if (session != null) {
            // 过滤掉包含COMPLETE的内容，避免显示在用户界面
            if (content != null && !content.contains("COMPLETE:") && !content.trim().equals("[DONE]")) {
                // 保留原始格式，但分成小块发送
//                log.debug("发送分析内容 [长度:{}]", content.length());

                // 将内容分成较小的块发送，以确保前端能够正确处理
                int chunkSize = 30; // 每块最大30个字符

                if (content.length() > chunkSize) {
                    // 分块发送长内容
                    for (int i = 0; i < content.length(); i += chunkSize) {
                        int endIndex = Math.min(i + chunkSize, content.length());
                        String subContent = content.substring(i, endIndex);

                        sendMessage(session, createMessage("AI_CONTENT", Map.of("content", subContent)));

                        // 添加短暂延迟，确保前端可以按顺序处理
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else {
                    // 发送较短的内容
                    sendMessage(session, createMessage("AI_CONTENT", Map.of("content", content)));
                }
            } else {
                log.debug("跳过发送完成标记内容: {}", content);
            }
        }
    }

    /**
     * 发送分析完成消息
     */
    private void sendAnalysisComplete(String userId,String id) {
        Session session = SESSION_MAPS.get(userId+id);
        if (session != null) {
            // 发送完成消息，但不包含任何可能会显示在界面上的内容
            sendMessage(session, createMessage("AI_COMPLETE", Map.of("message", "分析已完成")));
        }
        FUTURE_MAP.remove(id);
    }

    /**
     * 发送错误消息
     */
    private void sendError(String userId,String id, String errorMessage) {
        Session session = SESSION_MAPS.get(userId+id);
        if (session != null) {
            sendMessage(session, createMessage("ERROR", Map.of("message", errorMessage)));
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error,@PathParam("roomCode") String roomCode, @PathParam("userId") String userId,@PathParam("id") String id) {
        log.error("WebSocket错误 - 会话ID: {}", id, error);

        SESSION_MAPS.remove(userId+id);

        // 取消相关任务
        Future<?> future = FUTURE_MAP.remove(userId+id);
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
    }

    /**
     * 发送消息
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息异常", e);
        }
    }

    /**
     * 创建消息
     */
    private String createMessage(String type, Object data) {
        Map<String, Object> message = Map.of(
                "type", type,
                "data", data
        );
        return JSON.toJSONString(message);
    }

    /**
     * 自定义SSE发射器，用于将SSE事件转发到WebSocket
     */
    private class CustomSseEmitter extends SseEmitter {
        private final Consumer<String> contentCallback;
        private final Runnable completeCallback;
        private final Consumer<String> errorCallback;

        public CustomSseEmitter(Consumer<String> contentCallback,
                                Runnable completeCallback,
                                Consumer<String> errorCallback) {
            super(5 * 60 * 1000L); // 5分钟超时
            this.contentCallback = contentCallback;
            this.completeCallback = completeCallback;
            this.errorCallback = errorCallback;

            // 设置完成回调
            this.onCompletion(completeCallback);

            // 设置超时回调
            this.onTimeout(() -> {
                log.warn("SseEmitter超时");
                errorCallback.accept("分析请求超时");
            });

            // 设置错误回调
            this.onError((ex) -> {
                log.error("SseEmitter错误", ex);
                errorCallback.accept("分析处理错误: " + ex.getMessage());
            });
        }

        @Override
        public void send(Object object) throws IOException {
            super.send(object);

            try {
//                log.debug("SSE发送对象类型: {}", object != null ? object.getClass().getName() : "null");

                // 处理SseEventBuilder类型的消息
                if (object instanceof SseEmitter.SseEventBuilder) {
                    log.debug("检测到SseEventBuilder类型数据，尝试提取内容");
                    // 无法直接获取SseEventBuilder的内容
                    return;
                }

                // 处理Map类型的数据
                if (object instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) object;
                    log.debug("处理Map类型数据: {}", map);
                    if (map.containsKey("content")) {
                        String content = (String) map.get("content");
                        // 过滤掉完成标记
                        if (content != null && !content.contains("COMPLETE:") && !content.trim().equals("[DONE]")) {
                            log.debug("从Map中提取内容 [长度:{}]", content.length());
                            if (contentCallback != null) {
                                contentCallback.accept(content);
                            }
                        } else {
                            log.debug("跳过发送完成标记: {}", content);
                            // 检测到完成标记，调用完成回调
                            if (completeCallback != null) {
                                completeCallback.run();
                            }
                        }
                    }
                }

                // 处理字符串类型的数据
                else if (object instanceof String) {
                    String content = (String) object;
                    // 过滤掉完成标记
                    if (!content.contains("COMPLETE:") && !content.trim().equals("[DONE]")) {
                        if (contentCallback != null) {
                            contentCallback.accept(content);
                        }
                    } else {
                        log.debug("跳过字符串完成标记: {}", content);
                        // 检测到完成标记，调用完成回调
                        if (completeCallback != null) {
                            completeCallback.run();
                        }
                    }
                }

                // 处理其他任何类型的对象 - 转换为JSON字符串
                else if (object != null) {
                    log.debug("处理其他类型数据: {}", object);
                    String jsonContent = JSON.toJSONString(object);
                    // 过滤掉完成标记
                    if (!jsonContent.contains("COMPLETE:") && !jsonContent.trim().equals("[DONE]")) {
                        log.debug("转换为JSON: {}", jsonContent);
                        if (contentCallback != null) {
                            contentCallback.accept(jsonContent);
                        }
                    } else {
                        log.debug("跳过JSON完成标记: {}", jsonContent);
                        // 检测到完成标记，调用完成回调
                        if (completeCallback != null) {
                            completeCallback.run();
                        }
                    }
                }
            } catch (Exception e) {
                log.error("处理SseEmitter消息异常", e);
            }
        }
    }
}
