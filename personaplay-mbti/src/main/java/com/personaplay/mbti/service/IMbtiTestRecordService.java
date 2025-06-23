package com.personaplay.mbti.service;

import java.util.List;
import java.util.Map;
import com.personaplay.mbti.domain.MbtiTestRecord;
import com.personaplay.mbti.domain.vo.MbtiTestRecordVo;

/**
 * MBTI测试记录Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiTestRecordService
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
     * 批量删除MBTI测试记录
     *
     * @param recordIds 需要删除的MBTI测试记录主键集合
     * @return 结果
     */
    public int deleteMbtiTestRecordByRecordIds(Long[] recordIds);

    /**
     * 删除MBTI测试记录信息
     *
     * @param recordId MBTI测试记录主键
     * @return 结果
     */
    public int deleteMbtiTestRecordByRecordId(Long recordId);

    /**
     * 处理测试结果，从Redis获取数据，计算MBTI类型和匹配度，保存到数据库
     *
     * @param roomCode 房间编号
     * @param userId 用户ID
     * @return 测试结果
     */
    public Map<String, Object> processTestResult(String roomCode, String userId);

    /**
     * 获取双人测试结果
     *
     * @param roomCode 房间编号
     * @return 双人测试结果
     */
    public Map<String, Object> getDualTestResult(String roomCode);

    public List<MbtiTestRecordVo> selectMbtiTestRecordVoList(MbtiTestRecord mbtiTestRecord);

    /**
     * 获取用户的兼容性排名
     * @param userId 用户ID
     * @return 兼容性排名列表
     */
    List<Map> getCompatibilityRanks(Long userId);

    public List<Map> getChemistryRanks(Long userId);

    /**
     * 保存单人测试结果
     *
     * @param testData 测试数据，包含用户ID、测试版本、答案等信息
     * @return 测试结果
     */
    Map<String, Object> saveTestResult(Map<String, Object> testData);
}
