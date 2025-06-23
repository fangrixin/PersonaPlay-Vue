package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiRoomMemberMapper;
import com.personaplay.mbti.domain.MbtiRoomMember;
import com.personaplay.mbti.service.IMbtiRoomMemberService;

/**
 * 房间成员Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiRoomMemberServiceImpl implements IMbtiRoomMemberService
{
    @Autowired
    private MbtiRoomMemberMapper mbtiRoomMemberMapper;

    /**
     * 查询房间成员
     *
     * @param memberId 房间成员主键
     * @return 房间成员
     */
    @Override
    public MbtiRoomMember selectMbtiRoomMemberByMemberId(Long memberId)
    {
        return mbtiRoomMemberMapper.selectMbtiRoomMemberByMemberId(memberId);
    }

    /**
     * 查询房间成员列表
     *
     * @param mbtiRoomMember 房间成员
     * @return 房间成员
     */
    @Override
    public List<MbtiRoomMember> selectMbtiRoomMemberList(MbtiRoomMember mbtiRoomMember)
    {
        return mbtiRoomMemberMapper.selectMbtiRoomMemberList(mbtiRoomMember);
    }

    /**
     * 新增房间成员
     *
     * @param mbtiRoomMember 房间成员
     * @return 结果
     */
    @Override
    public int insertMbtiRoomMember(MbtiRoomMember mbtiRoomMember)
    {
        mbtiRoomMember.setCreateTime(DateUtils.getNowDate());
        return mbtiRoomMemberMapper.insertMbtiRoomMember(mbtiRoomMember);
    }

    /**
     * 修改房间成员
     *
     * @param mbtiRoomMember 房间成员
     * @return 结果
     */
    @Override
    public int updateMbtiRoomMember(MbtiRoomMember mbtiRoomMember)
    {
        mbtiRoomMember.setUpdateTime(DateUtils.getNowDate());
        return mbtiRoomMemberMapper.updateMbtiRoomMember(mbtiRoomMember);
    }

    /**
     * 批量删除房间成员
     *
     * @param memberIds 需要删除的房间成员主键
     * @return 结果
     */
    @Override
    public int deleteMbtiRoomMemberByMemberIds(Long[] memberIds)
    {
        return mbtiRoomMemberMapper.deleteMbtiRoomMemberByMemberIds(memberIds);
    }

    /**
     * 删除房间成员信息
     *
     * @param memberId 房间成员主键
     * @return 结果
     */
    @Override
    public int deleteMbtiRoomMemberByMemberId(Long memberId)
    {
        return mbtiRoomMemberMapper.deleteMbtiRoomMemberByMemberId(memberId);
    }
}
