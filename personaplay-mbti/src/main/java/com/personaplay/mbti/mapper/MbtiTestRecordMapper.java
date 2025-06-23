package com.personaplay.mbti.mapper;

import java.util.List;
import java.util.Map;

import com.personaplay.mbti.domain.MbtiTestRecord;
import com.personaplay.mbti.domain.vo.MbtiTestRecordVo;
import org.apache.ibatis.annotations.Param;

/**
 * MBTI测试记录Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiTestRecordMapper
{
    /**
     * 查询MBTI测试记录
     *
     * @param recordId MBTI测试记录主键
     * @return MBTI测试记录
     */
    public MbtiTestRecord selectMbtiTestRecordByRecordId(Long recordId);

    /**
     * 查询MBTI测试记录列表
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return MBTI测试记录集合
     */
    public List<MbtiTestRecord> selectMbtiTestRecordList(MbtiTestRecord mbtiTestRecord);

    /**
     * 新增MBTI测试记录
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return 结果
     */
    public int insertMbtiTestRecord(MbtiTestRecord mbtiTestRecord);

    /**
     * 修改MBTI测试记录
     *
     * @param mbtiTestRecord MBTI测试记录
     * @return 结果
     */
    public int updateMbtiTestRecord(MbtiTestRecord mbtiTestRecord);

    /**
     * 删除MBTI测试记录
     *
     * @param recordId MBTI测试记录主键
     * @return 结果
     */
    public int deleteMbtiTestRecordByRecordId(Long recordId);

    /**
     * 批量删除MBTI测试记录
     *
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiTestRecordByRecordIds(Long[] recordIds);

    public List<MbtiTestRecordVo> selectMbtiTestRecordVoList(MbtiTestRecord mbtiTestRecord);

    /**
     * 获取用户的兼容性排名
     * @param userId 用户ID
     * @return 兼容性排名列表
     */
    List<Map> getCompatibilityRanks(@Param("userId") Long userId);

    List<Map> getChemistryRanks(@Param("userId") Long userId);
}
