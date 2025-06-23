package com.personaplay.mbti.mapper;

import java.util.List;
import com.personaplay.mbti.domain.MbtiRoom;

/**
 * MBTI测试房间Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiRoomMapper
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
     * 删除MBTI测试房间
     *
     * @param roomId MBTI测试房间主键
     * @return 结果
     */
    public int deleteMbtiRoomByRoomId(Long roomId);

    /**
     * 批量删除MBTI测试房间
     *
     * @param roomIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiRoomByRoomIds(Long[] roomIds);
}
