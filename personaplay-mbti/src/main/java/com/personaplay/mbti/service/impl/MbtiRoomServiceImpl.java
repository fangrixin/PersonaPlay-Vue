package com.personaplay.mbti.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson2.JSON;
import com.personaplay.common.core.redis.RedisCache;
import com.personaplay.common.utils.DateUtils;
import com.personaplay.mbti.domain.MbtiQuestion;
import com.personaplay.mbti.domain.MbtiRoomMember;
import com.personaplay.mbti.domain.MbtiTestVersion;
import com.personaplay.mbti.mapper.MbtiQuestionMapper;
import com.personaplay.mbti.mapper.MbtiRoomMemberMapper;
import com.personaplay.mbti.mapper.MbtiTestVersionMapper;
import com.personaplay.mbti.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiRoomMapper;
import com.personaplay.mbti.domain.MbtiRoom;
import com.personaplay.mbti.service.IMbtiRoomService;

/**
 * MBTI测试房间Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiRoomServiceImpl implements IMbtiRoomService
{
    @Autowired
    private MbtiRoomMapper mbtiRoomMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MbtiRoomMemberMapper mbtiRoomMemberMapper;
    @Autowired
    private MbtiTestVersionMapper mbtiTestVersionMapper;

    @Autowired
    private MbtiQuestionMapper mbtiQuestionMapper;

    private static final Logger log = LoggerFactory.getLogger(MbtiRoomServiceImpl.class);
    /**
     * 查询MBTI测试房间
     *
     * @param roomId MBTI测试房间主键
     * @return MBTI测试房间
     */
    @Override
    public MbtiRoom selectMbtiRoomByRoomId(Long roomId)
    {
        return mbtiRoomMapper.selectMbtiRoomByRoomId(roomId);
    }

    /**
     * 查询MBTI测试房间列表
     *
     * @param mbtiRoom MBTI测试房间
     * @return MBTI测试房间
     */
    @Override
    public List<MbtiRoom> selectMbtiRoomList(MbtiRoom mbtiRoom)
    {
        return mbtiRoomMapper.selectMbtiRoomList(mbtiRoom);
    }

    /**
     * 新增MBTI测试房间
     *
     * @param mbtiRoom MBTI测试房间
     * @return 结果
     */
    @Override
    public int insertMbtiRoom(MbtiRoom mbtiRoom)
    {
        mbtiRoom.setCreateTime(DateUtils.getNowDate());
        return mbtiRoomMapper.insertMbtiRoom(mbtiRoom);
    }

    /**
     * 修改MBTI测试房间
     *
     * @param mbtiRoom MBTI测试房间
     * @return 结果
     */
    @Override
    public int updateMbtiRoom(MbtiRoom mbtiRoom)
    {
        mbtiRoom.setUpdateTime(DateUtils.getNowDate());
        return mbtiRoomMapper.updateMbtiRoom(mbtiRoom);
    }

    /**
     * 批量删除MBTI测试房间
     *
     * @param roomIds 需要删除的MBTI测试房间主键
     * @return 结果
     */
    @Override
    public int deleteMbtiRoomByRoomIds(Long[] roomIds)
    {
        return mbtiRoomMapper.deleteMbtiRoomByRoomIds(roomIds);
    }

    /**
     * 删除MBTI测试房间信息
     *
     * @param roomId MBTI测试房间主键
     * @return 结果
     */
    @Override
    public int deleteMbtiRoomByRoomId(Long roomId)
    {
        return mbtiRoomMapper.deleteMbtiRoomByRoomId(roomId);
    }





    @Override
    public String createRoom(Map<String, Object> roomData) {
        String roomCode = generateRoomCode();
        long userId = (Long) roomData.get("creatorId");
        String creatorId = String.valueOf(userId);
        String creatorName = (String) roomData.get("creatorName");
        String creatorAvatar = (String) roomData.get("creatorAvatar");

        // 生成版本信息
        Integer versionId = (Integer) roomData.get("versionId");
        MbtiTestVersion mbtiTestVersion = mbtiTestVersionMapper.selectMbtiTestVersionByVersionId(Long.valueOf(versionId));
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
            questions = JSON.toJSONString(mbtiQuestionList);
            // 打印日志
            log.info("NewQuestions: {}", questions);
        }

        // 存储房间信息  "mbti:room:" + roomCode
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        Map<String, String> roomInfo = new HashMap<>();
        roomInfo.put("status", "0"); // 等待中
        roomInfo.put("version", mbtiTestVersion.getVersionCode());
        roomInfo.put("versionId", String.valueOf(versionId));
        roomInfo.put("creatorId", creatorId);
        roomInfo.put("createTime", DateUtils.dateTime());
        roomInfo.put("questions",questions);
        roomInfo.put("type","2");
        roomInfo.put("totalQuestionNum", String.valueOf(totalQuestionNum));
        redisCache.setCacheMap(roomKey, roomInfo);

        // 添加创建者为房间成员  mbti:room:" + roomCode + ":members
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        redisCache.setCacheSet(roomMembersKey, Set.of(creatorId));
        // 存储创建者信息  "mbti:room:" + roomCode + ":member:" + userId
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, creatorId);
        Map<String, String> memberInfo = new HashMap<>();
        memberInfo.put("nickName", creatorName);
        memberInfo.put("avatarUrl", creatorAvatar);
        memberInfo.put("userId", creatorId);
        memberInfo.put("isCreator", "true");
        memberInfo.put("isReady", "true"); // 创建者默认已准备
        memberInfo.put("joinTime", DateUtils.dateTime());
        memberInfo.put("currentQuestion", "0");
        memberInfo.put("answers", "{}");
        redisCache.setCacheMap(memberKey, memberInfo);

        // 设置过期时间
        redisCache.expire(roomKey, 24, TimeUnit.HOURS);
        redisCache.expire(roomMembersKey, 24, TimeUnit.HOURS);
        redisCache.expire(memberKey, 24, TimeUnit.HOURS);
        // 数据入库
        MbtiRoom mbtiRoom = new MbtiRoom();
        mbtiRoom.setRoomCode(roomCode);
        mbtiRoom.setCreatorId(Long.parseLong(creatorId));
        mbtiRoom.setVersionId(Long.valueOf(versionId));
        mbtiRoom.setCreateTime(new Date());
        mbtiRoom.setUpdateTime(new Date());
        mbtiRoomMapper.insertMbtiRoom(mbtiRoom);
        redisCache.setCacheMapValue(roomKey, "roomId", mbtiRoom.getRoomId());
        MbtiRoomMember mbtiRoomMember = new MbtiRoomMember();
        mbtiRoomMember.setRoomId(Long.valueOf(roomCode));
        mbtiRoomMember.setUserId(userId);
        mbtiRoomMember.setJoinTime(new Date());
        mbtiRoomMember.setIsCreator(1);
        mbtiRoomMember.setCreateTime(new Date());
        mbtiRoomMember.setUpdateTime(new Date());
        mbtiRoomMemberMapper.insertMbtiRoomMember(mbtiRoomMember);
        return roomCode;
    }

    @Override
    public Map<String, Object> getRoomInfo(String roomCode) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return null;
        }

        Map<String, String> roomInfo = redisCache.getCacheMap(roomKey);
        Map<String, Object> result = new HashMap<>(roomInfo);

        // 获取成员数量
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
        result.put("memberCount", memberIds.size());

        return result;
    }

    @Override
    public boolean joinRoom(String roomCode, Map<String, Object> userData) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return false;
        }

        // 检查房间状态
        String status = redisCache.getCacheMapValue(roomKey, "status");
        if (!"0".equals(status)) { // 只有等待中的房间可以加入
            return false;
        }

        String userId = String.valueOf((Long) userData.get("userId"));
        String nickName = (String) userData.get("nickName");
        String avatarUrl = (String) userData.get("avatarUrl");

        // 添加成员
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        redisCache.setCacheSet(roomMembersKey, Set.of(userId));

        // 存储成员信息
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        Map<String, String> memberInfo = new HashMap<>();
        memberInfo.put("nickName", nickName);
        memberInfo.put("avatarUrl", avatarUrl);
        memberInfo.put("userId", userId);
        memberInfo.put("isCreator", "false");
        memberInfo.put("isReady", "false");
        memberInfo.put("joinTime", DateUtils.dateTime());
        memberInfo.put("currentQuestion", "0");
        memberInfo.put("answers", "{}");
        redisCache.setCacheMap(memberKey, memberInfo);

        // 设置过期时间
        redisCache.expire(memberKey, 24, TimeUnit.HOURS);
        //数据入库
        MbtiRoomMember mbtiRoomMember = new MbtiRoomMember();
        mbtiRoomMember.setRoomId(Long.valueOf(roomCode));
        mbtiRoomMember.setUserId(Long.parseLong(userId));
        mbtiRoomMember.setJoinTime(new Date());
        mbtiRoomMember.setIsCreator(0);
        mbtiRoomMember.setUpdateTime(new Date());
        mbtiRoomMember.setCreateTime(new Date());
        MbtiRoomMember params = new MbtiRoomMember();
        params.setUserId(mbtiRoomMember.getUserId());
        params.setRoomId(mbtiRoomMember.getRoomId());
       List<MbtiRoomMember> members = mbtiRoomMemberMapper.selectMbtiRoomMemberList(params);
       if(members == null || members.size() <1) {
           mbtiRoomMemberMapper.insertMbtiRoomMember(mbtiRoomMember);
       }
        return true;
    }

    @Override
    public List<Map<String, Object>> getRoomMembers(String roomCode) {
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);

        List<Map<String, Object>> members = new ArrayList<>();
        for (String memberId : memberIds) {
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
            Map<String, String> memberInfo = redisCache.getCacheMap(memberKey);

            Map<String, Object> member = new HashMap<>(memberInfo);
            member.put("userId", memberId);
            members.add(member);
        }

        // 按加入时间排序
        members.sort(Comparator.comparing(m -> Long.parseLong((String) m.get("joinTime"))));

        return members;
    }

    @Override
    public boolean updateReadyStatus(String roomCode, String userId, boolean isReady) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return false;
        }

        // 检查用户是否在房间内
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
        if (!memberIds.contains(userId)) {
            return false;
        }

        // 更新状态
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "isReady", String.valueOf(isReady));

        return true;
    }

    @Override
    public boolean startTest(String roomCode, String userId) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return false;
        }

        // 检查是否是创建者
        String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");
        if (!userId.equals(creatorId)) {
            return false;
        }

        // 更新房间状态
        redisCache.setCacheMapValue(roomKey, "status", "1"); // 测试中

        return true;
    }

    @Override
    public boolean updateAnswer(String roomCode, String userId, int questionIndex, String answer) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return false;
        }

        // 检查用户是否在房间内
        String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
        Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
        if (!memberIds.contains(userId)) {
            return false;
        }

        // 更新答题状态
        String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
        redisCache.setCacheMapValue(memberKey, "currentQuestion", String.valueOf(questionIndex + 1));

        // 更新答题记录
        String answersJson = redisCache.getCacheMapValue(memberKey, "answers");
        Map<String, String> answers = answersJson == null || answersJson.isEmpty()
                ? new HashMap<>()
                : JSON.parseObject(answersJson, Map.class);

        answers.put(String.valueOf(questionIndex), answer);
        redisCache.setCacheMapValue(memberKey, "answers", JSON.toJSONString(answers));

        return true;
    }

    @Override
    public boolean closeRoom(String roomCode, String userId) {
        String roomKey = RedisKeyUtil.getRoomKey(roomCode);
        if (!redisCache.hasKey(roomKey)) {
            return false;
        }

        // 检查是否是创建者
        String creatorId = redisCache.getCacheMapValue(roomKey, "creatorId");
        if (!userId.equals(creatorId)) {
            return false;
        }

        // 更新房间状态
        redisCache.setCacheMapValue(roomKey, "status", "3"); // 已关闭
        redisCache.deleteObject(roomKey);


        return true;
    }

    /**
     * 生成6位随机房间码
     */
    private String generateRoomCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6位数
        return String.valueOf(code);
    }

}
