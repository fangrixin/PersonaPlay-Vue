package com.personaplay.mbti.service.impl;

import java.util.*;
import java.util.concurrent.TimeUnit;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.personaplay.common.core.redis.RedisCache;
import com.personaplay.common.utils.DateUtils;
import com.personaplay.mbti.domain.*;
import com.personaplay.mbti.domain.vo.MbtiTestRecordVo;
import com.personaplay.mbti.mapper.*;
import com.personaplay.mbti.util.MBTIMatchCalculator;
import com.personaplay.mbti.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.service.IMbtiTestRecordService;

/**
 * MBTI测试记录Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiTestRecordServiceImpl implements IMbtiTestRecordService
{
    private static final Logger log = LoggerFactory.getLogger(MbtiTestRecordServiceImpl.class);

    @Autowired
    private MbtiTestRecordMapper mbtiTestRecordMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MbtiRoomMapper mbtiRoomMapper;

    @Autowired
    private MbtiPersonalityTypeMapper mbtiPersonalityTypeMapper;

    @Autowired
    private MbtiCompatibilityMapper mbtiCompatibilityMapper;

    @Autowired
    private MbtiTestVersionMapper mbtiTestVersionMapper;

    /**
     * 查询MBTI测试记录
     *
     * @param recordId MBTI测试记录主键
     * @return MBTI测试记录
     */
    @Override
    public MbtiTestRecord selectMbtiTestRecordByRecordId(Long recordId)
    {
        return mbtiTestRecordMapper.selectMbtiTestRecordByRecordId(recordId);
    }

    /**
     * 查询MBTI测试记录列表
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return MBTI测试记录
     */
    @Override
    public List<MbtiTestRecord> selectMbtiTestRecordList(MbtiTestRecord mbtiTestRecord)
    {
        return mbtiTestRecordMapper.selectMbtiTestRecordList(mbtiTestRecord);
    }

    /**
     * 新增MBTI测试记录
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return 结果
     */
    @Override
    public int insertMbtiTestRecord(MbtiTestRecord mbtiTestRecord)
    {
        mbtiTestRecord.setCreateTime(DateUtils.getNowDate());
        return mbtiTestRecordMapper.insertMbtiTestRecord(mbtiTestRecord);
    }

    /**
     * 修改MBTI测试记录
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return 结果
     */
    @Override
    public int updateMbtiTestRecord(MbtiTestRecord mbtiTestRecord)
    {
        mbtiTestRecord.setUpdateTime(DateUtils.getNowDate());
        return mbtiTestRecordMapper.updateMbtiTestRecord(mbtiTestRecord);
    }

    /**
     * 批量删除MBTI测试记录
     *
     * @param recordIds 需要删除的MBTI测试记录主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestRecordByRecordIds(Long[] recordIds)
    {
        return mbtiTestRecordMapper.deleteMbtiTestRecordByRecordIds(recordIds);
    }

    /**
     * 删除MBTI测试记录信息
     *
     * @param recordId MBTI测试记录主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestRecordByRecordId(Long recordId)
    {
        return mbtiTestRecordMapper.deleteMbtiTestRecordByRecordId(recordId);
    }

    /**
     * 处理测试结果，从Redis获取数据，计算MBTI类型和匹配度，保存到数据库
     *
     * @param roomCode 房间编号
     * @param userId 用户ID
     * @return 测试结果
     */
    @Override
    public Map<String, Object> processTestResult(String roomCode, String userId) {
        try {
            log.info("处理测试结果 - 房间: {}, 用户: {}", roomCode, userId);

            // 从Redis获取房间信息
            String roomKey = RedisKeyUtil.getRoomKey(roomCode);
            Map<String, String> roomInfo = redisCache.getCacheMap(roomKey);
            if (roomInfo == null || roomInfo.isEmpty()) {
                return Map.of("success", false, "message", "房间不存在");
            }

            // 从Redis获取用户答题数据
            String memberKey = RedisKeyUtil.getRoomMemberKey(roomCode, userId);
            String answersJson = redisCache.getCacheMapValue(memberKey, "answers");
            if (answersJson == null || answersJson.isEmpty()) {
                return Map.of("success", false, "message", "未找到答题数据");
            }

            // 解析答题数据
            Map<String, String> answers = JSON.parseObject(answersJson, Map.class);

            // 计算MBTI结果
            String testType = "1"; // 默认单人测试
            if ("2".equals(roomInfo.get("type"))) {
                testType = "2"; // 双人测试
            }
            Map<String, String> member = redisCache.getCacheMap(memberKey);
            // 确定MBTI类型
            String mbtiResult = member.get("mbti");
            String myEiScore = member.get("eiScore");
            String mySnScore = member.get("snScore");
            String myTfScore = member.get("tfScore");
            String myJpScore = member.get("jpScore");

            // 查询MBTI类型详细信息
            MbtiPersonalityType personalityType = new MbtiPersonalityType();
            personalityType.setTypeCode(mbtiResult);
            List<MbtiPersonalityType> types = mbtiPersonalityTypeMapper.selectMbtiPersonalityTypeList(personalityType);
            MbtiPersonalityType mbtiType = null;
            if (types != null && !types.isEmpty()) {
                mbtiType = types.get(0);
            }

            // 获取房间信息
            Long roomId = null;
            MbtiRoom room = null;
            Date startTime = null;
            if (roomInfo.containsKey("roomId")) {
                roomId = Long.valueOf(String.valueOf(roomInfo.get("roomId")));
                room = mbtiRoomMapper.selectMbtiRoomByRoomId(roomId);
                if (room != null) {
                    startTime = room.getCreateTime();
                }
            }

            // 创建测试记录
            MbtiTestRecord record = new MbtiTestRecord();
            record.setUserId(Long.valueOf(userId));
            record.setTestType(testType);
            record.setVersionId(Long.valueOf(roomInfo.getOrDefault("versionId", "1")));
            record.setRoomId(Long.valueOf(room.getRoomCode()));
            record.setStartTime(startTime != null ? startTime : DateUtils.getNowDate());
            record.setEndTime(DateUtils.getNowDate());
            if (startTime != null) {
                long duration = (record.getEndTime().getTime() - startTime.getTime()) / 1000;
                record.setDuration(duration);
            }
            record.setMbtiResult(mbtiResult);
            record.seteIScore(Long.valueOf(myEiScore));
            record.setsNScore(Long.valueOf(mySnScore));
            record.settFScore(Long.valueOf(myTfScore));
            record.setjPScore(Long.valueOf(myJpScore));
            record.setTotalQuestions((long) answers.size());
            record.setStatus("1"); // 已完成
            record.setCreateTime(new Date());
            String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
            Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
            JSONObject partnerInfo = new JSONObject();
            for (String memberId : memberIds) {
                if(!memberId.equals(userId)){
                    record.setPartnerId(memberId);
                    String partnerKey = RedisKeyUtil.getRoomMemberKey(roomCode, memberId);
                    partnerInfo = JSONUtil.parseObj(redisCache.getCacheMap(partnerKey));
                    String nickName = redisCache.getCacheMapValue(partnerKey, "nickName");
                    record.setPartnerName(nickName);
                    record.setPartnerMbtiResult(redisCache.getCacheMapValue(partnerKey, "mbti"));
                    String partnerEiScore = redisCache.getCacheMapValue(partnerKey, "eiScore");
                    String partnerSnScore = redisCache.getCacheMapValue(partnerKey, "snScore");
                    String partnerTfScore = redisCache.getCacheMapValue(partnerKey, "tfScore");
                    String partnerJpScore = redisCache.getCacheMapValue(partnerKey, "jpScore");
                    MBTIMatchCalculator.MBTIScore myScore = new MBTIMatchCalculator.MBTIScore(Integer.valueOf(myEiScore),Integer.valueOf(mySnScore),Integer.valueOf(myTfScore),Integer.valueOf(myJpScore));
                    MBTIMatchCalculator.MBTIScore partnerScore = new MBTIMatchCalculator.MBTIScore(Integer.valueOf(partnerEiScore), Integer.valueOf(partnerSnScore), Integer.valueOf(partnerTfScore), Integer.valueOf(partnerJpScore));
                    int match = MBTIMatchCalculator.calculateMatch(myScore, partnerScore);
                    log.info("匹配度：" + match + "%");
                    record.setCompatibilityScore((long) match);
                    String partnerAnswersJson = redisCache.getCacheMapValue(partnerKey, "answers");
                    // 解析伙伴答题数据
                    Map<String, String> partnerAnswers = JSON.parseObject(partnerAnswersJson, Map.class);

                    // 比较答案相同数量
                   int sameAnswerCount = (int) answers.entrySet().stream()
                            .filter(e -> e.getValue().equals(partnerAnswers.get(e.getKey())))
                            .count();
                    record.setIdenticalAnswers((long) sameAnswerCount);
                    // 新增默契度计算
                    int total = answers.size();
                    if(total > 0) {
                        int chemistry = (sameAnswerCount * 100) / total;
                        record.setChemistryScore((long) chemistry);
                        log.info("默契度：{}% ({}题相同/共{}题)", chemistry, sameAnswerCount, total);
                    }
                }
            }
            MbtiTestRecord mbtiTestRecordParam = new MbtiTestRecord();
            mbtiTestRecordParam.setRoomId(record.getRoomId());
            mbtiTestRecordParam.setUserId(Long.valueOf(userId));
            List<MbtiTestRecord> records = mbtiTestRecordMapper.selectMbtiTestRecordList(mbtiTestRecordParam);
            if (records != null && !records.isEmpty()) {
            }else{
                JSONObject roomInfoJson = JSONUtil.parseObj(roomInfo);

                JSONObject memberJson = JSONUtil.parseObj(member);
                JSONObject testDetail = new JSONObject();
                testDetail.put("roomInfo", roomInfoJson);
                testDetail.put("myself", memberJson);
                testDetail.put("partner", partnerInfo);
                record.setRemark(testDetail.toString());
                // 查询匹配度信息
                MbtiCompatibility compatibility = getCompatibility(record.getMbtiResult(), record.getPartnerMbtiResult());
                record.setRecommendations(compatibility.getRecommendations());
                record.setAdvantages(compatibility.getAdvantages());
                record.setChallenges(compatibility.getChallenges());
                record.setRelationshipAnalysis(compatibility.getRelationshipAnalysis());
                // 保存到数据库
                mbtiTestRecordMapper.insertMbtiTestRecord(record);
            }
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("mbti", mbtiResult);
            result.put("recordId", record.getRecordId());
            if (mbtiType != null) {
                result.put("typeName", mbtiType.getTypeName());
                result.put("description", mbtiType.getDescription());
                result.put("characteristics", mbtiType.getCharacteristics());
            }
            return result;
        } catch (Exception e) {
            log.error("处理测试结果异常", e);
            return Map.of("success", false, "message", "处理测试结果异常: " + e.getMessage());
        }
    }

    /**
     * 获取双人测试结果
     *
     * @param roomCode 房间编号
     * @return 双人测试结果
     */
    @Override
    public Map<String, Object> getDualTestResult(String roomCode) {
        try {
            log.info("获取双人测试结果 - 房间: {}", roomCode);

            // 从Redis获取房间信息
            String roomKey = RedisKeyUtil.getRoomKey(roomCode);
            Map<String, String> roomInfo = redisCache.getCacheMap(roomKey);
            if (roomInfo == null || roomInfo.isEmpty()) {
                return Map.of("success", false, "message", "房间不存在");
            }

            // 获取房间成员
            String roomMembersKey = RedisKeyUtil.getRoomMembersKey(roomCode);
            Set<String> memberIds = redisCache.getCacheSet(roomMembersKey);
            if (memberIds.size() != 2) {
                return Map.of("success", false, "message", "房间成员数量不正确");
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);

            String[] members = memberIds.toArray(new String[0]);
            String member1 = members[0];
            String member2 = members[1];

            // 获取成员1的测试结果
            String member1Key = RedisKeyUtil.getRoomMemberKey(roomCode, member1);
            Map<String, String> member1Info = redisCache.getCacheMap(member1Key);

            // 获取成员2的测试结果
            String member2Key = RedisKeyUtil.getRoomMemberKey(roomCode, member2);
            Map<String, String> member2Info = redisCache.getCacheMap(member2Key);

            // 确保双方都有MBTI结果
            if (!member1Info.containsKey("mbti") || !member2Info.containsKey("mbti")) {
                return Map.of("success", false, "message", "某一方还未完成测试");
            }

            String mbti1 = member1Info.get("mbti");
            String mbti2 = member2Info.get("mbti");

            // 获取猜测结果
            String guessedMbti1 = member1Info.getOrDefault("guessedMbti", "");
            String guessedMbti2 = member2Info.getOrDefault("guessedMbti", "");

            // 查询MBTI类型详细信息
            MbtiPersonalityType type1 = getPersonalityType(mbti1);
            MbtiPersonalityType type2 = getPersonalityType(mbti2);

            // 查询匹配度信息
            MbtiCompatibility compatibility = getCompatibility(mbti1, mbti2);

            // 构建用户1信息
            Map<String, Object> user1Info = new HashMap<>();
            user1Info.put("userId", member1);
            user1Info.put("userName", member1Info.getOrDefault("userName", member1));
            user1Info.put("mbti", mbti1);
            user1Info.put("guessedMbti", guessedMbti1);
            user1Info.put("eiScore", member1Info.get("eiScore"));
            user1Info.put("snScore", member1Info.get("snScore"));
            user1Info.put("jpScore", member1Info.get("jpScore"));
            user1Info.put("tfScore", member1Info.get("tfScore"));
            if (type1 != null) {
                user1Info.put("typeName", type1.getTypeName());
                user1Info.put("description", type1.getDescription());
                user1Info.put("characteristics", type1.getCharacteristics());
            }

            // 构建用户2信息
            Map<String, Object> user2Info = new HashMap<>();
            user2Info.put("userId", member2);
            user2Info.put("userName", member2Info.getOrDefault("userName", member2));
            user2Info.put("mbti", mbti2);
            user2Info.put("guessedMbti", guessedMbti2);
            user2Info.put("eiScore", member2Info.get("eiScore"));
            user2Info.put("snScore", member2Info.get("snScore"));
            user2Info.put("jpScore", member2Info.get("jpScore"));
            user2Info.put("tfScore", member2Info.get("tfScore"));
            if (type2 != null) {
                user2Info.put("typeName", type2.getTypeName());
                user2Info.put("description", type2.getDescription());
                user2Info.put("characteristics", type2.getCharacteristics());
            }

            // 构建匹配度信息
            Map<String, Object> compatibilityInfo = new HashMap<>();
            if (compatibility != null) {
                compatibilityInfo.put("score", compatibility.getCompatibilityScore());
                compatibilityInfo.put("level", compatibility.getCompatibilityScore());
                compatibilityInfo.put("advantages", compatibility.getAdvantages());
                compatibilityInfo.put("challenges", compatibility.getChallenges());
                compatibilityInfo.put("suggestions", compatibility.getRecommendations());
            } else {
                compatibilityInfo.put("advantages", mbti1 + "和" + mbti2 + "的组合可以互相补充，一个人的优势可以弥补另一个人的不足。");
                compatibilityInfo.put("challenges", mbti1 + "和" + mbti2 + "在沟通方式和决策过程中可能存在差异，需要相互理解和包容。");
                compatibilityInfo.put("suggestions",
                    "尊重彼此的差异，理解不同性格类型的思考和行为方式;建立有效的沟通机制，避免误解和冲突;发挥各自的优势，在团队合作中互相补充 ;给予对方足够的空间和自由，尊重个人需求"
                );
            }

            // 添加到结果中
            result.put("player1", user1Info);
            result.put("player2", user2Info);
            result.put("compatibility", compatibilityInfo);

            // 将结果缓存到Redis
            String resultKey = RedisKeyUtil.getRoomResultKey(roomCode);
            redisCache.setCacheObject(resultKey, JSON.toJSONString(result), 24, TimeUnit.HOURS);

            return result;
        } catch (Exception e) {
            log.error("获取双人测试结果异常", e);
            return Map.of("success", false, "message", "获取测试结果异常: " + e.getMessage());
        }
    }

    @Override
    public List<MbtiTestRecordVo> selectMbtiTestRecordVoList(MbtiTestRecord mbtiTestRecord) {
        return mbtiTestRecordMapper.selectMbtiTestRecordVoList(mbtiTestRecord);
    }

    @Override
    public List<Map> getCompatibilityRanks(Long userId) {
        return mbtiTestRecordMapper.getCompatibilityRanks(userId);
    }

    @Override
    public List<Map> getChemistryRanks(Long userId) {
        return mbtiTestRecordMapper.getChemistryRanks(userId);
    }

    /**
     * 获取性格类型信息
     */
    private MbtiPersonalityType getPersonalityType(String mbtiCode) {
        MbtiPersonalityType query = new MbtiPersonalityType();
        query.setTypeCode(mbtiCode);
        List<MbtiPersonalityType> types = mbtiPersonalityTypeMapper.selectMbtiPersonalityTypeList(query);
        return types != null && !types.isEmpty() ? types.get(0) : null;
    }

    /**
     * 获取匹配度信息
     */
    private MbtiCompatibility getCompatibility(String mbti1, String mbti2) {
        MbtiCompatibility query = new MbtiCompatibility();
        query.setTypeCode1(mbti1);
        query.setTypeCode2(mbti2);
        List<MbtiCompatibility> list = mbtiCompatibilityMapper.selectMbtiCompatibilityList(query);

        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }

        // 尝试反向查询
        query.setTypeCode1(mbti2);
        query.setTypeCode2(mbti1);
        list = mbtiCompatibilityMapper.selectMbtiCompatibilityList(query);

        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 保存单人测试结果
     *
     * @param testData 测试数据，包含用户ID、测试版本、答案等信息
     * @return 测试结果
     */
    @Override
    public Map<String, Object> saveTestResult(Map<String, Object> testData) {
        try {
            log.info("保存单人测试结果 - 数据: {}", testData);

            // 解析传入的数据
            Long userId = Long.parseLong(testData.get("userId").toString());
            String version = (String) testData.get("version");
            String mbtiResult = (String) testData.get("mbti");
            List answers = (List) testData.get("answers");
            Long eiScore = Long.parseLong(testData.get("eiScore").toString());
            Long snScore = Long.parseLong(testData.get("snScore").toString());
            Long tfScore = Long.parseLong(testData.get("tfScore").toString());
            Long jpScore = Long.parseLong(testData.get("jpScore").toString());

            // 创建测试记录
            MbtiTestRecord record = new MbtiTestRecord();
            record.setUserId(userId);
            record.setTestType("1"); // 单人测试

            // 获取测试版本ID
            MbtiTestVersion mbtiTestVersionParam = new MbtiTestVersion();
            mbtiTestVersionParam.setVersionCode(version);
            List<MbtiTestVersion> mbtiTestVersions = mbtiTestVersionMapper.selectMbtiTestVersionList(mbtiTestVersionParam);
            MbtiTestVersion mbtiTestVersion = new MbtiTestVersion();
            if(mbtiTestVersions != null && mbtiTestVersions.size() > 0){
                mbtiTestVersion = mbtiTestVersions.get(0);
            }
            if(mbtiTestVersion != null){
                record.setVersionId(mbtiTestVersion.getVersionId());
            }
            // 设置测试时间和结果
            record.setStartTime(new Date());
            record.setEndTime(new Date());
            record.setDuration(0L); // 单人测试不记录时长
            record.setMbtiResult(mbtiResult);
            record.seteIScore(eiScore);
            record.setsNScore(snScore);
            record.settFScore(tfScore);
            record.setjPScore(jpScore);
            record.setTotalQuestions((long) answers.size());
            record.setStatus("1"); // 已完成
            record.setCreateTime(new Date());

            // 查询MBTI类型详细信息
            MbtiPersonalityType personalityType = new MbtiPersonalityType();
            personalityType.setTypeCode(mbtiResult);
            List<MbtiPersonalityType> types = mbtiPersonalityTypeMapper.selectMbtiPersonalityTypeList(personalityType);
            MbtiPersonalityType mbtiType = null;
            if (types != null && !types.isEmpty()) {
                mbtiType = types.get(0);
            }

            // 保存测试记录
            mbtiTestRecordMapper.insertMbtiTestRecord(record);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("mbti", mbtiResult);
            result.put("recordId", record.getRecordId());
            if (mbtiType != null) {
                result.put("typeName", mbtiType.getTypeName());
                result.put("description", mbtiType.getDescription());
                result.put("characteristics", mbtiType.getCharacteristics());
            }

            return result;
        } catch (Exception e) {
            log.error("保存测试结果异常", e);
            return Map.of("success", false, "message", "保存测试结果异常: " + e.getMessage());
        }
    }
}
