package com.personaplay.mbti.service.impl;

import java.util.List;
import com.personaplay.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.personaplay.mbti.mapper.MbtiUserProfileMapper;
import com.personaplay.mbti.domain.MbtiUserProfile;
import com.personaplay.mbti.service.IMbtiUserProfileService;

/**
 * 用户MBTI档案Service业务层处理
 *
 * @author fangrx
 * @date 2025-03-24
 */
@Service
public class MbtiUserProfileServiceImpl implements IMbtiUserProfileService
{
    @Autowired
    private MbtiUserProfileMapper mbtiUserProfileMapper;

    /**
     * 查询用户MBTI档案
     *
     * @param profileId 用户MBTI档案主键
     * @return 用户MBTI档案
     */
    @Override
    public MbtiUserProfile selectMbtiUserProfileByProfileId(Long profileId)
    {
        return mbtiUserProfileMapper.selectMbtiUserProfileByProfileId(profileId);
    }

    /**
     * 查询用户MBTI档案列表
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 用户MBTI档案
     */
    @Override
    public List<MbtiUserProfile> selectMbtiUserProfileList(MbtiUserProfile mbtiUserProfile)
    {
        return mbtiUserProfileMapper.selectMbtiUserProfileList(mbtiUserProfile);
    }

    /**
     * 新增用户MBTI档案
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 结果
     */
    @Override
    public int insertMbtiUserProfile(MbtiUserProfile mbtiUserProfile)
    {
        mbtiUserProfile.setCreateTime(DateUtils.getNowDate());
        return mbtiUserProfileMapper.insertMbtiUserProfile(mbtiUserProfile);
    }

    /**
     * 修改用户MBTI档案
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 结果
     */
    @Override
    public int updateMbtiUserProfile(MbtiUserProfile mbtiUserProfile)
    {
        mbtiUserProfile.setUpdateTime(DateUtils.getNowDate());
        return mbtiUserProfileMapper.updateMbtiUserProfile(mbtiUserProfile);
    }

    /**
     * 批量删除用户MBTI档案
     *
     * @param profileIds 需要删除的用户MBTI档案主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserProfileByProfileIds(Long[] profileIds)
    {
        return mbtiUserProfileMapper.deleteMbtiUserProfileByProfileIds(profileIds);
    }

    /**
     * 删除用户MBTI档案信息
     *
     * @param profileId 用户MBTI档案主键
     * @return 结果
     */
    @Override
    public int deleteMbtiUserProfileByProfileId(Long profileId)
    {
        return mbtiUserProfileMapper.deleteMbtiUserProfileByProfileId(profileId);
    }
}
