package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiTestVersionMapper;
import com.personaplay.mbti.domain.MbtiTestVersion;
import com.personaplay.mbti.service.IMbtiTestVersionService;

/**
 * MBTI测试版本Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiTestVersionServiceImpl implements IMbtiTestVersionService
{
    @Autowired
    private MbtiTestVersionMapper mbtiTestVersionMapper;

    /**
     * 查询MBTI测试版本
     *
     * @param versionId MBTI测试版本主键
     * @return MBTI测试版本
     */
    @Override
    public MbtiTestVersion selectMbtiTestVersionByVersionId(Long versionId)
    {
        return mbtiTestVersionMapper.selectMbtiTestVersionByVersionId(versionId);
    }

    /**
     * 查询MBTI测试版本列表
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return MBTI测试版本
     */
    @Override
    public List<MbtiTestVersion> selectMbtiTestVersionList(MbtiTestVersion mbtiTestVersion)
    {
        return mbtiTestVersionMapper.selectMbtiTestVersionList(mbtiTestVersion);
    }

    /**
     * 新增MBTI测试版本
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return 结果
     */
    @Override
    public int insertMbtiTestVersion(MbtiTestVersion mbtiTestVersion)
    {
        mbtiTestVersion.setCreateTime(DateUtils.getNowDate());
        return mbtiTestVersionMapper.insertMbtiTestVersion(mbtiTestVersion);
    }

    /**
     * 修改MBTI测试版本
     *
     * @param mbtiTestVersion MBTI测试版本
     * @return 结果
     */
    @Override
    public int updateMbtiTestVersion(MbtiTestVersion mbtiTestVersion)
    {
        mbtiTestVersion.setUpdateTime(DateUtils.getNowDate());
        return mbtiTestVersionMapper.updateMbtiTestVersion(mbtiTestVersion);
    }

    /**
     * 批量删除MBTI测试版本
     *
     * @param versionIds 需要删除的MBTI测试版本主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestVersionByVersionIds(Long[] versionIds)
    {
        return mbtiTestVersionMapper.deleteMbtiTestVersionByVersionIds(versionIds);
    }

    /**
     * 删除MBTI测试版本信息
     *
     * @param versionId MBTI测试版本主键
     * @return 结果
     */
    @Override
    public int deleteMbtiTestVersionByVersionId(Long versionId)
    {
        return mbtiTestVersionMapper.deleteMbtiTestVersionByVersionId(versionId);
    }
}
