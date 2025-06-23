package com.personaplay.mbti.service;

import java.util.List;
import java.util.Map;

import com.personaplay.mbti.domain.MbtiRoom;

/**
 * MBTI测试房间Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiRoomService
{
    /**
     * 查询MBTI测试房间
     *
     * @param roomId MBTI测试房间主键
     * @return MBTI测试房间
     */
    public MbtiRoom selectMbtiRoomByRoomId(Long roomId);

    /**
     * 查询MBTI测试房间列表
     *
     * @param mbtiRoom MBTI测试房间
     * @return MBTI测试房间集合
     */
    public List<MbtiRoom> selectMbtiRoomList(MbtiRoom mbtiRoom);

    /**
     * 新增MBTI测试房间
     *
     * @param mbtiRoom MBTI测试房间
     * @return 结果
     */
    public int insertMbtiRoom(MbtiRoom mbtiRoom);

    /**
     * 修改MBTI测试房间
     *
     * @param mbtiRoom MBTI测试房间
     * @return 结果
     */
    public int updateMbtiRoom(MbtiRoom mbtiRoom);

    /**
     * 批量删除MBTI测试房间
     *
     * @param roomIds 需要删除的MBTI测试房间主键集合
     * @return 结果
     */
    public int deleteMbtiRoomByRoomIds(Long[] roomIds);

    /**
     * 删除MBTI测试房间信息
     *
     * @param roomId MBTI测试房间主键
     * @return 结果
     */
    public int deleteMbtiRoomByRoomId(Long roomId);

    public String createRoom(Map<String, Object> roomData);

    public Map<String, Object> getRoomInfo(String roomCode);

    public boolean joinRoom(String roomCode, Map<String, Object> userData);

    public List<Map<String, Object>> getRoomMembers(String roomCode);

    public boolean updateReadyStatus(String roomCode, String userId, boolean isReady);

    public boolean startTest(String roomCode, String userId);

    public boolean updateAnswer(String roomCode, String userId, int questionIndex, String answer);

    public boolean closeRoom(String roomCode, String userId);




}
