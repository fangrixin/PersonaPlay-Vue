package com.personaplay.mbti.util;

/**
 * Redis键值工具类
 *
 * @author fangrx
 */
public class RedisKeyUtil {

    /**
     * 房间信息key前缀
     */
    private static final String ROOM_PREFIX = "mbti:room:";

    /**
     * 房间成员列表key前缀
     */
    private static final String ROOM_MEMBERS_PREFIX = "mbti:room:members:";

    /**
     * 房间成员信息key前缀
     */
    private static final String ROOM_MEMBER_PREFIX = "mbti:room:member:";

    /**
     * 房间结果key前缀
     */
    private static final String ROOM_RESULT_PREFIX = "mbti:room:result:";

    /**
     * 获取房间信息key
     *
     * @param roomCode 房间编号
     * @return 房间信息key
     */
    public static String getRoomKey(String roomCode) {
        return ROOM_PREFIX + roomCode;
    }

    /**
     * 获取房间成员列表key
     *
     * @param roomCode 房间编号
     * @return 房间成员列表key
     */
    public static String getRoomMembersKey(String roomCode) {
        return ROOM_MEMBERS_PREFIX + roomCode;
    }

    /**
     * 获取房间成员信息key
     *
     * @param roomCode 房间编号
     * @param userId 用户ID
     * @return 房间成员信息key
     */
    public static String getRoomMemberKey(String roomCode, String userId) {
        return ROOM_MEMBER_PREFIX + roomCode + ":" + userId;
    }

    /**
     * 获取房间结果key
     *
     * @param roomCode 房间编号
     * @return 房间结果key
     */
    public static String getRoomResultKey(String roomCode) {
        return ROOM_RESULT_PREFIX + roomCode;
    }
}
