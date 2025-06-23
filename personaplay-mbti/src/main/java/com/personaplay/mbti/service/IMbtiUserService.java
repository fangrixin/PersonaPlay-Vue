package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiUser;

/**
 * MBTI用户信息Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiUserService
{
    /**
     * 查询MBTI用户信息
     *
     * @param userId MBTI用户信息主键
     * @return MBTI用户信息
     */
    public MbtiUser selectMbtiUserByUserId(Long userId);

    /**
     * 查询MBTI用户信息列表
     *
     * @param mbtiUser MBTI用户信息
     * @return MBTI用户信息集合
     */
    public List<MbtiUser> selectMbtiUserList(MbtiUser mbtiUser);

    /**
     * 新增MBTI用户信息
     *
     * @param mbtiUser MBTI用户信息
     * @return 结果
     */
    public int insertMbtiUser(MbtiUser mbtiUser);

    /**
     * 修改MBTI用户信息
     *
     * @param mbtiUser MBTI用户信息
     * @return 结果
     */
    public int updateMbtiUser(MbtiUser mbtiUser);

    /**
     * 批量删除MBTI用户信息
     *
     * @param userIds 需要删除的MBTI用户信息主键集合
     * @return 结果
     */
    public int deleteMbtiUserByUserIds(Long[] userIds);

    /**
     * 删除MBTI用户信息信息
     *
     * @param userId MBTI用户信息主键
     * @return 结果
     */
    public int deleteMbtiUserByUserId(Long userId);

    public MbtiUser selectMbtiUserByOpenID(String openid);
}
