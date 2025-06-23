package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiUserProfile;

/**
 * 用户MBTI档案Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiUserProfileService
{
    /**
     * 查询用户MBTI档案
     *
     * @param profileId 用户MBTI档案主键
     * @return 用户MBTI档案
     */
    public MbtiUserProfile selectMbtiUserProfileByProfileId(Long profileId);

    /**
     * 查询用户MBTI档案列表
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 用户MBTI档案集合
     */
    public List<MbtiUserProfile> selectMbtiUserProfileList(MbtiUserProfile mbtiUserProfile);

    /**
     * 新增用户MBTI档案
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 结果
     */
    public int insertMbtiUserProfile(MbtiUserProfile mbtiUserProfile);

    /**
     * 修改用户MBTI档案
     *
     * @param mbtiUserProfile 用户MBTI档案
     * @return 结果
     */
    public int updateMbtiUserProfile(MbtiUserProfile mbtiUserProfile);

    /**
     * 批量删除用户MBTI档案
     *
     * @param profileIds 需要删除的用户MBTI档案主键集合
     * @return 结果
     */
    public int deleteMbtiUserProfileByProfileIds(Long[] profileIds);

    /**
     * 删除用户MBTI档案信息
     *
     * @param profileId 用户MBTI档案主键
     * @return 结果
     */
    public int deleteMbtiUserProfileByProfileId(Long profileId);
}
