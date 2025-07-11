package com.personaplay.mbti.mapper;

import java.util.List;
import com.personaplay.mbti.domain.MbtiRoomMember;

/**
 * 房间成员Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiRoomMemberMapper
{
    /**
     * 查询房间成员
     *
     * @param memberId 房间成员主键
     * @return 房间成员
     */
    public MbtiRoomMember selectMbtiRoomMemberByMemberId(Long memberId);

    /**
     * 查询房间成员列表
     *
     * @param mbtiRoomMember 房间成员
     * @return 房间成员集合
     */
    public List<MbtiRoomMember> selectMbtiRoomMemberList(MbtiRoomMember mbtiRoomMember);

    /**
     * 新增房间成员
     *
     * @param mbtiRoomMember 房间成员
     * @return 结果
     */
    public int insertMbtiRoomMember(MbtiRoomMember mbtiRoomMember);

    /**
     * 修改房间成员
     *
     * @param mbtiRoomMember 房间成员
     * @return 结果
     */
    public int updateMbtiRoomMember(MbtiRoomMember mbtiRoomMember);

    /**
     * 删除房间成员
     *
     * @param memberId 房间成员主键
     * @return 结果
     */
    public int deleteMbtiRoomMemberByMemberId(Long memberId);

    /**
     * 批量删除房间成员
     *
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiRoomMemberByMemberIds(Long[] memberIds);
}
