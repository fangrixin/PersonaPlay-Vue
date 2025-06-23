package com.personaplay.mbti.websocket;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.personaplay.common.core.redis.RedisCache;
import com.personaplay.mbti.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@ServerEndpoint("/websocket/mbti/{roomCode}/{userId}")
@Component
public class MbtiWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger(MbtiWebSocketServer.class);

    // 使用静态变量存储所有连接会话
    private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<>();

    // Redis缓存
    private static RedisCache redisCache;

    /**
     * 注入RedisCache
     */
    @Autowired
    public void setRedisCache(RedisCache redisCache) {
        MbtiWebSocketServer.redisCache = redisCache;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomCode") String roomCode, @PathParam("userId") String userId) {
        log.info("WebSocket连接建立 - 房间: {}, 用户: {}", roomCode, userId);

        // 存储会话
        String sessionKey = getSessionKey(roomCode, userId);
        SESSION_MAP.put(sessionKey, session);

        // 检查房间是否存在
        if (!redisCache.hasKey(RedisKeyUtil.getRoomKey(roomCode))) {
            sendMessage(session, createMessage("ERROR", "房间不存在或已关闭"));
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭会话异常", e);
            }
            return;
        }

        // 通知房间内所有人有新成员加入
        sendToRoom(roomCode, createMessage("MEMBER_JOIN", Map.of(
            "roomCode", roomCode,
            "userId", userId
        )));

        // 查询并发送当前房间状态
        sendRoomStatus(roomCode, session, true);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("roomCode") String roomCode, @PathParam("userId") String userId) {
        String sessionKey = getSessionKey(roomCode, userId);
        SESSION_MAP.remove(sessionKey);
        log.info("WebSocket连接关闭 - 房间: {}, 用户: {}", roomCode, userId);

        // 通知房间内其他人成员离开
        sendToRoom(roomCode, createMessage("MEMBER_LEAVE", Map.of(
            "roomCode", roomCode,
            "userId", userId
        )));
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, @PathParam("roomCode") String roomCode, @PathParam("userId") String userId) {
        log.info("收到消息 - 房间: {}, 用户: {}, 消息: {}", roomCode, userId, message);

        try {
            Map<String, Object> msgMap = JSON.parseObject(message, Map.class);
            String type = (String) msgMap.get("type");
            Map<String, Object> data = (Map<String, Object>) msgMap.get("data");

            switch (type) {
                case "READY_STATUS_CHANGE":
                    handleReadyStatusChange(roomCode, userId, data);
                    break;
                case "ANSWER_UPDATE":
                    handleAnswerUpdate(roomCode, userId, data);
                    break;
                case "START_TEST":
                    handleStartTest(roomCode, userId);
                    break;
                case "CLOSE_ROOM":
                    handleCloseRoom(roomCode, userId);
                    break;
                case "EXIT_ROOM":
                    handleExitRoom(roomCode, userId);
                    break;
                case "TEST_RESULT":
                    handleTestResult(roomCode, userId, data);
                    break;
                case "GUESS_RESULT":
                    handleGuessResult(roomCode, userId, data);
                    break;
                case "ROOM_STATUS":
                    sendRoomStatus(roomCode, null, true);
                    break;
                case "EMOJI":
                    handleEmojiMessage(roomCode, userId, data);
                    break;
                default:
                    log.warn("未知消息类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理消息异常", e);
        }
    }

    /**
     * 处理准备状态变更
     */
    private void handleReadyStatusChange(String roomCode, String userId, Map<String, Object> data) {
        boolean isReady = Boolean.parseBoolean(data.get("isReady").toString());

        // 更新Redis中的准备状态
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "isReady", String.valueOf(isReady));

        // 通知房间内所有人状态变更
        sendToRoom(roomCode, createMessage("READY_STATUS_CHANGE", Map.of(
            "roomCode", roomCode,
            "userId", userId,
            "isReady", String.valueOf(isReady)
        )));

        // 检查是否所有人都已准备好，如果是创建者，可以发送开始测试按钮可用的状态
        checkAllReady(roomCode);
    }

    /**
     * 处理答题更新
     */
    private void handleAnswerUpdate(String roomCode, String userId, Map<String, Object> data) {
        int questionIndex = Integer.parseInt(data.get("questionIndex").toString());
        String answer = data.get("answer").toString();

        // 更新Redis中的答题状态
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "currentQuestion", String.valueOf(questionIndex + 1));

        // 获取当前答题记录
        String answersJson = redisCache.getCacheMapValue(memberKey, "answers");
        Map<String, String> answers = answersJson == null || answersJson.isEmpty()
            ? new ConcurrentHashMap<>()
            : JSON.parseObject(answersJson, Map.class);

        // 更新答题记录
        answers.put(String.valueOf(questionIndex), answer);
        redisCache.setCacheMapValue(memberKey, "answers", JSON.toJSONString(answers));

        // 通知房间内所有人答题状态更新
        sendToRoom(roomCode, createMessage("ANSWER_UPDATE", Map.of(
            "roomCode", roomCode,
            "userId", userId,
            "questionIndex", questionIndex,
            "answer", answer
        )));

        // 检查当前题目是否所有人都已回答
        checkQuestionAnswered(roomCode, questionIndex);
    }

    /**
     * 检查当前题目是否所有人都已回答
     */
    private void checkQuestionAnswered(String roomCode, int questionIndex) {
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        boolean allAnswered = true;
        Map<String, String> memberAnswers = new ConcurrentHashMap<>();

        for (String memberId : memberIds) {
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
            String answersJson = redisCache.getCacheMapValue(memberKey, "answers");

            if (answersJson == null || answersJson.isEmpty()) {
                allAnswered = false;
                break;
            }

            Map<String, String> answers = JSON.parseObject(answersJson, Map.class);
            if (!answers.containsKey(String.valueOf(questionIndex))) {
                allAnswered = false;
                break;
            }

            // 收集所有成员的答案
            memberAnswers.put(memberId, answers.get(String.valueOf(questionIndex)));
        }

        // 如果所有人都已回答当前题目，发送通知
        if (allAnswered) {
            sendToRoom(roomCode, createMessage("QUESTION_COMPLETED", Map.of(
                "roomCode", roomCode,
                "questionIndex", questionIndex,
                "memberAnswers", memberAnswers
            )));
            checkAllCompleted(roomCode);
        }
    }

    /**
     * 处理开始测试
     */
    private void handleStartTest(String roomCode, String userId) {
        // 检查是否是房间创建者
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");

        if (!userId.equals(creatorId)) {
            String sessionKey = getSessionKey(roomCode, userId);
            Session session = SESSION_MAP.get(sessionKey);
            if (session != null) {
                sendMessage(session, createMessage("ERROR", "只有创建者可以开始测试"));
            }
            return;
        }

        // 更新房间状态为测试中
        redisCache.setCacheMapValue(roomKey, "status", "1");

        // 通知房间内所有人开始测试
        sendToRoom(roomCode, createMessage("START_TEST", Map.of(
            "roomCode", roomCode
        )));
    }

    /**
     * 处理关闭房间
     */
    private void handleCloseRoom(String roomCode, String userId) {
        // 检查是否是房间创建者
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");

        if (!userId.equals(creatorId)) {
            String sessionKey = getSessionKey(roomCode, userId);
            Session session = SESSION_MAP.get(sessionKey);
            if (session != null) {
                sendMessage(session, createMessage("ERROR", "只有创建者可以关闭房间"));
            }
            return;
        }

        // 更新房间状态为已关闭
        redisCache.setCacheMapValue(roomKey, "status", "3");

        // 通知房间内所有人房间已关闭
        sendToRoom(roomCode, createMessage("CLOSE_ROOM", Map.of(
            "roomCode", roomCode
        )));

        // 关闭所有连接
        closeRoomConnections(roomCode);

        // 设置Redis数据过期时间
        redisCache.expire(roomKey, 1, TimeUnit.HOURS);
        redisCache.expire(RedisKeyUtil.getRoomMembersKey(roomCode), 1, TimeUnit.HOURS);
    }

    /**
     * 处理测试结果
     */
    private void handleTestResult(String roomCode, String userId, Map<String, Object> data) {
        log.info("处理测试结果 - 房间: {}, 用户: {}", roomCode, userId);

        String mbti = data.get("mbti").toString();
        String userName = data.containsKey("userName") ? data.get("userName").toString() : userId;
        String guessedMbti = data.containsKey("guessedMbti") ? data.get("guessedMbti").toString() : "";
        String eiScore = data.containsKey("eiScore") ? data.get("eiScore").toString() : "0";
        String snScore = data.containsKey("snScore") ? data.get("snScore").toString() : "0";
        String jpScore = data.containsKey("jpScore") ? data.get("jpScore").toString() : "0";
        String tfScore = data.containsKey("tfScore") ? data.get("tfScore").toString() : "0";
        // 存储测试结果到Redis
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "mbti", mbti);
        redisCache.setCacheMapValue(memberKey, "userName", userName);
        redisCache.setCacheMapValue(memberKey, "eiScore", eiScore);
        redisCache.setCacheMapValue(memberKey, "snScore", snScore);
        redisCache.setCacheMapValue(memberKey, "jpScore", jpScore);
        redisCache.setCacheMapValue(memberKey, "tfScore", tfScore);
        if (!guessedMbti.isEmpty()) {
            redisCache.setCacheMapValue(memberKey, "guessedMbti", guessedMbti);
        }

        // 通知房间内所有人测试结果
        sendToRoom(roomCode, createMessage("TEST_RESULT", Map.of(
            "roomCode", roomCode,
            "userId", userId,
            "mbti", mbti,
            "userName", userName,
            "guessedMbti", guessedMbti
        )));
    }

    /**
     * 处理猜测结果
     */
    private void handleGuessResult(String roomCode, String userId, Map<String, Object> data) {
        log.info("处理猜测结果 - 房间: {}, 用户: {}", roomCode, userId);

        String guessedMbti = data.get("guessedMbti").toString();

        // 存储猜测结果到Redis
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "guessedMbti", guessedMbti);

        // 通知房间内所有人猜测结果
        sendToRoom(roomCode, createMessage("GUESS_RESULT", Map.of(
            "roomCode", roomCode,
            "userId", userId,
            "guessedMbti", guessedMbti
        )));
    }

    /**
     * 处理退出房间
     */
    private void handleExitRoom(String roomCode, String userId) {
        // 检查是否是房间创建者
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");

        if (!userId.equals(creatorId)) {
            // 通知房间内其他人成员离开
            sendToRoom(roomCode, createMessage("MEMBER_LEAVE", Map.of(
                    "roomCode", roomCode,
                    "userId", userId
            )));
            sendRoomStatus(roomCode, null, true);
            String sessionKey = getSessionKey(roomCode, userId);
            SESSION_MAP.remove(sessionKey);
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
            redisCache.deleteObject(memberKey);

            String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
            redisCache.deleteObject(roomMembersKey);
            redisCache.setCacheSet(roomMembersKey, Set.of(creatorId));

        }else{
            log.error("退出房间失败 - 房间: {}, 用户: {}", roomCode, userId);
        }
    }

    /**
     * 处理表情消息
     */
    private void handleEmojiMessage(String roomCode, String userId, Map<String, Object> data) {
        String emoji = data.get("emoji").toString();

        log.info("用户 {} 在房间 {} 发送表情: {}", userId, roomCode, emoji);

        // 验证房间是否存在
        if (!redisCache.hasKey(RedisKeyUtil.getRoomKey(roomCode))) {
            log.warn("房间不存在: {}", roomCode);
            return;
        }

        // 构建表情消息
        JSONObject message = new JSONObject();
        message.put("type", "EMOJI");

        JSONObject dataJson = new JSONObject();
        dataJson.put("roomCode", roomCode);
        dataJson.put("userId", userId);
        dataJson.put("emoji", emoji);
        dataJson.put("timestamp", System.currentTimeMillis());

        message.put("data", dataJson);

        // 广播给房间里的所有人
        sendToRoom(roomCode, message.toJSONString());
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam("roomCode") String roomCode, @PathParam("userId") String userId) {
        log.error("WebSocket错误 - 房间: {}, 用户: {}", roomCode, userId, error);
        String sessionKey = getSessionKey(roomCode, userId);
        SESSION_MAP.remove(sessionKey);
    }

    /**
     * 检查是否所有人都已准备好
     */
    private void checkAllReady(String roomCode) {
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
        if (memberIds.size() < 2) {
            return;
        }

        boolean allReady = true;
        for (String memberId : memberIds) {
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
            String isReady = redisCache.getCacheMapValue(memberKey, "isReady");
            if (!"true".equals(isReady)) {
                allReady = false;
                break;
            }
        }

        // 通知房间创建者所有人是否已准备好
        if (allReady) {
            String roomKey = RedisKeyUtil.getRoomKey(roomCode);
            String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");
            String creatorSessionKey = getSessionKey(roomCode, creatorId);
            Session creatorSession = SESSION_MAP.get(creatorSessionKey);

            if (creatorSession != null) {
                sendMessage(creatorSession, createMessage("ALL_READY", Map.of(
                    "roomCode", roomCode,
                    "allReady", true
                )));
            }
        }
    }

    /**
     * 检查是否所有人都完成答题
     */
    private void checkAllCompleted(String roomCode) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        String status = redisCache.getCacheMapValue(roomKey, "status");

        // 只有在测试中才检查
        if (!"1".equals(status)) {
            return;
        }

        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        // 获取测试版本确定题目总数
        String totalQuestionNum = redisCache.getCacheMapValue(roomKey, "totalQuestionNum");


        boolean allCompleted = true;
        for (String memberId : memberIds) {
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
            String currentQuestion = redisCache.getCacheMapValue(memberKey, "currentQuestion");

            if (currentQuestion == null || Integer.parseInt(currentQuestion) < Integer.valueOf(totalQuestionNum)) {
                allCompleted = false;
                break;
            }
        }

        // 如果所有人都完成了答题，更新房间状态为已完成
        if (allCompleted) {
            redisCache.setCacheMapValue(roomKey, "status", "2");

            // 通知房间内所有人测试已完成
            sendToRoom(roomCode, createMessage("TEST_COMPLETED", Map.of(
                "roomCode", roomCode
            )));
        }
    }

    /**
     * 发送当前房间状态
     */
    private void sendRoomStatus(String roomCode, Session session,boolean sendAll) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        Map<String, String> roomInfo = redisCache.getCacheMap(roomKey);

        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        Map<String, Object> membersInfo = new ConcurrentHashMap<>();
        for (String memberId : memberIds) {
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
            Map<String, String> memberInfo = redisCache.getCacheMap(memberKey);
            membersInfo.put(memberId, memberInfo);
        }

        Map<String, Object> statusData = Map.of(
            "roomCode", roomCode,
            "roomInfo", roomInfo,
            "members", membersInfo
        );
        if(sendAll){
            sendToRoom(roomCode, createMessage("ROOM_STATUS", statusData));
        }else{
            sendMessage(session, createMessage("ROOM_STATUS", statusData));
        }
    }

    /**
     * 向房间内所有人发送消息
     */
    private void sendToRoom(String roomCode, String message) {
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        for (String memberId : memberIds) {
            String sessionKey = getSessionKey(roomCode, memberId);
            Session session = SESSION_MAP.get(sessionKey);

            if (session != null) {
                sendMessage(session, message);
            }
        }
    }

    /**
     * 关闭房间内的所有连接
     */
    private void closeRoomConnections(String roomCode) {
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        for (String memberId : memberIds) {
            String sessionKey = getSessionKey(roomCode, memberId);
            Session session = SESSION_MAP.get(sessionKey);

            if (session != null) {
                try {
                    session.close();
                } catch (IOException e) {
                    log.error("关闭会话异常", e);
                }
            }
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
     * 获取会话的唯一键
     */
    private String getSessionKey(String roomCode, String userId) {
        return roomCode + ":" + userId;
    }
}
