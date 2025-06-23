package com.personaplay.mbti.service;

import java.util.List;
import com.personaplay.mbti.domain.MbtiUserRelationship;

/**
 * 用户关系Service接口
 *
 * @author fangrx
 * @date 2025-03-24
 */
public interface IMbtiUserRelationshipService
{
    /**
     * 查询用户关系
     *
     * @param relationshipId 用户关系主键
     * @return 用户关系
     */
    public MbtiUserRelationship selectMbtiUserRelationshipByRelationshipId(Long relationshipId);

    /**
     * 查询用户关系列表
     *
     * @param mbtiUserRelationship 用户关系
     * @return 用户关系集合
     */
    public List<MbtiUserRelationship> selectMbtiUserRelationshipList(MbtiUserRelationship mbtiUserRelationship);

    /**
     * 新增用户关系
     *
     * @param mbtiUserRelationship 用户关系
     * @return 结果
     */
    public int insertMbtiUserRelationship(MbtiUserRelationship mbtiUserRelationship);

    /**
     * 修改用户关系
     *
     * @param mbtiUserRelationship 用户关系
     * @return 结果
     */
    public int updateMbtiUserRelationship(MbtiUserRelationship mbtiUserRelationship);

    /**
     * 批量删除用户关系
     *
     * @param relationshipIds 需要删除的用户关系主键集合
     * @return 结果
     */
    public int deleteMbtiUserRelationshipByRelationshipIds(Long[] relationshipIds);

    /**
     * 删除用户关系信息
     *
     * @param relationshipId 用户关系主键
     * @return 结果
     */
    public int deleteMbtiUserRelationshipByRelationshipId(Long relationshipId);
}
