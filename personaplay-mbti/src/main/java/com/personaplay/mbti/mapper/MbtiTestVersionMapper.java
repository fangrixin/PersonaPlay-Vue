package com.personaplay.mbti.mapper;

import java.util.List;
import com.personaplay.mbti.domain.MbtiTestVersion;

/**
 * MBTI测试版本Mapper接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface MbtiTestVersionMapper
{
    /**
     * 查询MBTI测试版本
     *
     * @param versionId MBTI测试版本主键
     * @return MBTI测试版本
     */
    public MbtiTestVersion selectMbtiTestVersionByVersionId(Long versionId);

    /**
     * 查询MBTI测试版本列表
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return MBTI测试版本集合
     */
    public List<MbtiTestVersion> selectMbtiTestVersionList(MbtiTestVersion mbtiTestVersion);

    /**
     * 新增MBTI测试版本
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return 结果
     */
    public int insertMbtiTestVersion(MbtiTestVersion mbtiTestVersion);

    /**
     * 修改MBTI测试版本
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return 结果
     */
    public int updateMbtiTestVersion(MbtiTestVersion mbtiTestVersion);

    /**
     * 删除MBTI测试版本
     *
     * @param versionId MBTI测试版本主键
     * @return 结果
     */
    public int deleteMbtiTestVersionByVersionId(Long versionId);

    /**
     * 批量删除MBTI测试版本
     *
     * @param versionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMbtiTestVersionByVersionIds(Long[] versionIds);
}
